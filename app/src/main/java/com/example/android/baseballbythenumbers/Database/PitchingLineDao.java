package com.example.android.baseballbythenumbers.Database;

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

        @Update
        void update(PitchingLine... pitchingLines);

        @Delete
        void delete(PitchingLine... pitchingLines);
        @Query("SELECT * FROM pitching_line")
        List<PitchingLine> getAllPitchingLines();

        @Query("SELECT * FROM pitching_line WHERE box_score_id=:boxScoreId")
        List<PitchingLine> findPitchingLinesForBoxScore(final int boxScoreId);
}
