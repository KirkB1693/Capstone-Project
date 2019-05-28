package com.example.android.baseballbythenumbers.Data;

public class Constants {

    // Data is taken from 2009-2018 seasons with minimum at bats for batters and innings pitched for pitchers in order to get solid averages over time and eliminate extreme stats due to small sample size.
    // Data was sourced mainly from FanGraphs.com, but some stats not available at FanGraphs were sourced at Baseball-Reference.com

    public class PitcherBaseStats {
        //Pitcher Base Percentages to 4 decimal places represented by integers (2950 = 29.50%)
        public static final int PITCHER_O_SWING_PCT_MEAN = 2950;
        public static final int PITCHER_O_SWING_PCT_STD_DEV = 281;
        public static final int PITCHER_O_SWING_PCT_MIN = 2010;
        public static final int PITCHER_O_SWING_PCT_MAX = 3890;

        public static final int PITCHER_Z_SWING_PCT_MEAN = 6609;
        public static final int PITCHER_Z_SWING_PCT_STD_DEV = 295;
        public static final int PITCHER_Z_SWING_PCT_MIN = 5460;
        public static final int PITCHER_Z_SWING_PCT_MAX = 7680;

        public static final int PITCHER_O_CONTACT_PCT_MEAN = 6475;
        public static final int PITCHER_O_CONTACT_PCT_STD_DEV = 665;
        public static final int PITCHER_O_CONTACT_PCT_MIN = 3680;
        public static final int PITCHER_O_CONTACT_PCT_MAX = 8220;

        public static final int PITCHER_Z_CONTACT_PCT_MEAN = 8676;
        public static final int PITCHER_Z_CONTACT_PCT_STD_DEV = 346;
        public static final int PITCHER_Z_CONTACT_PCT_MIN = 6920;
        public static final int PITCHER_Z_CONTACT_PCT_MAX = 9450;

        public static final int PITCHER_GROUND_BALL_PCT_MEAN = 4402;
        public static final int PITCHER_GROUND_BALL_PCT_STD_DEV = 709;
        public static final int PITCHER_GROUND_BALL_PCT_MIN = 2490;
        public static final int PITCHER_GROUND_BALL_PCT_MAX = 7210;

        public static final int PITCHER_LINE_DRIVE_PCT_MEAN = 2018;
        public static final int PITCHER_LINE_DRIVE_PCT_STD_DEV = 184;
        public static final int PITCHER_LINE_DRIVE_PCT_MIN = 1430;
        public static final int PITCHER_LINE_DRIVE_PCT_MAX = 2750;

        public static final int PITCHER_HOME_RUN_PCT_MEAN = 396;
        public static final int PITCHER_HOME_RUN_PCT_STD_DEV = 111;
        public static final int PITCHER_HOME_RUN_PCT_MIN = 91;
        public static final int PITCHER_HOME_RUN_PCT_MAX = 827;

        public static final int PITCHER_INFIELD_FLY_BALL_PCT_MEAN = 964;
        public static final int PITCHER_INFIELD_FLY_BALL_STD_DEV = 260;
        public static final int PITCHER_INFIELD_FLY_BALL_PCT_MIN = 180;
        public static final int PITCHER_INFIELD_FLY_BALL_PCT_MAX = 2130;

        public static final int PITCHER_HIT_BY_PITCH_PCT_MEAN = 90;
        public static final int PITCHER_HIT_BY_PITCH_STD_DEV = 43;
        public static final int PITCHER_HIT_BY_PITCH_PCT_MIN = 0;
        public static final int PITCHER_HIT_BY_PITCH_PCT_MAX = 296;

        public static final int PITCHER_WILD_PITCH_PCT_MEAN = 99;
        public static final int PITCHER_WILD_PITCH_STD_DEV = 57;
        public static final int PITCHER_WILD_PITCH_PCT_MIN = 0;
        public static final int PITCHER_WILD_PITCH_PCT_MAX = 524;

        public static final int PITCHER_BALK_PCT_MEAN = 8;
        public static final int PITCHER_BALK_STD_DEV = 11;
        public static final int PITCHER_BALK_PCT_MIN = 0;
        public static final int PITCHER_BALK_PCT_MAX = 80;

        public static final int PITCHER_ZONE_PCT_MEAN = 4505;
        public static final int PITCHER_ZONE_STD_DEV = 265;
        public static final int PITCHER_ZONE_PCT_MIN = 3330;
        public static final int PITCHER_ZONE_PCT_MAX = 5440;

