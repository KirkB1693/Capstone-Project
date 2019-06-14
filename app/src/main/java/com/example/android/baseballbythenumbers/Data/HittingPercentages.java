package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class HittingPercentages implements Parcelable
{

    @SerializedName("OSwingPct")
    @Expose
    @ColumnInfo(name = "batting_o_swing_pct")
    private int oSwingPct;
    @SerializedName("ZSwingPct")
    @Expose
    @ColumnInfo(name = "batting_z_swing_pct")
    private int zSwingPct;
    @SerializedName("OContactPct")
    @Expose
    @ColumnInfo(name = "batting_o_contact_pct")
    private int oContactPct;
    @SerializedName("ZContactPct")
    @Expose
    @ColumnInfo(name = "batting_z_contact_pct")
    private int zContactPct;
    @SerializedName("Speed")
    @Expose
    @ColumnInfo(name = "speed")
    private int speed;
    @SerializedName("LineDrivePct")
    @Expose
    @ColumnInfo(name = "batting_line_drive_pct")
    private int lineDrivePct;
    @SerializedName("GroundBallPct")
    @Expose
    @ColumnInfo(name = "batting_ground_ball_pct")
    private int groundBallPct;
    @SerializedName("hardPct")
    @Expose
    @ColumnInfo(name = "batting_hard_pct")
    private int hardPct;
    @SerializedName("medPct")
    @Expose
    @ColumnInfo(name = "batting_med_pct")
    private int medPct;
    @SerializedName("PullPct")
    @Expose
    @ColumnInfo(name = "batting_pull_pct")
    private int pullPct;
    @SerializedName("CenterPct")
    @Expose
    @ColumnInfo(name = "batting_center_pct")
    private int centerPct;
    @SerializedName("HomeRunPct")
    @Expose
    @ColumnInfo(name = "batting_home_run_pct")
    private int homeRunPct;
    @SerializedName("TriplePct")
    @Expose
    @ColumnInfo(name = "batting_triple_pct")
    private int triplePct;
    @SerializedName("DoublePct")
    @Expose
    @ColumnInfo(name = "batting_double_pct")
    private int doublePct;
    @SerializedName("StolenBasePct")
    @Expose
    @ColumnInfo(name = "batting_stolen_base_pct")
    private int stolenBasePct;
    @SerializedName("InfieldFlyBallPct")
    @Expose
    @ColumnInfo(name = "batting_infield_fly_pct")
    private int infieldFlyBallPct;
    @SerializedName("HitByPitchPct")
    @Expose
    @ColumnInfo(name = "batting_hit_by_pitch_pct")
    private int hitByPitchPct;
    @SerializedName("BattingAverageBallsInPlay")
    @Expose
    @ColumnInfo(name = "batting_BABIP")
    private int battingAverageBallsInPlay;
    @SerializedName("FoulBallPct")
    @Expose
    @ColumnInfo(name = "batting_foul_ball_pct")
    private int foulBallPct;
    @SerializedName("StolenBaseRate")
    @Expose
    @ColumnInfo(name = "batting_stolen_base_rate")
    private int stolenBaseRate;
    @SerializedName("BaseRunning")
    @Expose
    @ColumnInfo(name = "batting_baserunning")
    private int baseRunning;
    @SerializedName("ErrorPct")
    @Expose
    @ColumnInfo(name = "fielding_error_pct")
    private int errorPct;
    @SerializedName("Stamina")
    @Expose
    @ColumnInfo(name = "batting_stamina")
    private int stamina;
    @SerializedName("StaminaUsed")
    @Expose
    @ColumnInfo(name = "batting_stamina_used")
    private int staminaUsed;
    public final static Parcelable.Creator<HittingPercentages> CREATOR = new Creator<HittingPercentages>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HittingPercentages createFromParcel(Parcel in) {
            return new HittingPercentages(in);
        }

        public HittingPercentages[] newArray(int size) {
            return (new HittingPercentages[size]);
        }

    }
            ;

    protected HittingPercentages(Parcel in) {
        this.oSwingPct = ((int) in.readValue((int.class.getClassLoader())));
        this.zSwingPct = ((int) in.readValue((int.class.getClassLoader())));
        this.oContactPct = ((int) in.readValue((int.class.getClassLoader())));
        this.zContactPct = ((int) in.readValue((int.class.getClassLoader())));
        this.speed = ((int) in.readValue((int.class.getClassLoader())));
        this.lineDrivePct = ((int) in.readValue((int.class.getClassLoader())));
        this.groundBallPct = ((int) in.readValue((int.class.getClassLoader())));
        this.hardPct = ((int) in.readValue((int.class.getClassLoader())));
        this.medPct = ((int) in.readValue((int.class.getClassLoader())));
        this.pullPct = ((int) in.readValue((int.class.getClassLoader())));
        this.centerPct = ((int) in.readValue((int.class.getClassLoader())));
        this.homeRunPct = ((int) in.readValue((int.class.getClassLoader())));
        this.triplePct = ((int) in.readValue((int.class.getClassLoader())));
        this.doublePct = ((int) in.readValue((int.class.getClassLoader())));
        this.stolenBasePct = ((int) in.readValue((int.class.getClassLoader())));
        this.infieldFlyBallPct = ((int) in.readValue((int.class.getClassLoader())));
        this.hitByPitchPct = ((int) in.readValue((int.class.getClassLoader())));
        this.battingAverageBallsInPlay = ((int) in.readValue((int.class.getClassLoader())));
        this.foulBallPct = ((int) in.readValue((int.class.getClassLoader())));
        this.stolenBaseRate = ((int) in.readValue((int.class.getClassLoader())));
        this.baseRunning = ((int) in.readValue((int.class.getClassLoader())));
        this.errorPct = ((int) in.readValue((int.class.getClassLoader())));
        this.stamina = ((int) in.readValue((int.class.getClassLoader())));
        this.staminaUsed = ((int) in.readValue((int.class.getClassLoader())));
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(oSwingPct);
        dest.writeValue(zSwingPct);
        dest.writeValue(oContactPct);
        dest.writeValue(zContactPct);
        dest.writeValue(speed);
        dest.writeValue(lineDrivePct);
        dest.writeValue(groundBallPct);
        dest.writeValue(hardPct);
        dest.writeValue(medPct);
        dest.writeValue(pullPct);
        dest.writeValue(centerPct);
        dest.writeValue(homeRunPct);
        dest.writeValue(triplePct);
        dest.writeValue(doublePct);
        dest.writeValue(stolenBasePct);
        dest.writeValue(infieldFlyBallPct);
        dest.writeValue(hitByPitchPct);
        dest.writeValue(battingAverageBallsInPlay);
        dest.writeValue(foulBallPct);
        dest.writeValue(stolenBaseRate);
        dest.writeValue(baseRunning);
        dest.writeValue(errorPct);
        dest.writeValue(stamina);
        dest.writeValue(staminaUsed);
    }

    public int describeContents() {
        return 0;
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
}