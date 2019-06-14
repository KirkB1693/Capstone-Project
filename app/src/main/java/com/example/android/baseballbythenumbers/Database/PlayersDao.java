package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.Player;

import java.util.List;

@Dao
public interface PlayersDao {
    @Insert
    void insert(Player player);

    @Update
    void update(Player... players);

    @Delete
    void delete(Player... players);
    @Query("SELECT * FROM players")
    List<Player> getAllPlayers();

    @Query("SELECT * FROM players WHERE teamId=:teamId")
    List<Player> findPlayersForTeam(final long teamId);
}