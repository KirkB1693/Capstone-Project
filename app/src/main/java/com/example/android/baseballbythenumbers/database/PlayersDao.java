package com.example.android.baseballbythenumbers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.baseballbythenumbers.data.Player;

import java.util.List;

@Dao
public interface PlayersDao {
    @Insert
    void insert(Player player);

    @Insert
    void insertAll (List<Player> playerList);

    @Update
    void update(Player... players);

    @Update
    void updateAll(List<Player> playerList);

    @Delete
    void delete(Player... players);
    @Query("SELECT * FROM players")
    LiveData<List<Player>> getAllPlayers();

    @Query("SELECT * FROM players WHERE teamId=:teamId")
    List<Player> findPlayersForTeam(String teamId);

    @Query("SELECT * FROM players WHERE teamId=:teamId")
    LiveData<List<Player>> findLiveDataPlayersForTeam(String teamId);
}
