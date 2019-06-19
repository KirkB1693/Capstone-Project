package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {

        @Insert
        void insert(Schedule schedule);

        @Update
        void update(Schedule... schedules);

        @Delete
        void delete(Schedule... schedules);
        @Query("SELECT * FROM schedules")
        List<Schedule> getAllSchedules();

        @Query("SELECT * FROM schedules WHERE organization_id=:orgId")
        List<Schedule> findSchedulesForOrganization(final int orgId);
}
