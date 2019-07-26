package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

public class HittingPercentages implements Parcelable
{

    @ColumnInfo(name = "batting_o_swing_pct")
    private int oSwingPct;

    @ColumnInfo(name = "batting_z_swing_pct")
    private int zSwingPct;

    @ColumnInfo(name = "batting_o_contact_pct")
    private int oContactPct;

    @ColumnInfo(name = "batting_z_contact_pct")
    private int zContactPct;

    @ColumnInfo(name = "speed")
    private int speed;

    @ColumnInfo(name = "batting_line_drive_pct")
    private int lineDrivePct;

    @ColumnInfo(name = "batting_ground_ball_pct")
    private int groundBallPct;

    @ColumnInfo(name = "batting_hard_pct")
    private int hardPct;

    @ColumnInfo(name = "batting_med_pct")
    private int medPct;

    @ColumnInfo(name = "batting_pull_pct")
    private int pullPct;

    @ColumnInfo(name = "batting_center_pct")
    private int centerPct;

    @ColumnInfo(name = "batting_home_run_pct")
    private int homeRunPct;

    @ColumnInfo(name = "batting_triple_pct")
    private int triplePct;

    @ColumnInfo(name = "batting_double_pct")
    private int doublePct;

    @ColumnInfo(name = "batting_stolen_base_pct")
    private int stolenBasePct;

    @ColumnInfo(name = "batting_infield_fly_pct")
    private int infieldFlyBallPct;

    @ColumnInfo(name = "batting_hit_by_pitch_pct")
    private int hitByPitchPct;

    @ColumnInfo(name = "batting_BABIP")
    private int battingAverageBallsInPlay;

    @ColumnInfo(name = "batting_foul_ball_pct")
    private int foulBallPct;

    @ColumnInfo(name = "batting_stolen_base_rate")
    private int stolenBaseRate;

    @ColumnInfo(name = "batting_baserunning")
    private int baseRunning;

    @ColumnInfo(name = "fielding_error_pct")
    private int errorPct;

    @ColumnInfo(name = "batting_stamina")
    private int stamina;

    @ColumnInfo(name = "batting_stamina_used")
    private int staminaUsed;

    /**
     * No args constructor for use in serialization
     *
     */
    public HittingPercentages() {
    }

    /**
     *
     * @param staminaUsed
     * @param baseRunning
     * @param triplePct
     * @param zContactPct
     * @param hitByPitchPct
     * @param stamina
     * @param medPct
     * @param speed
     * @param errorPct
     * @param oSwingPct
     * @param groundBallPct
     * @param pullPct
     * @param stolenBasePct
     * @param oContactPct
     * @param hardPct
     * @param zSwingPct
     * @param lineDrivePct
     * @param centerPct
     * @param doublePct
     * @param stolenBaseRate
     * @param infieldFlyBallPct
     * @param homeRunPct
     * @param foulBallPct
     * @param battingAverageBallsInPlay
     */
    @Ignore
    public HittingPercentages(int oSwingPct, int zSwingPct, int oContactPct, int zContactPct, int speed, int lineDrivePct, int groundBallPct, int hardPct, int medPct, int pullPct, int centerPct, int homeRunPct, int triplePct, int doublePct, int stolenBasePct, int infieldFlyBallPct, int hitByPitchPct, int battingAverageBallsInPlay, int foulBallPct, int stolenBaseRate, int baseRunning, int errorPct, int stamina, int staminaUsed) {
        super();
        this.oSwingPct = oSwingPct;
        this.zSwingPct = zSwingPct;
        this.oContactPct = oContactPct;
        this.zContactPct = zContactPct;
        this.speed = speed;
        this.lineDrivePct = lineDrivePct;
        this.groundBallPct = groundBallPct;
        this.hardPct = hardPct;
        this.medPct = medPct;
        this.pullPct = pullPct;
        this.centerPct = centerPct;
        this.homeRunPct = homeRunPct;
        this.triplePct = triplePct;
        this.doublePct = doublePct;
        this.stolenBasePct = stolenBasePct;
        this.infieldFlyBallPct = infieldFlyBallPct;
        this.hitByPitchPct = hitByPitchPct;
        this.battingAverageBallsInPlay = battingAverageBallsInPlay;
        this.foulBallPct = foulBallPct;
        this.stolenBaseRate = stolenBaseRate;
        this.baseRunning = baseRunning;
        this.errorPct = errorPct;
        this.stamina = stamina;
        this.staminaUsed = staminaUsed;
    }