        public static final int PITCHER_FIRST_STRIKE_PCT_MEAN = 5958;
        public static final int PITCHER_FIRST_STRIKE_STD_DEV = 324;
        public static final int PITCHER_FIRST_STRIKE_PCT_MIN = 4900;
        public static final int PITCHER_FIRST_STRIKE_PCT_MAX = 7080;

        public static final int STARTER_NUM_PITCH_TYPES_MEAN = 432;
        public static final int STARTER_NUM_PITCH_TYPES_STD_DEV = 78;
        public static final int STARTER_NUM_PITCH_TYPES_MIN = 300;
        public static final int STARTER_NUM_PITCH_TYPES_MAX = 700;

        public static final int RELIEVER_NUM_PITCH_TYPES_MEAN = 418;
        public static final int RELIEVER_NUM_PITCH_TYPES_STD_DEV = 82;
        public static final int RELIEVER_NUM_PITCH_TYPES_MIN = 200;
        public static final int RELIEVER_NUM_PITCH_TYPES_MAX = 700;

        public static final int STARTER_STAMINA_MEAN = 9048;
        public static final int STARTER_STAMINA_STD_DEV = 732;
        public static final int STARTER_STAMINA_MIN = 6152;
        public static final int STARTER_STAMINA_MAX = 10953;

        public static final int LONG_RELIEVER_STAMINA_MEAN = 4990;
        public static final int LONG_RELIEVER_STAMINA_STD_DEV = 1936;
        public static final int LONG_RELIEVER_STAMINA_MIN = 2102;
        public static final int LONG_RELIEVER_STAMINA_MAX = 8684;

        public static final int SHORT_RELIEVER_STAMINA_MEAN = 1629;
        public static final int SHORT_RELIEVER_STAMINA_STD_DEV = 204;
        public static final int SHORT_RELIEVER_STAMINA_MIN = 807;
        public static final int SHORT_RELIEVER_STAMINA_MAX = 2099;

        public static final int PITCHERS_THAT_THROW_SLIDER_PCT = 8840;
        public static final int PITCHERS_THAT_THROW_CUTTER_PCT = 4212;
        public static final int PITCHERS_THAT_THROW_CURVE_PCT = 7735;
        public static final int PITCHERS_THAT_THROW_CHANGE_PCT = 9365;
        public static final int PITCHERS_THAT_THROW_SPLITTER_PCT = 1674;
        public static final int PITCHERS_THAT_THROW_KNUCKLER_PCT = 77;

        public static final int SLIDER_THROWN_MEAN = 1767;
        public static final int SLIDER_THROWN_STD_DEV = 1120;
        public static final int SLIDER_THROWN_MIN = 0;
        public static final int SLIDER_THROWN_MAX = 6430;

        public static final int CUTTER_THROWN_MEAN = 1270;
        public static final int CUTTER_THROWN_STD_DEV = 1290;
        public static final int CUTTER_THROWN_MIN = 0;
        public static final int CUTTER_THROWN_MAX = 8890;

        public static final int CURVE_THROWN_MEAN = 1169;
        public static final int CURVE_THROWN_STD_DEV = 844;
        public static final int CURVE_THROWN_MIN = 0;
        public static final int CURVE_THROWN_MAX = 4450;

        public static final int CHANGE_THROWN_MEAN = 1057;
        public static final int CHANGE_THROWN_STD_DEV = 789;
        public static final int CHANGE_THROWN_MIN = 0;
        public static final int CHANGE_THROWN_MAX = 3880;

        public static final int SPLITTER_THROWN_MEAN = 1007;
        public static final int SPLITTER_THROWN_STD_DEV = 1150;
        public static final int SPLITTER_THROWN_MIN = 0;
        public static final int SPLITTER_THROWN_MAX = 5860;

        public static final int KNUCKLER_THROWN_MEAN = 3847;
        public static final int KNUCKLER_THROWN_STD_DEV = 4033;
        public static final int KNUCKLER_THROWN_MIN = 0;
        public static final int KNUCKLER_THROWN_MAX = 8580;

        public static final int FASTBALL_THROWN_MIN = 110;

    }

    public class PitcherBatting {
        public static final int PITCHER_BATTING_O_SWING_PCT_MEAN = 3401;
        public static final int PITCHER_BATTING_O_SWING_PCT_STD_DEV = 712;
        public static final int PITCHER_BATTING_O_SWING_PCT_MIN = 1540;
        public static final int PITCHER_BATTING_O_SWING_PCT_MAX = 5110;

