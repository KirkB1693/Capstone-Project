package com.example.android.baseballbythenumbers.Data;

public class Runner {

    private Player runner;

    private Player pitcherResponsible;

    private boolean earnedRun;

    public Runner (Player runner, Player pitcherResponsible, boolean earnedRun){
        this.runner = runner;
        this.pitcherResponsible = pitcherResponsible;
        this.earnedRun = earnedRun;
    }

    public Player getRunner() {
        return runner;
    }

    public Player getPitcherResponsible() {
        return pitcherResponsible;
    }

    public boolean isEarnedRun() {
        return earnedRun;
    }

    public void setRunner(Player runner) {
        this.runner = runner;
    }

    public void setPitcherResponsible(Player pitcherResponsible) {
        this.pitcherResponsible = pitcherResponsible;
    }

    public void setEarnedRun(boolean earnedRun) {
        this.earnedRun = earnedRun;
    }

}
