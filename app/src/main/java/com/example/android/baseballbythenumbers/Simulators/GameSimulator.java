package com.example.android.baseballbythenumbers.Simulators;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;

import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.Data.GameStates.getGameStateString;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_PITCHER;
import static com.example.android.baseballbythenumbers.Generators.LineupAndDefense.DefenseGenerator.defenseFromLineup;
import static com.example.android.baseballbythenumbers.Generators.LineupAndDefense.LineupGenerator.lineupFromTeam;
import static com.example.android.baseballbythenumbers.Simulators.AtBatSimulator.getAtBatSummary;

public class GameSimulator {

    public static final int DEFAULT_RECOVERY = 25;
    private Context context;

    private Team homeTeam;

    private Team visitingTeam;

    private boolean homeTeamManualControl;

    private boolean visitingTeamManualControl;

    private int runsScoredInInning;
    private int outs;
    private int homeScore;
    private int visitorScore;
    private int inningsPlayed;
    private int currentBatterVisitor;
    private int currentBatterHome;
    private int currentBatter;
    private boolean isVisitorHitting;
    private Player[] runners;
    private static final Player[] basesEmpty = new Player[]{null, null, null};
    private static final int[] freshCount = new int[]{0, 0};

    private TreeMap<Integer, Player> lineupHome;
    private TreeMap<Integer, Player> defenseHome;
    private TreeMap<Integer, Player> lineupVisitor;
    private TreeMap<Integer, Player> defenseVisitor;

    private TreeMap<Integer, Player> lineup;
    private TreeMap<Integer, Player> defense;
    private String visitingTeamName;
    private String homeTeamName;
    private String batttingTeamName;
    private String fieldingTeamName;

    private static StringBuilder displayText;

    public GameSimulator(Context context, Team homeTeam, boolean homeTeamManualControl, Team visitingTeam, boolean visitingTeamManualControl) {
        this.context = context;
        this.homeTeam = homeTeam;
        this.homeTeamManualControl = homeTeamManualControl;
        this.visitingTeam = visitingTeam;
        this.visitingTeamManualControl = visitingTeamManualControl;
    }

    public int[] simulateGame() {
        displayText = new StringBuilder();
        int[] finalScore = new int[2];

        doPregameSetup();

        playGame();

        finalScore[0] = homeScore;
        finalScore[1] = visitorScore;

        return finalScore;
    }

    private void playGame() {

        boolean gameNotOver = true;
        boolean homeTeamFinishedAtBat = false;
        boolean switchSides = false;
        runners = basesEmpty;

        while (gameNotOver) {
            displayText.append("\n\n");

            displayText.append("Current Score After ").append(getInningsForDisplay(inningsPlayed)).append(" Innings.\n").append(homeTeamName).append(" ").append(homeScore).append(" - ")
                    .append(visitingTeamName).append(" ").append(visitorScore);

            displayText.append("\n\n");
            AtBatSimulator currentAtBat = generateNewAtBat(context, freshCount, outs, currentBatter, lineup, runners, defense);
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
        }
    }

    private void doPregameSetup() {
        // this is a new game so players have had a chance to rest and regain some stamina
        reduceFatigueforPlayers(homeTeam);
        reduceFatigueforPlayers(visitingTeam);

        lineupHome = lineupFromTeam(homeTeam);
        defenseHome = defenseFromLineup(lineupHome, homeTeam);
        lineupVisitor = lineupFromTeam(visitingTeam);
        defenseVisitor = defenseFromLineup(lineupVisitor, visitingTeam);

        lineup = lineupVisitor;
        defense = defenseHome;

        homeTeamName = homeTeam.getTeamCity() + " " + homeTeam.getTeamName();
        visitingTeamName = visitingTeam.getTeamCity() + " " + visitingTeam.getTeamName();

        batttingTeamName = visitingTeamName;
        fieldingTeamName = homeTeamName;

        homeScore = 0;
        visitorScore = 0;

        inningsPlayed = 0;

        currentBatter = 1;
        isVisitorHitting = true;

        currentBatterHome = 1;
        currentBatterVisitor =1;
    }

    private void reduceFatigueforPlayers(Team team) {
        for (Player player : team.getPlayers()) {
            int newHittingStaminaUsed = player.getHittingPercentages().getStaminaUsed() - DEFAULT_RECOVERY;
            if (newHittingStaminaUsed < 0) {
                newHittingStaminaUsed = 0;
            }
            int newPitchingStaminaUsed = player.getPitchingPercentages().getPitchingStaminaUsed() - DEFAULT_RECOVERY;
            if (newPitchingStaminaUsed < 0) {
                newPitchingStaminaUsed = 0;
            }
            player.getHittingPercentages().setStaminaUsed(newHittingStaminaUsed);
            player.getPitchingPercentages().setPitchingStaminaUsed(newPitchingStaminaUsed);
        }

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
        String baseInningString = String.valueOf(inningsPlayed / 10);
        if (completeInning) {
            return baseInningString;
        } else {
            return baseInningString + " and 1/2";
        }
    }

    private AtBatSimulator generateNewAtBat(Context context, int[] freshCount, int outs, int currentBatter, TreeMap<Integer, Player> lineupFromDefense, Player[] runners, TreeMap<Integer, Player> defense) {
        return new AtBatSimulator(context, freshCount, outs, currentBatter, lineupFromDefense, runners, defense);
    }

    public static StringBuilder getGameRecapString()  {
        return displayText;
    }
}
