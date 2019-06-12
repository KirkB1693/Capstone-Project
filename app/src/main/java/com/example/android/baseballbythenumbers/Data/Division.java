package com.example.android.baseballbythenumbers.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Division implements Parcelable
{

    @SerializedName("divisionName")
    @Expose
    private String divisionName;
    @SerializedName("teams")
    @Expose
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
     */
    public Division(String divisionName, List<Team> teams) {
        super();
        this.divisionName = divisionName;
        this.teams = teams;
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

    @NotNull
    @Override
    public String toString() {
        return "Division Name : " + divisionName + "\nTeams : \n" + teams;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(divisionName);
        dest.writeList(teams);
    }

    public int describeContents() {
        return 0;
    }

}