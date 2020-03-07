package com.shs.trophiesapp.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shs.trophiesapp.database.entities.Sport;

import java.util.List;

@Dao public interface SportDao {

    @Query("SELECT * FROM sport") List<Sport> getAll();

    @Query("SELECT * FROM sport")
    List<Sport> getSports();

    @Query("SELECT * FROM sport WHERE rowId IN (:userIds)") List<Sport> loadAllByIds(int[] userIds);
    @Query("SELECT * FROM sport WHERE name LIKE :sportName AND " +
            "imageUrl LIKE :imageUrl LIMIT 1") Sport findByName(String sportName, String imageUrl);
    @Insert void insertAll(Sport... sports);
    @Delete void delete(Sport sport);
}
