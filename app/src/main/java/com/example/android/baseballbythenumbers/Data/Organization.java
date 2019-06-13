package com.example.android.baseballbythenumbers.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Organization implements Parcelable
{

    @SerializedName("organizationName")
    @Expose
    private String organizationName;
    @SerializedName("currentYear")
    @Expose
    private int currentYear;
    @SerializedName("leagues")
    @Expose
    private List<League> leagues = null;
    public final static Parcelable.Creator<Organization> CREATOR = new Creator<Organization>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Organization createFromParcel(Parcel in) {
            return new Organization(in);
        }

        public Organization[] newArray(int size) {
            return (new Organization[size]);
        }

    }
            ;

    protected Organization(Parcel in) {
        this.organizationName = ((String) in.readValue((String.class.getClassLoader())));
        this.currentYear = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.leagues, (com.example.android.baseballbythenumbers.Data.League.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Organization() {
    }

    /**
     *
     * @param leagues
     * @param currentYear
     * @param levelName
     */
    public Organization(String levelName, int currentYear, List<League> leagues) {
        super();
        this.organizationName = levelName;
        this.currentYear = currentYear;
        this.leagues = leagues;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
        dest.writeValue(organizationName);
        dest.writeValue(currentYear);
        dest.writeList(leagues);
    }

    public int describeContents() {
        return 0;
    }

}