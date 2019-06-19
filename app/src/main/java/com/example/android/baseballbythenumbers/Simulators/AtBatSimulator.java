package com.example.android.baseballbythenumbers.Simulators;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.example.android.baseballbythenumbers.Data.BattingStats;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Runner;
import com.example.android.baseballbythenumbers.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_CENTER_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_PULL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.FieldArea.CENTER;
import static com.example.android.baseballbythenumbers.Constants.Constants.FieldArea.LEFT;
import static com.example.android.baseballbythenumbers.Constants.Constants.FieldArea.RIGHT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_HARD_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_HARD_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_HARD_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_MED_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_MED_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_MED_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_SOFT_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_SOFT_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_SOFT_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.QualityOfContact.HARD;
import static com.example.android.baseballbythenumbers.Constants.Constants.QualityOfContact.MEDIUM;
import static com.example.android.baseballbythenumbers.Constants.Constants.QualityOfContact.SOFT;
import static com.example.android.baseballbythenumbers.Constants.Constants.TypeOfHit.FLYBALL;
import static com.example.android.baseballbythenumbers.Constants.Constants.TypeOfHit.GROUNDBALL;
import static com.example.android.baseballbythenumbers.Constants.Constants.TypeOfHit.LINE_DRIVE;
import static com.example.android.baseballbythenumbers.Constants.GameStates.NO_OUTS_BASES_LOADED;
import static com.example.android.baseballbythenumbers.Constants.GameStates.NO_OUTS_NOBODY_ON;
import static com.example.android.baseballbythenumbers.Constants.GameStates.NO_OUTS_RUNNER_ON_FIRST;
import static com.example.android.baseballbythenumbers.Constants.GameStates.NO_OUTS_RUNNER_ON_FIRST_AND_SECOND;
import static com.example.android.baseballbythenumbers.Constants.GameStates.NO_OUTS_RUNNER_ON_FIRST_AND_THIRD;
import static com.example.android.baseballbythenumbers.Constants.GameStates.NO_OUTS_RUNNER_ON_SECOND;
import static com.example.android.baseballbythenumbers.Constants.GameStates.NO_OUTS_RUNNER_ON_SECOND_AND_THIRD;
import static com.example.android.baseballbythenumbers.Constants.GameStates.NO_OUTS_RUNNER_ON_THIRD;
import static com.example.android.baseballbythenumbers.Constants.Positions.FIRST_BASE;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_CATCHER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_CENTER_FIELD;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_FIRST_BASE;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_LEFT_FIELD;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_PITCHER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_RIGHT_FIELD;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_SECOND_BASE;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_SHORTSTOP;
import static com.example.android.baseballbythenumbers.Constants.Positions.SCOREKEEPING_THIRD_BASE;
import static com.example.android.baseballbythenumbers.Constants.Positions.getPositionNameFromScorekeeperPosition;

public class AtBatSimulator {

    private static final int ONE_HUNDERED_PERCENT = 10000;
    private static final int FIFTY_PERCENT = 5000;
    private static final int TEN_PERCENT = 1000;
    private static final int FORTY_PERCENT = 4000;
    private int[] count;

    private int outs;

    private int runsScored;

    private int currentBatter;

    private TreeMap<Integer, Player> lineup;

    private Runner[] runners;

    private TreeMap<Integer, Player> defense;

    private Random random;

    private Runner runnerOnFirst;

    private Runner runnerOnSecond;

    private Runner runnerOnThird;

    private int balls;

    private int strikes;

    private int gameStateAfterAtBat;

    private boolean errorOnPlay;

    private Context context;

    private static SpannableStringBuilder atBatSummary;

    private int startOfSpan;

    private boolean areRunsEarned;

    private List<Player> pitchersResponsible;

    public AtBatSimulator(Context context, int[] count, int outs, int currentBatter, @NonNull TreeMap<Integer, Player> lineup, @NonNull Runner[] runners, @NonNull TreeMap<Integer, Player> defense) {
        this.count = count;
        this.outs = outs;
        this.currentBatter = currentBatter;
        this.lineup = lineup;
        this.runners = runners;
        this.defense = defense;
        this.random = new Random();
        this.context = context;

        checkForValidCount(count);
        checkForValidOuts(outs);
        checkForValidCurrentBatter(currentBatter);

        balls = this.count[0];
        strikes = this.count[1];

        runnerOnFirst = this.runners[0];
        runnerOnSecond = this.runners[1];
        runnerOnThird = this.runners[2];

        atBatSummary = new SpannableStringBuilder();

        gameStateAfterAtBat = 0;

        errorOnPlay = false;

        areRunsEarned = true;

        pitchersResponsible = new ArrayList<>();
    }


    private void checkForValidCurrentBatter(int currentBatter) {
        //if the currentBatter passed in is more than 9 it is invalid, throw an error
        try {
            if (currentBatter > 9 || outs < 0) {
                throw new IllegalArgumentException("Current Batter passed to AtBatSimulator is Invalid!");
            }
        } catch (IllegalArgumentException e) {
            Timber.e(e, " Current Batter passed was %s", currentBatter);
        }
    }

    private void checkForValidOuts(int outs) {
        //if the outs passed in has more than 2 it is invalid, throw an error
        try {
            if (outs > 2 || outs < 0) {
                throw new IllegalArgumentException("Outs passed to AtBatSimulator are Invalid!");
            }
        } catch (IllegalArgumentException e) {
            Timber.e(e, " Illegal Outs passed were %s", outs);
        }
    }

    private void checkForValidCount(int[] count) {
        //if the count passed in has more than 3 balls or more than 2 strikes it is invalid throw an error
        try {
            if (count == null) {
                throw new IllegalArgumentException("Count passed to AtBatSimulator is Null, that is Invalid!");
            }
            if (count.length != 2 || count[0] > 3 || count[0] < 0 || count[1] > 2 || count[1] < 0) {
                throw new IllegalArgumentException("Count passed to AtBatSimulator is Invalid!");
            }
        } catch (IllegalArgumentException e) {
            Timber.e(e, " Illegal Count passed was %s", count.toString());
        }
    }


    public int simulateAtBat(int pitcherStaminaAdjustment, int batterStaminaAdjustment, boolean areRunsEarned) {
        runsScored = 0;
        this.areRunsEarned = areRunsEarned;
        if (!areRunsEarned) {
            for (Runner runner : runners) {
                if (runner != null) {
                    runner.setEarnedRun(false);
                }
            }
        }

        Player batter = lineup.get(currentBatter);
        Player pitcher = defense.get(1);

        assert batter != null;
        BattingStats currentBatterBattingStatsForThisSeason = batter.getBattingStats().get(0);
        currentBatterBattingStatsForThisSeason.setPlateAppearances(currentBatterBattingStatsForThisSeason.getPlateAppearances() + 1);

        assert pitcher != null;
        PitchingStats currentPitcherPitchingStatsForThisSeason = pitcher.getPitchingStats().get(0);
        currentPitcherPitchingStatsForThisSeason.setTotalBattersFaced(currentPitcherPitchingStatsForThisSeason.getTotalBattersFaced() + 1);

        while (gameStateAfterAtBat == 0) {
            setStartOfSpan();
            if (isPitchThrown(pitcher)) {
                if (isPitchInStrikeZone(pitcher)) {
                    //Pitch in Strike Zone, check for batter swing
                    int pitcherZSwingPct = pitcher.getPitchingPercentages().getZSwingPct() + pitcherStaminaAdjustment;  //swinging at pitches in zone is worse for pitcher (less strikeouts), so add adjustment
                    int batterZSwingPct = batter.getHittingPercentages().getZSwingPct() - batterStaminaAdjustment; // swinging at pitches in zone is better for batter so subtract adjustment
                    if (oddsRatioMethod(batterZSwingPct, pitcherZSwingPct, BATTING_Z_SWING_PCT_MEAN)) {
                        //Batter swings at the pitch, check for contact
                        int batterZContact = batter.getHittingPercentages().getZContactPct() - batterStaminaAdjustment;
                        int pitcherZContactPct = pitcher.getPitchingPercentages().getZContactPct() + pitcherStaminaAdjustment;
                        if (oddsRatioMethod(batterZContact, pitcherZContactPct, BATTING_Z_CONTACT_PCT_MEAN)) {
                            // Batter makes contact
                            resolveContact(batter, pitcher, batterStaminaAdjustment, pitcherStaminaAdjustment, areRunsEarned);
                        } else {
                            // Batter swings and misses
                            atBatSummary.append("Strike Swinging, ");
                            addStrikeAndCheckResults(batter, pitcher);
                        }
                    } else {
                        //Batter takes the pitch
                        atBatSummary.append("Strike Looking, ");
                        addStrikeAndCheckResults(batter, pitcher);
                    }
                } else {
                    //Pitch out of strike zone, check for batter swing
                    int pitcherOSwingPct = pitcher.getPitchingPercentages().getOSwingPct() - pitcherStaminaAdjustment;  //Swinging at pitchers out of the strike zone is better for the pitcher so subtract adjustment
                    int batterOSwingPct = batter.getHittingPercentages().getOSwingPct() + batterStaminaAdjustment;  //Worse for batter so add adjustment
                    if (oddsRatioMethod(batterOSwingPct, pitcherOSwingPct, BATTING_O_SWING_PCT_MEAN)) {
                        //Batter swings at the pitch
                        int batterOContact = batter.getHittingPercentages().getOContactPct() - batterStaminaAdjustment;
                        int pitcherOContactPct = pitcher.getPitchingPercentages().getOContactPct() + pitcherStaminaAdjustment;
                        if (oddsRatioMethod(batterOContact, pitcherOContactPct, BATTING_O_CONTACT_PCT_MEAN)) {
                            // Batter makes contact
                            resolveContact(batter, pitcher, batterStaminaAdjustment, pitcherStaminaAdjustment, areRunsEarned);
                        } else {
                            // Batter swings and misses
                            atBatSummary.append("Strike Swinging, ");
                            checkForWildPitch(pitcher);
                            addStrikeAndCheckResults(batter, pitcher);
                        }
                    } else {
                        //Batter takes the pitch
                        checkForWildPitch(pitcher);
                        addBallAndCheckResults(batter, pitcher);
                    }
                }
            }
        }

        batter.getHittingPercentages().setStaminaUsed(batter.getHittingPercentages().getStaminaUsed() + 5);
        return gameStateAfterAtBat;
    }

