package com.example.android.baseballbythenumbers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.BattingLine;
import com.example.android.baseballbythenumbers.Data.BoxScore;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.PitchingRotationGenerator;
import com.example.android.baseballbythenumbers.Generators.OrganizationGenerator;
import com.example.android.baseballbythenumbers.Repository.Repository;
import com.example.android.baseballbythenumbers.Simulators.GameSimulator;
import com.example.android.baseballbythenumbers.databinding.ActivityMainBinding;

import net.danlew.android.joda.JodaTimeAndroid;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Simulators.GameSimulator.getGameRecapString;

public class MainActivity extends AppCompatActivity {

    public static final String ORG_ID_SHARED_PREF_KEY = "orgId";
    private List<League> leagues;
    private OrganizationGenerator organizationGenerator;
    private List<Division> divisions;
    private SpannableStringBuilder displayText;
    private Schedule schedule;
    private int gameToPlay;
    private Organization organization;
    private TextView textView;
    private Repository repository;
    private ActivityMainBinding mainBinding;
    private static boolean orgInDatabase;
    private int dayOfSchedule;
    private SharedPreferences sharedPreferences;

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
                orgId = extras.getString(NewLeagueSetupActivity.ORGANIZATION_EXTRA);
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


        if (orgId != null) {                                 // If we have an orgId try to get organization from database
            getOrganizationFromDb(orgId);
        } else {
            goToNewLeagueSetupActivity();                   // Otherwise setup a new league
        }