    public int getOSwingPct() {
        return oSwingPct;
    }

    public void setOSwingPct(int oSwingPct) {
        this.oSwingPct = oSwingPct;
    }

    public int getZSwingPct() {
        return zSwingPct;
    }

    public void setZSwingPct(int zSwingPct) {
        this.zSwingPct = zSwingPct;
    }

    public int getOContactPct() {
        return oContactPct;
    }

    public void setOContactPct(int oContactPct) {
        this.oContactPct = oContactPct;
    }

    public int getZContactPct() {
        return zContactPct;
    }

    public void setZContactPct(int zContactPct) {
        this.zContactPct = zContactPct;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLineDrivePct() {
        return lineDrivePct;
    }

    public void setLineDrivePct(int lineDrivePct) {
        this.lineDrivePct = lineDrivePct;
    }

    public int getGroundBallPct() {
        return groundBallPct;
    }

    public void setGroundBallPct(int groundBallPct) {
        this.groundBallPct = groundBallPct;
    }

    public int getHardPct() {
        return hardPct;
    }

    public void setHardPct(int hardPct) {
        this.hardPct = hardPct;
    }

    public int getMedPct() {
        return medPct;
    }

    public void setMedPct(int medPct) {
        this.medPct = medPct;
    }

    public int getPullPct() {
        return pullPct;
    }

    public void setPullPct(int pullPct) {
        this.pullPct = pullPct;
    }

    public int getCenterPct() {
        return centerPct;
    }

    public void setCenterPct(int centerPct) {
        this.centerPct = centerPct;
    }

    public int getHomeRunPct() {
        return homeRunPct;
    }

    public void setHomeRunPct(int homeRunPct) {
        this.homeRunPct = homeRunPct;
    }

    public int getTriplePct() {
        return triplePct;
    }

    public void setTriplePct(int triplePct) {
        this.triplePct = triplePct;
    }

    public int getDoublePct() {
        return doublePct;
    }

    public void setDoublePct(int doublePct) {
        this.doublePct = doublePct;
    }

    public int getStolenBasePct() {
        return stolenBasePct;
    }

    public void setStolenBasePct(int stolenBasePct) {
        this.stolenBasePct = stolenBasePct;
    }

    public int getInfieldFlyBallPct() {
        return infieldFlyBallPct;
    }

    public void setInfieldFlyBallPct(int infieldFlyBallPct) {
        this.infieldFlyBallPct = infieldFlyBallPct;
    }

    public int getHitByPitchPct() {
        return hitByPitchPct;
    }

    public void setHitByPitchPct(int hitByPitchPct) {
        this.hitByPitchPct = hitByPitchPct;
    }

    public int getBattingAverageBallsInPlay() {
        return battingAverageBallsInPlay;
    }

    public void setBattingAverageBallsInPlay(int battingAverageBallsInPlay) {
        this.battingAverageBallsInPlay = battingAverageBallsInPlay;
    }

    public int getFoulBallPct() {
        return foulBallPct;
    }

    public void setFoulBallPct(int foulBallPct) {
        this.foulBallPct = foulBallPct;
    }

    public int getStolenBaseRate() {
        return stolenBaseRate;
    }

    public void setStolenBaseRate(int stolenBaseRate) {
        this.stolenBaseRate = stolenBaseRate;
    }

    public int getBaseRunning() {
        return baseRunning;
    }

    public void setBaseRunning(int baseRunning) {
        this.baseRunning = baseRunning;
    }

    public int getErrorPct() {
        return errorPct;
    }

    public void setErrorPct(int errorPct) {
        this.errorPct = errorPct;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getStaminaUsed() {
        return staminaUsed;
    }

    public void setStaminaUsed(int staminaUsed) {
        this.staminaUsed = staminaUsed;
    }

    @NotNull
    @Override
    public String toString() {
        return "O Swing Pct : " + formatPct(oSwingPct) + ", Z Swing Pct : " + formatPct(zSwingPct) + ", O Contact Pct : " + formatPct(oContactPct) + ", Z Contact Pct : " + formatPct(zContactPct) +
                ", GB Pct : " + formatPct(groundBallPct) + ", LD Pct : " + formatPct(lineDrivePct) + ", Hard Hit% : " + formatPct(hardPct) + ", Med Hit% : " + formatPct(medPct) +
                ", HR Pct : " + formatPct(homeRunPct) + ", 3B Pct : " + formatPct(triplePct) +
                ", 2B Pct : " + formatPct(doublePct) + ", SB Pct : " + formatPct(stolenBasePct) + ", IFFB Pct : " + formatPct(infieldFlyBallPct) + ", HBP Pct : " + formatPct(hitByPitchPct) +
                ", BABIP Pct : " + formatPct(battingAverageBallsInPlay) + ", Foul Pct : " + formatPct(foulBallPct) + ", Pull Pct : " + formatPct(pullPct) + ", Center Pct : " + formatPct(centerPct) +
                ", SB Rate : " + formatPct(stolenBaseRate) + ", Baserunning : " + baseRunning/100 + ", Speed : " + speed/100 + ", Error Pct : " + formatPct(errorPct) + ", Fielding Stamina : " + stamina;
    }

    private String formatPct(int percentAsInteger) {
        double percent = ((double) percentAsInteger)/100.0;
        return percent + "%";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.oSwingPct);
        dest.writeInt(this.zSwingPct);
        dest.writeInt(this.oContactPct);
        dest.writeInt(this.zContactPct);
        dest.writeInt(this.speed);
        dest.writeInt(this.lineDrivePct);
        dest.writeInt(this.groundBallPct);
        dest.writeInt(this.hardPct);
        dest.writeInt(this.medPct);
        dest.writeInt(this.pullPct);
        dest.writeInt(this.centerPct);
        dest.writeInt(this.homeRunPct);
        dest.writeInt(this.triplePct);
        dest.writeInt(this.doublePct);
        dest.writeInt(this.stolenBasePct);
        dest.writeInt(this.infieldFlyBallPct);
        dest.writeInt(this.hitByPitchPct);
        dest.writeInt(this.battingAverageBallsInPlay);
        dest.writeInt(this.foulBallPct);
        dest.writeInt(this.stolenBaseRate);
        dest.writeInt(this.baseRunning);
        dest.writeInt(this.errorPct);
        dest.writeInt(this.stamina);
        dest.writeInt(this.staminaUsed);
    }

    protected HittingPercentages(Parcel in) {
        this.oSwingPct = in.readInt();
        this.zSwingPct = in.readInt();
        this.oContactPct = in.readInt();
        this.zContactPct = in.readInt();
        this.speed = in.readInt();
        this.lineDrivePct = in.readInt();
        this.groundBallPct = in.readInt();
        this.hardPct = in.readInt();
        this.medPct = in.readInt();
        this.pullPct = in.readInt();
        this.centerPct = in.readInt();
        this.homeRunPct = in.readInt();
        this.triplePct = in.readInt();
        this.doublePct = in.readInt();
        this.stolenBasePct = in.readInt();
        this.infieldFlyBallPct = in.readInt();
        this.hitByPitchPct = in.readInt();
        this.battingAverageBallsInPlay = in.readInt();
        this.foulBallPct = in.readInt();
        this.stolenBaseRate = in.readInt();
        this.baseRunning = in.readInt();
        this.errorPct = in.readInt();
        this.stamina = in.readInt();
        this.staminaUsed = in.readInt();
    }

    public static final Creator<HittingPercentages> CREATOR = new Creator<HittingPercentages>() {
        @Override
        public HittingPercentages createFromParcel(Parcel source) {
            return new HittingPercentages(source);
        }

        @Override
        public HittingPercentages[] newArray(int size) {
            return new HittingPercentages[size];
        }
    };
}