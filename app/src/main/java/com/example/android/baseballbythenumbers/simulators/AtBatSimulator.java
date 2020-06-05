package com.example.android.baseballbythenumbers.simulators;

import android.text.SpannableStringBuilder;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.example.android.baseballbythenumbers.R;
import com.example.android.baseballbythenumbers.ResourceProvider;
import com.example.android.baseballbythenumbers.data.BattingLine;
import com.example.android.baseballbythenumbers.data.PitchingLine;
import com.example.android.baseballbythenumbers.data.PitchingStats;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.data.Runner;
import com.example.android.baseballbythenumbers.repository.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_CENTER_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_PULL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.FieldArea.CENTER;
import static com.example.android.baseballbythenumbers.constants.Constants.FieldArea.LEFT;
import static com.example.android.baseballbythenumbers.constants.Constants.FieldArea.RIGHT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_HARD_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_HARD_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_HARD_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_MED_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_MED_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_MED_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_SOFT_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_SOFT_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_SOFT_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.QualityOfContact.HARD;
import static com.example.android.baseballbythenumbers.constants.Constants.QualityOfContact.MEDIUM;
import static com.example.android.baseballbythenumbers.constants.Constants.QualityOfContact.SOFT;
import static com.example.android.baseballbythenumbers.constants.Constants.TypeOfHit.FLYBALL;
import static com.example.android.baseballbythenumbers.constants.Constants.TypeOfHit.GROUNDBALL;
import static com.example.android.baseballbythenumbers.constants.Constants.TypeOfHit.LINE_DRIVE;
import static com.example.android.baseballbythenumbers.constants.GameStates.NO_OUTS_BASES_LOADED;
import static com.example.android.baseballbythenumbers.constants.GameStates.NO_OUTS_NOBODY_ON;
import static com.example.android.baseballbythenumbers.constants.GameStates.NO_OUTS_RUNNER_ON_FIRST;
import static com.example.android.baseballbythenumbers.constants.GameStates.NO_OUTS_RUNNER_ON_FIRST_AND_SECOND;
import static com.example.android.baseballbythenumbers.constants.GameStates.NO_OUTS_RUNNER_ON_FIRST_AND_THIRD;
import static com.example.android.baseballbythenumbers.constants.GameStates.NO_OUTS_RUNNER_ON_SECOND;
import static com.example.android.baseballbythenumbers.constants.GameStates.NO_OUTS_RUNNER_ON_SECOND_AND_THIRD;
import static com.example.android.baseballbythenumbers.constants.GameStates.NO_OUTS_RUNNER_ON_THIRD;
import static com.example.android.baseballbythenumbers.constants.Positions.FIRST_BASE;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_CATCHER;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_CENTER_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_FIRST_BASE;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_LEFT_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_PITCHER;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_RIGHT_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_SECOND_BASE;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_SHORTSTOP;
import static com.example.android.baseballbythenumbers.constants.Positions.SCOREKEEPING_THIRD_BASE;
import static com.example.android.baseballbythenumbers.constants.Positions.getPositionNameFromScorekeeperPosition;

public class AtBatSimulator {

    private static final int ONE_HUNDRED_PERCENT = 10000;
    private static final int FIFTY_PERCENT = 5000;
    private static final int TEN_PERCENT = 1000;
    private static final int FORTY_PERCENT = 4000;
    private int[] count;

    private int outs;

    private int outsAtStartOfAtBat;

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

    private boolean hitOnPlay;

    private static SpannableStringBuilder atBatSummary;

    private int startOfSpan;

    private boolean areAllRunsEarned;

    private List<Player> pitchersResponsible;

    private PitchingLine pitchingLineForCurrentPitcher;

    private BattingLine battingLineForCurrentBatter;
    private int year;
    private Repository repository;
    private ResourceProvider resourceProvider;
    private TreeMap<Integer, Pair<Integer, Boolean>> animationData;

    public AtBatSimulator(ResourceProvider resourceProvider, int[] count, int outs, int currentBatter, @NonNull TreeMap<Integer, Player> lineup, @NonNull Runner[] runners, @NonNull TreeMap<Integer, Player> defense, BattingLine battingLineForCurrentBatter, PitchingLine pitchingLineForCurrentPitcher, int year, Repository repository) {
        this.count = count;
        this.outs = outs;
        this.outsAtStartOfAtBat = outs;
        this.currentBatter = currentBatter;
        this.lineup = lineup;
        this.runners = runners;
        this.defense = defense;
        this.random = new Random();
        this.resourceProvider = resourceProvider;
        this.battingLineForCurrentBatter = battingLineForCurrentBatter;
        this.pitchingLineForCurrentPitcher = pitchingLineForCurrentPitcher;
        this.year = year;
        this.repository = repository;

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

        hitOnPlay = false;

        areAllRunsEarned = true;

        pitchersResponsible = new ArrayList<>();

        resetAnimationData();
    }

