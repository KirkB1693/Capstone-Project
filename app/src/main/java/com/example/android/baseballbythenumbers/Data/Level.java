package com.example.android.baseballbythenumbers.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Level implements Parcelable
{

    @SerializedName("levelName")
    @Expose
    private String levelName;
    @SerializedName("currentYear")
    @Expose
    private int currentYear;
    @SerializedName("leagues")
    @Expose
    private List<League> leagues = null;
    public final static Parcelable.Creator<Level> CREATOR = new Creator<Level>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Level createFromParcel(Parcel in) {
            return new Level(in);
        }

        public Level[] newArray(int size) {
            return (new Level[size]);
        }

    }
            ;

    protected Level(Parcel in) {
        this.levelName = ((String) in.readValue((String.class.getClassLoader())));
        this.currentYear = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.leagues, (com.example.android.baseballbythenumbers.Data.League.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Level() {
    }

    /**
     *
     * @param leagues
     * @param currentYear
     * @param levelName
     */
    public Level(String levelName, int currentYear, List<League> leagues) {
        super();
        this.levelName = levelName;
        this.currentYear = currentYear;
        this.leagues = leagues;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(levelName);
        dest.writeValue(currentYear);
        dest.writeList(leagues);
    }

    public int describeContents() {
        return 0;
    }

}