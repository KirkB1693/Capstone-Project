package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.BoxScore;

import java.util.List;

@Dao
public interface BoxScoreDao {
        @Insert
        void insert(BoxScore boxScore);

        @Update
        void update(BoxScore... boxScores);

        @Delete
        void delete(BoxScore... boxScores);
        @Query("SELECT * FROM box_scores")
        List<BoxScore> getAllBoxScores();

        @Query("SELECT * FROM box_scores WHERE game_id=:gameId")
        List<BoxScore> findBoxScoresForGame(final int gameId);
}
