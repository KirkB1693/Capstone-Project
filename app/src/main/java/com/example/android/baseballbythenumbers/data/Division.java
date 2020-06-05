package com.example.android.baseballbythenumbers.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
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


@SuppressWarnings("NotNullFieldNotInitialized")
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


        public Division createFromParcel(Parcel in) {
            return new Division(in);
        }

        public Division[] newArray(int size) {
            return (new Division[size]);
        }

    }
            ;

    @SuppressWarnings("ConstantConditions")
    protected Division(Parcel in) {
        divisionId = in.readString();
        leagueId = in.readString();
        divisionName = in.readString();
        if (in.readByte() == 0x01) {
            teams = new ArrayList<>();
            in.readList(teams, Team.class.getClassLoader());
        } else {
            teams = null;
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Division() {
    }

    /**
     *
     * @param teams a list of the teams in the division
     * @param divisionName the name of the division
     * @param leagueId the league ID that this division belongs to
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(divisionId);
        dest.writeString(leagueId);
        dest.writeString(divisionName);
        dest.writeByte((byte) (0x00));
    }

    public int describeContents() {
        return 0;
    }

}