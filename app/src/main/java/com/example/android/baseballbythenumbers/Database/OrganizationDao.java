package com.example.android.baseballbythenumbers.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.baseballbythenumbers.Data.Organization;

import java.util.List;

import static com.example.android.baseballbythenumbers.Constants.TableNames.ORGANIZATION_TABLE_NAME;

@Dao
public interface OrganizationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Organization organization);

    @Update
    void update(Organization... organizations);

    @Delete
    void delete(Organization... organizations);

    @Query("SELECT * FROM " + ORGANIZATION_TABLE_NAME)
    LiveData<List<Organization>> getAllOrganizations();

    @Query("SELECT * from " + ORGANIZATION_TABLE_NAME + " LIMIT 1")
    Organization[] getAnyOrganization();

    @Query("DELETE FROM " + ORGANIZATION_TABLE_NAME)
    void deleteAll();

}
