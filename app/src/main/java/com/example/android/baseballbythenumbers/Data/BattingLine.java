package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "batting_line", foreignKeys = @ForeignKey(entity = BoxScore.class, parentColumns = "box_score_id", childColumns = "box_score_id", onDelete = CASCADE), indices = @Index(value = "box_score_id"))
public class BattingLine {

    @ColumnInfo(name = "box_score_id")
    private String boxScoreId;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "batting_line_id")
    private String battingLineId;

    @ColumnInfo(name = "position_in_batting_order")
    private int positionInBattingOrder;

    private boolean substitute;

    @ColumnInfo(name = "sub_number")
    private int substituteNumber;

    @ColumnInfo(name = "batter_name")
    private String batterName;

    private int atBats;

    private int runs;

    private int hits;

    private int rbis;

    @ColumnInfo(name = "home_runs")
    private int homeRuns;

    private int walks;

    @ColumnInfo(name = "strike_outs")
    private int strikeOuts;

    @ColumnInfo(name = "left_on_base")
    private int leftOnBase;

    private double average;

    @ColumnInfo(name = "on_base_pct")
    private double onBasePct;

    public BattingLine(){
    }

    @Ignore
    public BattingLine(String boxScoreId, int positionInBattingOrder, boolean substitute, int substituteNumber, String batterName){
        this.boxScoreId = boxScoreId;
        this.positionInBattingOrder = positionInBattingOrder;
        this.substitute = substitute;
        this.substituteNumber = substituteNumber;
        this.batterName = batterName;
        this.battingLineId = UUID.randomUUID().toString();
    }

    public String getBoxScoreId() {
        return boxScoreId;
    }

    public void setBoxScoreId(String boxScoreId) {
        this.boxScoreId = boxScoreId;
    }

    public int getPositionInBattingOrder() {
        return positionInBattingOrder;
    }

    public void setPositionInBattingOrder(int positionInBattingOrder) {
        this.positionInBattingOrder = positionInBattingOrder;
    }

    public boolean isSubstitute() {
        return substitute;
    }

    public void setSubstitute(boolean substitute) {
        this.substitute = substitute;
    }

    public String getBatterName() {
        return batterName;
    }

    public void setBatterName(String batterName) {
        this.batterName = batterName;
    }

    public int getAtBats() {
        return atBats;
    }

    public void setAtBats(int atBats) {
        this.atBats = atBats;
    }

    public void incrementAtBats() {
        atBats ++;
    }

    public String getBattingLineId() {
        return battingLineId;
    }

    public void setBattingLineId(String battingLineId) {
        this.battingLineId = battingLineId;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void incrementHits() {
        hits ++;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double getOnBasePct() {
        return onBasePct;
    }

    public void setOnBasePct(double onBasePct) {
        this.onBasePct = onBasePct;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }

    public void incrementHomeRuns() {
        homeRuns ++;
    }

    public int getLeftOnBase() {
        return leftOnBase;
    }

    public void setLeftOnBase(int leftOnBase) {
        this.leftOnBase = leftOnBase;
    }

    public int getRbis() {
        return rbis;
    }

    public void setRbis(int rbis) {
        this.rbis = rbis;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public void incrementRuns() {
        runs ++;
    }

    public int getStrikeOuts() {
        return strikeOuts;
    }

    public void setStrikeOuts(int strikeOuts) {
        this.strikeOuts = strikeOuts;
    }

    public void incrementStrikeOuts() {
        strikeOuts ++;
    }

    public int getWalks() {
        return walks;
    }

    public void setWalks(int walks) {
        this.walks = walks;
    }

    public void incrementWalks() {
        walks ++;
    }

    public int getSubstituteNumber() {
        return substituteNumber;
    }

    public void setSubstituteNumber(int substituteNumber) {
        this.substituteNumber = substituteNumber;
    }

    @NotNull
    @Override
    public String toString() {
        return "Box Score Id : " + boxScoreId + ", \nPosition In Order : " + positionInBattingOrder + ", Name : " + batterName + "\n";
    }
}
