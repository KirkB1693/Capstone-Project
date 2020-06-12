package com.example.android.baseballbythenumbers.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.baseballbythenumbers.data.BattingStats;

import java.util.List;

@Dao
public interface BattingStatsDao {
    @Insert
    void insert(BattingStats battingStats);

    @Insert
    void insertAll(List<BattingStats> battingStatsList);

    @Update
    void update(BattingStats... battingStats);

    @Update
    void updateAll(List<BattingStats> battingStatsList);

    @Delete
    void delete(BattingStats... battingStats);
    @Query("SELECT * FROM batting_stats")
    LiveData<List<BattingStats>> getAllBattingStats();

    @Query("SELECT * FROM batting_stats WHERE playerId=:playerId")
    List<BattingStats> findBattingStatsForPlayer(String playerId);

}
