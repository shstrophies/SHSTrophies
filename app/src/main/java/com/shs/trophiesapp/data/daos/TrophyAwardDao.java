package com.shs.trophiesapp.data.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shs.trophiesapp.data.entities.TrophyAward;

import java.util.List;

@Dao public interface TrophyAwardDao {
    @Query("SELECT * FROM TrophyAward") List<TrophyAward> getAll();

    @Query("SELECT * FROM TrophyAward")
    List<TrophyAward> getTrophies();

    @Query("SELECT * FROM TrophyAward WHERE rowid IN (:userIds)") List<TrophyAward> loadAllByIds(int[] userIds);
    @Insert void insertAll(TrophyAward... trophies);
    @Delete void delete(TrophyAward trophyAward);

    // User-defined Queries
    @Query("SELECT * FROM TrophyAward WHERE title LIKE :trophy_title")
    List<TrophyAward> findByTrophyTitle(String trophy_title);
    @Query("SELECT * FROM TrophyAward WHERE Player LIKE :player") //TODO: Normalize DB Structure (IF NECESSARY)
    List<TrophyAward> findByName(String player);
    @Query("SELECT * FROM TrophyAward WHERE Year LIKE :year")
    List<TrophyAward> findByYear(int year);
    @Query("SELECT * FROM TrophyAward WHERE sportName LIKE :sport_name")
    List<TrophyAward> findBySport(String sport_name);
    @Query("SELECT * FROM TrophyAward WHERE Category LIKE :category")
    List<TrophyAward> findByCategory(String category);


    @Query("SELECT * FROM TrophyAward WHERE (sportName LIKE :sport_name) AND (Player LIKE :player)")
    List<TrophyAward> findBySportAndPlayer(String sport_name, String player);

    @Query("SELECT * FROM TrophyAward WHERE (sportName LIKE :sport_name) AND (Year LIKE :year)")
    List<TrophyAward> findBySportAndYear(String sport_name, int year);

    @Query("SELECT * FROM TrophyAward WHERE (sportName LIKE :sport_name) AND (title LIKE :trophy_title)")
    List<TrophyAward> findBySportAndTitle(String sport_name, String trophy_title);
}
