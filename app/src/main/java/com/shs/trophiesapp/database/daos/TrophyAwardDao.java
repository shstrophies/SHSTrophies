package com.shs.trophiesapp.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.generators.YearRange;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao public interface TrophyAwardDao {
    @Query("SELECT * FROM TrophyAward") List<TrophyAward> getAll();

    @Query("SELECT * FROM TrophyAward")
    List<TrophyAward> getAwards();

    @Insert(onConflict = REPLACE)
    long insert(TrophyAward award);
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

    @Query("SELECT player FROM TrophyAward WHERE (player LIKE :player) LIMIT :limit")
    List<String> searchPlayerName(String player, int limit);


    // select year from TrophyAward where ( year<1979 and  year>1970  )
    @Query("SELECT year FROM TrophyAward WHERE ( year >= :from AND year <= :to  ) LIMIT :limit")
    List<Integer> searchYear(int from, int to, int limit);



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
            "INNER JOIN Trophy ON trophy.id=trophyId " +
            "WHERE year LIKE :year " +
            "ORDER BY trophy.title ASC " +
            "LIMIT :limit " +
            "OFFSET ((:page - 1) * :limit)")
    List<TrophyAward> findByYearLimited(int year, int limit, int page);

    @Transaction
    @Query("SELECT * FROM TrophyAward " +
            "WHERE player LIKE :player " +
            "ORDER BY year ASC " +
            "LIMIT :limit " +
            "OFFSET ((:page - 1) * :limit)")
    List<TrophyAward> findByPlayerLimited(String player, int limit, int page); //Ordered by year – ordering by Trophy Name requires INNER JOIN or DB View
}
