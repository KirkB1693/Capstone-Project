package com.example.android.baseballbythenumbers.Simulators;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.PitchingRotationGenerator;

import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.Data.GameStates.getGameStateString;
import static com.example.android.baseballbythenumbers.Data.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Generators.LineupAndDefense.DefenseGenerator.defenseFromLineup;
import static com.example.android.baseballbythenumbers.Generators.LineupAndDefense.LineupGenerator.lineupFromTeam;
import static com.example.android.baseballbythenumbers.Simulators.AtBatSimulator.getAtBatSummary;

public class GameSimulator {

    public static final int DEFAULT_RECOVERY = 15;
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
    private int batterStaminaAdjustment;
    private int pitcherStaminaAdjustment;
    private boolean isVisitorHitting;
    private boolean homeCloserUsed;
    private boolean visitorCloserUsed;
    private Player[] runners;
    private static final Player[] basesEmpty = new Player[]{null, null, null};
    private static final int[] freshCount = new int[]{0, 0};

    private TreeMap<Integer, Player> lineupHome;
    private TreeMap<Integer, Player> defenseHome;
    private TreeMap<Integer, Player> lineupVisitor;
    private TreeMap<Integer, Player> defenseVisitor;
    private TreeMap<Integer, Player> visitorRelievers;
    private TreeMap<Integer, Player> homeRelievers;
    private TreeMap<Integer, Player> lineup;
    private TreeMap<Integer, Player> defense;
    private String visitingTeamName;
    private String homeTeamName;
    private String batttingTeamName;
    private String fieldingTeamName;

    private static StringBuilder displayText;
    private static StringBuilder homePitchersUsed;
    private static StringBuilder visitorPitchersUsed;

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
            getBatterStaminaAdjustment(lineup.get(currentBatter));
            getPitcherStaminaAdjustment(defense.get(SCOREKEEPING_PITCHER));
            int atBatResult = currentAtBat.simulateAtBat(pitcherStaminaAdjustment, batterStaminaAdjustment);
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

