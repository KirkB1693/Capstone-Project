package com.example.android.baseballbythenumbers.ui.main;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.android.baseballbythenumbers.AppExecutors;
import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.BuildConfig;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.data.BattingStats;
import com.example.android.baseballbythenumbers.data.Division;
import com.example.android.baseballbythenumbers.data.Game;
import com.example.android.baseballbythenumbers.data.League;
import com.example.android.baseballbythenumbers.data.Organization;
import com.example.android.baseballbythenumbers.data.PitchingStats;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Schedule;
import com.example.android.baseballbythenumbers.data.Team;
import com.example.android.baseballbythenumbers.databinding.ActivityMainBinding;
import com.example.android.baseballbythenumbers.generators.ScheduleGenerator;
import com.example.android.baseballbythenumbers.generators.lineupAndDefense.LineupGenerator;
import com.example.android.baseballbythenumbers.generators.lineupAndDefense.PitchingRotationGenerator;
import com.example.android.baseballbythenumbers.repository.Repository;
import com.example.android.baseballbythenumbers.ui.gameplay.GamePlayActivity;
import com.example.android.baseballbythenumbers.ui.newleaguesetup.NewLeagueSetupActivity;
import com.example.android.baseballbythenumbers.ui.roster.RosterActivity;
import com.example.android.baseballbythenumbers.ui.standings.StandingsActivity;
import com.example.android.baseballbythenumbers.viewModels.MainActivityViewModel;
import com.example.android.baseballbythenumbers.widget.BaseballByTheNumbersAppWidgetProvider;
import com.example.android.baseballbythenumbers.widget.BaseballByTheNumbersWidgetService;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.danlew.android.joda.JodaTimeAndroid;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.Future;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 123;
    public static final String ORG_ID_SHARED_PREF_KEY = "orgId";
    public static final String NEXT_GAME_EXTRA = "Next_Game";
    public static final String HOME_TEAM_EXTRA = "home_team_extra";
    public static final String ORGANIZATION_EXTRA = "organization_extra";
    public static final String VISITING_TEAM_EXTRA = "visiting_team_extra";
    public static final String USER_TEAM_NAME = "user_team_name";
    public static final String USER_TEAM_EXTRA = "user_team_extra";
    public static final String USER_NAME_EXTRA = "user_name_extra";
    private List<Game> gamesForUserToPlay;
    private Organization organization;
    private Repository mRepository;
    private ActivityMainBinding mainBinding;
    private int dayOfSchedule;
    private SharedPreferences sharedPreferences;
    private Game nextGame;
    private Division usersDivision;

    private TreeMap<String, Team> listOfAllTeams;
    private MainActivityViewModel mainActivityViewModel;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Boolean authFlag = false;
    private String mUsername;

    private Boolean setupNewLeague = false;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private boolean mConfigureWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainBinding.mainToolbar.setTitle("");
        setSupportActionBar(mainBinding.mainToolbar);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        JodaTimeAndroid.init(this);

        mRepository = ((BaseballByTheNumbersApp) getApplicationContext()).getRepository();

        mUsername = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent widgetIntent = getIntent();
        Bundle widgetExtras = widgetIntent.getExtras();
        if (widgetExtras != null) {
            mAppWidgetId = widgetExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            setResult(RESULT_CANCELED);
            mConfigureWidget = mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID;
        }

        if (mainActivityViewModel.getOrganization() != null) {
            setupOrganizationAndUI();
        } else {
            dayOfSchedule = 0;
            String orgId = null;

            Intent intent = getIntent();                        // Check intent for an orgId
            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    organization = extras.getParcelable(NewLeagueSetupActivity.ORGANIZATION_EXTRA);
                    if (organization != null) {
                        orgId = organization.getId();
                        mainActivityViewModel.setOrganization(organization);
                    }
                }
            }

            if (orgId == null) {                                // If orgId wasn't found in intent, check shared preferences
                String sharedPrefOrgId = sharedPreferences.getString(ORG_ID_SHARED_PREF_KEY, null);
                if (sharedPrefOrgId != null) {
                    orgId = sharedPrefOrgId;
                } else {
                    mRepository.deleteAll();
                }
            }

            if (orgId != null) {                                 // If we have an orgId move forward
                mainActivityViewModel.loadOrganizationData(orgId);
                waitForRoomReadWriteToComplete();

                if (mainActivityViewModel.isOrganizationLoaded()) {
                    setupOrganizationAndUI();
                } else {
                    Timber.e("Organization not properly loaded");
                }
            } else {
                setupNewLeague = true;
                checkForUserSignIn();
            }
        }


        mainBinding.include.editLineupButton.setOnClickListener(this);
        mainBinding.include.coachSetsLineupButton.setOnClickListener(this);
        mainBinding.include.startGameButton.setOnClickListener(this);
        mainBinding.include.simulateGameButton.setOnClickListener(this);
        mainBinding.include.standingsButton.setOnClickListener(this);
        
        checkForUserSignIn();
    }

    private void checkForUserSignIn() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // user is signed in
                onSignedInInitialize(user.getDisplayName());
                if (setupNewLeague) {
                    goToNewLeagueSetupActivity();
                }
            } else {
                if (!authFlag) {
                    // user is signed out
                    onSignedOutCleanup();
                    // Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                    authFlag = true;
                }
            }
        }
    };

    }

    private void setupOrganizationAndUI() {
        organization = mainActivityViewModel.getOrganization();
        gamesForUserToPlay = mainActivityViewModel.generateListOfGamesForUser();
        nextGame = mainActivityViewModel.findNextUnplayedGame(gamesForUserToPlay);
        listOfAllTeams = mainActivityViewModel.getMapOfAllTeams();
        setUsersDivision();
        enableGamePlayButtons();
        updateUI();
    }

    private void setUsersDivision() {
        List<League> leagues = organization.getLeagues();
        List<Division> divisions = new ArrayList<>();
        for (League league: leagues) {
            divisions.addAll(league.getDivisions());
        }
        for (Division division: divisions) {
            if (division.getDivisionId().equals(mainActivityViewModel.getUsersTeam().getDivisionId())) {
                usersDivision = division;
                break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                // sign out
                AuthUI.getInstance().signOut(this);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
                authFlag = false;
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Signed in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        authFlag = false;
        FirebaseAuth.getInstance().signOut();

    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        mainBinding.mainToolbar.setTitle(getString(R.string.main_screen_title_prefix)+ mUsername);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void waitForRoomReadWriteToComplete() {
        List<Future<?>> futureList = mainActivityViewModel.getFutureList();
        boolean allDone = false;
        while (!allDone) {
            allDone = true;
            for (Future<?> future : futureList) {
                allDone &= future.isDone();
            }
        }
    }


    private void goToNewLeagueSetupActivity() {
        // Go to NewLeagueSetupActivity
        Intent newLeagueIntent = new Intent(this, NewLeagueSetupActivity.class);
        newLeagueIntent.putExtra(USER_NAME_EXTRA, mUsername);
        this.startActivity(newLeagueIntent);
    }

    private void updateUI() {

        if (sharedPreferences.getString(ORG_ID_SHARED_PREF_KEY, null) == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ORG_ID_SHARED_PREF_KEY, organization.getId());
            editor.apply();
        }

        updateWidgets();

        if (nextGame == null) {
            endOfSeason();
        }

        if (mainActivityViewModel.getHomeTeam() != null && mainActivityViewModel.getVisitingTeam() != null) {
            Player nextHomeStarter = PitchingRotationGenerator.getBestStarterAvailableForNextGame(mainActivityViewModel.getHomeTeam());
            Player nextVisitingStarter = PitchingRotationGenerator.getBestStarterAvailableForNextGame(mainActivityViewModel.getVisitingTeam());
            mainBinding.include.mainProgressBar.setVisibility(View.GONE);
            mainBinding.include.homeCityTV.setText(mainActivityViewModel.getHomeTeam().getTeamCity());
            mainBinding.include.homeTeamNameTV.setText(mainActivityViewModel.getHomeTeam().getTeamName());
            String homeWL = formatWL(mainActivityViewModel.getHomeTeam());
            mainBinding.include.homeTeamWLTV.setText(homeWL);
            mainBinding.include.homeStarterTV.setText(nextHomeStarter.getName());
            String homeStarterStats = formatStarterStats(nextHomeStarter.getPitchingStats());
            mainBinding.include.homeStarterStatsTV.setText(homeStarterStats);

            mainBinding.include.visitingTeamNameTV.setText(mainActivityViewModel.getVisitingTeam().getTeamName());
            mainBinding.include.visitingCityTV.setText(mainActivityViewModel.getVisitingTeam().getTeamCity());
            String visitorWL = formatWL(mainActivityViewModel.getVisitingTeam());
            mainBinding.include.visitingTeamWLTV.setText(visitorWL);
            mainBinding.include.visitingStarterTV.setText(nextVisitingStarter.getName());
            String visitingStarterStats = formatStarterStats(nextVisitingStarter.getPitchingStats());
            mainBinding.include.visitingStarterStatsTV.setText(visitingStarterStats);
        } else {
            Timber.e("Error!!! Either home or visiting team was null!");
        }
    }

    private void updateWidgets() {
        // Update widgets
        updateBaseballByTheNumbersWidget(this, usersDivision, mainActivityViewModel.getUsersTeam().getTeamId());
        SharedPreferences settings = getSharedPreferences(BaseballByTheNumbersWidgetService.DIVISION_TO_USE_ACTION,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(BaseballByTheNumbersWidgetService.DIVISION_TO_USE, mainActivityViewModel.getUsersTeam().getDivisionId());
        editor.putString(BaseballByTheNumbersWidgetService.USER_TEAM_ID, mainActivityViewModel.getUsersTeam().getTeamId());
        editor.apply();

        // Configure widget
        if (mConfigureWidget) {
            Intent widgetResult = new Intent();
            widgetResult.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, widgetResult);
            finish();
        }
    }

    private void updateBaseballByTheNumbersWidget(Context context, Division usersDivision, String teamId) {
        Intent intent = new Intent(context, BaseballByTheNumbersAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, BaseballByTheNumbersAppWidgetProvider.class));
        if (ids != null && ids.length > 0) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
            BaseballByTheNumbersAppWidgetProvider.updateAppWidgets(context, appWidgetManager, ids, usersDivision, teamId);
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_teams_lv);
        }
    }

    @NotNull
    private String formatWL(Team team) {
        return String.format(Locale.getDefault(),"%d - %d", team.getWins(), team.getLosses());
    }

    @NotNull
    private String formatStarterStats(List<PitchingStats> pitchingStats) {
        return String.format(Locale.getDefault(),"ERA : %s, WHIP : %s", pitchingStats.get(organization.getCurrentYear()).getERA(), pitchingStats.get(organization.getCurrentYear()).getWHIP());
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editLineupButton:
                startRosterActivity();
                break;
            case R.id.coachSetsLineupButton:
                onCoachSetsLineupButtonPressed();
                break;
            case R.id.startGameButton:
                startGamePlayActivity();
                break;
            case R.id.simulateGameButton:
                onSimulateGameButtonPressed();
                break;
            case R.id.standingsButton:
                onStandingsButtonPressed();
                break;
        }
    }

    private void onStandingsButtonPressed() {
        Intent standingsIntent = new Intent(this, StandingsActivity.class);
        standingsIntent.putExtra(ORGANIZATION_EXTRA, organization);
        this.startActivity(standingsIntent);
    }

    private void startRosterActivity() {
        Intent rosterIntent = new Intent(this, RosterActivity.class);
        rosterIntent.putExtra(USER_TEAM_EXTRA, mainActivityViewModel.getUsersTeam());
        this.startActivity(rosterIntent);
    }

    private void startGamePlayActivity() {
        Intent gamePlayIntent = new Intent(this, GamePlayActivity.class);
        gamePlayIntent.putExtra(NEXT_GAME_EXTRA, nextGame);
        Team homeTeamFromGame = listOfAllTeams.get(nextGame.getHomeTeamName());
        gamePlayIntent.putExtra(HOME_TEAM_EXTRA, homeTeamFromGame);
        Team visitingTeamFromGame = listOfAllTeams.get(nextGame.getVisitingTeamName());
        gamePlayIntent.putExtra(VISITING_TEAM_EXTRA, visitingTeamFromGame);
        gamePlayIntent.putExtra(ORGANIZATION_EXTRA, organization);
        gamePlayIntent.putExtra(USER_TEAM_NAME, mainActivityViewModel.getUsersTeam().getTeamName());
        this.startActivity(gamePlayIntent);
        this.finish();
    }

    private void onCoachSetsLineupButtonPressed() {
        TreeMap<Integer, Player> lineup = LineupGenerator.lineupFromTeam(mainActivityViewModel.getUsersTeam(), mainActivityViewModel.getHomeTeam().isUseDh());
        Toast toast = Toast.makeText(this, "The Coach Filled Out The Lineup.", Toast.LENGTH_SHORT);
        formatToast(toast);
        toast.show();
    }

    private void formatToast(Toast toast) {
        View view = toast.getView();
        view.getBackground().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(this, R.color.secondaryColor), PorterDuff.Mode.SRC_IN));
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(ContextCompat.getColor(this, R.color.secondaryTextColor));
        text.setGravity(Gravity.CENTER);
    }

    private void onSimulateGameButtonPressed() {
        disableGameButtons();
        mainBinding.include.mainProgressBar.setVisibility(View.VISIBLE);
        mainBinding.include.gameScoresCV.setVisibility(View.VISIBLE);
        mainBinding.include.simulateGameButton.setEnabled(false);
        dayOfSchedule = nextGame.getDay();
        mainActivityViewModel.simAllGamesForDay(dayOfSchedule);
        nextGame = mainActivityViewModel.findNextUnplayedGame(gamesForUserToPlay);
        dayOfSchedule++;
        if (nextGame != null) {
            while (dayOfSchedule != nextGame.getDay()) {
                if (dayOfSchedule > nextGame.getDay()) {
                    break;
                }
                mainActivityViewModel.simAllGamesForDay(dayOfSchedule);
                dayOfSchedule++;
            }
            if (mainActivityViewModel.getHomeTeam() == null || mainActivityViewModel.getVisitingTeam() == null) {
                Timber.e("Games didn't load properly, either homeTeamName or visitingTeamName was null!!!");
            }
        } else {
            endOfSeason();
        }
        displayGameResults();
        waitForRoomReadWriteToComplete();
        updateUI();

    }

    private void displayGameResults() {
        final List<Game> gamesToDisplay = mainActivityViewModel.getListOfGamesPlayed();
        final TreeMap<String, Team> teams = listOfAllTeams;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final Game game : gamesToDisplay) {
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            mainBinding.include.gameScoresCV.setVisibility(View.VISIBLE);
                            mainBinding.include.gameScoresHomeCityTV.setText(teams.get(game.getHomeTeamName()).getTeamCity());
                            mainBinding.include.gameScoresHomeTeamNameTV.setText(game.getHomeTeamName());
                            mainBinding.include.gameScoresHomeScoreTV.setText(String.format(Locale.getDefault(),"%d",game.getHomeScore()));
                            mainBinding.include.gameScoresVisitorCityTV.setText(teams.get(game.getVisitingTeamName()).getTeamCity());
                            mainBinding.include.gameScoresVisitorTeamNameTV.setText(game.getVisitingTeamName());
                            mainBinding.include.gameScoresVisitorScoreTV.setText(String.format(Locale.getDefault(),"%d", game.getVisitorScore()));
                         }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mainBinding.include.gameScoresCV.setVisibility(View.GONE);
                        enableGamePlayButtons();
                    }
                });
            }
        }).start();
    }

    private void endOfSeason() {
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(organization);
        organization.setCurrentYear(organization.getCurrentYear() + 1);
        Schedule newSeasonSchedule = scheduleGenerator.generateSchedule(mainBinding.include.mainProgressBar);
        List<Schedule> newScheduleList = organization.getSchedules();
        newScheduleList.add(newSeasonSchedule);
        organization.setSchedules(newScheduleList);

        mRepository.updateOrganization(organization);
        mRepository.insertSchedule(newSeasonSchedule);
        mRepository.insertAllGames(newSeasonSchedule.getGameList());
        addNewStatsForPlayers();
        mainActivityViewModel.setNewListOfAllGamesByDay(newSeasonSchedule.getGameList());
        mainActivityViewModel.generateListOfGamesForUser();
        nextGame = mainActivityViewModel.findNextUnplayedGame(gamesForUserToPlay);
        resetTeamRecords();
        updateUI();
    }

    private void resetTeamRecords() {
        for (TreeMap.Entry<String, Team> entry : listOfAllTeams.entrySet()) {
            Team team = entry.getValue();
            team.setWins(0);
            team.setLosses(0);
            mRepository.updateTeam(team);
        }
    }



    private void addNewStatsForPlayers() {
        List<Player> playerList = getAllPlayers();
        for (Player player : playerList) {
            List<BattingStats> battingStatsList = player.getBattingStats();
            BattingStats newBattingStats = new BattingStats(organization.getCurrentYear(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, player.getPlayerId());
            battingStatsList.add(organization.getCurrentYear(), newBattingStats);
            mRepository.insertBattingStats(newBattingStats);
            List<PitchingStats> pitchingStatsList = player.getPitchingStats();
            PitchingStats newPitchingStats = new PitchingStats(organization.getCurrentYear(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, player.getPlayerId());
            pitchingStatsList.add(organization.getCurrentYear(), newPitchingStats);
            mRepository.insertPitchingStats(newPitchingStats);
            player.setBattingStats(battingStatsList);
            player.setPitchingStats(pitchingStatsList);
        }
    }

    private List<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<>();
        for (TreeMap.Entry<String, Team> entry : listOfAllTeams.entrySet()) {
            Team team = entry.getValue();
            playerList.addAll(team.getPlayers());
        }
        return playerList;
    }


    private void disableGameButtons() {
        mainBinding.include.editLineupButton.setEnabled(false);
        mainBinding.include.simulateGameButton.setEnabled(false);
        mainBinding.include.coachSetsLineupButton.setEnabled(false);
        mainBinding.include.startGameButton.setEnabled(false);
        mainBinding.include.standingsButton.setEnabled(false);
    }


    private void enableGamePlayButtons() {
        mainBinding.include.editLineupButton.setEnabled(true);
        mainBinding.include.simulateGameButton.setEnabled(true);
        mainBinding.include.coachSetsLineupButton.setEnabled(true);
        mainBinding.include.startGameButton.setEnabled(true);
        mainBinding.include.standingsButton.setEnabled(true);
    }


}
