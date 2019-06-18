package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "games", foreignKeys = @ForeignKey(entity = Schedule.class, parentColumns = "schedule_id", childColumns = "schedule_id", onDelete = CASCADE),indices = @Index(value = "schedule_id"))
public class Game {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    private int gameId;

    @ColumnInfo(name = "schedule_id")
    private int scheduleId;

    private int day;

    @ColumnInfo(name = "home_team_id")
    private int homeTeamId;

    @ColumnInfo(name = "visiting_team_id")
    private int visitingTeamId;

    @ColumnInfo(name = "home_score")
    private int homeScore;

    @ColumnInfo(name = "visitor_score")
    private int visitorScore;

    @ColumnInfo(name = "played_game")
    private boolean playedGame;

    @ColumnInfo(name = "game_log")
    private String gameLog;

    @Ignore
    @ColumnInfo(name = "home_box_score")
    private BoxScore homeBoxScore;

    @Ignore
    @ColumnInfo(name = "visitor_box_score")
    private BoxScore visitorBoxScore;

    public Game(int scheduleId) {
        this.scheduleId = scheduleId;

    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isPlayedGame() {
        return playedGame;
    }

    public void setPlayedGame(boolean playedGame) {
        this.playedGame = playedGame;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getVisitorScore() {
        return visitorScore;
    }

    public void setVisitorScore(int visitorScore) {
        this.visitorScore = visitorScore;
    }

    public String getGameLog() {
        return gameLog;
    }

    public void setGameLog(String gameLog) {
        this.gameLog = gameLog;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public BoxScore getHomeBoxScore() {
        return homeBoxScore;
    }

    public void setHomeBoxScore(BoxScore homeBoxScore) {
        this.homeBoxScore = homeBoxScore;
    }

    public int getVisitingTeamId() {
        return visitingTeamId;
    }

    public void setVisitingTeamId(int visitingTeamId) {
        this.visitingTeamId = visitingTeamId;
    }

    public BoxScore getVisitorBoxScore() {
        return visitorBoxScore;
    }

    public void setVisitorBoxScore(BoxScore visitorBoxScore) {
        this.visitorBoxScore = visitorBoxScore;
    }
}
