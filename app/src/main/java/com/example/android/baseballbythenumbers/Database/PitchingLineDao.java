package com.example.android.baseballbythenumbers.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.PitchingLine;

import java.util.List;

@Dao
public interface PitchingLineDao {
        @Insert
        void insert(PitchingLine pitchingLine);

        @Insert
        void insertAll(List<PitchingLine> pitchingLineList);

        @Update
        void update(PitchingLine... pitchingLines);

        @Delete
        void delete(PitchingLine... pitchingLines);

        @Query("SELECT * FROM pitching_line")
        LiveData<List<PitchingLine>> getAllPitchingLines();

        @Query("SELECT * FROM pitching_line WHERE box_score_id=:boxScoreId")
        List<PitchingLine> findPitchingLinesForBoxScore(String boxScoreId);

        @Query("SELECT * FROM pitching_line WHERE box_score_id=:boxScoreId")
        LiveData<List<PitchingLine>> findLiveDataPitchingLinesForBoxScore(String boxScoreId);
}
