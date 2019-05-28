package com.example.android.baseballbythenumbers.TeamGenerator;

import android.content.Context;

import com.example.android.baseballbythenumbers.Data.HittingPercentages;
import com.example.android.baseballbythenumbers.Data.PitchingPercentages;
import com.example.android.baseballbythenumbers.Data.Player;
import com.example.android.baseballbythenumbers.NameGenerator.Name;
import com.example.android.baseballbythenumbers.NameGenerator.NameGenerator;
import com.example.android.baseballbythenumbers.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_BABIP_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_BABIP_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_BABIP_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_BABIP_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_BASERUNNING_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_BASERUNNING_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_BASERUNNING_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_BASERUNNING_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_CENTER_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_CENTER_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_CENTER_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_CENTER_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_DOUBLE_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_DOUBLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_DOUBLE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_DOUBLE_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_ERROR_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_ERROR_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_ERROR_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_ERROR_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_FOUL_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_FOUL_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_FOUL_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_FOUL_BALL_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_GROUND_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_GROUND_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_GROUND_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_GROUND_BALL_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HIT_BY_PITCH_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HIT_BY_PITCH_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HIT_BY_PITCH_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HIT_BY_PITCH_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_INFIELD_FLY_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_INFIELD_FLY_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_INFIELD_FLY_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_INFIELD_FLY_BALL_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_LINE_DRIVE_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_LINE_DRIVE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_LINE_DRIVE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_LINE_DRIVE_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_O_SWING_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_PULL_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_PULL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_PULL_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_PULL_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_SPEED_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_SPEED_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_SPEED_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_SPEED_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STAMINA_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STAMINA_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STAMINA_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STAMINA_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STOLEN_BASE_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STOLEN_BASE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STOLEN_BASE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STOLEN_BASE_RATE_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STOLEN_BASE_RATE_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STOLEN_BASE_RATE_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STOLEN_BASE_RATE_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_STOLEN_BASE_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_TRIPLE_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_TRIPLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_TRIPLE_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_TRIPLE_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MAX;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.Data.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.Data.PitchTypes.CHANGE_UP;
import static com.example.android.baseballbythenumbers.Data.PitchTypes.CURVEBALL;
import static com.example.android.baseballbythenumbers.Data.PitchTypes.CUTTER;
import static com.example.android.baseballbythenumbers.Data.PitchTypes.FASTBALL;
import static com.example.android.baseballbythenumbers.Data.PitchTypes.KNUCKLEBALL;
import static com.example.android.baseballbythenumbers.Data.PitchTypes.SLIDER;
import static com.example.android.baseballbythenumbers.Data.PitchTypes.SPLIT_FINGER;
import static com.example.android.baseballbythenumbers.Data.Positions.CATCHER;
import static com.example.android.baseballbythenumbers.Data.Positions.CENTER_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.DESIGNATED_HITTER;
import static com.example.android.baseballbythenumbers.Data.Positions.FIRST_BASE;
import static com.example.android.baseballbythenumbers.Data.Positions.LEFT_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.RIGHT_FIELD;
import static com.example.android.baseballbythenumbers.Data.Positions.SECOND_BASE;
import static com.example.android.baseballbythenumbers.Data.Positions.SHORTSTOP;
import static com.example.android.baseballbythenumbers.Data.Positions.THIRD_BASE;

public class HitterGenerator {

    public static final double HITTER_AGE_STD_DEV = 3.77;
    public static final double HITTER_AGE_MEAN = 28.06;
    public static final int HITTER_AGE_MIN = 19;
    public static final int HITTER_AGE_MAX = 44;

    private static final int ONE_HUNDRED_PERCENT = 10000;

    private int catchers;

    private int firstBasemen;

    private int secondBasemen;

    private int thirdBasemen;

    private int shortstops;

    private int leftField;

    private int centerField;

    private int rightField;

    private int utilityInfield;

    private int utilityOutfield;

    private int utility;

    private int designatedHitter;

    private Context context;

    private NameGenerator nameGenerator;

    private final Random random;

