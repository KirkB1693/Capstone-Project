package com.example.android.baseballbythenumbers.Data;

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

@Entity (tableName = "teams", foreignKeys = @ForeignKey(entity = Division.class, parentColumns = "divisionId", childColumns = "divisionId", onDelete = CASCADE),indices = @Index(value = "divisionId"))
public class Team implements Parcelable
{
    @PrimaryKey
    @NonNull
    private String teamId;

    private String divisionId;

    @SerializedName("teamName")
    @Expose
    private String teamName;
    @SerializedName("teamCity")
    @Expose
    private String teamCity;
    @SerializedName("useDh")
    @Expose
    private boolean useDh;
    @SerializedName("teamBudget")
    @Expose
    private int teamBudget;
    @SerializedName("wins")
    @Expose
    private int wins;
    @SerializedName("losses")
    @Expose
    private int losses;

    @SerializedName("players")
    @Expose
    @Ignore
    private List<Player> players = null;
    public final static Parcelable.Creator<Team> CREATOR = new Creator<Team>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        public Team[] newArray(int size) {
            return (new Team[size]);
        }

    }
            ;

    protected Team(Parcel in) {
        teamId = in.readString();
        divisionId = in.readString();
        teamName = in.readString();
        teamCity = in.readString();
        useDh = in.readByte() != 0x00;
        teamBudget = in.readInt();
        wins = in.readInt();
        losses = in.readInt();
        if (in.readByte() == 0x01) {
            players = new ArrayList<Player>();
            in.readList(players, Player.class.getClassLoader());
        } else {
            players = null;
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */

    public Team() {
    }

    /**
     *
     * @param teamName
     * @param teamBudget
     * @param teamCity
     * @param players
     * @param useDh
     */
    @Ignore
    public Team(String teamName, String teamCity, boolean useDh, int teamBudget, List<Player> players, String divisionId, int wins, int losses) {
        this.teamName = teamName;
        this.teamCity = teamCity;
        this.useDh = useDh;
        this.teamBudget = teamBudget;
        this.players = players;
        this.divisionId = divisionId;
        this.wins = wins;
        this.losses = losses;
        this.teamId = UUID.randomUUID().toString();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamCity() {
        return teamCity;
    }

    public void setTeamCity(String teamCity) {
        this.teamCity = teamCity;
    }

    public boolean isUseDh() {
        return useDh;
    }

    public void setUseDh(boolean useDh) {
        this.useDh = useDh;
    }

    public int getTeamBudget() {
        return teamBudget;
    }

    public void setTeamBudget(int teamBudget) {
        this.teamBudget = teamBudget;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @NotNull
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(@NotNull String teamId) {
        this.teamId = teamId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void incrementWins() {
        wins ++;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void incrementLosses() {
        losses ++;
    }

    @Override
    public String toString() {
        return "Team Name : " + teamName + "\nTeam City : " + teamCity + "\nTeam Budget : " + teamBudget +"\nPlayers :\n" + players.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(teamId);
        dest.writeString(divisionId);
        dest.writeString(teamName);
        dest.writeString(teamCity);
        dest.writeByte((byte) (useDh ? 0x01 : 0x00));
        dest.writeInt(teamBudget);
        dest.writeInt(wins);
        dest.writeInt(losses);
        dest.writeByte((byte) (0x00));
    }

    public int describeContents() {
        return 0;
    }

}