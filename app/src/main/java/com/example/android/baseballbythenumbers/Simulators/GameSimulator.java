package com.example.android.baseballbythenumbers.Simulators;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Runner;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.PitchingRotationGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.Constants.GameStates.getGameStateString;
import static com.example.android.baseballbythenumbers.Data.Player.BestOnBaseComparator;
import static com.example.android.baseballbythenumbers.Constants.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_PITCHER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.STARTING_PITCHER;
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
    private Runner[] runners;
    private static final Runner[] basesEmpty = new Runner[]{null, null, null};
    private static final int[] freshCount = new int[]{0, 0};

    private TreeMap<Integer, Player> homeLineup;
    private TreeMap<Integer, Player> homeDefense;
    private TreeMap<Integer, Player> visitingLineup;
    private TreeMap<Integer, Player> visitingDefense;
    private TreeMap<Integer, Player> visitorRelievers;
    private TreeMap<Integer, Player> homeRelievers;
    private TreeMap<Integer, Player> visitorPinchHitters;
    private TreeMap<Integer, Player> homePinchHitters;
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
        boolean areRunsEarned = true;
        int errorsMade = 0;
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
            Player playerWePinchHitFor = shouldWePinchHit(lineup.get(currentBatter));
            int atBatResult = currentAtBat.simulateAtBat(pitcherStaminaAdjustment, batterStaminaAdjustment, areRunsEarned);
            if (playerWePinchHitFor != null) {
                if (isPitcher(playerWePinchHitFor)) {
                    isVisitorHitting = !isVisitorHitting;             // switchPitchers assumes we change the pitcher on defense so change the hitting team temporarily
                    if (!isVisitorHitting) {
                        switchPitchers(visitingDefense.get(SCOREKEEPING_PITCHER));
                    } else {
                        switchPitchers(homeDefense.get(SCOREKEEPING_PITCHER));
                    }
                    isVisitorHitting = !isVisitorHitting;             // now that we have subbed change the hitting team back to the correct team
                }
            }
            int playerWhoJustHit = currentBatter;
            currentBatter = currentAtBat.getCurrentBatter();
            outs = currentAtBat.getOuts();
            if (currentAtBat.wasErrorMade()) {
                errorsMade++;
                if ((outs + errorsMade) >= 3) {
                    areRunsEarned = false;
                }
            }
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
                    lineup = visitingLineup;
                    defense = homeDefense;
                    batttingTeamName = visitingTeamName;
                    fieldingTeamName = homeTeamName;
                    homeTeamFinishedAtBat = true;
                } else {
                    currentBatterVisitor = currentBatter;
                    currentBatter = currentBatterHome;
                    lineup = homeLineup;
                    defense = visitingDefense;
                    batttingTeamName = homeTeamName;
                    fieldingTeamName = visitingTeamName;
                    homeTeamFinishedAtBat = false;
                }
                errorsMade = 0;
                areRunsEarned = true;
                runsScoredInInning = 0;
                inningsPlayed += 5;
                switchSides = false;
            }

            Player currentPitcher = defense.get(SCOREKEEPING_PITCHER);
            boolean didWeSwitchPitchers = shouldWeSwitchPitchers(currentPitcher);
            if (didWeSwitchPitchers) {
                if (isVisitorHitting) {
                    defense = homeDefense;
                } else {
                    defense = visitingDefense;
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

    private Player shouldWePinchHit(Player player) {
        TreeMap<Integer, Player> lineupToChange = homeLineup;
        TreeMap<Integer, Player> defenseToChange = homeDefense;
        int runDifferential = homeScore - visitorScore;
        if (isVisitorHitting) {
            lineupToChange = visitingLineup;
            defenseToChange = visitingDefense;
            runDifferential = visitorScore - homeScore;
        }
        if (inningsPlayed >= 50 && runDifferential < 0 && isPitcher(player)) {
            return pinchHitForPitcher(player, lineupToChange, defenseToChange);
        }

        if (inningsPlayed >= 50 && runDifferential < 10 && isReliever(player)) {
            return pinchHitForPitcher(player, lineupToChange, defenseToChange);
        }

        if (inningsPlayed >= 60 && runDifferential < 2 && isPitcher(player)) {
            return pinchHitForPitcher(player, lineupToChange, defenseToChange);
        }

        if (inningsPlayed >= 70 && runDifferential < 4 && isPitcher(player)) {
            return pinchHitForPitcher(player, lineupToChange, defenseToChange);
        }

        if (inningsPlayed >=80 && isPitcher(player) && isPitcher(player)) {
            return pinchHitForPitcher(player, lineupToChange, defenseToChange);
        }

        if (inningsPlayed >=90 && player.getPrimaryPosition() == STARTING_PITCHER) {
            return pinchHitForPitcher(player, lineupToChange, defenseToChange);
        }
        return null;
    }

    private boolean isReliever(Player player) {
        return player.getPrimaryPosition() == LONG_RELIEVER || player.getPrimaryPosition() == SHORT_RELEIVER;
    }

    private Player pinchHitForPitcher(Player pitcher, TreeMap<Integer, Player> lineupToChange, TreeMap<Integer, Player> defenseToChange) {
        Player pinchHitter = getBestPinchHitter();
        if (pinchHitter == null) { return null; }
        lineupToChange.put(currentBatter, pinchHitter);
        defenseToChange.put(SCOREKEEPING_PITCHER, pinchHitter);
        lineup = lineupToChange;
        addPinchHitterForPitcherToLog(pitcher, pinchHitter);
        return pitcher;
    }

    private void addPinchHitterForPitcherToLog(Player pitcher, Player pinchHitter) {
        if (isVisitorHitting) {
            visitorPitchersUsed.append(pitcher.getName()).append(" Pitches Thrown when subbed : ").append(pitcher.getPitchingPercentages().getPitchingStaminaUsed()).append("\n")
                    .append(pinchHitter.getName()).append(" pinch hits in the ").append(getInningString(inningsPlayed / 10 + 1)).append("\n");
        } else {
            homePitchersUsed.append(pitcher.getName()).append(" Pitches Thrown when subbed : ").append(pitcher.getPitchingPercentages().getPitchingStaminaUsed()).append("\n")
                    .append(pinchHitter.getName()).append(" pinch hits in the ").append(getInningString(inningsPlayed / 10 + 1)).append("\n");
        }
    }

    private Player getBestPinchHitter() {
        Player pinchHitter;
        int pinchHitterKey = -1;
        TreeMap<Integer, Player> pinchHittersAvailable = homePinchHitters;
        if (isVisitorHitting) {
            pinchHittersAvailable = visitorPinchHitters;
        }
        if (pinchHittersAvailable.size() == 0) {
            return null;
        }
        if (runnerInScoringPosition()) {
            while (pinchHitterKey == -1) {
                    if (pinchHittersAvailable.ceilingKey(1) != null) {
                        pinchHitterKey = pinchHittersAvailable.ceilingKey(1);               // Get best available pinchhitter
                }
            }
            pinchHitter = pinchHittersAvailable.get(pinchHitterKey);
            pinchHittersAvailable.remove(pinchHitterKey);                    // Take pinch hitter out of available list
            return pinchHitter;
        }

        if (inningsPlayed<=70) {
            int pinchHitterPosition = 5;
            while (pinchHitterKey == -1) {
                if (pinchHittersAvailable.ceilingKey(pinchHitterPosition) != null) {
                    pinchHitterKey = pinchHittersAvailable.ceilingKey(pinchHitterPosition);               // Get a lower ranked pinchhitter
                }
                pinchHitterPosition--;
            }
            pinchHitter = pinchHittersAvailable.get(pinchHitterKey);
            pinchHittersAvailable.remove(pinchHitterKey);                    // Take pinch hitter out of available list
            return pinchHitter;
        } else {
            int pinchHitterPosition = 2;
            while (pinchHitterKey == -1) {
                if (pinchHittersAvailable.ceilingKey(pinchHitterPosition) != null) {
                    pinchHitterKey = pinchHittersAvailable.ceilingKey(pinchHitterPosition);               // Get second best available pinchhitter
                }
                pinchHitterPosition--;
            }
            pinchHitter = pinchHittersAvailable.get(pinchHitterKey);
            pinchHittersAvailable.remove(pinchHitterKey);                    // Take pinch hitter out of available list
            return pinchHitter;
        }
    }

    private boolean isPitcher(Player player) {
        if (player.getPrimaryPosition() == STARTING_PITCHER || player.getPrimaryPosition() == LONG_RELIEVER || player.getPrimaryPosition() == SHORT_RELEIVER) {
            return true;
        } else {
            return false;
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

                if (inningsPlayed < 50 && (battingScore - pitchingScore) > 5 && defense.get(SCOREKEEPING_PITCHER).getPrimaryPosition() == STARTING_PITCHER) {  // Starter is losing by more than 5 runs before the 5th inning
                    switchPitchers(currentPitcher);
                    didWeSwitch = true;
                } else if (inningsPlayed >= 50 && (battingScore - pitchingScore) > 2 && defense.get(SCOREKEEPING_PITCHER).getPrimaryPosition() == STARTING_PITCHER) {  // Starter is losing by more than 2 runs after the 5th inning
                    switchPitchers(currentPitcher);
                    didWeSwitch = true;
                } else if (inningsPlayed >= 80 && (pitchingScore > battingScore) && (pitchingScore - battingScore <= 3) && !closerUsed) {  // After 8th in save situation, switch pitchers
                    switchPitchers(currentPitcher);
                    didWeSwitch = true;
                } else if (inningsPlayed >= 60 && (pitcherStaminaAdjustment > 0)) {   // After the sixth inning make sure pitcher is not tiring
                    switchPitchers(currentPitcher);
                    didWeSwitch = true;
                } else if (inningsPlayed >= 50 && currentPitcher.getPrimaryPosition() == STARTING_PITCHER && pitcherStaminaAdjustment > 0) { // Starter has pitched more than 5 innings and is starting to tire
                    switchPitchers(currentPitcher);
                    didWeSwitch = true;
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
        TreeMap<Integer, Player> lineupToModify = visitingLineup;
        TreeMap<Integer, Player> defenseToModify = visitingDefense;
        boolean closerUsed = visitorCloserUsed;
        if (isVisitorHitting) {
            pitchingScore = homeScore;
            battingScore = visitorScore;
            pitchingTeam = homeTeam;
            relievers = homeRelievers;
            lineupToModify = homeLineup;
            defenseToModify = homeDefense;
            closerUsed = homeCloserUsed;
        }
        if (inningsPlayed >= 80 && pitchingScore > battingScore && (pitchingScore - battingScore <= 3) && !closerUsed) {     // Save situation = after 8 innings are played and pitching team ahead by less than 3

            Player closer = PitchingRotationGenerator.getBestCloserAvailable(pitchingTeam);
            defenseToModify.put(SCOREKEEPING_PITCHER, closer);
            for (TreeMap.Entry<Integer, Player> entry : lineupToModify.entrySet()) {
                if (entry.getValue() == currentPitcher) {
                    lineupToModify.put(entry.getKey(), closer);
                }
            }
            if (isVisitorHitting) {
                homeCloserUsed = true;
            } else {
                visitorCloserUsed = true;
            }
            addPitchingChangeToLog(currentPitcher, defenseToModify.get(SCOREKEEPING_PITCHER));
            return;
        }
        int relieverChoice = checkSituationForBestRelieverToBringIn();
        if (relievers.containsKey(relieverChoice)) {

            defenseToModify.put(SCOREKEEPING_PITCHER, relievers.get(relieverChoice));
            for (TreeMap.Entry<Integer, Player> entry : lineupToModify.entrySet()) {
                if (entry.getValue() == currentPitcher) {
                    lineupToModify.put(entry.getKey(), relievers.get(relieverChoice));
                }
            }
            relievers.remove(relieverChoice);
            addPitchingChangeToLog(currentPitcher, defenseToModify.get(SCOREKEEPING_PITCHER));
            return;
        } else {
            for (TreeMap.Entry<Integer, Player> entry : relievers.entrySet()) {       // put in best reliever available
                relieverChoice = entry.getKey();


                for (TreeMap.Entry<Integer, Player> lineupEntry : lineupToModify.entrySet()) {
                    if (lineupEntry.getValue() == currentPitcher) {
                        lineupToModify.put(lineupEntry.getKey(), relievers.get(relieverChoice));
                        defenseToModify.put(SCOREKEEPING_PITCHER, relievers.get(relieverChoice));
                    }
                }
                relievers.remove(relieverChoice);
                addPitchingChangeToLog(currentPitcher, defenseToModify.get(SCOREKEEPING_PITCHER));
                return;
            }
        }
    }

    private void addPitchingChangeToLog(Player oldPitcher, Player newPitcher) {
        if (isPitcher(oldPitcher)) {
            if (isVisitorHitting) {
                homePitchersUsed.append(oldPitcher.getName()).append(" Pitches Thrown when subbed : ").append(oldPitcher.getPitchingPercentages().getPitchingStaminaUsed()).append("\n")
                        .append(newPitcher.getName()).append(" enters the game in the ").append(getInningString(inningsPlayed / 10 + 1)).append("\n");
            } else {
                visitorPitchersUsed.append(oldPitcher.getName()).append(" Pitches Thrown when subbed : ").append(oldPitcher.getPitchingPercentages().getPitchingStaminaUsed()).append("\n")
                        .append(newPitcher.getName()).append(" enters the game in the ").append(getInningString(inningsPlayed / 10 + 1)).append("\n");
            }
        } else {
            if (isVisitorHitting) {
                homePitchersUsed.append(oldPitcher.getName()).append(" was a Pinch Hitter ").append("\n")
                        .append(newPitcher.getName()).append(" enters the game in the ").append(getInningString(inningsPlayed / 10 + 1)).append("\n");
            } else {
                visitorPitchersUsed.append(oldPitcher.getName()).append(" was a Pinch Hitter ").append("\n")
                        .append(newPitcher.getName()).append(" enters the game in the ").append(getInningString(inningsPlayed / 10 + 1)).append("\n");
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
            for (TreeMap.Entry<Integer, Player> entry : relievers.entrySet()) {
                if (entry.getValue().getPrimaryPosition() == LONG_RELIEVER) {
                    reliverToBringIn = entry.getKey();
                }
            }
            return reliverToBringIn;
        }
        int relieverPositionToStartSearch = 1;    // Best available reliever = 1
        if (pitchingScore - battingScore < -2) {
            relieverPositionToStartSearch += 2;
        }
        if (relievers.size() < relieverPositionToStartSearch) {
            relieverPositionToStartSearch = relievers.size();
        }
        int count = 0;
        if (inningsPlayed >= 70) {
            for (TreeMap.Entry<Integer, Player> entry : relievers.entrySet()) {
                count++;
                if (relieverPositionToStartSearch <= count) {
                    reliverToBringIn = entry.getKey();
                    return reliverToBringIn;
                }
            }
        }
        if (inningsPlayed >= 60) {
            relieverPositionToStartSearch++;
            for (TreeMap.Entry<Integer, Player> entry : relievers.entrySet()) {
                count++;
                if (relieverPositionToStartSearch <= count) {
                    reliverToBringIn = entry.getKey();
                    return reliverToBringIn;
                }
            }
        }
        if (inningsPlayed >= 50) {
            relieverPositionToStartSearch += 2;
            for (TreeMap.Entry<Integer, Player> entry : relievers.entrySet()) {
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
            batterStaminaAdjustment = (Math.abs(batterStaminaLeft) / 10) * 5;
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

        homeLineup = lineupFromTeam(homeTeam, homeTeam.isUseDh());
        homeDefense = defenseFromLineup(homeLineup, homeTeam, homeTeam.isUseDh());
        visitingLineup = lineupFromTeam(visitingTeam, homeTeam.isUseDh());              // Home team determines if DH is used for both teams
        visitingDefense = defenseFromLineup(visitingLineup, visitingTeam, homeTeam.isUseDh());
        visitorPinchHitters = getPinchHitters(visitingLineup, visitingTeam);
        homePinchHitters = getPinchHitters(homeLineup, homeTeam);

        lineup = visitingLineup;
        defense = homeDefense;

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
        currentBatterVisitor = 1;

        homePitchersUsed = new StringBuilder();
        visitorPitchersUsed = new StringBuilder();

        homePitchersUsed.append(homeTeamName).append(" Pitchers Used : \n").append(homeDefense.get(SCOREKEEPING_PITCHER).getName()).append("\n");
        visitorPitchersUsed.append(visitingTeamName).append(" Pitchers Used : \n").append(visitingDefense.get(SCOREKEEPING_PITCHER).getName()).append("\n");
    }

    private TreeMap<Integer, Player> getPinchHitters(TreeMap<Integer, Player> currentLineup, Team currentTeam) {
        List<Player> possiblePinchHitters = new ArrayList<>();
        for (Player player : currentTeam.getPlayers()) {
            if (!isPitcher(player)) {
                if (hitterNotTired(player) && !currentLineup.containsValue(player))
                    possiblePinchHitters.add(player);
            }
        }

        Collections.sort(possiblePinchHitters, BestOnBaseComparator);
        TreeMap<Integer, Player> pinchHittersAvailable = new TreeMap<>();
        for (int i = 0; i < possiblePinchHitters.size(); i++) {
            pinchHittersAvailable.put(i + 1, possiblePinchHitters.get(i));
        }
        return pinchHittersAvailable;
    }

    private boolean runnerInScoringPosition() {
        return runners[1] != null || runners[2] != null;
    }

    private boolean hitterNotTired(Player player) {
        int staminaLeft = player.getHittingPercentages().getStamina() - player.getHittingPercentages().getStaminaUsed();
        if (staminaLeft < 0) {
            return false;
        } else {
            return true;
        }

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

    private AtBatSimulator generateNewAtBat(Context context, int[] freshCount, int outs, int currentBatter, TreeMap<Integer, Player> lineupFromDefense, Runner[] runners, TreeMap<Integer, Player> defense) {
        return new AtBatSimulator(context, freshCount, outs, currentBatter, lineupFromDefense, runners, defense);
    }

    public static StringBuilder getGameRecapString() {
        return displayText;
    }

    public static StringBuilder getHomePitchersUsed() {
        return homePitchersUsed;
    }

    public static StringBuilder getVisitorPitchersUsed() {
        return visitorPitchersUsed;
    }


}