    public HitterGenerator(Context context, NameGenerator nameGenerator, int catchers, int firstBasemen, int secondBasemen, int thirdBasemen, int shortstops, int leftField, int centerField, int rightField, int utilityInfield, int utilityOutfield, int utility, int designatedHitter) {
        this.context = context;
        this.nameGenerator = nameGenerator;
        this.catchers = catchers;
        this.firstBasemen = firstBasemen;
        this.secondBasemen = secondBasemen;
        this.thirdBasemen = thirdBasemen;
        this.shortstops = shortstops;
        this.leftField = leftField;
        this.centerField = centerField;
        this.rightField = rightField;
        this.utilityInfield = utilityInfield;
        this.utilityOutfield = utilityOutfield;
        this.utility = utility;
        this.designatedHitter = designatedHitter;
        this.random = new Random();
    }

    public List<Player> generateHitters() {
        List<Player> hitters = new ArrayList<>();

        for (int i = 0; i < catchers; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), CATCHER, FIRST_BASE + LEFT_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < firstBasemen; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), FIRST_BASE,  LEFT_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < secondBasemen; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SECOND_BASE,  FIRST_BASE + SHORTSTOP + THIRD_BASE + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < thirdBasemen; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), THIRD_BASE,  SECOND_BASE + FIRST_BASE + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < shortstops; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SHORTSTOP,  THIRD_BASE + SECOND_BASE + FIRST_BASE + CENTER_FIELD + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < leftField; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), LEFT_FIELD,  RIGHT_FIELD + FIRST_BASE + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < centerField; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), CENTER_FIELD,  LEFT_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < rightField; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), RIGHT_FIELD,  LEFT_FIELD + FIRST_BASE + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < utilityInfield; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SECOND_BASE,  FIRST_BASE + THIRD_BASE + SHORTSTOP + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < utilityOutfield; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), CENTER_FIELD,  LEFT_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < utility; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SHORTSTOP,  CATCHER + FIRST_BASE + SECOND_BASE + THIRD_BASE + LEFT_FIELD + CENTER_FIELD+ RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        for (int i = 0; i < designatedHitter; i++) {
            Name newName = nameGenerator.generateName();
            double trueAge = ((random.nextGaussian() * HITTER_AGE_STD_DEV) + HITTER_AGE_MEAN);
            int age = checkBounds((int) trueAge, HITTER_AGE_MIN, HITTER_AGE_MAX);
            if (trueAge < HITTER_AGE_MIN) {
                trueAge = HITTER_AGE_MIN + (trueAge - (int) trueAge);
            } else if (trueAge > HITTER_AGE_MAX) {
                trueAge = HITTER_AGE_MAX + (trueAge - (int) trueAge);
            }

            String birthdate = getFormattedBirthdate(trueAge);
            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), DESIGNATED_HITTER,  FIRST_BASE + LEFT_FIELD,
                    age, birthdate, "", "", generateHittingPercentages(), generatePitchingPercentages());
            hitters.add(newHitter);
        }

        return hitters;
    }

    private String getFormattedBirthdate(double trueAge) {
        DateTime currentDate = DateTime.now();
        DateTime birthdate = currentDate.minusYears((int) trueAge).minusDays((int) ((trueAge - (int) trueAge) * context.getResources().getInteger(R.integer.daysInYear)));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM d, yyyy");
        return new LocalDate(birthdate).toString(fmt);
    }

    // If percentToCheck is below min set it to min, if it is above max set it to max, otherwise return the percentToCheck
    private int checkBounds(int percentToCheck, int min, int max) {
        if (percentToCheck < min) return min;
        if (percentToCheck > max) return max;
        return percentToCheck;
    }

    private HittingPercentages generateHittingPercentages() {
        int oSwingPct = (int) (random.nextGaussian() * BATTING_O_SWING_PCT_STD_DEV) + BATTING_O_SWING_PCT_MEAN;
        oSwingPct = checkBounds(oSwingPct, BATTING_O_SWING_PCT_MIN, BATTING_O_SWING_PCT_MAX);

        int zSwingPct = (int) (random.nextGaussian() * BATTING_Z_SWING_PCT_STD_DEV) + BATTING_Z_SWING_PCT_MEAN;
        zSwingPct = checkBounds(zSwingPct, BATTING_Z_SWING_PCT_MIN, BATTING_Z_SWING_PCT_MAX);

        int oContactPct = (int) (random.nextGaussian() * BATTING_O_CONTACT_PCT_STD_DEV) + BATTING_O_CONTACT_PCT_MEAN;
        oContactPct = checkBounds(oContactPct, BATTING_O_CONTACT_PCT_MIN, BATTING_O_CONTACT_PCT_MAX);

        int zContactPct = (int) (random.nextGaussian() * BATTING_Z_CONTACT_PCT_STD_DEV) + BATTING_Z_CONTACT_PCT_MEAN;
        zContactPct = checkBounds(zContactPct, BATTING_Z_CONTACT_PCT_MIN, BATTING_Z_CONTACT_PCT_MAX);

        int groundBallPct = (int) (random.nextGaussian() * BATTING_GROUND_BALL_PCT_STD_DEV) + BATTING_GROUND_BALL_PCT_MEAN;
        groundBallPct = checkBounds(groundBallPct, BATTING_GROUND_BALL_PCT_MIN, BATTING_GROUND_BALL_PCT_MAX);

        int lineDrivePct = (int) (random.nextGaussian() * BATTING_LINE_DRIVE_PCT_STD_DEV) + BATTING_LINE_DRIVE_PCT_MEAN;
        lineDrivePct = checkBounds(lineDrivePct, BATTING_LINE_DRIVE_PCT_MIN, BATTING_LINE_DRIVE_PCT_MAX);

        int homeRunPct = (int) (random.nextGaussian() * BATTING_HOME_RUN_PCT_STD_DEV) + BATTING_HOME_RUN_PCT_MEAN;
        homeRunPct = checkBounds(homeRunPct, BATTING_HOME_RUN_PCT_MIN, BATTING_HOME_RUN_PCT_MAX);

        int triplePct = (int) (random.nextGaussian() * BATTING_TRIPLE_STD_DEV) + BATTING_TRIPLE_PCT_MEAN;
        triplePct = checkBounds(triplePct, BATTING_TRIPLE_PCT_MIN, BATTING_TRIPLE_PCT_MAX);

        int doublePct = (int) (random.nextGaussian() * BATTING_DOUBLE_STD_DEV) + BATTING_DOUBLE_PCT_MEAN;
        doublePct = checkBounds(doublePct, BATTING_DOUBLE_PCT_MIN, BATTING_DOUBLE_PCT_MAX);

        int stolenBasePct = (int) (random.nextGaussian() * BATTING_STOLEN_BASE_STD_DEV) + BATTING_STOLEN_BASE_PCT_MEAN;
        stolenBasePct = checkBounds(stolenBasePct, BATTING_STOLEN_BASE_PCT_MIN, BATTING_STOLEN_BASE_PCT_MAX);

        int infieldFlyBallPct = (int) (random.nextGaussian() * BATTING_INFIELD_FLY_BALL_STD_DEV) + BATTING_INFIELD_FLY_BALL_PCT_MEAN;
        infieldFlyBallPct = checkBounds(infieldFlyBallPct, BATTING_INFIELD_FLY_BALL_PCT_MIN, BATTING_INFIELD_FLY_BALL_PCT_MAX);

        int hitByPitchPct = (int) (random.nextGaussian() * BATTING_HIT_BY_PITCH_STD_DEV) + BATTING_HIT_BY_PITCH_PCT_MEAN;
        hitByPitchPct = checkBounds(hitByPitchPct, BATTING_HIT_BY_PITCH_PCT_MIN, BATTING_HIT_BY_PITCH_PCT_MAX);

        int battingAverageBallsInPlay = (int) (random.nextGaussian() * BATTING_BABIP_STD_DEV) + BATTING_BABIP_PCT_MEAN;
        battingAverageBallsInPlay = checkBounds(battingAverageBallsInPlay, BATTING_BABIP_PCT_MIN, BATTING_BABIP_PCT_MAX);

        int foulBallPct = (int) (random.nextGaussian() * BATTING_FOUL_BALL_STD_DEV) + BATTING_FOUL_BALL_PCT_MEAN;
        foulBallPct = checkBounds(foulBallPct, BATTING_FOUL_BALL_PCT_MIN, BATTING_FOUL_BALL_PCT_MAX);

        int pullPct = (int) (random.nextGaussian() * BATTING_PULL_STD_DEV) + BATTING_PULL_PCT_MEAN;
        pullPct = checkBounds(pullPct, BATTING_PULL_PCT_MIN, BATTING_PULL_PCT_MAX);

        int centerPct = (int) (random.nextGaussian() * BATTING_CENTER_STD_DEV) + BATTING_CENTER_PCT_MEAN;
        centerPct = checkBounds(centerPct, BATTING_CENTER_PCT_MIN, BATTING_CENTER_PCT_MAX);

        int stolenBaseRate = (int) (random.nextGaussian() * BATTING_STOLEN_BASE_RATE_STD_DEV) + BATTING_STOLEN_BASE_RATE_MEAN;
        stolenBaseRate = checkBounds(stolenBaseRate, BATTING_STOLEN_BASE_RATE_MIN, BATTING_STOLEN_BASE_RATE_MAX);

        int baseRunning = (int) (random.nextGaussian() * BATTING_BASERUNNING_STD_DEV) + BATTING_BASERUNNING_MEAN;
        baseRunning = checkBounds(baseRunning, BATTING_BASERUNNING_MIN, BATTING_BASERUNNING_MAX);

        int speed = (int) (random.nextGaussian() * BATTING_SPEED_STD_DEV) + BATTING_SPEED_MEAN;
        speed = checkBounds(speed, BATTING_SPEED_MIN, BATTING_SPEED_MAX);

        int errorPct = (int) (random.nextGaussian() * BATTING_ERROR_PCT_STD_DEV) + BATTING_ERROR_PCT_MEAN;
        errorPct = checkBounds(errorPct, BATTING_ERROR_PCT_MIN, BATTING_ERROR_PCT_MAX);

        int stamina = (int) (random.nextGaussian() * BATTING_STAMINA_STD_DEV) + BATTING_STAMINA_MEAN;
        stamina = checkBounds(stamina, BATTING_STAMINA_MIN, BATTING_STAMINA_MAX);


        return new HittingPercentages(oSwingPct, zSwingPct, oContactPct, zContactPct, groundBallPct, lineDrivePct, homeRunPct, triplePct, doublePct,
                stolenBasePct, infieldFlyBallPct, hitByPitchPct, battingAverageBallsInPlay, foulBallPct, pullPct, centerPct,
                stolenBaseRate, baseRunning, speed, errorPct, stamina);
    }

    private PitchingPercentages generatePitchingPercentages() {
        int oSwingPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherOSwingPctStdDev)) + context.getResources().getInteger(R.integer.pitcherOSwingPctMean);
        oSwingPct = checkBounds(oSwingPct, context.getResources().getInteger(R.integer.pitcherOSwingPctMin), context.getResources().getInteger(R.integer.pitcherOSwingPctMax));

        int zSwingPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherZSwingPctStdDev)) + context.getResources().getInteger(R.integer.pitcherZSwingPctMean);
        zSwingPct = checkBounds(zSwingPct, context.getResources().getInteger(R.integer.pitcherZSwingPctMin), context.getResources().getInteger(R.integer.pitcherZSwingPctMax));

        int oContactPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherOContactPctStdDev)) + context.getResources().getInteger(R.integer.pitcherOContactPctMean);
        oContactPct = checkBounds(oContactPct, context.getResources().getInteger(R.integer.pitcherOContactPctMin), context.getResources().getInteger(R.integer.pitcherOContactPctMax));

        int zContactPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherZContactPctStdDev)) + context.getResources().getInteger(R.integer.pitcherZContactPctMean);
        zContactPct = checkBounds(zContactPct, context.getResources().getInteger(R.integer.pitcherZContactPctMin), context.getResources().getInteger(R.integer.pitcherZContactPctMax));

        int groundBallPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherGroundBallPctStdDev)) + context.getResources().getInteger(R.integer.pitcherGroundBallPctMean);
        groundBallPct = checkBounds(groundBallPct, context.getResources().getInteger(R.integer.pitcherGroundBallPctMin), context.getResources().getInteger(R.integer.pitcherGroundBallPctMax));

        int lineDrivePct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherLineDrivePctStdDev)) + context.getResources().getInteger(R.integer.pitcherLineDrivePctMean);
        lineDrivePct = checkBounds(lineDrivePct, context.getResources().getInteger(R.integer.pitcherLineDrivePctMin), context.getResources().getInteger(R.integer.pitcherLineDrivePctMax));

        int homeRunPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherHomeRunPctStdDev)) + context.getResources().getInteger(R.integer.pitcherHomeRunPctMean);
        homeRunPct = checkBounds(homeRunPct, context.getResources().getInteger(R.integer.pitcherHomeRunPctMin), context.getResources().getInteger(R.integer.pitcherHomeRunPctMax));

        int infieldFlyBallPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherInfieldFlyBallPctStdDev)) + context.getResources().getInteger(R.integer.pitcherInfieldFlyBallPctMean);
        infieldFlyBallPct = checkBounds(infieldFlyBallPct, context.getResources().getInteger(R.integer.pitcherInfieldFlyBallPctMin), context.getResources().getInteger(R.integer.pitcherInfieldFlyBallPctMax));

        int hitByPitchPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherHitByPitchPctStdDev)) + context.getResources().getInteger(R.integer.pitcherHitByPitchPctMean);
        hitByPitchPct = checkBounds(hitByPitchPct, context.getResources().getInteger(R.integer.pitcherHitByPitchPctMin), context.getResources().getInteger(R.integer.pitcherHitByPitchPctMax));

        int wildPitchPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherWildPitchPctStdDev)) + context.getResources().getInteger(R.integer.pitcherWildPitchPctMean);
        wildPitchPct = checkBounds(wildPitchPct, context.getResources().getInteger(R.integer.pitcherWildPitchPctMin), context.getResources().getInteger(R.integer.pitcherWildPitchPctMax));

        int balkPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherBalkPctStdDev)) + context.getResources().getInteger(R.integer.pitcherBalkPctMean);
        balkPct = checkBounds(balkPct, context.getResources().getInteger(R.integer.pitcherBalkPctMin), context.getResources().getInteger(R.integer.pitcherBalkPctMax));

        int zonePct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherZonePctStdDev)) + context.getResources().getInteger(R.integer.pitcherZonePctMean);
        zonePct = checkBounds(zonePct, context.getResources().getInteger(R.integer.pitcherZonePctMin), context.getResources().getInteger(R.integer.pitcherZonePctMax));

        int firstStrikePct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.pitcherFirstStrikePctStdDev)) + context.getResources().getInteger(R.integer.pitcherFirstStrikePctMean);
        firstStrikePct = checkBounds(firstStrikePct, context.getResources().getInteger(R.integer.pitcherFirstStrikePctMin), context.getResources().getInteger(R.integer.pitcherFirstStrikePctMax));

        List<Integer> pitchTypes = new ArrayList<>();
        int stamina = 0;

        int numberOfPitchTypes = (int) (random.nextGaussian() + 2);
        numberOfPitchTypes = checkBounds(numberOfPitchTypes, 1, 3);

        pitchTypes = generatePitchTypesThrown(pitchTypes, numberOfPitchTypes);

        stamina = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.shortReliefStaminaStdDev)) + context.getResources().getInteger(R.integer.shortReliefStaminaMean);
        stamina = checkBounds(stamina, context.getResources().getInteger(R.integer.shortReliefStaminaMin), context.getResources().getInteger(R.integer.shortReliefStaminaMax));

        int maxPercentLeft = ONE_HUNDRED_PERCENT - context.getResources().getInteger(R.integer.fastballThrownPctMin);
        int sliderPct = 0;
        if (pitchTypes.contains(SLIDER)) {
            sliderPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.sliderThrownPctStdDev)) + context.getResources().getInteger(R.integer.sliderThrownPctMean);
            if (context.getResources().getInteger(R.integer.sliderThrownPctMax) < maxPercentLeft) {
                sliderPct = checkBounds(sliderPct, 0, context.getResources().getInteger(R.integer.sliderThrownPctMax));
            } else {
                sliderPct = checkBounds(sliderPct, 0, maxPercentLeft);
            }
        }
        maxPercentLeft -= sliderPct;
        int cutterPct = 0;
        if (pitchTypes.contains(CUTTER)) {
            cutterPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.cutterThrownPctStdDev)) + context.getResources().getInteger(R.integer.cutterThrownPctMean);
            if (context.getResources().getInteger(R.integer.cutterThrownPctMax) < maxPercentLeft) {
                cutterPct = checkBounds(cutterPct, 0, context.getResources().getInteger(R.integer.cutterThrownPctMax));
            } else {
                cutterPct = checkBounds(cutterPct, 0, maxPercentLeft);
            }
        }
        maxPercentLeft -= cutterPct;
        int curveballPct = 0;
        if (pitchTypes.contains(CURVEBALL)) {
            curveballPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.curveThrownPctStdDev)) + context.getResources().getInteger(R.integer.curveThrownPctMean);
            if (context.getResources().getInteger(R.integer.curveThrownPctMax) < maxPercentLeft) {
                curveballPct = checkBounds(curveballPct, 0, context.getResources().getInteger(R.integer.curveThrownPctMax));
            } else {
                curveballPct = checkBounds(curveballPct, 0, maxPercentLeft);
            }
        }
        maxPercentLeft -= curveballPct;
        int changeUpPct = 0;
        if (pitchTypes.contains(CHANGE_UP)) {
            changeUpPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.changeThrownPctStdDev)) + context.getResources().getInteger(R.integer.changeThrownPctMean);
            if (context.getResources().getInteger(R.integer.changeThrownPctMax) < maxPercentLeft) {
                changeUpPct = checkBounds(changeUpPct, 0, context.getResources().getInteger(R.integer.changeThrownPctMax));
            } else {
                changeUpPct = checkBounds(changeUpPct, 0, maxPercentLeft);
            }
        }
        maxPercentLeft -= changeUpPct;
        int splitFingerPct = 0;
        if (pitchTypes.contains(SPLIT_FINGER)) {
            splitFingerPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.splitterThrownPctStdDev)) + context.getResources().getInteger(R.integer.splitterThrownPctMean);
            if (context.getResources().getInteger(R.integer.splitterThrownPctMax) < maxPercentLeft) {
                splitFingerPct = checkBounds(splitFingerPct, 0, context.getResources().getInteger(R.integer.splitterThrownPctMax));
            } else {
                splitFingerPct = checkBounds(splitFingerPct, 0, maxPercentLeft);
            }
        }
        maxPercentLeft -= splitFingerPct;
        int knuckleballPct = 0;
        if (pitchTypes.contains(KNUCKLEBALL)) {
            knuckleballPct = (int) (random.nextGaussian() * context.getResources().getInteger(R.integer.knucklerThrownPctStdDev)) + context.getResources().getInteger(R.integer.knucklerThrownPctMean);
            if (context.getResources().getInteger(R.integer.knucklerThrownPctMax) < maxPercentLeft) {
                knuckleballPct = checkBounds(knuckleballPct, 0, context.getResources().getInteger(R.integer.knucklerThrownPctMax));
            } else {
                knuckleballPct = checkBounds(knuckleballPct, 0, maxPercentLeft);
            }
        }

        int fastballPct = maxPercentLeft + context.getResources().getInteger(R.integer.fastballThrownPctMin);


        return new PitchingPercentages(oSwingPct, zSwingPct, oContactPct, zContactPct, groundBallPct, lineDrivePct, homeRunPct, infieldFlyBallPct, hitByPitchPct, wildPitchPct, balkPct, zonePct,
                firstStrikePct, fastballPct, sliderPct, cutterPct, curveballPct, changeUpPct, splitFingerPct, knuckleballPct, stamina);
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
            if (!pitchTypes.contains(CHANGE_UP) && random.nextInt(ONE_HUNDRED_PERCENT) < context.getResources().getInteger(R.integer.pitchersThatThrowChangePct)) {
                pitchTypes.add(CHANGE_UP);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(SLIDER) && random.nextInt(ONE_HUNDRED_PERCENT) < context.getResources().getInteger(R.integer.pitchersThatThrowSliderPct)) {
                pitchTypes.add(SLIDER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(CURVEBALL) && random.nextInt(ONE_HUNDRED_PERCENT) < context.getResources().getInteger(R.integer.pitchersThatThrowCurvePct)) {
                pitchTypes.add(CURVEBALL);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(CUTTER) && random.nextInt(ONE_HUNDRED_PERCENT) < context.getResources().getInteger(R.integer.pitchersThatThrowCutterPct)) {
                pitchTypes.add(CUTTER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(SPLIT_FINGER) && random.nextInt(ONE_HUNDRED_PERCENT) < context.getResources().getInteger(R.integer.pitchersThatThrowSplitFingerPct)) {
                pitchTypes.add(SPLIT_FINGER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes > 0 && !pitchTypes.contains(KNUCKLEBALL) && random.nextInt(ONE_HUNDRED_PERCENT) < context.getResources().getInteger(R.integer.pitchersThatThrowKnuckleballPct)) {
                pitchTypes.add(KNUCKLEBALL);
                numberOfPitchTypes--;
            }
        }
        return pitchTypes;
    }
}
