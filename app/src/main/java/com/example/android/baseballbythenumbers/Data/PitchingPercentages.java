package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

public class PitchingPercentages implements Parcelable
{

    private int oSwingPct;

    private int zSwingPct;

    private int oContactPct;

    private int zContactPct;

    private int groundBallPct;

    private int lineDrivePct;

    private int homeRunPct;

    private int infieldFlyBallPct;

    private int hitByPitchPct;

    private int wildPitchPct;

    private int balkPct;

    private int zonePct;

    private int firstPitchStrikePct;

    private int fastballPct;

    private int sliderPct;

    private int cutterPct;

    private int curveballPct;

    private int changeUpPct;

    private int splitFingerPct;

    private int knuckleballPct;

    private int pitchingStamina;

    private int pitchingStaminaUsed;

    /**
     * No args constructor for use in serialization
     *
     */
    public PitchingPercentages() {
    }

    /**
     *
     * @param balkPct
     * @param zContactPct
     * @param hitByPitchPct
     * @param cutterPct
     * @param oSwingPct
     * @param groundBallPct
     * @param pitchingStaminaUsed
     * @param oContactPct
     * @param knuckleballPct
     * @param zSwingPct
     * @param sliderPct
     * @param lineDrivePct
     * @param changeUpPct
     * @param wildPitchPct
     * @param zonePct
     * @param curveballPct
     * @param pitchingStamina
     * @param infieldFlyBallPct
     * @param homeRunPct
     * @param splitFingerPct
     * @param firstPitchStrikePct
     * @param fastballPct
     */
    @Ignore
    public PitchingPercentages(int oSwingPct, int zSwingPct, int oContactPct, int zContactPct, int groundBallPct, int lineDrivePct, int homeRunPct, int infieldFlyBallPct, int hitByPitchPct, int wildPitchPct, int balkPct, int zonePct, int firstPitchStrikePct, int fastballPct, int sliderPct, int cutterPct, int curveballPct, int changeUpPct, int splitFingerPct, int knuckleballPct, int pitchingStamina, int pitchingStaminaUsed) {
        super();
        this.oSwingPct = oSwingPct;
        this.zSwingPct = zSwingPct;
        this.oContactPct = oContactPct;
        this.zContactPct = zContactPct;
        this.groundBallPct = groundBallPct;
        this.lineDrivePct = lineDrivePct;
        this.homeRunPct = homeRunPct;
        this.infieldFlyBallPct = infieldFlyBallPct;
        this.hitByPitchPct = hitByPitchPct;
        this.wildPitchPct = wildPitchPct;
        this.balkPct = balkPct;
        this.zonePct = zonePct;
        this.firstPitchStrikePct = firstPitchStrikePct;
        this.fastballPct = fastballPct;
        this.sliderPct = sliderPct;
        this.cutterPct = cutterPct;
        this.curveballPct = curveballPct;
        this.changeUpPct = changeUpPct;
        this.splitFingerPct = splitFingerPct;
        this.knuckleballPct = knuckleballPct;
        this.pitchingStamina = pitchingStamina;
        this.pitchingStaminaUsed = pitchingStaminaUsed;
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

    public int getWildPitchPct() {
        return wildPitchPct;
    }

    public void setWildPitchPct(int wildPitchPct) {
        this.wildPitchPct = wildPitchPct;
    }

    public int getBalkPct() {
        return balkPct;
    }

    public void setBalkPct(int balkPct) {
        this.balkPct = balkPct;
    }

    public int getZonePct() {
        return zonePct;
    }

    public void setZonePct(int zonePct) {
        this.zonePct = zonePct;
    }

    public int getFirstPitchStrikePct() {
        return firstPitchStrikePct;
    }

    public void setFirstPitchStrikePct(int firstPitchStrikePct) {
        this.firstPitchStrikePct = firstPitchStrikePct;
    }

    public int getFastballPct() {
        return fastballPct;
    }

    public void setFastballPct(int fastballPct) {
        this.fastballPct = fastballPct;
    }

    public int getSliderPct() {
        return sliderPct;
    }

    public void setSliderPct(int sliderPct) {
        this.sliderPct = sliderPct;
    }

    public int getCutterPct() {
        return cutterPct;
    }

    public void setCutterPct(int cutterPct) {
        this.cutterPct = cutterPct;
    }

    public int getCurveballPct() {
        return curveballPct;
    }

    public void setCurveballPct(int curveballPct) {
        this.curveballPct = curveballPct;
    }

    public int getChangeUpPct() {
        return changeUpPct;
    }

    public void setChangeUpPct(int changeUpPct) {
        this.changeUpPct = changeUpPct;
    }

    public int getSplitFingerPct() {
        return splitFingerPct;
    }

    public void setSplitFingerPct(int splitFingerPct) {
        this.splitFingerPct = splitFingerPct;
    }

    public int getKnuckleballPct() {
        return knuckleballPct;
    }

    public void setKnuckleballPct(int knuckleballPct) {
        this.knuckleballPct = knuckleballPct;
    }

    public int getPitchingStamina() {
        return pitchingStamina;
    }

    public void setPitchingStamina(int pitchingStamina) {
        this.pitchingStamina = pitchingStamina;
    }

    public int getPitchingStaminaUsed() {
        return pitchingStaminaUsed;
    }

    public void setPitchingStaminaUsed(int pitchingStaminaUsed) {
        this.pitchingStaminaUsed = pitchingStaminaUsed;
    }


    @NotNull
    @Override
    public String toString() {
        return "O Swing Pct : " + formatPct(oSwingPct) + ", Z Swing Pct : " + formatPct(zSwingPct) + ", O Contact Pct : " + formatPct(oContactPct) + ", Z Contact Pct : " + formatPct(zContactPct) +
                ", GB Pct : " + formatPct(groundBallPct) + ", LD Pct : " + formatPct(lineDrivePct) + ", HR Pct : " + formatPct(homeRunPct) + ", 3B Pct : " +
                ", IFFB Pct : " + formatPct(infieldFlyBallPct) + ", HBP Pct : " + formatPct(hitByPitchPct) + ", WP Pct : " + formatPct(wildPitchPct) + ", Balk Pct : " + formatPct(balkPct) +
                ", Zone Pct : " + formatPct(zonePct) + ", F-Strike Pct : " + formatPct(firstPitchStrikePct) + ", Fastball Pct : " + formatPct(fastballPct) + ", Slider Pct : " + formatPct(sliderPct) +
                ", Cutter Pct : " + formatPct(cutterPct) + ", Curve Pct : " + formatPct(curveballPct) + ", Change Up Pct : " + formatPct(changeUpPct) + ", Splitter Pct : " + formatPct(splitFingerPct) +
                ", Knuckler Pct : " + formatPct(knuckleballPct) + ", Pitching Stamina : " + pitchingStamina/100;
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
        dest.writeInt(this.groundBallPct);
        dest.writeInt(this.lineDrivePct);
        dest.writeInt(this.homeRunPct);
        dest.writeInt(this.infieldFlyBallPct);
        dest.writeInt(this.hitByPitchPct);
        dest.writeInt(this.wildPitchPct);
        dest.writeInt(this.balkPct);
        dest.writeInt(this.zonePct);
        dest.writeInt(this.firstPitchStrikePct);
        dest.writeInt(this.fastballPct);
        dest.writeInt(this.sliderPct);
        dest.writeInt(this.cutterPct);
        dest.writeInt(this.curveballPct);
        dest.writeInt(this.changeUpPct);
        dest.writeInt(this.splitFingerPct);
        dest.writeInt(this.knuckleballPct);
        dest.writeInt(this.pitchingStamina);
        dest.writeInt(this.pitchingStaminaUsed);
    }

    protected PitchingPercentages(Parcel in) {
        this.oSwingPct = in.readInt();
        this.zSwingPct = in.readInt();
        this.oContactPct = in.readInt();
        this.zContactPct = in.readInt();
        this.groundBallPct = in.readInt();
        this.lineDrivePct = in.readInt();
        this.homeRunPct = in.readInt();
        this.infieldFlyBallPct = in.readInt();
        this.hitByPitchPct = in.readInt();
        this.wildPitchPct = in.readInt();
        this.balkPct = in.readInt();
        this.zonePct = in.readInt();
        this.firstPitchStrikePct = in.readInt();
        this.fastballPct = in.readInt();
        this.sliderPct = in.readInt();
        this.cutterPct = in.readInt();
        this.curveballPct = in.readInt();
        this.changeUpPct = in.readInt();
        this.splitFingerPct = in.readInt();
        this.knuckleballPct = in.readInt();
        this.pitchingStamina = in.readInt();
        this.pitchingStaminaUsed = in.readInt();
    }

    public static final Creator<PitchingPercentages> CREATOR = new Creator<PitchingPercentages>() {
        @Override
        public PitchingPercentages createFromParcel(Parcel source) {
            return new PitchingPercentages(source);
        }

        @Override
        public PitchingPercentages[] newArray(int size) {
            return new PitchingPercentages[size];
        }
    };
}