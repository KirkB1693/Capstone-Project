package com.example.android.baseballbythenumbers.Simulators;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.BattingStats;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.R;

import java.util.Random;
import java.util.TreeMap;

import timber.log.Timber;

import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.FieldArea.CENTER;
import static com.example.android.baseballbythenumbers.Data.Constants.FieldArea.LEFT;
import static com.example.android.baseballbythenumbers.Data.Constants.FieldArea.RIGHT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_HARD_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_HARD_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_HARD_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_MED_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_MED_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_MED_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_SOFT_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_SOFT_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.HitRates.BATTING_SOFT_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Data.Constants.QualityOfContact.HARD;
import static com.example.android.baseballbythenumbers.Data.Constants.QualityOfContact.MEDIUM;
import static com.example.android.baseballbythenumbers.Data.Constants.QualityOfContact.SOFT;
import static com.example.android.baseballbythenumbers.Data.Constants.TypeOfHit.FLYBALL;
import static com.example.android.baseballbythenumbers.Data.Constants.TypeOfHit.GROUNDBALL;
import static com.example.android.baseballbythenumbers.Data.Constants.TypeOfHit.LINE_DRIVE;
import static com.example.android.baseballbythenumbers.Data.GameStates.NO_OUTS_BASES_LOADED;
import static com.example.android.baseballbythenumbers.Data.GameStates.NO_OUTS_NOBODY_ON;
import static com.example.android.baseballbythenumbers.Data.GameStates.NO_OUTS_RUNNER_ON_FIRST;
import static com.example.android.baseballbythenumbers.Data.GameStates.NO_OUTS_RUNNER_ON_FIRST_AND_SECOND;
import static com.example.android.baseballbythenumbers.Data.GameStates.NO_OUTS_RUNNER_ON_FIRST_AND_THIRD;
import static com.example.android.baseballbythenumbers.Data.GameStates.NO_OUTS_RUNNER_ON_SECOND;
import static com.example.android.baseballbythenumbers.Data.GameStates.NO_OUTS_RUNNER_ON_SECOND_AND_THIRD;
import static com.example.android.baseballbythenumbers.Data.GameStates.NO_OUTS_RUNNER_ON_THIRD;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_CATCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_CENTER_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_FIRST_BASE;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_LEFT_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_PITCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_RIGHT_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_SECOND_BASE;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_SHORTSTOP;
import static com.example.android.baseballbythenumbers.Data.Positions.SCOREKEEPING_THIRD_BASE;

public class AtBatSimulator {

    private static final int ONE_HUNDERED_PERCENT = 10000;
    private int[] count;

    private int outs;

    private int runsScored;

    private int currentBatter;

    private TreeMap<Integer, Player> lineup;

    private Player[] runners;

    private TreeMap<Integer, Player> defense;

    private Random random;

    private Player runnerOnFirst;

    private Player runnerOnSecond;

    private Player runnerOnThird;

    private int balls;

    private int strikes;

    private int gameStateAfterAtBat;

    private Context context;

    public AtBatSimulator(Context context, int[] count, int outs, int currentBatter, TreeMap<Integer, Player> lineup, Player[] runners, TreeMap<Integer, Player> defense) {
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
        checkForValidLineupOrDefense(lineup);
        checkForValidRunners(runners);
        checkForValidLineupOrDefense(defense);

        balls = count[0];
        strikes = count[1];

        runnerOnFirst = runners[0];
        runnerOnSecond = runners[1];
        runnerOnThird = runners[2];

        gameStateAfterAtBat = 0;
    }

    private void checkForValidRunners(Player[] runners) {
        try {
            if (runners == null) {
                throw new IllegalArgumentException("Runners passed to AtBatSimulator is Null, that is Invalid!");
            } else if (runners.length != 3) {
                throw new IllegalArgumentException("Runners passed to AtBatSimulator are Invalid!");
            }
        } catch (IllegalArgumentException e) {
            Timber.e(e, " The runners passed in were : %s", runners.toString());
        }
    }

