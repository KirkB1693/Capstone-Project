package com.example.android.baseballbythenumbers.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "schedules", foreignKeys = @ForeignKey(entity = Organization.class, parentColumns = "id", childColumns = "organization_id",onDelete = CASCADE), indices = @Index(value = "organization_id"))
public class Schedule {

    @ColumnInfo(name = "organization_id")
    private String organizationId;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "schedule_id")
    private String scheduleId;

    @Ignore
    private List<Game> gameList;

    public Schedule(){
    }

    @Ignore
    public Schedule(String organizationId, List<Game> games){
        this.organizationId = organizationId;
        this.gameList = games;
        this.scheduleId = UUID.randomUUID().toString();
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
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
}
