package com.shs.trophiesapp.data.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shs.trophiesapp.data.entities.Sport;

import java.util.List;

@Dao public interface SportDao {

    @Query("SELECT * FROM sport") List<Sport> getAll();

    @Query("SELECT * FROM sport")
    List<Sport> getSports();

    @Query("SELECT * FROM sport WHERE rowId IN (:userIds)") List<Sport> loadAllByIds(int[] userIds);
    @Query("SELECT * FROM sport WHERE Sports LIKE :sport_name AND " +
            "Image_URL LIKE :image_url LIMIT 1") Sport findByName(String sport_name, String image_url);
    @Insert void insertAll(Sport... sports);
    @Delete void delete(Sport sport);
}