    private void checkForValidLineupOrDefense(TreeMap<Integer, Player> lineupOrDefense) {
        try {
            if (lineupOrDefense.isEmpty()) {
                throw new IllegalArgumentException("Lineup or Defense passed to AtBatSimulator is Empty!");
            } else if (lineupOrDefense.size() != 9) {
                throw new IllegalArgumentException("Lineup or Defense passed to AtBatSimulator is Invalid!");
            }
        } catch (IllegalArgumentException e) {
            Timber.e(e, " The lineup or defense passed in was : %s", lineupOrDefense);
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
            Timber.e(e, " Illegal Count passed was %s", count.toString());
        }
    }


    public int simulateAtBat() {
        runsScored = 0;

        Player batter = lineup.get(currentBatter);
        Player pitcher = defense.get(1);

        assert batter != null;
        BattingStats currentBatterBattingStatsForThisSeason = batter.getBattingStats().get(0);
        currentBatterBattingStatsForThisSeason.setPlateAppearances(currentBatterBattingStatsForThisSeason.getPlateAppearances() + 1);

        assert pitcher != null;
        PitchingStats currentPitcherPitchingStatsForThisSeason = pitcher.getPitchingStats().get(0);
        currentPitcherPitchingStatsForThisSeason.setTotalBattersFaced(currentPitcherPitchingStatsForThisSeason.getTotalBattersFaced() + 1);

        while (gameStateAfterAtBat == 0) {
            if (isPitchThrown(pitcher)) {
                if (isPitchInStrikeZone(pitcher)) {
                    //Pitch in Strike Zone, check for batter swing
                    int pitcherZSwingPct = pitcher.getPitchingPercentages().getZSwingPct();
                    int batterZSwingPct = batter.getHittingPercentages().getZSwingPct();
                    if (oddsRatioMethod(batterZSwingPct, pitcherZSwingPct, BATTING_Z_SWING_PCT_MEAN)) {
                        //Batter swings at the pitch, check for contact
                        int batterZContact = batter.getHittingPercentages().getZContactPct();
                        int pitcherZContactPct = pitcher.getPitchingPercentages().getZContactPct();
                        if (oddsRatioMethod(batterZContact, pitcherZContactPct, BATTING_Z_CONTACT_PCT_MEAN)) {
                            // Batter makes contact
                            resolveContact(batter, pitcher);
                        } else {
                            // Batter swings and misses
                            addStrikeAndCheckResults(batter, pitcher);
                        }
                    } else {
                        //Batter takes the pitch
                        addStrikeAndCheckResults(batter, pitcher);
                    }
                } else {
                    //Pitch out of strike zone, check for batter swing
                    int pitcherOSwingPct = pitcher.getPitchingPercentages().getOSwingPct();
                    int batterOSwingPct = batter.getHittingPercentages().getOSwingPct();
                    if (oddsRatioMethod(batterOSwingPct, pitcherOSwingPct, BATTING_O_SWING_PCT_MEAN)) {
                        //Batter swings at the pitch
                        int batterOContact = batter.getHittingPercentages().getOContactPct();
                        int pitcherOContactPct = pitcher.getPitchingPercentages().getOContactPct();
                        if (oddsRatioMethod(batterOContact, pitcherOContactPct, BATTING_O_CONTACT_PCT_MEAN)) {
                            // Batter makes contact
                            resolveContact(batter, pitcher);
                        } else {
                            // Batter swings and misses
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

        return gameStateAfterAtBat;
    }

    private void addBallAndCheckResults(Player batter, Player pitcher) {
        // add ball to count, check stealing, if ball four, advance runners as needed and update stats and game state
        balls++;
        if (balls == 4) {
            // Update batter and pitcher stats to reflect walk
            batter.getBattingStats().get(0).setWalks(batter.getBattingStats().get(0).getWalks() + 1);
            pitcher.getPitchingStats().get(0).setWalks(pitcher.getPitchingStats().get(0).getWalks() + 1);
            if (runnerOnFirst == null) {
                // No runner on first, move batter to first and game state
                runnerOnFirst = batter;
                moveToNextBatterInLineup();
                atBatOver();
            } else {
                if (runnerOnSecond == null) {
                    // runner on first, nobody on second, move batter to first and runner on first to second
                    runnerOnSecond = runnerOnFirst;
                    runnerOnFirst = batter;
                    moveToNextBatterInLineup();
                    atBatOver();
                } else {
                    if (runnerOnThird == null) {
                        // runners on first and second
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = batter;
                        moveToNextBatterInLineup();
                        atBatOver();
                    } else {
                        // bases loaded
                        runsScored++;
                        batter.getBattingStats().get(0).setRunsBattedIn(batter.getBattingStats().get(0).getRunsBattedIn() + 1);
                        pitcher.getPitchingStats().get(0).setRuns(pitcher.getPitchingStats().get(0).getRuns() + 1);
                        pitcher.getPitchingStats().get(0).setEarnedRuns(pitcher.getPitchingStats().get(0).getEarnedRuns() + 1);
                        runnerOnThird.getBattingStats().get(0).setRunsBattedIn(runnerOnThird.getBattingStats().get(0).getRunsBattedIn() + 1);
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = batter;
                        moveToNextBatterInLineup();
                        atBatOver();
                    }
                }
            }
        } else {
            checkForStealingAndUpdate(getGameState());
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

    private void resolveContact(Player batter, Player pitcher) {
        int typeOfHit = getTypeOfHit(batter);
        if (isHomeRun(batter)) {
            int numberOfRunners = 0;
            if (runnerOnFirst != null) {
                numberOfRunners++;
                runsScored++;
                runnerOnFirst.getBattingStats().get(0).setRuns(runnerOnFirst.getBattingStats().get(0).getRuns() + 1);
                runnerOnFirst = null;
            }
            if (runnerOnSecond != null) {
                numberOfRunners++;
                runsScored++;
                runnerOnSecond.getBattingStats().get(0).setRuns(runnerOnSecond.getBattingStats().get(0).getRuns() + 1);
            }
            if (runnerOnThird != null) {
                numberOfRunners++;
                runsScored++;
                runnerOnThird.getBattingStats().get(0).setRuns(runnerOnThird.getBattingStats().get(0).getRuns() + 1);
            }
            runsScored++;
            batter.getBattingStats().get(0).setRuns(batter.getBattingStats().get(0).getRuns() + 1);
            batter.getBattingStats().get(0).setHomeRuns(batter.getBattingStats().get(0).getHomeRuns() + 1);
            batter.getBattingStats().get(0).setHits(batter.getBattingStats().get(0).getHits() + 1);
            batter.getBattingStats().get(0).setRunsBattedIn(batter.getBattingStats().get(0).getRunsBattedIn() + 1 + numberOfRunners);

            pitcher.getPitchingStats().get(0).setRuns(pitcher.getPitchingStats().get(0).getRuns() + 1 + numberOfRunners);
            pitcher.getPitchingStats().get(0).setHomeRuns(pitcher.getPitchingStats().get(0).getHomeRuns() + 1);
            pitcher.getPitchingStats().get(0).setHits(pitcher.getPitchingStats().get(0).getHits() + 1);
            pitcher.getPitchingStats().get(0).setEarnedRuns(pitcher.getPitchingStats().get(0).getEarnedRuns() + 1 + numberOfRunners);

        } else {
            if (checkForFoulBall(batter)) {
                // ball is foul
                if (strikes < 2) {
                    strikes++;
                }
            } else {
                // ball in play
                int contactQuality = getQualityOfContact(batter);
                if (isHit(typeOfHit, contactQuality)) {
                    resolveHit(batter, pitcher, typeOfHit, contactQuality);
                } else {
                    resolveOut(batter, pitcher, typeOfHit, contactQuality);
                }
            }
        }
    }

    private void resolveOut(Player batter, Player pitcher, int typeOfHit, int contactQuality) {
        int whereBallIsHit = getHitLocation(batter, typeOfHit, contactQuality, true);
        outs ++;
        if (outs == 3) {
            moveToNextBatterInLineup();
            atBatOver();
        }
    }

    private void resolveHit(Player batter, Player pitcher, int typeOfHit, int contactQuality) {
        int whereBallIsHit = getHitLocation(batter, typeOfHit, contactQuality, false);
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
        int fieldArea = 0;
        int checkForFieldArea = random.nextInt(ONE_HUNDERED_PERCENT);
        if (checkForFieldArea < batter.getHittingPercentages().getPullPct()) {
            // Batter pulled the ball
            if (batter.getHits().equals(context.getString(R.string.left_handed))) {
                fieldArea = RIGHT;
            } else {
                fieldArea = LEFT;
            }
            if (checkForFieldArea < (batter.getHittingPercentages().getPullPct() + batter.getHittingPercentages().getCenterPct())) {
                fieldArea = CENTER;
            } else {
                if (batter.getHits().equals(context.getString(R.string.left_handed))) {
                    fieldArea = LEFT;
                } else {
                    fieldArea = RIGHT;
                }
            }

        }
        return fieldArea;
    }



    private boolean isHomeRun(Player batter) {
        return random.nextInt(ONE_HUNDERED_PERCENT) < batter.getHittingPercentages().getHomeRunPct();
    }

    private boolean isHit(int typeOfHit, int contactQuality) {
        int checkForHit = random.nextInt(ONE_HUNDERED_PERCENT);
        if (typeOfHit == GROUNDBALL) {
            if (contactQuality == HARD) {
                return checkForHit < BATTING_HARD_GROUNDBALL_HIT_PCT;
            } else if (contactQuality == MEDIUM) {
                return checkForHit < BATTING_MED_GROUNDBALL_HIT_PCT;
            } else {
                return checkForHit < BATTING_SOFT_GROUNDBALL_HIT_PCT;
            }
        } else if (typeOfHit == LINE_DRIVE) {
            if (contactQuality == HARD) {
                return checkForHit < BATTING_HARD_LINE_DRIVE_HIT_PCT;
            } else if (contactQuality == MEDIUM) {
                return checkForHit < BATTING_MED_LINE_DRIVE_HIT_PCT;
            } else {
                return checkForHit < BATTING_SOFT_LINE_DRIVE_HIT_PCT;
            }
        } else {
            if (contactQuality == HARD) {
                return checkForHit < BATTING_HARD_FLYBALL_HIT_PCT;
            } else if (contactQuality == MEDIUM) {
                return checkForHit < BATTING_MED_FLYBALL_HIT_PCT;
            } else {
                return checkForHit < BATTING_SOFT_FLYBALL_HIT_PCT;
            }
        }
    }


    private int getQualityOfContact(Player batter) {
        int checkForHitQuality = random.nextInt(ONE_HUNDERED_PERCENT);
        if (checkForHitQuality < batter.getHittingPercentages().getHardPct()) {
            return HARD;
        } else if (checkForHitQuality < (batter.getHittingPercentages().getHardPct() + batter.getHittingPercentages().getMedPct())) {
            return MEDIUM;
        } else {
            return SOFT;
        }
    }

    private boolean checkForFoulBall(Player batter) {
        return random.nextInt(ONE_HUNDERED_PERCENT) < batter.getHittingPercentages().getFoulBallPct();
    }

    private int getTypeOfHit(Player batter) {
        int checkForHitType = random.nextInt(ONE_HUNDERED_PERCENT);
        if (checkForHitType < batter.getHittingPercentages().getGroundBallPct()) {
            return GROUNDBALL;
        } else if (checkForHitType < (batter.getHittingPercentages().getGroundBallPct() + batter.getHittingPercentages().getLineDrivePct())) {
            return LINE_DRIVE;
        } else {
            return FLYBALL;
        }
    }

    private void checkForWildPitch(Player pitcher) {
        if (basesAreEmpty()) {
            return;
        }
        if (random.nextInt(ONE_HUNDERED_PERCENT) < pitcher.getPitchingPercentages().getWildPitchPct()) {
            pitcher.getPitchingStats().get(0).setWildPitches(pitcher.getPitchingStats().get(0).getWildPitches() + 1);
            if (runnerOnThird != null) {
                runsScored++;
                runnerOnThird.getBattingStats().get(0).setRuns(runnerOnThird.getBattingStats().get(0).getRuns() + 1);
                pitcher.getPitchingStats().get(0).setRuns(pitcher.getPitchingStats().get(0).getRuns() + 1);
                runnerOnThird = null;
            }
            runnerOnThird = runnerOnSecond;
            runnerOnSecond = runnerOnFirst;
            runnerOnFirst = null;
        }
    }

    private void addStrikeAndCheckResults(Player Batter, Player Pitcher) {
        // Add a strike to count, if strikeout change gameState (if not 3 outs and runners on base check stealing)
        strikes++;
        if (strikes == 3) {
            // Record an out, check stealing if necessary, update stats and change game state
            outs++;
            balls = 0;
            strikes = 0;
            if (outs < 3) {
                if (!basesAreEmpty()) {
                    checkForStealingAndUpdate(getGameState());
                    moveToNextBatterInLineup();
                    atBatOver();
                } else {
                    return;
                }
            } else {
                moveToNextBatterInLineup();
                atBatOver();
            }
        }
        checkForStealingAndUpdate(getGameState());
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
                    fielderReceivingThrow = getFielderRecevingThrow();
                    // check for error on throw (catcher) or on catch (fielderReceivingThrow)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(fielderReceivingThrow));
                    if (ifRunnerIsSuccessful(runnerOnFirst)) {
                        //Runner stole the base
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;
                        runnerOnSecond.getBattingStats().get(0).setStolenBases(runnerOnSecond.getBattingStats().get(0).getStolenBases() + 1);
                        if (error) {
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                        }
                        return;
                    } else {
                        if (error) {
                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                            return;
                        } else {
                            //Runner is caught stealing
                            outs++;
                            runnerOnFirst.getBattingStats().get(0).setCaughtStealing(runnerOnFirst.getBattingStats().get(0).getCaughtStealing() + 1);
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
                    // check for error on throw (catcher) or on catch (third base)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(SCOREKEEPING_THIRD_BASE));
                    if (ifRunnerIsSuccessful(runnerOnSecond)) {
                        //Runner stole the base
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = null;
                        runnerOnThird.getBattingStats().get(0).setStolenBases(runnerOnSecond.getBattingStats().get(0).getStolenBases() + 1);
                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runner
                            runsScored++;
                            runnerOnThird.getBattingStats().get(0).setRuns(runnerOnThird.getBattingStats().get(0).getRuns() + 1);
                            defense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).setRuns(defense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).getRuns() + 1);
                            runnerOnThird = null;
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner safe on error, advance runner but no credit for stolen base
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                            return;
                        } else {
                            //Runner is caught stealing
                            outs++;
                            runnerOnSecond.getBattingStats().get(0).setCaughtStealing(runnerOnSecond.getBattingStats().get(0).getCaughtStealing() + 1);
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
                    // check for error on throw (catcher) or on catch (third base)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(SCOREKEEPING_THIRD_BASE));
                    if (ifRunnerIsSuccessful(runnerOnSecond)) {
                        //Runner stole the base
                        runnerOnThird = runnerOnSecond;
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;
                        runnerOnSecond.getBattingStats().get(0).setStolenBases(runnerOnSecond.getBattingStats().get(0).getStolenBases() + 1);
                        runnerOnFirst.getBattingStats().get(0).setStolenBases(runnerOnFirst.getBattingStats().get(0).getStolenBases() + 1);
                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            runsScored++;
                            runnerOnThird.getBattingStats().get(0).setRuns(runnerOnThird.getBattingStats().get(0).getRuns() + 1);
                            defense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).setRuns(defense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).getRuns() + 1);
                            runnerOnThird = null;
                            runnerOnThird = runnerOnSecond;
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner safe on error, advance runner but no credit for stolen base
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                            return;
                        } else {
                            //Runner is caught stealing
                            outs++;
                            runnerOnSecond.getBattingStats().get(0).setCaughtStealing(runnerOnSecond.getBattingStats().get(0).getCaughtStealing() + 1);
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
                    fielderReceivingThrow = getFielderRecevingThrow();
                    // check for error on throw (catcher) or on catch (fielderReceivingThrow)
                    error = (checkForError(SCOREKEEPING_CATCHER) || checkForError(fielderReceivingThrow));
                    if (ifRunnerIsSuccessful(runnerOnFirst)) {
                        //Runner stole the base
                        runnerOnFirst.getBattingStats().get(0).setStolenBases(runnerOnFirst.getBattingStats().get(0).getStolenBases() + 1);
                        runnerOnSecond = runnerOnFirst;
                        runnerOnFirst = null;

                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            runsScored++;
                            runnerOnThird.getBattingStats().get(0).setRuns(runnerOnThird.getBattingStats().get(0).getRuns() + 1);
                            defense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).setRuns(defense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).getRuns() + 1);
                            runnerOnThird = runnerOnSecond;
                            runnerOnSecond = null;
                        }
                        return;
                    } else {
                        if (error) {
                            // Runner scores on error, add run scored to pitcher and runner stats, advance the runners
                            runsScored++;
                            runnerOnThird.getBattingStats().get(0).setRuns(runnerOnThird.getBattingStats().get(0).getRuns() + 1);
                            defense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).setRuns(defense.get(SCOREKEEPING_PITCHER).getPitchingStats().get(0).getRuns() + 1);
                            runnerOnThird = null;
                            runnerOnSecond = runnerOnFirst;
                            runnerOnFirst = null;
                            return;
                        } else {
                            //Runner is caught stealing
                            runnerOnFirst.getBattingStats().get(0).setCaughtStealing(runnerOnFirst.getBattingStats().get(0).getCaughtStealing() + 1);
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
            defense.get(playerPositionNumber).getBattingStats().get(0).setErrors(defense.get(playerPositionNumber).getBattingStats().get(0).getErrors() + 1);
            return true;
        }
        // no error, return false
        return false;
    }

    private boolean ifRunnerIsSuccessful(Player runner) {
        int stolenBasePct = runner.getHittingPercentages().getStolenBasePct();
        if (runnerOnSecond != null) {
            stolenBasePct -= 500;

        }
        return (random.nextInt(ONE_HUNDERED_PERCENT) < stolenBasePct);
    }

    private boolean ifRunnerSteals(Player runner) {
        int stolenBaseRate = runner.getHittingPercentages().getStolenBaseRate();
        if (runnerOnSecond != null) {
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
        if (balls == 0 && strikes == 0) {
            if (randomToCheck < pitcher.getPitchingPercentages().getFirstPitchStrikePct()) {
                //In the strike zone
                return true;
            } else {
                //Outside the strike zone
                return false;
            }
        } else if (randomToCheck < pitcher.getPitchingPercentages().getZonePct()) {
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
                BattingStats runnerOnThirdStats = runnerOnThird.getBattingStats().get(0);
                runnerOnThirdStats.setRuns(runnerOnThirdStats.getRuns() + 1);
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
            return true;
        }
    }

    private boolean basesAreEmpty() {
        return (runnerOnFirst == null && runnerOnSecond == null && runnerOnThird == null);
    }

    public int getRunsScored() {
        return runsScored;
    }

    public Player[] getRunners() {
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

    public void setRunners(Player[] runners) {
        this.runners = runners;
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

}
