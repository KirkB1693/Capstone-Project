package com.example.android.baseballbythenumbers.Data;

public class GameStates {
    public static final int NO_OUTS_NOBODY_ON = 1;
    public static final int NO_OUTS_RUNNER_ON_FIRST = 2;
    public static final int NO_OUTS_RUNNER_ON_SECOND = 3;
    public static final int NO_OUTS_RUNNER_ON_THIRD = 4;
    public static final int NO_OUTS_RUNNER_ON_FIRST_AND_SECOND = 5;
    public static final int NO_OUTS_RUNNER_ON_FIRST_AND_THIRD = 6;
    public static final int NO_OUTS_RUNNER_ON_SECOND_AND_THIRD = 7;
    public static final int NO_OUTS_BASES_LOADED = 8;

    public static final int ONE_OUT_NOBODY_ON = 9;
    public static final int ONE_OUT_RUNNER_ON_FIRST = 10;
    public static final int ONE_OUT_RUNNER_ON_SECOND = 11;
    public static final int ONE_OUT_RUNNER_ON_THIRD = 12;
    public static final int ONE_OUT_RUNNER_ON_FIRST_AND_SECOND = 13;
    public static final int ONE_OUT_RUNNER_ON_FIRST_AND_THIRD = 14;
    public static final int ONE_OUT_RUNNER_ON_SECOND_AND_THIRD = 15;
    public static final int ONE_OUT_BASES_LOADED = 16;

    public static final int TWO_OUTS_NOBODY_ON = 17;
    public static final int TWO_OUTS_RUNNER_ON_FIRST = 18;
    public static final int TWO_OUTS_RUNNER_ON_SECOND = 19;
    public static final int TWO_OUTS_RUNNER_ON_THIRD = 20;
    public static final int TWO_OUTS_RUNNER_ON_FIRST_AND_SECOND = 21;
    public static final int TWO_OUTS_RUNNER_ON_FIRST_AND_THIRD = 22;
    public static final int TWO_OUTS_RUNNER_ON_SECOND_AND_THIRD = 23;
    public static final int TWO_OUTS_BASES_LOADED = 24;

    public static final int THREE_OUTS = 25;

    public static String getGameStateString(int gameState) {
        switch (gameState) {
            case NO_OUTS_NOBODY_ON:
                return "Outs: 0, Runners: None";
            case NO_OUTS_RUNNER_ON_FIRST:
                return "Outs: 0, Runners: 1B";
            case NO_OUTS_RUNNER_ON_SECOND:
                return "Outs: 0, Runners: 2B";
            case NO_OUTS_RUNNER_ON_THIRD:
                return "Outs: 0, Runners: 3B";
            case NO_OUTS_RUNNER_ON_FIRST_AND_SECOND:
                return "Outs: 0, Runners: 1B, 2B";
            case NO_OUTS_RUNNER_ON_FIRST_AND_THIRD:
                return "Outs: 0, Runners: 1B, 3B";
            case NO_OUTS_RUNNER_ON_SECOND_AND_THIRD:
                return "Outs: 0, Runners: 2B, 3B";
            case NO_OUTS_BASES_LOADED:
                return "Outs: 0, Runners: 1B, 2B, 3B";

            case ONE_OUT_NOBODY_ON:
                return "Outs: 1, Runners: None";
            case ONE_OUT_RUNNER_ON_FIRST:
                return "Outs: 1, Runners: 1B";
            case ONE_OUT_RUNNER_ON_SECOND:
                return "Outs: 1, Runners: 2B";
            case ONE_OUT_RUNNER_ON_THIRD:
                return "Outs: 1, Runners: 3B";
            case ONE_OUT_RUNNER_ON_FIRST_AND_SECOND:
                return "Outs: 1, Runners: 1B, 2B";
            case ONE_OUT_RUNNER_ON_FIRST_AND_THIRD:
                return "Outs: 1, Runners: 1B, 3B";
            case ONE_OUT_RUNNER_ON_SECOND_AND_THIRD:
                return "Outs: 1, Runners: 2B, 3B";
            case ONE_OUT_BASES_LOADED:
                return "Outs: 1, Runners: 1B, 2B, 3B";

            case TWO_OUTS_NOBODY_ON:
                return "Outs: 2, Runners: None";
            case TWO_OUTS_RUNNER_ON_FIRST:
                return "Outs: 2, Runners: 1B";
            case TWO_OUTS_RUNNER_ON_SECOND:
                return "Outs: 2, Runners: 2B";
            case TWO_OUTS_RUNNER_ON_THIRD:
                return "Outs: 2, Runners: 3B";
            case TWO_OUTS_RUNNER_ON_FIRST_AND_SECOND:
                return "Outs: 2, Runners: 1B, 2B";
            case TWO_OUTS_RUNNER_ON_FIRST_AND_THIRD:
                return "Outs: 2, Runners: 1B, 3B";
            case TWO_OUTS_RUNNER_ON_SECOND_AND_THIRD:
                return "Outs: 2, Runners: 2B, 3B";
            case TWO_OUTS_BASES_LOADED:
                return "Outs: 2, Runners: 1B, 2B, 3B";

            case THREE_OUTS:
                return "Outs: 3, Inning Over";
            default:
                return "Game State Not Found";
        }
    }
}