        // simGame(null);
    }

    private void getOrganizationFromDb(String orgId) {
        mainBinding.progressBar.setVisibility(View.VISIBLE);
        new getOrgFromDbAsyncTask(repository).execute(orgId);
    }

    private void goToNewLeagueSetupActivity() {
        // Go to NewLeagueSetupActivity
        Intent newLeagueintent = new Intent(this, NewLeagueSetupActivity.class);
        this.startActivity(newLeagueintent);
        finish();
    }

    private void updateUI(Organization orgToDisplay) {
        organization = orgToDisplay;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ORG_ID_SHARED_PREF_KEY, organization.getId());
        editor.apply();
        Team usersTeam = findTeam(organization.getUserTeamName());
        Game nextGame = null;
        Team homeTeam = null;
        Team visitingTeam = null;
        if (usersTeam != null) {

            Schedule schedule = organization.getSchedules().get(organization.getCurrentYear());
            List<Game> gameList = schedule.getGameList();
            for (Game game : gameList) {
                if (!game.isPlayedGame()) {
                    if (game.getHomeTeamName().equals(usersTeam.getTeamName())) {
                        nextGame = game;
                        homeTeam = usersTeam;
                        break;
                    }
                    if (game.getVisitingTeamName().equals(usersTeam.getTeamName())) {
                        nextGame = game;
                        visitingTeam = usersTeam;
                        break;
                    }
                }
            }
            if (nextGame != null) {
                dayOfSchedule = nextGame.getDay();
                if (homeTeam != null) {
                    visitingTeam = findTeam(nextGame.getVisitingTeamName());
                } else {
                    homeTeam = findTeam(nextGame.getHomeTeamName());
                }
                if (homeTeam != null && visitingTeam != null) {
                    Player nextHomeStarter = PitchingRotationGenerator.getBestStarterAvailable(homeTeam);
                    Player nextVisitingStarter = PitchingRotationGenerator.getBestStarterAvailable(visitingTeam);
                    mainBinding.progressBar.setVisibility(View.GONE);
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
            }

        }

    }

    @NotNull
    private String formatWL(Team team) {
        return team.getWins() + " - " + team.getLosses();
    }

    @NotNull
    private String formatStarterStats(List<PitchingStats> pitchingStats) {
        return "ERA : " + String.format(Locale.US, "%.2f", pitchingStats.get(0).getERA()) + ", WHIP : " +
                String.format(Locale.US, "%.2f", pitchingStats.get(0).getWHIP());
    }


    private Team findTeam(String teamName) {
        List<League> leagueList = organization.getLeagues();
        List<Division> divisionList = new ArrayList<>();
        for (League league : leagueList) {
            divisionList.addAll(league.getDivisions());
        }
        for (Division division : divisionList) {
            List<Team> teamList = division.getTeams();
            for (Team team : teamList) {
                if (teamName.equals(team.getTeamName())) {
                    return team;
                }
            }
        }
        return null;
    }


    public void simGame(View view) {

        gameToPlay++;
        if (gameToPlay > schedule.getGameList().size() - 1) {
            gameToPlay = 0;
        }
        displayText = new SpannableStringBuilder();

        Game nextGame = schedule.getGameList().get(gameToPlay);

        Team homeTeam = getTeamFromId(leagues, nextGame.getHomeTeamName());
        Team visitingTeam = getTeamFromId(leagues, nextGame.getVisitingTeamName());

        GameSimulator gameSimulator = new GameSimulator(this, nextGame, homeTeam, false, visitingTeam, false);
        int[] result = gameSimulator.simulateGame();

        nextGame.setHomeScore(result[0]);
        nextGame.setVisitorScore(result[1]);

        displayText.append(getGameRecapString()).append("\n\n\n");

        displayText.delete(0, displayText.length() - 500);

        // displayText.append(gameSimulator.getHomePitchersUsed()).append("\n\n").append(gameSimulator.getVisitorPitchersUsed()).append("\n\n\n");

        //displayText.append("Schedule : \n").append(schedule.toString()).append("\n\n\n");

        displayText.append("BattingLines : \n").append(nextGame.getHomeBoxScore().getBattingLines().toString()).append("\n\n\n");


        repository.insertOrganization(organization);

        List<Schedule> scheduleList = new ArrayList<>();

        scheduleList.add(schedule);

        organization.setSchedules(scheduleList);

        repository.insertAllSchedules(organization.getSchedules());

        repository.insertAllGames(organization.getSchedules().get(0).getGameList());

        repository.insertBoxScore(nextGame.getHomeBoxScore());

        repository.insertAllBattingLines(nextGame.getHomeBoxScore().getBattingLines());

        //displayText.append("Schedule from Database : \n").append(repository.getAllSchedules().toString()).append("\n\n\n");

        displayText.append("Box Score ID Searched : ").append(nextGame.getHomeBoxScore().getBoxScoreId()).append("\n");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new BattingLinesAsyncTask(repository, textView, displayText).execute(nextGame.getHomeBoxScore().getBoxScoreId());


    }


    private static class BattingLinesAsyncTask extends AsyncTask<String, Void, List<BattingLine>> {
        private Repository repository;
        private TextView textView;
        private SpannableStringBuilder displayText;


        public BattingLinesAsyncTask(Repository repository, TextView textView, SpannableStringBuilder displayText) {
            this.repository = repository;
            this.textView = textView;
            this.displayText = displayText;
        }

        @Override
        protected List<BattingLine> doInBackground(String... strings) {
            String boxScoreId = strings[0];
            return repository.getBattingLinesForBoxScore(boxScoreId);
        }

        @Override
        protected void onPostExecute(List<BattingLine> result) {
            displayText.append("BattingLines from Database : \n").append(result.toString()).append("\n\n\n");

            textView.setText(displayText);

            repository.deleteAll();
        }
    }


    private Team getTeamFromId(List<League> leagues, String teamId) {
        for (League league : leagues) {
            for (Division division : league.getDivisions()) {
                for (Team team : division.getTeams()) {
                    if (team.getTeamName().equals(teamId)) {
                        return team;
                    }
                }
            }
        }
        return null;
    }


    private class getOrgFromDbAsyncTask extends AsyncTask<String, Void, Organization> {
        private Repository repository;

        public getOrgFromDbAsyncTask(Repository repository) {
            this.repository = repository;
        }

        @Override
        protected Organization doInBackground(String... orgIds) {
            Organization orgById = repository.getOrganizationById(orgIds[0]);
            fillOutOrgFromDb(orgById, orgIds[0]);
            return orgById;
        }

        private void fillOutOrgFromDb(Organization orgById, String orgId) {
            if (orgById == null) {
                goToNewLeagueSetupActivity();
                return;
            }
            List<League> leagueList = repository.getLeaguesForOrganization(orgId);
            if (leagueList == null) {
                invalidDatabaseSetupNewLeague(orgById);
                return;
            }
            orgById.setLeagues(leagueList);
            List<Division> divisionList = new ArrayList<>();
            for (League league : leagueList) {
                league.setDivisions(repository.getDivisionsForLeague(league.getLeagueId()));
                divisionList.addAll(league.getDivisions());
            }
            if (divisionList.isEmpty()) {
                invalidDatabaseSetupNewLeague(orgById);
                return;
            }
            List<Team> teamList = new ArrayList<>();
            for (Division division : divisionList) {
                division.setTeams(repository.getTeamsForDivision(division.getDivisionId()));
                teamList.addAll(division.getTeams());
            }
            if (teamList.isEmpty()) {
                invalidDatabaseSetupNewLeague(orgById);
                return;
            }
            List<Player> playerList = new ArrayList<>();
            for (Team team : teamList) {
                team.setPlayers(repository.getPlayersForTeam(team.getTeamId()));
                playerList.addAll(team.getPlayers());
            }
            if (playerList.isEmpty()) {
                invalidDatabaseSetupNewLeague(orgById);
                return;
            }
            for (Player player : playerList) {
                player.setBattingStats(repository.getBattingStatsForPlayer(player.getPlayerId()));
                player.setPitchingStats(repository.getPitchingStatsForPlayer(player.getPlayerId()));
            }

            List<Schedule> scheduleList = repository.getSchedulesForOrganization(orgId);
            if (scheduleList == null) {
                invalidDatabaseSetupNewLeague(orgById);
                return;
            }
            orgById.setSchedules(scheduleList);
            List<Game> gameList = new ArrayList<>();
            for (Schedule schedule : scheduleList) {
                schedule.setGameList(repository.getGamesForSchedule(schedule.getScheduleId()));
                gameList.addAll(schedule.getGameList());
            }
            if (gameList.isEmpty()) {
                invalidDatabaseSetupNewLeague(orgById);
                return;
            }
            List<BoxScore> boxScoreList = new ArrayList<>();
            for (Game game : gameList) {
                List<BoxScore> tempList = repository.getBoxScoresForGame(game.getGameId());
                if (tempList != null) {
                    for (BoxScore boxScore : tempList) {
                        if (boxScore.isBoxScoreForHomeTeam()) {
                            game.setHomeBoxScore(boxScore);
                        } else {
                            game.setVisitorBoxScore(boxScore);
                        }
                    }
                    boxScoreList.addAll(tempList);
                }
            }
            if (!boxScoreList.isEmpty()) {
                for (BoxScore boxScore : boxScoreList) {
                    boxScore.setBattingLines(repository.getBattingLinesForBoxScore(boxScore.getBoxScoreId()));
                    boxScore.setPitchingLines(repository.getPitchingLinesForBoxScore(boxScore.getBoxScoreId()));
                }
            }
        }

        private void invalidDatabaseSetupNewLeague(Organization orgById) {
            orgById = null;
            goToNewLeagueSetupActivity();
        }

        @Override
        protected void onPostExecute(Organization organization) {
            super.onPostExecute(organization);
            if (organization != null) {
                updateUI(organization);
            }
        }
    }
}
