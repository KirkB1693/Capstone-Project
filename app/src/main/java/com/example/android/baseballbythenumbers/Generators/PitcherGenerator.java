package com.example.android.baseballbythenumbers.Generators;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.BattingStats;
import com.example.android.baseballbythenumbers.Data.HittingPercentages;
import com.example.android.baseballbythenumbers.Data.PitchingPercentages;
import com.example.android.baseballbythenumbers.Data.PitchingStats;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.Data.Name;
import com.example.android.baseballbythenumbers.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.android.baseballbythenumbers.Constants.Constants.Handedness.PITCHER_THROWS_LEFT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_HARD_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_HARD_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_HARD_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_MED_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_MED_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_MED_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_SOFT_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_SOFT_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.HitRates.BATTING_SOFT_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBaseStats.*;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_BABIP_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_BABIP_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_BASERUNNING_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_BASERUNNING_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_BASERUNNING_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_BASERUNNING_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_CENTER_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_CENTER_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_CENTER_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_CENTER_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_DOUBLE_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_DOUBLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_DOUBLE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_DOUBLE_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_ERROR_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_ERROR_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_ERROR_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_ERROR_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_FOUL_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_FOUL_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_FOUL_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_FOUL_BALL_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_GROUND_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_GROUND_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_GROUND_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_GROUND_BALL_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HARD_HIT_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HARD_HIT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HARD_HIT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HARD_HIT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HIT_BY_PITCH_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HIT_BY_PITCH_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HIT_BY_PITCH_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HIT_BY_PITCH_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HOME_RUN_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_HOME_RUN_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_INFIELD_FLY_BALL_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_LINE_DRIVE_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_LINE_DRIVE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_LINE_DRIVE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_LINE_DRIVE_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_MED_HIT_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_MED_HIT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_MED_HIT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_MED_HIT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_O_CONTACT_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_O_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_O_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_O_CONTACT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_O_SWING_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_O_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_O_SWING_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_PULL_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_PULL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_PULL_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_PULL_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_SPEED_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_SPEED_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_SPEED_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_SPEED_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STAMINA_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STAMINA_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STAMINA_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STAMINA_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STOLEN_BASE_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STOLEN_BASE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STOLEN_BASE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STOLEN_BASE_RATE_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STOLEN_BASE_RATE_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STOLEN_BASE_RATE_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_STOLEN_BASE_RATE_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_TRIPLE_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_TRIPLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_TRIPLE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_Z_CONTACT_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_Z_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_Z_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_Z_CONTACT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_Z_SWING_PCT_MAX;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_Z_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.Constants.Constants.PitcherBatting.PITCHER_BATTING_Z_SWING_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Constants.PitchTypes.CHANGE_UP;
import static com.example.android.baseballbythenumbers.Constants.PitchTypes.CURVEBALL;
import static com.example.android.baseballbythenumbers.Constants.PitchTypes.CUTTER;
import static com.example.android.baseballbythenumbers.Constants.PitchTypes.FASTBALL;
import static com.example.android.baseballbythenumbers.Constants.PitchTypes.KNUCKLEBALL;
import static com.example.android.baseballbythenumbers.Constants.PitchTypes.SLIDER;
import static com.example.android.baseballbythenumbers.Constants.PitchTypes.SPLIT_FINGER;
import static com.example.android.baseballbythenumbers.Constants.Positions.LONG_RELIEVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.SHORT_RELEIVER;
import static com.example.android.baseballbythenumbers.Constants.Positions.STARTING_PITCHER;

public class PitcherGenerator {

    private static final double PITCHER_AGE_STD_DEV = 3.68;
    private static final double PITCHER_AGE_MEAN = 28.15;
    private static final int PITCHER_AGE_MIN = 19;
    private static final int PITCHER_AGE_MAX = 47;

    public static final int ONE_HUNDRED_PERCENT = 10000;
    public static final int MIN_PITCH_TYPES = 2;


    private int startingPitchers;

    private int longReliefPitchers;

    private int shortReliefPitchers;

