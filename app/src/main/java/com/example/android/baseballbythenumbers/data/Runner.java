package com.example.android.baseballbythenumbers.data;

public class Runner {

    private Player runner;

    private BattingLine battingLineForRunner;

    private Player pitcherResponsible;

    private PitchingLine pitchingLineForPitcherResponsible;

    private boolean earnedRun;

    public Runner (Player runner, BattingLine battingLineForRunner, Player pitcherResponsible, PitchingLine pitchingLineForPitcherResponsible, boolean earnedRun){
        this.runner = runner;
        this.battingLineForRunner = battingLineForRunner;
        this.pitcherResponsible = pitcherResponsible;
        this.pitchingLineForPitcherResponsible = pitchingLineForPitcherResponsible;
        this.earnedRun = earnedRun;
    }

    public Player getRunner() {
        return runner;
    }

    public BattingLine getBattingLineForRunner() {
        return battingLineForRunner;
    }

    public Player getPitcherResponsible() {
        return pitcherResponsible;
    }

    public PitchingLine getPitchingLineForPitcherResponsible() {
        return pitchingLineForPitcherResponsible;
    }

    public boolean isEarnedRun() {
        return earnedRun;
    }

    public void setRunner(Player runner) {
        this.runner = runner;
    }

    public void setBattingLineForRunner(BattingLine battingLineForRunner) {
        this.battingLineForRunner = battingLineForRunner;
    }

    public void setPitcherResponsible(Player pitcherResponsible) {
        this.pitcherResponsible = pitcherResponsible;
    }

    public void setPitchingLineForPitcherResponsible(PitchingLine pitchingLineForPitcherResponsible) {
        this.pitchingLineForPitcherResponsible = pitchingLineForPitcherResponsible;
    }

    public void setEarnedRun(boolean earnedRun) {
        this.earnedRun = earnedRun;
    }

}
