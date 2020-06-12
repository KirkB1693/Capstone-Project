package com.example.android.baseballbythenumbers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.baseballbythenumbers.data.PitchingStats;

import java.util.List;

@Dao
public interface PitchingStatsDao {
    @Insert
    void insert(PitchingStats pitchingStats);

    @Insert
    void insertAll(List<PitchingStats> pitchingStatsList);

    @Update
    void update(PitchingStats... pitchingStats);

    @Update
    void updateAll(List<PitchingStats> pitchingStatsList);

    @Delete
    void delete(PitchingStats... pitchingStats);
    @Query("SELECT * FROM pitching_stats")
    LiveData<List<PitchingStats>> getAllPitchingStats();

    @Query("SELECT * FROM pitching_stats WHERE playerId=:playerId")
    List<PitchingStats> findPitchingStatsForPlayer(String playerId);

}
