package com.example.android.baseballbythenumbers.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.data.BoxScore;

import java.util.List;

@Dao
public interface BoxScoreDao {
        @Insert
        void insert(BoxScore boxScore);

        @Update
        void update(BoxScore... boxScores);

        @Update
        void updateAll(List<BoxScore> boxScoreList);

        @Delete
        void delete(BoxScore... boxScores);

        @Query("SELECT * FROM box_scores")
        LiveData<List<BoxScore>> getAllBoxScores();

        @Query("SELECT * FROM box_scores WHERE game_id=:gameId")
        List<BoxScore> findBoxScoresForGame(String gameId);

        @Query("SELECT * FROM box_scores WHERE game_id=:gameId")
        LiveData<List<BoxScore>> findLiveDataBoxScoresForGame(String gameId);

        @Query("SELECT * FROM box_scores WHERE box_score_id=:boxScoreId")
        LiveData<BoxScore> findLiveDataBoxScoreWithBoxScoreId(String boxScoreId);
}
