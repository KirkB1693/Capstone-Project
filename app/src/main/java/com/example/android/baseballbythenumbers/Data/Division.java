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

import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "divisions", foreignKeys = @ForeignKey(entity = League.class, parentColumns = "leagueId", childColumns = "leagueId", onDelete = CASCADE), indices = @Index(value = "leagueId"))
public class Division implements Parcelable
{
    @PrimaryKey
    @NonNull
    private String divisionId;

    private String leagueId;

    @SerializedName("divisionName")
    @Expose
    private String divisionName;
    @SerializedName("teams")
    @Expose
    @Ignore
    private List<Team> teams = null;
    public final static Parcelable.Creator<Division> CREATOR = new Creator<Division>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Division createFromParcel(Parcel in) {
            return new Division(in);
        }

        public Division[] newArray(int size) {
            return (new Division[size]);
        }

    }
            ;

    protected Division(Parcel in) {
        this.divisionId = ((String) in.readValue((String.class.getClassLoader())));
        this.leagueId = ((String) in.readValue((String.class.getClassLoader())));
        this.divisionName = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.teams, (com.example.android.baseballbythenumbers.Data.Team.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Division() {
    }

    /**
     *
     * @param teams
     * @param divisionName
     * @param leagueId
     */
    public Division(String divisionName, List<Team> teams, String leagueId) {
        super();
        this.divisionName = divisionName;
        this.teams = teams;
        this.leagueId = leagueId;
        this.divisionId = UUID.randomUUID().toString();
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    @NotNull
    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(@NotNull String divisionId) {
        this.divisionId = divisionId;
    }

    @NotNull
    @Override
    public String toString() {
        return "Division Name : " + divisionName + "\nTeams : \n" + teams;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(divisionId);
        dest.writeValue(leagueId);
        dest.writeValue(divisionName);
        dest.writeList(teams);
    }

    public int describeContents() {
        return 0;
    }

}