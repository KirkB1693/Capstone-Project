package com.example.android.baseballbythenumbers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Simulators.AtBatSimulator;
import com.example.android.baseballbythenumbers.TeamGenerator.TeamGenerator;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Data.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Data.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.getPositionName;
import static com.example.android.baseballbythenumbers.LineupAndDefense.DefenseGenerator.defenseFromTeam;
import static com.example.android.baseballbythenumbers.LineupAndDefense.LineupGenerator.lineupFromDefense;
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
        StringBuilder displayText = new StringBuilder();
        int teamNumber = 0;

        for (Team team : teams) {
            TreeMap<Integer, Player> defense = defenseFromTeam(team);
            TreeMap<Integer, Player> lineup = lineupFromTeam(team);
            TreeMap<Integer, Player> lineupFromDefense = lineupFromDefense(defense, team);
            displayText.append(team.getTeamCity() + " " + team.getTeamName() + " : \n");
            List<Player> playerList = team.getPlayers();
            Collections.sort(playerList, Player.PrimaryPositionComparator);
            int babipForPitchers = 0;
            int numPitchers = 0;
            int babipForBatters = 0;
            int numBatters = 0;

            for (Player player : playerList) {
                int position = player.getPrimaryPosition();
                switch (position) {
                    case STARTING_PITCHER:
                        babipForPitchers += player.getHittingPercentages().getBattingAverageBallsInPlay();
                        numPitchers++;
                        break;
                    case LONG_RELIEVER:
                        babipForPitchers += player.getHittingPercentages().getBattingAverageBallsInPlay();
                        numPitchers++;
                        break;
                    case SHORT_RELEIVER:
                        babipForPitchers += player.getHittingPercentages().getBattingAverageBallsInPlay();
                        numPitchers++;
                        break;
                    default:
                        babipForBatters += player.getHittingPercentages().getBattingAverageBallsInPlay();
                        numBatters++;
                }
                displayText.append(getPositionName(position) + " - " + player.getName() + " , Error Pct : " + player.getHittingPercentages().getErrorPct() +
                        " , Power : " + (player.getHittingPercentages().getHomeRunPct() + player.getHittingPercentages().getTriplePct() + player.getHittingPercentages().getDoublePct()) +
                        " , OBP : " + (BATTING_Z_SWING_PCT_MEAN - player.getHittingPercentages().getZSwingPct() +
                        player.getHittingPercentages().getBattingAverageBallsInPlay()) + "\n");

            }

            displayText.append("\nBABIP AVE For Pitchers : " + babipForPitchers/numPitchers + "\nBABIP AVE For Batters : " + babipForBatters/numBatters + "\n\n");


            teamNumber++;
            displayText.append("Best Defense for Team ").append(teamNumber).append(":\n");
            for (TreeMap.Entry entry : defense.entrySet()) {
                Player player = (Player) entry.getValue();
                String position = getPositionName(player.getPrimaryPosition());
                displayText.append("Pos : ").append(position).append(" - Player : ").append(player.getName()).append(" - Throws : ").append(player.getThrowingHand()).append("\n");
            }

            displayText.append("Best Lineup from Defense " + teamNumber + ":\n");
            for (TreeMap.Entry entry : lineupFromDefense.entrySet()) {
                Player player = (Player) entry.getValue();
                String orderPosition = entry.getKey().toString();
                displayText.append(orderPosition + " - Player : " + player.getName() + " , Pos : " + getPositionName(player.getPrimaryPosition()) + " , Bats : " + player.getHits() + "\n");
            }

            displayText.append("Best Lineup for Team " + teamNumber + ":\n");
            for (TreeMap.Entry entry : lineup.entrySet()) {
                Player player = (Player) entry.getValue();
                String orderPosition = entry.getKey().toString();
                displayText.append(orderPosition + " - Player : " + player.getName() + " , Pos : " + getPositionName(player.getPrimaryPosition()) + " , Bats : " + player.getHits() + "\n");
            }
            displayText.append("\n\n\n");
            AtBatSimulator currentAtBat = new AtBatSimulator(this, new int[]{0,0}, 0, 1, lineupFromDefense, new Player[] {null, null, null}, defense);
            int atBatResult = currentAtBat.simulateAtBat();

            displayText.append("Player : " + lineupFromDefense.get(1).getName() + " - plate appearances this season : " + lineupFromDefense.get(1).getBattingStats().get(0).getPlateAppearances() );
            displayText.append("\n\n\n");
        }



        textView.setText(displayText);
    }
}