    private void addBallAndCheckResults(Player batter, Player pitcher) {
        // add ball to count, check stealing, if ball four, advance runners as needed and update stats and game state
        balls++;
        atBatSummary.append("Ball, ");
        if (balls == 4) {
            setStartOfSpan();
            atBatSummary.append(batter.getLastName()).append(" Walked ");
            formatAtBatResult();

            // Update batter and pitcher stats to reflect walk
            batter.getBattingStats().get(0).setWalks(batter.getBattingStats().get(0).getWalks() + 1);
            pitcher.getPitchingStats().get(0).setWalks(pitcher.getPitchingStats().get(0).getWalks() + 1);
            if (runnerOnFirst == null) {
                // No runner on first, move batter to first and game state
                runnerOnFirst = new Runner(batter, pitcher, true);
                moveToNextBatterInLineup();
                atBatOver();
            } else {
                if (runnerOnSecond == null) {
                    // runner on first, nobody on second, move batter to first and runner on first to second
                    runnerOnSecond = runnerOnFirst;
                    runnerOnFirst = new Runner(batter, pitcher, true);
                    moveToNextBatterInLineup();
                    atBatOver();
                } else {
                    if (runnerOnThird == null) {
                        // runners on first and second
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = new Runner(batter, pitcher, true);
                        moveToNextBatterInLineup();
                        atBatOver();
                    } else {
                        // bases loaded
                        runsScored++;
                        batter.getBattingStats().get(0).setRunsBattedIn(batter.getBattingStats().get(0).getRunsBattedIn() + 1);
                        runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                        pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                        if (runnerOnThird.isEarnedRun()) {
                            runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getEarnedRuns() + 1);
                        }
                        runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = new Runner(batter, pitcher, true);
                        moveToNextBatterInLineup();
                        atBatOver();
                    }
                }
            }
        } else {
            checkForStealingAndUpdate(getGameState());
        }
    }

    private void formatAtBatResult() {
        atBatSummary.setSpan(new StyleSpan(Typeface.BOLD), startOfSpan, atBatSummary.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            atBatSummary.setSpan(new ForegroundColorSpan(context.getColor(R.color.black)), startOfSpan, atBatSummary.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void addRunnersToAtBatSummary() {
        if (runnerOnFirst != null) {
            atBatSummary.append("\n").append("Runner on First : ").append(runnerOnFirst.getRunner().getName());
        }
        if (runnerOnSecond != null) {
            atBatSummary.append("\n").append("Runner on Second : ").append(runnerOnSecond.getRunner().getName());
        }
        if (runnerOnThird != null) {
            atBatSummary.append("\n").append("Runner on Third : ").append(runnerOnThird.getRunner().getName());
        }
    }

    private void moveToNextBatterInLineup() {
        if (currentBatter == 9) {
            currentBatter = 1;
        } else {
            currentBatter++;
        }
    }

    private void atBatOver() {
        gameStateAfterAtBat = getGameState();
    }

    private void resolveContact(Player batter, Player pitcher, int batterStaminaAdjustment, int pitcherStaminaAdjustment, boolean areRunsEarned) {
        int typeOfHit = getTypeOfHit(batter, pitcher, batterStaminaAdjustment);
        if (isHomeRun(batter, pitcher, typeOfHit, batterStaminaAdjustment, pitcherStaminaAdjustment)) {
            List<String> runnersThatScored = new ArrayList<>();
            int numberOfRunners = 0;
            if (runnerOnThird != null) {
                numberOfRunners++;
                runsScored++;
                runnersThatScored.add(runnerOnThird.getRunner().getLastName());
                if (runnerOnThird.isEarnedRun() && areRunsEarned) {
                    runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getEarnedRuns() + 1);
                }
                runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                runnerOnThird = null;
            }
            if (runnerOnSecond != null) {
                numberOfRunners++;
                runsScored++;
                runnersThatScored.add(runnerOnSecond.getRunner().getLastName());
                runnerOnSecond.getRunner().getBattingStats().get(0).setRuns(runnerOnSecond.getRunner().getBattingStats().get(0).getRuns() + 1);
                if (runnerOnSecond.isEarnedRun() && areRunsEarned) {
                    runnerOnSecond.getPitcherResponsible().getPitchingStats().get(0).setEarnedRuns(runnerOnSecond.getPitcherResponsible().getPitchingStats().get(0).getEarnedRuns() + 1);
                }
                runnerOnSecond.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnSecond.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                pitchersResponsible.add(runnerOnSecond.getPitcherResponsible());
                runnerOnSecond = null;
            }
            if (runnerOnFirst != null) {
                numberOfRunners++;
                runsScored++;
                runnersThatScored.add(runnerOnFirst.getRunner().getLastName());
                runnerOnFirst.getRunner().getBattingStats().get(0).setRuns(runnerOnFirst.getRunner().getBattingStats().get(0).getRuns() + 1);
                if (runnerOnFirst.isEarnedRun() && areRunsEarned) {
                    runnerOnFirst.getPitcherResponsible().getPitchingStats().get(0).setEarnedRuns(runnerOnFirst.getPitcherResponsible().getPitchingStats().get(0).getEarnedRuns() + 1);
                }
                runnerOnFirst.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnFirst.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                pitchersResponsible.add(runnerOnFirst.getPitcherResponsible());
                runnerOnFirst = null;
            }

            runsScored++;
            setStartOfSpan();
            if (numberOfRunners == 0) {
                atBatSummary.append(batter.getLastName()).append(" hits a SOLO HOME RUN!!! ");
                formatScoringResult();
            } else if (numberOfRunners != 3) {
                atBatSummary.append(batter.getLastName()).append(" hits a ").append(Integer.toString(numberOfRunners + 1)).append(" run HOME RUN!!! ");
                for (String runner : runnersThatScored) {
                    atBatSummary.append(runner).append(" Scored.  ");
                }
                formatScoringResult();
            } else {
                atBatSummary.append(batter.getLastName()).append(" hits a GRAND SLAM!!! ");
                for (String runner : runnersThatScored) {
                    atBatSummary.append(runner).append(" Scored.  ");
                }
                formatScoringResult();
            }
            batter.getBattingStats().get(0).setRuns(batter.getBattingStats().get(0).getRuns() + 1);
            batter.getBattingStats().get(0).setHomeRuns(batter.getBattingStats().get(0).getHomeRuns() + 1);
            batter.getBattingStats().get(0).setHits(batter.getBattingStats().get(0).getHits() + 1);
            batter.getBattingStats().get(0).setRunsBattedIn(batter.getBattingStats().get(0).getRunsBattedIn() + (1 + numberOfRunners));

            pitcher.getPitchingStats().get(0).setRuns(pitcher.getPitchingStats().get(0).getRuns() + 1);
            pitchersResponsible.add(pitcher);
            pitcher.getPitchingStats().get(0).setHomeRuns(pitcher.getPitchingStats().get(0).getHomeRuns() + 1);
            pitcher.getPitchingStats().get(0).setHits(pitcher.getPitchingStats().get(0).getHits() + 1);
            if (areRunsEarned) {
                pitcher.getPitchingStats().get(0).setEarnedRuns(pitcher.getPitchingStats().get(0).getEarnedRuns() + 1);
            }
            moveToNextBatterInLineup();
            atBatOver();

        } else {
            if (checkForFoulBall(batter)) {
                // ball is foul
                atBatSummary.append("Foul Ball, ");
                if (strikes < 2) {
                    strikes++;
                }
            } else {
                // ball in play
                int contactQuality = getQualityOfContact(batter, batterStaminaAdjustment);
                if (isHit(batter, typeOfHit, contactQuality)) {
                    resolveHit(batter, pitcher, typeOfHit, contactQuality);
                } else {
                    resolveOut(batter, pitcher, typeOfHit, contactQuality);
                }
            }
        }
    }

    private void formatScoringResult() {
        atBatSummary.setSpan(new StyleSpan(Typeface.BOLD), startOfSpan, atBatSummary.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            atBatSummary.setSpan(new ForegroundColorSpan(context.getColor(R.color.red)), startOfSpan, atBatSummary.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void setStartOfSpan() {
        startOfSpan = atBatSummary.length();
    }

    private void resolveOut(Player batter, Player pitcher, int typeOfHit, int contactQuality) {
        int outsAtStartOfAtBat = outs;
        int whereBallIsHit = getHitLocation(batter, typeOfHit, contactQuality, true);
        int gameStateIgnoringOuts = getGameState() - (outs * 8);
        boolean errorMade = false;
        int fielderWhoMadeError = 0;
        List<Integer> fieldersWhoTouchedBall = new ArrayList<>();
        if (outs != 2 && typeOfHit != FLYBALL && whereBallIsHit < 7 && gameStateIgnoringOuts != NO_OUTS_NOBODY_ON) {
            // chance for doubleplay
            int oddsOfDoublePlay = 0;
            int chanceOfDoublePlay = random.nextInt(ONE_HUNDERED_PERCENT);
            boolean runnerSteals;
            switch (gameStateIgnoringOuts) {
                case NO_OUTS_RUNNER_ON_FIRST:
                    runnerSteals = ifRunnerSteals(runnerOnFirst);
                    if (typeOfHit == LINE_DRIVE) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 9500;
                        } else {
                            oddsOfDoublePlay = 500;
                        }
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality == SOFT) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 500;
                        } else {
                            oddsOfDoublePlay = 7500 - runnerOnFirst.getRunner().getHittingPercentages().getSpeed() - batter.getHittingPercentages().getSpeed();
                        }
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality != SOFT) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 5000;
                        } else {
                            oddsOfDoublePlay = 9500 - runnerOnFirst.getRunner().getHittingPercentages().getSpeed() - batter.getHittingPercentages().getSpeed();
                        }
                    }
                    if (typeOfHit == GROUNDBALL) {
                        setStartOfSpan();
                        atBatSummary.append("Grounder to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SECOND_BASE);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SHORTSTOP);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at second, but no double play
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, but batter reaches first on an error! ");
                                    formatAtBatResult();
                                    outs++;
                                    runnerOnFirst = new Runner(batter, runnerOnFirst.getPitcherResponsible(), false);
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append("all runners safe on Error! ");
                                    formatAtBatResult();
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                }
                            } else {
                                // no error made, turn double play
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, and over to ")
                                        .append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(2))).append(", Double Play!!!");
                                formatAtBatResult();
                                outs = outs + 2;
                                runnerOnFirst = null;
                            }
                        } else {
                            // Get a single out
                            if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SECOND_BASE);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SHORTSTOP);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            }

                            if (runnerSteals) {
                                // no play at second, runner goes to second, possible out at first
                                errorMade = checkForError(whereBallIsHit);

                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = whereBallIsHit;
                                }
                                if (!errorMade && whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                                    errorMade = checkForError(SCOREKEEPING_FIRST_BASE);
                                    fielderWhoMadeError = SCOREKEEPING_FIRST_BASE;
                                }
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    // everyone is safe
                                    atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" was stealing, only play is to first base, SAFE at first on Error! ");
                                    formatAtBatResult();
                                    defense.get(fielderWhoMadeError).getBattingStats().get(0).setErrors(defense.get(fielderWhoMadeError).getBattingStats().get(0).getErrors() + 1);
                                    runnerOnSecond = runnerOnFirst;
                                } else {
                                    atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" was stealing, only play is to first base, OUT at first! ");
                                    formatAtBatResult();
                                    outs++;
                                    runnerOnSecond = runnerOnFirst;
                                }
                            } else {
                                // out at second, but batter beats throw to first
                                for (Integer fielder : fieldersWhoTouchedBall) {
                                    errorMade = checkForError(fielder);
                                    if (errorMade) {
                                        errorOnPlay = true;
                                        if (outs == 2) {
                                            areRunsEarned = false;
                                        }
                                        fielderWhoMadeError = fielder;
                                        break;
                                    }
                                }
                                if (errorMade) {
                                    if (fielderWhoMadeError == SCOREKEEPING_FIRST_BASE) {
                                        atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                                .append(" Out!, throw to first gets away, batter goes to Second!!!");
                                        formatAtBatResult();
                                        outs++;
                                        runnerOnSecond = new Runner(batter, pitcher, false);
                                    } else {
                                        atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append("...Error on ")
                                                .append(getPositionNameFromScorekeeperPosition(fielderWhoMadeError)).append(" everyone is SAFE!!!");
                                        formatAtBatResult();
                                        runnerOnSecond = runnerOnFirst;
                                        runnerOnFirst = new Runner(batter, pitcher, false);
                                    }
                                } else {
                                    // Fielder's Choice, no error, pitcher responsible for previous runner is now responsible for current runner since an out was made.
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, but batter beats the throw to first, SAFE! ");
                                    formatAtBatResult();
                                    outs++;
                                    runnerOnFirst = new Runner(batter, runnerOnFirst.getPitcherResponsible(), runnerOnFirst.isEarnedRun());
                                }

                            }
                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        setStartOfSpan();
                        atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everone safe
                                    atBatSummary.append("and it kicks off his glove, everyone is safe on the Error! ");
                                    formatAtBatResult();
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    atBatSummary.append(batter.getLastName()).append(" is Out, but he throws it away trying to get ").append(runnerOnFirst.getRunner().getLastName()).append(" at first, ")
                                            .append(runnerOnFirst.getRunner().getLastName()).append(" advances to Second on the error!");
                                    formatAtBatResult();
                                    outs++;
                                    runnerOnSecond = runnerOnFirst;
                                    runnerOnFirst = null;
                                }
                            } else {
                                // no error, turn double play
                                atBatSummary.append(batter.getLastName()).append(" is Out, and he catches ").append(runnerOnFirst.getRunner().getLastName()).append(" off first base, Double Play!");
                                formatAtBatResult();
                                outs = outs + 2;
                                runnerOnFirst = null;
                            }
                        }
                    }
                    break;
                case NO_OUTS_RUNNER_ON_SECOND:
                    runnerSteals = ifRunnerSteals(runnerOnSecond);
                    if (typeOfHit == LINE_DRIVE) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 9500;
                        } else {
                            oddsOfDoublePlay = 500;
                        }
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality == SOFT) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 500;
                        } else {
                            oddsOfDoublePlay = 100;
                        }
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality != SOFT) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 100;
                        } else {
                            oddsOfDoublePlay = 100;
                        }
                    }
                    if (typeOfHit == GROUNDBALL) {
                        setStartOfSpan();
                        atBatSummary.append("Grounder to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP) {
                                // Tag runner and throw
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_THIRD_BASE);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at third, but no double play
                                    atBatSummary.append(batter.getLastName()).append(" is Out, but he throws it away trying to get ").append(runnerOnFirst.getRunner().getLastName()).append(" at first, ")
                                            .append(runnerOnFirst.getRunner().getLastName()).append(" advances to Second on the error!");
                                    formatAtBatResult();
                                    outs++;
                                    runnerOnSecond = null;
                                    runnerOnFirst = new Runner(batter, pitcher, false);
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(", Error everyone is Safe! ");
                                    formatAtBatResult();
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                }
                            } else {
                                // no error made, turn double play
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, and over to ")
                                        .append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(2))).append(", Double Play!!!");
                                formatAtBatResult();
                                outs = outs + 2;
                                runnerOnSecond = null;
                            }
                        } else {
                            if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                                // throw out runner at first
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                // everyone safe
                                atBatSummary.append("everyone safe on the Error ");
                                formatAtBatResult();
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                            } else {
                                // Get a single out
                                if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP || whereBallIsHit == SCOREKEEPING_PITCHER) {
                                    // runner stays, out at first
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(", Out! ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" stays at second.  ");
                                    formatAtBatResult();
                                    outs++;
                                } else {
                                    // runner advances, out at first
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(", Out! ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" advances to third.  ");
                                    formatAtBatResult();
                                    outs++;
                                    runnerOnThird = runnerOnSecond;
                                    runnerOnSecond = null;
                                }
                            }

                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit != SCOREKEEPING_SECOND_BASE) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SECOND_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everone safe
                                    atBatSummary.append("dropped, everyone safe on the Error ");
                                    formatAtBatResult();
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    atBatSummary.append("Out!, throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(", and it gets away, ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" goes to third on the Error.  ");
                                    formatAtBatResult();
                                    outs++;
                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, false);
                                }
                            } else {
                                // no error, turn double play
                                atBatSummary.append("Out!, throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(", and he catches ")
                                        .append(runnerOnSecond.getRunner().getLastName()).append(" off the base, Double Play!");
                                formatAtBatResult();
                                outs = outs + 2;
                                runnerOnSecond = null;
                            }
                        } else {
                            // no double play
                            errorMade = checkForError(whereBallIsHit);
                            if (errorMade) {
                                errorOnPlay = true;
                                if (outs == 2) {
                                    areRunsEarned = false;
                                }
                                atBatSummary.append(", and it kicks off his glove into the outfield, everyone is safe, ")
                                        .append(runnerOnSecond.getRunner().getLastName()).append(" goes to third on the Error.  ");
                                formatAtBatResult();
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                            } else {
                                // batter is out on caught line drive, runner holds
                                atBatSummary.append(", Out! ")
                                        .append(runnerOnSecond.getRunner().getLastName()).append(" stays at Second.  ");
                                formatAtBatResult();
                                outs++;
                            }
                        }
                    }
                    break;
                case NO_OUTS_RUNNER_ON_THIRD:
                    if (typeOfHit == LINE_DRIVE) {
                        oddsOfDoublePlay = 500;
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality == SOFT) {
                        oddsOfDoublePlay = 500;
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality != SOFT) {
                        oddsOfDoublePlay = 100;
                    }
                    if (typeOfHit == GROUNDBALL) {
                        setStartOfSpan();
                        atBatSummary.append("Grounder to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit == SCOREKEEPING_CATCHER) {
                                // Throw home tag runner and throw to first
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_CATCHER);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at home, but no double play
                                    atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Out at home!, ")
                                            .append(batter.getLastName()).append(" safe at first.  ");
                                    formatAtBatResult();
                                    outs++;
                                    runnerOnThird = null;
                                    runnerOnFirst = new Runner(batter, pitcher, false);
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(", and it kicks off his glove, everyone is safe, ")
                                            .append(batter.getLastName()).append(" safe at first.  ");
                                    formatAtBatResult();
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                }
                            } else {
                                // no error made, turn double play
                                atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Out at home!, ")
                                        .append(batter.getLastName()).append(" Out at first.  Double Play!");
                                formatAtBatResult();
                                outs = outs + 2;
                                runnerOnThird = null;
                            }
                        } else {
                            if (whereBallIsHit != SCOREKEEPING_CATCHER) {
                                // throw out runner at first
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                // everyone safe
                                atBatSummary.append(", and it kicks off his glove, everyone is Safe! ");
                                formatAtBatResult();
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                            } else {
                                // Get a single out
                                if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_FIRST_BASE || whereBallIsHit == SCOREKEEPING_PITCHER || whereBallIsHit == SCOREKEEPING_CATCHER) {
                                    // runner stays, out at first
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(batter.getLastName()).append(" out at first.  ").append(runnerOnThird.getRunner().getLastName()).append(" stays at third.  ");
                                    formatAtBatResult();
                                    outs++;
                                } else {
                                    // runner advances if less than 3 outs, batter out at first
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(batter.getLastName()).append(" out at first.  ");
                                    formatAtBatResult();
                                    outs++;
                                    if (outs < 3) {
                                        advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, true);
                                        runnerOnFirst = null;
                                    }
                                }
                            }

                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit != SCOREKEEPING_THIRD_BASE) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_THIRD_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everone safe
                                    atBatSummary.append(", dropped everyone is Safe! ");
                                    formatAtBatResult();
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    outs++;
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(batter.getLastName()).append(" Out! ").append(runnerOnThird.getRunner().getLastName()).append(" advances on Error.  ");
                                    formatAtBatResult();
                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, false);
                                }
                            } else {
                                // no error, turn double play
                                atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                        .append(" and he catches ").append(runnerOnThird.getRunner().getLastName()).append(" off the base, Out! Double Play!");
                                formatAtBatResult();
                                outs = outs + 2;
                                runnerOnSecond = null;
                            }
                        } else {
                            // no double play
                            errorMade = checkForError(whereBallIsHit);
                            if (errorMade) {
                                errorOnPlay = true;
                                if (outs == 2) {
                                    areRunsEarned = false;
                                }
                                // everyone safe and advances
                                atBatSummary.append(", off his glove into the outfield, everyone is Safe! ").append(runnerOnThird.getRunner().getLastName()).append(" advances on error.  ");
                                formatAtBatResult();
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                            } else {
                                // batter is out on caught line drive, runner holds
                                atBatSummary.append(", Out! ").append(runnerOnThird.getRunner().getLastName()).append(" stays at third.  ");
                                formatAtBatResult();
                                outs++;
                            }
                        }
                    }
                    break;
                case NO_OUTS_RUNNER_ON_FIRST_AND_SECOND:
                    runnerSteals = ifRunnerSteals(runnerOnSecond);
                    if (typeOfHit == LINE_DRIVE) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 9500;
                        } else {
                            oddsOfDoublePlay = 500;
                        }
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality == SOFT) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 500;
                        } else {
                            oddsOfDoublePlay = 7500 - runnerOnFirst.getRunner().getHittingPercentages().getSpeed() - batter.getHittingPercentages().getSpeed();
                        }
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality != SOFT) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 5000;
                        } else {
                            oddsOfDoublePlay = 9500 - runnerOnFirst.getRunner().getHittingPercentages().getSpeed() - batter.getHittingPercentages().getSpeed();
                        }
                    }
                    if (typeOfHit == GROUNDBALL) {
                        setStartOfSpan();
                        atBatSummary.append("Grounder to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SECOND_BASE);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SHORTSTOP);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at second, but no double play, fielder's choice
                                    outs++;
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(batter.getLastName()).append(" out at second.  ").append(runnerOnSecond.getRunner().getLastName()).append(" advances to third.  ");
                                    formatAtBatResult();
                                    runnerOnThird = runnerOnSecond;
                                    runnerOnSecond = null;
                                    runnerOnFirst = new Runner(batter, runnerOnFirst.getPitcherResponsible(), runnerOnFirst.isEarnedRun());
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" everyone is Safe on Error! ");
                                    formatAtBatResult();
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                }
                            } else {
                                // no error made, turn double play
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, and over to ")
                                        .append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(2))).append(", Double Play!!! ");
                                outs = outs + 2;
                                if (outs < 3) {
                                    atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" advances to third.  ");
                                }
                                formatAtBatResult();
                                runnerOnThird = runnerOnSecond;
                                runnerOnFirst = null;
                            }
                        } else {
                            // No Double play, Get a single out
                            if (runnerSteals) {
                                // runner goes to second, out at first
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(" Out! Runners were stealing and advance to second and third ");
                                outs++;
                                runnerOnThird = runnerOnSecond;
                                runnerOnSecond = runnerOnFirst;
                            } else {
                                // batter beats throw to first
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(" Out!, and over to ")
                                        .append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(", Safe at first! ");
                                outs++;
                                runnerOnFirst = null;
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areRunsEarned);
                            }
                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everone safe
                                    atBatSummary.append("drops it!  Everyone Safe! ");
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    atBatSummary.append("Out!, throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" and it gets away, the runners advance to second and third.  ");
                                    outs++;
                                    runnerOnThird = runnerOnSecond;
                                    runnerOnSecond = runnerOnFirst;
                                    runnerOnFirst = null;
                                }
                            } else {
                                // no error, turn double play
                                atBatSummary.append("Out!, throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" and he catches the runner off the base, Out! Double Play! ");
                                outs = outs + 2;
                                runnerOnFirst = null;
                            }
                        }
                    }
                    break;
                case NO_OUTS_RUNNER_ON_FIRST_AND_THIRD:
                    runnerSteals = ifRunnerSteals(runnerOnFirst);
                    if (typeOfHit == LINE_DRIVE) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 9500;
                        } else {
                            oddsOfDoublePlay = 500;
                        }
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality == SOFT) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 500;
                        } else {
                            oddsOfDoublePlay = 7500 - runnerOnFirst.getRunner().getHittingPercentages().getSpeed() - batter.getHittingPercentages().getSpeed();
                        }
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality != SOFT) {
                        if (runnerSteals) {
                            oddsOfDoublePlay = 5000;
                        } else {
                            oddsOfDoublePlay = 9500 - runnerOnFirst.getRunner().getHittingPercentages().getSpeed() - batter.getHittingPercentages().getSpeed();
                        }
                    }
                    if (typeOfHit == GROUNDBALL) {
                        setStartOfSpan();
                        atBatSummary.append("Grounder to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SECOND_BASE);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SHORTSTOP);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at second, but no double play
                                    outs++;
                                    runnerOnFirst = new Runner(batter, runnerOnFirst.getPitcherResponsible(), areRunsEarned);
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                }
                            } else {
                                // no error made, turn double play
                                outs = outs + 2;
                                runnerOnFirst = null;
                            }
                        } else {
                            // Get a single out
                            if (runnerSteals) {
                                // runner goes to second, out at first
                                outs++;
                                runnerOnSecond = runnerOnFirst;
                            } else {
                                // batter beats throw to first
                                outs++;
                                runnerOnFirst = new Runner(batter, runnerOnFirst.getPitcherResponsible(), areRunsEarned);
                            }
                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everone safe
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    outs++;
                                    runnerOnSecond = runnerOnFirst;
                                    runnerOnFirst = null;
                                }
                            } else {
                                // no error, turn double play
                                outs = outs + 2;
                                runnerOnFirst = null;
                            }
                        }
                    }
                    break;
                case NO_OUTS_RUNNER_ON_SECOND_AND_THIRD:
                    if (typeOfHit == LINE_DRIVE) {
                        oddsOfDoublePlay = 500;
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality == SOFT) {
                        oddsOfDoublePlay = 500;
                    }
                    if (typeOfHit == GROUNDBALL && contactQuality != SOFT) {
                        oddsOfDoublePlay = 100;
                    }
                    if (typeOfHit == GROUNDBALL) {
                        setStartOfSpan();
                        atBatSummary.append("Grounder to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit == SCOREKEEPING_CATCHER) {
                                // Throw home tag runner and throw to first
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_CATCHER);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at home, but no double play
                                    outs++;
                                    runnerOnThird = null;
                                    runnerOnFirst = new Runner(batter, pitcher, false);
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                }
                            } else {
                                // no error made, turn double play
                                outs = outs + 2;
                                runnerOnThird = null;
                            }
                        } else {
                            if (whereBallIsHit != SCOREKEEPING_CATCHER) {
                                // throw out runner at first
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                // everyone safe
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                            } else {
                                // Get a single out
                                if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_FIRST_BASE || whereBallIsHit == SCOREKEEPING_PITCHER || whereBallIsHit == SCOREKEEPING_CATCHER) {
                                    // runner stays, out at first
                                    outs++;
                                } else {
                                    // runner advances, out at first
                                    outs++;
                                    if (outs < 3) {
                                        advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, true);
                                        runnerOnFirst = null;
                                    }
                                }
                            }

                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit != SCOREKEEPING_THIRD_BASE) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_THIRD_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everone safe
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    outs++;
                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, false);
                                }
                            } else {
                                // no error, turn double play
                                outs = outs + 2;
                                runnerOnSecond = null;
                            }
                        } else {
                            // no double play
                            errorMade = checkForError(whereBallIsHit);
                            if (errorMade) {
                                errorOnPlay = true;
                                if (outs == 2) {
                                    areRunsEarned = false;
                                }
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                            } else {
                                // batter is out on caught line drive, runner holds
                                outs++;
                            }
                        }
                    }
                    break;
                case NO_OUTS_BASES_LOADED:
                    if (typeOfHit == LINE_DRIVE) {

                        oddsOfDoublePlay = 500;

                    }
                    if (typeOfHit == GROUNDBALL && contactQuality == SOFT) {

                        oddsOfDoublePlay = 7500 - runnerOnFirst.getRunner().getHittingPercentages().getSpeed() - batter.getHittingPercentages().getSpeed();

                    }
                    if (typeOfHit == GROUNDBALL && contactQuality != SOFT) {

                        oddsOfDoublePlay = 9500 - runnerOnFirst.getRunner().getHittingPercentages().getSpeed() - batter.getHittingPercentages().getSpeed();

                    }
                    if (typeOfHit == GROUNDBALL) {
                        setStartOfSpan();
                        atBatSummary.append("Grounder to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SECOND_BASE);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_SHORTSTOP);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at second, but no double play
                                    outs++;
                                    runnerOnFirst = new Runner(batter, runnerOnFirst.getPitcherResponsible(), areRunsEarned);
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                }
                            } else {
                                // no error made, turn double play
                                outs = outs + 2;
                                runnerOnFirst = null;
                            }
                        } else {
                            // TODO : Check for errors
                            // Get a single out
                            outs++;
                            runnerOnFirst = new Runner(batter, pitcher, areRunsEarned);
                        }
                    }

                    if (typeOfHit == LINE_DRIVE) {
                        atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
                        if (chanceOfDoublePlay < oddsOfDoublePlay) {
                            // Turn double play
                            if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everone safe
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    outs++;
                                    runnerOnSecond = runnerOnFirst;
                                    runnerOnFirst = null;
                                }
                            } else {
                                // no error, turn double play
                                outs = outs + 2;
                                runnerOnFirst = null;
                            }
                        }
                    }
                    break;
                default:
            }

        } else if (typeOfHit == GROUNDBALL && whereBallIsHit < 7) {
            atBatSummary.append("Grounder to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
            // Not attempting double play, try for a single out with catch and throw
            if ((whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP) && runnerOnFirst != null) {
                fieldersWhoTouchedBall.add(whereBallIsHit);
                fieldersWhoTouchedBall.add(SCOREKEEPING_SECOND_BASE);
            } else {
                fieldersWhoTouchedBall.add(whereBallIsHit);
                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
            }
            for (Integer fielder : fieldersWhoTouchedBall) {
                errorMade = checkForError(fielder);
                if (errorMade) {
                    errorOnPlay = true;
                    if (outs == 2) {
                        areRunsEarned = false;
                    }
                    fielderWhoMadeError = fielder;
                    break;
                }
            }
            if (errorMade) {
                // all runners are safe, advance runners
                if (fielderWhoMadeError == whereBallIsHit) {
                    atBatSummary.append(", Error!!! ");
                    formatAtBatResult();
                } else {
                    atBatSummary.append(" throw to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" and it gets by him!  Error! ");
                    formatAtBatResult();
                }
                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
            } else {
                // record out, (advance runners if < 3 outs, this shouldn't happen)
                if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                    atBatSummary.append(" throw to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(", OUT! ");
                    formatAtBatResult();
                } else {
                    atBatSummary.append(" he tags the bag, OUT! ");
                    formatAtBatResult();
                }

                outs++;
                if (outs < 3) {
                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, true);
                }
            }
        } else {
            // Flyball, Line Drive
            // single out with just a catch
            setStartOfSpan();
            errorMade = checkForError(whereBallIsHit);
            if (errorMade) {
                errorOnPlay = true;
                if (outs == 2) {
                    areRunsEarned = false;
                }
                // error made on catch, everyone is safe

                if (typeOfHit == FLYBALL) {
                    atBatSummary.append("Flyball to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" and he cant't hold on, Error!!! ");
                    formatAtBatResult();
                } else if (typeOfHit == LINE_DRIVE) {
                    atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" and it kicks off his glove, Error!!! ");
                    formatAtBatResult();
                }
                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false);
            } else {
                if (typeOfHit == FLYBALL) {
                    atBatSummary.append("Flyball to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(", OUT! ");
                    formatAtBatResult();
                } else if (typeOfHit == LINE_DRIVE) {
                    atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(", OUT! ");
                    formatAtBatResult();
                } else {
                    atBatSummary.append("Ground Ball to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(", OUT!, THIS SHOULDN'T HAPPEN !!!!!!!!!  ");
                    formatAtBatResult();
                }
                outs++;
                if (outs < 3 && !basesAreEmpty() && typeOfHit == FLYBALL && contactQuality != SOFT) {
                    advanceIfRunnersTagUp(whereBallIsHit, batter, pitcher);
                }
            }
        }
        if (outs > outsAtStartOfAtBat) {
            float currentInningsPitched = pitcher.getPitchingStats().get(0).getInningsPitched();
            currentInningsPitched += (outs - outsAtStartOfAtBat) * .1;
            if (Math.round(currentInningsPitched * 10) >= (((Math.round(currentInningsPitched) * 10) + 3))) {
                currentInningsPitched = currentInningsPitched + 1.0f - 0.3f;
                currentInningsPitched = Math.round(currentInningsPitched * 10) / 10.0f;
            }
            pitcher.getPitchingStats().get(0).setInningsPitched(currentInningsPitched);
        }
        moveToNextBatterInLineup();
        atBatOver();
    }

    private void advanceIfRunnersTagUp(int whereBallWasHit, Player batter, Player pitcher) {
        int chanceOfTaggingUp = random.nextInt(ONE_HUNDERED_PERCENT);
        boolean runnerThrownOutTaggingUp = random.nextInt(ONE_HUNDERED_PERCENT) < 1000;
        setStartOfSpan();
        if (runnerOnThird != null) {
            if (chanceOfTaggingUp < runnerOnThird.getRunner().getHittingPercentages().getBaseRunning() + 5000) {
                if (runnerThrownOutTaggingUp) {
                    // runner thrown out trying to tag up
                    atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" thrown out trying to tag up! OUT!!! ");
                    formatAtBatResult();
                    outs++;
                    runnerOnThird = null;
                } else {
                    // runner tags up successfully
                    atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" tags up...SAFE!!! ").append(runnerOnThird.getRunner().getLastName()).append(" Scores! ");
                    formatScoringResult();
                    runsScored++;
                    batter.getBattingStats().get(0).setRunsBattedIn(batter.getBattingStats().get(0).getRunsBattedIn() + 1);
                    runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                    pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                    runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                    if (runnerOnThird.isEarnedRun()) {
                        runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getEarnedRuns() + 1);
                    }
                    batter.getBattingStats().get(0).setRunsBattedIn(batter.getBattingStats().get(0).getRunsBattedIn() + 1);
                    runnerOnThird = null;
                }
            }
        } else if (runnerOnSecond != null) {
            // no runner on third
            if (whereBallWasHit < 8) {
                if (chanceOfTaggingUp < runnerOnSecond.getRunner().getHittingPercentages().getBaseRunning() - 4000) {
                    if (runnerThrownOutTaggingUp) {
                        // runner thrown out trying to tag up
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" thrown out trying to tag up! OUT!!! ");
                        formatAtBatResult();
                        outs++;
                        runnerOnSecond = null;
                    } else {
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" tags up...SAFE at third!!! ");
                        formatAtBatResult();
                        // runner tags up successfully
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = null;
                    }
                }
            }
            if (whereBallWasHit == 8) {
                if (chanceOfTaggingUp < runnerOnSecond.getRunner().getHittingPercentages().getBaseRunning() - 3000) {
                    if (runnerThrownOutTaggingUp) {
                        // runner thrown out trying to tag up
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" thrown out trying to tag up! OUT!!! ");
                        formatAtBatResult();
                        outs++;
                        runnerOnSecond = null;
                    } else {
                        // runner tags up successfully
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" tags up...SAFE!!! ");
                        formatAtBatResult();
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = null;
                    }
                }
            } else {
                // Ball hit to right field
                if (chanceOfTaggingUp < runnerOnSecond.getRunner().getHittingPercentages().getBaseRunning() - 2000) {
                    if (runnerThrownOutTaggingUp) {
                        // runner thrown out trying to tag up
                        atBatSummary.append(runnerOnSecond.getRunner().getName()).append(" thrown out trying to tag up! OUT!!! ");
                        formatAtBatResult();
                        outs++;
                        runnerOnSecond = null;
                    } else {
                        // runner tags up successfully
                        atBatSummary.append(runnerOnSecond.getRunner().getName()).append(" tags up...SAFE!!! ");
                        formatAtBatResult();
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = null;
                    }
                }
            }

        } else if (runnerOnFirst != null) {
            // no runner on second or third
            if (chanceOfTaggingUp < runnerOnFirst.getRunner().getHittingPercentages().getBaseRunning() - 4000) {
                if (runnerThrownOutTaggingUp) {
                    // runner thrown out trying to tag up
                    atBatSummary.append(runnerOnFirst.getRunner().getName()).append(" thrown out trying to tag up! OUT!!! ");
                    formatAtBatResult();
                    outs++;
                    runnerOnSecond = null;
                } else {
                    // runner tags up successfully
                    atBatSummary.append(runnerOnFirst.getRunner().getName()).append(" tags up...SAFE!!! ");
                    formatAtBatResult();
                    runnerOnSecond = runnerOnFirst;
                    runnerOnFirst = null;
                }
            }
        }
    }

    private void advanceAllRunnersOneBaseOnBattedBall(Player batter, Player pitcher, boolean earnedRun) {
        boolean batterAlreadyOnBase = false;
        setStartOfSpan();
        if (runnerOnThird != null) {
            atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Scored.  ");
            formatScoringResult();
            runsScored++;
            runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
            pitcher.getPitchingStats().get(0).setRuns(pitcher.getPitchingStats().get(0).getRuns() + 1);
            if (earnedRun && runnerOnThird.isEarnedRun()) {
                runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getEarnedRuns() + 1);
            }
            if (batter != null) {
                batter.getBattingStats().get(0).setRunsBattedIn(batter.getBattingStats().get(0).getRunsBattedIn() + 1);
            }
        }
        runnerOnThird = runnerOnSecond;
        runnerOnSecond = runnerOnFirst;
        if (runnerOnThird != null || runnerOnSecond != null || runnerOnFirst != null) {
            if (runnerOnThird != null) {
                if (runnerOnThird.getRunner() == batter) {
                    batterAlreadyOnBase = true;
                }
            }
            if (runnerOnSecond != null && !batterAlreadyOnBase) {
                if (runnerOnSecond.getRunner() == batter) {
                    batterAlreadyOnBase = true;
                }
            }
            if (runnerOnFirst != null && !batterAlreadyOnBase) {
                if (runnerOnFirst.getRunner() == batter) {
                    batterAlreadyOnBase = true;
                }
            }
        }

        if (batter == null || batterAlreadyOnBase) {
            runnerOnFirst = null;
        } else {
            runnerOnFirst = new Runner(batter, pitcher, earnedRun);
        }

    }

    private void resolveHit(Player batter, Player pitcher, int typeOfHit, int contactQuality) {
        int whereBallIsHit;
        boolean runnersOnBase = !basesAreEmpty();
        boolean hitIsTriple = random.nextInt(ONE_HUNDERED_PERCENT) < batter.getHittingPercentages().getTriplePct();
        boolean hitIsDouble;
        setStartOfSpan();
        if (hitIsTriple) {
            hitIsDouble = false;
        } else {
            hitIsDouble = random.nextInt(ONE_HUNDERED_PERCENT) < batter.getHittingPercentages().getDoublePct();
        }
        if (hitIsTriple || hitIsDouble) {
            int fieldArea = getFieldArea(batter);
            switch (fieldArea) {
                case LEFT:
                    whereBallIsHit = SCOREKEEPING_LEFT_FIELD;
                    break;
                case CENTER:
                    whereBallIsHit = SCOREKEEPING_CENTER_FIELD;
                    break;
                case RIGHT:
                    whereBallIsHit = SCOREKEEPING_RIGHT_FIELD;
                    break;
                default:
                    whereBallIsHit = 0;
            }
        } else {
            whereBallIsHit = getHitLocation(batter, typeOfHit, contactQuality, false);
        }

        if (hitIsTriple) {
            // Advance all runners 3 bases, adjust stats
            atBatSummary.append("Triple to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append("!!! ");
            formatAtBatResult();
            for (int i = 0; i < 3; i++) {
                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, true);
            }
            batter.getBattingStats().get(0).setTriples(batter.getBattingStats().get(0).getTriples() + 1);
            batter.getBattingStats().get(0).setHits(batter.getBattingStats().get(0).getHits() + 1);
            pitcher.getPitchingStats().get(0).setHits(pitcher.getPitchingStats().get(0).getHits() + 1);
        } else if (hitIsDouble) {
            // Advance all runners 2 bases, adjust stats
            atBatSummary.append("Double to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append("! ");
            formatAtBatResult();
            for (int i = 0; i < 2; i++) {
                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, true);
            }
            batter.getBattingStats().get(0).setDoubles(batter.getBattingStats().get(0).getDoubles() + 1);
            batter.getBattingStats().get(0).setHits(batter.getBattingStats().get(0).getHits() + 1);
            pitcher.getPitchingStats().get(0).setHits(pitcher.getPitchingStats().get(0).getHits() + 1);
            if (runnersOnBase) {
                checkIfRunnerTakesExtraBase(whereBallIsHit, batter, pitcher);
            }
        } else {
            // Single
            atBatSummary.append("Single to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");
            formatAtBatResult();
            advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, true);
            batter.getBattingStats().get(0).setHits(batter.getBattingStats().get(0).getHits() + 1);
            batter.getBattingStats().get(0).setSingles(batter.getBattingStats().get(0).getSingles() + 1);
            pitcher.getPitchingStats().get(0).setHits(pitcher.getPitchingStats().get(0).getHits() + 1);
            if (runnersOnBase) {
                checkIfRunnerTakesExtraBase(whereBallIsHit, batter, pitcher);
            }
        }

        moveToNextBatterInLineup();
        atBatOver();
    }

    private void checkIfRunnerTakesExtraBase(int whereBallIsHit, Player batter, Player pitcher) {
        boolean wasRunnerRunningWhenBallHit;
        setStartOfSpan();
        if (runnerOnThird != null) {
            wasRunnerRunningWhenBallHit = ifRunnerSteals(runnerOnThird);
        } else if (runnerOnSecond != null && runnerOnFirst != null) {
            wasRunnerRunningWhenBallHit = ifRunnerSteals(runnerOnSecond);
        } else {
            wasRunnerRunningWhenBallHit = false;
        }
        int chanceOfAdvancing = random.nextInt(ONE_HUNDERED_PERCENT);
        boolean runnerThrownOutAdvancing = random.nextInt(ONE_HUNDERED_PERCENT) < TEN_PERCENT;

        if (runnerOnThird != null) {
            int baserunnningAdjustment = FIFTY_PERCENT;
            if (whereBallIsHit < 7) {
                baserunnningAdjustment -= ONE_HUNDERED_PERCENT;
            }
            if (wasRunnerRunningWhenBallHit) {
                baserunnningAdjustment += FIFTY_PERCENT;
            }
            if (chanceOfAdvancing < runnerOnThird.getRunner().getHittingPercentages().getBaseRunning() + baserunnningAdjustment) {
                if (runnerThrownOutAdvancing) {
                    // runner thrown out trying to tag up
                    atBatSummary.append("\n").append(runnerOnThird.getRunner().getName()).append(" thrown out trying to advance! OUT!!! ");
                    formatAtBatResult();
                    outs++;
                    runnerOnThird = null;
                } else {
                    // runner tags up successfully
                    // atBatSummary.append(runnerOnThird.getRunner().getName()).append(" takes an additional base...SAFE!!! ");
                    atBatSummary.append(" and ").append(runnerOnThird.getRunner().getLastName()).append(" Scored!!! ");
                    formatScoringResult();
                    runsScored++;
                    batter.getBattingStats().get(0).setRunsBattedIn(batter.getBattingStats().get(0).getRunsBattedIn() + 1);
                    runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                    pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                    runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                    if (runnerOnThird.isEarnedRun()) {
                        runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getEarnedRuns() + 1);
                    }
                    runnerOnThird = null;
                }
            }
        }
        boolean runnerOnSecondThrownOutAdvancing = random.nextInt(ONE_HUNDERED_PERCENT) < TEN_PERCENT;
        if (runnerOnSecond != null && runnerOnThird == null) {
            // no runner on third
            int baserunnningAdjustment = FORTY_PERCENT;
            if (whereBallIsHit < 7) {
                baserunnningAdjustment += ONE_HUNDERED_PERCENT;
            }
            if (wasRunnerRunningWhenBallHit) {
                baserunnningAdjustment -= FIFTY_PERCENT;
            }
            if (whereBallIsHit > 7) {
                baserunnningAdjustment -= TEN_PERCENT;
            }
            if (whereBallIsHit == 9) {
                baserunnningAdjustment -= TEN_PERCENT;
            }
            if (chanceOfAdvancing < runnerOnSecond.getRunner().getHittingPercentages().getBaseRunning() - baserunnningAdjustment) {
                if (runnerOnSecondThrownOutAdvancing) {
                    // runner thrown out trying to advance
                    atBatSummary.append(runnerOnSecond.getRunner().getName()).append(" thrown out trying advance! OUT!!! ");
                    formatAtBatResult();
                    outs++;
                    runnerOnSecond = null;
                } else {
                    atBatSummary.append(runnerOnSecond.getRunner().getName()).append(" takes an additional base...SAFE!!! ");
                    formatAtBatResult();
                    // runner tags up successfully
                    runnerOnThird = runnerOnSecond;
                    runnerOnSecond = null;
                }
            }

        }
    }

    private int getHitLocation(Player batter, int typeOfHit, int contactQuality, boolean outOnHit) {
        int fieldArea = getFieldArea(batter);
        int randomInt = random.nextInt(100);
        if (typeOfHit == GROUNDBALL) {
            if (contactQuality == HARD) {
                if (fieldArea == LEFT) {
                    if (outOnHit) {
                        if (randomInt < 40) {
                            return SCOREKEEPING_THIRD_BASE;
                        } else {
                            return SCOREKEEPING_SHORTSTOP;
                        }
                    } else {
                        return SCOREKEEPING_LEFT_FIELD;
                    }
                }
                if (fieldArea == CENTER) {
                    if (outOnHit) {
                        if (randomInt < 15) {
                            return SCOREKEEPING_PITCHER;
                        } else {
                            if (random.nextInt(100) < 50) {
                                return SCOREKEEPING_SHORTSTOP;
                            } else {
                                return SCOREKEEPING_SECOND_BASE;
                            }
                        }
                    } else {
                        return SCOREKEEPING_CENTER_FIELD;
                    }
                } else {
                    // Hit to right
                    if (outOnHit) {
                        if (randomInt < 40) {
                            return SCOREKEEPING_FIRST_BASE;
                        } else {
                            return SCOREKEEPING_SECOND_BASE;
                        }
                    } else {
                        return SCOREKEEPING_RIGHT_FIELD;
                    }
                }
            } else if (contactQuality == MEDIUM) {
                if (fieldArea == LEFT) {
                    if (outOnHit) {
                        if (randomInt < 40) {
                            return SCOREKEEPING_THIRD_BASE;
                        } else {
                            return SCOREKEEPING_SHORTSTOP;
                        }
                    } else {
                        return SCOREKEEPING_LEFT_FIELD;
                    }
                }
                if (fieldArea == CENTER) {
                    if (outOnHit) {
                        if (randomInt < 15) {
                            return SCOREKEEPING_PITCHER;
                        } else {
                            if (random.nextInt(100) < 50) {
                                return SCOREKEEPING_SHORTSTOP;
                            } else {
                                return SCOREKEEPING_SECOND_BASE;
                            }
                        }
                    } else {
                        return SCOREKEEPING_CENTER_FIELD;
                    }
                } else {
                    // Hit to right
                    if (outOnHit) {
                        if (randomInt < 40) {
                            return SCOREKEEPING_FIRST_BASE;
                        } else {
                            return SCOREKEEPING_SECOND_BASE;
                        }
                    } else {
                        return SCOREKEEPING_RIGHT_FIELD;
                    }
                }
            } else {
                //Soft Contact

                if (fieldArea == LEFT) {
                    if (outOnHit) {
                        if (randomInt < 3) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 15) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 60) {
                            return SCOREKEEPING_THIRD_BASE;
                        } else {
                            return SCOREKEEPING_SHORTSTOP;
                        }
                    } else {
                        if (randomInt < 3) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 5) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 25) {
                            return SCOREKEEPING_THIRD_BASE;
                        } else if (randomInt < 50) {
                            return SCOREKEEPING_SHORTSTOP;
                        } else {
                            return SCOREKEEPING_LEFT_FIELD;
                        }
                    }
                }
                if (fieldArea == CENTER) {
                    if (outOnHit) {
                        if (randomInt < 3) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 30) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 65) {
                            return SCOREKEEPING_SHORTSTOP;
                        } else {
                            return SCOREKEEPING_SECOND_BASE;
                        }
                    } else {
                        if (randomInt < 3) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 5) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 25) {
                            return SCOREKEEPING_SHORTSTOP;
                        } else if (randomInt < 50) {
                            return SCOREKEEPING_SECOND_BASE;
                        } else {
                            return SCOREKEEPING_CENTER_FIELD;
                        }
                    }
                } else {
                    // Hit to right
                    if (outOnHit) {
                        if (randomInt < 3) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 15) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 60) {
                            return SCOREKEEPING_FIRST_BASE;
                        } else {
                            return SCOREKEEPING_SECOND_BASE;
                        }
                    } else {
                        if (randomInt < 3) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 5) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 25) {
                            return SCOREKEEPING_FIRST_BASE;
                        } else if (randomInt < 50) {
                            return SCOREKEEPING_SECOND_BASE;
                        } else {
                            return SCOREKEEPING_RIGHT_FIELD;
                        }

                    }
                }
            }
        } else if (typeOfHit == LINE_DRIVE) {
            if (contactQuality == HARD || contactQuality == MEDIUM) {
                if (fieldArea == LEFT) {
                    if (outOnHit) {
                        if (randomInt < 25) {
                            return SCOREKEEPING_THIRD_BASE;
                        } else if (randomInt < 55) {
                            return SCOREKEEPING_SHORTSTOP;
                        } else {
                            return SCOREKEEPING_LEFT_FIELD;
                        }
                    } else {
                        return SCOREKEEPING_LEFT_FIELD;
                    }
                }
                if (fieldArea == CENTER) {
                    if (outOnHit) {
                        if (randomInt < 5) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 30) {
                            return SCOREKEEPING_SHORTSTOP;
                        } else if (randomInt < 55) {
                            return SCOREKEEPING_SECOND_BASE;
                        } else {
                            return SCOREKEEPING_CENTER_FIELD;
                        }
                    } else {
                        return SCOREKEEPING_CENTER_FIELD;
                    }
                } else {
                    // Hit to right
                    if (outOnHit) {
                        if (randomInt < 25) {
                            return SCOREKEEPING_FIRST_BASE;
                        } else if (randomInt < 55) {
                            return SCOREKEEPING_SECOND_BASE;
                        } else {
                            return SCOREKEEPING_RIGHT_FIELD;
                        }
                    } else {
                        return SCOREKEEPING_RIGHT_FIELD;
                    }
                }
            } else {
                //Soft Contact

                if (fieldArea == LEFT) {
                    if (outOnHit) {
                        if (randomInt < 30) {
                            return SCOREKEEPING_THIRD_BASE;
                        } else if (randomInt < 75) {
                            return SCOREKEEPING_SHORTSTOP;
                        } else {
                            return SCOREKEEPING_LEFT_FIELD;
                        }
                    } else {
                        return SCOREKEEPING_LEFT_FIELD;
                    }
                }

                if (fieldArea == CENTER) {
                    if (outOnHit) {
                        if (randomInt < 10) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 40) {
                            return SCOREKEEPING_SHORTSTOP;
                        } else if (randomInt < 75) {
                            return SCOREKEEPING_SECOND_BASE;
                        } else {
                            return SCOREKEEPING_CENTER_FIELD;
                        }
                    } else {
                        return SCOREKEEPING_CENTER_FIELD;
                    }
                } else {
                    // Hit to right
                    if (outOnHit) {
                        if (randomInt < 30) {
                            return SCOREKEEPING_FIRST_BASE;
                        } else if (randomInt < 75) {
                            return SCOREKEEPING_SECOND_BASE;
                        } else {
                            return SCOREKEEPING_RIGHT_FIELD;
                        }
                    } else {
                        return SCOREKEEPING_RIGHT_FIELD;
                    }

                }
            }
        } else {
            // Flyball
            boolean isInInfield = random.nextInt(ONE_HUNDERED_PERCENT) < batter.getHittingPercentages().getInfieldFlyBallPct();

            if (fieldArea == LEFT) {
                if (outOnHit) {
                    if (isInInfield) {
                        if (randomInt < 3) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 15) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 60) {
                            return SCOREKEEPING_THIRD_BASE;
                        } else {
                            return SCOREKEEPING_SHORTSTOP;
                        }
                    } else {
                        return SCOREKEEPING_LEFT_FIELD;
                    }
                } else {
                    return SCOREKEEPING_LEFT_FIELD;
                }
            }
            if (fieldArea == CENTER) {
                if (outOnHit) {
                    if (isInInfield) {
                        if (randomInt < 3) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 15) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 63) {
                            return SCOREKEEPING_SHORTSTOP;
                        } else {
                            return SCOREKEEPING_SECOND_BASE;
                        }
                    } else {
                        return SCOREKEEPING_CENTER_FIELD;
                    }
                } else {
                    return SCOREKEEPING_CENTER_FIELD;
                }
            } else {
                // Hit to right
                if (outOnHit) {
                    if (isInInfield) {
                        if (randomInt < 3) {
                            return SCOREKEEPING_PITCHER;
                        } else if (randomInt < 15) {
                            return SCOREKEEPING_CATCHER;
                        } else if (randomInt < 60) {
                            return SCOREKEEPING_FIRST_BASE;
                        } else {
                            return SCOREKEEPING_SECOND_BASE;
                        }
                    } else {
                        return SCOREKEEPING_RIGHT_FIELD;
                    }
                } else {
                    return SCOREKEEPING_RIGHT_FIELD;
                }
            }


        }
    }


    private int getFieldArea(Player batter) {
        int fieldArea;
        int checkForFieldArea = random.nextInt(ONE_HUNDERED_PERCENT);
        if (checkForFieldArea < batter.getHittingPercentages().getPullPct()) {
            // Batter pulled the ball
            if (batter.getHits().equals(context.getString(R.string.left_handed))) {
                fieldArea = RIGHT;
            } else {
                fieldArea = LEFT;
            }
        } else if (checkForFieldArea < (batter.getHittingPercentages().getPullPct() + batter.getHittingPercentages().getCenterPct())) {
            fieldArea = CENTER;
        } else {
            if (batter.getHits().equals(context.getString(R.string.left_handed))) {
                fieldArea = LEFT;
            } else {
                fieldArea = RIGHT;
            }
        }
        return fieldArea;
    }


    private boolean isHomeRun(Player batter, Player pitcher, int typeOfHit, int batterAdjustment, int pitcherAdjustment) {
        if (typeOfHit == LINE_DRIVE || typeOfHit == FLYBALL) {
            return oddsRatioMethod(batter.getHittingPercentages().getHomeRunPct() - batterAdjustment, pitcher.getPitchingPercentages().getHomeRunPct() + pitcherAdjustment, BATTING_HOME_RUN_PCT_MEAN);
        } else {
            return false;
        }
    }

    private boolean isHit(Player batter, int typeOfHit, int contactQuality) {
        int checkForHit = random.nextInt(ONE_HUNDERED_PERCENT);
        int defensiveShiftAdjustment = (BATTING_PULL_PCT_MEAN - batter.getHittingPercentages().getPullPct()) / 45 + (batter.getHittingPercentages().getCenterPct() - BATTING_CENTER_PCT_MEAN) / 45 +
                ((ONE_HUNDERED_PERCENT - batter.getHittingPercentages().getPullPct() - batter.getHittingPercentages().getCenterPct()) - (ONE_HUNDERED_PERCENT - BATTING_CENTER_PCT_MEAN - BATTING_PULL_PCT_MEAN)) / 45;
        if (typeOfHit == GROUNDBALL) {
            if (contactQuality == HARD) {
                return checkForHit < BATTING_HARD_GROUNDBALL_HIT_PCT + defensiveShiftAdjustment;
            } else if (contactQuality == MEDIUM) {
                return checkForHit < BATTING_MED_GROUNDBALL_HIT_PCT + defensiveShiftAdjustment;
            } else {
                return checkForHit < BATTING_SOFT_GROUNDBALL_HIT_PCT + defensiveShiftAdjustment;
            }
        } else if (typeOfHit == LINE_DRIVE) {
            if (contactQuality == HARD) {
                return checkForHit < BATTING_HARD_LINE_DRIVE_HIT_PCT;
            } else if (contactQuality == MEDIUM) {
                return checkForHit < BATTING_MED_LINE_DRIVE_HIT_PCT + defensiveShiftAdjustment;
            } else {
                return checkForHit < BATTING_SOFT_LINE_DRIVE_HIT_PCT + defensiveShiftAdjustment;
            }
        } else {
            if (contactQuality == HARD) {
                return checkForHit < BATTING_HARD_FLYBALL_HIT_PCT + defensiveShiftAdjustment;
            } else if (contactQuality == MEDIUM) {
                return checkForHit < BATTING_MED_FLYBALL_HIT_PCT + defensiveShiftAdjustment;
            } else {
                return checkForHit < BATTING_SOFT_FLYBALL_HIT_PCT + defensiveShiftAdjustment;
            }
        }
    }


    private int getQualityOfContact(Player batter, int batterAdjustment) {
        int checkForHitQuality = random.nextInt(ONE_HUNDERED_PERCENT);
        if (checkForHitQuality < batter.getHittingPercentages().getHardPct() - batterAdjustment) {
            return HARD;
        } else if (checkForHitQuality < (batter.getHittingPercentages().getHardPct() - batterAdjustment + batter.getHittingPercentages().getMedPct() - batterAdjustment)) {
            return MEDIUM;
        } else {
            return SOFT;
        }
    }

    private boolean checkForFoulBall(Player batter) {
        return random.nextInt(ONE_HUNDERED_PERCENT) < batter.getHittingPercentages().getFoulBallPct();
    }

    private int getTypeOfHit(Player batter, Player pitcher, int batterAdjustment) {
        int checkForHitType = random.nextInt(ONE_HUNDERED_PERCENT);
        if (checkForHitType < batter.getHittingPercentages().getGroundBallPct() + batterAdjustment) {
            batter.getBattingStats().get(0).setGroundBalls(batter.getBattingStats().get(0).getGroundBalls() + 1);
            pitcher.getPitchingStats().get(0).setGroundBalls(pitcher.getPitchingStats().get(0).getGroundBalls() + 1);
            return GROUNDBALL;
        } else if (checkForHitType < (batter.getHittingPercentages().getGroundBallPct() + batter.getHittingPercentages().getLineDrivePct())) {
            batter.getBattingStats().get(0).setLineDrives(batter.getBattingStats().get(0).getLineDrives() + 1);
            pitcher.getPitchingStats().get(0).setLineDrives(pitcher.getPitchingStats().get(0).getLineDrives() + 1);
            return LINE_DRIVE;
        } else {
            batter.getBattingStats().get(0).setFlyBalls(batter.getBattingStats().get(0).getFlyBalls() + 1);
            pitcher.getPitchingStats().get(0).setFlyBalls(pitcher.getPitchingStats().get(0).getFlyBalls() + 1);
            return FLYBALL;
        }
    }

    private void checkForWildPitch(Player pitcher) {
        if (basesAreEmpty()) {
            return;
        }
        if (random.nextInt(ONE_HUNDERED_PERCENT) < pitcher.getPitchingPercentages().getWildPitchPct()) {
            atBatSummary.append(" Wild Pitch! ");
            pitcher.getPitchingStats().get(0).setWildPitches(pitcher.getPitchingStats().get(0).getWildPitches() + 1);
            if (runnerOnThird != null) {
                runsScored++;
                setStartOfSpan();
                atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Scores from Third!!! ");
                formatScoringResult();
                runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                runnerOnThird = null;
            }
            runnerOnThird = runnerOnSecond;
            runnerOnSecond = runnerOnFirst;
            runnerOnFirst = null;
        }
    }

    private void addStrikeAndCheckResults(Player batter, Player pitcher) {
        // Add a strike to count, if strikeout change gameState (if not 3 outs and runners on base check stealing)
        strikes++;
        if (strikes == 3) {
            // Record an out, check stealing if necessary, update stats and change game state
            setStartOfSpan();
            atBatSummary.append(batter.getLastName()).append(" Strikes Out! ");
            formatAtBatResult();
            outs++;
            batter.getBattingStats().get(0).setStrikeOuts(batter.getBattingStats().get(0).getStrikeOuts() + 1);
            pitcher.getPitchingStats().get(0).setStrikeOuts(pitcher.getPitchingStats().get(0).getStrikeOuts() + 1);
            if (outs < 3) {
                if (!basesAreEmpty()) {
                    // less than 3 outs and runners on base
                    checkForStealingAndUpdate(getGameState());
                    moveToNextBatterInLineup();
                    atBatOver();
                } else {
                    // less than 3 outs and nobody on base
                    moveToNextBatterInLineup();
                    atBatOver();
                }
            } else {
                // three outs
                moveToNextBatterInLineup();
                atBatOver();
            }
        } else {
            checkForStealingAndUpdate(getGameState());
        }

    }

    private void checkForStealingAndUpdate(int gameState) {

        // get the game state adjusted for no outs
        gameState -= (outs * 8);
        int fielderReceivingThrow;
        boolean error;
        switch (gameState) {
            case NO_OUTS_NOBODY_ON:
                return;
            case NO_OUTS_RUNNER_ON_FIRST:
                if (ifRunnerSteals(runnerOnFirst)) {
                    setStartOfSpan();
                    // atBatSummary.append("Stolen Base Attempt : ");
                    fielderReceivingThrow = getFielderRecevingThrow();
                    // check for error on throw (catcher) or on catch (fielderReceivingThrow)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(fielderReceivingThrow));
                    if (error) {
                        errorOnPlay = true;
                        if (outs == 2) {
                            areRunsEarned = false;
                        }
                    }
                    if (ifRunnerIsSuccessful(runnerOnFirst)) {
                        //Runner stole the base
                        atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" Stole Second Base! ");
                        formatAtBatResult();
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;
                        runnerOnSecond.getRunner().getBattingStats().get(0).setStolenBases(runnerOnSecond.getRunner().getBattingStats().get(0).getStolenBases() + 1);

                        if (error) {
                            atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(", advances to Third on Error! ");
                            formatAtBatResult();
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                        }
                        return;
                    } else {
                        if (error) {
                            atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" Safe at Second Base on Error! ");
                            formatAtBatResult();
                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                            return;
                        } else {
                            //Runner is caught stealing
                            atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" OUT! Caught Stealing Second Base! ");
                            formatAtBatResult();
                            outs++;
                            runnerOnFirst.getRunner().getBattingStats().get(0).setCaughtStealing(runnerOnFirst.getRunner().getBattingStats().get(0).getCaughtStealing() + 1);
                            runnerOnFirst = null;
                            if (outs == 3) {
                                atBatOver();
                            }
                            return;
                        }
                    }
                } else return;
            case NO_OUTS_RUNNER_ON_SECOND:
                if (ifRunnerSteals(runnerOnSecond)) {
                    setStartOfSpan();
                    atBatSummary.append("Stolen Base Attempt : ");
                    // check for error on throw (catcher) or on catch (third base)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(SCOREKEEPING_THIRD_BASE));
                    if (error) {
                        errorOnPlay = true;
                        if (outs == 2) {
                            areRunsEarned = false;
                        }
                    }
                    if (ifRunnerIsSuccessful(runnerOnSecond)) {
                        //Runner stole the base
                        atBatSummary.append(runnerOnSecond.getRunner().getName()).append(" Stole Third Base! ");
                        formatAtBatResult();
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = null;
                        runnerOnThird.getRunner().getBattingStats().get(0).setStolenBases(runnerOnThird.getRunner().getBattingStats().get(0).getStolenBases() + 1);
                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runner
                            setStartOfSpan();
                            atBatSummary.append(runnerOnThird.getRunner().getName()).append(" Scores on the Error! ");
                            formatScoringResult();
                            runsScored++;
                            runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                            runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                            runnerOnThird = null;
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner safe on error, advance runner but no credit for stolen base
                            atBatSummary.append(runnerOnSecond.getRunner().getName()).append(" Safe at Third Base on the Error! ");
                            formatAtBatResult();
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                            return;
                        } else {
                            //Runner is caught stealing
                            atBatSummary.append(runnerOnSecond.getRunner().getName()).append(" OUT! Caught Stealing Third Base! ");
                            formatAtBatResult();
                            outs++;
                            runnerOnSecond.getRunner().getBattingStats().get(0).setCaughtStealing(runnerOnSecond.getRunner().getBattingStats().get(0).getCaughtStealing() + 1);
                            runnerOnSecond = null;
                            if (outs == 3) {
                                atBatOver();
                            }
                        }
                        return;
                    }
                } else return;
            case NO_OUTS_RUNNER_ON_THIRD:
                // No stealing home right now, maybe add in later update but percenatge is VERY small
                return;
            case NO_OUTS_RUNNER_ON_FIRST_AND_SECOND:
                if (ifRunnerSteals(runnerOnSecond)) {
                    setStartOfSpan();
                    // check for error on throw (catcher) or on catch (third base)
                    atBatSummary.append("Stolen Base Attempt : ");
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(SCOREKEEPING_THIRD_BASE));
                    if (error) {
                        errorOnPlay = true;
                        if (outs == 2) {
                            areRunsEarned = false;
                        }
                    }
                    if (ifRunnerIsSuccessful(runnerOnSecond)) {
                        //Runner stole the base
                        atBatSummary.append("Double Steal!!! ").append(runnerOnSecond.getRunner().getLastName()).append(" Stole Third! and ").append(runnerOnFirst.getRunner().getLastName())
                                .append(" Stole Second! ");
                        formatAtBatResult();
                        runnerOnSecond.getRunner().getBattingStats().get(0).setStolenBases(runnerOnSecond.getRunner().getBattingStats().get(0).getStolenBases() + 1);
                        runnerOnFirst.getRunner().getBattingStats().get(0).setStolenBases(runnerOnFirst.getRunner().getBattingStats().get(0).getStolenBases() + 1);
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;

                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            atBatSummary.append("Runners advance on Error ").append(runnerOnThird.getRunner().getName()).append(" Scores!!! ").append(runnerOnSecond.getRunner().getName())
                                    .append(" advances to Third.  ");
                            formatAtBatResult();
                            runsScored++;
                            runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                            runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner safe on error, advance runner but no credit for stolen base
                            atBatSummary.append("Double Steal!!! ").append(runnerOnSecond.getRunner().getName()).append(" safe at Third on Error and ").append(runnerOnFirst.getRunner().getName())
                                    .append(" advances to Second Base! ");
                            formatAtBatResult();
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                            return;
                        } else {
                            //Runner is caught stealing
                            atBatSummary.append("Double Steal!!! ").append(runnerOnSecond.getRunner().getName()).append(" OUT at Third Base! and ").append(runnerOnFirst.getRunner().getName())
                                    .append(" advances to Second Base! ");
                            formatAtBatResult();
                            outs++;
                            runnerOnSecond.getRunner().getBattingStats().get(0).setCaughtStealing(runnerOnSecond.getRunner().getBattingStats().get(0).getCaughtStealing() + 1);
                            runnerOnSecond = null;
                            if (outs == 3) {
                                atBatOver();
                            } else {
                                runnerOnSecond = runnerOnFirst;
                                runnerOnFirst = null;
                            }
                            return;
                        }
                    }

                } else return;

            case NO_OUTS_RUNNER_ON_FIRST_AND_THIRD:
                if (ifRunnerSteals(runnerOnFirst)) {
                    setStartOfSpan();
                    //atBatSummary.append("Stolen Base Attempt : ");
                    fielderReceivingThrow = getFielderRecevingThrow();
                    // check for error on throw (catcher) or on catch (fielderReceivingThrow)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(fielderReceivingThrow));
                    if (error) {
                        errorOnPlay = true;
                        if (outs == 2) {
                            areRunsEarned = false;
                        }
                    }
                    if (ifRunnerIsSuccessful(runnerOnFirst)) {
                        //Runner stole the base
                        atBatSummary.append(runnerOnFirst.getRunner().getName()).append(" Stole Second Base! ");
                        formatAtBatResult();
                        runnerOnFirst.getRunner().getBattingStats().get(0).setStolenBases(runnerOnFirst.getRunner().getBattingStats().get(0).getStolenBases() + 1);
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;

                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            atBatSummary.append(runnerOnSecond.getRunner().getName()).append(" advances to Third on Error, ").append(runnerOnThird.getRunner().getName()).append(" Scores on Error!!! ");
                            formatScoringResult();
                            runsScored++;
                            runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                            runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            atBatSummary.append(runnerOnFirst.getRunner().getName()).append(" Safe at Second on Error, ").append(runnerOnThird.getRunner().getName()).append(" Scores on Error!!! ");
                            formatScoringResult();
                            runsScored++;
                            runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                            runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                            runnerOnThird = null;
                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                            return;
                        } else {
                            //Runner is caught stealing
                            atBatSummary.append(runnerOnFirst.getRunner().getName()).append(" Caught Stealing, OUT at Second!!! ");
                            formatAtBatResult();
                            runnerOnFirst.getRunner().getBattingStats().get(0).setCaughtStealing(runnerOnFirst.getRunner().getBattingStats().get(0).getCaughtStealing() + 1);
                            outs++;
                            runnerOnFirst = null;

                            if (outs == 3) {
                                atBatOver();
                            }
                            return;
                        }
                    }
                } else return;
            case NO_OUTS_RUNNER_ON_SECOND_AND_THIRD:
                return;
            case NO_OUTS_BASES_LOADED:
                return;
            default:
                return;
        }
    }

    private int getFielderRecevingThrow() {
        if (lineup.get(currentBatter).getHits().equals(context.getString(R.string.left_handed))) {
            // Shortstop covers when batter is left-handed
            return SCOREKEEPING_SHORTSTOP;
        } else if (lineup.get(currentBatter).getHits().equals(context.getString(R.string.right_handed))) {
            // Second covers when batter is right-handed
            return SCOREKEEPING_SECOND_BASE;
        } else if (defense.get(SCOREKEEPING_PITCHER).getThrowingHand().equals(context.getString(R.string.left_handed))) {
            // If batter is a switch hitter, he hits right-handed when pitcher is left-handed, so second covers
            return SCOREKEEPING_SECOND_BASE;
        } else {
            // If batter is a switch hitter, he hits left-handed when pitcher is right-handed, so shortstop covers
            return SCOREKEEPING_SHORTSTOP;
        }
    }

    private boolean checkForError(int playerPositionNumber) {
        int playerErrorRate = defense.get(playerPositionNumber).getHittingPercentages().getErrorPct();
        if (random.nextInt(ONE_HUNDERED_PERCENT) < playerErrorRate) {
            // Error committed add to stats and return true
            atBatSummary.append(", Error : ").append(getPositionNameFromScorekeeperPosition(playerPositionNumber)).append(" , ");
            defense.get(playerPositionNumber).getBattingStats().get(0).setErrors(defense.get(playerPositionNumber).getBattingStats().get(0).getErrors() + 1);
            return true;
        }
        // no error, return false
        return false;
    }

    private boolean ifRunnerIsSuccessful(Runner runner) {
        int stolenBasePct = runner.getRunner().getHittingPercentages().getStolenBasePct();
        if (runnerOnSecond != null) {
            stolenBasePct -= 500;

        }
        return (random.nextInt(ONE_HUNDERED_PERCENT) < stolenBasePct);
    }

    private boolean ifRunnerSteals(Runner runner) {
        int stolenBaseRate = runner.getRunner().getHittingPercentages().getStolenBaseRate();
        if (runnerOnSecond == runner) {
            stolenBaseRate = stolenBaseRate / 5;

        }
        return (random.nextInt(ONE_HUNDERED_PERCENT) < stolenBaseRate);
    }

    private int getGameState() {
        int gameState = 0;
        if (outs == 3) {
            gameState = 25;
        } else {
            gameState = 1 + (outs * 8);
            if (runnerOnFirst != null) {
                // runner on first
                if (runnerOnSecond != null) {
                    if (runnerOnThird != null) {
                        //bases loaded
                        gameState += 7;
                    } else {
                        // runners on first and second
                        gameState += 4;
                    }
                } else {
                    // no runner on second
                    if (runnerOnThird != null) {
                        // runners on first and third
                        gameState += 5;
                    } else {
                        // only runner on first
                        gameState += 1;
                    }
                }
            } else {
                //no runner on first
                if (runnerOnSecond != null) {
                    //runner on second
                    if (runnerOnThird != null) {
                        //runners on second and third
                        gameState += 6;
                    } else {
                        // runner on second
                        gameState += 2;
                    }
                } else {
                    // no runner on second
                    if (runnerOnThird != null) {
                        // runner on third
                        gameState += 3;
                    }
                }
            }
        }
        return gameState;
    }

    private boolean oddsRatioMethod(int batterPct, int pitcherPct, int leagueAveragePct) {
        // This formula is from http://www.insidethebook.com/ee/index.php/site/comments/the_odds_ratio_method/
        return random.nextInt(ONE_HUNDERED_PERCENT) < ((batterPct * pitcherPct) / leagueAveragePct);
    }

    private boolean isPitchInStrikeZone(Player pitcher) {
        int randomToCheck = random.nextInt(ONE_HUNDERED_PERCENT);
        /*if (balls == 0 && strikes == 0) {
            if (randomToCheck < pitcher.getPitchingPercentages().getFirstPitchStrikePct()) {
                //In the strike zone
                return true;
            } else {
                //Outside the strike zone
                return false;
            }
        } else */
        if (randomToCheck < pitcher.getPitchingPercentages().getZonePct()) {
            //In the strike zone
            return true;
        } else {
            //Outside the strike zone
            return false;
        }

    }

    private boolean isPitchThrown(Player pitcher) {
        if (random.nextInt(ONE_HUNDERED_PERCENT) < pitcher.getPitchingPercentages().getBalkPct()) {
            //Balk - add it to the pitchers stats, then check runners
            PitchingStats currentPitchingStats = pitcher.getPitchingStats().get(0);
            currentPitchingStats.setBalks(currentPitchingStats.getBalks() + 1);

            //Check if bases are empty...if so add a ball to the count and return.
            if (basesAreEmpty()) {
                balls++;
                //No Pitch Thrown
                return false;
            }
            //Otherwise advance runners and adjust stats
            if (runnerOnThird != null) {
                runnerOnThird.getRunner().getBattingStats().get(0).setRuns(runnerOnThird.getRunner().getBattingStats().get(0).getRuns() + 1);
                runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStats().get(0).getRuns() + 1);
                //We've advanced the runner and adjusted stats, now empty third base
                runnerOnThird = null;
            }
            if (runnerOnSecond != null) {
                //move runner to third and empty second base
                runnerOnThird = runnerOnSecond;
                runnerOnSecond = null;
            }
            if (runnerOnFirst != null) {
                //move runner to second and empty first base
                runnerOnSecond = runnerOnFirst;
                runnerOnFirst = null;
            }
            //No Pitch Thrown
            return false;
        } else {
            //Throw Pitch
            pitcher.getPitchingPercentages().setPitchingStaminaUsed(pitcher.getPitchingPercentages().getPitchingStaminaUsed() + 1);
            return true;
        }
    }

    private boolean basesAreEmpty() {
        return (runnerOnFirst == null && runnerOnSecond == null && runnerOnThird == null);
    }

    public int getRunsScored() {
        return runsScored;
    }

    public Runner[] getRunners() {
        runners[0] = runnerOnFirst;
        runners[1] = runnerOnSecond;
        runners[2] = runnerOnThird;
        return runners;
    }

    public int[] getCount() {
        count[0] = balls;
        count[1] = strikes;
        return count;
    }

    public int getOuts() {
        return outs;
    }

    public void setCurrentBatter(int currentBatter) {
        this.currentBatter = currentBatter;
    }

    public void setDefense(TreeMap<Integer, Player> defense) {
        this.defense = defense;
    }

    public void setLineup(TreeMap<Integer, Player> lineup) {
        this.lineup = lineup;
    }

    public TreeMap<Integer, Player> getDefense() {
        return defense;
    }

    public int getCurrentBatter() {
        return currentBatter;
    }

    public void setRunners(Runner[] runners) {
        for (int i = 0; i < runners.length; i++) {
            this.runners[i] = runners[i];
        }
        runnerOnFirst = runners[0];
        runnerOnSecond = runners[1];
        runnerOnThird = runners[2];
    }

    public void setCount(int[] count) {
        this.count = count;
    }

    public void setOuts(int outs) {
        this.outs = outs;
    }

    public TreeMap<Integer, Player> getLineup() {
        return lineup;
    }

    public boolean wasErrorMade() {
        return errorOnPlay;
    }

    public static SpannableStringBuilder getAtBatSummary() {
        return atBatSummary;
    }

    public List<Player> getPitchersResponsible() {
        return pitchersResponsible;
    }
}
