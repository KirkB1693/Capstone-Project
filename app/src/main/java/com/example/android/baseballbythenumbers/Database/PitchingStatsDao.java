package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.PitchingStats;

import java.util.List;

@Dao
public interface PitchingStatsDao {
    @Insert
    void insert(PitchingStats pitchingStats);

    @Update
    void update(PitchingStats... pitchingStats);

    @Delete
    void delete(PitchingStats... pitchingStats);
    @Query("SELECT * FROM pitching_stats")
    List<PitchingStats> getAllPitchingStats();

    @Query("SELECT * FROM pitching_stats WHERE playerId=:playerId")
    List<PitchingStats> findPitchingStatsForPlayer(final long playerId);
}
