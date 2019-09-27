package com.example.android.baseballbythenumbers.UI.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.baseballbythenumbers.BuildConfig;
import com.example.android.baseballbythenumbers.Data.BattingStats;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.UI.GamePlayActivity.GamePlayActivity;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.LineupGenerator;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.PitchingRotationGenerator;
import com.example.android.baseballbythenumbers.Generators.ScheduleGenerator;
import com.example.android.baseballbythenumbers.UI.NewLeagueSetupActivity.NewLeagueSetupActivity;
import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.Repository.Repository;
import com.example.android.baseballbythenumbers.Simulators.GameSimulator;
import com.example.android.baseballbythenumbers.UI.RosterActivity.RosterActivity;
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
    public static final String NEXT_GAME_EXTRA = "Next_Game";
    public static final String HOME_TEAM_EXTRA = "home_team_extra";
    public static final String ORGANIZATION_EXTRA = "organization_extra";
    public static final String VISITING_TEAM_EXTRA = "visiting_team_extra";
    public static final String USER_TEAM_NAME = "user_team_name";
    public static final String USER_TEAM_EXTRA = "user_team_extra";
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
    private boolean orgLoaded;

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
                if (organization != null) {
                    orgId = organization.getId();
                }
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

        orgLoaded = false;
        teamsAreLoaded = false;
        gamesAreLoaded = false;

        if (orgId != null) {                                 // If we have an orgId move forward
            updateUIFromDb(orgId);
            new loadAllTeamsInBackgroundTask(repository).execute(orgId);
            new loadAllGamesInBackgroundTask(repository).execute(orgId);
        } else {
            goToNewLeagueSetupActivity();                   // Otherwise setup a new league
        }

    }

    private void updateUIFromDb(String orgId) {
        new getOrgFromDbAndCreateUserGameListAsyncTask(repository, organization).execute(orgId);
    }

    private void goToNewLeagueSetupActivity() {
        // Go to NewLeagueSetupActivity
        Intent newLeagueintent = new Intent(this, NewLeagueSetupActivity.class);
        this.startActivity(newLeagueintent);
        this.finish();
    }

    private void updateUI() {

        if (sharedPreferences.getString(ORG_ID_SHARED_PREF_KEY, null) == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ORG_ID_SHARED_PREF_KEY, organization.getId());
            editor.apply();
        }
        if (nextGame == null) {
            endofSeason();
        }

        if (homeTeam != null && visitingTeam != null) {
            Player nextHomeStarter = PitchingRotationGenerator.getBestStarterAvailableForNextGame(homeTeam);
            Player nextVisitingStarter = PitchingRotationGenerator.getBestStarterAvailableForNextGame(visitingTeam);
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


    public void simGame(Game game, Team homeTeam, Team visitingTeam) {

        GameSimulator gameSimulator = new GameSimulator(this, game, homeTeam, false, visitingTeam, false, organization.getCurrentYear(), repository);
        int[] result = gameSimulator.simulateGame();

        game.setHomeScore(result[0]);
        game.setVisitorScore(result[1]);
        game.setPlayedGame(true);

        mainBinding.gameScoresTV.setText(String.format(Locale.US, "%s - %s\n%d    -    %d", game.getHomeTeamName(), game.getVisitingTeamName(), game.getHomeScore(), game.getVisitorScore()));

        repository.updateGame(game);
        repository.updateBoxScore(game.getHomeBoxScore());
        repository.insertAllBattingLines(game.getHomeBoxScore().getBattingLines());
        repository.insertAllPitchingLines(game.getHomeBoxScore().getPitchingLines());
        repository.updateBoxScore(game.getVisitorBoxScore());
        repository.insertAllBattingLines(game.getVisitorBoxScore().getBattingLines());
        repository.insertAllPitchingLines(game.getVisitorBoxScore().getPitchingLines());
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
        }
    }

    private void startRosterActivity() {
        Intent rosterIntent = new Intent(this, RosterActivity.class);
        rosterIntent.putExtra(USER_TEAM_EXTRA, usersTeam);
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
        gamePlayIntent.putExtra(USER_TEAM_NAME, usersTeam.getTeamName());
        this.startActivity(gamePlayIntent);
        this.finish();
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
        disableGameButtons();
        mainBinding.mainProgressBar.setVisibility(View.VISIBLE);
        mainBinding.gameScoresTV.setVisibility(View.VISIBLE);
        mainBinding.simulateGameButton.setEnabled(false);
        dayOfSchedule = nextGame.getDay();
        simAllGamesForDay();
        nextGame = findNextUnplayedGame(gamesForUserToPlay);
        dayOfSchedule++;
        if (nextGame != null) {
            while (dayOfSchedule != nextGame.getDay()) {
                if (dayOfSchedule > nextGame.getDay()) {
                    break;
                }
                simAllGamesForDay();
                dayOfSchedule++;
            }
            if (homeTeam == null || visitingTeam == null) {
                Timber.e("Games didn't load properly, either homeTeamName or visitingTeamName was null!!!");
            }
        } else {
            endofSeason();
        }


    }

    private void endofSeason() {
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(organization);
        organization.setCurrentYear(organization.getCurrentYear() + 1);
        Schedule newSeasonSchedule = scheduleGenerator.generateSchedule(mainBinding.mainProgressBar);
        List<Schedule> newSchedList = organization.getSchedules();
        newSchedList.add(newSeasonSchedule);
        organization.setSchedules(newSchedList);

        repository.updateOrganization(organization);
        repository.insertSchedule(newSeasonSchedule);
        repository.insertAllGames(newSeasonSchedule.getGameList());
        addNewStatsForPlayers();
        setNewListOfAllGamesByDay(newSeasonSchedule.getGameList());
        generateListOfGamesForUser();
        nextGame = findNextUnplayedGame(gamesForUserToPlay);
        resetTeamRecords();
        updateUI();
    }

    private void resetTeamRecords() {
        for (TreeMap.Entry entry : listOfAllTeams.entrySet()) {
            Team team = (Team) entry.getValue();
            team.setWins(0);
            team.setLosses(0);
            repository.updateTeam(team);
        }
    }

    private void setNewListOfAllGamesByDay(List<Game> gameList) {
        if (gameList == null) {
            Timber.e("Error!!!, Tried to setNewListOfAllGamesByDay with a null list of games!!!");
            return;
        }
        Collections.sort(gameList);
        int numOfDaysInSchedule = gameList.get(gameList.size() - 1).getDay() + 1;
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
        listOfAllGamesByDay = gamesTreeMap;
    }

    private void addNewStatsForPlayers() {
        List<Player> playerList = getAllPlayers();
        for (Player player : playerList) {
            List<BattingStats> battingStatsList = player.getBattingStats();
            BattingStats newBattingStats = new BattingStats(organization.getCurrentYear(), 0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0, player.getPlayerId());
            battingStatsList.add(organization.getCurrentYear(), newBattingStats);
            repository.insertBattingStats(newBattingStats);
            List<PitchingStats> pitchingStatsList = player.getPitchingStats();
            PitchingStats newPitchingStats = new PitchingStats(organization.getCurrentYear(),0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, player.getPlayerId());
            pitchingStatsList.add(organization.getCurrentYear(), newPitchingStats);
            repository.insertPitchingStats(newPitchingStats);
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


    private void simAllGamesForDay() {
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
            enableGamePlayButtons();
            updateUI();
        } else {
            Timber.e("Error teams and games should be loaded but aren't!!!");
        }
    }


    private class getOrgFromDbAndCreateUserGameListAsyncTask extends AsyncTask<String, Void, Organization> {
        private Repository asyncRepository;
        private Organization asyncOrganization;

        getOrgFromDbAndCreateUserGameListAsyncTask(Repository repository, Organization organization) {
            this.asyncRepository = repository;
            this.asyncOrganization = organization;

        }

        @Override
        protected Organization doInBackground(String... orgIds) {
            if (asyncOrganization == null) {
                asyncOrganization = asyncRepository.getOrganizationById(orgIds[0]);
                List<League> leagues = asyncRepository.getLeaguesForOrganization(asyncOrganization.getId());
                asyncOrganization.setLeagues(leagues);
                for (League league : leagues) {
                    league.setDivisions(asyncRepository.getDivisionsForLeague(league.getLeagueId()));
                }
                List<Schedule> schedules = asyncRepository.getSchedulesForOrganization(asyncOrganization.getId());
                asyncOrganization.setSchedules(schedules);
            }
            return asyncOrganization;
        }

        @Override
        protected void onPostExecute(Organization dataReturned) {
            super.onPostExecute(dataReturned);
            if (dataReturned != null) {
                orgLoaded = true;
                organization = dataReturned;
                if (teamsAreLoaded) {
                    combineTeamsWithOrganization();
                    if (gamesAreLoaded) {
                        generateListOfGamesForUser();
                        nextGame = findNextUnplayedGame(gamesForUserToPlay);
                        enableGamePlayButtons();
                        updateUI();
                    }
                }
            } else {
                Timber.e("Error Loading Organization!!!  -  Organization is null!");
            }
        }

    }

    private void generateListOfGamesForUser() {
        usersTeam = listOfAllTeams.get(organization.getUserTeamName());
        List<Game> gameListForUser = new ArrayList<>();
        for (TreeMap.Entry entry : listOfAllGamesByDay.entrySet())  {
            @SuppressWarnings("unchecked")
            List<Game> gameListforDay = (List<Game>) entry.getValue();
            if (gameListforDay != null) {
                for (Game game : gameListforDay){
                    if (game.getHomeTeamName().equals(usersTeam.getTeamName()) || game.getVisitingTeamName().equals(usersTeam.getTeamName())) {
                        gameListForUser.add(game);
                    }
                }
            }

        }
        gamesForUserToPlay = gameListForUser;
    }

    private void combineTeamsWithOrganization() {
        List<League> leagueList = organization.getLeagues();
        List<Division> divisionList = new ArrayList<>();
        for (League league : leagueList) {
            divisionList.addAll(league.getDivisions());
        }
        for (Division division : divisionList) {
            List<Team> teamListForDiv = new ArrayList<>();
            for (TreeMap.Entry entry : listOfAllTeams.entrySet()) {
                Team team = (Team) entry.getValue();
                if (division.getDivisionId().equals(team.getDivisionId())) {
                    teamListForDiv.add(team);
                }
            }
            division.setTeams(teamListForDiv);
        }
    }

    private Game findNextUnplayedGame(List<Game> gameListForUserTeam) {
        Collections.sort(gameListForUserTeam);
        for (Game game : gameListForUserTeam) {
            if (!game.isPlayedGame()) {
                if (teamsAreLoaded) {
                    homeTeam = listOfAllTeams.get(game.getHomeTeamName());
                    visitingTeam = listOfAllTeams.get(game.getVisitingTeamName());
                }
                return game;
            }
        }
        return null;
    }


    private void disableGameButtons() {
        mainBinding.editLineupButton.setEnabled(false);
        mainBinding.simulateGameButton.setEnabled(false);
        mainBinding.coachSetsLineupButton.setEnabled(false);
        mainBinding.startGameButton.setEnabled(false);
    }


    private class loadAllTeamsInBackgroundTask extends AsyncTask<String, Void, TreeMap<String, Team>> {
        private Repository asyncRepository;

        public loadAllTeamsInBackgroundTask(Repository repository) {
            asyncRepository = repository;
        }

        @Override
        protected TreeMap<String, Team> doInBackground(String... orgIds) {
            List<League> leagueList = asyncRepository.getLeaguesForOrganization(orgIds[0]);
            List<Division> divisionList = new ArrayList<>();
            for (League league : leagueList) {
                divisionList.addAll(asyncRepository.getDivisionsForLeague(league.getLeagueId()));
            }
            List<Team> teamList = new ArrayList<>();
            for (Division division : divisionList) {
                teamList.addAll(asyncRepository.getTeamsForDivision(division.getDivisionId()));
            }
            List<Player> playerList = new ArrayList<>();
            for (Team team : teamList) {
                team.setPlayers(asyncRepository.getPlayersForTeam(team.getTeamId()));
                playerList.addAll(team.getPlayers());
            }
            for (Player player : playerList) {
                List<BattingStats> battingStatsList = asyncRepository.getBattingStatsForPlayer(player.getPlayerId());
                Collections.sort(battingStatsList);
                player.setBattingStats(battingStatsList);
                List<PitchingStats> pitchingStatsList = asyncRepository.getPitchingStatsForPlayer(player.getPlayerId());
                Collections.sort(pitchingStatsList);
                player.setPitchingStats(pitchingStatsList);
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
                teamsAreLoaded = true;
                if (orgLoaded) {
                    combineTeamsWithOrganization();
                    if (gamesAreLoaded) {
                        generateListOfGamesForUser();
                        nextGame = findNextUnplayedGame(gamesForUserToPlay);
                        enableGamePlayButtons();
                        updateUI();
                    }
                }
            } else {
                Timber.e("Error!!! Error loading teams... Teams TreeMap returned null.");
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
            organization.setSchedules(scheduleList);
            List<Game> gameList = repository.getGamesForSchedule(organization.getScheduleForCurrentYear().getScheduleId());
            if (gameList == null) {
                return null;
            }
            Collections.sort(gameList);
            int numOfDaysInSchedule = gameList.get(gameList.size() - 1).getDay() + 1;
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
                if (teamsAreLoaded && orgLoaded) {
                    generateListOfGamesForUser();
                    nextGame = findNextUnplayedGame(gamesForUserToPlay);
                    enableGamePlayButtons();
                    updateUI();
                }
            }
        }
    }

    private void enableGamePlayButtons() {
        mainBinding.editLineupButton.setEnabled(true);
        mainBinding.simulateGameButton.setEnabled(true);
        mainBinding.coachSetsLineupButton.setEnabled(true);
        mainBinding.startGameButton.setEnabled(true);
    }
}
