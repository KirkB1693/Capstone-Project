package com.example.android.baseballbythenumbers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.TeamGenerator.TeamGenerator;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Positions.getPositionName;
import static com.example.android.baseballbythenumbers.LineupAndDefense.DefenseGenerator.defenseFromTeam;
import static com.example.android.baseballbythenumbers.LineupAndDefense.LineupGenerator.lineupFromTeam;

public class MainActivity extends AppCompatActivity {

    TeamGenerator teamGenerator;
    List<Team> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        JodaTimeAndroid.init(this);

        teams = new ArrayList<Team>();
        teamGenerator = new TeamGenerator(this, 5, 3, 4, 2,
                1, 1, 1, 1, 1, 1, 1,
                2, 1, 1, 0);
        newName(null);

    }

    public void newName(View view) {
        teams.add(teamGenerator.generateTeam("Padres", "San Diego", false, 100000000));
        TextView textView = findViewById(R.id.helloWorld);
        List<TreeMap<Integer, Player>> defenses = new ArrayList<>();
        List<TreeMap<Integer, Player>> lineups = new ArrayList<>();
        StringBuilder displayText = new StringBuilder();


        for (Team team : teams) {
            TreeMap<Integer, Player> defense = defenseFromTeam(team);
            TreeMap<Integer, Player> lineup = lineupFromTeam(team);
            defenses.add(defense);
            lineups.add(lineup);
            displayText.append(team.getTeamCity() + " " + team.getTeamName() + " : \n");
            List<Player> playerList = team.getPlayers();
            Collections.sort(playerList, Player.PrimaryPositionComparator);
            for (Player player : playerList) {

                displayText.append(getPositionName(player.getPrimaryPosition()) + " - " + player.getName() + " , Error Pct : " + player.getHittingPercentages().getErrorPct() +
                        " , Power : " + (player.getHittingPercentages().getHomeRunPct() + player.getHittingPercentages().getTriplePct() + player.getHittingPercentages().getDoublePct()) +
                        " , OBP : " + (BATTING_Z_SWING_PCT_MEAN - player.getHittingPercentages().getZSwingPct() +
                        player.getHittingPercentages().getBattingAverageBallsInPlay()) + "\n");

            }

        }

        int teamNumber = 0;
        for (TreeMap<Integer, Player> defense : defenses) {
            teamNumber++;
            displayText.append("Best Defense for Team " + teamNumber + ":\n");
            for (TreeMap.Entry entry : defense.entrySet()) {
                Player player = (Player) entry.getValue();
                String position = getPositionName((Integer) entry.getKey());
                displayText.append("Pos : " + position + " - Player : " + player.getName() + " - Throws : " + player.getThrows() + "\n");
            }
        }
        teamNumber = 0;
        for (TreeMap<Integer, Player> lineup : lineups) {
            teamNumber++;
            displayText.append("Best Lineup for Team " + teamNumber + ":\n");
            for (TreeMap.Entry entry : lineup.entrySet()) {
                Player player = (Player) entry.getValue();
                String orderPosition = entry.getKey().toString();
                displayText.append(orderPosition + " - Player : " + player.getName() + " , Pos : " + getPositionName(player.getPrimaryPosition()) + " , Bats : " + player.getHits() + "\n");
            }
        }
        textView.setText(displayText);
    }
}
