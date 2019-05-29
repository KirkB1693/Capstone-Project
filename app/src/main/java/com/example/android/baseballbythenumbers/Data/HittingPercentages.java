package com.example.android.baseballbythenumbers.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HittingPercentages implements Parcelable
{

    @SerializedName("OSwingPct")
    @Expose
    private int oSwingPct;
    @SerializedName("ZSwingPct")
    @Expose
    private int zSwingPct;
    @SerializedName("OContactPct")
    @Expose
    private int oContactPct;
    @SerializedName("ZContactPct")
    @Expose
    private int zContactPct;
    @SerializedName("GroundBallPct")
    @Expose
    private int groundBallPct;
    @SerializedName("LineDrivePct")
    @Expose
    private int lineDrivePct;
    @SerializedName("HomeRunPct")
    @Expose
    private int homeRunPct;
    @SerializedName("TriplePct")
    @Expose
    private int triplePct;
    @SerializedName("DoublePct")
    @Expose
    private int doublePct;
    @SerializedName("StolenBasePct")
    @Expose
    private int stolenBasePct;
    @SerializedName("InfieldFlyBallPct")
    @Expose
    private int infieldFlyBallPct;
    @SerializedName("HitByPitchPct")
    @Expose
    private int hitByPitchPct;
    @SerializedName("BattingAverageBallsInPlay")
    @Expose
    private int battingAverageBallsInPlay;
    @SerializedName("FoulBallPct")
    @Expose
    private int foulBallPct;
    @SerializedName("PullPct")
    @Expose
    private int pullPct;
    @SerializedName("CenterPct")
    @Expose
    private int centerPct;
    @SerializedName("StolenBaseRate")
    @Expose
    private int stolenBaseRate;
    @SerializedName("BaseRunning")
    @Expose
    private int baseRunning;
    @SerializedName("Speed")
    @Expose
    private int speed;
    @SerializedName("ErrorPct")
    @Expose
    private int errorPct;
    @SerializedName("Stamina")
    @Expose
    private int stamina;
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
        this.groundBallPct = ((int) in.readValue((int.class.getClassLoader())));
        this.lineDrivePct = ((int) in.readValue((int.class.getClassLoader())));
        this.homeRunPct = ((int) in.readValue((int.class.getClassLoader())));
        this.triplePct = ((int) in.readValue((int.class.getClassLoader())));
        this.doublePct = ((int) in.readValue((int.class.getClassLoader())));
        this.stolenBasePct = ((int) in.readValue((int.class.getClassLoader())));
        this.infieldFlyBallPct = ((int) in.readValue((int.class.getClassLoader())));
        this.hitByPitchPct = ((int) in.readValue((int.class.getClassLoader())));
        this.battingAverageBallsInPlay = ((int) in.readValue((int.class.getClassLoader())));
        this.foulBallPct = ((int) in.readValue((int.class.getClassLoader())));
        this.pullPct = ((int) in.readValue((int.class.getClassLoader())));
        this.centerPct = ((int) in.readValue((int.class.getClassLoader())));
        this.stolenBaseRate = ((int) in.readValue((int.class.getClassLoader())));
        this.baseRunning = ((int) in.readValue((int.class.getClassLoader())));
        this.speed = ((int) in.readValue((int.class.getClassLoader())));
        this.errorPct = ((int) in.readValue((int.class.getClassLoader())));
        this.stamina = ((int) in.readValue((int.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public HittingPercentages() {
    }

    /**
     *
     * @param baseRunning
     * @param zContactPct
     * @param triplePct
     * @param hitByPitchPct
     * @param stamina
     * @param speed
     * @param errorPct
     * @param oSwingPct
     * @param groundBallPct
     * @param pullPct
     * @param stolenBasePct
     * @param oContactPct
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
    public HittingPercentages(int oSwingPct, int zSwingPct, int oContactPct, int zContactPct, int groundBallPct, int lineDrivePct, int homeRunPct, int triplePct, int doublePct, int stolenBasePct, int infieldFlyBallPct, int hitByPitchPct, int battingAverageBallsInPlay, int foulBallPct, int pullPct, int centerPct, int stolenBaseRate, int baseRunning, int speed, int errorPct, int stamina) {
        super();
        this.oSwingPct = oSwingPct;
        this.zSwingPct = zSwingPct;
        this.oContactPct = oContactPct;
        this.zContactPct = zContactPct;
        this.groundBallPct = groundBallPct;
        this.lineDrivePct = lineDrivePct;
        this.homeRunPct = homeRunPct;
        this.triplePct = triplePct;
        this.doublePct = doublePct;
        this.stolenBasePct = stolenBasePct;
        this.infieldFlyBallPct = infieldFlyBallPct;
        this.hitByPitchPct = hitByPitchPct;
        this.battingAverageBallsInPlay = battingAverageBallsInPlay;
        this.foulBallPct = foulBallPct;
        this.pullPct = pullPct;
        this.centerPct = centerPct;
        this.stolenBaseRate = stolenBaseRate;
        this.baseRunning = baseRunning;
        this.speed = speed;
        this.errorPct = errorPct;
        this.stamina = stamina;
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

    public int getGroundBallPct() {
        return groundBallPct;
    }

    public void setGroundBallPct(int groundBallPct) {
        this.groundBallPct = groundBallPct;
    }

    public int getLineDrivePct() {
        return lineDrivePct;
    }

    public void setLineDrivePct(int lineDrivePct) {
        this.lineDrivePct = lineDrivePct;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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

    @Override
    public String toString() {
        return "O Swing Pct : " + formatPct(oSwingPct) + ", Z Swing Pct : " + formatPct(zSwingPct) + ", O Contact Pct : " + formatPct(oContactPct) + ", Z Contact Pct : " + formatPct(zContactPct) +
                ", GB Pct : " + formatPct(groundBallPct) + ", LD Pct : " + formatPct(lineDrivePct) + ", HR Pct : " + formatPct(homeRunPct) + ", 3B Pct : " + formatPct(triplePct) +
                ", 2B Pct : " + formatPct(doublePct) + ", SB Pct : " + formatPct(stolenBasePct) + ", IFFB Pct : " + formatPct(infieldFlyBallPct) + ", HBP Pct : " + formatPct(hitByPitchPct) +
                ", BABIP Pct : " + formatPct(battingAverageBallsInPlay) + ", Foul Pct : " + formatPct(foulBallPct) + ", Pull Pct : " + formatPct(pullPct) + ", Center Pct : " + formatPct(centerPct) +
                ", SB Rate : " + formatPct(stolenBaseRate) + ", Baserunning : " + baseRunning/100 + ", Speed : " + speed/100 + ", Error Pct : " + errorPct + ", Fielding Stamina : " + stamina;
    }

    private String formatPct(int percentAsInteger) {
        double percent = ((double) percentAsInteger)/100.0;
        return percent + "%";
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(oSwingPct);
        dest.writeValue(zSwingPct);
        dest.writeValue(oContactPct);
        dest.writeValue(zContactPct);
        dest.writeValue(groundBallPct);
        dest.writeValue(lineDrivePct);
        dest.writeValue(homeRunPct);
        dest.writeValue(triplePct);
        dest.writeValue(doublePct);
        dest.writeValue(stolenBasePct);
        dest.writeValue(infieldFlyBallPct);
        dest.writeValue(hitByPitchPct);
        dest.writeValue(battingAverageBallsInPlay);
        dest.writeValue(foulBallPct);
        dest.writeValue(pullPct);
        dest.writeValue(centerPct);
        dest.writeValue(stolenBaseRate);
        dest.writeValue(baseRunning);
        dest.writeValue(speed);
        dest.writeValue(errorPct);
        dest.writeValue(stamina);
    }

    public int describeContents() {
        return 0;
    }

}