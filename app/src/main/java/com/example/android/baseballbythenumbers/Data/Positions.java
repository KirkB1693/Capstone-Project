package com.example.android.baseballbythenumbers.Data;

public class Positions {

    public static final int STARTING_PITCHER = 1;
    public static final int CATCHER = 2;
    public static final int FIRST_BASE = 4;
    public static final int SECOND_BASE = 8;
    public static final int THIRD_BASE = 16;
    public static final int SHORTSTOP = 32;
    public static final int LEFT_FIELD = 64;
    public static final int CENTER_FIELD = 128;
    public static final int RIGHT_FIELD = 256;
    public static final int DESIGNATED_HITTER = 512;
    public static final int LONG_RELIEVER = 1024;
    public static final int SHORT_RELEIVER = 2048;

    public static final int SCOREKEEPING_PITCHER = 1;
    public static final int SCOREKEEPING_CATCHER = 2;
    public static final int SCOREKEEPING_FIRST_BASE = 3;
    public static final int SCOREKEEPING_SECOND_BASE = 4;
    public static final int SCOREKEEPING_THIRD_BASE = 5;
    public static final int SCOREKEEPING_SHORTSTOP = 6;
    public static final int SCOREKEEPING_LEFT_FIELD = 7;
    public static final int SCOREKEEPING_CENTER_FIELD = 8;
    public static final int SCOREKEEPING_RIGHT_FIELD = 9;

    public static String getPositionNameFromPrimaryPosition(int primaryPosition) {
        switch (primaryPosition) {
            case STARTING_PITCHER:
                return "P";
            case LONG_RELIEVER:
                return "P";
            case SHORT_RELEIVER:
                return "P";
            case CATCHER:
                return "C";
            case FIRST_BASE:
                return "1B";
            case SECOND_BASE:
                return "2B";
            case THIRD_BASE:
                return "3B";
            case SHORTSTOP:
                return "SS";
            case LEFT_FIELD:
                return "LF";
            case CENTER_FIELD:
                return "CF";
            case RIGHT_FIELD:
                return "RF";
            case DESIGNATED_HITTER:
                return "DH";
            default:
                return "Position Not Found";
        }
    }

    public static String getPositionNameFromScorekeeperPosition(int scorekeeperPosition) {
        switch (scorekeeperPosition) {
            case SCOREKEEPING_PITCHER:
                return "P";
            case SCOREKEEPING_CATCHER:
                return "C";
            case SCOREKEEPING_FIRST_BASE:
                return "1B";
            case SCOREKEEPING_SECOND_BASE:
                return "2B";
            case SCOREKEEPING_THIRD_BASE:
                return "3B";
            case SCOREKEEPING_SHORTSTOP:
                return "SS";
            case SCOREKEEPING_LEFT_FIELD:
                return "LF";
            case SCOREKEEPING_CENTER_FIELD:
                return "CF";
            case SCOREKEEPING_RIGHT_FIELD:
                return "RF";
            default:
                return "Position Not Found";
        }
    }
}
