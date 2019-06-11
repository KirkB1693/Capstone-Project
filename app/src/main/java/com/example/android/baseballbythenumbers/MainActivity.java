package com.example.android.baseballbythenumbers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.baseballbythenumbers.Data.Division;
import com.example.android.baseballbythenumbers.Data.League;
import com.example.android.baseballbythenumbers.Data.Level;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Generators.CityGenerator;
import com.example.android.baseballbythenumbers.Generators.LevelGenerator;
import com.example.android.baseballbythenumbers.Generators.TeamGenerator;
import com.example.android.baseballbythenumbers.Generators.TeamNameGenerator;
import com.example.android.baseballbythenumbers.Simulators.AtBatSimulator;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Generators.LineupAndDefense.DefenseGenerator.defenseFromLineup;
import static com.example.android.baseballbythenumbers.Generators.LineupAndDefense.LineupGenerator.lineupFromTeam;

public class MainActivity extends AppCompatActivity {

    TeamGenerator teamGenerator;
    TeamNameGenerator teamNameGenerator;
    CityGenerator cityGenerator;
    List<Team> teams;
    List<League> leagues;
    LevelGenerator levelGenerator;
    List<Division> divisions;
    int currentBatterVisitor;
    int currentBatterHome;
    int currentBatter;
    boolean isVisitorHitting;
    Player[] runners;
    private static final Player[] basesEmpty = new Player[]{null, null, null};
    private static final int[] freshCount = new int[]{0, 0};
    int runsScoredInInning;
    int outs;
    int homeScore = 0;
    int visitorScore = 0;
    int inningsPlayed = 0;
    TreeMap<Integer, Player> lineupHome;
    TreeMap<Integer, Player> defenseHome;
    TreeMap<Integer, Player> lineupVisitor;
    TreeMap<Integer, Player> defenseVisitor;

    TreeMap<Integer, Player> lineup;
    TreeMap<Integer, Player> defense;
    String visitingTeamName;
    String homeTeamName;
    String batttingTeamName;
    String fieldingTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        JodaTimeAndroid.init(this);

        levelGenerator = new LevelGenerator(this);
        divisions = new ArrayList<>();
       /* teamNameGenerator = new TeamNameGenerator(this);
        String teamName = teamNameGenerator.generateTeamName();
        cityGenerator = new CityGenerator(this);
        String city = cityGenerator.generateCity();
        teams = new ArrayList<Team>();
        teamGenerator = new TeamGenerator(this, 5, 3, 4, 2,
                1, 1, 1, 1, 1, 1, 1,
                2, 1, 1, 2);
        teams.add(teamGenerator.generateTeam(teamName, city, true, 100000000));*/
        outs = 0;
        runsScoredInInning = 0;
        currentBatterVisitor = 1;
        currentBatterHome = 1;
        currentBatter = currentBatterVisitor;
        runners = basesEmpty;
        isVisitorHitting = true;
        int numberOfTeamsInDivision = 5;
        int numberOfDivisions = 3;
        int countriesToInclude = 9;

        Level mlbClone = levelGenerator.generateLevel("MLB Clone", 0, 2, new String[] {"American", "National"}, new boolean[] {true, false}, numberOfTeamsInDivision
                , numberOfDivisions, countriesToInclude, null);

        leagues = mlbClone.getLeagues();
        divisions = leagues.get(0).getDivisions();
        /*int[] teamMakeup = new int[]{5, 3, 4, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 0};


        String divisionName = EAST;
        switch (numberOfDivisions) {
            case 0:
                divisions.add(divisionGenerator.generateDivision(NO_DIVISONS, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                break;
            case 1:
                divisions.add(divisionGenerator.generateDivision(NO_DIVISONS, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                break;
            case 2:
                divisions.add(divisionGenerator.generateDivision(WEST, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                divisions.add(divisionGenerator.generateDivision(EAST, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                break;
            case 3:
                divisions.add(divisionGenerator.generateDivision(WEST, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                divisions.add(divisionGenerator.generateDivision(CENTRAL, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                divisions.add(divisionGenerator.generateDivision(EAST, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                break;
            case 4:
                divisions.add(divisionGenerator.generateDivision(WEST, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                divisions.add(divisionGenerator.generateDivision(NORTH, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                divisions.add(divisionGenerator.generateDivision(SOUTH, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                divisions.add(divisionGenerator.generateDivision(EAST, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                break;
            default:
                divisions.add(divisionGenerator.generateDivision(NO_DIVISONS, false, numberOfTeamsInDivision, teamMakeup, numberOfDivisions, countriesToInclude));
                break;
        }*/

        //Team 1 Plays : Team 2

        Team team1 = divisions.get(0).getTeams().get(0);
        Team team2 = divisions.get(0).getTeams().get(1);

