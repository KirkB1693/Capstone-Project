package com.example.android.baseballbythenumbers.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.BattingLine;

import java.util.List;

@Dao
public interface BattingLineDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(List<BattingLine> battingLines);

        @Insert
        void insert(BattingLine battingLine);

        @Update
        void update(BattingLine... battingLines);

        @Delete
        void delete(BattingLine... battingLines);

        @Query("SELECT * FROM batting_line")
        LiveData<List<BattingLine>> getAllBattingLines();

        @Query("SELECT * FROM batting_line WHERE box_score_id=:boxScoreId")
        List<BattingLine> findBattingLinesForBoxScore(final String boxScoreId);
}
