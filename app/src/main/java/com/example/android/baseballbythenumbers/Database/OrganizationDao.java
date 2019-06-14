package com.example.android.baseballbythenumbers.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.Organization;

import java.util.List;

@Dao
public interface OrganizationDao {
    @Insert
    void insert(Organization organization);

    @Update
    void update(Organization... organizations);

    @Delete
    void delete(Organization... organizations);
    @Query("SELECT * FROM organizations")
    List<Organization> getAllOrganizations();

}
