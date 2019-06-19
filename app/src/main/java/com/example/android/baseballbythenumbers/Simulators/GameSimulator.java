package com.example.android.baseballbythenumbers.Simulators;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.example.android.baseballbythenumbers.Data.BattingLine;
import com.example.android.baseballbythenumbers.Data.BoxScore;
import com.example.android.baseballbythenumbers.Data.Game;
import com.example.android.baseballbythenumbers.Data.PitchingLine;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Runner;
import com.example.android.baseballbythenumbers.Data.Team;
import com.example.android.baseballbythenumbers.Generators.LineupAndDefense.PitchingRotationGenerator;
import com.example.android.baseballbythenumbers.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.example.android.baseballbythenumbers.Constants.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_PITCHER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.STARTING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Player.BestOnBaseComparator;
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
    private Player pitcherOfRecordForWin;
    private Player pitcherOfRecordForLoss;
    private boolean isHomeTeamWinning;
    private boolean isVisitingTeamWinning;

    private static SpannableStringBuilder gameLog;
    private static StringBuilder homePitchersUsed;
    private static StringBuilder visitorPitchersUsed;

    private Game game;

    public GameSimulator(Context context, Game game, Team homeTeam, boolean homeTeamManualControl, Team visitingTeam, boolean visitingTeamManualControl) {
        this.context = context;
        this.game = game;
        this.homeTeam = homeTeam;
        this.homeTeamManualControl = homeTeamManualControl;
        this.visitingTeam = visitingTeam;
        this.visitingTeamManualControl = visitingTeamManualControl;
    }

    public int[] simulateGame() {
        gameLog = new SpannableStringBuilder();
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

        int start = gameLog.length();
        gameLog.append("                             --- ").append(getInningString(inningsPlayed / 10 + 1)).append(" Inning ---                             \n\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gameLog.setSpan(new BackgroundColorSpan(context.getColor(R.color.colorPrimaryDark)), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            gameLog.setSpan(new ForegroundColorSpan(context.getColor(R.color.white)),start,gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        gameLog.setSpan(new StyleSpan(Typeface.BOLD),start,gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gameLog.append(defense.get(SCOREKEEPING_PITCHER).getName()).append(" Pitching for the ").append(fieldingTeamName).append(" : \n\n");
        gameLog.append(batttingTeamName).append(" now at bat : ");
        while (gameNotOver) {
            gameLog.append("\n\n");


            AtBatSimulator currentAtBat = generateNewAtBat(context, freshCount, outs, currentBatter, lineup, runners, defense);

            gameLog.append(lineup.get(currentBatter).getName()).append(" : ");
            getBatterStaminaAdjustment(lineup.get(currentBatter));
            getPitcherStaminaAdjustment(defense.get(SCOREKEEPING_PITCHER));
            Player pitcherOfRecordForHittingTeam;
            if (isVisitorHitting) {
                pitcherOfRecordForHittingTeam = visitingDefense.get(SCOREKEEPING_PITCHER);
            } else {
                pitcherOfRecordForHittingTeam = homeDefense.get(SCOREKEEPING_PITCHER);
            }
            Player playerWePinchHitFor = shouldWePinchHit(lineup.get(currentBatter));
            int atBatResult = currentAtBat.simulateAtBat(pitcherStaminaAdjustment, batterStaminaAdjustment, areRunsEarned);
            gameLog.append(getAtBatSummary());

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

            if(homeScore == visitorScore) {
                pitcherOfRecordForWin = null;
                pitcherOfRecordForLoss = null;
                isHomeTeamWinning = false;
                isVisitingTeamWinning = false;
            } else {
                List<Player> pitchersToCheck = currentAtBat.getPitchersResponsible();
                if (homeScore > visitorScore && !isHomeTeamWinning){                                     // did lead change
                    pitcherOfRecordForLoss = pitchersToCheck.get((homeScore-visitorScore) - 1);
                    pitcherOfRecordForWin = pitcherOfRecordForHittingTeam;
                    isHomeTeamWinning = true;
                    isVisitingTeamWinning = false;
                }
                if (visitorScore > homeScore && !isVisitingTeamWinning){                                // did lead change
                    pitcherOfRecordForLoss = pitchersToCheck.get((visitorScore-homeScore) - 1);
                    pitcherOfRecordForWin = pitcherOfRecordForHittingTeam;
                    isVisitingTeamWinning = true;
                    isHomeTeamWinning = false;
                }

            }



            // gameLog.append("Currently - Outs in Inning : ").append(Integer.toString(outs)).append(", Runs In Inning : ").append(Integer.toString(runsScoredInInning)).append("\n\n");
            if (outs == 3) {
                //gameLog.append("Current Score After ").append(getInningsForDisplay(inningsPlayed)).append(" Innings.\n").append(homeTeamName).append(" ").append(Integer.toString(homeScore)).append(" - ").append(visitingTeamName).append(" ").append(Integer.toString(visitorScore));

                gameLog.append("\n\n");
                start = gameLog.length();
                if (isVisitorHitting) {
                    gameLog.append("--- That's the end of the top of the ").append(getInningString(inningsPlayed / 10 + 1)).append(" ---\n\n");
                    gameLog.setSpan(new StyleSpan(Typeface.BOLD),start,gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    gameLog.append("--- That's the end of the bottom of the ").append(getInningString(inningsPlayed / 10 + 1)).append(" ---\n\n");
                    gameLog.setSpan(new StyleSpan(Typeface.BOLD),start,gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                for (int i = 0; i < runners.length; i++) {
                    runners[i] = null;
                }
                outs = 0;
                switchSides = true;
            } else {
                runners = currentAtBat.getRunners();
            }


            // gameLog.append("Result from Result Code : ").append(getGameStateString(atBatResult)).append("\n\n");

            // gameLog.append("Player : ").append(lineup.get(playerWhoJustHit).getName()).append(" - batting stats this season : ").append(lineup.get(playerWhoJustHit).getBattingStats().get(0).toString());
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
                if (!(((inningsPlayed > 80 && homeScore > visitorScore) || inningsPlayed > 85) && ifScoreNotTiedAtEndOfInning(inningsPlayed, homeScore, visitorScore, homeTeamFinishedAtBat))) {
                    start = gameLog.length();
                    if (homeTeamFinishedAtBat) {
                        gameLog.append("                             --- ").append(getInningString(inningsPlayed / 10 + 1)).append(" Inning ---                             \n\n");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            gameLog.setSpan(new BackgroundColorSpan(context.getColor(R.color.colorPrimaryDark)), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            gameLog.setSpan(new ForegroundColorSpan(context.getColor(R.color.white)), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        gameLog.setSpan(new StyleSpan(Typeface.BOLD), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (!isPitcher(defense.get(SCOREKEEPING_PITCHER))) {     // if we pinch hit for pitcher with a position player, we need a new pitcher
                        switchPitchers(defense.get(SCOREKEEPING_PITCHER));
                    }
                    gameLog.append(defense.get(SCOREKEEPING_PITCHER).getName()).append(" Pitching for the ").append(fieldingTeamName).append(" : \n\n");
                    gameLog.append(batttingTeamName).append(" now at bat : ");
                }
            }


            if (((inningsPlayed > 80 && homeScore > visitorScore) || inningsPlayed > 85) && ifScoreNotTiedAtEndOfInning(inningsPlayed, homeScore, visitorScore, homeTeamFinishedAtBat)) {
                homePitchersUsed.append(homeDefense.get(SCOREKEEPING_PITCHER).getName()).append(" pitches thrown : ").append(homeDefense.get(SCOREKEEPING_PITCHER).getPitchingPercentages().getPitchingStaminaUsed()).append("\n");
                visitorPitchersUsed.append(visitingDefense.get(SCOREKEEPING_PITCHER).getName()).append(" pitches thrown : ").append(visitingDefense.get(SCOREKEEPING_PITCHER).getPitchingPercentages().getPitchingStaminaUsed()).append("\n");
                start = gameLog.length();
                gameLog.append("\n\n                             --- GAME OVER!!! ---                             \n\n");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    gameLog.setSpan(new BackgroundColorSpan(context.getColor(R.color.colorPrimaryDark)), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    gameLog.setSpan(new ForegroundColorSpan(context.getColor(R.color.white)),start,gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                gameLog.setSpan(new StyleSpan(Typeface.BOLD),start,gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                gameLog.append("Final Score :\n").append(homeTeamName).append(" ").append(Integer.toString(homeScore)).append(" - ")
                        .append(visitingTeamName).append(" ").append(Integer.toString(visitorScore));
                pitcherOfRecordForWin.getPitchingStats().get(0).setWins(pitcherOfRecordForWin.getPitchingStats().get(0).getWins() + 1);
                pitcherOfRecordForLoss.getPitchingStats().get(0).setLosses(pitcherOfRecordForLoss.getPitchingStats().get(0).getLosses() + 1);
                if (homeScore > visitorScore) {
                    gameLog.append("\nWP : ").append(pitcherOfRecordForWin.getName()).append(" (").append(Integer.toString(pitcherOfRecordForWin.getPitchingStats().get(0).getWins())).append("-")
                            .append(Integer.toString(pitcherOfRecordForWin.getPitchingStats().get(0).getLosses())).append(") - LP : ")
                            .append(pitcherOfRecordForLoss.getName()).append(" (").append(Integer.toString(pitcherOfRecordForLoss.getPitchingStats().get(0).getWins())).append("-")
                            .append(Integer.toString(pitcherOfRecordForLoss.getPitchingStats().get(0).getLosses())).append(")");
                    homeTeam.setWins(homeTeam.getWins() + 1);
                    visitingTeam.setLosses(visitingTeam.getLosses() + 1);
                } else {
                    gameLog.append("\nLP : ").append(pitcherOfRecordForLoss.getName()).append(" (").append(Integer.toString(pitcherOfRecordForLoss.getPitchingStats().get(0).getWins())).append("-")
                            .append(Integer.toString(pitcherOfRecordForLoss.getPitchingStats().get(0).getLosses())).append(") - WP : ")
                            .append(pitcherOfRecordForWin.getName()).append(" (").append(Integer.toString(pitcherOfRecordForWin.getPitchingStats().get(0).getWins())).append("-")
                            .append(Integer.toString(pitcherOfRecordForWin.getPitchingStats().get(0).getLosses())).append(")");
                    visitingTeam.setWins(visitingTeam.getWins() + 1);
                    homeTeam.setLosses(homeTeam.getLosses() + 1);
                }

                gameNotOver = false;
            }

            if (gameNotOver) {
                Player currentPitcher = defense.get(SCOREKEEPING_PITCHER);
                boolean didWeSwitchPitchers = shouldWeSwitchPitchers(currentPitcher);
                if (didWeSwitchPitchers) {
                    if (isVisitorHitting) {
                        defense = homeDefense;
                    } else {
                        defense = visitingDefense;
                    }
                }
            }
            homeTeamFinishedAtBat = false;
            // gameLog.append("\n\n");
        }
        game.setGameLog(String.valueOf(gameLog));
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
        if (isPitcher(player)) {
            // if after the fifth inning and losing pinch hit
            if (inningsPlayed >= 50 && runDifferential < 0) {
                return pinchHitForPitcher(player, lineupToChange, defenseToChange);
            }
            // if after the fifth inning and winning by less than 10 and reliever is pitching
            if (inningsPlayed >= 50 && runDifferential < 10 && isReliever(player)) {
                return pinchHitForPitcher(player, lineupToChange, defenseToChange);
            }
            // if after the sixth inning and winning by less than 2
            if (inningsPlayed >= 60 && runDifferential < 2) {
                return pinchHitForPitcher(player, lineupToChange, defenseToChange);
            }
            // if after the seventh inning and winning by less than 4 (save/hold situation)
            if (inningsPlayed >= 70 && runDifferential < 4) {
                return pinchHitForPitcher(player, lineupToChange, defenseToChange);
            }
            // if after the eighth inning and pitcher is up
            if (inningsPlayed >=80) {
                return pinchHitForPitcher(player, lineupToChange, defenseToChange);
            }
            // if after the ninth inning and starter is still in the game
            if (inningsPlayed >=90 && player.getPrimaryPosition() == STARTING_PITCHER) {
                return pinchHitForPitcher(player, lineupToChange, defenseToChange);
            }
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
        int start = gameLog.length();
        gameLog.append("\n\nNow Pinch Hitting for ").append(pitcher.getName()).append(" : \n\n").append(pinchHitter.getName()).append(" : ");
        formatSubstitution(start);
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
                if (currentPitcher.getPrimaryPosition() == STARTING_PITCHER && inningsPlayed < 50 && pitchingScore > battingScore) {
                    if (isVisitorHitting) {
                        pitcherOfRecordForWin = homeDefense.get(SCOREKEEPING_PITCHER);
                    } else {
                        pitcherOfRecordForWin = visitingDefense.get(SCOREKEEPING_PITCHER);
                    }
                }
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
        int start = gameLog.length();
        gameLog.append("\n\nNow Pitching : ").append(newPitcher.getName()).append(" ");
        formatSubstitution(start);
    }

    private void formatSubstitution(int start) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gameLog.setSpan(new ForegroundColorSpan(context.getColor(R.color.colorPrimary)), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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

        isHomeTeamWinning = false;
        isVisitingTeamWinning = false;

        homePitchersUsed = new StringBuilder();
        visitorPitchersUsed = new StringBuilder();

        homePitchersUsed.append(homeTeamName).append(" Pitchers Used : \n").append(homeDefense.get(SCOREKEEPING_PITCHER).getName()).append("\n");
        visitorPitchersUsed.append(visitingTeamName).append(" Pitchers Used : \n").append(visitingDefense.get(SCOREKEEPING_PITCHER).getName()).append("\n");

        game.setVisitorScore(visitorScore);
        game.setHomeScore(homeScore);
        game.setVisitorBoxScore(new BoxScore(game.getGameId()));
        game.setHomeBoxScore(new BoxScore(game.getGameId()));
        initializeBoxScore(game.getHomeBoxScore(), homeLineup, homeDefense);
        initializeBoxScore(game.getVisitorBoxScore(), visitingLineup, visitingDefense);
        game.setGameLog("");
        game.setPlayedGame(false);
    }

    private void initializeBoxScore(BoxScore boxScore, TreeMap<Integer, Player> lineup, TreeMap<Integer, Player> defense) {
        for (TreeMap.Entry entry: lineup.entrySet()) {
            Player player = (Player) entry.getValue();
            boxScore.addBattingLine(new BattingLine(boxScore.getBoxScoreId(), (int) entry.getKey(), false, player.getName()));
        }
        boxScore.addPitchingLine(new PitchingLine(boxScore.getBoxScoreId(),1,defense.get(SCOREKEEPING_PITCHER).getName()));
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

    public static SpannableStringBuilder getGameRecapString() {
        return gameLog;
    }

    public static StringBuilder getHomePitchersUsed() {
        return homePitchersUsed;
    }

    public static StringBuilder getVisitorPitchersUsed() {
        return visitorPitchersUsed;
    }


    public Player getPitcherOfRecordForWin() {
        return pitcherOfRecordForWin;
    }

    public Player getPitcherOfRecordForLoss() {
        return pitcherOfRecordForLoss;
    }
}
