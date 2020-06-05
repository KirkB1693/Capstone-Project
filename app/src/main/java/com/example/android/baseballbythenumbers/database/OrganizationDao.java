package com.example.android.baseballbythenumbers.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.baseballbythenumbers.data.Organization;

import java.util.List;

import static com.example.android.baseballbythenumbers.constants.TableNames.ORGANIZATION_TABLE_NAME;

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

    @Query("SELECT * from " + ORGANIZATION_TABLE_NAME + " WHERE id= :id")
    Organization getOrganizationById(String id);

    @Query("DELETE FROM " + ORGANIZATION_TABLE_NAME)
    void deleteAll();

}
