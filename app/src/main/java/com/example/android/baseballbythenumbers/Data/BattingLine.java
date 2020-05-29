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

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "batting_line", foreignKeys = @ForeignKey(entity = BoxScore.class, parentColumns = "box_score_id", childColumns = "box_score_id", onDelete = CASCADE), indices = @Index(value = "box_score_id"))
public class BattingLine implements Parcelable, Comparable<BattingLine> {

    @ColumnInfo(name = "box_score_id")
    @SerializedName("boxScoreId")
    @Expose
    private String boxScoreId;

    @SerializedName("battingLineId")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "batting_line_id")
    private String battingLineId;

    @SerializedName("positionInBattingOrder")
    @Expose
    @ColumnInfo(name = "position_in_batting_order")
    private int positionInBattingOrder;

    @SerializedName("substitute")
    @Expose
    private boolean substitute;

    @SerializedName("substituteNumber")
    @Expose
    @ColumnInfo(name = "sub_number")
    private int substituteNumber;

    @SerializedName("batterName")
    @Expose
    @ColumnInfo(name = "batter_name")
    private String batterName;

    @SerializedName("atBats")
    @Expose
    private int atBats;

    @SerializedName("runs")
    @Expose
    private int runs;

    @SerializedName("hits")
    @Expose
    private int hits;

    @SerializedName("rbis")
    @Expose
    private int rbis;

    @SerializedName("homeRuns")
    @Expose
    @ColumnInfo(name = "home_runs")
    private int homeRuns;

    @SerializedName("walks")
    @Expose
    private int walks;

    @SerializedName("strikeOuts")
    @Expose
    @ColumnInfo(name = "strike_outs")
    private int strikeOuts;

    @ColumnInfo(name = "left_on_base")
    @SerializedName("leftOnBase")
    @Expose
    private int leftOnBase;

    @SerializedName("average")
    @Expose
    private double average;

    @SerializedName("onBasePct")
    @Expose
    @ColumnInfo(name = "on_base_pct")
    private double onBasePct;

    public final static Parcelable.Creator<BattingLine> CREATOR = new Creator<BattingLine>() {


        public BattingLine createFromParcel(Parcel in) {
            return new BattingLine(in);
        }

        public BattingLine[] newArray(int size) {
            return (new BattingLine[size]);
        }

    };

    protected BattingLine(Parcel in) {
        boxScoreId = in.readString();
        battingLineId = in.readString();
        positionInBattingOrder = in.readInt();
        substitute = in.readByte() != 0x00;
        substituteNumber = in.readInt();
        batterName = in.readString();
        atBats = in.readInt();
        runs = in.readInt();
        hits = in.readInt();
        rbis = in.readInt();
        homeRuns = in.readInt();
        walks = in.readInt();
        strikeOuts = in.readInt();
        leftOnBase = in.readInt();
        average = in.readDouble();
        onBasePct = in.readDouble();
    }


    public BattingLine() {
    }

    @Ignore
    public BattingLine(String boxScoreId, int positionInBattingOrder, boolean substitute, int substituteNumber, String batterName) {
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
        atBats++;
    }

    @NotNull
    public String getBattingLineId() {
        return battingLineId;
    }

    public void setBattingLineId(@NotNull String battingLineId) {
        this.battingLineId = battingLineId;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void incrementHits() {
        hits++;
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
        homeRuns++;
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
        runs++;
    }

    public int getStrikeOuts() {
        return strikeOuts;
    }

    public void setStrikeOuts(int strikeOuts) {
        this.strikeOuts = strikeOuts;
    }

    public void incrementStrikeOuts() {
        strikeOuts++;
    }

    public int getWalks() {
        return walks;
    }

    public void setWalks(int walks) {
        this.walks = walks;
    }

    public void incrementWalks() {
        walks++;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boxScoreId);
        dest.writeString(battingLineId);
        dest.writeInt(positionInBattingOrder);
        dest.writeByte((byte) (substitute ? 0x01 : 0x00));
        dest.writeInt(substituteNumber);
        dest.writeString(batterName);
        dest.writeInt(atBats);
        dest.writeInt(runs);
        dest.writeInt(hits);
        dest.writeInt(rbis);
        dest.writeInt(homeRuns);
        dest.writeInt(walks);
        dest.writeInt(strikeOuts);
        dest.writeInt(leftOnBase);
        dest.writeDouble(average);
        dest.writeDouble(onBasePct);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(BattingLine battingLine) {
        if (positionInBattingOrder > battingLine.positionInBattingOrder) {
            return 1;
        } else if (positionInBattingOrder < battingLine.positionInBattingOrder) {
            return -1;
        } else if (!substitute) {
            return -1;
        } else if (!battingLine.substitute) {
            return 1;
        } else if (substituteNumber > battingLine.substituteNumber) {
            return 1;
        } else if (substituteNumber < battingLine.substituteNumber) {
            return -1;
        } else {
            return 0;
        }

    }
}
