package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity (tableName = "teams", foreignKeys = @ForeignKey(entity = Division.class, parentColumns = "divisionId", childColumns = "divisionId", onDelete = CASCADE),indices = @Index(value = "divisionId"))
public class Team implements Parcelable
{
    @PrimaryKey (autoGenerate = true)
    private int teamId;

    private int divisionId;

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
        this.teamId = ((int) in.readValue((int.class.getClassLoader())));
        this.divisionId = ((int) in.readValue((int.class.getClassLoader())));
        this.teamName = ((String) in.readValue((String.class.getClassLoader())));
        this.teamCity = ((String) in.readValue((String.class.getClassLoader())));
        this.useDh = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.teamBudget = ((int) in.readValue((int.class.getClassLoader())));
        this.wins = ((int) in.readValue((int.class.getClassLoader())));
        this.losses = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.players, (com.example.android.baseballbythenumbers.Data.Player.class.getClassLoader()));
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
    public Team(String teamName, String teamCity, boolean useDh, int teamBudget, List<Player> players, int divisionId, int wins, int losses) {
        this.teamName = teamName;
        this.teamCity = teamCity;
        this.useDh = useDh;
        this.teamBudget = teamBudget;
        this.players = players;
        this.divisionId = divisionId;
        this.wins = wins;
        this.losses = losses;
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

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(teamId);
        dest.writeValue(divisionId);
        dest.writeValue(teamName);
        dest.writeValue(teamCity);
        dest.writeValue(useDh);
        dest.writeValue(teamBudget);
        dest.writeValue(wins);
        dest.writeValue(losses);
        dest.writeList(players);
    }

    public int describeContents() {
        return 0;
    }

}