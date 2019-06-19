package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "box_scores", foreignKeys = @ForeignKey(entity = Game.class, parentColumns = "game_id",childColumns = "game_id", onDelete = CASCADE), indices = @Index(value = "game_id"))
public class BoxScore {

    @ColumnInfo(name = "game_id")
    private int gameId;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "box_score_id")
    private int boxScoreId;

    @Ignore
    List<BattingLine> battingLines;

    @Ignore
    List<PitchingLine> pitchingLines;

    public BoxScore (int gameId) {
        this.gameId = gameId;
        battingLines = new ArrayList<>();
        pitchingLines = new ArrayList<>();
    }

    public int getBoxScoreId() {
        return boxScoreId;
    }

    public void setBoxScoreId(int boxScoreId) {
        this.boxScoreId = boxScoreId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
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

    public void addPitchingLine(PitchingLine pitchingLine) {
        pitchingLines.add(pitchingLine);
    }
}
