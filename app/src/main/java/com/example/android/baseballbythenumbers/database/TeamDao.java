package com.example.android.baseballbythenumbers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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


    @Query("SELECT * FROM teams WHERE teamName=:teamName")
    Team getTeamWithTeamName(String teamName);

}
