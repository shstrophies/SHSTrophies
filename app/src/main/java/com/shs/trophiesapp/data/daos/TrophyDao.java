package com.shs.trophiesapp.data.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shs.trophiesapp.data.entities.Trophy;

import java.util.List;

@Dao public interface TrophyDao {
    @Query("SELECT * FROM trophy") List<Trophy> getAll();

    @Query("SELECT * FROM trophy")
    List<Trophy> getTrophies();

    @Query("SELECT * FROM trophy WHERE rowid IN (:userIds)") List<Trophy> loadAllByIds(int[] userIds);
    @Insert void insertAll(Trophy... trophies);
    @Delete void delete(Trophy trophy);

    // User-defined Queries
    @Query("SELECT * FROM trophy WHERE Trophy_Title LIKE :tr_title")
    List<Trophy> findByTrophyTitle(String tr_title);
    @Query("SELECT * FROM trophy WHERE Player_Name LIKE :player") //TODO: Normalize DB Structure (IF NECESSARY)
    List<Trophy> findByName(String player);
    @Query("SELECT * FROM trophy WHERE Year LIKE :year")
    List<Trophy> findByYear(int year);
    @Query("SELECT * FROM trophy WHERE Sport LIKE :sport_name")
    List<Trophy> findBySport(String sport_name);
    @Query("SELECT * FROM trophy WHERE Category LIKE :category")
    List<Trophy> findByCategory(String category);


    @Query("SELECT * FROM trophy WHERE (Sport LIKE :sport_name) AND (Player_Name LIKE :player)")
    List<Trophy> findBySportAndPlayer(String sport_name, String player);

    @Query("SELECT * FROM trophy WHERE (Sport LIKE :sport_name) AND (Year LIKE :year)")
    List<Trophy> findBySportAndYear(String sport_name, int year);
}
