package com.example.android.baseballbythenumbers.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.data.Team;

import java.util.List;

@Dao
public interface TeamDao {

    @Insert
    void insert(Team team);

    @Insert
    void insertAll(List<Team> teamList);

    @Update
    void update(Team... teams);

    @Update
    void updateAll(List<Team> teamList);

    @Delete
    void delete(Team... teams);

    @Query("SELECT * FROM teams")
    LiveData<List<Team>> getAllTeams();

    @Query("SELECT * FROM teams WHERE divisionId=:divisionId")
    List<Team> findTeamsForDivision(String divisionId);

    @Query("SELECT * FROM teams WHERE teamId=:teamId")
    Team getTeamWithTeamId(String teamId);

    @Query("SELECT * FROM teams WHERE teamName=:teamName")
    Team getTeamWithTeamName(String teamName);

    @Query("SELECT * FROM teams WHERE teamName=:teamName")
    LiveData<Team> getLiveDataTeamWithTeamName(String teamName);
}
