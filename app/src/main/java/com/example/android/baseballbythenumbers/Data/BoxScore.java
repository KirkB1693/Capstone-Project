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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

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


        @SuppressWarnings({
                "unchecked"
        })
        public BoxScore createFromParcel(Parcel in) {
            return new BoxScore(in);
        }

        public BoxScore[] newArray(int size) {
            return (new BoxScore[size]);
        }

    }
            ;

    protected BoxScore(Parcel in) {
        this.boxScoreId = ((String) in.readValue((String.class.getClassLoader())));
        this.gameId = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.battingLines, (com.example.android.baseballbythenumbers.Data.BattingLine.class.getClassLoader()));
        in.readList(this.pitchingLines, (com.example.android.baseballbythenumbers.Data.PitchingLine.class.getClassLoader()));
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(boxScoreId);
        dest.writeValue(gameId);
        dest.writeList(battingLines);
        dest.writeList(pitchingLines);
    }

    public int describeContents() {
        return 0;
    }


}
