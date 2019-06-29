package com.example.android.baseballbythenumbers.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.Team;

import java.util.List;

@Dao
public interface TeamDao {

    @Insert
    void insert(Team team);

    @Insert
    void insertAll(List<Team> teamList);

    @Update
    void update(Team... teams);

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
}
