package com.example.android.baseballbythenumbers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.baseballbythenumbers.data.Division;

import java.util.List;

@Dao
public interface DivisionDao {

    @Insert
    void insert(Division division);

    @Insert
    void insertAll (List<Division> divisionList);

    @Update
    void update(Division... divisions);

    @Delete
    void delete(Division... divisions);
    @Query("SELECT * FROM divisions")
    LiveData<List<Division>> getAllDivisions();

    @Query("SELECT * FROM divisions WHERE leagueId=:leagueId")
    List<Division> findDivisionsForLeague(String leagueId);
}
