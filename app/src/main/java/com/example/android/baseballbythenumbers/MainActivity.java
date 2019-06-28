package com.example.android.baseballbythenumbers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.BattingLine;
import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Generators.OrganizationGenerator;
import com.example.android.baseballbythenumbers.Repository.Repository;
import com.example.android.baseballbythenumbers.Simulators.GameSimulator;
import com.example.android.baseballbythenumbers.databinding.ActivityMainBinding;

import net.danlew.android.joda.JodaTimeAndroid;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Simulators.GameSimulator.getGameRecapString;

public class MainActivity extends AppCompatActivity {

    List<League> leagues;
    OrganizationGenerator organizationGenerator;
    List<Division> divisions;
    static SpannableStringBuilder displayText;
    Schedule schedule;
    int gameToPlay;
    Organization mlbClone;
    static TextView textView;
    static Repository repository;
    static ActivityMainBinding mainBinding;
    static boolean orgInDatabase;


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

        mainBinding.progressBar.setVisibility(View.VISIBLE);

        if (!isOrganizationCreated()) {
            mainBinding.progressBar.setVisibility(View.INVISIBLE);
            // Create a new League...
            mainBinding.helloWorld.setText("Database not created!!!");
            // Go to NewLeagueSetupActivity
            Intent intent = new Intent(this, NewLeagueSetupActivity.class);
            this.startActivity(intent);
        } else {
            mainBinding.progressBar.setVisibility(View.INVISIBLE);
            mainBinding.helloWorld.setText("Database created??");
        }

/*        organizationGenerator = new OrganizationGenerator(this);
        displayText = new SpannableStringBuilder();

        int numberOfTeamsInDivision = 5;
        int numberOfDivisions = 3;
        int countriesToInclude = 9;
        mlbClone = organizationGenerator.generateOrganization("MLB Clone", 0, 2, new String[] {"American", "National"}, new boolean[] {true, false}, numberOfTeamsInDivision
                , numberOfDivisions, countriesToInclude, null);

        leagues = mlbClone.getLeagues();

        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(mlbClone);
        schedule = scheduleGenerator.generateSchedule(1, true);
        gameToPlay = schedule.getGameList().size();*/
        /*
        for (League league : leagues) {
            displayText.append("\n\n").append(league.getLeagueName()).append(" League : \n");
            divisions = league.getDivisions();
            for (Division division : divisions) {
                displayText.append("\n\n");
                displayText.append("Division Name : ").append(division.getDivisionName()).append("\n");
                for (Team team : division.getTeams()) {
                    displayText.append("    ").append(team.getTeamCity()).append(" ").append(team.getTeamName()).append("\n");
                    displayText.append("        Team has ").append(team.getPlayers().size()).append(" players. Player 1) ").append(team.getPlayers().get(0).getName()).append("\n");
                }
            }
        }*/
        // simGame(null);
    }

    private boolean isOrganizationCreated() {
        final CountDownLatch taskcomplete = new CountDownLatch(1);
        CheckDatabaseAsyncTask task = new CheckDatabaseAsyncTask(taskcomplete);
        task.execute();
        try {
            taskcomplete.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return orgInDatabase;
    }


    private static class CheckDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        private CountDownLatch latch;

        public CheckDatabaseAsyncTask(CountDownLatch latch){
            this.latch = latch;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Organization[] anyOrganization = repository.getAnyOrganization();
            if (anyOrganization.length > 0) {
                orgInDatabase = true;
                latch.countDown();
            } else {
                orgInDatabase = false;
                latch.countDown();
            }
            return null;
        }

    }

    public void simGame(View view) {

        gameToPlay ++;
        if (gameToPlay > schedule.getGameList().size()-1) {
            gameToPlay = 0;
        }
        displayText = new SpannableStringBuilder();

        textView = findViewById(R.id.helloWorld);
        Game nextGame = schedule.getGameList().get(gameToPlay);

        Team homeTeam = getTeamFromId(leagues, nextGame.getHomeTeamId());
        Team visitingTeam = getTeamFromId(leagues, nextGame.getVisitingTeamId());

        GameSimulator gameSimulator = new GameSimulator(this, nextGame, homeTeam, false, visitingTeam, false);
        int[] result = gameSimulator.simulateGame();

        nextGame.setHomeScore(result[0]);
        nextGame.setVisitorScore(result[1]);

        displayText.append(getGameRecapString()).append("\n\n\n");

        displayText.delete(0,displayText.length()-500);

        // displayText.append(gameSimulator.getHomePitchersUsed()).append("\n\n").append(gameSimulator.getVisitorPitchersUsed()).append("\n\n\n");

        //displayText.append("Schedule : \n").append(schedule.toString()).append("\n\n\n");

        displayText.append("BattingLines : \n").append(nextGame.getHomeBoxScore().getBattingLines().toString()).append("\n\n\n");



        repository.insertOrganization(mlbClone);

        List<Schedule> scheduleList = new ArrayList<>();

        scheduleList.add(schedule);

        mlbClone.setSchedules(scheduleList);

        repository.insertAllSchedules(mlbClone.getSchedules());

        repository.insertAllGames(mlbClone.getSchedules().get(0).getGameList());

        repository.insertBoxScore(nextGame.getHomeBoxScore());

        repository.insertAllBattingLines(nextGame.getHomeBoxScore().getBattingLines());

        //displayText.append("Schedule from Database : \n").append(repository.getAllSchedules().toString()).append("\n\n\n");

        displayText.append("Box Score ID Searched : ").append(nextGame.getHomeBoxScore().getBoxScoreId()).append("\n");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BattingLinesAsyncTask battingLinesAsyncTask = (BattingLinesAsyncTask) new BattingLinesAsyncTask().execute(nextGame.getHomeBoxScore().getBoxScoreId());




    }


    private static class BattingLinesAsyncTask extends AsyncTask<String, Void, List<BattingLine>> {

        @Override
        protected List<BattingLine> doInBackground(String... strings) {
            String boxScoreId = strings[0];
            return repository.getBattingLinesForBoxScore(boxScoreId);
        }

        @Override
        protected void onPostExecute (List<BattingLine> result) {
            displayText.append("BattingLines from Database : \n").append(result.toString()).append("\n\n\n");

            textView.setText(displayText);

            repository.deleteAll();
        }
    }


    private Team getTeamFromId(List<League> leagues, String teamId) {
        for (League league : leagues) {
            for (Division division: league.getDivisions()) {
                for (Team team: division.getTeams()) {
                    if (team.getTeamName().equals(teamId)) {
                        return team;
                    }
                }
            }
        }
        return null;
    }

    private Team getRandomTeam(@NotNull List<League> leagues) {
        Random random = new Random();
        League randomLeague = leagues.get(random.nextInt(leagues.size()));
        Division randomDivision = randomLeague.getDivisions().get(random.nextInt(randomLeague.getDivisions().size()));
        return randomDivision.getTeams().get(random.nextInt(randomDivision.getTeams().size()));
    }

}
