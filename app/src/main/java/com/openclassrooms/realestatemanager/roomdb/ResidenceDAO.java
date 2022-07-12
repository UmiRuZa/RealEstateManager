package com.openclassrooms.realestatemanager.roomdb;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.placeholder.PlaceholderContent;

import java.util.List;

@Dao
public interface ResidenceDAO {

    @Query("SELECT * FROM residence")
    List<PlaceholderContent.PlaceholderItem> getAll();

    @Query("SELECT * FROM residence WHERE resid_id = :residenceIDs")
    LiveData<List<PlaceholderContent.PlaceholderItem>> getItems(long residenceIDs);

    @Insert
    long insertAll(PlaceholderContent.PlaceholderItem residence);

    @Query("DELETE FROM residence WHERE resid_id = :residenceIDs")
    int deleteResidence(long residenceIDs);

    @Update
    int update(PlaceholderContent.PlaceholderItem residence);

    @Query("SELECT * FROM residence WHERE resid_id = :residenceIDs")
    Cursor getItemsWithCursor(long residenceIDs);
}