    private NameGenerator nameGenerator;

    private final Random random;

    private Context context;

    private String leftHanded;

    private String rightHanded;


    public PitcherGenerator(Context context, NameGenerator nameGenerator, int startingPitchers, int longReliefPitchers, int shortReliefPitchers) {
        this.startingPitchers = startingPitchers;
        this.longReliefPitchers = longReliefPitchers;
        this.shortReliefPitchers = shortReliefPitchers;
        this.nameGenerator = nameGenerator;
        this.context = context;
        this.leftHanded = context.getResources().getString(R.string.left_handed);
        this.rightHanded = context.getResources().getString(R.string.right_handed);
        this.random = new Random();
    }

    public List<Player> generatePitchers(String teamId) {
        List<Player> starters = new ArrayList<>();
        List<Player> longRelievers = new ArrayList<>();
        List<Player> shortRelievers = new ArrayList<>();

        starters = generateStarters(teamId);
        longRelievers = generateLongRelievers(teamId);
        shortRelievers = generateShortRelievers(teamId);

        starters.addAll(longRelievers);
        starters.addAll(shortRelievers);
        return starters;
    }

    private List<Player> generateStarters(String teamId) {
        List<Player> startersList = new ArrayList<>();
        for (int i = 0; i < startingPitchers; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * PITCHER_AGE_STD_DEV) + PITCHER_AGE_MEAN);
            int age = checkBounds((int) trueAge, PITCHER_AGE_MIN, PITCHER_AGE_MAX);
            if (trueAge < PITCHER_AGE_MIN) {
                trueAge = PITCHER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > PITCHER_AGE_MAX) {
                trueAge = PITCHER_AGE_MAX + (trueAge - (int) trueAge);
            }
            String birthdate = getFormattedBirthdate(trueAge);
            String throwingSide;
            if (random.nextInt(ONE_HUNDRED_PERCENT) < PITCHER_THROWS_LEFT_PCT) {
                throwingSide = leftHanded;
            } else {
                throwingSide = rightHanded;
            }
            String hittingSide = throwingSide;

