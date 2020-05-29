package com.example.android.baseballbythenumbers.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.baseballbythenumbers.AppExecutors;
import com.example.android.baseballbythenumbers.BaseballByTheNumbersApp;
import com.example.android.baseballbythenumbers.BuildConfig;
import com.example.android.baseballbythenumbers.Data.BattingStats;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.ui.gameplay.GamePlayActivity;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.LineupGenerator;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.PitchingRotationGenerator;
import com.example.android.baseballbythenumbers.Generators.ScheduleGenerator;
import com.example.android.baseballbythenumbers.ui.newleaguesetup.NewLeagueSetupActivity;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.Repository.Repository;
import com.example.android.baseballbythenumbers.ui.roster.RosterActivity;
import com.example.android.baseballbythenumbers.ui.standings.StandingsActivity;
import com.example.android.baseballbythenumbers.ViewModels.MainActivityViewModel;
import com.example.android.baseballbythenumbers.databinding.ActivityMainBinding;

import net.danlew.android.joda.JodaTimeAndroid;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.Future;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ORG_ID_SHARED_PREF_KEY = "orgId";
    public static final String NEXT_GAME_EXTRA = "Next_Game";
    public static final String HOME_TEAM_EXTRA = "home_team_extra";
    public static final String ORGANIZATION_EXTRA = "organization_extra";
    public static final String VISITING_TEAM_EXTRA = "visiting_team_extra";
    public static final String USER_TEAM_NAME = "user_team_name";
    public static final String USER_TEAM_EXTRA = "user_team_extra";
    private List<Game> gamesForUserToPlay;
    private Organization organization;
    private Repository mRepository;
    private ActivityMainBinding mainBinding;
    private int dayOfSchedule;
    private SharedPreferences sharedPreferences;
    private Game nextGame;

    private TreeMap<String, Team> listOfAllTeams;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        JodaTimeAndroid.init(this);

        mRepository = ((BaseballByTheNumbersApp) getApplicationContext()).getRepository();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (mainActivityViewModel.getOrganization() != null) {
            organization = mainActivityViewModel.getOrganization();
            gamesForUserToPlay = mainActivityViewModel.generateListOfGamesForUser();
            nextGame = mainActivityViewModel.findNextUnplayedGame(gamesForUserToPlay);
            listOfAllTeams = mainActivityViewModel.getMapOfAllTeams();
            enableGamePlayButtons();
            updateUI();
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
                    organization = mainActivityViewModel.getOrganization();
                    gamesForUserToPlay = mainActivityViewModel.generateListOfGamesForUser();
                    nextGame = mainActivityViewModel.findNextUnplayedGame(gamesForUserToPlay);
                    listOfAllTeams = mainActivityViewModel.getMapOfAllTeams();
                    enableGamePlayButtons();
                    updateUI();
                } else {
                    Timber.e("Organization not properly loaded");
                }
            } else {
                goToNewLeagueSetupActivity();                   // Otherwise setup a new league
            }
        }


        mainBinding.editLineupButton.setOnClickListener(this);
        mainBinding.coachSetsLineupButton.setOnClickListener(this);
        mainBinding.startGameButton.setOnClickListener(this);
        mainBinding.simulateGameButton.setOnClickListener(this);
        mainBinding.standingsButton.setOnClickListener(this);


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
        this.startActivity(newLeagueIntent);
        this.finish();
    }

    private void updateUI() {

        if (sharedPreferences.getString(ORG_ID_SHARED_PREF_KEY, null) == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ORG_ID_SHARED_PREF_KEY, organization.getId());
            editor.apply();
        }
        if (nextGame == null) {
            endOfSeason();
        }

        if (mainActivityViewModel.getHomeTeam() != null && mainActivityViewModel.getVisitingTeam() != null) {
            Player nextHomeStarter = PitchingRotationGenerator.getBestStarterAvailableForNextGame(mainActivityViewModel.getHomeTeam());
            Player nextVisitingStarter = PitchingRotationGenerator.getBestStarterAvailableForNextGame(mainActivityViewModel.getVisitingTeam());
            mainBinding.mainProgressBar.setVisibility(View.GONE);
            mainBinding.userNameTV.setText(organization.getOrganizationName());
            mainBinding.homeCityTV.setText(mainActivityViewModel.getHomeTeam().getTeamCity());
            mainBinding.homeTeamNameTV.setText(mainActivityViewModel.getHomeTeam().getTeamName());
            String homeWL = formatWL(mainActivityViewModel.getHomeTeam());
            mainBinding.homeTeamWLTV.setText(homeWL);
            mainBinding.homeStarterTV.setText(nextHomeStarter.getName());
            String homeStarterStats = formatStarterStats(nextHomeStarter.getPitchingStats());
            mainBinding.homeStarterStatsTV.setText(homeStarterStats);

            mainBinding.visitingTeamNameTV.setText(mainActivityViewModel.getVisitingTeam().getTeamName());
            mainBinding.visitingCityTV.setText(mainActivityViewModel.getVisitingTeam().getTeamCity());
            String visitorWL = formatWL(mainActivityViewModel.getVisitingTeam());
            mainBinding.visitingTeamWLTV.setText(visitorWL);
            mainBinding.visitingStarterTV.setText(nextVisitingStarter.getName());
            String visitingStarterStats = formatStarterStats(nextVisitingStarter.getPitchingStats());
            mainBinding.visitingStarterStatsTV.setText(visitingStarterStats);
        } else {
            Timber.e("Error!!! Either home or visiting team was null!");
        }
    }

    @NotNull
    private String formatWL(Team team) {
        return team.getWins() + " - " + team.getLosses();
    }

    @NotNull
    private String formatStarterStats(List<PitchingStats> pitchingStats) {
        return "ERA : " + pitchingStats.get(organization.getCurrentYear()).getERA() + ", WHIP : " + pitchingStats.get(organization.getCurrentYear()).getWHIP();
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
        this.finish();
    }

    private void startRosterActivity() {
        Intent rosterIntent = new Intent(this, RosterActivity.class);
        rosterIntent.putExtra(USER_TEAM_EXTRA, mainActivityViewModel.getUsersTeam());
        this.startActivity(rosterIntent);
        this.finish();
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
        view.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.secondaryColor), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(ContextCompat.getColor(this, R.color.secondaryTextColor));
        text.setGravity(Gravity.CENTER);
    }

    private void onSimulateGameButtonPressed() {
        disableGameButtons();
        mainBinding.mainProgressBar.setVisibility(View.VISIBLE);
        mainBinding.gameScoresCV.setVisibility(View.VISIBLE);
        mainBinding.simulateGameButton.setEnabled(false);
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
                            mainBinding.gameScoresCV.setVisibility(View.VISIBLE);
                            mainBinding.gameScoresHomeCityTV.setText(teams.get(game.getHomeTeamName()).getTeamCity());
                            mainBinding.gameScoresHomeTeamNameTV.setText(game.getHomeTeamName());
                            mainBinding.gameScoresHomeScoreTV.setText(String.format(Locale.getDefault(),"%d",game.getHomeScore()));
                            mainBinding.gameScoresVisitorCityTV.setText(teams.get(game.getVisitingTeamName()).getTeamCity());
                            mainBinding.gameScoresVisitorTeamNameTV.setText(game.getVisitingTeamName());
                            mainBinding.gameScoresVisitorScoreTV.setText(String.format(Locale.getDefault(),"%d", game.getVisitorScore()));
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
                        mainBinding.gameScoresCV.setVisibility(View.GONE);
                        enableGamePlayButtons();
                    }
                });
            }
        }).start();
    }

    private void endOfSeason() {
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(organization);
        organization.setCurrentYear(organization.getCurrentYear() + 1);
        Schedule newSeasonSchedule = scheduleGenerator.generateSchedule(mainBinding.mainProgressBar);
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
        for (TreeMap.Entry entry : listOfAllTeams.entrySet()) {
            Team team = (Team) entry.getValue();
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
        for (TreeMap.Entry entry : listOfAllTeams.entrySet()) {
            Team team = (Team) entry.getValue();
            playerList.addAll(team.getPlayers());
        }
        return playerList;
    }


    private void disableGameButtons() {
        mainBinding.editLineupButton.setEnabled(false);
        mainBinding.simulateGameButton.setEnabled(false);
        mainBinding.coachSetsLineupButton.setEnabled(false);
        mainBinding.startGameButton.setEnabled(false);
        mainBinding.standingsButton.setEnabled(false);
    }


    private void enableGamePlayButtons() {
        mainBinding.editLineupButton.setEnabled(true);
        mainBinding.simulateGameButton.setEnabled(true);
        mainBinding.coachSetsLineupButton.setEnabled(true);
        mainBinding.startGameButton.setEnabled(true);
        mainBinding.standingsButton.setEnabled(true);
    }


}
