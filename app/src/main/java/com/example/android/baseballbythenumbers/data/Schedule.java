package com.example.android.baseballbythenumbers.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
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
@Entity(tableName = "schedules", foreignKeys = @ForeignKey(entity = Organization.class, parentColumns = "id", childColumns = "organization_id",onDelete = CASCADE), indices = @Index(value = "organization_id"))
public class Schedule implements Parcelable, Comparable<Schedule> {

    @SerializedName("scheduleId")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "schedule_id")
    private String scheduleId;

    @SerializedName("organizationId")
    @Expose
    @ColumnInfo(name = "organization_id")
    private String organizationId;

    @SerializedName("scheduleYear")
    @Expose
    @ColumnInfo(name = "schedule_year")
    private int scheduleYear;

    @Ignore
    @SerializedName("games")
    @Expose
    private List<Game> gameList;

    public final static Parcelable.Creator<Schedule> CREATOR = new Creator<Schedule>() {


        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        public Schedule[] newArray(int size) {
            return (new Schedule[size]);
        }

    }
            ;

    @SuppressWarnings("ConstantConditions")
    protected Schedule(Parcel in) {
        scheduleId = in.readString();
        organizationId = in.readString();
        scheduleYear = in.readInt();
        if (in.readByte() == 0x01) {
            gameList = new ArrayList<>();
            in.readList(gameList, Game.class.getClassLoader());
        } else {
            gameList = null;
        }
    }



    public Schedule(){
    }

    @Ignore
    public Schedule(String organizationId, List<Game> games){
        this.organizationId = organizationId;
        this.gameList = games;
        this.scheduleId = UUID.randomUUID().toString();
    }


    @NotNull
    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(@NotNull String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public int getScheduleYear() {
        return scheduleYear;
    }

    public void setScheduleYear(int scheduleYear) {
        this.scheduleYear = scheduleYear;
    }

    @NonNull
    @Override
    public String toString() {
        return "OrgId : " + organizationId + ", SchedId : " + scheduleId + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(scheduleId);
        dest.writeString(organizationId);
        dest.writeInt(scheduleYear);
        dest.writeByte((byte) (0x00));
    }

    @Override
    public int compareTo(Schedule schedule) {
        if (scheduleYear > schedule.scheduleYear) {
            return 1;
        } else if (scheduleYear < schedule.scheduleYear) {
            return -1;
        } else {
            return 0;
        }
    }
}
