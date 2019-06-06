package com.example.android.baseballbythenumbers;

import android.content.Context;
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

import static com.example.android.baseballbythenumbers.Data.GameStates.getGameStateString;
import static com.example.android.baseballbythenumbers.Data.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Data.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Data.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.getPositionNameFromPrimaryPosition;
import static com.example.android.baseballbythenumbers.LineupAndDefense.DefenseGenerator.defenseFromTeam;
import static com.example.android.baseballbythenumbers.LineupAndDefense.LineupGenerator.lineupFromDefense;
import static com.example.android.baseballbythenumbers.Simulators.AtBatSimulator.getAtBatSummary;

public class MainActivity extends AppCompatActivity {

    TeamGenerator teamGenerator;
    List<Team> teams;
    int currentBatter;
    Player[] runners;
    private static final Player[] basesEmpty = new Player[]{null, null, null};
    private static final int[] freshCount = new int[]{0, 0};
    int runsScoredInInning;
    int outs;

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
        teams.add(teamGenerator.generateTeam("Padres", "San Diego", false, 100000000));
        outs = 0;
        runsScoredInInning = 0;
        currentBatter = 1;
        runners = basesEmpty;
        simAtBat(null);
    }

    public void simAtBat(View view) {

        TextView textView = findViewById(R.id.helloWorld);
        StringBuilder displayText = new StringBuilder();
        int teamNumber = 0;


        for (Team team : teams) {
            TreeMap<Integer, Player> defense = defenseFromTeam(team);
//                TreeMap<Integer, Player> lineup = lineupFromTeam(team);
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
                displayText.append(getPositionNameFromPrimaryPosition(position) + " - " + player.getName() + " , Error Pct : " + player.getHittingPercentages().getErrorPct() +
                        " , Power : " + (player.getHittingPercentages().getHomeRunPct() + player.getHittingPercentages().getTriplePct() + player.getHittingPercentages().getDoublePct()) +
                        " , BABIP : " + (player.getHittingPercentages().getBattingAverageBallsInPlay()) + "\n");

            }

            displayText.append("\nBABIP AVE For Pitchers : " + babipForPitchers/numPitchers + "\nBABIP AVE For Batters : " + babipForBatters/numBatters + "\n\n");


            teamNumber++;
            /*displayText.append("Best Defense for Team ").append(teamNumber).append(":\n");
            for (TreeMap.Entry entry : defense.entrySet()) {
                Player player = (Player) entry.getValue();
                String position = getPositionNameFromPrimaryPosition(player.getPrimaryPosition());
                displayText.append("Pos : ").append(position).append(" - Player : ").append(player.getName()).append(" - Throws : ").append(player.getThrowingHand()).append("\n");
            }*/

            displayText.append("Batting Order " + teamNumber + ":\n");
            for (TreeMap.Entry entry : lineupFromDefense.entrySet()) {
                Player player = (Player) entry.getValue();
                String orderPosition = entry.getKey().toString();
                displayText.append(orderPosition + " - Player : " + player.getName() + " , Pos : " + getPositionNameFromPrimaryPosition(player.getPrimaryPosition()) + " , Bats : " + player.getHits() + "\n");
            }

           /* displayText.append("Best Lineup for Team " + teamNumber + ":\n");
            for (TreeMap.Entry entry : lineup.entrySet()) {
                Player player = (Player) entry.getValue();
                String orderPosition = entry.getKey().toString();
                displayText.append(orderPosition + " - Player : " + player.getName() + " , Pos : " + getPositionNameFromPrimaryPosition(player.getPrimaryPosition()) + " , Bats : " + player.getHits() + "\n");
            }*/
            for (int j = 0; j < 100; j++) {
                displayText.append("\n\n");
                AtBatSimulator currentAtBat = generateNewAtBat(this, freshCount, outs, currentBatter, lineupFromDefense, runners, defense);
                displayText.append(lineupFromDefense.get(currentBatter).getName()).append(" At Bat Result : \n");
                int atBatResult = currentAtBat.simulateAtBat();
                int playerWhoJustHit = currentBatter;
                currentBatter = currentAtBat.getCurrentBatter();
                outs = currentAtBat.getOuts();
                runsScoredInInning += currentAtBat.getRunsScored();
                displayText.append("\nCurrently - Outs in Inning : ").append(outs).append(", Runs In Inning : ").append(runsScoredInInning).append("\n\n");
                if (outs == 3) {
                    runsScoredInInning = 0;
                    for (int i = 0; i < runners.length; i++) {
                        runners[i] = null;
                    }
                    outs = 0;
                } else {
                    runners = currentAtBat.getRunners();
                }

                displayText.append(getAtBatSummary()).append("\n\n");


                displayText.append("Result from Result Code : ").append(getGameStateString(atBatResult)).append("\n\n");

                displayText.append("Player : " + lineupFromDefense.get(playerWhoJustHit).getName() + " - batting stats this season : " + lineupFromDefense.get(playerWhoJustHit).getBattingStats().get(0).toString());
                displayText.append("\n\n\n\n");
            }


        }
        textView.setText(displayText);
    }

    private AtBatSimulator generateNewAtBat(Context context, int[] freshCount, int outs, int currentBatter, TreeMap<Integer, Player> lineupFromDefense, Player[] runners, TreeMap<Integer, Player> defense) {
        return new AtBatSimulator(context, freshCount, outs, currentBatter, lineupFromDefense, runners, defense);
    }
}
