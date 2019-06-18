package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "schedules", foreignKeys = @ForeignKey(entity = Organization.class, parentColumns = "id", childColumns = "organization_id",onDelete = CASCADE), indices = @Index(value = "organization_id"))
public class Schedule {

    @ColumnInfo(name = "organization_id")
    private int organizationId;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    private int scheduleId;

    @Ignore
    private List<Game> gameList;

    public Schedule(){
    }

    @Ignore
    public Schedule(int organizationId, List<Game> games){
        this.organizationId = organizationId;
        this.gameList = games;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }
}
