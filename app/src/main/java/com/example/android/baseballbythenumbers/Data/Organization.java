package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

import static com.example.android.baseballbythenumbers.Constants.TableNames.ORGANIZATION_TABLE_NAME;

@Entity (tableName = ORGANIZATION_TABLE_NAME)
public class Organization implements Parcelable
{
    @PrimaryKey
    @NonNull
    private String id;

    @SerializedName("organizationName")
    @Expose
    @ColumnInfo(name = "organization_name")
    private String organizationName;

    @SerializedName("currentYear")
    @Expose
    @ColumnInfo(name = "current_year")
    private int currentYear;

    @SerializedName("leagues")
    @Expose
    @Ignore
    private List<League> leagues = null;

    @Ignore
    private List<Schedule> schedules = null;
    public final static Parcelable.Creator<Organization> CREATOR = new Creator<Organization>() {


        public Organization createFromParcel(Parcel in) {
            return new Organization(in);
        }

        public Organization[] newArray(int size) {
            return (new Organization[size]);
        }

    }
            ;

    protected Organization(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.organizationName = ((String) in.readValue((String.class.getClassLoader())));
        this.currentYear = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.leagues, (com.example.android.baseballbythenumbers.Data.League.class.getClassLoader()));
        in.readList(this.schedules, (com.example.android.baseballbythenumbers.Data.League.class.getClassLoader()));
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
    public Organization(String levelName, int currentYear, List<League> leagues, List<Schedule> schedules) {
        super();
        this.organizationName = levelName;
        this.currentYear = currentYear;
        this.leagues = leagues;
        this.schedules = schedules;
        this.id = UUID.randomUUID().toString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        for (Schedule s: schedules) {
            s.setOrganizationId(id);
        }
        this.schedules = schedules;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(organizationName);
        dest.writeValue(currentYear);
        dest.writeList(leagues);
        dest.writeList(schedules);
    }

    public int describeContents() {
        return 0;
    }

}