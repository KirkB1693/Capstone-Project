package com.example.android.baseballbythenumbers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.baseballbythenumbers.data.League;

import java.util.List;

@Dao
public interface LeagueDao {

    @Insert
    void insert(League league);

    @Insert
    void insertAll(List<League> leagueList);

    @Update
    void update(League... leagues);

    @Delete
    void delete(League... leagues);
    @Query("SELECT * FROM leagues")
    LiveData<List<League>> getAllLeagues();

    @Query("SELECT * FROM leagues WHERE orgId=:orgId")
    List<League> findLeaguesForOrganization(String orgId);
}
