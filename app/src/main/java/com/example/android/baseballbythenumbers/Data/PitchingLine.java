package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "pitching_line", foreignKeys = @ForeignKey(entity = BoxScore.class, parentColumns = "box_score_id", childColumns = "box_score_id", onDelete = CASCADE), indices = @Index(value = "box_score_id"))
public class PitchingLine {

    @ColumnInfo(name = "box_score_id")
    private int boxScoreId;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pitching_line_id")
    private int pitchingLineId;

    @ColumnInfo(name = "pitcher_number")
    private int pitcherNumber;

    @ColumnInfo(name = "pitcher_name")
    private String pitcherName;

    @ColumnInfo(name = "innings_pitched")
    private int inningsPitched;

    @ColumnInfo(name = "hits_allowed")
    private int hitsAllowed;

    @ColumnInfo(name = "runs_allowed")
    private int runsAllowed;

    @ColumnInfo(name = "earned_runs")
    private int earnedRuns;

    @ColumnInfo(name = "walks_allowed")
    private int walksAllowed;

    @ColumnInfo(name = "strike_outs_made")
    private int strikeOutsMade;

    @ColumnInfo(name = "home_runs_allowed")
    private int homeRunsAllowed;

    private double era;

    private double whip;

    @ColumnInfo(name = "pitches_thrown")
    private int pitchesThrown;

    @ColumnInfo(name = "strikes_thrown")
    private int strikesThrown;

    public PitchingLine(){}

    @Ignore
    public PitchingLine(int boxScoreId, int pitcherNumber, String pitcherName){
        this.boxScoreId = boxScoreId;
        this.pitcherNumber = pitcherNumber;
        this.pitcherName = pitcherName;
    }

    public int getBoxScoreId() {
        return boxScoreId;
    }

    public void setBoxScoreId(int boxScoreId) {
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

    public int getHomeRunsAllowed() {
        return homeRunsAllowed;
    }

    public void setHomeRunsAllowed(int homeRunsAllowed) {
        this.homeRunsAllowed = homeRunsAllowed;
    }

    public int getInningsPitched() {
        return inningsPitched;
    }

    public void setInningsPitched(int inningsPitched) {
        this.inningsPitched = inningsPitched;
    }

    public int getPitcherNumber() {
        return pitcherNumber;
    }

    public void setPitcherNumber(int pitcherNumber) {
        this.pitcherNumber = pitcherNumber;
    }

    public int getPitchingLineId() {
        return pitchingLineId;
    }

    public void setPitchingLineId(int pitchingLineId) {
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

    public int getStrikeOutsMade() {
        return strikeOutsMade;
    }

    public void setStrikeOutsMade(int strikeOutsMade) {
        this.strikeOutsMade = strikeOutsMade;
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

    public int getStrikesThrown() {
        return strikesThrown;
    }

    public void setStrikesThrown(int strikesThrown) {
        this.strikesThrown = strikesThrown;
    }
}