        lineupHome = lineupFromTeam(team1);
        defenseHome = defenseFromLineup(lineupHome, team1);
        lineupVisitor = lineupFromTeam(team2);
        defenseVisitor = defenseFromLineup(lineupVisitor, team2);

        lineup = lineupVisitor;
        defense = defenseHome;

        homeTeamName = team1.getTeamCity() + " " + team1.getTeamName();
        visitingTeamName = team2.getTeamCity() + " " + team2.getTeamName();

        batttingTeamName = visitingTeamName;
        fieldingTeamName = homeTeamName;

        simAtBat(null);
    }

    public void simAtBat(View view) {

        TextView textView = findViewById(R.id.helloWorld);
        StringBuilder displayText = new StringBuilder();
        // int teamNumber = 0;
        boolean switchSides = false;

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
        }
       /*
        boolean gameNotOver = true;
        boolean homeTeamFinishedAtBat = false;

        while (gameNotOver) {
            displayText.append("\n\n");

            displayText.append("Current Score After ").append(getInningsForDisplay(inningsPlayed)).append(" Innings.\n").append(homeTeamName).append(" ").append(homeScore).append(" - ")
                    .append(visitingTeamName).append(" ").append(visitorScore);

            displayText.append("\n\n");
            AtBatSimulator currentAtBat = generateNewAtBat(this, freshCount, outs, currentBatter, lineup, runners, defense);
            displayText.append(defense.get(SCOREKEEPING_PITCHER).getName()).append(" Pitching for the ").append(fieldingTeamName).append(" : \n");
            displayText.append(lineup.get(currentBatter).getName()).append(" At Bat for the ").append(batttingTeamName).append(" Result : \n");
            int atBatResult = currentAtBat.simulateAtBat();
            int playerWhoJustHit = currentBatter;
            currentBatter = currentAtBat.getCurrentBatter();
            outs = currentAtBat.getOuts();
            if (isVisitorHitting) {
                visitorScore += currentAtBat.getRunsScored();
            } else {
                homeScore += currentAtBat.getRunsScored();
            }
            runsScoredInInning += currentAtBat.getRunsScored();

            displayText.append(getAtBatSummary()).append("\n\n");
            displayText.append("\nCurrently - Outs in Inning : ").append(outs).append(", Runs In Inning : ").append(runsScoredInInning).append("\n\n");
            if (outs == 3) {
                if (isVisitorHitting) {
                    displayText.append("--- That's the end of the top of the ").append(getInningString(inningsPlayed / 10 + 1)).append(" ---\n\n");
                } else {
                    displayText.append("--- That's the end of the bottom of the ").append(getInningString(inningsPlayed / 10 + 1)).append(" ---\n\n");
                }
                for (int i = 0; i < runners.length; i++) {
                    runners[i] = null;
                }
                outs = 0;
                switchSides = true;
            } else {
                runners = currentAtBat.getRunners();
            }


            displayText.append("Result from Result Code : ").append(getGameStateString(atBatResult)).append("\n\n");

            displayText.append("Player : ").append(lineup.get(playerWhoJustHit).getName()).append(" - batting stats this season : ").append(lineup.get(playerWhoJustHit).getBattingStats().get(0).toString());
            if (switchSides) {
                isVisitorHitting = !isVisitorHitting;
                if (isVisitorHitting) {
                    currentBatterHome = currentBatter;
                    currentBatter = currentBatterVisitor;
                    lineup = lineupVisitor;
                    defense = defenseHome;
                    batttingTeamName = visitingTeamName;
                    fieldingTeamName = homeTeamName;
                    homeTeamFinishedAtBat = true;
                } else {
                    currentBatterVisitor = currentBatter;
                    currentBatter = currentBatterHome;
                    lineup = lineupHome;
                    defense = defenseVisitor;
                    batttingTeamName = homeTeamName;
                    fieldingTeamName = visitingTeamName;
                    homeTeamFinishedAtBat = false;
                }
                runsScoredInInning = 0;
                inningsPlayed += 5;
                switchSides = false;
            }

            if (((inningsPlayed > 80 && homeScore > visitorScore) || inningsPlayed > 85) && ifScoreNotTiedAtEndOfInning(inningsPlayed, homeScore, visitorScore, homeTeamFinishedAtBat)) {
                displayText.append("\n\nGAME OVER!!!\n").append("Final Score :\n").append(homeTeamName).append(" ").append(homeScore).append(" - ")
                        .append(visitingTeamName).append(" ").append(visitorScore);
                inningsPlayed = 0;
                currentBatter = 1;
                currentBatterVisitor = 1;
                currentBatterHome = 1;
                batttingTeamName = visitingTeamName;
                fieldingTeamName = homeTeamName;
                lineup = lineupVisitor;
                defense = defenseHome;
                visitorScore = 0;
                homeScore = 0;
                isVisitorHitting = true;
                gameNotOver = false;
            }
            homeTeamFinishedAtBat = false;
            displayText.append("\n\n\n\n");

        }*/
      /*  for (Team team : teams) {
            TreeMap<Integer, Player> defense = defenseFromTeam(team);
            TreeMap<Integer, Player> lineupFromDefense = lineupFromDefense(defense, team);
            TreeMap<Integer, Player> lineup = lineupFromTeam(team);
            TreeMap<Integer, Player> defenseFromLineup = defenseFromLineup(lineup, team);
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
            displayText.append("Best Defense for Team ").append(teamNumber).append(":\n");
            for (TreeMap.Entry entry : defense.entrySet()) {
                Player player = (Player) entry.getValue();
                String position = getPositionNameFromPrimaryPosition(player.getPrimaryPosition());
                displayText.append("Pos : ").append(position).append(" - Player : ").append(player.getName()).append(" - Throws : ").append(player.getThrowingHand()).append("\n");
            }

            displayText.append("Batting Order " + teamNumber + ":\n");
            for (TreeMap.Entry entry : lineupFromDefense.entrySet()) {
                Player player = (Player) entry.getValue();
                String orderPosition = entry.getKey().toString();
                displayText.append(orderPosition + " - Player : " + player.getName() + " , Pos : " + getPositionNameFromPrimaryPosition(player.getPrimaryPosition()) + " , Bats : " + player.getHits() + "\n");
            }

            displayText.append("Best Lineup for Team " + teamNumber + ":\n");
            for (TreeMap.Entry entry : lineup.entrySet()) {
                Player player = (Player) entry.getValue();
                String orderPosition = entry.getKey().toString();
                displayText.append(orderPosition + " - Player : " + player.getName() + " , Pos : " + getPositionNameFromPrimaryPosition(player.getPrimaryPosition()) + " , Bats : " + player.getHits() + "\n");
            }

            displayText.append("Defense from Best Lineup for Team " + teamNumber + ":\n");
            for (TreeMap.Entry entry : defenseFromLineup.entrySet()) {
                Player player = (Player) entry.getValue();
                String position = getPositionNameFromPrimaryPosition(player.getPrimaryPosition());
                displayText.append("Pos : ").append(position).append(" - Player : ").append(player.getName()).append(" - Throws : ").append(player.getThrowingHand()).append("\n");
            }




                displayText.append("\n\n");
                AtBatSimulator currentAtBat = generateNewAtBat(this, freshCount, outs, currentBatter, lineup, runners, defenseFromLineup);
                displayText.append(lineup.get(currentBatter).getName()).append(" At Bat Result : \n");
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

                displayText.append("Player : " + lineup.get(playerWhoJustHit).getName() + " - batting stats this season : " + lineup.get(playerWhoJustHit).getBattingStats().get(0).toString());
                displayText.append("\n\n\n\n");



        }*/
        textView.setText(displayText);
    }

    private String getInningString(int inningsPlayed) {
        switch (inningsPlayed) {
            case 1:
                return "First";
            case 2:
                return "Second";
            case 3:
                return "Third";
            case 4:
                return "Fourth";
            case 5:
                return "Fifth";
            case 6:
                return "Sixth";
            case 7:
                return "Seventh";
            case 8:
                return "Eighth";
            case 9:
                return "Ninth";
            case 10:
                return "Tenth";
            case 11:
                return "Eleventh";
            case 12:
                return "Twelfth";
            default:
                return String.valueOf(inningsPlayed);
        }
    }

    private boolean ifScoreNotTiedAtEndOfInning(int inningsPlayed, int homeScore, int visitorScore, boolean didHomeTeamFinishAtBat) {
        //check for end of inning
        if (homeScore > visitorScore) {
            // end game
            return true;
        }
        boolean completeInning = inningsPlayed % 10 == 0;
        if (completeInning) {
            if (homeScore == visitorScore) {
                //keep playing
                return false;
            } else {
                return didHomeTeamFinishAtBat;
            }
        } else {
            return false;
        }
    }

    private String getInningsForDisplay(int inningsPlayed) {
        boolean completeInning = inningsPlayed % 10 == 0;
        if (completeInning) {
            return String.valueOf(inningsPlayed / 10);
        } else {
            return String.valueOf(inningsPlayed / 10) + " and 1/2";
        }
    }

    private AtBatSimulator generateNewAtBat(Context context, int[] freshCount, int outs, int currentBatter, TreeMap<Integer, Player> lineupFromDefense, Player[] runners, TreeMap<Integer, Player> defense) {
        return new AtBatSimulator(context, freshCount, outs, currentBatter, lineupFromDefense, runners, defense);
    }
}
