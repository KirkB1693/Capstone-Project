package com.example.android.baseballbythenumbers.Database;

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

        @Update
        void update(Game... games);

        @Delete
        void delete(Game... games);
        @Query("SELECT * FROM games")
        List<Game> getAllGames();

        @Query("SELECT * FROM games WHERE schedule_id=:scheduleId")
        List<Game> findGamesForSchedule(final int scheduleId);

}