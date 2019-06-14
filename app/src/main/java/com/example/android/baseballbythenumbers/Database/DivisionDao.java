package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.Division;

import java.util.List;

@Dao
public interface DivisionDao {

    @Insert
    void insert(Division division);

    @Update
    void update(Division... divisions);

    @Delete
    void delete(Division... divisions);
    @Query("SELECT * FROM divisions")
    List<Division> getAllDivisions();

    @Query("SELECT * FROM divisions WHERE leagueId=:leagueId")
    List<Division> findDivisionsForLeague(final long leagueId);
}