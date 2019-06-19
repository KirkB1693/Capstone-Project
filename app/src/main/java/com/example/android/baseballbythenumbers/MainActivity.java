package com.example.android.baseballbythenumbers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Organization;
import com.example.android.baseballbythenumbers.Data.Schedule;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Generators.OrganizationGenerator;
import com.example.android.baseballbythenumbers.Generators.ScheduleGenerator;
import com.example.android.baseballbythenumbers.Simulators.GameSimulator;

import net.danlew.android.joda.JodaTimeAndroid;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Simulators.GameSimulator.getGameRecapString;

public class MainActivity extends AppCompatActivity {

    List<League> leagues;
    OrganizationGenerator organizationGenerator;
    List<Division> divisions;
    SpannableStringBuilder displayText;
    Schedule schedule;
    int gameToPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        JodaTimeAndroid.init(this);

        organizationGenerator = new OrganizationGenerator(this);
        displayText = new SpannableStringBuilder();

        int numberOfTeamsInDivision = 5;
        int numberOfDivisions = 3;
        int countriesToInclude = 9;
        Organization mlbClone = organizationGenerator.generateOrganization("MLB Clone", 0, 2, new String[] {"American", "National"}, new boolean[] {true, false}, numberOfTeamsInDivision
                , numberOfDivisions, countriesToInclude, null);

        leagues = mlbClone.getLeagues();

        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(mlbClone);
        schedule = scheduleGenerator.generateSchedule(1, true);
        gameToPlay = schedule.getGameList().size();
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
        simGame(null);
    }

    public void simGame(View view) {

        gameToPlay ++;
        if (gameToPlay > schedule.getGameList().size()-1) {
            gameToPlay = 0;
        }
        displayText = new SpannableStringBuilder();

        TextView textView = findViewById(R.id.helloWorld);
        Game nextGame = schedule.getGameList().get(gameToPlay);

        Team homeTeam = getTeamFromId(leagues, nextGame.getHomeTeamId());
        Team visitingTeam = getTeamFromId(leagues, nextGame.getVisitingTeamId());

        GameSimulator gameSimulator = new GameSimulator(this, nextGame, homeTeam, false, visitingTeam, false);
        int[] result = gameSimulator.simulateGame();

        nextGame.setHomeScore(result[0]);
        nextGame.setVisitorScore(result[1]);

        displayText.append(getGameRecapString()).append("\n\n\n");

       // displayText.delete(0,displayText.length()-2000);

        // displayText.append(gameSimulator.getHomePitchersUsed()).append("\n\n").append(gameSimulator.getVisitorPitchersUsed()).append("\n\n\n");

        textView.setText(displayText);
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
