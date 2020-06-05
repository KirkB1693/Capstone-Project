package com.example.android.baseballbythenumbers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