        public static final int PITCHER_BATTING_Z_SWING_PCT_MEAN = 5793;
        public static final int PITCHER_BATTING_Z_SWING_PCT_STD_DEV = 658;
        public static final int PITCHER_BATTING_Z_SWING_PCT_MIN = 4100;
        public static final int PITCHER_BATTING_Z_SWING_PCT_MAX = 7670;

        public static final int PITCHER_BATTING_O_CONTACT_PCT_MEAN = 5352;
        public static final int PITCHER_BATTING_O_CONTACT_PCT_STD_DEV = 1068;
        public static final int PITCHER_BATTING_O_CONTACT_PCT_MIN = 2230;
        public static final int PITCHER_BATTING_O_CONTACT_PCT_MAX = 7840;

        public static final int PITCHER_BATTING_Z_CONTACT_PCT_MEAN = 8013;
        public static final int PITCHER_BATTING_Z_CONTACT_PCT_STD_DEV = 616;
        public static final int PITCHER_BATTING_Z_CONTACT_PCT_MIN = 6520;
        public static final int PITCHER_BATTING_Z_CONTACT_PCT_MAX = 9520;

        public static final int PITCHER_BATTING_GROUND_BALL_PCT_MEAN = 6080;
        public static final int PITCHER_BATTING_GROUND_BALL_PCT_STD_DEV = 1095;
        public static final int PITCHER_BATTING_GROUND_BALL_PCT_MIN = 3220;
        public static final int PITCHER_BATTING_GROUND_BALL_PCT_MAX = 8480;

        public static final int PITCHER_BATTING_LINE_DRIVE_PCT_MEAN = 1723;
        public static final int PITCHER_BATTING_LINE_DRIVE_PCT_STD_DEV = 462;
        public static final int PITCHER_BATTING_LINE_DRIVE_PCT_MIN = 780;
        public static final int PITCHER_BATTING_LINE_DRIVE_PCT_MAX = 3370;

        public static final int PITCHER_BATTING_HOME_RUN_PCT_MEAN = 92;
        public static final int PITCHER_BATTING_HOME_RUN_PCT_STD_DEV = 136;
        public static final int PITCHER_BATTING_HOME_RUN_PCT_MIN = 0;
        public static final int PITCHER_BATTING_HOME_RUN_PCT_MAX = 678;

        public static final int PITCHER_BATTING_TRIPLE_PCT_MEAN = 22;
        public static final int PITCHER_BATTING_TRIPLE_STD_DEV = 51;
        public static final int PITCHER_BATTING_TRIPLE_PCT_MIN = 0;
        public static final int PITCHER_BATTING_TRIPLE_PCT_MAX = 331;

        public static final int PITCHER_BATTING_DOUBLE_PCT_MEAN = 378;
        public static final int PITCHER_BATTING_DOUBLE_STD_DEV = 225;
        public static final int PITCHER_BATTING_DOUBLE_PCT_MIN = 0;
        public static final int PITCHER_BATTING_DOUBLE_PCT_MAX = 1017;

        public static final int PITCHER_BATTING_STOLEN_BASE_PCT_MEAN = 1307;
        public static final int PITCHER_BATTING_STOLEN_BASE_STD_DEV = 3276;
        public static final int PITCHER_BATTING_STOLEN_BASE_PCT_MIN = 0;
        public static final int PITCHER_BATTING_STOLEN_BASE_PCT_MAX = 10000;

        public static final int PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MEAN = 1039;
        public static final int PITCHER_BATTING_INFIELD_FLY_BALL_STD_DEV = 850;
        public static final int PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MIN = 0;
        public static final int PITCHER_BATTING_INFIELD_FLY_BALL_PCT_MAX = 4615;

        public static final int PITCHER_BATTING_HIT_BY_PITCH_PCT_MEAN = 28;
        public static final int PITCHER_BATTING_HIT_BY_PITCH_STD_DEV = 38;
        public static final int PITCHER_BATTING_HIT_BY_PITCH_PCT_MIN = 0;
        public static final int PITCHER_BATTING_HIT_BY_PITCH_PCT_MAX = 207;

        public static final int PITCHER_BATTING_BABIP_PCT_MEAN = 2271;
        public static final int PITCHER_BATTING_BABIP_STD_DEV = 551;
        public static final int PITCHER_BATTING_BABIP_PCT_MIN = 1080;
        public static final int PITCHER_BATTING_BABIP_PCT_MAX = 4210;

