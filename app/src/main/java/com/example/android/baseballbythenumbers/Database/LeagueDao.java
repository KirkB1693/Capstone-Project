package com.example.android.baseballbythenumbers.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.League;

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
    List<League> findLeaguesForOrganization(final long orgId);
}
