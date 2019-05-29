package com.example.android.baseballbythenumbers.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PitchingPercentages implements Parcelable
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
    @SerializedName("InfieldFlyBallPct")
    @Expose
    private int infieldFlyBallPct;
    @SerializedName("HitByPitchPct")
    @Expose
    private int hitByPitchPct;
    @SerializedName("WildPitchPct")
    @Expose
    private int wildPitchPct;
    @SerializedName("BalkPct")
    @Expose
    private int balkPct;
    @SerializedName("ZonePct")
    @Expose
    private int zonePct;
    @SerializedName("FirstPitchStrikePct")
    @Expose
    private int firstPitchStrikePct;
    @SerializedName("FastballPct")
    @Expose
    private int fastballPct;
    @SerializedName("SliderPct")
    @Expose
    private int sliderPct;
    @SerializedName("CutterPct")
    @Expose
    private int cutterPct;
    @SerializedName("CurveballPct")
    @Expose
    private int curveballPct;
    @SerializedName("ChangeUpPct")
    @Expose
    private int changeUpPct;
    @SerializedName("SplitFingerPct")
    @Expose
    private int splitFingerPct;
    @SerializedName("KnuckleballPct")
    @Expose
    private int knuckleballPct;
    @SerializedName("pitchingStamina")
    @Expose
    private int pitchingStamina;
    public final static Parcelable.Creator<PitchingPercentages> CREATOR = new Creator<PitchingPercentages>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PitchingPercentages createFromParcel(Parcel in) {
            return new PitchingPercentages(in);
        }

        public PitchingPercentages[] newArray(int size) {
            return (new PitchingPercentages[size]);
        }

    }
            ;

    protected PitchingPercentages(Parcel in) {
        this.oSwingPct = ((int) in.readValue((int.class.getClassLoader())));
        this.zSwingPct = ((int) in.readValue((int.class.getClassLoader())));
        this.oContactPct = ((int) in.readValue((int.class.getClassLoader())));
        this.zContactPct = ((int) in.readValue((int.class.getClassLoader())));
        this.groundBallPct = ((int) in.readValue((int.class.getClassLoader())));
        this.lineDrivePct = ((int) in.readValue((int.class.getClassLoader())));
        this.homeRunPct = ((int) in.readValue((int.class.getClassLoader())));
        this.infieldFlyBallPct = ((int) in.readValue((int.class.getClassLoader())));
        this.hitByPitchPct = ((int) in.readValue((int.class.getClassLoader())));
        this.wildPitchPct = ((int) in.readValue((int.class.getClassLoader())));
        this.balkPct = ((int) in.readValue((int.class.getClassLoader())));
        this.zonePct = ((int) in.readValue((int.class.getClassLoader())));
        this.firstPitchStrikePct = ((int) in.readValue((int.class.getClassLoader())));
        this.fastballPct = ((int) in.readValue((int.class.getClassLoader())));
        this.sliderPct = ((int) in.readValue((int.class.getClassLoader())));
        this.cutterPct = ((int) in.readValue((int.class.getClassLoader())));
        this.curveballPct = ((int) in.readValue((int.class.getClassLoader())));
        this.changeUpPct = ((int) in.readValue((int.class.getClassLoader())));
        this.splitFingerPct = ((int) in.readValue((int.class.getClassLoader())));
        this.knuckleballPct = ((int) in.readValue((int.class.getClassLoader())));
        this.pitchingStamina = ((int) in.readValue((int.class.getClassLoader())));
    }

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
     * @param pitchingStamina
     * @param cutterPct
     * @param oSwingPct
     * @param groundBallPct
     * @param oContactPct
     * @param knuckleballPct
     * @param zSwingPct
     * @param sliderPct
     * @param lineDrivePct
     * @param changeUpPct
     * @param wildPitchPct
     * @param zonePct
     * @param curveballPct
     * @param infieldFlyBallPct
     * @param homeRunPct
     * @param splitFingerPct
     * @param firstPitchStrikePct
     * @param fastballPct
     */
    public PitchingPercentages(int oSwingPct, int zSwingPct, int oContactPct, int zContactPct, int groundBallPct, int lineDrivePct, int homeRunPct, int infieldFlyBallPct, int hitByPitchPct, int wildPitchPct, int balkPct, int zonePct, int firstPitchStrikePct, int fastballPct, int sliderPct, int cutterPct, int curveballPct, int changeUpPct, int splitFingerPct, int knuckleballPct, int pitchingStamina) {
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

    public int getpitchingStamina() {
        return pitchingStamina;
    }

    public void setpitchingStamina(int pitchingStamina) {
        this.pitchingStamina = pitchingStamina;
    }


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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(oSwingPct);
        dest.writeValue(zSwingPct);
        dest.writeValue(oContactPct);
        dest.writeValue(zContactPct);
        dest.writeValue(groundBallPct);
        dest.writeValue(lineDrivePct);
        dest.writeValue(homeRunPct);
        dest.writeValue(infieldFlyBallPct);
        dest.writeValue(hitByPitchPct);
        dest.writeValue(wildPitchPct);
        dest.writeValue(balkPct);
        dest.writeValue(zonePct);
        dest.writeValue(firstPitchStrikePct);
        dest.writeValue(fastballPct);
        dest.writeValue(sliderPct);
        dest.writeValue(cutterPct);
        dest.writeValue(curveballPct);
        dest.writeValue(changeUpPct);
        dest.writeValue(splitFingerPct);
        dest.writeValue(knuckleballPct);
        dest.writeValue(pitchingStamina);
    }

    public int describeContents() {
        return 0;
    }

}