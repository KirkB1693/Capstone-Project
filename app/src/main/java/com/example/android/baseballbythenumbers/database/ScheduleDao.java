package com.example.android.baseballbythenumbers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.baseballbythenumbers.data.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {

        @Insert
        void insert(Schedule schedule);

        @Insert
        void insertAll(List<Schedule> schedules);

        @Update
        void update(Schedule... schedules);

        @Delete
        void delete(Schedule... schedules);

        @Query("SELECT * FROM schedules")
        LiveData<List<Schedule>> getAllSchedules();

        @Query("SELECT * FROM schedules WHERE organization_id=:orgId")
        List<Schedule> getSchedulesForOrganization(String orgId);
}
