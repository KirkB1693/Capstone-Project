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

import java.util.ArrayList;
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

    @SerializedName("interleaguePlay")
    @Expose
    @ColumnInfo(name = "interleague_play")
    private boolean interleaguePlay;

    @SerializedName("seriesLength")
    @Expose
    @ColumnInfo(name = "series_length")
    private int seriesLength;

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
        id = in.readString();
        organizationName = in.readString();
        userTeamName = in.readString();
        currentYear = in.readInt();
        interleaguePlay = in.readByte() != 0x00;
        seriesLength = in.readInt();
        if (in.readByte() == 0x01) {
            leagues = new ArrayList<League>();
            in.readList(leagues, League.class.getClassLoader());
        } else {
            leagues = null;
        }
        if (in.readByte() == 0x01) {
            schedules = new ArrayList<Schedule>();
            in.readList(schedules, Schedule.class.getClassLoader());
        } else {
            schedules = null;
        }
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
     * @param interleaguePlay
     * @param userTeamName
     * @param organizationName
     */
    public Organization(String organizationName, String userTeamName, int currentYear, boolean interleaguePlay, int seriesLength,List<League> leagues, List<Schedule> schedules) {
        super();
        this.organizationName = organizationName;
        this.userTeamName = userTeamName;
        this.currentYear = currentYear;
        this.interleaguePlay = interleaguePlay;
        this.seriesLength = seriesLength;
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

    public boolean isInterleaguePlay() {
        return interleaguePlay;
    }

    public void setInterleaguePlay(boolean interleaguePlay) {
        this.interleaguePlay = interleaguePlay;
    }

    public int getSeriesLength() {
        return seriesLength;
    }

    public void setSeriesLength(int seriesLength) {
        this.seriesLength = seriesLength;
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

    public Schedule getScheduleForCurrentYear() {
        if (schedules != null) {
            for (Schedule schedule : schedules) {
                if (schedule.getScheduleYear() == currentYear) {
                    return schedule;
                }
            }
        }
        return null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(organizationName);
        dest.writeString(userTeamName);
        dest.writeInt(currentYear);
        dest.writeByte((byte) (interleaguePlay ? 0x01 : 0x00));
        dest.writeInt(seriesLength);
        if (leagues == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(leagues);
        }
        if (schedules == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(schedules);
        }
    }


    public int describeContents() {
        return 0;
    }

}