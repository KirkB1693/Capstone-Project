package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "schedules", foreignKeys = @ForeignKey(entity = Organization.class, parentColumns = "id", childColumns = "organization_id",onDelete = CASCADE), indices = @Index(value = "organization_id"))
public class Schedule implements Parcelable {

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

    @Ignore
    @SerializedName("games")
    @Expose
    private List<Game> gameList;

    public final static Parcelable.Creator<Schedule> CREATOR = new Creator<Schedule>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        public Schedule[] newArray(int size) {
            return (new Schedule[size]);
        }

    }
            ;

    protected Schedule(Parcel in) {
        scheduleId = in.readString();
        organizationId = in.readString();
        if (in.readByte() == 0x01) {
            gameList = new ArrayList<Game>();
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
        dest.writeByte((byte) (0x00));
    }
}
