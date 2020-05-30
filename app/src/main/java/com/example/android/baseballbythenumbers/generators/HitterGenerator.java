package com.example.android.baseballbythenumbers.generators;

import android.content.Context;
import android.widget.ProgressBar;

import com.example.android.baseballbythenumbers.data.BattingStats;
import com.example.android.baseballbythenumbers.data.HittingPercentages;
import com.example.android.baseballbythenumbers.data.Name;
import com.example.android.baseballbythenumbers.data.PitchingPercentages;
import com.example.android.baseballbythenumbers.data.PitchingStats;
import com.example.android.baseballbythenumbers.data.Player;
import com.example.android.baseballbythenumbers.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_BABIP_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_BABIP_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_BASERUNNING_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_BASERUNNING_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_BASERUNNING_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_BASERUNNING_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_CENTER_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_CENTER_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_CENTER_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_CENTER_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_DOUBLE_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_DOUBLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_DOUBLE_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_DOUBLE_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_ERROR_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_ERROR_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_ERROR_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_ERROR_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_FOUL_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_FOUL_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_FOUL_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_FOUL_BALL_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_GROUND_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_GROUND_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_GROUND_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_GROUND_BALL_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HARD_HIT_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HARD_HIT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HARD_HIT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HARD_HIT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HIT_BY_PITCH_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HIT_BY_PITCH_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HIT_BY_PITCH_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HIT_BY_PITCH_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_HOME_RUN_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_INFIELD_FLY_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_INFIELD_FLY_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_INFIELD_FLY_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_INFIELD_FLY_BALL_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_LINE_DRIVE_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_LINE_DRIVE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_LINE_DRIVE_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_LINE_DRIVE_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_MED_HIT_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_MED_HIT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_MED_HIT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_MED_HIT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_CONTACT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_O_SWING_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_PULL_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_PULL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_PULL_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_PULL_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_SPEED_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_SPEED_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_SPEED_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_SPEED_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STAMINA_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STAMINA_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STAMINA_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STAMINA_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STOLEN_BASE_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STOLEN_BASE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STOLEN_BASE_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STOLEN_BASE_RATE_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STOLEN_BASE_RATE_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STOLEN_BASE_RATE_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_STOLEN_BASE_RATE_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_TRIPLE_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_TRIPLE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_TRIPLE_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_CONTACT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.BatterBaseStats.BATTING_Z_SWING_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.Handedness.FIRST_AND_OF_HITS_SWITCH_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.Handedness.FIRST_HITS_LEFT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.Handedness.INF_HITS_LEFT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.Handedness.INF_HITS_SWITCH_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.Handedness.OF_HITS_LEFT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.Handedness.POPULATION_LEFT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_HARD_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_HARD_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_HARD_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_MED_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_MED_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_MED_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_SOFT_FLYBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_SOFT_GROUNDBALL_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.HitRates.BATTING_SOFT_LINE_DRIVE_HIT_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CHANGE_THROWN_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CHANGE_THROWN_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CHANGE_THROWN_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CHANGE_THROWN_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CURVE_THROWN_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CURVE_THROWN_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CURVE_THROWN_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CURVE_THROWN_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CUTTER_THROWN_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CUTTER_THROWN_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CUTTER_THROWN_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.CUTTER_THROWN_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.FASTBALL_THROWN_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.KNUCKLER_THROWN_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.KNUCKLER_THROWN_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.KNUCKLER_THROWN_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.KNUCKLER_THROWN_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHERS_THAT_THROW_CHANGE_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHERS_THAT_THROW_CURVE_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHERS_THAT_THROW_CUTTER_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHERS_THAT_THROW_KNUCKLER_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHERS_THAT_THROW_SLIDER_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHERS_THAT_THROW_SPLITTER_PCT;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_BALK_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_BALK_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_BALK_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_BALK_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_FIRST_STRIKE_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_FIRST_STRIKE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_FIRST_STRIKE_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_FIRST_STRIKE_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_GROUND_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_GROUND_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_GROUND_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_GROUND_BALL_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HIT_BY_PITCH_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HIT_BY_PITCH_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HIT_BY_PITCH_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HIT_BY_PITCH_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HOME_RUN_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HOME_RUN_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HOME_RUN_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_HOME_RUN_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_INFIELD_FLY_BALL_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_INFIELD_FLY_BALL_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_INFIELD_FLY_BALL_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_INFIELD_FLY_BALL_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_LINE_DRIVE_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_LINE_DRIVE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_LINE_DRIVE_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_LINE_DRIVE_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_CONTACT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_SWING_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_O_SWING_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_WILD_PITCH_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_WILD_PITCH_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_WILD_PITCH_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_WILD_PITCH_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_ZONE_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_ZONE_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_ZONE_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_ZONE_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_CONTACT_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_PCT_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_PCT_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_PCT_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.PITCHER_Z_SWING_PCT_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SHORT_RELIEVER_STAMINA_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SHORT_RELIEVER_STAMINA_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SHORT_RELIEVER_STAMINA_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SHORT_RELIEVER_STAMINA_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SLIDER_THROWN_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SLIDER_THROWN_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SLIDER_THROWN_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SLIDER_THROWN_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SPLITTER_THROWN_MAX;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SPLITTER_THROWN_MEAN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SPLITTER_THROWN_MIN;
import static com.example.android.baseballbythenumbers.constants.Constants.PitcherBaseStats.SPLITTER_THROWN_STD_DEV;
import static com.example.android.baseballbythenumbers.constants.PitchTypes.CHANGE_UP;
import static com.example.android.baseballbythenumbers.constants.PitchTypes.CURVEBALL;
import static com.example.android.baseballbythenumbers.constants.PitchTypes.CUTTER;
import static com.example.android.baseballbythenumbers.constants.PitchTypes.FASTBALL;
import static com.example.android.baseballbythenumbers.constants.PitchTypes.KNUCKLEBALL;
import static com.example.android.baseballbythenumbers.constants.PitchTypes.SLIDER;
import static com.example.android.baseballbythenumbers.constants.PitchTypes.SPLIT_FINGER;
import static com.example.android.baseballbythenumbers.constants.Positions.CATCHER;
import static com.example.android.baseballbythenumbers.constants.Positions.CENTER_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.DESIGNATED_HITTER;
import static com.example.android.baseballbythenumbers.constants.Positions.FIRST_BASE;
import static com.example.android.baseballbythenumbers.constants.Positions.LEFT_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.RIGHT_FIELD;
import static com.example.android.baseballbythenumbers.constants.Positions.SECOND_BASE;
import static com.example.android.baseballbythenumbers.constants.Positions.SHORTSTOP;
import static com.example.android.baseballbythenumbers.constants.Positions.THIRD_BASE;

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

    private String hittingSide;

    private String throwingHand;

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

    public List<Player> generateHitters(String teamId, ProgressBar progressBar) {

        String leftHanded = context.getResources().getString(R.string.left_handed);
        String rightHanded = context.getResources().getString(R.string.right_handed);
        String switchHitter = context.getResources().getString(R.string.switch_hitter);
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

            throwingHand = rightHanded;

            hittingSide = getInfielderHittingSide();

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), CATCHER, FIRST_BASE + LEFT_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            int handedness = random.nextInt(ONE_HUNDRED_PERCENT);
            if (handedness < FIRST_AND_OF_HITS_SWITCH_PCT) {
                hittingSide = switchHitter;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < POPULATION_LEFT_PCT) {
                    throwingHand = leftHanded;
                } else {
                    throwingHand = rightHanded;
                }
            } else if (handedness < (FIRST_AND_OF_HITS_SWITCH_PCT + FIRST_HITS_LEFT_PCT)) {
                hittingSide = leftHanded;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < INF_HITS_LEFT_PCT) {
                    throwingHand = rightHanded;
                } else {
                    throwingHand = leftHanded;
                }
            } else {
                hittingSide = rightHanded;
                throwingHand = rightHanded;
            }

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), FIRST_BASE,  LEFT_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            throwingHand = rightHanded;

            hittingSide = getInfielderHittingSide();

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SECOND_BASE, FIRST_BASE + SHORTSTOP + THIRD_BASE + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            throwingHand = rightHanded;

            hittingSide = getInfielderHittingSide();

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), THIRD_BASE, FIRST_BASE + SECOND_BASE + SHORTSTOP + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            throwingHand = rightHanded;

            hittingSide = getInfielderHittingSide();

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SHORTSTOP, SECOND_BASE + THIRD_BASE + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            int handedness = random.nextInt(ONE_HUNDRED_PERCENT);
            if (handedness < FIRST_AND_OF_HITS_SWITCH_PCT) {
                hittingSide = switchHitter;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < POPULATION_LEFT_PCT) {
                    throwingHand = leftHanded;
                } else {
                    throwingHand = rightHanded;
                }
            } else if (handedness < (FIRST_AND_OF_HITS_SWITCH_PCT + OF_HITS_LEFT_PCT)) {
                hittingSide = leftHanded;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < INF_HITS_LEFT_PCT) {
                    throwingHand = rightHanded;
                } else {
                    throwingHand = leftHanded;
                }
            } else {
                hittingSide = rightHanded;
                throwingHand = rightHanded;
            }

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), LEFT_FIELD, FIRST_BASE + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            int handedness = random.nextInt(ONE_HUNDRED_PERCENT);
            if (handedness < FIRST_AND_OF_HITS_SWITCH_PCT) {
                hittingSide = switchHitter;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < POPULATION_LEFT_PCT) {
                    throwingHand = leftHanded;
                } else {
                    throwingHand = rightHanded;
                }
            } else if (handedness < (FIRST_AND_OF_HITS_SWITCH_PCT + OF_HITS_LEFT_PCT)) {
                hittingSide = leftHanded;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < INF_HITS_LEFT_PCT) {
                    throwingHand = rightHanded;
                } else {
                    throwingHand = leftHanded;
                }
            } else {
                hittingSide = rightHanded;
                throwingHand = rightHanded;
            }

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), CENTER_FIELD, FIRST_BASE + LEFT_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            int handedness = random.nextInt(ONE_HUNDRED_PERCENT);
            if (handedness < FIRST_AND_OF_HITS_SWITCH_PCT) {
                hittingSide = switchHitter;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < POPULATION_LEFT_PCT) {
                    throwingHand = leftHanded;
                } else {
                    throwingHand = rightHanded;
                }
            } else if (handedness < (FIRST_AND_OF_HITS_SWITCH_PCT + OF_HITS_LEFT_PCT)) {
                hittingSide = leftHanded;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < INF_HITS_LEFT_PCT) {
                    throwingHand = rightHanded;
                } else {
                    throwingHand = leftHanded;
                }
            } else {
                hittingSide = rightHanded;
                throwingHand = rightHanded;
            }

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), RIGHT_FIELD, FIRST_BASE + LEFT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            throwingHand = rightHanded;

            hittingSide = getInfielderHittingSide();

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SECOND_BASE, FIRST_BASE + THIRD_BASE + SHORTSTOP + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            int handedness = random.nextInt(ONE_HUNDRED_PERCENT);
            if (handedness < FIRST_AND_OF_HITS_SWITCH_PCT) {
                hittingSide = switchHitter;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < POPULATION_LEFT_PCT) {
                    throwingHand = leftHanded;
                } else {
                    throwingHand = rightHanded;
                }
            } else if (handedness < (FIRST_AND_OF_HITS_SWITCH_PCT + OF_HITS_LEFT_PCT)) {
                hittingSide = leftHanded;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < INF_HITS_LEFT_PCT) {
                    throwingHand = rightHanded;
                } else {
                    throwingHand = leftHanded;
                }
            } else {
                hittingSide = rightHanded;
                throwingHand = rightHanded;
            }

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), CENTER_FIELD, FIRST_BASE +LEFT_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            throwingHand = rightHanded;

            hittingSide = getInfielderHittingSide();

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), SHORTSTOP, CATCHER + FIRST_BASE + SECOND_BASE +
                    THIRD_BASE + LEFT_FIELD + CENTER_FIELD + RIGHT_FIELD + DESIGNATED_HITTER,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(), teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
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
            int handedness = random.nextInt(ONE_HUNDRED_PERCENT);
            if (handedness < FIRST_AND_OF_HITS_SWITCH_PCT) {
                hittingSide = switchHitter;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < POPULATION_LEFT_PCT) {
                    throwingHand = leftHanded;
                } else {
                    throwingHand = rightHanded;
                }
            } else if (handedness < (FIRST_AND_OF_HITS_SWITCH_PCT + OF_HITS_LEFT_PCT)) {
                hittingSide = leftHanded;
                if (random.nextInt(ONE_HUNDRED_PERCENT) < INF_HITS_LEFT_PCT) {
                    throwingHand = rightHanded;
                } else {
                    throwingHand = leftHanded;
                }
            } else {
                hittingSide = rightHanded;
                throwingHand = rightHanded;
            }

            Player newHitter = new Player(newName.getFirstName(), newName.getMiddleName(), newName.getLastName(), DESIGNATED_HITTER, FIRST_BASE + LEFT_FIELD,
                    age, birthdate, hittingSide, throwingHand, null, null, generateHittingPercentages(), generatePitchingPercentages(),teamId);
            generateStatsAndUpdateProgress(hitters, progressBar, newHitter);
        }

        return hitters;
    }

    private void generateStatsAndUpdateProgress(List<Player> hitters, ProgressBar progressBar, Player newHitter) {
        newHitter.setBattingStats(generateBattingStats(newHitter.getPlayerId()));
        newHitter.setPitchingStats(generatePitchingStats(newHitter.getPlayerId()));
        hitters.add(newHitter);
        if (progressBar != null) {
            progressBar.setProgress(progressBar.getProgress() + 1);
        }
    }

    private List<PitchingStats> generatePitchingStats(String playerId) {
        List<PitchingStats> newPitchingStats = new ArrayList<>();
        newPitchingStats.add(0,new PitchingStats(0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, playerId));
        return newPitchingStats;
    }

    private List<BattingStats> generateBattingStats(String playerId) {
        List<BattingStats> newBattingStats = new ArrayList<>();
        newBattingStats.add(0,new BattingStats(0, 0, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0, playerId));
        return newBattingStats;
    }

    private String getInfielderHittingSide() {
        int handedness = random.nextInt(ONE_HUNDRED_PERCENT);
        if (handedness < INF_HITS_SWITCH_PCT) {
            return context.getResources().getString(R.string.switch_hitter);
        } else if (handedness< (INF_HITS_SWITCH_PCT + INF_HITS_LEFT_PCT)) {
            return context.getResources().getString(R.string.left_handed);
        } else {
            return context.getResources().getString(R.string.right_handed);
        }
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
        return Math.min(percentToCheck, max);
    }

    private HittingPercentages generateHittingPercentages() {
        int oSwingPct = getRandomNormalizedPercentage(BATTING_O_SWING_PCT_STD_DEV, BATTING_O_SWING_PCT_MEAN, BATTING_O_SWING_PCT_MIN, BATTING_O_SWING_PCT_MAX);

        int zSwingPct =  getRandomNormalizedPercentage(BATTING_Z_SWING_PCT_STD_DEV, BATTING_Z_SWING_PCT_MEAN, BATTING_Z_SWING_PCT_MIN, BATTING_Z_SWING_PCT_MAX);

        int oContactPct = getRandomNormalizedPercentage (BATTING_O_CONTACT_PCT_STD_DEV, BATTING_O_CONTACT_PCT_MEAN, BATTING_O_CONTACT_PCT_MIN, BATTING_O_CONTACT_PCT_MAX);

        int zContactPct = getRandomNormalizedPercentage(BATTING_Z_CONTACT_PCT_STD_DEV, BATTING_Z_CONTACT_PCT_MEAN, BATTING_Z_CONTACT_PCT_MIN, BATTING_Z_CONTACT_PCT_MAX);

        int groundBallPct = getRandomNormalizedPercentage(BATTING_GROUND_BALL_PCT_STD_DEV, BATTING_GROUND_BALL_PCT_MEAN, BATTING_GROUND_BALL_PCT_MIN, BATTING_GROUND_BALL_PCT_MAX);

        int lineDrivePct = getRandomNormalizedPercentage(BATTING_LINE_DRIVE_PCT_STD_DEV, BATTING_LINE_DRIVE_PCT_MEAN, BATTING_LINE_DRIVE_PCT_MIN, BATTING_LINE_DRIVE_PCT_MAX);

        int flyBallPct = ONE_HUNDRED_PERCENT - groundBallPct - lineDrivePct;

        int hardPct = getRandomNormalizedPercentage(BATTING_HARD_HIT_PCT_STD_DEV, BATTING_HARD_HIT_PCT_MEAN, BATTING_HARD_HIT_PCT_MIN, BATTING_HARD_HIT_PCT_MAX);

        int medPctMax =BATTING_MED_HIT_PCT_MAX;
        if ((ONE_HUNDRED_PERCENT - hardPct - 1040) < medPctMax ) {
            medPctMax = ONE_HUNDRED_PERCENT - hardPct - 1040;
        }
        int medPct = getRandomNormalizedPercentage(BATTING_MED_HIT_PCT_STD_DEV, BATTING_MED_HIT_PCT_MEAN, BATTING_MED_HIT_PCT_MIN, medPctMax);

        int homeRunPct = ((((flyBallPct * hardPct) / ONE_HUNDRED_PERCENT) * 7) + ((lineDrivePct * hardPct)/ONE_HUNDRED_PERCENT) * 2) / 9 - 270;
        homeRunPct = checkBounds(homeRunPct, BATTING_HOME_RUN_PCT_MIN, BATTING_HOME_RUN_PCT_MAX);

        int speed = getRandomNormalizedPercentage(BATTING_SPEED_STD_DEV, BATTING_SPEED_MEAN, BATTING_SPEED_MIN, BATTING_SPEED_MAX);

        int baseRunning = getRandomNormalizedPercentage(BATTING_BASERUNNING_STD_DEV, BATTING_BASERUNNING_MEAN, BATTING_BASERUNNING_MIN, BATTING_BASERUNNING_MAX);

        int triplePct = (int) ((speed - BATTING_SPEED_MEAN) * .9 + BATTING_TRIPLE_PCT_MEAN);
        triplePct = checkBounds(triplePct, BATTING_TRIPLE_PCT_MIN, BATTING_TRIPLE_PCT_MAX);

        int doublePct = (int) (random.nextGaussian() * BATTING_DOUBLE_STD_DEV) + BATTING_DOUBLE_PCT_MEAN;
        doublePct = checkBounds(doublePct, BATTING_DOUBLE_PCT_MIN, BATTING_DOUBLE_PCT_MAX);

        int stolenBasePct = BATTING_STOLEN_BASE_PCT_MEAN + ((speed - BATTING_SPEED_MEAN + baseRunning - BATTING_BASERUNNING_MEAN));
        stolenBasePct = checkBounds(stolenBasePct, BATTING_STOLEN_BASE_PCT_MIN, BATTING_STOLEN_BASE_PCT_MAX);

        int infieldFlyBallPct = (int) (random.nextGaussian() * BATTING_INFIELD_FLY_BALL_STD_DEV) + BATTING_INFIELD_FLY_BALL_PCT_MEAN;
        infieldFlyBallPct = checkBounds(infieldFlyBallPct, BATTING_INFIELD_FLY_BALL_PCT_MIN, BATTING_INFIELD_FLY_BALL_PCT_MAX);

        int hitByPitchPct = (int) (random.nextGaussian() * BATTING_HIT_BY_PITCH_STD_DEV) + BATTING_HIT_BY_PITCH_PCT_MEAN;
        hitByPitchPct = checkBounds(hitByPitchPct, BATTING_HIT_BY_PITCH_PCT_MIN, BATTING_HIT_BY_PITCH_PCT_MAX);

        int pullPct = (int) (random.nextGaussian() * BATTING_PULL_STD_DEV) + BATTING_PULL_PCT_MEAN;
        pullPct = checkBounds(pullPct, BATTING_PULL_PCT_MIN, BATTING_PULL_PCT_MAX);

        int centerPct = (int) (random.nextGaussian() * BATTING_CENTER_STD_DEV) + BATTING_CENTER_PCT_MEAN;
        centerPct = checkBounds(centerPct, BATTING_CENTER_PCT_MIN, BATTING_CENTER_PCT_MAX);

        int battingAverageBallsInPlay = 150 + (((hardPct*lineDrivePct)/ONE_HUNDRED_PERCENT)* BATTING_HARD_LINE_DRIVE_HIT_PCT) / ONE_HUNDRED_PERCENT +
                ((medPct*lineDrivePct)/ONE_HUNDRED_PERCENT)* BATTING_MED_LINE_DRIVE_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((ONE_HUNDRED_PERCENT-hardPct-medPct)*lineDrivePct)/ONE_HUNDRED_PERCENT * BATTING_SOFT_LINE_DRIVE_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((hardPct*groundBallPct)/ONE_HUNDRED_PERCENT)* BATTING_HARD_GROUNDBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((medPct*groundBallPct)/ONE_HUNDRED_PERCENT)* BATTING_MED_GROUNDBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((ONE_HUNDRED_PERCENT-hardPct-medPct)*groundBallPct)/ONE_HUNDRED_PERCENT * BATTING_SOFT_GROUNDBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((hardPct*(ONE_HUNDRED_PERCENT-lineDrivePct-groundBallPct))/ONE_HUNDRED_PERCENT)* BATTING_HARD_FLYBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((medPct*(ONE_HUNDRED_PERCENT-lineDrivePct-groundBallPct))/ONE_HUNDRED_PERCENT)* BATTING_MED_FLYBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                ((ONE_HUNDRED_PERCENT-hardPct-medPct)*(ONE_HUNDRED_PERCENT-lineDrivePct-groundBallPct))/ONE_HUNDRED_PERCENT * BATTING_SOFT_FLYBALL_HIT_PCT / ONE_HUNDRED_PERCENT +
                (BATTING_PULL_PCT_MEAN - pullPct)/5 + (centerPct - BATTING_CENTER_PCT_MEAN)/5 + (((ONE_HUNDRED_PERCENT - pullPct - centerPct) -
                (ONE_HUNDRED_PERCENT - BATTING_PULL_PCT_MEAN - BATTING_CENTER_PCT_MEAN))/5);
        battingAverageBallsInPlay = checkBounds(battingAverageBallsInPlay, BATTING_BABIP_PCT_MIN, BATTING_BABIP_PCT_MAX);

        int foulBallPct = (int) (random.nextGaussian() * BATTING_FOUL_BALL_STD_DEV) + BATTING_FOUL_BALL_PCT_MEAN;
        foulBallPct = checkBounds(foulBallPct, BATTING_FOUL_BALL_PCT_MIN, BATTING_FOUL_BALL_PCT_MAX);

        int stolenBaseRate = (int) (random.nextGaussian() * BATTING_STOLEN_BASE_RATE_STD_DEV) + BATTING_STOLEN_BASE_RATE_MEAN;
        stolenBaseRate = checkBounds(stolenBaseRate, BATTING_STOLEN_BASE_RATE_MIN, BATTING_STOLEN_BASE_RATE_MAX);

        int errorPct = (int) (random.nextGaussian() * BATTING_ERROR_PCT_STD_DEV) + BATTING_ERROR_PCT_MEAN;
        errorPct = checkBounds(errorPct, BATTING_ERROR_PCT_MIN, BATTING_ERROR_PCT_MAX);

        int stamina = (int) (random.nextGaussian() * BATTING_STAMINA_STD_DEV) + BATTING_STAMINA_MEAN;
        stamina = checkBounds(stamina, BATTING_STAMINA_MIN, BATTING_STAMINA_MAX);


        return new HittingPercentages(oSwingPct, zSwingPct, oContactPct, zContactPct, speed, lineDrivePct, groundBallPct, hardPct, medPct, pullPct, centerPct, homeRunPct, triplePct, doublePct,
                stolenBasePct, infieldFlyBallPct, hitByPitchPct, battingAverageBallsInPlay, foulBallPct, stolenBaseRate, baseRunning, errorPct, stamina, 0);
    }



    private PitchingPercentages generatePitchingPercentages() {
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

        List<Integer> pitchTypes;
        int stamina;

        int numberOfPitchTypes = getRandomNormalizedPercentage(1, 2, 1, 3);

        stamina = getRandomNormalizedPercentage(SHORT_RELIEVER_STAMINA_STD_DEV, SHORT_RELIEVER_STAMINA_MEAN, SHORT_RELIEVER_STAMINA_MIN, SHORT_RELIEVER_STAMINA_MAX);
        pitchTypes = generatePitchTypesThrown(numberOfPitchTypes);

        int maxPercentLeft = ONE_HUNDRED_PERCENT - FASTBALL_THROWN_MIN;
        int sliderPct = 0;
        if (pitchTypes.contains(SLIDER)){
            sliderPct =  getRandomNormalizedPercentage(SLIDER_THROWN_STD_DEV, SLIDER_THROWN_MEAN, SLIDER_THROWN_MIN, SLIDER_THROWN_MAX);
            if (sliderPct > maxPercentLeft) {
                sliderPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= sliderPct;
        int cutterPct = 0;
        if (pitchTypes.contains(CUTTER)){
            cutterPct =  getRandomNormalizedPercentage(CUTTER_THROWN_STD_DEV, CUTTER_THROWN_MEAN, CUTTER_THROWN_MIN, CUTTER_THROWN_MAX);
            if (cutterPct > maxPercentLeft) {
                cutterPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= cutterPct;
        int curveballPct = 0;
        if (pitchTypes.contains(CURVEBALL)){
            curveballPct =  getRandomNormalizedPercentage(CURVE_THROWN_STD_DEV, CURVE_THROWN_MEAN, CURVE_THROWN_MIN, CURVE_THROWN_MAX);
            if (curveballPct > maxPercentLeft) {
                curveballPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= curveballPct;
        int changeUpPct = 0;
        if (pitchTypes.contains(CHANGE_UP)){
            changeUpPct =  getRandomNormalizedPercentage(CHANGE_THROWN_STD_DEV, CHANGE_THROWN_MEAN, CHANGE_THROWN_MIN, CHANGE_THROWN_MAX);
            if (changeUpPct > maxPercentLeft) {
                changeUpPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= changeUpPct;
        int splitFingerPct = 0;
        if (pitchTypes.contains(SPLIT_FINGER)){
            splitFingerPct =  getRandomNormalizedPercentage(SPLITTER_THROWN_STD_DEV, SPLITTER_THROWN_MEAN, SPLITTER_THROWN_MIN, SPLITTER_THROWN_MAX);
            if (splitFingerPct > maxPercentLeft) {
                splitFingerPct = maxPercentLeft;
            }
        }
        maxPercentLeft -= splitFingerPct;
        int knuckleballPct = 0;
        if (pitchTypes.contains(KNUCKLEBALL)){
            knuckleballPct =  getRandomNormalizedPercentage(KNUCKLER_THROWN_STD_DEV, KNUCKLER_THROWN_MEAN, KNUCKLER_THROWN_MIN, KNUCKLER_THROWN_MAX);
            if (knuckleballPct > maxPercentLeft) {
                knuckleballPct = maxPercentLeft;
            }
        }

        int fastballPct = maxPercentLeft + FASTBALL_THROWN_MIN;


        return new PitchingPercentages(oSwingPct, zSwingPct, oContactPct, zContactPct, groundBallPct, lineDrivePct, homeRunPct, infieldFlyBallPct, hitByPitchPct, wildPitchPct, balkPct, zonePct,
                firstStrikePct, fastballPct, sliderPct, cutterPct, curveballPct, changeUpPct, splitFingerPct, knuckleballPct, stamina, 0);
    }

    private int getRandomNormalizedPercentage(int stdDev, int mean, int min, int max) {
        int normalizedPercentage = (int) (random.nextGaussian() * stdDev) + mean;
        return checkBounds(normalizedPercentage, min, max);
    }

    private List<Integer> generatePitchTypesThrown(int numberOfPitchTypes) {
        List<Integer> pitchTypes = new ArrayList<>();
        // Every pitcher can throw a fastball so add that to pitchTypes and reduce numberOfPitchTypes by 1
        pitchTypes.add(FASTBALL);
        numberOfPitchTypes--;
        if (numberOfPitchTypes == 6) {
            Collections.addAll(pitchTypes, CHANGE_UP, SLIDER, CURVEBALL, CUTTER, SPLIT_FINGER, KNUCKLEBALL);
            return pitchTypes;
        }
        while (numberOfPitchTypes>0) {
            if (!pitchTypes.contains(CHANGE_UP) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_CHANGE_PCT) {
                pitchTypes.add(CHANGE_UP);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes>0 && !pitchTypes.contains(SLIDER) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_SLIDER_PCT) {
                pitchTypes.add(SLIDER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes>0 && !pitchTypes.contains(CURVEBALL) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_CURVE_PCT) {
                pitchTypes.add(CURVEBALL);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes>0 && !pitchTypes.contains(CUTTER) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_CUTTER_PCT) {
                pitchTypes.add(CUTTER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes>0 && !pitchTypes.contains(SPLIT_FINGER) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_SPLITTER_PCT) {
                pitchTypes.add(SPLIT_FINGER);
                numberOfPitchTypes--;
            }
            if (numberOfPitchTypes>0 && !pitchTypes.contains(KNUCKLEBALL) && random.nextInt(ONE_HUNDRED_PERCENT) < PITCHERS_THAT_THROW_KNUCKLER_PCT) {
                pitchTypes.add(KNUCKLEBALL);
                numberOfPitchTypes--;
            }
        }
        return pitchTypes;
    }
}
