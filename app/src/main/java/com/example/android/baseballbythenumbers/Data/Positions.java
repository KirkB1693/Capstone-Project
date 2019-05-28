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

    public static String getPositionName(int primaryPosition) {
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
}