        public static final int PITCHER_BATTING_FOUL_BALL_PCT_MEAN = 2705;
        public static final int PITCHER_BATTING_FOUL_BALL_STD_DEV = 289;
        public static final int PITCHER_BATTING_FOUL_BALL_PCT_MIN = 1820;
        public static final int PITCHER_BATTING_FOUL_BALL_PCT_MAX = 3700;

        public static final int PITCHER_BATTING_PULL_PCT_MEAN = 4023;
        public static final int PITCHER_BATTING_PULL_STD_DEV = 499;
        public static final int PITCHER_BATTING_PULL_PCT_MIN = 2500;
        public static final int PITCHER_BATTING_PULL_PCT_MAX = 5450;

        public static final int PITCHER_BATTING_CENTER_PCT_MEAN = 3476;
        public static final int PITCHER_BATTING_CENTER_STD_DEV = 224;
        public static final int PITCHER_BATTING_CENTER_PCT_MIN = 2930;
        public static final int PITCHER_BATTING_CENTER_PCT_MAX = 4190;

        public static final int PITCHER_BATTING_STOLEN_BASE_RATE_MEAN = 519;
        public static final int PITCHER_BATTING_STOLEN_BASE_RATE_STD_DEV = 566;
        public static final int PITCHER_BATTING_STOLEN_BASE_RATE_MIN = 0;
        public static final int PITCHER_BATTING_STOLEN_BASE_RATE_MAX = 4756;

        public static final int PITCHER_BATTING_BASERUNNING_MEAN = 3;
        public static final int PITCHER_BATTING_BASERUNNING_STD_DEV = 1009;
        public static final int PITCHER_BATTING_BASERUNNING_MIN = -4550;
        public static final int PITCHER_BATTING_BASERUNNING_MAX = 4720;

        public static final int PITCHER_BATTING_SPEED_MEAN = 409;
        public static final int PITCHER_BATTING_SPEED_STD_DEV = 161;
        public static final int PITCHER_BATTING_SPEED_MIN = 90;
        public static final int PITCHER_BATTING_SPEED_MAX = 880;

        public static final int PITCHER_BATTING_ERROR_PCT_MEAN = 507;
        public static final int PITCHER_BATTING_ERROR_PCT_STD_DEV = 405;
        public static final int PITCHER_BATTING_ERROR_PCT_MIN = 0;
        public static final int PITCHER_BATTING_ERROR_PCT_MAX = 2308;

        public static final int PITCHER_BATTING_STAMINA_MEAN = 14615;
        public static final int PITCHER_BATTING_STAMINA_STD_DEV = 1008;
        public static final int PITCHER_BATTING_STAMINA_MIN = 122;
        public static final int PITCHER_BATTING_STAMINA_MAX = 162;
    }

    public class BatterBaseStats {
        public static final int BATTING_O_SWING_PCT_MEAN = 2963;
        public static final int BATTING_O_SWING_PCT_STD_DEV = 521;
        public static final int BATTING_O_SWING_PCT_MIN = 1600;
        public static final int BATTING_O_SWING_PCT_MAX = 4470;

        public static final int BATTING_Z_SWING_PCT_MEAN = 6638;
        public static final int BATTING_Z_SWING_PCT_STD_DEV = 513;
        public static final int BATTING_Z_SWING_PCT_MIN = 5150;
        public static final int BATTING_Z_SWING_PCT_MAX = 8120;

        public static final int BATTING_O_CONTACT_PCT_MEAN = 6542;
        public static final int BATTING_O_CONTACT_PCT_STD_DEV = 851;
        public static final int BATTING_O_CONTACT_PCT_MIN = 4050;
        public static final int BATTING_O_CONTACT_PCT_MAX = 8800;

        public static final int BATTING_Z_CONTACT_PCT_MEAN = 8725;
        public static final int BATTING_Z_CONTACT_PCT_STD_DEV = 450;
        public static final int BATTING_Z_CONTACT_PCT_MIN = 7030;
        public static final int BATTING_Z_CONTACT_PCT_MAX = 9710;

        public static final int BATTING_GROUND_BALL_PCT_MEAN = 4383;
        public static final int BATTING_GROUND_BALL_PCT_STD_DEV = 578;
        public static final int BATTING_GROUND_BALL_PCT_MIN = 2384;
        public static final int BATTING_GROUND_BALL_PCT_MAX = 6209;

        public static final int BATTING_LINE_DRIVE_PCT_MEAN = 2032;
        public static final int BATTING_LINE_DRIVE_PCT_STD_DEV = 205;
        public static final int BATTING_LINE_DRIVE_PCT_MIN = 1419;
        public static final int BATTING_LINE_DRIVE_PCT_MAX = 2769;

