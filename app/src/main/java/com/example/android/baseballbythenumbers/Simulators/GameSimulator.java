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
    private Player pitcherOfRecordForSave;
    private int visitorSubstituteNumber;
    private int homeSubstituteNumber;
    private int homePitcherNumber;
    private int visitorPitcherNumber;

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
            gameLog.setSpan(new ForegroundColorSpan(context.getColor(R.color.white)), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        gameLog.setSpan(new StyleSpan(Typeface.BOLD), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gameLog.append(defense.get(SCOREKEEPING_PITCHER).getName()).append(" Pitching for the ").append(fieldingTeamName).append(" : \n\n");
        gameLog.append(batttingTeamName).append(" now at bat : ");
        while (gameNotOver) {
            gameLog.append("\n\n");


            AtBatSimulator currentAtBat = generateNewAtBat(context, freshCount, outs, currentBatter, lineup, runners, defense, getBattingLineForCurrentBatter(), getPitchingLineForCurrentPitcher());

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

            if (homeScore == visitorScore) {
                pitcherOfRecordForWin = null;
                pitcherOfRecordForLoss = null;
                checkForBlownSave();
                pitcherOfRecordForSave = null;
                isHomeTeamWinning = false;
                isVisitingTeamWinning = false;
            } else {
                List<Player> pitchersToCheck = currentAtBat.getPitchersResponsible();
                if (homeScore > visitorScore && !isHomeTeamWinning) {                                     // did lead change
                    pitcherOfRecordForLoss = pitchersToCheck.get((homeScore - visitorScore) - 1);
                    pitcherOfRecordForWin = pitcherOfRecordForHittingTeam;
                    checkForBlownSave();
                    pitcherOfRecordForSave = null;
                    isHomeTeamWinning = true;
                    isVisitingTeamWinning = false;
                }
                if (visitorScore > homeScore && !isVisitingTeamWinning) {                                // did lead change
                    pitcherOfRecordForLoss = pitchersToCheck.get((visitorScore - homeScore) - 1);
                    pitcherOfRecordForWin = pitcherOfRecordForHittingTeam;
                    checkForBlownSave();
                    pitcherOfRecordForSave = null;
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
                    gameLog.setSpan(new StyleSpan(Typeface.BOLD), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    gameLog.append("--- That's the end of the bottom of the ").append(getInningString(inningsPlayed / 10 + 1)).append(" ---\n\n");
                    gameLog.setSpan(new StyleSpan(Typeface.BOLD), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                updateEndOfGameStats();
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

    private PitchingLine getPitchingLineForCurrentPitcher() {
        String currentPitcherName = defense.get(SCOREKEEPING_PITCHER).getName();
        List<PitchingLine> pitchingLines;
        if (isVisitorHitting) {
            pitchingLines = game.getHomeBoxScore().getPitchingLines();
        } else {
            pitchingLines = game.getVisitorBoxScore().getPitchingLines();
        }
        for (PitchingLine pitchingLine: pitchingLines) {
            if (pitchingLine.getPitcherName().equals(currentPitcherName)) {
                return pitchingLine;
            }
        }
        return null;
    }

    private BattingLine getBattingLineForCurrentBatter() {
        String currentBatterName = lineup.get(currentBatter).getName();
        List<BattingLine> battingLines;
        if (isVisitorHitting) {
            battingLines = game.getVisitorBoxScore().getBattingLines();
        } else {
            battingLines = game.getHomeBoxScore().getBattingLines();
        }
        for (BattingLine battingline: battingLines) {
            if (battingline.getBatterName().equals(currentBatterName)) {
                return battingline;
            }
        }
        return null;                            //  This should never happen, if batter is in lineup they should have a batting line
    }

    private void updateEndOfGameStats() {
        int start;
        homePitchersUsed.append(homeDefense.get(SCOREKEEPING_PITCHER).getName()).append(" pitches thrown : ").append(homeDefense.get(SCOREKEEPING_PITCHER).getPitchingPercentages().getPitchingStaminaUsed()).append("\n");
        visitorPitchersUsed.append(visitingDefense.get(SCOREKEEPING_PITCHER).getName()).append(" pitches thrown : ").append(visitingDefense.get(SCOREKEEPING_PITCHER).getPitchingPercentages().getPitchingStaminaUsed()).append("\n");
        start = gameLog.length();
        gameLog.append("\n\n                             --- GAME OVER!!! ---                             \n\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gameLog.setSpan(new BackgroundColorSpan(context.getColor(R.color.colorPrimaryDark)), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            gameLog.setSpan(new ForegroundColorSpan(context.getColor(R.color.white)), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        gameLog.setSpan(new StyleSpan(Typeface.BOLD), start, gameLog.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gameLog.append("Final Score :\n").append(homeTeamName).append(" ").append(Integer.toString(homeScore)).append(" - ")
                .append(visitingTeamName).append(" ").append(Integer.toString(visitorScore));

        game.setHomeScore(homeScore);
        game.setVisitorScore(visitorScore);
        game.setPlayedGame(true);

        updateBattingLines();
        updatePitchingLines();

        pitcherOfRecordForWin.getPitchingStats().get(0).incrementWins();
        pitcherOfRecordForLoss.getPitchingStats().get(0).incrementLosses();
        if (pitcherOfRecordForWin.getPrimaryPosition() == STARTING_PITCHER) {
            pitcherOfRecordForWin.getPitchingStats().get(0).incrementCompleteGames();
            if(homeScore == 0 || visitorScore == 0) {
                pitcherOfRecordForWin.getPitchingStats().get(0).incrementShutOuts();
            }
        }
        if (pitcherOfRecordForLoss.getPrimaryPosition() == STARTING_PITCHER) {
            pitcherOfRecordForLoss.getPitchingStats().get(0).incrementCompleteGames();
        }
        if (homeScore > visitorScore) {
            gameLog.append("\nWP : ").append(pitcherOfRecordForWin.getName()).append(" (").append(Integer.toString(pitcherOfRecordForWin.getPitchingStats().get(0).getWins())).append("-")
                    .append(Integer.toString(pitcherOfRecordForWin.getPitchingStats().get(0).getLosses())).append(") - LP : ")
                    .append(pitcherOfRecordForLoss.getName()).append(" (").append(Integer.toString(pitcherOfRecordForLoss.getPitchingStats().get(0).getWins())).append("-")
                    .append(Integer.toString(pitcherOfRecordForLoss.getPitchingStats().get(0).getLosses())).append(")");
            homeTeam.incrementWins();
            visitingTeam.incrementLosses();
        } else {
            gameLog.append("\nLP : ").append(pitcherOfRecordForLoss.getName()).append(" (").append(Integer.toString(pitcherOfRecordForLoss.getPitchingStats().get(0).getWins())).append("-")
                    .append(Integer.toString(pitcherOfRecordForLoss.getPitchingStats().get(0).getLosses())).append(") - WP : ")
                    .append(pitcherOfRecordForWin.getName()).append(" (").append(Integer.toString(pitcherOfRecordForWin.getPitchingStats().get(0).getWins())).append("-")
                    .append(Integer.toString(pitcherOfRecordForWin.getPitchingStats().get(0).getLosses())).append(")");
            visitingTeam.incrementWins();
            homeTeam.incrementLosses();
        }
        if (pitcherOfRecordForSave != null) {
            pitcherOfRecordForSave.getPitchingStats().get(0).incrementSaves();
            gameLog.append("\nSAVE : ").append(pitcherOfRecordForSave.getName()).append(" (").append(Integer.toString(pitcherOfRecordForSave.getPitchingStats().get(0).getSaves())).append(")");
        }
    }

    private void updatePitchingLines() {
        for (PitchingLine pitchingLine : game.getHomeBoxScore().getPitchingLines()) {
            String playerName = pitchingLine.getPitcherName();
            for (Player player : homeTeam.getPlayers()) {
                if (player.getName().equals(playerName)) {
                    pitchingLine.setEra(player.getPitchingStats().get(0).getERA());
                    pitchingLine.setWhip(player.getPitchingStats().get(0).getWHIP());
                    break;
                }
            }
        }
        for (PitchingLine pitchingLine : game.getVisitorBoxScore().getPitchingLines()) {
            String playerName = pitchingLine.getPitcherName();
            for (Player player : visitingTeam.getPlayers()) {
                if (player.getName().equals(playerName)) {
                    pitchingLine.setEra(player.getPitchingStats().get(0).getERA());
                    pitchingLine.setWhip(player.getPitchingStats().get(0).getWHIP());
                    break;
                }
            }
        }
    }

    private void updateBattingLines() {
        for (BattingLine battingLine : game.getHomeBoxScore().getBattingLines()) {
            String playerName = battingLine.getBatterName();
            for (Player player : homeTeam.getPlayers()) {
                if (player.getName().equals(playerName)) {
                    battingLine.setAverage(player.getBattingStats().get(0).getAverage());
                    battingLine.setOnBasePct(player.getBattingStats().get(0).getOnBasePct());
                    break;
                }
            }
        }
        for (BattingLine battingLine : game.getVisitorBoxScore().getBattingLines()) {
            String playerName = battingLine.getBatterName();
            for (Player player : visitingTeam.getPlayers()) {
                if (player.getName().equals(playerName)) {
                    battingLine.setAverage(player.getBattingStats().get(0).getAverage());
                    battingLine.setOnBasePct(player.getBattingStats().get(0).getOnBasePct());
                    break;
                }
            }
        }
    }

    private void checkForBlownSave() {
        if (pitcherOfRecordForSave != null) {
            pitcherOfRecordForSave.getPitchingStats().get(0).incrementBlownSaves();
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
            if (inningsPlayed >= 80) {
                return pinchHitForPitcher(player, lineupToChange, defenseToChange);
            }
            // if after the ninth inning and starter is still in the game
            if (inningsPlayed >= 90 && player.getPrimaryPosition() == STARTING_PITCHER) {
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
        if (pinchHitter == null) {
            return null;
        }
        lineupToChange.put(currentBatter, pinchHitter);
        defenseToChange.put(SCOREKEEPING_PITCHER, pinchHitter);
        lineup = lineupToChange;
        pinchHitter.getBattingStats().get(0).incrementGames();
        addPinchHitterForPitcherToLog(pitcher, pinchHitter);
        addBattingLineForSub(currentBatter, pinchHitter);
        return pitcher;
    }

    private void addBattingLineForSub(int placeInOrder, Player newHitter) {
        boolean visitorHitting = isVisitorHitting;
        if (isPitcher(newHitter)) {
            visitorHitting = !visitorHitting;               // Pitchers are only subbed in on defense so need to switch which boxscore the batting line is added to
        }
        if (visitorHitting) {
            game.getVisitorBoxScore().addBattingLine(new BattingLine(game.getVisitorBoxScore().getBoxScoreId(), placeInOrder, true , visitorSubstituteNumber, newHitter.getName()));
            visitorSubstituteNumber++;
        } else {
            game.getHomeBoxScore().addBattingLine(new BattingLine(game.getHomeBoxScore().getBoxScoreId(), placeInOrder, true , homeSubstituteNumber, newHitter.getName()));
            homeSubstituteNumber++;
        }
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

        if (inningsPlayed <= 70) {
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
        Player newPitcher;
        int pitchingScore = visitorScore;
        int battingScore = homeScore;
        Team pitchingTeam = visitingTeam;
        int lineupPosition = 0;
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

        if(relievers.isEmpty()) {
            Player worstStarter = PitchingRotationGenerator.getWorstStarterAvailable(pitchingTeam);
            if (worstStarter != null) {
                relievers.put(10, worstStarter);
            } else {
                return;                                    // Don't switch and the position player stays in....
            }
        }

        if (inningsPlayed >= 80 && pitchingScore > battingScore && (pitchingScore - battingScore <= 3) && !closerUsed) {     // Save situation = after 8 innings are played and pitching team ahead by less than 3

            Player closer = PitchingRotationGenerator.getBestCloserAvailable(pitchingTeam);
            defenseToModify.put(SCOREKEEPING_PITCHER, closer);
            for (TreeMap.Entry<Integer, Player> entry : lineupToModify.entrySet()) {
                if (entry.getValue() == currentPitcher) {
                    lineupPosition = entry.getKey();
                    lineupToModify.put(lineupPosition, closer);
                }
            }
            if (isVisitorHitting) {
                homeCloserUsed = true;
            } else {
                visitorCloserUsed = true;
            }
            if (isSaveOrHoldSituation()) {
                updateSaveAndHoldSituation(currentPitcher, closer);
            }
            closer.getPitchingStats().get(0).incrementGames();
            closer.getBattingStats().get(0).incrementGames();
            addPitchingLineForNewPitcher(closer);
            if (lineupPosition != 0) {
                addBattingLineForSub(lineupPosition, closer);
            }
            addPitchingChangeToLog(currentPitcher, defenseToModify.get(SCOREKEEPING_PITCHER));
            return;
        }

        int relieverChoice = checkSituationForBestRelieverToBringIn();
        if (relievers.containsKey(relieverChoice)) {
            newPitcher = relievers.get(relieverChoice);
            defenseToModify.put(SCOREKEEPING_PITCHER, newPitcher);
            for (TreeMap.Entry<Integer, Player> entry : lineupToModify.entrySet()) {
                if (entry.getValue() == currentPitcher) {
                    lineupPosition = entry.getKey();
                    lineupToModify.put(lineupPosition, newPitcher);
                }
            }
            relievers.remove(relieverChoice);
            if (isSaveOrHoldSituation()) {
                updateSaveAndHoldSituation(currentPitcher, newPitcher);
            }
            newPitcher.getPitchingStats().get(0).incrementGames();
            newPitcher.getBattingStats().get(0).incrementGames();
            addPitchingLineForNewPitcher(newPitcher);
            if (lineupPosition != 0) {
                addBattingLineForSub(lineupPosition, newPitcher);
            }
            addPitchingChangeToLog(currentPitcher, newPitcher);
        } else {
            // put in best reliever available
            relieverChoice = relievers.ceilingKey(0);

            newPitcher = relievers.get(relieverChoice);
            defenseToModify.put(SCOREKEEPING_PITCHER, newPitcher);
            for (TreeMap.Entry<Integer, Player> lineupEntry : lineupToModify.entrySet()) {
                if (lineupEntry.getValue() == currentPitcher) {
                    lineupPosition = lineupEntry.getKey();
                    lineupToModify.put(lineupPosition, newPitcher);
                }
            }

            relievers.remove(relieverChoice);
            if (isSaveOrHoldSituation()) {
                updateSaveAndHoldSituation(currentPitcher, newPitcher);
            }
            newPitcher.getPitchingStats().get(0).incrementGames();
            newPitcher.getBattingStats().get(0).incrementGames();
            addPitchingLineForNewPitcher(newPitcher);
            if (lineupPosition != 0) {
                addBattingLineForSub(lineupPosition, newPitcher);
            }
            addPitchingChangeToLog(currentPitcher, newPitcher);
        }
    }

    private void addPitchingLineForNewPitcher(Player newPitcher) {
        if (isVisitorHitting) {
            homePitcherNumber ++;
            game.getHomeBoxScore().addPitchingLine(new PitchingLine(game.getHomeBoxScore().getBoxScoreId(), homePitcherNumber, newPitcher.getName()));
        } else {
            visitorPitcherNumber ++;
            game.getVisitorBoxScore().addPitchingLine(new PitchingLine(game.getVisitorBoxScore().getBoxScoreId(), visitorPitcherNumber, newPitcher.getName()));
        }
    }

    private void updateSaveAndHoldSituation(Player currentPitcher, Player newPitcher) {
        if (pitcherOfRecordForSave != null) {
            if (currentPitcher == pitcherOfRecordForSave) {
                // add a hold to current pitcher
                currentPitcher.getPitchingStats().get(0).incrementHolds();
            }
        }
        pitcherOfRecordForSave = newPitcher;
    }

    private boolean isSaveOrHoldSituation() {
        if (isVisitorHitting) {
            if (homeScore > visitorScore) {
                // save situation if 1) winning by three runs or less, 2) potential tying run is on base, at bat, or on deck
                return (homeScore - visitorScore <= 3) || (homeScore - visitorScore - countRunners() - 2 <= 0);
            }
        } else {
            if (visitorScore > homeScore) {
                // save situation if 1) winning by three runs or less, 2) potential tying run is on base, at bat, or on deck
                return (visitorScore - homeScore <= 3) || (visitorScore - homeScore - countRunners() - 2 <= 0);
            }
        }
        return false;
    }

    private int countRunners() {
        int count = 0;
        for (Runner runner : runners) {
            if (runner != null) {
                count++;
            }
        }
        return count;
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

        homeDefense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).incrementGamesStarted();
        homeDefense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).incrementGames();
        homeDefense.get(SCOREKEEPING_PITCHER).getBattingStats().get(0).incrementGames();
        visitingDefense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).incrementGamesStarted();
        visitingDefense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).incrementGames();
        visitingDefense.get(SCOREKEEPING_PITCHER).getBattingStats().get(0).incrementGames();

        for (int i = 1; i < 10; i++) {
            if (homeLineup.get(i).getPrimaryPosition() != STARTING_PITCHER) {
                homeLineup.get(i).getBattingStats().get(0).incrementGames();
            }
            if (visitingLineup.get(i).getPrimaryPosition() != STARTING_PITCHER) {
                visitingLineup.get(i).getBattingStats().get(0).incrementGames();
            }
        }

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
        game.setVisitorBoxScore(new BoxScore(game.getGameId(), false));
        game.setHomeBoxScore(new BoxScore(game.getGameId(), true));
        initializeBoxScore(game.getHomeBoxScore(), homeLineup, homeDefense);
        initializeBoxScore(game.getVisitorBoxScore(), visitingLineup, visitingDefense);
        game.setGameLog("");
        game.setPlayedGame(false);

        homeSubstituteNumber = 1;
        visitorSubstituteNumber = 1;

        homePitcherNumber = 1;
        visitorPitcherNumber = 1;
    }

    private void initializeBoxScore(BoxScore boxScore, TreeMap<Integer, Player> lineup, TreeMap<Integer, Player> defense) {
        for (TreeMap.Entry entry : lineup.entrySet()) {
            Player player = (Player) entry.getValue();
            boxScore.addBattingLine(new BattingLine(boxScore.getBoxScoreId(), (int) entry.getKey(), false, 0, player.getName()));
        }
        boxScore.addPitchingLine(new PitchingLine(boxScore.getBoxScoreId(), 1, defense.get(SCOREKEEPING_PITCHER).getName()));
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
                pitcherRecovery = player.getPitchingPercentages().getPitchingStamina() / 5;
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

    private AtBatSimulator generateNewAtBat(Context context, int[] freshCount, int outs, int currentBatter, TreeMap<Integer, Player> lineupFromDefense, Runner[] runners, TreeMap<Integer, Player> defense, BattingLine battingLine, PitchingLine pitchingLine) {
        return new AtBatSimulator(context, freshCount, outs, currentBatter, lineupFromDefense, runners, defense, battingLine, pitchingLine);
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
