package com.example.android.baseballbythenumbers.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
        LiveData<List<BattingLine>> findLiveDataBattingLinesForBoxScore(String boxScoreId);
}
