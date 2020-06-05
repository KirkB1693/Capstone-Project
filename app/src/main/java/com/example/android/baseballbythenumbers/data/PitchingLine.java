package com.example.android.baseballbythenumbers.data;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;


@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(tableName = "pitching_line", foreignKeys = @ForeignKey(entity = BoxScore.class, parentColumns = "box_score_id", childColumns = "box_score_id", onDelete = CASCADE), indices = @Index(value = "box_score_id"))
public class PitchingLine implements Parcelable, Comparable<PitchingLine>{

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
    private String era;

    @SerializedName("whip")
    @Expose
    private String whip;

    @SerializedName("pitchesThrown")
    @Expose
    @ColumnInfo(name = "pitches_thrown")
    private int pitchesThrown;

    @SerializedName("strikesThrown")
    @Expose
    @ColumnInfo(name = "strikes_thrown")
    private int strikesThrown;

    public final static Parcelable.Creator<PitchingLine> CREATOR = new Creator<PitchingLine>() {


        public PitchingLine createFromParcel(Parcel in) {
            return new PitchingLine(in);
        }

        public PitchingLine[] newArray(int size) {
            return (new PitchingLine[size]);
        }

    }
            ;

    @SuppressWarnings("ConstantConditions")
    protected PitchingLine(Parcel in) {
        boxScoreId = in.readString();
        pitchingLineId = in.readString();
        pitcherNumber = in.readInt();
        pitcherName = in.readString();
        inningsPitched = in.readFloat();
        hitsAllowed = in.readInt();
        runsAllowed = in.readInt();
        earnedRuns = in.readInt();
        walksAllowed = in.readInt();
        strikeOutsMade = in.readInt();
        homeRunsAllowed = in.readInt();
        era = in.readString();
        whip = in.readString();
        pitchesThrown = in.readInt();
        strikesThrown = in.readInt();
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

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }

    public String getWhip() {
        return whip;
    }

    public void setWhip(String whip) {
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

    @NotNull
    public String getPitchingLineId() {
        return pitchingLineId;
    }

    public void setPitchingLineId(@NotNull String pitchingLineId) {
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

    public void incrementStrikeOutsMade() {
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


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boxScoreId);
        dest.writeString(pitchingLineId);
        dest.writeInt(pitcherNumber);
        dest.writeString(pitcherName);
        dest.writeFloat(inningsPitched);
        dest.writeInt(hitsAllowed);
        dest.writeInt(runsAllowed);
        dest.writeInt(earnedRuns);
        dest.writeInt(walksAllowed);
        dest.writeInt(strikeOutsMade);
        dest.writeInt(homeRunsAllowed);
        dest.writeString(era);
        dest.writeString(whip);
        dest.writeInt(pitchesThrown);
        dest.writeInt(strikesThrown);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(PitchingLine pitchingLine) {
        if (pitcherNumber > pitchingLine.pitcherNumber) {
            return 1;
        } else if (pitcherNumber < pitchingLine.pitcherNumber) {
            return -1;
        }  else {
            return 0;
        }

    }
}
