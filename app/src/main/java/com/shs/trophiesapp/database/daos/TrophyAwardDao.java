package com.shs.trophiesapp.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;

import java.util.List;

@Dao public interface TrophyAwardDao {
    @Query("SELECT * FROM TrophyAward") List<TrophyAward> getAll();

    @Query("SELECT * FROM TrophyAward")
    List<TrophyAward> getAwards();

    @Insert void insertAll(TrophyAward... trophies);
    @Delete void delete(TrophyAward trophyAward);

    @Transaction
    @Query("SELECT * FROM trophy WHERE id LIKE :trophyId")
    public abstract List<TrophyWithAwards> getAwardsByTrophyId(long trophyId);

    // User-defined Queries
    @Query("SELECT * FROM TrophyAward WHERE Year LIKE :year")
    List<TrophyAward> findByYear(int year);
    @Query("SELECT * FROM TrophyAward WHERE (player LIKE :player)")
    List<TrophyAward> findByPlayer(String player);
    @Query("SELECT * FROM TrophyAward WHERE Category LIKE :category")
    List<TrophyAward> findByCategory(String category);

    //Enumerated Queries
    @Transaction
    @Query("SELECT * FROM TrophyAward " +
            "WHERE id LIKE :id " +
            "ORDER BY year ASC " +
            "LIMIT :limit " +
            "OFFSET ((:page - 1) * :limit)")
    List<TrophyAward> findByIdLimited(long id, int limit, int page);

    @Transaction
    @Query("SELECT * FROM TrophyAward " +
            "WHERE year LIKE :year " +
            "ORDER BY trophyId ASC " +
            "LIMIT :limit " +
            "OFFSET ((:page - 1) * :limit)")
    List<TrophyAward> findByYearLimited(int year, int limit, int page); //Ordered by Trophy ID – ordering by Trophy Name requires INNER JOIN or DB View

    @Transaction
    @Query("SELECT * FROM TrophyAward " +
            "WHERE player LIKE :player " +
            "ORDER BY year ASC " +
            "LIMIT :limit " +
            "OFFSET ((:page - 1) * :limit)")
    List<TrophyAward> findByPlayer(String player, int limit, int page); //Ordered by year – ordering by Trophy Name requires INNER JOIN or DB View
}