            Player currentPitcher = defense.get(SCOREKEEPING_PITCHER);
            boolean didWeSwitchPitchers = shouldWeSwitchPitchers(currentPitcher);
            if (didWeSwitchPitchers) {
                if (isVisitorHitting) {
                    defense = defenseHome;
                } else {
                    defense = defenseVisitor;
                }
            }
            if (((inningsPlayed > 80 && homeScore > visitorScore) || inningsPlayed > 85) && ifScoreNotTiedAtEndOfInning(inningsPlayed, homeScore, visitorScore, homeTeamFinishedAtBat)) {
                displayText.append("\n\nGAME OVER!!!\n").append("Final Score :\n").append(homeTeamName).append(" ").append(homeScore).append(" - ")
                        .append(visitingTeamName).append(" ").append(visitorScore);
                gameNotOver = false;
            }
            homeTeamFinishedAtBat = false;
            displayText.append("\n\n\n\n");
        }
    }

    private boolean shouldWeSwitchPitchers(Player currentPitcher) {
        boolean didWeSwitch;
        int pitchingScore = visitorScore;
        int battingScore = homeScore;
        TreeMap<Integer, Player> relievers = visitorRelievers;
        boolean closerUsed = visitorCloserUsed;
        if (isVisitorHitting) {
            pitchingScore = homeScore;
            battingScore = visitorScore;
            relievers = homeRelievers;
            closerUsed = homeCloserUsed;
        }
        didWeSwitch = false;
        if (relievers.size() > 0) {
            getPitcherStaminaAdjustment(currentPitcher);
            if (pitcherStaminaAdjustment > 500) {                      // If pitcher is tired switch pitchers
                switchPitchers(currentPitcher);
                didWeSwitch = true;
            } else {                                                   // Pitcher not tired, but other reasons to switch...

                if (inningsPlayed < 50 && (battingScore-pitchingScore) > 5  && defense.get(SCOREKEEPING_PITCHER).getPrimaryPosition() == STARTING_PITCHER) {  // Starter is losing by more than 5 runs before the 5th inning
                    switchPitchers(currentPitcher);
                    didWeSwitch =true;
                } else if (inningsPlayed >= 50 && (battingScore-pitchingScore) > 2  && defense.get(SCOREKEEPING_PITCHER).getPrimaryPosition() == STARTING_PITCHER) {  // Starter is losing by more than 2 runs after the 5th inning
                    switchPitchers(currentPitcher);
                    didWeSwitch =true;
                } else if (inningsPlayed >=80 && (pitchingScore > battingScore) && (pitchingScore-battingScore <= 3) && !closerUsed) {  // After 8th in save situation, switch pitchers
                    switchPitchers(currentPitcher);
                    didWeSwitch =true;
                } else if (inningsPlayed >=60 && (pitcherStaminaAdjustment > 0)) {   // After the sixth inning make sure pitcher is not tiring
                    switchPitchers(currentPitcher);
                    didWeSwitch =true;
                } else if (inningsPlayed >= 50 && currentPitcher.getPrimaryPosition() == STARTING_PITCHER && pitcherStaminaAdjustment > 0) { // Starter has pitched more than 5 innings and is starting to tire
                    switchPitchers(currentPitcher);
                    didWeSwitch =true;
                }
            }
        }
        return didWeSwitch;
    }

    private void switchPitchers(Player currentPitcher) {
        int pitchingScore = visitorScore;
        int battingScore = homeScore;
        Team pitchingTeam = visitingTeam;
        TreeMap<Integer, Player> relievers = visitorRelievers;
        TreeMap<Integer, Player> lineupToModify = lineupVisitor;
        TreeMap<Integer, Player> defenseToModify = defenseVisitor;
        boolean closerUsed = visitorCloserUsed;
        if (isVisitorHitting) {
            pitchingScore = homeScore;
            battingScore = visitorScore;
            pitchingTeam = homeTeam;
            relievers = homeRelievers;
            lineupToModify = lineupHome;
            defenseToModify = defenseHome;
            closerUsed = homeCloserUsed;
        }
        if (inningsPlayed >=80 && pitchingScore > battingScore && (pitchingScore-battingScore <= 3) && !closerUsed) {     // Save situation = after 8 innings are played and pitching team ahead by less than 3

            Player closer =  PitchingRotationGenerator.getBestCloserAvailable(pitchingTeam);
            defenseToModify.put(SCOREKEEPING_PITCHER, closer);
            for (TreeMap.Entry<Integer, Player> entry: lineupToModify.entrySet()) {
                if (entry.getValue() == currentPitcher) {
                    lineupToModify.put(entry.getKey(),closer);
                }
            }
            if (isVisitorHitting) {
                homeCloserUsed = true;
            } else {
                visitorCloserUsed = true;
            }
            if(isVisitorHitting) {
                homePitchersUsed.append(currentPitcher.getName()).append(" Pitches Thrown when subbed : ").append(currentPitcher.getPitchingPercentages().getPitchingStaminaUsed()).append("\n").append(defenseHome.get(SCOREKEEPING_PITCHER).getName()).append("\n");
            } else {
                visitorPitchersUsed.append(currentPitcher.getName()).append(" Pitches Thrown when subbed : ").append(currentPitcher.getPitchingPercentages().getPitchingStaminaUsed()).append("\n").append(defenseVisitor.get(SCOREKEEPING_PITCHER).getName()).append("\n");
            }
            return;
        }
        int relieverChoice = checkSituationForBestRelieverToBringIn();
        if (relievers.containsKey(relieverChoice)) {

            defenseToModify.put(SCOREKEEPING_PITCHER, relievers.get(relieverChoice));
            for (TreeMap.Entry<Integer, Player> entry: lineupToModify.entrySet()) {
                if (entry.getValue() == currentPitcher) {
                    lineupToModify.put(entry.getKey(), relievers.get(relieverChoice));
                }
            }
            relievers.remove(relieverChoice);
            if(isVisitorHitting) {
                homePitchersUsed.append(currentPitcher.getName()).append(" Pitches Thrown when subbed : ").append(currentPitcher.getPitchingPercentages().getPitchingStaminaUsed())
                        .append("\n").append(defenseHome.get(SCOREKEEPING_PITCHER).getName()).append("\n");
            } else {
                visitorPitchersUsed.append(currentPitcher.getName()).append(" Pitches Thrown when subbed : ").append(currentPitcher.getPitchingPercentages().getPitchingStaminaUsed())
                        .append("\n").append(defenseVisitor.get(SCOREKEEPING_PITCHER).getName()).append("\n");
            }
            return;
        } else {
            for (TreeMap.Entry<Integer, Player> entry: relievers.entrySet()){       // put in best reliever available
                relieverChoice = entry.getKey();



                for (TreeMap.Entry<Integer, Player> lineupEntry: lineupToModify.entrySet()) {
                    if (lineupEntry.getValue() == currentPitcher) {
                        lineupToModify.put(lineupEntry.getKey(), relievers.get(relieverChoice));
                        defenseToModify.put(SCOREKEEPING_PITCHER, relievers.get(relieverChoice));
                    }
                }
                relievers.remove(relieverChoice);
                if(isVisitorHitting) {
                    homePitchersUsed.append(currentPitcher.getName()).append(" Pitches Thrown when subbed : ").append(currentPitcher.getPitchingPercentages().getPitchingStaminaUsed()).append("\n").append(defenseHome.get(SCOREKEEPING_PITCHER).getName()).append("\n");
                } else {
                    visitorPitchersUsed.append(currentPitcher.getName()).append(" Pitches Thrown when subbed : ").append(currentPitcher.getPitchingPercentages().getPitchingStaminaUsed()).append("\n").append(defenseVisitor.get(SCOREKEEPING_PITCHER).getName()).append("\n");
                }
                return;
            }
        }
    }

    private int checkSituationForBestRelieverToBringIn() {
        int pitchingScore = visitorScore;
        int battingScore = homeScore;
        TreeMap<Integer, Player> relievers = visitorRelievers;
        if (isVisitorHitting) {
            pitchingScore = homeScore;
            battingScore = visitorScore;
            relievers = homeRelievers;
        }
        int reliverToBringIn = -1;
        if (inningsPlayed < 50) {  // Things are bad bring in worst long reliever
            for (TreeMap.Entry<Integer, Player> entry: relievers.entrySet()) {
                if (entry.getValue().getPrimaryPosition() == LONG_RELIEVER) {
                    reliverToBringIn = entry.getKey();
                }
            }
            return reliverToBringIn;
        }
        int relieverPositionToStartSearch = 1;    // Best available reliever = 1
        if (pitchingScore-battingScore < -2) {
            relieverPositionToStartSearch += 2;
        }
        if (relievers.size() < relieverPositionToStartSearch) {
            relieverPositionToStartSearch = relievers.size();
        }
        int count = 0;
        if (inningsPlayed >= 70) {
            for (TreeMap.Entry<Integer, Player> entry: relievers.entrySet()) {
                count++;
                if (relieverPositionToStartSearch <= count) {
                    reliverToBringIn = entry.getKey();
                    return reliverToBringIn;
                }
            }
        }
        if (inningsPlayed >= 60) {
            relieverPositionToStartSearch++;
            for (TreeMap.Entry<Integer, Player> entry: relievers.entrySet()) {
                count++;
                if (relieverPositionToStartSearch <= count) {
                    reliverToBringIn = entry.getKey();
                    return reliverToBringIn;
                }
            }
        }
        if (inningsPlayed >= 50) {
            relieverPositionToStartSearch += 2;
            for (TreeMap.Entry<Integer, Player> entry: relievers.entrySet()) {
                count++;
                if (relieverPositionToStartSearch <= count) {
                    reliverToBringIn = entry.getKey();
                    return reliverToBringIn;
                }
            }
        }
        return reliverToBringIn;
    }

    private void getBatterStaminaAdjustment(Player batter) {
        int batterStaminaLeft = batter.getHittingPercentages().getStamina() - batter.getHittingPercentages().getStaminaUsed();
        if (batterStaminaLeft >= 0) {
            batterStaminaAdjustment = 0;
        } else {
            batterStaminaAdjustment = (Math.abs(batterStaminaLeft)/10) * 5;
        }
    }

    private void getPitcherStaminaAdjustment(Player pitcher) {
        int pitcherStaminaLeft = pitcher.getPitchingPercentages().getPitchingStamina() - pitcher.getPitchingPercentages().getPitchingStaminaUsed();
        if (pitcherStaminaLeft >= 0) {
            pitcherStaminaAdjustment = 0;
        } else {
            pitcherStaminaAdjustment = Math.abs(pitcherStaminaLeft) * 50;
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

        homeRelievers = PitchingRotationGenerator.getAvailableRelievers(homeTeam);
        visitorRelievers = PitchingRotationGenerator.getAvailableRelievers(visitingTeam);

        homeScore = 0;
        visitorScore = 0;

        inningsPlayed = 0;

        currentBatter = 1;
        isVisitorHitting = true;

        currentBatterHome = 1;
        currentBatterVisitor =1;

        homePitchersUsed = new StringBuilder();
        visitorPitchersUsed = new StringBuilder();

        homePitchersUsed.append(homeTeamName).append(" Pitchers Used : \n").append(defenseHome.get(SCOREKEEPING_PITCHER).getName()).append("\n");
        visitorPitchersUsed.append(visitingTeamName).append(" Pitchers Used : \n").append(defenseVisitor.get(SCOREKEEPING_PITCHER).getName()).append("\n");
    }

    private void reduceFatigueforPlayers(Team team) {
        for (Player player : team.getPlayers()) {
            int newHittingStaminaUsed = player.getHittingPercentages().getStaminaUsed() - DEFAULT_RECOVERY;
            if (newHittingStaminaUsed < 0) {
                newHittingStaminaUsed = 0;
            }
            int pitcherRecovery = DEFAULT_RECOVERY;
            if (player.getPrimaryPosition() == STARTING_PITCHER) {
                pitcherRecovery = 25;
            }
            int newPitchingStaminaUsed = player.getPitchingPercentages().getPitchingStaminaUsed() - pitcherRecovery;
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

    public static StringBuilder getHomePitchersUsed() {
        return homePitchersUsed;
    }

    public static StringBuilder getVisitorPitchersUsed(){
        return visitorPitchersUsed;
    }


}
