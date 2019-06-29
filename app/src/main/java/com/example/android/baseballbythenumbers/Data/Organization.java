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

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static com.example.android.baseballbythenumbers.Constants.TableNames.ORGANIZATION_TABLE_NAME;

@Entity (tableName = ORGANIZATION_TABLE_NAME)
public class Organization implements Parcelable {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private String id;

    @SerializedName("organizationName")
    @Expose
    @ColumnInfo(name = "organization_name")
    private String organizationName;

    @SerializedName("organizationName")
    @Expose
    @ColumnInfo(name = "user_team_name")
    private String userTeamName;

    @SerializedName("currentYear")
    @Expose
    @ColumnInfo(name = "current_year")
    private int currentYear;

    @SerializedName("leagues")
    @Expose
    @Ignore
    private List<League> leagues = null;

    @SerializedName("schedules")
    @Expose
    @Ignore
    private List<Schedule> schedules = null;


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
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.currentYear = ((int) in.readValue((int.class.getClassLoader())));
        this.organizationName = ((String) in.readValue((String.class.getClassLoader())));
        this.userTeamName = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.leagues, (com.example.android.baseballbythenumbers.Data.League.class.getClassLoader()));
        in.readList(this.schedules, (com.example.android.baseballbythenumbers.Data.Schedule.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */

    public Organization() {
    }

    /**
     *
     * @param schedules
     * @param leagues
     * @param currentYear
     * @param organizationName
     */
    public Organization(String organizationName, String userTeamName, int currentYear, List<League> leagues, List<Schedule> schedules) {
        super();
        this.organizationName = organizationName;
        this.userTeamName = userTeamName;
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

    public String getUserTeamName() {
        return userTeamName;
    }

    public void setUserTeamName(String userTeamName) {
        this.userTeamName = userTeamName;
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

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
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
        dest.writeValue(userTeamName);
        dest.writeValue(currentYear);
        dest.writeList(leagues);
        dest.writeList(schedules);
    }

    public int describeContents() {
        return 0;
    }

}