            Player newPitcher = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), STARTING_PITCHER, LONG_RELIEVER + SHORT_RELEIVER,
                    age, birthdate, hittingSide, throwingSide, null, null, generateHittingPercentages(), generatePitchingPercentages(STARTING_PITCHER), teamId);
            newPitcher.setBattingStats(generateBattingStats(newPitcher.getPlayerId()));
            newPitcher.setPitchingStats(generatePitchingStats(newPitcher.getPlayerId()));
            startersList.add(newPitcher);
        }
        return startersList;
    }

    private List<Player> generateLongRelievers(String teamId) {
        List<Player> longRelieversList = new ArrayList<>();
            for (int i = 0; i < longReliefPitchers; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * PITCHER_AGE_STD_DEV) + PITCHER_AGE_MEAN);
            int age = checkBounds((int) trueAge, PITCHER_AGE_MIN, PITCHER_AGE_MAX);
            if (trueAge < PITCHER_AGE_MIN) {
                trueAge = PITCHER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > PITCHER_AGE_MAX) {
                trueAge = PITCHER_AGE_MAX + (trueAge - (int) trueAge);
            }
            String birthdate = getFormattedBirthdate(trueAge);
            String throwingSide;
            if (random.nextInt(ONE_HUNDRED_PERCENT) < PITCHER_THROWS_LEFT_PCT) {
                throwingSide = leftHanded;
            } else {
                throwingSide = rightHanded;
            }
            String hittingSide = throwingSide;
            Player newPitcher = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), LONG_RELIEVER, STARTING_PITCHER + SHORT_RELEIVER,
                    age, birthdate, hittingSide, throwingSide, null, null, generateHittingPercentages(), generatePitchingPercentages(LONG_RELIEVER), teamId);
                newPitcher.setBattingStats(generateBattingStats(newPitcher.getPlayerId()));
                newPitcher.setPitchingStats(generatePitchingStats(newPitcher.getPlayerId()));
            longRelieversList.add(newPitcher);
        }
        return longRelieversList;
    }

    private List<Player> generateShortRelievers(String teamId) {
        List<Player> shortRelieversList = new ArrayList<>();
        for (int i = 0; i < shortReliefPitchers; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * PITCHER_AGE_STD_DEV) + PITCHER_AGE_MEAN);
            int age = checkBounds((int) trueAge, PITCHER_AGE_MIN, PITCHER_AGE_MAX);
            if (trueAge < PITCHER_AGE_MIN) {
                trueAge = PITCHER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > PITCHER_AGE_MAX) {
                trueAge = PITCHER_AGE_MAX + (trueAge - (int) trueAge);
            }
            String birthdate = getFormattedBirthdate(trueAge);
            String throwingSide;
            if (random.nextInt(ONE_HUNDRED_PERCENT) < PITCHER_THROWS_LEFT_PCT) {
                throwingSide = leftHanded;
            } else {
                throwingSide = rightHanded;
            }
            String hittingSide = throwingSide;
            Player newPitcher = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SHORT_RELEIVER, LONG_RELIEVER,
                    age, birthdate, hittingSide, throwingSide, null, null, generateHittingPercentages(), generatePitchingPercentages(SHORT_RELEIVER), teamId);
            newPitcher.setBattingStats(generateBattingStats(newPitcher.getPlayerId()));
            newPitcher.setPitchingStats(generatePitchingStats(newPitcher.getPlayerId()));
            shortRelieversList.add(newPitcher);
        }
        return shortRelieversList;
    }

    private String getFormattedBirthdate(double trueAge) {
        DateTime currentDate = DateTime.now();
        DateTime birthdate = currentDate.minusYears((int) trueAge).minusDays((int) ((trueAge - (int) trueAge) * context.getResources().getInteger(R.integer.daysInYear)));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM d, yyyy");
        return new LocalDate(birthdate).toString(fmt);
    }

    private PitchingPercentages generatePitchingPercentages(int primaryPosition) {
        int oSwingPct = getRandomNormalizedPercentage(PITCHER_O_SWING_PCT_STD_DEV, PITCHER_O_SWING_PCT_MEAN, PITCHER_O_SWING_PCT_MIN, PITCHER_O_SWING_PCT_MAX);

        int zSwingPct = getRandomNormalizedPercentage(PITCHER_Z_SWING_PCT_STD_DEV, PITCHER_Z_SWING_PCT_MEAN, PITCHER_Z_SWING_PCT_MIN, PITCHER_Z_SWING_PCT_MAX);

        int oContactPct = getRandomNormalizedPercentage(PITCHER_O_CONTACT_PCT_STD_DEV, PITCHER_O_CONTACT_PCT_MEAN, PITCHER_O_CONTACT_PCT_MIN, PITCHER_O_CONTACT_PCT_MAX);

        int zContactPct = getRandomNormalizedPercentage(PITCHER_Z_CONTACT_PCT_STD_DEV, PITCHER_Z_CONTACT_PCT_MEAN, PITCHER_Z_CONTACT_PCT_MIN, PITCHER_Z_CONTACT_PCT_MAX);

        int groundBallPct = getRandomNormalizedPercentage(PITCHER_GROUND_BALL_PCT_STD_DEV, PITCHER_GROUND_BALL_PCT_MEAN, PITCHER_GROUND_BALL_PCT_MIN, PITCHER_GROUND_BALL_PCT_MAX);

        int lineDrivePct = getRandomNormalizedPercentage(PITCHER_LINE_DRIVE_PCT_STD_DEV, PITCHER_LINE_DRIVE_PCT_MEAN, PITCHER_LINE_DRIVE_PCT_MIN, PITCHER_LINE_DRIVE_PCT_MAX);

        int homeRunPct = getRandomNormalizedPercentage(PITCHER_HOME_RUN_PCT_STD_DEV, PITCHER_HOME_RUN_PCT_MEAN, PITCHER_HOME_RUN_PCT_MIN, PITCHER_HOME_RUN_PCT_MAX);

        int infieldFlyBallPct = getRandomNormalizedPercentage(PITCHER_INFIELD_FLY_BALL_STD_DEV, PITCHER_INFIELD_FLY_BALL_PCT_MEAN, PITCHER_INFIELD_FLY_BALL_PCT_MIN, PITCHER_INFIELD_FLY_BALL_PCT_MAX);

        int hitByPitchPct = getRandomNormalizedPercentage(PITCHER_HIT_BY_PITCH_STD_DEV, PITCHER_HIT_BY_PITCH_PCT_MEAN, PITCHER_HIT_BY_PITCH_PCT_MIN, PITCHER_HIT_BY_PITCH_PCT_MAX);

        int wildPitchPct = getRandomNormalizedPercentage(PITCHER_WILD_PITCH_STD_DEV, PITCHER_WILD_PITCH_PCT_MEAN, PITCHER_WILD_PITCH_PCT_MIN, PITCHER_WILD_PITCH_PCT_MAX);

        int balkPct = getRandomNormalizedPercentage(PITCHER_BALK_STD_DEV, PITCHER_BALK_PCT_MEAN, PITCHER_BALK_PCT_MIN, PITCHER_BALK_PCT_MAX);

        int zonePct = getRandomNormalizedPercentage(PITCHER_ZONE_STD_DEV, PITCHER_ZONE_PCT_MEAN, PITCHER_ZONE_PCT_MIN, PITCHER_ZONE_PCT_MAX);

        int firstStrikePct = getRandomNormalizedPercentage(PITCHER_FIRST_STRIKE_STD_DEV, PITCHER_FIRST_STRIKE_PCT_MEAN, PITCHER_FIRST_STRIKE_PCT_MIN, PITCHER_FIRST_STRIKE_PCT_MAX);

        List<Integer> pitchTypes = new ArrayList<>();
        int numberOfPitchTypes = MIN_PITCH_TYPES;
        int stamina = 0;
        if (primaryPosition == STARTING_PITCHER) {
            numberOfPitchTypes = getRandomNormalizedPercentage(STARTER_NUM_PITCH_TYPES_STD_DEV, STARTER_NUM_PITCH_TYPES_MEAN, STARTER_NUM_PITCH_TYPES_MIN, STARTER_NUM_PITCH_TYPES_MAX) / 100;
            stamina = getRandomNormalizedPercentage(STARTER_STAMINA_STD_DEV, STARTER_STAMINA_MEAN, STARTER_STAMINA_MIN, STARTER_STAMINA_MAX);
        } else if (primaryPosition == LONG_RELIEVER) {
            numberOfPitchTypes = getRandomNormalizedPercentage(RELIEVER_NUM_PITCH_TYPES_STD_DEV, RELIEVER_NUM_PITCH_TYPES_MEAN, RELIEVER_NUM_PITCH_TYPES_MIN, RELIEVER_NUM_PITCH_TYPES_MAX) / 100;
            stamina = getRandomNormalizedPercentage(LONG_RELIEVER_STAMINA_STD_DEV, LONG_RELIEVER_STAMINA_MEAN, LONG_RELIEVER_STAMINA_MIN, LONG_RELIEVER_STAMINA_MAX);
        } else if (primaryPosition == SHORT_RELEIVER) {
            numberOfPitchTypes = getRandomNormalizedPercentage(RELIEVER_NUM_PITCH_TYPES_STD_DEV, RELIEVER_NUM_PITCH_TYPES_MEAN, RELIEVER_NUM_PITCH_TYPES_MIN, RELIEVER_NUM_PITCH_TYPES_MAX) / 100;
            stamina = getRandomNormalizedPercentage(SHORT_RELIEVER_STAMINA_STD_DEV, SHORT_RELIEVER_STAMINA_MEAN, SHORT_RELIEVER_STAMINA_MIN, SHORT_RELIEVER_STAMINA_MAX);
        }
        pitchTypes = generatePitchTypesThrown(pitchTypes, numberOfPitchTypes);
        int maxPercentLeft = ONE_HUNDRED_PERCENT - FASTBALL_THROWN_MIN;
        int sliderPct = 0;
        if (pitchTypes.contains(SLIDER)) {
            sliderPct = getRandomNormalizedPercentage(SLIDER_THROWN_STD_DEV, SLIDER_THROWN_MEAN, SLIDER_THROWN_MIN, SLIDER_THROWN_MAX);
            if (sliderPct > maxPercentLeft) {
                sliderPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= sliderPct;
        int cutterPct = 0;
        if (pitchTypes.contains(CUTTER)) {
            cutterPct = getRandomNormalizedPercentage(CUTTER_THROWN_STD_DEV, CUTTER_THROWN_MEAN, CUTTER_THROWN_MIN, CUTTER_THROWN_MAX);
            if (cutterPct > maxPercentLeft) {
                cutterPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= cutterPct;
        int curveballPct = 0;
        if (pitchTypes.contains(CURVEBALL)) {
            curveballPct = getRandomNormalizedPercentage(CURVE_THROWN_STD_DEV, CURVE_THROWN_MEAN, CURVE_THROWN_MIN, CURVE_THROWN_MAX);
            if (curveballPct > maxPercentLeft) {
                curveballPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= curveballPct;
        int changeUpPct = 0;
        if (pitchTypes.contains(CHANGE_UP)) {
            changeUpPct = getRandomNormalizedPercentage(CHANGE_THROWN_STD_DEV, CHANGE_THROWN_MEAN, CHANGE_THROWN_MIN, CHANGE_THROWN_MAX);
            if (changeUpPct > maxPercentLeft) {
                changeUpPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= changeUpPct;
        int splitFingerPct = 0;
        if (pitchTypes.contains(SPLIT_FINGER)) {
            splitFingerPct = getRandomNormalizedPercentage(SPLITTER_THROWN_STD_DEV, SPLITTER_THROWN_MEAN, SPLITTER_THROWN_MIN, SPLITTER_THROWN_MAX);
            if (splitFingerPct > maxPercentLeft) {
                splitFingerPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= splitFingerPct;
        int knuckleballPct = 0;
        if (pitchTypes.contains(KNUCKLEBALL)) {
            knuckleballPct = getRandomNormalizedPercentage(KNUCKLER_THROWN_STD_DEV, KNUCKLER_THROWN_MEAN, KNUCKLER_THROWN_MIN, KNUCKLER_THROWN_MAX);
            if (knuckleballPct > maxPercentLeft) {
                knuckleballPct = maxPercentLeft;
            }
        }

        int fastballPct = maxPercentLeft + FASTBALL_THROWN_MIN;


        return new PitchingPercentages(oSwingPct, zSwingPct, oContactPct, zContactPct, groundBallPct, lineDrivePct, homeRunPct, infieldFlyBallPct, hitByPitchPct, wildPitchPct, balkPct, zonePct,
                firstStrikePct, fastballPct, sliderPct, cutterPct, curveballPct, changeUpPct, splitFingerPct, knuckleballPct, stamina, 0);
    }


    private List<Integer> generatePitchTypesThrown(List<Integer> pitchTypes, int numberOfPitchTypes) {
        // Every pitcher can throw a fastball so add that to pitchTypes and reduce numberOfPitchTypes by 1
        pitchTypes.add(FASTBALL);
        numberOfPitchTypes--;
        if (numberOfPitchTypes == 6) {
            Collections.addAll(pitchTypes, CHANGE_UP, SLIDER, CURVEBALL, CUTTER, SPLIT_FINGER, KNUCKLEBALL);
            return pitchTypes;
        }
        while (numberOfPitchTypes > 0) {
            if (!pitchTypes.contains(CHANGE_UP) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_CHANGE_PCT) {
                pitchTypes.add(CHANGE_UP);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(SLIDER) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_SLIDER_PCT) {
                pitchTypes.add(SLIDER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(CURVEBALL) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_CURVE_PCT) {
                pitchTypes.add(CURVEBALL);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(CUTTER) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_CUTTER_PCT) {
                pitchTypes.add(CUTTER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(SPLIT_FINGER) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_SPLITTER_PCT) {
                pitchTypes.add(SPLIT_FINGER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(KNUCKLEBALL) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_KNUCKLER_PCT) {
                pitchTypes.add(KNUCKLEBALL);
                numberOfPitchTypes--;
            }
        }
        return pitchTypes;
    }


    private int getRandomNormalizedPercentage(int stdDev, int mean, int min, int max) {
        int normalizedPercentage = (int) (random.nextGaussian() * stdDev) + mean;
        return checkBounds(normalizedPercentage, min, max);
    }

    // If percentToCheck is below min set it to min, if it is above max set it to max, otherwise return the percentToCheck
    private int checkBounds(int percentToCheck, int min, int max) {
        if (percentToCheck < min) return min;
        if (percentToCheck > max) return max;
        return percentToCheck;
    }

    private HittingPercentages generateHittingPercentages() {
        int oSwingPct = getRandomNormalizedPercentage(PITCHER_BATTING_O_SWING_PCT_STD_DEV, PITCHER_BATTING_O_SWING_PCT_MEAN, PITCHER_BATTING_O_SWING_PCT_MIN, PITCHER_BATTING_O_SWING_PCT_MAX);

        int zSwingPct = (int) (random.nextGaussian() * PITCHER_BATTING_Z_SWING_PCT_STD_DEV) + PITCHER_BATTING_Z_SWING_PCT_MEAN;
        zSwingPct = checkBounds(zSwingPct, PITCHER_BATTING_Z_SWING_PCT_MIN, PITCHER_BATTING_Z_SWING_PCT_MAX);

        int oContactPct = (int) (random.nextGaussian() * PITCHER_BATTING_O_CONTACT_PCT_STD_DEV) + PITCHER_BATTING_O_CONTACT_PCT_MEAN;
        oContactPct = checkBounds(oContactPct, PITCHER_BATTING_O_CONTACT_PCT_MIN, PITCHER_BATTING_O_CONTACT_PCT_MAX);

        int zContactPct = (int) (random.nextGaussian() * PITCHER_BATTING_Z_CONTACT_PCT_STD_DEV) + PITCHER_BATTING_Z_CONTACT_PCT_MEAN;
        zContactPct = checkBounds(zContactPct, PITCHER_BATTING_Z_CONTACT_PCT_MIN, PITCHER_BATTING_Z_CONTACT_PCT_MAX);

        int groundBallPct = (int) (random.nextGaussian() * PITCHER_BATTING_GROUND_BALL_PCT_STD_DEV) + PITCHER_BATTING_GROUND_BALL_PCT_MEAN;
        groundBallPct = checkBounds(groundBallPct, PITCHER_BATTING_GROUND_BALL_PCT_MIN, PITCHER_BATTING_GROUND_BALL_PCT_MAX);

        int lineDrivePct = (int) (random.nextGaussian() * PITCHER_BATTING_LINE_DRIVE_PCT_STD_DEV) + PITCHER_BATTING_LINE_DRIVE_PCT_MEAN;
        lineDrivePct = checkBounds(lineDrivePct, PITCHER_BATTING_LINE_DRIVE_PCT_MIN, PITCHER_BATTING_LINE_DRIVE_PCT_MAX);

        int hardPct = getRandomNormalizedPercentage(PITCHER_BATTING_HARD_HIT_PCT_STD_DEV, PITCHER_BATTING_HARD_HIT_PCT_MEAN, PITCHER_BATTING_HARD_HIT_PCT_MIN, PITCHER_BATTING_HARD_HIT_PCT_MAX);

        int flyBallPct = ONE_HUNDRED_PERCENT - groundBallPct - lineDrivePct;

        int medPctMax = PITCHER_BATTING_MED_HIT_PCT_MAX;
        if ((ONE_HUNDRED_PERCENT - hardPct - 1040) < medPctMax) {
            medPctMax = ONE_HUNDRED_PERCENT - hardPct - 1040;
        }
        int medPct = getRandomNormalizedPercentage(PITCHER_BATTING_MED_HIT_PCT_STD_DEV, PITCHER_BATTING_MED_HIT_PCT_MEAN, PITCHER_BATTING_MED_HIT_PCT_MIN, medPctMax);

        int homeRunPct = (((((flyBallPct) * hardPct) / ONE_HUNDRED_PERCENT) * 3) + (lineDrivePct * hardPct)/ONE_HUNDRED_PERCENT)/4 - 250;
        homeRunPct = checkBounds(homeRunPct, PITCHER_BATTING_HOME_RUN_PCT_MIN, PITCHER_BATTING_HOME_RUN_PCT_MAX);

        int speed = getRandomNormalizedPercentage(PITCHER_BATTING_SPEED_STD_DEV, PITCHER_BATTING_SPEED_MEAN, PITCHER_BATTING_SPEED_MIN, PITCHER_BATTING_SPEED_MAX);

        int baseRunning = getRandomNormalizedPercentage(PITCHER_BATTING_BASERUNNING_STD_DEV, PITCHER_BATTING_BASERUNNING_MEAN, PITCHER_BATTING_BASERUNNING_MIN, PITCHER_BATTING_BASERUNNING_MAX);

        int triplePct = (int) ((speed - PITCHER_BATTING_SPEED_MEAN) / 2.5 + PITCHER_BATTING_TRIPLE_PCT_MEAN);
        triplePct = checkBounds(triplePct, PITCHER_BATTING_TRIPLE_PCT_MIN, PITCHER_BATTING_TRIPLE_PCT_MAX);

        int doublePct = (int) (random.nextGaussian() * PITCHER_BATTING_DOUBLE_STD_DEV) + PITCHER_BATTING_DOUBLE_PCT_MEAN;
        doublePct = checkBounds(doublePct, PITCHER_BATTING_DOUBLE_PCT_MIN, PITCHER_BATTING_DOUBLE_PCT_MAX);

        int stolenBasePct = PITCHER_BATTING_STOLEN_BASE_PCT_MEAN + ((speed - PITCHER_BATTING_SPEED_MEAN + baseRunning - PITCHER_BATTING_BASERUNNING_MEAN));
        stolenBasePct = checkBounds(stolenBasePct, PITCHER_BATTING_STOLEN_BASE_PCT_MIN, PITCHER_BATTING_STOLEN_BASE_PCT_MAX);

        int infieldFlyBallPct = (int) (random.nextGaussian() * PITCHER_BATTING_INFIELD_FLY_BALL_STD_DEV) + PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MEAN;
        infieldFlyBallPct = checkBounds(infieldFlyBallPct, PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MIN, PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MAX);

        int hitByPitchPct = (int) (random.nextGaussian() * PITCHER_BATTING_HIT_BY_PITCH_STD_DEV) + PITCHER_BATTING_HIT_BY_PITCH_PCT_MEAN;
        hitByPitchPct = checkBounds(hitByPitchPct, PITCHER_BATTING_HIT_BY_PITCH_PCT_MIN, PITCHER_BATTING_HIT_BY_PITCH_PCT_MAX);

        int pullPct = (int) (random.nextGaussian() * PITCHER_BATTING_PULL_STD_DEV) + PITCHER_BATTING_PULL_PCT_MEAN;
        pullPct = checkBounds(pullPct, PITCHER_BATTING_PULL_PCT_MIN, PITCHER_BATTING_PULL_PCT_MAX);

        int centerPct = (int) (random.nextGaussian() * PITCHER_BATTING_CENTER_STD_DEV) + PITCHER_BATTING_CENTER_PCT_MEAN;
        centerPct = checkBounds(centerPct, PITCHER_BATTING_CENTER_PCT_MIN, PITCHER_BATTING_CENTER_PCT_MAX);

        int battingAverageBallsInPlay = ((hardPct * lineDrivePct) / ONE_HUNDRED_PERCENT) * BATTING_HARD_LINE_DRIVE_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((medPct * lineDrivePct) / ONE_HUNDRED_PERCENT) * BATTING_MED_LINE_DRIVE_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((ONE_HUNDRED_PERCENT - hardPct - medPct) * lineDrivePct) / ONE_HUNDRED_PERCENT * BATTING_SOFT_LINE_DRIVE_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((hardPct * groundBallPct) / ONE_HUNDRED_PERCENT) * BATTING_HARD_GROUNDBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((medPct * groundBallPct) / ONE_HUNDRED_PERCENT) * BATTING_MED_GROUNDBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((ONE_HUNDRED_PERCENT - hardPct - medPct) * groundBallPct) / ONE_HUNDRED_PERCENT * BATTING_SOFT_GROUNDBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((hardPct * (ONE_HUNDRED_PERCENT - lineDrivePct - groundBallPct)) / ONE_HUNDRED_PERCENT) * BATTING_HARD_FLYBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((medPct * (ONE_HUNDRED_PERCENT - lineDrivePct - groundBallPct)) / ONE_HUNDRED_PERCENT) * BATTING_MED_FLYBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((ONE_HUNDRED_PERCENT - hardPct - medPct) * (ONE_HUNDRED_PERCENT - lineDrivePct - groundBallPct)) / ONE_HUNDRED_PERCENT * BATTING_SOFT_FLYBALL_HIT_PCT / ONE_HUNDRED_PERCENT
                - 300;
        battingAverageBallsInPlay = checkBounds(battingAverageBallsInPlay, PITCHER_BATTING_BABIP_PCT_MIN, PITCHER_BATTING_BABIP_PCT_MAX);

        int foulBallPct = (int) (random.nextGaussian() * PITCHER_BATTING_FOUL_BALL_STD_DEV) + PITCHER_BATTING_FOUL_BALL_PCT_MEAN;
        foulBallPct = checkBounds(foulBallPct, PITCHER_BATTING_FOUL_BALL_PCT_MIN, PITCHER_BATTING_FOUL_BALL_PCT_MAX);

        int stolenBaseRate = (int) (random.nextGaussian() * PITCHER_BATTING_STOLEN_BASE_RATE_STD_DEV) + PITCHER_BATTING_STOLEN_BASE_RATE_MEAN;
        stolenBaseRate = checkBounds(stolenBaseRate, PITCHER_BATTING_STOLEN_BASE_RATE_MIN, PITCHER_BATTING_STOLEN_BASE_RATE_MAX);

        int errorPct = (int) (random.nextGaussian() * PITCHER_BATTING_ERROR_PCT_STD_DEV) + PITCHER_BATTING_ERROR_PCT_MEAN;
        errorPct = checkBounds(errorPct, PITCHER_BATTING_ERROR_PCT_MIN, PITCHER_BATTING_ERROR_PCT_MAX);

        int stamina = (int) (random.nextGaussian() * PITCHER_BATTING_STAMINA_STD_DEV) + PITCHER_BATTING_STAMINA_MEAN;
        stamina = checkBounds(stamina, PITCHER_BATTING_STAMINA_MIN, PITCHER_BATTING_STAMINA_MAX);


        return new HittingPercentages(oSwingPct, zSwingPct, oContactPct, zContactPct, speed, lineDrivePct, groundBallPct, hardPct, medPct, pullPct, centerPct, homeRunPct, triplePct, doublePct,
                stolenBasePct, infieldFlyBallPct, hitByPitchPct, battingAverageBallsInPlay, foulBallPct, stolenBaseRate, baseRunning, errorPct, stamina, 0);
    }

    private List<PitchingStats> generatePitchingStats(String playerId) {
        List<PitchingStats> newPitchingStats = new ArrayList<>();
        newPitchingStats.add(0, new PitchingStats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, playerId));
        return newPitchingStats;
    }

    private List<BattingStats> generateBattingStats(String playerId) {
        List<BattingStats> newBattingStats = new ArrayList<>();
        newBattingStats.add(0, new BattingStats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, playerId));
        return newBattingStats;
    }

}
