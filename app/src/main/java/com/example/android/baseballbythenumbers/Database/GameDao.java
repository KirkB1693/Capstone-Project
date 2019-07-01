package com.example.android.baseballbythenumbers.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.Game;

import java.util.List;

@Dao
public interface GameDao {
        @Insert
        void insert(Game game);

        @Insert
        void insertAll(List<Game> gamesList);

        @Update
        int update(Game... games);

        @Delete
        void delete(Game... games);

        @Query("SELECT * FROM games")
        LiveData<List<Game>> getAllGames();

        @Query("SELECT * FROM games WHERE schedule_id=:scheduleId")
        List<Game> findGamesForSchedule(String scheduleId);

        @Query("SELECT * FROM games WHERE day=:day AND schedule_id=:scheduleId")
        List<Game> findGamesForDayInSchedule(final int day, String scheduleId);

        @Query("SELECT * FROM games WHERE (home_team_name=:teamName OR visiting_team_name=:teamName) AND schedule_id=:scheduleId")
        List<Game> findGamesForTeamNameInSchedule(String teamName, String scheduleId);
}