    private void resetAnimationData() {
        animationData = new TreeMap<>();
        Pair<Integer, Boolean> noMovement = new Pair<>(0, true);
        animationData.put(0, noMovement);
        if (runnerOnFirst != null) {
            animationData.put(1, noMovement);
        } else {
            animationData.remove(1);
        }
        if (runnerOnSecond != null) {
            animationData.put(2, noMovement);
        } else {
            animationData.remove(2);
        }
        if (runnerOnThird != null) {
            animationData.put(3, noMovement);
        } else {
            animationData.remove(3);
        }
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
            if (count != null) {
                Timber.e(e, " Illegal Count passed was %s", Arrays.toString(count));
            }
        }
    }


    public int simulateAtBat(int pitcherStaminaAdjustment, int batterStaminaAdjustment, boolean areAllRunsEarned) {
        runsScored = 0;
        this.areAllRunsEarned = areAllRunsEarned;
        if (!areAllRunsEarned) {
            for (Runner runner : runners) {
                if (runner != null) {
                    runner.setEarnedRun(false);
                }
            }
        }

        Player batter = lineup.get(currentBatter);
        Player pitcher = defense.get(1);


        while (gameStateAfterAtBat == 0) {
            simulatePitch(pitcherStaminaAdjustment, batterStaminaAdjustment, areAllRunsEarned, batter, pitcher);
        }

        if (batter != null) {
            batter.getHittingPercentages().setStaminaUsed(batter.getHittingPercentages().getStaminaUsed() + 5);
        }
        return gameStateAfterAtBat;
    }


    public void initiateAtBat(boolean areAllRunsEarned) {
        runsScored = 0;
        this.areAllRunsEarned = areAllRunsEarned;
        if (!areAllRunsEarned) {
            for (Runner runner : runners) {
                if (runner != null) {
                    runner.setEarnedRun(false);
                }
            }
        }
    }


    public void simulatePitch(int pitcherStaminaAdjustment, int batterStaminaAdjustment, boolean areAllRunsEarned, Player batter, Player pitcher) {
        setStartOfSpan();
        if (isPitchThrown(pitcher)) {
            if (isPitchInStrikeZone(pitcher)) {
                pitchingLineForCurrentPitcher.incrementStrikesThrown();
                repository.updatePitchingLine(pitchingLineForCurrentPitcher);
                //Pitch in Strike Zone, check for batter swing
                int pitcherZSwingPct = pitcher.getPitchingPercentages().getZSwingPct() + pitcherStaminaAdjustment;  //swinging at pitches in zone is worse for pitcher (less strikeouts), so add adjustment
                int batterZSwingPct = batter.getHittingPercentages().getZSwingPct() - batterStaminaAdjustment; // swinging at pitches in zone is better for batter so subtract adjustment
                if (oddsRatioMethod(batterZSwingPct, pitcherZSwingPct, BATTING_Z_SWING_PCT_MEAN)) {
                    //Batter swings at the pitch, check for contact
                    int batterZContact = batter.getHittingPercentages().getZContactPct() - batterStaminaAdjustment;
                    int pitcherZContactPct = pitcher.getPitchingPercentages().getZContactPct() + pitcherStaminaAdjustment;
                    if (oddsRatioMethod(batterZContact, pitcherZContactPct, BATTING_Z_CONTACT_PCT_MEAN)) {
                        // Batter makes contact
                        resolveContact(batter, pitcher, batterStaminaAdjustment, pitcherStaminaAdjustment, areAllRunsEarned);
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
                    pitchingLineForCurrentPitcher.incrementStrikesThrown();
                    repository.updatePitchingLine(pitchingLineForCurrentPitcher);
                    //Batter swings at the pitch
                    int batterOContact = batter.getHittingPercentages().getOContactPct() - batterStaminaAdjustment;
                    int pitcherOContactPct = pitcher.getPitchingPercentages().getOContactPct() + pitcherStaminaAdjustment;
                    if (oddsRatioMethod(batterOContact, pitcherOContactPct, BATTING_O_CONTACT_PCT_MEAN)) {
                        // Batter makes contact
                        resolveContact(batter, pitcher, batterStaminaAdjustment, pitcherStaminaAdjustment, areAllRunsEarned);
                    } else {
                        // Batter swings and misses
                        atBatSummary.append("Strike Swinging, ");
                        checkForWildPitch(pitcher);
                        addStrikeAndCheckResults(batter, pitcher);
                    }
                } else {
                    //Batter takes the pitch
                    if (!isHitByPitch(batter, pitcher)) {
                        checkForWildPitch(pitcher);
                        addBallAndCheckResults(batter, pitcher);
                    }
                }
            }
        }
    }

    private void addBallAndCheckResults(Player batter, Player pitcher) {
        // add ball to count, check stealing, if ball four, advance runners as needed and update stats and game state
        balls++;
        atBatSummary.append("Ball, ");
        if (balls == 4) {
            setStartOfSpan();
            atBatSummary.append(batter.getLastName()).append(" Walked ");

            // Update batter and pitcher stats to reflect walk
            batter.getBattingStatsForYear(year).incrementWalks();
            battingLineForCurrentBatter.incrementWalks();
            repository.updateBattingLine(battingLineForCurrentBatter);
            pitcher.getPitchingStatsForYear(year).incrementWalks();
            pitchingLineForCurrentPitcher.incrementWalksAllowed();
            repository.updatePitchingLine(pitchingLineForCurrentPitcher);
            advanceRunnersOnWalkOrHBP(batter, pitcher);
        } else {
            checkForStealingAndUpdate(getGameState(), pitcher);
        }
    }

    private void advanceRunnersOnWalkOrHBP(Player batter, Player pitcher) {
        if (runnerOnFirst == null) {
            // No runner on first, move batter to first and game state
            runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, pitcher, pitchingLineForCurrentPitcher, true);
            animationData.put(0, new Pair<>(1, true));
            moveToNextBatterInLineup();
            atBatOver(pitcher);

        } else {
            if (runnerOnSecond == null) {
                // runner on first, nobody on second, move batter to first and runner on first to second
                runnerOnSecond = runnerOnFirst;
                runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, pitcher, pitchingLineForCurrentPitcher, true);
                animationData.put(0, new Pair<>(1, true));
                animationData.put(1, new Pair<>(1, true));

            } else {
                if (runnerOnThird == null) {
                    // runners on first and second
                    runnerOnThird = runnerOnSecond;
                    runnerOnSecond = runnerOnFirst;
                    runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, pitcher, pitchingLineForCurrentPitcher, true);
                    animationData.put(0, new Pair<>(1, true));
                    animationData.put(1, new Pair<>(1, true));
                    animationData.put(2, new Pair<>(1, true));

                } else {
                    // bases loaded
                    runsScored++;
                    batter.getBattingStatsForYear(year).setRunsBattedIn(batter.getBattingStatsForYear(year).getRunsBattedIn() + 1);
                    battingLineForCurrentBatter.setRbis(battingLineForCurrentBatter.getRbis() + 1);
                    repository.updateBattingLine(battingLineForCurrentBatter);
                    runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                    runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);

                    pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                    if (runnerOnThird.isEarnedRun()) {
                        runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getEarnedRuns() + 1);
                        runnerOnThird.getPitchingLineForPitcherResponsible().setEarnedRuns(runnerOnThird.getPitchingLineForPitcherResponsible().getEarnedRuns() + 1);
                    }
                    repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                    runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                    runnerOnThird.getBattingLineForRunner().incrementRuns();
                    repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                    runnerOnThird = runnerOnSecond;
                    runnerOnSecond = runnerOnFirst;
                    runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, pitcher, pitchingLineForCurrentPitcher, true);
                    animationData.put(0, new Pair<>(1, true));
                    animationData.put(1, new Pair<>(1, true));
                    animationData.put(2, new Pair<>(1, true));
                    animationData.put(3, new Pair<>(1, true));
                }
            }
            moveToNextBatterInLineup();
            atBatOver(pitcher);
        }
    }


    private void moveToNextBatterInLineup() {
        lineup.get(currentBatter).getBattingStatsForYear(year).incrementPlateAppearances();
        defense.get(SCOREKEEPING_PITCHER).getPitchingStatsForYear(year).incrementTotalBattersFaced();
        if (currentBatter == 9) {
            currentBatter = 1;
        } else {
            currentBatter++;
        }
    }

    private void atBatOver(Player pitcher) {
        if (outs > outsAtStartOfAtBat) {
            pitcher.getPitchingStatsForYear(year).addToInningsPitched(outs - outsAtStartOfAtBat);
            pitchingLineForCurrentPitcher.addToInningsPitched(outs-outsAtStartOfAtBat);
            repository.updatePitchingLine(pitchingLineForCurrentPitcher);
        }
        gameStateAfterAtBat = getGameState();
    }

    private void resolveContact(Player batter, Player pitcher, int batterStaminaAdjustment, int pitcherStaminaAdjustment, boolean areRunsEarned) {
        int typeOfHit = getTypeOfHit(batter, pitcher, batterStaminaAdjustment);
        if (isHomeRun(batter, pitcher, typeOfHit, batterStaminaAdjustment, pitcherStaminaAdjustment)) {
            hitOnPlay = true;
            List<String> runnersThatScored = new ArrayList<>();
            int numberOfRunners = 0;
            if (runnerOnThird != null) {
                numberOfRunners++;
                runsScored++;
                runnersThatScored.add(runnerOnThird.getRunner().getLastName());
                if (runnerOnThird.isEarnedRun() && areRunsEarned) {
                    runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getEarnedRuns() + 1);
                    runnerOnThird.getPitchingLineForPitcherResponsible().setEarnedRuns(runnerOnThird.getPitchingLineForPitcherResponsible().getEarnedRuns() + 1);
                }
                runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                runnerOnThird = null;
                animationData.put(3, new Pair<>(1, true));
            }
            if (runnerOnSecond != null) {
                numberOfRunners++;
                runsScored++;
                runnersThatScored.add(runnerOnSecond.getRunner().getLastName());
                runnerOnSecond.getRunner().getBattingStatsForYear(year).incrementRuns();
                runnerOnSecond.getBattingLineForRunner().incrementRuns();
                repository.updateBattingLine(runnerOnSecond.getBattingLineForRunner());
                if (runnerOnSecond.isEarnedRun() && areRunsEarned) {
                    runnerOnSecond.getPitcherResponsible().getPitchingStatsForYear(year).setEarnedRuns(runnerOnSecond.getPitcherResponsible().getPitchingStatsForYear(year).getEarnedRuns() + 1);
                    runnerOnSecond.getPitchingLineForPitcherResponsible().setEarnedRuns(runnerOnSecond.getPitchingLineForPitcherResponsible().getEarnedRuns() + 1);
                }
                runnerOnSecond.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnSecond.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                runnerOnSecond.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnSecond.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                repository.updatePitchingLine(runnerOnSecond.getPitchingLineForPitcherResponsible());
                pitchersResponsible.add(runnerOnSecond.getPitcherResponsible());
                runnerOnSecond = null;
                animationData.put(2, new Pair<>(2, true));
            }
            if (runnerOnFirst != null) {
                numberOfRunners++;
                runsScored++;
                runnersThatScored.add(runnerOnFirst.getRunner().getLastName());
                runnerOnFirst.getRunner().getBattingStatsForYear(year).incrementRuns();
                runnerOnFirst.getBattingLineForRunner().incrementRuns();
                repository.updateBattingLine(runnerOnFirst.getBattingLineForRunner());
                if (runnerOnFirst.isEarnedRun() && areRunsEarned) {
                    runnerOnFirst.getPitcherResponsible().getPitchingStatsForYear(year).setEarnedRuns(runnerOnFirst.getPitcherResponsible().getPitchingStatsForYear(year).getEarnedRuns() + 1);
                    runnerOnFirst.getPitchingLineForPitcherResponsible().setEarnedRuns(runnerOnFirst.getPitchingLineForPitcherResponsible().getEarnedRuns() + 1);
                }
                runnerOnFirst.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnFirst.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                runnerOnFirst.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnFirst.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                repository.updatePitchingLine(runnerOnFirst.getPitchingLineForPitcherResponsible());
                pitchersResponsible.add(runnerOnFirst.getPitcherResponsible());
                runnerOnFirst = null;
                animationData.put(1, new Pair<>(3, true));
            }

            runsScored++;
            setStartOfSpan();
            if (numberOfRunners == 0) {
                atBatSummary.append(batter.getLastName()).append(" hits a SOLO HOME RUN!!! ");

            } else if (numberOfRunners != 3) {
                atBatSummary.append(batter.getLastName()).append(" hits a ").append(Integer.toString(numberOfRunners + 1)).append(" run HOME RUN!!! ");
                for (String runner : runnersThatScored) {
                    atBatSummary.append(runner).append(" Scored ");
                }

            } else {
                atBatSummary.append(batter.getLastName()).append(" hits a GRAND SLAM!!! ");
                for (String runner : runnersThatScored) {
                    atBatSummary.append(runner).append(" Scored ");
                }

            }
            battingLineForCurrentBatter.incrementAtBats();
            batter.getBattingStatsForYear(year).incrementRuns();
            battingLineForCurrentBatter.incrementRuns();
            batter.getBattingStatsForYear(year).incrementHomeRuns();
            battingLineForCurrentBatter.incrementHomeRuns();
            batter.getBattingStatsForYear(year).incrementHits();
            battingLineForCurrentBatter.incrementHits();
            batter.getBattingStatsForYear(year).setRunsBattedIn(batter.getBattingStatsForYear(year).getRunsBattedIn() + (1 + numberOfRunners));
            battingLineForCurrentBatter.setRbis(battingLineForCurrentBatter.getRbis() + (1 + numberOfRunners));
            repository.updateBattingLine(battingLineForCurrentBatter);

            pitcher.getPitchingStatsForYear(year).setRuns(pitcher.getPitchingStatsForYear(year).getRuns() + 1);
            pitchingLineForCurrentPitcher.setRunsAllowed(pitchingLineForCurrentPitcher.getRunsAllowed() + 1);
            pitchersResponsible.add(pitcher);
            pitcher.getPitchingStatsForYear(year).incrementHomeRuns();
            pitchingLineForCurrentPitcher.incrementHomeRunsAllowed();
            pitcher.getPitchingStatsForYear(year).incrementHits();
            pitchingLineForCurrentPitcher.incrementHitsAllowed();
            if (areRunsEarned) {
                pitcher.getPitchingStatsForYear(year).setEarnedRuns(pitcher.getPitchingStatsForYear(year).getEarnedRuns() + 1);
                pitchingLineForCurrentPitcher.setEarnedRuns(pitchingLineForCurrentPitcher.getEarnedRuns() + 1);
            }
            repository.updatePitchingLine(pitchingLineForCurrentPitcher);
            animationData.put(0, new Pair<>(4, true));
            moveToNextBatterInLineup();
            atBatOver(pitcher);

        } else {
            if (checkForFoulBall(batter)) {
                // ball is foul
                atBatSummary.append("Foul Ball, ");
                if (strikes < 2) {
                    strikes++;
                }
            } else {
                // ball in play
                battingLineForCurrentBatter.incrementAtBats();
                repository.updateBattingLine(battingLineForCurrentBatter);
                int contactQuality = getQualityOfContact(batter, batterStaminaAdjustment);
                if (isHit(batter, typeOfHit, contactQuality)) {
                    hitOnPlay =true;
                    resolveHit(batter, pitcher, typeOfHit, contactQuality);
                } else {
                    resolveOut(batter, pitcher, typeOfHit, contactQuality);
                }
            }
        }
    }


    private void setStartOfSpan() {
        startOfSpan = atBatSummary.length();
    }

    private void resolveOut(Player batter, Player pitcher, int typeOfHit, int contactQuality) {
        int whereBallIsHit = getHitLocation(batter, typeOfHit, contactQuality, true);
        int gameStateIgnoringOuts = getGameState() - (outs * 8);
        boolean errorMade = false;
        int fielderWhoMadeError = 0;
        List<Integer> fieldersWhoTouchedBall = new ArrayList<>();
        if (outs != 2 && typeOfHit != FLYBALL && whereBallIsHit < 7 && gameStateIgnoringOuts != NO_OUTS_NOBODY_ON) {
            // chance for double play
            int oddsOfDoublePlay = 0;
            int chanceOfDoublePlay = random.nextInt(ONE_HUNDRED_PERCENT);
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at second, but no double play
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, but batter reaches first on an error! ");

                                    outs++;
                                    runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, runnerOnFirst.getPitcherResponsible(), runnerOnFirst.getPitchingLineForPitcherResponsible(), false);
                                    animationData.put(1, new Pair<>(1, false));
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append("all runners safe on Error! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(1, new Pair<>(1, true));
                                }
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // no error made, turn double play
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, and over to ")
                                        .append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(2))).append(", Double Play!!!");

                                outs = outs + 2;
                                runnerOnFirst = null;
                                animationData.put(1, new Pair<>(1, false));
                                animationData.put(0, new Pair<>(1, false));
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
                                        areAllRunsEarned = false;
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
                                        areAllRunsEarned = false;
                                    }
                                    // everyone is safe
                                    atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" was stealing, only play is to first base, SAFE at first on Error! ");

                                    defense.get(fielderWhoMadeError).getBattingStatsForYear(year).incrementErrors();
                                    runnerOnSecond = runnerOnFirst;
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, true));
                                } else {
                                    atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" was stealing, only play is to first base, OUT at first! ");

                                    outs++;
                                    runnerOnSecond = runnerOnFirst;
                                    runnerOnFirst = null;
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, false));
                                }
                            } else {
                                // out at second, but batter beats throw to first
                                for (Integer fielder : fieldersWhoTouchedBall) {
                                    errorMade = checkForError(fielder);
                                    if (errorMade) {
                                        errorOnPlay = true;
                                        if (outs == 2) {
                                            areAllRunsEarned = false;
                                        }
                                        fielderWhoMadeError = fielder;
                                        break;
                                    }
                                }
                                if (errorMade) {
                                    if (fielderWhoMadeError == SCOREKEEPING_FIRST_BASE) {
                                        atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                                .append(" Out!, throw to first gets away, batter goes to Second!!!");

                                        outs++;
                                        runnerOnSecond = new Runner(batter, battingLineForCurrentBatter, pitcher, pitchingLineForCurrentPitcher, false);
                                        animationData.put(1, new Pair<>(1, false));
                                        animationData.put(0, new Pair<>(2, true));
                                    } else {
                                        atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append("...Error on ")
                                                .append(getPositionNameFromScorekeeperPosition(fielderWhoMadeError)).append(" everyone is SAFE!!!");

                                        runnerOnSecond = runnerOnFirst;
                                        runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, pitcher, pitchingLineForCurrentPitcher, false);
                                        animationData.put(1, new Pair<>(1, true));
                                        animationData.put(0, new Pair<>(1, true));
                                    }
                                } else {
                                    // Fielder's Choice, no error, pitcher responsible for previous runner is now responsible for current runner since an out was made.
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, but batter beats the throw to first, SAFE! ");

                                    outs++;
                                    runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, runnerOnFirst.getPitcherResponsible(), runnerOnFirst.getPitchingLineForPitcherResponsible(),runnerOnFirst.isEarnedRun());
                                    animationData.put(1, new Pair<>(1, false));
                                    animationData.put(0, new Pair<>(1, true));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everyone safe
                                    atBatSummary.append("and it kicks off his glove, everyone is safe on the Error! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, true));
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    atBatSummary.append(batter.getLastName()).append(" is Out, but he throws it away trying to get ").append(runnerOnFirst.getRunner().getLastName()).append(" at first, ")
                                            .append(runnerOnFirst.getRunner().getLastName()).append(" advances to Second on the error!");

                                    outs++;
                                    runnerOnSecond = runnerOnFirst;
                                    runnerOnFirst = null;
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(0, false));
                                }
                            } else {
                                // no error, turn double play
                                atBatSummary.append(batter.getLastName()).append(" is Out, and he catches ").append(runnerOnFirst.getRunner().getLastName()).append(" off first base, Double Play!");

                                outs = outs + 2;
                                runnerOnFirst = null;
                                animationData.put(1, new Pair<>(0, false));
                                animationData.put(0, new Pair<>(0, true));
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
                            oddsOfDoublePlay = 150;
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
                                // force at first, runner caught advancing to third
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_THIRD_BASE);
                            }
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == SCOREKEEPING_THIRD_BASE && fieldersWhoTouchedBall.size() == 3) {
                                    // Still got out at first, but no double play
                                    atBatSummary.append(batter.getLastName()).append(" is Out at first, but ball is thrown away trying to get ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" at third, ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" scores on the error!");

                                    outs++;
                                    runnerOnThird = runnerOnSecond;
                                    runnerOnSecond = null;
                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, false, false);
                                    animationData.put(2, new Pair<>(2, true));
                                    animationData.put(0, new Pair<>(1, false));
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(", Error everyone is Safe! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, true));
                                }
                            } else {
                                // no error made, turn double play
                                if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP) {
                                    atBatSummary.append("tags the runner ").append(runnerOnSecond.getRunner().getLastName()).append(" Out!, and over to ")
                                            .append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(", Double Play!!!");
                                } else {
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, and over to ")
                                            .append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(2))).append(", Double Play!!!");
                                }


                                outs = outs + 2;
                                runnerOnSecond = null;
                                animationData.put(2, new Pair<>(1, false));
                                animationData.put(0, new Pair<>(1, false));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                // everyone safe
                                atBatSummary.append("everyone safe on the Error ");

                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // Get a single out
                                if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_SHORTSTOP || whereBallIsHit == SCOREKEEPING_PITCHER) {
                                    // runner stays, out at first
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(", Out! ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" stays at second.  ");

                                    outs++;
                                    animationData.put(2, new Pair<>(0, true));
                                } else {
                                    // runner advances, out at first
                                    atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(", Out! ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" advances to third.  ");

                                    outs++;
                                    runnerOnThird = runnerOnSecond;
                                    runnerOnSecond = null;
                                    animationData.put(2, new Pair<>(1, true));
                                }
                                animationData.put(0, new Pair<>(1, false));
                            }

                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        setStartOfSpan();
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everyone safe
                                    atBatSummary.append("dropped, everyone safe on the Error ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, true));
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    atBatSummary.append("Out!, throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(", and it gets away, ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" goes to third on the Error.  ");

                                    outs++;
                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, false, false);
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(0, false));
                                }
                            } else {
                                // no error, turn double play
                                if (whereBallIsHit != SCOREKEEPING_SECOND_BASE) {
                                    atBatSummary.append("Out!, throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(", and he catches ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" off the base, Double Play!");
                                } else {
                                    atBatSummary.append("Out!, and he catches ")
                                            .append(runnerOnSecond.getRunner().getLastName()).append(" off the base and tags the bag, Double Play!");
                                }

                                outs = outs + 2;
                                runnerOnSecond = null;
                                animationData.put(2, new Pair<>(0, false));
                                animationData.put(0, new Pair<>(0, false));
                            }
                        } else {
                            // no double play
                            errorMade = checkForError(whereBallIsHit);
                            if (errorMade) {
                                errorOnPlay = true;
                                if (outs == 2) {
                                    areAllRunsEarned = false;
                                }
                                atBatSummary.append(", and it kicks off his glove into the outfield, everyone is safe, ")
                                        .append(runnerOnSecond.getRunner().getLastName()).append(" goes to third on the Error.  ");

                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // batter is out on caught line drive, runner holds
                                atBatSummary.append(", Out! ")
                                        .append(runnerOnSecond.getRunner().getLastName()).append(" stays at Second.  ");

                                outs++;
                                animationData.put(2, new Pair<>(0, true));
                                animationData.put(0, new Pair<>(0, false));
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
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_CATCHER);
                            }
                            fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areAllRunsEarned = false;
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

                                    outs++;
                                    runnerOnThird = null;
                                    runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, pitcher, pitchingLineForCurrentPitcher, false);
                                    animationData.put(3, new Pair<>(1, false));
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(", and it kicks off his glove, everyone is safe, ")
                                            .append(batter.getLastName()).append(" safe at first.  ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                }
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // no error made, turn double play
                                atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Out at home!, ")
                                        .append(batter.getLastName()).append(" Out at first.  Double Play!");

                                outs = outs + 2;
                                runnerOnThird = null;
                                animationData.put(3, new Pair<>(1, false));
                                animationData.put(0, new Pair<>(1, false));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                // everyone safe
                                atBatSummary.append(", and it kicks off his glove, everyone is Safe! ");

                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                animationData.put(3, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // Get a single out at first
                                if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_FIRST_BASE || whereBallIsHit == SCOREKEEPING_PITCHER || whereBallIsHit == SCOREKEEPING_CATCHER) {
                                    // runner stays, out at first
                                    if (whereBallIsHit != SCOREKEEPING_CATCHER) {
                                        atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" ")
                                                .append(batter.getLastName()).append(" out at first.  ").append(runnerOnThird.getRunner().getLastName()).append(" stays at third.  ");
                                    } else {
                                        atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(" ")
                                                .append(batter.getLastName()).append(" out at first.  ").append(runnerOnThird.getRunner().getLastName()).append(" stays at third.  ");
                                    }

                                    outs++;
                                    animationData.put(3, new Pair<>(0, true));
                                    animationData.put(0, new Pair<>(1, false));
                                } else {
                                    // runner advances if less than 3 outs, batter out at first
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" ")
                                            .append(batter.getLastName()).append(" out at first.  ");

                                    outs++;
                                    if (outs < 3) {
                                        advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
                                        runnerOnFirst = null;
                                        animationData.put(3, new Pair<>(1, true));
                                        animationData.put(0, new Pair<>(1, false));
                                    }
                                }
                            }

                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        setStartOfSpan();
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everyone safe
                                    atBatSummary.append(", dropped everyone is Safe! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, true));
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    outs++;
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" ")
                                            .append(batter.getLastName()).append(" Out! ").append(runnerOnThird.getRunner().getLastName()).append(" advances on Error.  ");

                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, false));
                                }
                            } else {
                                // no error, turn double play
                                if (whereBallIsHit == SCOREKEEPING_THIRD_BASE) {
                                    atBatSummary.append(" and he catches ").append(runnerOnThird.getRunner().getLastName()).append(" off the base and steps on the bag, Out! Double Play!");
                                } else {
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" and he catches ").append(runnerOnThird.getRunner().getLastName()).append(" off the base, Out! Double Play!");
                                }

                                outs = outs + 2;
                                runnerOnSecond = null;
                                animationData.put(3, new Pair<>(0, false));
                                animationData.put(0, new Pair<>(1, false));
                            }
                        } else {
                            // no double play
                            errorMade = checkForError(whereBallIsHit);
                            if (errorMade) {
                                errorOnPlay = true;
                                if (outs == 2) {
                                    areAllRunsEarned = false;
                                }
                                // everyone safe and advances
                                atBatSummary.append(", off his glove into the outfield, everyone is Safe! ").append(runnerOnThird.getRunner().getLastName()).append(" advances on error.  ");

                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                animationData.put(3, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // batter is out on caught line drive, runner holds
                                atBatSummary.append(", Out! ").append(runnerOnThird.getRunner().getLastName()).append(" stays at third.  ");

                                outs++;
                                animationData.put(3, new Pair<>(0, true));
                                animationData.put(0, new Pair<>(1, false));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at second, but no double play, fielder's choice
                                    outs++;
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" ")
                                            .append(batter.getLastName()).append(" out at second.  ").append(runnerOnSecond.getRunner().getLastName()).append(" advances to third.  ");

                                    runnerOnThird = runnerOnSecond;
                                    runnerOnSecond = null;
                                    runnerOnFirst = new Runner(batter, battingLineForCurrentBatter,runnerOnFirst.getPitcherResponsible(), runnerOnFirst.getPitchingLineForPitcherResponsible(),runnerOnFirst.isEarnedRun());
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, false));
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" everyone is Safe on Error! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, true));
                                }
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // no error made, turn double play
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" Out!, and over to ")
                                        .append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(2))).append(", Double Play!!! ");

                                outs = outs + 2;
                                if (outs < 3) {
                                    atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" advances to third.  ");
                                }

                                runnerOnThird = runnerOnSecond;
                                runnerOnSecond = null;
                                runnerOnFirst = null;
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(1, new Pair<>(1, false));
                                animationData.put(0, new Pair<>(1, false));
                            }
                        } else {
                            // No Double play, Get a single out
                            if (runnerSteals) {
                                // runner goes to second, out at first
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(" Out! Runners were stealing and advance to second and third ");

                                outs++;
                                runnerOnThird = runnerOnSecond;
                                runnerOnSecond = runnerOnFirst;
                                runnerOnFirst = null;
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(1, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, false));
                            } else {
                                // batter beats throw to first
                                atBatSummary.append("throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(" Out!, and over to ")
                                        .append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(", Safe at first! ");

                                outs++;
                                runnerOnFirst = null;
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(1, new Pair<>(1, false));
                                animationData.put(0, new Pair<>(1, true));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everyone safe
                                    atBatSummary.append("drops it!  Everyone Safe! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    atBatSummary.append("Out!, throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" and it gets away, the runners advance to second and third.  ");

                                    outs++;
                                    runnerOnThird = runnerOnSecond;
                                    runnerOnSecond = runnerOnFirst;
                                    runnerOnFirst = null;
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(0, false));
                                }
                            } else {
                                // no error, turn double play
                                atBatSummary.append("Out!, and he catches the runner off the base, Out! Double Play! ");

                                outs = outs + 2;
                                runnerOnFirst = null;
                                animationData.put(2, new Pair<>(0, true));
                                animationData.put(1, new Pair<>(0, false));
                                animationData.put(0, new Pair<>(0, false));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at second, but no double play, runner on third scores
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" Out! ").append(batter.getLastName()).append(" safe at first on Error!  ");

                                    outs++;
                                    runnerOnFirst = null;
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, false));
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(" everyone safe on the Error! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, true));
                                }
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // no error made, turn double play
                                atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" ")
                                        .append(batter.getLastName()).append(" out at second.  ");

                                outs = outs + 2;
                                runnerOnFirst = null;
                                if (outs < 3) {
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, false);  // score runner from third if less than 3 outs, but no rbi on double play
                                    animationData.put(3, new Pair<>(1, true));
                                }
                                animationData.put(1, new Pair<>(1, false));
                                animationData.put(0, new Pair<>(1, false));
                            }
                        } else {
                            // Get a single out
                            if (runnerSteals) {
                                // runner goes to second, out at first
                                atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_FIRST_BASE)).append(" ")
                                        .append(batter.getLastName()).append(" out at first.  ").append(runnerOnFirst.getRunner().getLastName()).append(" advances to second.  ");

                                outs++;
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
                                runnerOnFirst = null;
                                animationData.put(3, new Pair<>(1, true));
                                animationData.put(1, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, false));
                            } else {
                                // batter beats throw to first
                                atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" out at second.  ").append(batter.getLastName()).append(" beats the throw to first, Safe!  ");

                                outs++;
                                runnerOnFirst = null;
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
                                animationData.put(3, new Pair<>(1, true));
                                animationData.put(1, new Pair<>(1, false));
                                animationData.put(0, new Pair<>(1, true));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everyone safe
                                    atBatSummary.append(" everyone safe on the Error! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, true));
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    atBatSummary.append(", Out! but he threw it away trying for the double play!  Runners advance on Error!  ");

                                    outs++;
                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, areAllRunsEarned, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(0, false));
                                }
                            } else {
                                // no error, turn double play
                                if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                                    atBatSummary.append(", Out!  He throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" and catches the runner off the bag!  Double Play!  ");
                                } else {
                                    atBatSummary.append(", Out!  He catches the runner off the bag!  Double Play!  ");
                                }

                                outs = outs + 2;
                                runnerOnFirst = null;
                                animationData.put(3, new Pair<>(0, true));
                                animationData.put(1, new Pair<>(0, false));
                                animationData.put(0, new Pair<>(0, false));
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
                            } else {
                                fieldersWhoTouchedBall.add(whereBallIsHit);
                                fieldersWhoTouchedBall.add(SCOREKEEPING_CATCHER);
                            }
                            fieldersWhoTouchedBall.add(SCOREKEEPING_FIRST_BASE);
                            for (Integer fielder : fieldersWhoTouchedBall) {
                                errorMade = checkForError(fielder);
                                if (errorMade) {
                                    errorOnPlay = true;
                                    if (outs == 2) {
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at home, but no double play
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_CATCHER)).append(" ")
                                            .append(runnerOnThird.getRunner().getLastName()).append(" out at home!  ").append(runnerOnSecond.getRunner().getLastName()).append(" advances to third.  ");

                                    outs++;
                                    runnerOnThird = null;
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
                                    animationData.put(3, new Pair<>(1, false));
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(" everyone is safe on the Error!  ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                }
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // no error made, turn double play
                                atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(SCOREKEEPING_CATCHER)).append(" ")
                                        .append(runnerOnThird.getRunner().getLastName()).append(" Out at home!  Throws to first, in time.  Double Play!  ");

                                outs = outs + 2;
                                runnerOnThird = runnerOnSecond;
                                runnerOnSecond = null;
                                animationData.put(3, new Pair<>(1, false));
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, false));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                // everyone safe
                                atBatSummary.append(" everyone is safe on the Error!  ");

                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                animationData.put(3, new Pair<>(1, true));
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // Get a single out
                                if (whereBallIsHit == SCOREKEEPING_THIRD_BASE || whereBallIsHit == SCOREKEEPING_FIRST_BASE || whereBallIsHit == SCOREKEEPING_PITCHER || whereBallIsHit == SCOREKEEPING_CATCHER) {
                                    // runner stays, out at first
                                    atBatSummary.append(batter.getLastName()).append(" Out at first!  Runners hold.  ");

                                    outs++;
                                    animationData.put(3, new Pair<>(0, true));
                                    animationData.put(2, new Pair<>(0, true));
                                } else {
                                    // runner advances, out at first
                                    atBatSummary.append(batter.getLastName()).append(" Out at first!  ");

                                    outs++;
                                    if (outs < 3) {
                                        advanceAllRunnersOneBaseOnBattedBall(null, pitcher, areAllRunsEarned, true);
                                        animationData.put(3, new Pair<>(1, true));
                                        animationData.put(2, new Pair<>(1, true));
                                    }
                                }
                                animationData.put(0, new Pair<>(1, false));
                            }

                        }

                    }
                    if (typeOfHit == LINE_DRIVE) {
                        setStartOfSpan();
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everyone safe
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, true));
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    outs++;
                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(0, false));
                                }
                            } else {
                                // no error, turn double play
                                outs = outs + 2;
                                runnerOnSecond = null;
                                animationData.put(3, new Pair<>(0, true));
                                animationData.put(2, new Pair<>(0, false));
                                animationData.put(0, new Pair<>(0, false));
                            }
                        } else {
                            // no double play
                            errorMade = checkForError(whereBallIsHit);
                            if (errorMade) {
                                errorOnPlay = true;
                                if (outs == 2) {
                                    areAllRunsEarned = false;
                                }
                                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                animationData.put(3, new Pair<>(1, true));
                                animationData.put(2, new Pair<>(1, true));
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // batter is out on caught line drive, runner holds
                                outs++;
                                animationData.put(3, new Pair<>(0, true));
                                animationData.put(2, new Pair<>(0, true));
                                animationData.put(0, new Pair<>(0, false));
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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == FIRST_BASE) {
                                    // Still got out at second, but no double play, runner on third scores
                                    atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" Out! ").append(batter.getLastName()).append(" safe at first on Error!  ");

                                    outs++;
                                    runnerOnFirst = null;
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, false));
                                } else {
                                    // Error was made trying to get lead runner, all runners safe
                                    atBatSummary.append(" everyone safe on the Error! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, true));
                                }
                                animationData.put(0, new Pair<>(1, true));
                            } else {
                                // no error made, turn double play
                                atBatSummary.append(" throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" ")
                                        .append(batter.getLastName()).append(" out at second.  ").append(runnerOnSecond.getRunner().getLastName()).append(" advances to third.  ");

                                outs = outs + 2;
                                runnerOnFirst = null;
                                if (outs < 3) {
                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, false);  // score runner from third if less than 3 outs, but no rbi on double play
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(2, new Pair<>(1, true));
                                }
                                animationData.put(1, new Pair<>(1, false));
                                animationData.put(0, new Pair<>(1, false));
                            }
                        } else {
                            // Get a single out

                            // batter beats throw to first
                            atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" out at second.  ").append(batter.getLastName()).append(" beats the throw to first, Safe!  ");

                            outs++;
                            runnerOnFirst = null;
                            advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
                            animationData.put(3, new Pair<>(1, true));
                            animationData.put(2, new Pair<>(1, true));
                            animationData.put(1, new Pair<>(1, false));
                            animationData.put(0, new Pair<>(1, true));

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
                                        areAllRunsEarned = false;
                                    }
                                    fielderWhoMadeError = fielder;
                                    break;
                                }
                            }
                            if (errorMade) {
                                if (fielderWhoMadeError == whereBallIsHit) {
                                    // muffed line drive, everyone safe
                                    atBatSummary.append(" everyone safe on the Error! ");

                                    advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(1, true));
                                } else {
                                    // catch made batter is out, error made trying to double off runner, runner advances
                                    atBatSummary.append(", Out! but he threw it away trying for the double play!  Runners advance on Error!  ");

                                    outs++;
                                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, areAllRunsEarned, false);
                                    animationData.put(3, new Pair<>(1, true));
                                    animationData.put(2, new Pair<>(1, true));
                                    animationData.put(1, new Pair<>(1, true));
                                    animationData.put(0, new Pair<>(0, false));
                                }
                            } else {
                                // no error, turn double play
                                if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                                    atBatSummary.append(", Out!  He throws to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1)))
                                            .append(" and catches the runner off the bag!  Double Play!  ");
                                } else {
                                    atBatSummary.append(", Out!  He catches the runner off first and steps on the bag!  Double Play!  ");
                                }

                                outs = outs + 2;
                                runnerOnFirst = null;
                                animationData.put(3, new Pair<>(0, true));
                                animationData.put(2, new Pair<>(0, true));
                                animationData.put(1, new Pair<>(0, false));
                                animationData.put(0, new Pair<>(0, false));
                            }
                        }
                    }
                    break;
                default:
            }

        } else if (typeOfHit == GROUNDBALL && whereBallIsHit < 7) {
            setStartOfSpan();
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
                        areAllRunsEarned = false;
                    }
                    fielderWhoMadeError = fielder;
                    break;
                }
            }
            if (errorMade) {
                // all runners are safe, advance runners
                if (fielderWhoMadeError == whereBallIsHit) {
                    atBatSummary.append(", Error!!!  Everyone is safe!");

                } else {
                    atBatSummary.append(" throw to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(" and it gets by him!  Error! ");

                }
                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                animationData.put(0, new Pair<>(1, true));
            } else {
                // record out, (advance runners if < 3 outs, this shouldn't happen)
                if (whereBallIsHit != SCOREKEEPING_FIRST_BASE) {
                    atBatSummary.append(" throw to ").append(getPositionNameFromScorekeeperPosition(fieldersWhoTouchedBall.get(1))).append(", OUT! ");

                } else {
                    atBatSummary.append(" he tags the bag, OUT! ");

                }

                outs++;
                animationData.put(0, new Pair<>(1, false));
                if (outs < 3) {
                    advanceAllRunnersOneBaseOnBattedBall(null, pitcher, areAllRunsEarned, true);
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
                    areAllRunsEarned = false;
                }
                // error made on catch, everyone is safe

                if (typeOfHit == FLYBALL) {
                    atBatSummary.append("Flyball to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" and he cant't hold on, Error!!! ");

                } else if (typeOfHit == LINE_DRIVE) {
                    atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" and it kicks off his glove, Error!!! ");

                }
                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, false, false);
                animationData.put(0, new Pair<>(1, true));
            } else {
                if (typeOfHit == FLYBALL) {
                    atBatSummary.append("Flyball to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(", OUT! ");

                } else if (typeOfHit == LINE_DRIVE) {
                    atBatSummary.append("Line Drive to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(", OUT! ");

                } else {
                    atBatSummary.append("Ground Ball to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(", OUT!, THIS SHOULDN'T HAPPEN !!!!!!!!!  ");

                }
                outs++;
                if (outs < 3 && !basesAreEmpty() && typeOfHit == FLYBALL && contactQuality != SOFT) {
                    advanceIfRunnersTagUp(whereBallIsHit, batter, pitcher);
                }
                animationData.put(0, new Pair<>(0, false));
            }
        }
        if (outsAtStartOfAtBat != outs) {
            battingLineForCurrentBatter.setLeftOnBase(battingLineForCurrentBatter.getLeftOnBase() + countRunners());
            repository.updateBattingLine(battingLineForCurrentBatter);
        }
        moveToNextBatterInLineup();
        atBatOver(pitcher);
    }

    private int countRunners() {
        int count = 0;
        if (runnerOnThird != null) {
            count++;
        }
        if (runnerOnSecond != null) {
            count++;
        }
        if (runnerOnFirst != null) {
            count++;
        }
        return count;
    }

    private void advanceIfRunnersTagUp(int whereBallWasHit, Player batter, Player pitcher) {
        int chanceOfTaggingUp = random.nextInt(ONE_HUNDRED_PERCENT);
        boolean runnerThrownOutTaggingUp = random.nextInt(ONE_HUNDRED_PERCENT) < 1000;
        setStartOfSpan();
        if (runnerOnThird != null) {
            if (chanceOfTaggingUp < runnerOnThird.getRunner().getHittingPercentages().getBaseRunning() + 5000) {
                if (runnerThrownOutTaggingUp) {
                    // runner thrown out trying to tag up
                    atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" thrown out trying to tag up! OUT!!! ");

                    animationData.put(3, new Pair<>(1, false));
                    outs++;
                } else {
                    // runner tags up successfully
                    atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" tags up...SAFE!!! ").append(runnerOnThird.getRunner().getLastName()).append(" Scores! ");

                    animationData.put(3, new Pair<>(1, true));
                    runsScored++;
                    batter.getBattingStatsForYear(year).setRunsBattedIn(batter.getBattingStatsForYear(year).getRunsBattedIn() + 1);
                    battingLineForCurrentBatter.setRbis(battingLineForCurrentBatter.getRbis() + 1);
                    repository.updateBattingLine(battingLineForCurrentBatter);
                    runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                    runnerOnThird.getBattingLineForRunner().incrementRuns();
                    repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                    pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                    runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                    runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                    if (runnerOnThird.isEarnedRun()) {
                        runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getEarnedRuns() + 1);
                        runnerOnThird.getPitchingLineForPitcherResponsible().setEarnedRuns(runnerOnThird.getPitchingLineForPitcherResponsible().getEarnedRuns() + 1);
                    }
                    repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                }
                runnerOnThird = null;
            }
        } else if (runnerOnSecond != null) {
            // no runner on third
            if (whereBallWasHit < 8) {
                if (chanceOfTaggingUp < runnerOnSecond.getRunner().getHittingPercentages().getBaseRunning() - 4000) {
                    if (runnerThrownOutTaggingUp) {
                        // runner thrown out trying to tag up
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" thrown out trying to tag up! OUT!!! ");

                        animationData.put(2, new Pair<>(1, false));
                        outs++;
                    } else {
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" tags up...SAFE at third!!! ");

                        animationData.put(2, new Pair<>(1, true));
                        // runner tags up successfully
                        runnerOnThird = runnerOnSecond;
                    }
                    runnerOnSecond = null;
                }
            }
            if (whereBallWasHit == 8) {
                if (chanceOfTaggingUp < runnerOnSecond.getRunner().getHittingPercentages().getBaseRunning() - 3000) {
                    if (runnerThrownOutTaggingUp) {
                        // runner thrown out trying to tag up
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" thrown out trying to tag up! OUT!!! ");

                        animationData.put(2, new Pair<>(1, false));
                        outs++;
                    } else {
                        // runner tags up successfully
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" tags up...SAFE!!! ");

                        animationData.put(2, new Pair<>(1, true));
                        runnerOnThird = runnerOnSecond;
                    }
                    runnerOnSecond = null;
                }
            } else {
                // Ball hit to right field
                if (chanceOfTaggingUp < runnerOnSecond.getRunner().getHittingPercentages().getBaseRunning() - 2000) {
                    if (runnerThrownOutTaggingUp) {
                        // runner thrown out trying to tag up
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" thrown out trying to tag up! OUT!!! ");

                        animationData.put(2, new Pair<>(1, false));
                        outs++;
                    } else {
                        // runner tags up successfully
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" tags up...SAFE!!! ");

                        animationData.put(2, new Pair<>(1, true));
                        runnerOnThird = runnerOnSecond;
                    }
                    runnerOnSecond = null;
                }
            }

        } else if (runnerOnFirst != null) {
            // no runner on second or third
            if (chanceOfTaggingUp < runnerOnFirst.getRunner().getHittingPercentages().getBaseRunning() - 4000) {
                if (runnerThrownOutTaggingUp) {
                    // runner thrown out trying to tag up
                    atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" thrown out trying to tag up! OUT!!! ");

                    animationData.put(1, new Pair<>(1, false));
                    outs++;
                    runnerOnSecond = null;
                } else {
                    // runner tags up successfully
                    atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" tags up...SAFE!!! ");

                    animationData.put(1, new Pair<>(1, true));
                    runnerOnSecond = runnerOnFirst;
                    runnerOnFirst = null;
                }
            }
        }
    }

    private void advanceAllRunnersOneBaseOnBattedBall(Player batter, Player pitcher, boolean earnedRun, boolean rbi) {
        boolean batterAlreadyOnBase = false;
        setStartOfSpan();
        if (runnerOnThird != null) {
            atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Scored ");

            runsScored++;
            runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
            runnerOnThird.getBattingLineForRunner().incrementRuns();
            repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
            runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
            runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
            if (earnedRun && runnerOnThird.isEarnedRun()) {
                runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getEarnedRuns() + 1);
                runnerOnThird.getPitchingLineForPitcherResponsible().setEarnedRuns(runnerOnThird.getPitchingLineForPitcherResponsible().getEarnedRuns() + 1);
            }
            repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
            if (batter != null && rbi) {
                batter.getBattingStatsForYear(year).setRunsBattedIn(batter.getBattingStatsForYear(year).getRunsBattedIn() + 1);
                battingLineForCurrentBatter.setRbis(battingLineForCurrentBatter.getRbis() + 1);
                repository.updateBattingLine(battingLineForCurrentBatter);
            }
        }
        runnerOnThird = runnerOnSecond;
        runnerOnSecond = runnerOnFirst;
        if (runnerOnThird != null || runnerOnSecond != null) {
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
            runnerOnFirst = new Runner(batter, battingLineForCurrentBatter, pitcher, pitchingLineForCurrentPitcher, earnedRun);
        }

    }

    private void resolveHit(Player batter, Player pitcher, int typeOfHit, int contactQuality) {
        int whereBallIsHit;
        boolean runnersOnBase = !basesAreEmpty();
        boolean hitIsTriple = random.nextInt(ONE_HUNDRED_PERCENT) < batter.getHittingPercentages().getTriplePct();
        boolean hitIsDouble;
        setStartOfSpan();
        if (hitIsTriple) {
            hitIsDouble = false;
        } else {
            hitIsDouble = random.nextInt(ONE_HUNDRED_PERCENT) < batter.getHittingPercentages().getDoublePct();
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

            if (runnerOnThird != null) {
                animationData.put(3, new Pair<>(1, true));
            }
            if (runnerOnSecond != null) {
                animationData.put(2, new Pair<>(2, true));
            }
            if (runnerOnFirst != null) {
                animationData.put(1, new Pair<>(3, true));
            }
            animationData.put(0, new Pair<>(3, true));
            for (int i = 0; i < 3; i++) {
                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
            }
            batter.getBattingStatsForYear(year).incrementTriples();
            batter.getBattingStatsForYear(year).incrementHits();
            battingLineForCurrentBatter.incrementHits();
            repository.updateBattingLine(battingLineForCurrentBatter);
            pitcher.getPitchingStatsForYear(year).incrementHits();
            pitchingLineForCurrentPitcher.incrementHitsAllowed();
        } else if (hitIsDouble) {
            // Advance all runners 2 bases, adjust stats
            atBatSummary.append("Double to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append("! ");

            if (runnerOnThird != null) {
                animationData.put(3, new Pair<>(1, true));
            }
            if (runnerOnSecond != null) {
                animationData.put(2, new Pair<>(2, true));
            }
            if (runnerOnFirst != null) {
                animationData.put(1, new Pair<>(2, true));
            }
            animationData.put(0, new Pair<>(2, true));
            for (int i = 0; i < 2; i++) {
                advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
            }
            batter.getBattingStatsForYear(year).incrementDoubles();
            batter.getBattingStatsForYear(year).incrementHits();
            battingLineForCurrentBatter.incrementHits();
            repository.updateBattingLine(battingLineForCurrentBatter);
            pitcher.getPitchingStatsForYear(year).incrementHits();
            pitchingLineForCurrentPitcher.incrementHitsAllowed();
            if (runnersOnBase) {
                checkIfRunnerTakesExtraBase(whereBallIsHit, batter, pitcher);
            }
        } else {
            // Single
            atBatSummary.append("Single to ").append(getPositionNameFromScorekeeperPosition(whereBallIsHit)).append(" ");

            if (runnerOnThird != null) {
                animationData.put(3, new Pair<>(1, true));
            }
            if (runnerOnSecond != null) {
                animationData.put(2, new Pair<>(1, true));
            }
            if (runnerOnFirst != null) {
                animationData.put(1, new Pair<>(1, true));
            }
            animationData.put(0, new Pair<>(1, true));
            advanceAllRunnersOneBaseOnBattedBall(batter, pitcher, areAllRunsEarned, true);
            batter.getBattingStatsForYear(year).incrementHits();
            battingLineForCurrentBatter.incrementHits();
            repository.updateBattingLine(battingLineForCurrentBatter);
            batter.getBattingStatsForYear(year).incrementSingles();
            pitcher.getPitchingStatsForYear(year).incrementHits();
            pitchingLineForCurrentPitcher.incrementHitsAllowed();
            if (runnersOnBase) {
                checkIfRunnerTakesExtraBase(whereBallIsHit, batter, pitcher);
            }
        }
        repository.updatePitchingLine(pitchingLineForCurrentPitcher);
        moveToNextBatterInLineup();
        atBatOver(pitcher);
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
        int chanceOfAdvancing = random.nextInt(ONE_HUNDRED_PERCENT);
        boolean runnerThrownOutAdvancing = random.nextInt(ONE_HUNDRED_PERCENT) < TEN_PERCENT;

        if (runnerOnThird != null) {
            int baserunnningAdjustment = FIFTY_PERCENT;
            if (whereBallIsHit < 7) {
                baserunnningAdjustment -= ONE_HUNDRED_PERCENT;
            }
            if (wasRunnerRunningWhenBallHit) {
                baserunnningAdjustment += FIFTY_PERCENT;
            }
            if (chanceOfAdvancing < runnerOnThird.getRunner().getHittingPercentages().getBaseRunning() + baserunnningAdjustment) {
                if (runnerThrownOutAdvancing) {
                    // runner thrown out trying to advance
                    atBatSummary.append("\n").append(runnerOnThird.getRunner().getLastName()).append(" thrown out trying to advance! OUT!!! ");

                    outs++;
                    runnerOnThird = null;
                    if (animationData.get(2) != null) {
                        if (animationData.get(2).first == 1) {
                            animationData.put(2, new Pair<>(2, false));
                        }
                    }
                    if (animationData.get(1) != null) {
                        if (animationData.get(1).first == 2) {
                            animationData.put(1 , new Pair<>(3, false));
                        }
                    }
                } else {
                    // runner advances successfully
                    atBatSummary.append(" and ").append(runnerOnThird.getRunner().getLastName()).append(" Scored!!! ");

                    if (animationData.get(2) != null) {
                        if (animationData.get(2).first == 1) {
                            animationData.put(2, new Pair<>(2, true));
                        }
                    }
                    if (animationData.get(1) != null) {
                        if (animationData.get(1).first == 2) {
                            animationData.put(1 , new Pair<>(3, true));
                        }
                    }
                    runsScored++;
                    batter.getBattingStatsForYear(year).setRunsBattedIn(batter.getBattingStatsForYear(year).getRunsBattedIn() + 1);
                    battingLineForCurrentBatter.setRbis(battingLineForCurrentBatter.getRbis() + 1);
                    repository.updateBattingLine(battingLineForCurrentBatter);
                    runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                    runnerOnThird.getBattingLineForRunner().incrementRuns();
                    repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                    pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                    runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                    runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                    if (runnerOnThird.isEarnedRun()) {
                        runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setEarnedRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getEarnedRuns() + 1);
                        runnerOnThird.getPitchingLineForPitcherResponsible().setEarnedRuns(runnerOnThird.getPitchingLineForPitcherResponsible().getEarnedRuns() + 1);
                    }
                    repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                    runnerOnThird = null;
                }
            }
        }
        boolean runnerOnSecondThrownOutAdvancing = random.nextInt(ONE_HUNDRED_PERCENT) < TEN_PERCENT;
        if (runnerOnSecond != null && runnerOnThird == null) {
            // no runner on third
            int baserunnningAdjustment = FORTY_PERCENT;
            if (whereBallIsHit < 7) {
                baserunnningAdjustment += ONE_HUNDRED_PERCENT;
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
                    atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" thrown out trying advance! OUT!!! ");

                    if (animationData.get(1) != null) {
                        if (animationData.get(1).first == 1) {
                            animationData.put(1 , new Pair<>(2, false));
                        }
                    }
                    outs++;
                } else {
                    atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" takes an additional base...SAFE!!! ");

                    if (animationData.get(1) != null) {
                        if (animationData.get(1).first == 1) {
                            animationData.put(1 , new Pair<>(2, false));
                        }
                    }
                    // runner advances successfully
                    runnerOnThird = runnerOnSecond;
                }
                runnerOnSecond = null;
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
            boolean isInInfield = random.nextInt(ONE_HUNDRED_PERCENT) < batter.getHittingPercentages().getInfieldFlyBallPct();

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
        int checkForFieldArea = random.nextInt(ONE_HUNDRED_PERCENT);
        if (checkForFieldArea < batter.getHittingPercentages().getPullPct()) {
            // Batter pulled the ball
            if (batter.getHits().equals(resourceProvider.getString(R.string.left_handed))) {
                fieldArea = RIGHT;
            } else {
                fieldArea = LEFT;
            }
        } else if (checkForFieldArea < (batter.getHittingPercentages().getPullPct() + batter.getHittingPercentages().getCenterPct())) {
            fieldArea = CENTER;
        } else {
            if (batter.getHits().equals(resourceProvider.getString(R.string.left_handed))) {
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
        int checkForHit = random.nextInt(ONE_HUNDRED_PERCENT);
        int defensiveShiftAdjustment = (BATTING_PULL_PCT_MEAN - batter.getHittingPercentages().getPullPct()) / 45 + (batter.getHittingPercentages().getCenterPct() - BATTING_CENTER_PCT_MEAN) / 45 +
                ((ONE_HUNDRED_PERCENT - batter.getHittingPercentages().getPullPct() - batter.getHittingPercentages().getCenterPct()) - (ONE_HUNDRED_PERCENT - BATTING_CENTER_PCT_MEAN - BATTING_PULL_PCT_MEAN)) / 45;
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
        int checkForHitQuality = random.nextInt(ONE_HUNDRED_PERCENT);
        if (checkForHitQuality < batter.getHittingPercentages().getHardPct() - batterAdjustment) {
            return HARD;
        } else if (checkForHitQuality < (batter.getHittingPercentages().getHardPct() - batterAdjustment + batter.getHittingPercentages().getMedPct() - batterAdjustment)) {
            return MEDIUM;
        } else {
            return SOFT;
        }
    }

    private boolean checkForFoulBall(Player batter) {
        return random.nextInt(ONE_HUNDRED_PERCENT) < batter.getHittingPercentages().getFoulBallPct();
    }

    private int getTypeOfHit(Player batter, Player pitcher, int batterAdjustment) {
        int checkForHitType = random.nextInt(ONE_HUNDRED_PERCENT);
        if (checkForHitType < batter.getHittingPercentages().getGroundBallPct() + batterAdjustment) {
            batter.getBattingStatsForYear(year).incrementGroundBalls();
            pitcher.getPitchingStatsForYear(year).incrementGroundBalls();
            return GROUNDBALL;
        } else if (checkForHitType < (batter.getHittingPercentages().getGroundBallPct() + batter.getHittingPercentages().getLineDrivePct())) {
            batter.getBattingStatsForYear(year).incrementLineDrives();
            pitcher.getPitchingStatsForYear(year).incrementLineDrives();
            return LINE_DRIVE;
        } else {
            batter.getBattingStatsForYear(year).incrementFlyBalls();
            pitcher.getPitchingStatsForYear(year).incrementFlyBalls();
            return FLYBALL;
        }
    }

    private void checkForWildPitch(Player pitcher) {
        if (basesAreEmpty()) {
            return;
        }
        if (random.nextInt(ONE_HUNDRED_PERCENT) < pitcher.getPitchingPercentages().getWildPitchPct()) {
            atBatSummary.append(" Wild Pitch! ");
            pitcher.getPitchingStatsForYear(year).incrementWildPitches();
            if (runnerOnThird != null) {
                runsScored++;
                setStartOfSpan();
                atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Scores from Third!!! ");

                runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                runnerOnThird.getBattingLineForRunner().incrementRuns();
                repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                runnerOnThird = null;
            }
            runnerOnThird = runnerOnSecond;
            runnerOnSecond = runnerOnFirst;
            runnerOnFirst = null;
        }
    }

    private boolean isHitByPitch(Player batter, Player pitcher) {
        if (random.nextInt(ONE_HUNDRED_PERCENT) < pitcher.getPitchingPercentages().getHitByPitchPct()) {
            batter.getBattingStatsForYear(year).incrementHitByPitch();
            pitcher.getPitchingStatsForYear(year).incrementHitByPitch();

            advanceRunnersOnWalkOrHBP(batter, pitcher);

            setStartOfSpan();
            atBatSummary.append(batter.getLastName()).append(" Hit By Pitch! ");


            return true;
        }
        return false;
    }

    private void addStrikeAndCheckResults(Player batter, Player pitcher) {
        // Add a strike to count, if strikeout change gameState (if not 3 outs and runners on base check stealing)
        strikes++;
        if (strikes == 3) {
            // Record an out, check stealing if necessary, update stats and change game state
            setStartOfSpan();
            atBatSummary.append(batter.getLastName()).append(" Strikes Out! ");

            outs++;
            animationData.put(0, new Pair<>(0, false));
            battingLineForCurrentBatter.incrementAtBats();
            batter.getBattingStatsForYear(year).incrementStrikeOuts();
            battingLineForCurrentBatter.incrementStrikeOuts();
            repository.updateBattingLine(battingLineForCurrentBatter);
            pitcher.getPitchingStatsForYear(year).incrementStrikeOuts();
            pitchingLineForCurrentPitcher.incrementStrikeOutsMade();
            repository.updatePitchingLine(pitchingLineForCurrentPitcher);
            if (outs < 3) {
                if (!basesAreEmpty()) {
                    // less than 3 outs and runners on base
                    checkForStealingAndUpdate(getGameState(), pitcher);
                }
            }
            moveToNextBatterInLineup();
            atBatOver(pitcher);
        } else {
            checkForStealingAndUpdate(getGameState(), pitcher);
        }

    }

    private void checkForStealingAndUpdate(int gameState, Player pitcher) {

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
                    fielderReceivingThrow = getFielderReceivingThrow();
                    // check for error on throw (catcher) or on catch (fielderReceivingThrow)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(fielderReceivingThrow));
                    if (error) {
                        errorOnPlay = true;
                        if (outs == 2) {
                            areAllRunsEarned = false;
                        }
                    }
                    if (ifRunnerIsSuccessful(runnerOnFirst)) {
                        //Runner stole the base
                        atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" Stole Second Base! ");

                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;
                        runnerOnSecond.getRunner().getBattingStatsForYear(year).incrementStolenBases();
                        animationData.put(1, new Pair<>(1, true));

                        if (error) {
                            atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(", advances to Third on Error! ");

                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                            animationData.put(1, new Pair<>(2, true));
                        }
                        return;
                    } else {
                        if (error) {
                            atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" Safe at Second Base on Error! ");

                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                            animationData.put(1, new Pair<>(1, true));
                        } else {
                            //Runner is caught stealing
                            atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" OUT! Caught Stealing Second Base! ");

                            outs++;
                            animationData.put(1, new Pair<>(1, false));
                            runnerOnFirst.getRunner().getBattingStatsForYear(year).incrementCaughtStealing();
                            runnerOnFirst = null;
                            if (outs == 3) {
                                atBatOver(pitcher);
                            }
                        }
                        return;
                    }
                } else return;
            case NO_OUTS_RUNNER_ON_SECOND:
                if (ifRunnerSteals(runnerOnSecond)) {
                    setStartOfSpan();
                    // atBatSummary.append("Stolen Base Attempt : ");
                    // check for error on throw (catcher) or on catch (third base)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(SCOREKEEPING_THIRD_BASE));
                    if (error) {
                        errorOnPlay = true;
                        if (outs == 2) {
                            areAllRunsEarned = false;
                        }
                    }
                    if (ifRunnerIsSuccessful(runnerOnSecond)) {
                        //Runner stole the base
                        atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" Stole Third Base! ");

                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = null;
                        runnerOnThird.getRunner().getBattingStatsForYear(year).incrementStolenBases();
                        animationData.put(2, new Pair<>(1, true));
                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runner
                            setStartOfSpan();
                            atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Scores on the Error! ");

                            runsScored++;
                            runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                            runnerOnThird.getBattingLineForRunner().incrementRuns();
                            repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                            runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                            runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                            repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                            runnerOnThird = null;
                            animationData.put(2, new Pair<>(2, true));
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner safe on error, advance runner but no credit for stolen base
                            atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" Safe at Third Base on the Error! ");

                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                            animationData.put(2, new Pair<>(1, true));
                            return;
                        } else {
                            //Runner is caught stealing
                            atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" OUT! Caught Stealing Third Base! ");

                            outs++;
                            runnerOnSecond.getRunner().getBattingStatsForYear(year).incrementCaughtStealing();
                            runnerOnSecond = null;
                            animationData.put(2, new Pair<>(1, false));
                            if (outs == 3) {
                                atBatOver(pitcher);
                            }
                        }
                        return;
                    }
                } else return;
            case NO_OUTS_RUNNER_ON_THIRD:
                // No stealing home right now, maybe add in later update but percentage is VERY small
                return;
            case NO_OUTS_RUNNER_ON_FIRST_AND_SECOND:
                if (ifRunnerSteals(runnerOnSecond)) {
                    setStartOfSpan();
                    // check for error on throw (catcher) or on catch (third base)
                    // atBatSummary.append("Stolen Base Attempt : ");
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(SCOREKEEPING_THIRD_BASE));
                    if (error) {
                        errorOnPlay = true;
                        if (outs == 2) {
                            areAllRunsEarned = false;
                        }
                    }
                    if (ifRunnerIsSuccessful(runnerOnSecond)) {
                        //Runner stole the base
                        atBatSummary.append("Double Steal!!! ").append(runnerOnSecond.getRunner().getLastName()).append(" Stole Third! and ").append(runnerOnFirst.getRunner().getLastName())
                                .append(" Stole Second! ");

                        runnerOnSecond.getRunner().getBattingStatsForYear(year).incrementStolenBases();
                        runnerOnFirst.getRunner().getBattingStatsForYear(year).incrementStolenBases();
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;
                        animationData.put(2, new Pair<>(1, true));
                        animationData.put(1, new Pair<>(1, true));
                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            atBatSummary.append("Runners advance on Error ").append(runnerOnThird.getRunner().getLastName()).append(" Scores!!! ").append(runnerOnSecond.getRunner().getLastName())
                                    .append(" advances to Third.  ");

                            runsScored++;
                            runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                            runnerOnThird.getBattingLineForRunner().incrementRuns();
                            repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                            runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                            runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                            repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                            animationData.put(2, new Pair<>(2, true));
                            animationData.put(1, new Pair<>(2, true));
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner safe on error, advance runner but no credit for stolen base
                            atBatSummary.append("Double Steal!!! ").append(runnerOnSecond.getRunner().getLastName()).append(" safe at Third on Error and ").append(runnerOnFirst.getRunner().getLastName())
                                    .append(" advances to Second Base! ");

                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                            animationData.put(2, new Pair<>(1, true));
                            animationData.put(1, new Pair<>(1, true));
                        } else {
                            //Runner is caught stealing
                            atBatSummary.append("Double Steal!!! ").append(runnerOnSecond.getRunner().getLastName()).append(" OUT at Third Base! and ").append(runnerOnFirst.getRunner().getLastName())
                                    .append(" advances to Second Base! ");

                            outs++;
                            runnerOnSecond.getRunner().getBattingStatsForYear(year).incrementCaughtStealing();
                            runnerOnSecond = null;
                            animationData.put(2, new Pair<>(1, false));

                            if (outs == 3) {
                                atBatOver(pitcher);
                            } else {
                                runnerOnSecond = runnerOnFirst;
                                runnerOnFirst = null;
                                animationData.put(1, new Pair<>(1, true));
                            }
                        }
                        return;
                    }

                } else return;

            case NO_OUTS_RUNNER_ON_FIRST_AND_THIRD:
                if (ifRunnerSteals(runnerOnFirst)) {
                    setStartOfSpan();
                    //atBatSummary.append("Stolen Base Attempt : ");
                    fielderReceivingThrow = getFielderReceivingThrow();
                    // check for error on throw (catcher) or on catch (fielderReceivingThrow)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(fielderReceivingThrow));
                    if (error) {
                        errorOnPlay = true;
                        if (outs == 2) {
                            areAllRunsEarned = false;
                        }
                    }
                    if (ifRunnerIsSuccessful(runnerOnFirst)) {
                        //Runner stole the base
                        atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" Stole Second Base! ");

                        runnerOnFirst.getRunner().getBattingStatsForYear(year).incrementStolenBases();
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;

                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" advances to Third on Error, ").append(runnerOnThird.getRunner().getLastName()).append(" Scores on Error!!! ");

                            runsScored++;
                            runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                            runnerOnThird.getBattingLineForRunner().incrementRuns();
                            repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                            runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                            runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                            repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" Safe at Second on Error, ").append(runnerOnThird.getRunner().getLastName()).append(" Scores on Error!!! ");

                            runsScored++;
                            runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                            runnerOnThird.getBattingLineForRunner().incrementRuns();
                            repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                            pitchersResponsible.add(runnerOnThird.getPitcherResponsible());
                            runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                            runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                            repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                            runnerOnThird = null;
                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                        } else {
                            //Runner is caught stealing
                            atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" Caught Stealing, OUT at Second!!! ");

                            runnerOnFirst.getRunner().getBattingStatsForYear(year).incrementCaughtStealing();
                            outs++;
                            runnerOnFirst = null;

                            if (outs == 3) {
                                atBatOver(pitcher);
                            }
                        }
                        return;
                    }
                } else return;
            case NO_OUTS_RUNNER_ON_SECOND_AND_THIRD:
                return;
            case NO_OUTS_BASES_LOADED:
                return;
            default:
        }
    }

    private int getFielderReceivingThrow() {
        if (lineup.get(currentBatter).getHits().equals(resourceProvider.getString(R.string.left_handed))) {
            // Shortstop covers when batter is left-handed
            return SCOREKEEPING_SHORTSTOP;
        } else if (lineup.get(currentBatter).getHits().equals(resourceProvider.getString(R.string.right_handed))) {
            // Second covers when batter is right-handed
            return SCOREKEEPING_SECOND_BASE;
        } else if (defense.get(SCOREKEEPING_PITCHER).getThrowingHand().equals(resourceProvider.getString(R.string.left_handed))) {
            // If batter is a switch hitter, he hits right-handed when pitcher is left-handed, so second covers
            return SCOREKEEPING_SECOND_BASE;
        } else {
            // If batter is a switch hitter, he hits left-handed when pitcher is right-handed, so shortstop covers
            return SCOREKEEPING_SHORTSTOP;
        }
    }

    private boolean checkForError(int playerPositionNumber) {
        int playerErrorRate = defense.get(playerPositionNumber).getHittingPercentages().getErrorPct();
        if (random.nextInt(ONE_HUNDRED_PERCENT) < playerErrorRate) {
            // Error committed add to stats and return true
            // atBatSummary.append(", Error : ").append(getPositionNameFromScorekeeperPosition(playerPositionNumber)).append(" , ");
            defense.get(playerPositionNumber).getBattingStatsForYear(year).incrementErrors();
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
        return (random.nextInt(ONE_HUNDRED_PERCENT) < stolenBasePct);
    }

    private boolean ifRunnerSteals(Runner runner) {
        int stolenBaseRate = runner.getRunner().getHittingPercentages().getStolenBaseRate();
        if (runnerOnSecond == runner) {
            stolenBaseRate = stolenBaseRate / 5;

        }
        return (random.nextInt(ONE_HUNDRED_PERCENT) < stolenBaseRate);
    }

    private int getGameState() {
        int gameState;
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
        return random.nextInt(ONE_HUNDRED_PERCENT) < ((batterPct * pitcherPct) / leagueAveragePct);
    }

    private boolean isPitchInStrikeZone(Player pitcher) {
        int randomToCheck = random.nextInt(ONE_HUNDRED_PERCENT);
        /*if (balls == 0 && strikes == 0) {  // right now the first pitch strike percentage includes foul balls and balls in play that are outside the zone, so can't be used to determine if pitch is a strike
            if (randomToCheck < pitcher.getPitchingPercentages().getFirstPitchStrikePct()) {
                //In the strike zone
                return true;
            } else {
                //Outside the strike zone
                return false;
            }
        } else */

        return randomToCheck < pitcher.getPitchingPercentages().getZonePct();   //returns true - if in the strike zone
                                                                                //       false - if outside the strike zone

    }

    private boolean isPitchThrown(Player pitcher) {
        if (random.nextInt(ONE_HUNDRED_PERCENT) < pitcher.getPitchingPercentages().getBalkPct()) {
            //Balk - add it to the pitchers stats, then check runners
            PitchingStats currentPitchingStats = pitcher.getPitchingStatsForYear(year);
            currentPitchingStats.incrementBalks();
            startOfSpan = atBatSummary.length();
            atBatSummary.append(" Balk, ");


            //Check if bases are empty...if so add a ball to the count and return.
            if (basesAreEmpty()) {
                atBatSummary.append(" (add a ball to count), ");
                balls++;
                //No Pitch Thrown
                return false;
            }
            //Otherwise advance runners and adjust stats
            if (runnerOnThird != null) {
                startOfSpan = atBatSummary.length();
                atBatSummary.append(runnerOnThird.getRunner().getLastName()).append(" Scores ");

                runnerOnThird.getRunner().getBattingStatsForYear(year).incrementRuns();
                runnerOnThird.getBattingLineForRunner().incrementRuns();
                repository.updateBattingLine(runnerOnThird.getBattingLineForRunner());
                runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).setRuns(runnerOnThird.getPitcherResponsible().getPitchingStatsForYear(year).getRuns() + 1);
                runnerOnThird.getPitchingLineForPitcherResponsible().setRunsAllowed(runnerOnThird.getPitchingLineForPitcherResponsible().getRunsAllowed() + 1);
                repository.updatePitchingLine(runnerOnThird.getPitchingLineForPitcherResponsible());
                //We've advanced the runner and adjusted stats, now empty third base
                runnerOnThird = null;
            }
            if (runnerOnSecond != null) {
                //move runner to third and empty second base
                startOfSpan = atBatSummary.length();
                atBatSummary.append(runnerOnSecond.getRunner().getLastName()).append(" goes to Third ");

                runnerOnThird = runnerOnSecond;
                runnerOnSecond = null;
            }
            if (runnerOnFirst != null) {
                //move runner to second and empty first base
                startOfSpan = atBatSummary.length();
                atBatSummary.append(runnerOnFirst.getRunner().getLastName()).append(" goes to Second ");

                runnerOnSecond = runnerOnFirst;
                runnerOnFirst = null;
            }
            //No Pitch Thrown
            return false;
        } else {
            //Throw Pitch
            pitcher.getPitchingPercentages().setPitchingStaminaUsed(pitcher.getPitchingPercentages().getPitchingStaminaUsed() + 1);
            pitchingLineForCurrentPitcher.incrementPitchesThrown();
            repository.updatePitchingLine(pitchingLineForCurrentPitcher);
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
        System.arraycopy(runners, 0, this.runners, 0, runners.length);
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

    public boolean isHit() {
        return hitOnPlay;
    }

    public static SpannableStringBuilder getAtBatSummary() {
        return atBatSummary;
    }

    public List<Player> getPitchersResponsible() {
        return pitchersResponsible;
    }

    public Runner getRunnerOnFirst() {
        return runnerOnFirst;
    }

    public Runner getRunnerOnSecond() {
        return runnerOnSecond;
    }

    public Runner getRunnerOnThird() {
        return runnerOnThird;
    }

    public TreeMap<Integer, Pair<Integer, Boolean>> getAnimationData() {
        return animationData;
    }
}
