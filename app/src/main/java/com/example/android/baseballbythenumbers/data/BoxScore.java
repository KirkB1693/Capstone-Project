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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "box_scores", foreignKeys = @ForeignKey(entity = Game.class, parentColumns = "game_id",childColumns = "game_id", onDelete = CASCADE), indices = @Index(value = "game_id"))
public class BoxScore implements Parcelable {

    @SerializedName("boxScoreId")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "box_score_id")
    private String boxScoreId;

    @SerializedName("gameId")
    @Expose
    @ColumnInfo(name = "game_id")
    private String gameId;

    @SerializedName("battingLines")
    @Expose
    @Ignore
    List<BattingLine> battingLines;

    @SerializedName("pitchingLines")
    @Expose
    @Ignore
    List<PitchingLine> pitchingLines;

    @SerializedName("isBoxScoreForHomeTeam")
    @Expose
    @ColumnInfo(name = "is_box_score_for_home_team")
    private boolean isBoxScoreForHomeTeam;

    public final static Parcelable.Creator<BoxScore> CREATOR = new Creator<BoxScore>() {


        public BoxScore createFromParcel(Parcel in) {
            return new BoxScore(in);
        }

        public BoxScore[] newArray(int size) {
            return (new BoxScore[size]);
        }

    }
            ;

    @SuppressWarnings("ConstantConditions")
    protected BoxScore(Parcel in) {
        boxScoreId = in.readString();
        gameId = in.readString();
        if (in.readByte() == 0x01) {
            battingLines = new ArrayList<>();
            in.readList(battingLines, (BattingLine.class.getClassLoader()));
        } else {
            battingLines = null;
        }
        if (in.readByte() == 0x01) {
            pitchingLines = new ArrayList<>();
            in.readList(pitchingLines, PitchingLine.class.getClassLoader());
        } else {
            pitchingLines = null;
        }
        isBoxScoreForHomeTeam = in.readByte() != 0x00;
    }

    public BoxScore (String gameId, boolean isBoxScoreForHomeTeam) {
        this.gameId = gameId;
        battingLines = new ArrayList<>();
        pitchingLines = new ArrayList<>();
        this.isBoxScoreForHomeTeam = isBoxScoreForHomeTeam;
        boxScoreId = UUID.randomUUID().toString();
    }

    @NotNull
    public String getBoxScoreId() {
        return boxScoreId;
    }

    public void setBoxScoreId(@NotNull String boxScoreId) {
        this.boxScoreId = boxScoreId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<BattingLine> getBattingLines() {
        return battingLines;
    }

    public void setBattingLines(List<BattingLine> battingLines) {
        this.battingLines = battingLines;
    }

    public void addBattingLine(BattingLine battingLine){
        battingLines.add(battingLine);
    }

    public List<PitchingLine> getPitchingLines() {
        return pitchingLines;
    }

    public void setPitchingLines(List<PitchingLine> pitchingLines) {
        this.pitchingLines = pitchingLines;
    }

    public boolean isBoxScoreForHomeTeam() {
        return isBoxScoreForHomeTeam;
    }

    public void setBoxScoreForHomeTeam(boolean boxScoreForHomeTeam) {
        isBoxScoreForHomeTeam = boxScoreForHomeTeam;
    }

    public void addPitchingLine(PitchingLine pitchingLine) {
        pitchingLines.add(pitchingLine);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boxScoreId);
        dest.writeString(gameId);
        if (battingLines == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(battingLines);
        }
        if (pitchingLines == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(pitchingLines);
        }
        dest.writeByte((byte) (isBoxScoreForHomeTeam ? 0x01 : 0x00));
    }

    public int describeContents() {
        return 0;
    }


}
