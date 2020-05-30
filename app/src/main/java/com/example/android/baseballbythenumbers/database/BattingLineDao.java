package com.example.android.baseballbythenumbers.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.data.BattingLine;

import java.util.List;

@Dao
public interface BattingLineDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(List<BattingLine> battingLines);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(BattingLine battingLine);

        @Update
        void update(BattingLine... battingLines);

        @Delete
        void delete(BattingLine... battingLines);

        @Query("SELECT * FROM batting_line")
        LiveData<List<BattingLine>> getAllBattingLines();

        @Query("SELECT * FROM batting_line WHERE box_score_id=:boxScoreId")
        List<BattingLine> findBattingLinesForBoxScore(String boxScoreId);

        @Query("SELECT * FROM batting_line WHERE box_score_id=:boxScoreId")
        LiveData<List<BattingLine>> findLiveDataBattingLinesForBoxScore(String boxScoreId);
}
