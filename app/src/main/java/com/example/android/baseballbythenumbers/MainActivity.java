package com.example.android.baseballbythenumbers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.baseballbythenumbers.Data.DataForMainScreen;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.LineupGenerator;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.PitchingRotationGenerator;
import com.example.android.baseballbythenumbers.Repository.Repository;
import com.example.android.baseballbythenumbers.Simulators.GameSimulator;
import com.example.android.baseballbythenumbers.databinding.ActivityMainBinding;

import net.danlew.android.joda.JodaTimeAndroid;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ORG_ID_SHARED_PREF_KEY = "orgId";
    private SpannableStringBuilder displayText;
    private List<Game> gamesForUserToPlay;
    private Organization organization;
    private Repository repository;
    private ActivityMainBinding mainBinding;
    private int dayOfSchedule;
    private SharedPreferences sharedPreferences;
    private Game nextGame;
    private Team usersTeam;
    private Team homeTeam;
    private Team visitingTeam;
    private TreeMap<Integer, Player> lineup;
    private TreeMap<String, Team> listOfAllTeams;
    private boolean teamsAreLoaded;
    private TreeMap<Integer, List<Game>> listOfAllGamesByDay;
    private boolean gamesAreLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        JodaTimeAndroid.init(this);

        repository = new Repository(getApplication());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dayOfSchedule = 0;
        String orgId = null;

        Intent intent = getIntent();                        // Check intent for an orgId
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                organization = extras.getParcelable(NewLeagueSetupActivity.ORGANIZATION_EXTRA);
                orgId = organization.getId();
            }
        }

        if (orgId == null) {                                // If orgId wasn't found in intent, check shared preferences
            String sharedPrefOrgId = sharedPreferences.getString(ORG_ID_SHARED_PREF_KEY, null);
            if (sharedPrefOrgId != null) {
                orgId = sharedPrefOrgId;
            } else {
                repository.deleteAll();
            }
        }

        mainBinding.editLineupButton.setOnClickListener(this);
        mainBinding.coachSetsLineupButton.setOnClickListener(this);
        mainBinding.startGameButton.setOnClickListener(this);
        mainBinding.simulateGameButton.setOnClickListener(this);

        if (orgId != null) {                                 // If we have an orgId move forward
            updateUIFromDb(orgId);
            new loadAllTeamsInBackgroundTask(repository).execute(orgId);
            new loadAllGamesInBackgroundTask(repository).execute(orgId);
        } else {
            goToNewLeagueSetupActivity();                   // Otherwise setup a new league
        }


        // simGame(null);
    }

    private void updateUIFromDb(String orgId) {
        new getDataFromDbAndUpdateUIAsyncTask(repository).execute(orgId);
    }

    private void goToNewLeagueSetupActivity() {
        // Go to NewLeagueSetupActivity
        Intent newLeagueintent = new Intent(this, NewLeagueSetupActivity.class);
        this.startActivity(newLeagueintent);
        finish();
    }

    private void updateUI(Organization orgToDisplay, Team usersTeam, Team homeTeam, Team visitingTeam, Game nextGame, List<Game> gamesForTeamList) {
        organization = orgToDisplay;
        this.usersTeam = usersTeam;
        this.homeTeam = homeTeam;
        this.visitingTeam = visitingTeam;
        this.nextGame = nextGame;
        this.gamesForUserToPlay = gamesForTeamList;

        if (sharedPreferences.getString(ORG_ID_SHARED_PREF_KEY, null) == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ORG_ID_SHARED_PREF_KEY, organization.getId());
            editor.apply();
        }

        Player nextHomeStarter = PitchingRotationGenerator.getBestStarterAvailable(homeTeam);
        Player nextVisitingStarter = PitchingRotationGenerator.getBestStarterAvailable(visitingTeam);
        mainBinding.mainProgressBar.setVisibility(View.GONE);
        mainBinding.userNameTV.setText(organization.getOrganizationName());
        mainBinding.homeCityTV.setText(homeTeam.getTeamCity());
        mainBinding.homeTeamNameTV.setText(homeTeam.getTeamName());
        String homeWL = formatWL(homeTeam);
        mainBinding.homeTeamWLTV.setText(homeWL);
        mainBinding.homeStarterTV.setText(nextHomeStarter.getName());
        String homeStarterStats = formatStarterStats(nextHomeStarter.getPitchingStats());
        mainBinding.homeStarterStatsTV.setText(homeStarterStats);

        mainBinding.visitingTeamNameTV.setText(visitingTeam.getTeamName());
        mainBinding.visitingCityTV.setText(visitingTeam.getTeamCity());
        String visitorWL = formatWL(visitingTeam);
        mainBinding.visitingTeamWLTV.setText(visitorWL);
        mainBinding.visitingStarterTV.setText(nextVisitingStarter.getName());
        String visitingStarterStats = formatStarterStats(nextVisitingStarter.getPitchingStats());
        mainBinding.visitingStarterStatsTV.setText(visitingStarterStats);

    }

    @NotNull
    private String formatWL(Team team) {
        return team.getWins() + " - " + team.getLosses();
    }

    @NotNull
    private String formatStarterStats(List<PitchingStats> pitchingStats) {
        String ps = "ERA : " + String.format(Locale.US, "%.2f", pitchingStats.get(organization.getCurrentYear()).getERA()) + ", WHIP : " +
                String.format(Locale.US, "%.2f", pitchingStats.get(0).getWHIP());
        return ps;
    }


    public void simGame(final Game game, final Team homeTeam, Team visitingTeam) {

        GameSimulator gameSimulator = new GameSimulator(this, game, homeTeam, false, visitingTeam, false);
        int[] result = gameSimulator.simulateGame();

        game.setHomeScore(result[0]);
        game.setVisitorScore(result[1]);
        game.setPlayedGame(true);

        mainBinding.gameScoresTV.post(new Runnable() {
            @Override
            public void run() {
                mainBinding.gameScoresTV.setText(String.format(Locale.US, "%s - %s\n%d    -    %d", game.getHomeTeamName(), game.getVisitingTeamName(), game.getHomeScore(), game.getVisitorScore()));
            }
        });

        repository.updateGame(game);
        repository.insertBoxScore(game.getHomeBoxScore());
        repository.insertAllBattingLines(game.getHomeBoxScore().getBattingLines());
        repository.insertBoxScore(game.getVisitorBoxScore());
        repository.insertAllBattingLines(game.getVisitorBoxScore().getBattingLines());
        repository.updateTeam(homeTeam);
        repository.updateTeam(visitingTeam);
        List<Player> playerList = new ArrayList<>();
        playerList.addAll(homeTeam.getPlayers());
        playerList.addAll(visitingTeam.getPlayers());
        for (Player player : playerList) {
            repository.updatePlayer(player);
            repository.updateBattingStats(player.getBattingStats().get(organization.getCurrentYear()));
            repository.updatePitchingStats(player.getPitchingStats().get(organization.getCurrentYear()));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editLineupButton:
                break;
            case R.id.coachSetsLineupButton:
                onCoachSetsLineupButtonPressed();
                break;
            case R.id.startGameButton:
                break;
            case R.id.simulateGameButton:
                onSimulateGameButtonPressed();
                break;
        }
    }

    private void onCoachSetsLineupButtonPressed() {
        lineup = LineupGenerator.lineupFromTeam(usersTeam, homeTeam.isUseDh());
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
        mainBinding.mainProgressBar.setVisibility(View.VISIBLE);
        mainBinding.gameScoresTV.setVisibility(View.VISIBLE);
        mainBinding.simulateGameButton.setEnabled(false);
        dayOfSchedule = nextGame.getDay();
        simRestOfGamesForDay();
        nextGame = findNextUnplayedGame(gamesForUserToPlay);
        dayOfSchedule++;
        if (nextGame != null) {
            while (dayOfSchedule != nextGame.getDay()) {
                if (dayOfSchedule > nextGame.getDay()) {
                    break;
                }
                simRestOfGamesForDay();
                dayOfSchedule++;
            }
            if (homeTeam == null || visitingTeam == null) {
                new getTeamFromDbAndUpdateUIAsyncTask(repository, organization, nextGame, usersTeam, gamesForUserToPlay).execute();
            }
        }
    }

    private void simRestOfGamesForDay() {
        simRestOfGamesOnMainThread();
        //new simRestOfGamesAsyncTask(repository, dayOfSchedule, listOfAllTeams).execute(organization);
    }

    private void simRestOfGamesOnMainThread() {
        if (teamsAreLoaded && gamesAreLoaded) {
            List<Game> gameList;
            Team homeTeam;
            Team visitingTeam;
            gameList = listOfAllGamesByDay.get(dayOfSchedule);
            if (gameList != null) {
                for (Game game : gameList) {
                    if (!game.isPlayedGame()) {
                        homeTeam = listOfAllTeams.get(game.getHomeTeamName());
                        visitingTeam = listOfAllTeams.get((game.getVisitingTeamName()));
                        simGame(game, homeTeam, visitingTeam);
                    }
                }
            }
            mainBinding.gameScoresTV.setText("");
            mainBinding.gameScoresTV.setVisibility(View.INVISIBLE);
            mainBinding.mainProgressBar.setVisibility(View.GONE);
            mainBinding.simulateGameButton.setEnabled(true);
            updateUI(organization, usersTeam, this.homeTeam, this.visitingTeam, nextGame, gamesForUserToPlay);
        } else {
            new simRestOfGamesAsyncTask(repository, dayOfSchedule, listOfAllTeams).execute(organization);
        }
    }


    private class getDataFromDbAndUpdateUIAsyncTask extends AsyncTask<String, Void, DataForMainScreen> {
        private Repository repository;

        public getDataFromDbAndUpdateUIAsyncTask(Repository repository) {
            this.repository = repository;
        }

        @Override
        protected DataForMainScreen doInBackground(String... orgIds) {
            Organization orgById = repository.getOrganizationById(orgIds[0]);
            List<Schedule> schedules = repository.getSchedulesForOrganization(orgById.getId());
            Team usersTeam = getTeamWithPlayersAndStatsFromName(orgById.getUserTeamName());
            List<Game> gameListForUserTeam = repository.getGamesForTeamInSchedule(orgById.getUserTeamName(), schedules.get(orgById.getCurrentYear()).getScheduleId());
            Game nextGame = findNextUnplayedGame(gameListForUserTeam);
            Team homeTeam = null;
            Team visitingTeam = null;
            if (nextGame != null) {
                if (nextGame.getHomeTeamName().equals(orgById.getUserTeamName())) {
                    homeTeam = usersTeam;
                    visitingTeam = getTeamWithPlayersAndStatsFromName(nextGame.getVisitingTeamName());
                } else {
                    homeTeam = getTeamWithPlayersAndStatsFromName(nextGame.getHomeTeamName());
                    visitingTeam = usersTeam;
                }
            }

            if (usersTeam == null || nextGame == null || homeTeam == null || visitingTeam == null) {
                goToNewLeagueSetupActivity();
                return null;
            }
            return new DataForMainScreen(orgById, usersTeam, homeTeam, visitingTeam, nextGame, gameListForUserTeam);
        }

        @Override
        protected void onPostExecute(DataForMainScreen dataForMainScreen) {
            super.onPostExecute(dataForMainScreen);
            if (dataForMainScreen != null) {
                updateUI(dataForMainScreen.getOrganization(), dataForMainScreen.getUserTeam(), dataForMainScreen.getHomeTeam(), dataForMainScreen.getVisitingTeam(),
                        dataForMainScreen.getNextGame(), dataForMainScreen.getGamesForTeamList());
            }
        }
    }

    private Game findNextUnplayedGame(List<Game> gameListForUserTeam) {
        Collections.sort(gameListForUserTeam);
        for (Game game : gameListForUserTeam) {
            if (!game.isPlayedGame()) {
                if (teamsAreLoaded) {
                    homeTeam = listOfAllTeams.get(game.getHomeTeamName());
                    visitingTeam = listOfAllTeams.get(game.getVisitingTeamName());
                } else {
                    homeTeam = null;
                }
                return game;
            }
        }
        return null;
    }

    private Team getTeamWithPlayersAndStatsFromName(String userTeamName) {
        Team team = repository.getTeamWithTeamName(userTeamName);
        if (team != null) {
            team.setPlayers(repository.getPlayersForTeam(team.getTeamId()));
            if (team.getPlayers() != null) {
                for (Player player : team.getPlayers()) {
                    player.setBattingStats(repository.getBattingStatsForPlayer(player.getPlayerId()));
                    player.setPitchingStats(repository.getPitchingStatsForPlayer(player.getPlayerId()));
                }
            }
        }
        return team;
    }

    private class simRestOfGamesAsyncTask extends AsyncTask<Organization, Void, Void> {
        private Repository mRepository;
        private int mDayToSim;
        private TreeMap<String, Team> teamTreeMap;

        public simRestOfGamesAsyncTask(Repository repository, int dayToSim, TreeMap<String, Team> teamTreeMap) {
            mRepository = repository;
            mDayToSim = dayToSim;
            this.teamTreeMap = teamTreeMap;
        }

        @Override
        protected Void doInBackground(Organization... organizations) {
            Organization myOrg = organizations[0];
            List<Schedule> scheduleList = mRepository.getSchedulesForOrganization(myOrg.getId());
            Schedule schedule = scheduleList.get(myOrg.getCurrentYear());
            schedule.setGameList(mRepository.getGamesForSchedule(schedule.getScheduleId()));
            List<Game> gameList;
            Team homeTeam;
            Team visitingTeam;
            gameList = repository.getGamesForDay(mDayToSim, schedule.getScheduleId());
            if (gameList != null) {
                for (Game game : gameList) {
                    if (!game.isPlayedGame()) {
                        homeTeam = teamTreeMap.get(game.getHomeTeamName());
                        visitingTeam = teamTreeMap.get((game.getVisitingTeamName()));
                        if (homeTeam == null) {
                            homeTeam = getTeamWithPlayersAndStatsFromName(game.getHomeTeamName());
                        }
                        if (visitingTeam == null) {
                            visitingTeam = getTeamWithPlayersAndStatsFromName(game.getVisitingTeamName());
                        }
                        simGame(game, homeTeam, visitingTeam);
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainBinding.gameScoresTV.setText("");
            mainBinding.gameScoresTV.setVisibility(View.INVISIBLE);
            mainBinding.mainProgressBar.setVisibility(View.GONE);
            mainBinding.simulateGameButton.setEnabled(true);
            updateUI(organization, usersTeam, homeTeam, visitingTeam, nextGame, gamesForUserToPlay);
        }
    }

    private class getTeamFromDbAndUpdateUIAsyncTask extends AsyncTask<Void, Void, DataForMainScreen> {
        private Repository mRepository;
        private Organization mOrg;
        private Game mGame;
        private Team mUsersTeam;
        private List<Game> mGameList;

        public getTeamFromDbAndUpdateUIAsyncTask(Repository repository, Organization organization, Game nextGame, Team usersTeam, List<Game> gamesToPlay) {
            mRepository = repository;
            mOrg = organization;
            mGame = nextGame;
            mUsersTeam = usersTeam;
            mGameList = gamesToPlay;
        }

        @Override
        protected DataForMainScreen doInBackground(Void... voids) {
            Team homeTeam = null;
            Team visitingTeam = null;
            if (mGame.getHomeTeamName().equals(mUsersTeam.getTeamName())) {
                homeTeam = mUsersTeam;
                visitingTeam = getTeamWithPlayersAndStatsFromName(mGame.getVisitingTeamName());
                return new DataForMainScreen(mOrg, mUsersTeam, homeTeam, visitingTeam, mGame, mGameList);
            } else if (mGame.getVisitingTeamName().equals(mUsersTeam.getTeamName())) {
                homeTeam = getTeamWithPlayersAndStatsFromName(mGame.getHomeTeamName());
                visitingTeam = mUsersTeam;
                return new DataForMainScreen(mOrg, mUsersTeam, homeTeam, visitingTeam, mGame, mGameList);
            }
            return null;
        }

        @Override
        protected void onPostExecute(DataForMainScreen dataForMainScreen) {
            super.onPostExecute(dataForMainScreen);
            mainBinding.simulateGameButton.setEnabled(true);
            updateUI(dataForMainScreen.getOrganization(), dataForMainScreen.getUserTeam(), dataForMainScreen.getHomeTeam(), dataForMainScreen.getVisitingTeam(), dataForMainScreen.getNextGame(), dataForMainScreen.getGamesForTeamList());
        }
    }

    private class loadAllTeamsInBackgroundTask extends AsyncTask<String, Void, TreeMap<String, Team>> {
        private Repository repository;

        public loadAllTeamsInBackgroundTask(Repository repository) {
            this.repository = repository;
        }

        @Override
        protected TreeMap<String, Team> doInBackground(String... orgIds) {
            List<League> leagueList = repository.getLeaguesForOrganization(orgIds[0]);
            List<Division> divisionList = new ArrayList<>();
            for (League league : leagueList) {
                divisionList.addAll(repository.getDivisionsForLeague(league.getLeagueId()));
            }
            List<Team> teamList = new ArrayList<>();
            for (Division division : divisionList) {
                teamList.addAll(repository.getTeamsForDivision(division.getDivisionId()));
            }
            List<Player> playerList = new ArrayList<>();
            for (Team team : teamList) {
                team.setPlayers(repository.getPlayersForTeam(team.getTeamId()));
                playerList.addAll(team.getPlayers());
            }
            for (Player player : playerList) {
                player.setBattingStats(repository.getBattingStatsForPlayer(player.getPlayerId()));
                player.setPitchingStats(repository.getPitchingStatsForPlayer(player.getPlayerId()));
            }
            TreeMap<String, Team> teamTreeMap = new TreeMap<>();
            if (!teamList.isEmpty()) {
                for (Team team : teamList) {
                    teamTreeMap.put(team.getTeamName(), team);
                }
                return teamTreeMap;
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(TreeMap<String, Team> teams) {
            super.onPostExecute(teams);
            if (teams != null) {
                listOfAllTeams = teams;
                listOfAllTeams.put(homeTeam.getTeamName(), homeTeam);
                listOfAllTeams.put(visitingTeam.getTeamName(), visitingTeam);
                teamsAreLoaded = true;
            }
        }
    }

    private class loadAllGamesInBackgroundTask extends AsyncTask<String, Void, TreeMap<Integer, List<Game>>> {
        private Repository repository;

        public loadAllGamesInBackgroundTask(Repository repository) {
            this.repository = repository;
        }

        @Override
        protected TreeMap<Integer, List<Game>> doInBackground(String... orgIds) {
            Organization organization = repository.getOrganizationById(orgIds[0]);
            List<Schedule> scheduleList = repository.getSchedulesForOrganization(orgIds[0]);
            List<Game> gameList = repository.getGamesForSchedule(scheduleList.get(organization.getCurrentYear()).getScheduleId());
            if (gameList == null) {
                return null;
            }
            Collections.sort(gameList);
            int numOfDaysInSchedule = gameList.get(gameList.size()-1).getDay() + 1;
            TreeMap<Integer, List<Game>> gamesTreeMap = new TreeMap<>();
            for (int i = 0; i < numOfDaysInSchedule; i++) {
                List<Game> gamesForDay = new ArrayList<>();
                for (Game game : gameList) {
                    if (game.getDay() == i) {
                        gamesForDay.add(game);
                    }
                }
                gamesTreeMap.put(i, gamesForDay);
            }
            return gamesTreeMap;

        }

        @Override
        protected void onPostExecute(TreeMap<Integer, List<Game>> gamesTreeMap) {
            super.onPostExecute(gamesTreeMap);
            if (gamesTreeMap != null) {
                listOfAllGamesByDay = gamesTreeMap;
                gamesAreLoaded = true;
            }
        }
    }
}
