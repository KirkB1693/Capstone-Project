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

@Entity(tableName = "games", foreignKeys = @ForeignKey(entity = Schedule.class, parentColumns = "schedule_id", childColumns = "schedule_id", onDelete = CASCADE),indices = @Index(value = "schedule_id"))
public class Game implements Parcelable, Comparable<Game> {

    @SerializedName("gameID")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "game_id")
    private String gameId;

    @SerializedName("scheduleId")
    @Expose
    @ColumnInfo(name = "schedule_id")
    private String scheduleId;

    @SerializedName("day")
    @Expose
    private int day;

    @SerializedName("homeTeamName")
    @Expose
    @ColumnInfo(name = "home_team_name")
    private String homeTeamName;

    @SerializedName("visitingTeamName")
    @Expose
    @ColumnInfo(name = "visiting_team_name")
    private String visitingTeamName;

    @SerializedName("homeScore")
    @Expose
    @ColumnInfo(name = "home_score")
    private int homeScore;

    @SerializedName("visitorScore")
    @Expose
    @ColumnInfo(name = "visitor_score")
    private int visitorScore;

    @SerializedName("playedGame")
    @Expose
    @ColumnInfo(name = "played_game")
    private boolean playedGame;

    @SerializedName("gameLog")
    @Expose
    @ColumnInfo(name = "game_log")
    private String gameLog;

    @SerializedName("homeBoxScore")
    @Expose
    @Ignore
    @ColumnInfo(name = "home_box_score")
    private BoxScore homeBoxScore;

    @SerializedName("visitorBoxScore")
    @Expose
    @Ignore
    @ColumnInfo(name = "visitor_box_score")
    private BoxScore visitorBoxScore;

    public final static Parcelable.Creator<Game> CREATOR = new Creator<Game>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return (new Game[size]);
        }

    };

    protected Game(Parcel in) {
        gameId = in.readString();
        scheduleId = in.readString();
        day = in.readInt();
        homeTeamName = in.readString();
        visitingTeamName = in.readString();
        homeScore = in.readInt();
        visitorScore = in.readInt();
        playedGame = in.readByte() != 0x00;
        gameLog = in.readString();
        homeBoxScore = (BoxScore) in.readValue(BoxScore.class.getClassLoader());
        visitorBoxScore = (BoxScore) in.readValue(BoxScore.class.getClassLoader());
    }



    public Game(String scheduleId) {
        this.scheduleId = scheduleId;
        this.gameId = UUID.randomUUID().toString();
    }

    @NotNull
    public String getGameId() {
        return gameId;
    }

    public void setGameId(@NotNull String gameId) {
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

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
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

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public BoxScore getHomeBoxScore() {
        return homeBoxScore;
    }

    public void setHomeBoxScore(BoxScore homeBoxScore) {
        this.homeBoxScore = homeBoxScore;
    }

    public String getVisitingTeamName() {
        return visitingTeamName;
    }

    public void setVisitingTeamName(String visitingTeamName) {
        this.visitingTeamName = visitingTeamName;
    }

    public BoxScore getVisitorBoxScore() {
        return visitorBoxScore;
    }

    public void setVisitorBoxScore(BoxScore visitorBoxScore) {
        this.visitorBoxScore = visitorBoxScore;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameId);
        dest.writeString(scheduleId);
        dest.writeInt(day);
        dest.writeString(homeTeamName);
        dest.writeString(visitingTeamName);
        dest.writeInt(homeScore);
        dest.writeInt(visitorScore);
        dest.writeByte((byte) (playedGame ? 0x01 : 0x00));
        dest.writeString(gameLog);
        dest.writeValue(homeBoxScore);
        dest.writeValue(visitorBoxScore);
    }

    public int describeContents() {
        return 0;
    }



    @Override
    public int compareTo(Game game) {
        if (day > game.day) {
            return 1;
        } else if (day < game.day) {
            return -1;
        } else {
            return 0;
        }
    }

}