        public static final int BATTING_HOME_RUN_PCT_MEAN = 408;
        public static final int BATTING_HOME_RUN_PCT_STD_DEV = 216;
        public static final int BATTING_HOME_RUN_PCT_MIN = 20;
        public static final int BATTING_HOME_RUN_PCT_MAX = 1469;

        public static final int BATTING_TRIPLE_PCT_MEAN = 71;
        public static final int BATTING_TRIPLE_STD_DEV = 51;
        public static final int BATTING_TRIPLE_PCT_MIN = 0;
        public static final int BATTING_TRIPLE_PCT_MAX = 267;

        public static final int BATTING_DOUBLE_PCT_MEAN = 663;
        public static final int BATTING_DOUBLE_STD_DEV = 111;
        public static final int BATTING_DOUBLE_PCT_MIN = 311;
        public static final int BATTING_DOUBLE_PCT_MAX = 1020;

        public static final int BATTING_STOLEN_BASE_PCT_MEAN = 6551;
        public static final int BATTING_STOLEN_BASE_STD_DEV = 1667;
        public static final int BATTING_STOLEN_BASE_PCT_MIN = 0;
        public static final int BATTING_STOLEN_BASE_PCT_MAX = 10000;

        public static final int BATTING_INFIELD_FLY_BALL_PCT_MEAN = 956;
        public static final int BATTING_INFIELD_FLY_BALL_STD_DEV = 324;
        public static final int BATTING_INFIELD_FLY_BALL_PCT_MIN = 69;
        public static final int BATTING_INFIELD_FLY_BALL_PCT_MAX = 1841;

        public static final int BATTING_HIT_BY_PITCH_PCT_MEAN = 92;
        public static final int BATTING_HIT_BY_PITCH_STD_DEV = 63;
        public static final int BATTING_HIT_BY_PITCH_PCT_MIN = 6;
        public static final int BATTING_HIT_BY_PITCH_PCT_MAX = 572;

        public static final int BATTING_BABIP_PCT_MEAN = 3001;
        public static final int BATTING_BABIP_STD_DEV = 234;
        public static final int BATTING_BABIP_PCT_MIN = 2330;
        public static final int BATTING_BABIP_PCT_MAX = 3600;

        public static final int BATTING_FOUL_BALL_PCT_MEAN = 2705;
        public static final int BATTING_FOUL_BALL_STD_DEV = 289;
        public static final int BATTING_FOUL_BALL_PCT_MIN = 1820;
        public static final int BATTING_FOUL_BALL_PCT_MAX = 3700;

        public static final int BATTING_PULL_PCT_MEAN = 4023;
        public static final int BATTING_PULL_STD_DEV = 499;
        public static final int BATTING_PULL_PCT_MIN = 2500;
        public static final int BATTING_PULL_PCT_MAX = 5450;

        public static final int BATTING_CENTER_PCT_MEAN = 3476;
        public static final int BATTING_CENTER_STD_DEV = 224;
        public static final int BATTING_CENTER_PCT_MIN = 2930;
        public static final int BATTING_CENTER_PCT_MAX = 4190;

        public static final int BATTING_STOLEN_BASE_RATE_MEAN = 519;
        public static final int BATTING_STOLEN_BASE_RATE_STD_DEV = 566;
        public static final int BATTING_STOLEN_BASE_RATE_MIN = 0;
        public static final int BATTING_STOLEN_BASE_RATE_MAX = 4756;

        public static final int BATTING_BASERUNNING_MEAN = 3;
        public static final int BATTING_BASERUNNING_STD_DEV = 1009;
        public static final int BATTING_BASERUNNING_MIN = -4550;
        public static final int BATTING_BASERUNNING_MAX = 4720;

        public static final int BATTING_SPEED_MEAN = 409;
        public static final int BATTING_SPEED_STD_DEV = 161;
        public static final int BATTING_SPEED_MIN = 90;
        public static final int BATTING_SPEED_MAX = 880;

        public static final int BATTING_ERROR_PCT_MEAN = 205;
        public static final int BATTING_ERROR_PCT_STD_DEV = 168;
        public static final int BATTING_ERROR_PCT_MIN = 0;
        public static final int BATTING_ERROR_PCT_MAX = 1120;

        public static final int BATTING_STAMINA_MEAN = 14615;
        public static final int BATTING_STAMINA_STD_DEV = 1008;
        public static final int BATTING_STAMINA_MIN = 122;
        public static final int BATTING_STAMINA_MAX = 162;
    }
}

