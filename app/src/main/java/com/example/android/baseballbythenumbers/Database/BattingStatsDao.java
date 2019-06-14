package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.BattingStats;

import java.util.List;

@Dao
public interface BattingStatsDao {
    @Insert
    void insert(BattingStats battingStats);

    @Update
    void update(BattingStats... battingStats);

    @Delete
    void delete(BattingStats... battingStats);
    @Query("SELECT * FROM batting_stats")
    List<BattingStats> getAllBattingStats();

    @Query("SELECT * FROM batting_stats WHERE playerId=:playerId")
    List<BattingStats> findBattingStatsForPlayer(final int playerId);
}