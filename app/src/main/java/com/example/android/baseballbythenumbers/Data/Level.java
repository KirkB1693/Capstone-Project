package com.example.android.baseballbythenumbers.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class Level implements Parcelable
{

    @SerializedName("levelName")
    @Expose
    private String levelName;
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
     * @param levelName
     */
    public Level(String levelName, List<League> leagues) {
        super();
        this.levelName = levelName;
        this.leagues = leagues;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("levelName", levelName).append("leagues", leagues).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(levelName);
        dest.writeList(leagues);
    }

    public int describeContents() {
        return 0;
    }

}