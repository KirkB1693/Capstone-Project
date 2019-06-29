package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "pitching_line", foreignKeys = @ForeignKey(entity = BoxScore.class, parentColumns = "box_score_id", childColumns = "box_score_id", onDelete = CASCADE), indices = @Index(value = "box_score_id"))
public class PitchingLine implements Parcelable{

    @SerializedName("boxScoreId")
    @Expose
    @ColumnInfo(name = "box_score_id")
    private String boxScoreId;

    @SerializedName("pitchingLineId")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "pitching_line_id")
    private String pitchingLineId;

    @SerializedName("pitcherNumber")
    @Expose
    @ColumnInfo(name = "pitcher_number")
    private int pitcherNumber;

    @SerializedName("pitcherName")
    @Expose
    @ColumnInfo(name = "pitcher_name")
    private String pitcherName;

    @SerializedName("inningsPitched")
    @Expose
    @ColumnInfo(name = "innings_pitched")
    private float inningsPitched;

    @SerializedName("hitsAllowed")
    @Expose
    @ColumnInfo(name = "hits_allowed")
    private int hitsAllowed;

    @SerializedName("runsAllowed")
    @Expose
    @ColumnInfo(name = "runs_allowed")
    private int runsAllowed;

    @SerializedName("earnedRuns")
    @Expose
    @ColumnInfo(name = "earned_runs")
    private int earnedRuns;

    @SerializedName("walksAllowed")
    @Expose
    @ColumnInfo(name = "walks_allowed")
    private int walksAllowed;

    @SerializedName("strikeOutsMade")
    @Expose
    @ColumnInfo(name = "strike_outs_made")
    private int strikeOutsMade;

    @SerializedName("homeRunsAllowed")
    @Expose
    @ColumnInfo(name = "home_runs_allowed")
    private int homeRunsAllowed;

    @SerializedName("era")
    @Expose
    private double era;

    @SerializedName("whip")
    @Expose
    private double whip;

    @SerializedName("pitchesThrown")
    @Expose
    @ColumnInfo(name = "pitches_thrown")
    private int pitchesThrown;

    @SerializedName("strikesThrown")
    @Expose
    @ColumnInfo(name = "strikes_thrown")
    private int strikesThrown;

    public final static Parcelable.Creator<PitchingLine> CREATOR = new Creator<PitchingLine>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PitchingLine createFromParcel(Parcel in) {
            return new PitchingLine(in);
        }

        public PitchingLine[] newArray(int size) {
            return (new PitchingLine[size]);
        }

    }
            ;

    protected PitchingLine(Parcel in) {
        this.boxScoreId = ((String) in.readValue((String.class.getClassLoader())));
        this.pitchingLineId = ((String) in.readValue((String.class.getClassLoader())));
        this.pitcherNumber = ((int) in.readValue((int.class.getClassLoader())));
        this.pitcherName = ((String) in.readValue((String.class.getClassLoader())));
        this.inningsPitched = ((float) in.readValue((float.class.getClassLoader())));
        this.hitsAllowed = ((int) in.readValue((int.class.getClassLoader())));
        this.runsAllowed = ((int) in.readValue((int.class.getClassLoader())));
        this.earnedRuns = ((int) in.readValue((int.class.getClassLoader())));
        this.walksAllowed = ((int) in.readValue((int.class.getClassLoader())));
        this.strikeOutsMade = ((int) in.readValue((int.class.getClassLoader())));
        this.homeRunsAllowed = ((int) in.readValue((int.class.getClassLoader())));
        this.era = ((double) in.readValue((double.class.getClassLoader())));
        this.whip = ((double) in.readValue((double.class.getClassLoader())));
        this.pitchesThrown = ((int) in.readValue((int.class.getClassLoader())));
        this.strikesThrown = ((int) in.readValue((int.class.getClassLoader())));
    }




    public PitchingLine(){}

    @Ignore
    public PitchingLine(String boxScoreId, int pitcherNumber, String pitcherName){
        this.boxScoreId = boxScoreId;
        this.pitcherNumber = pitcherNumber;
        this.pitcherName = pitcherName;
        this.pitchingLineId = UUID.randomUUID().toString();
    }

    public String getBoxScoreId() {
        return boxScoreId;
    }

    public void setBoxScoreId(String boxScoreId) {
        this.boxScoreId = boxScoreId;
    }

    public double getEra() {
        return era;
    }

    public void setEra(double era) {
        this.era = era;
    }

    public double getWhip() {
        return whip;
    }

    public void setWhip(double whip) {
        this.whip = whip;
    }

    public int getEarnedRuns() {
        return earnedRuns;
    }

    public void setEarnedRuns(int earnedRuns) {
        this.earnedRuns = earnedRuns;
    }

    public int getHitsAllowed() {
        return hitsAllowed;
    }

    public void setHitsAllowed(int hitsAllowed) {
        this.hitsAllowed = hitsAllowed;
    }

    public void incrementHitsAllowed() {
        hitsAllowed ++;
    }

    public int getHomeRunsAllowed() {
        return homeRunsAllowed;
    }

    public void setHomeRunsAllowed(int homeRunsAllowed) {
        this.homeRunsAllowed = homeRunsAllowed;
    }

    public void incrementHomeRunsAllowed() {
        homeRunsAllowed ++;
    }

    public float getInningsPitched() {
        return inningsPitched;
    }

    public void setInningsPitched(float inningsPitched) {
        this.inningsPitched = inningsPitched;
    }

    public int getPitcherNumber() {
        return pitcherNumber;
    }

    public void setPitcherNumber(int pitcherNumber) {
        this.pitcherNumber = pitcherNumber;
    }

    public String getPitchingLineId() {
        return pitchingLineId;
    }

    public void setPitchingLineId(String pitchingLineId) {
        this.pitchingLineId = pitchingLineId;
    }

    public int getRunsAllowed() {
        return runsAllowed;
    }

    public void setRunsAllowed(int runsAllowed) {
        this.runsAllowed = runsAllowed;
    }

    public int getWalksAllowed() {
        return walksAllowed;
    }

    public void setWalksAllowed(int walksAllowed) {
        this.walksAllowed = walksAllowed;
    }

    public void incrementWalksAllowed() {
        walksAllowed ++;
    }

    public int getStrikeOutsMade() {
        return strikeOutsMade;
    }

    public void setStrikeOutsMade(int strikeOutsMade) {
        this.strikeOutsMade = strikeOutsMade;
    }

    public void incrementStirkeOutsMade() {
        strikeOutsMade ++;
    }

    public String getPitcherName() {
        return pitcherName;
    }

    public void setPitcherName(String pitcherName) {
        this.pitcherName = pitcherName;
    }

    public int getPitchesThrown() {
        return pitchesThrown;
    }

    public void setPitchesThrown(int pitchesThrown) {
        this.pitchesThrown = pitchesThrown;
    }

    public void incrementPitchesThrown() {
        pitchesThrown ++;
    }

    public int getStrikesThrown() {
        return strikesThrown;
    }

    public void setStrikesThrown(int strikesThrown) {
        this.strikesThrown = strikesThrown;
    }

    public void incrementStrikesThrown() {
        strikesThrown ++;
    }

    public void addToInningsPitched(int outs) {
        for (int i = 0; i < outs; i++) {
            inningsPitched += .1;
            if (Math.round(inningsPitched * 100) > (((Math.round(inningsPitched) * 100) + 25))) {
                inningsPitched = inningsPitched + 1.0f - 0.3f;
                inningsPitched = Math.round(inningsPitched * 10) / 10.0f;
            }
        }
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(boxScoreId);
        dest.writeValue(pitchingLineId);
        dest.writeValue(pitcherNumber);
        dest.writeValue(pitcherName);
        dest.writeValue(inningsPitched);
        dest.writeValue(hitsAllowed);
        dest.writeValue(runsAllowed);
        dest.writeValue(earnedRuns);
        dest.writeValue(walksAllowed);
        dest.writeValue(strikeOutsMade);
        dest.writeValue(homeRunsAllowed);
        dest.writeValue(era);
        dest.writeValue(whip);
        dest.writeValue(pitchesThrown);
        dest.writeValue(strikesThrown);
    }

    public int describeContents() {
        return 0;
    }

}
