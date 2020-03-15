package com.shs.trophiesapp.database.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class TrophyDao {

    @Query("SELECT * FROM sport")
    public abstract List<Sport> getAllSports();

    @Transaction
    @Query("SELECT * FROM trophy WHERE id LIKE :id")
    public abstract List<Trophy> getTrophyById(long id);

    @Transaction
    @Query("SELECT * FROM sport")
    public abstract List<SportWithTrophies> getSportWithTrophies();


    @Transaction
    @Query("SELECT * FROM sport WHERE name LIKE :sportName")
    public abstract List<SportWithTrophies> getSportWithTrophiesBySportName(String sportName);

    @Transaction
    @Query("SELECT * FROM trophy t")
    public abstract List<TrophyWithAwards> getTrophiesWithAwards();

    @Query("SELECT * FROM trophyaward")
    public abstract List<TrophyAward> getAllTrophyAwards();

    @Query("SELECT * FROM trophyaward WHERE (year = :year)")
    public abstract List<TrophyAward> getTrophyAwardsByYear(int year);

    @Query("SELECT * FROM trophyaward WHERE (player LIKE :player)")
    public abstract List<TrophyAward> getTrophyAwardsByPlayer(String player);

    @Query("SELECT * FROM trophyaward JOIN trophy ON trophy.id = trophyaward.trophyId WHERE (trophy.sportId LIKE :sportId) AND (player LIKE :player)")
    public abstract List<TrophyAward> getTrophyAwardsBySportAndPlayer(long sportId, String player);

    @Query("SELECT * FROM trophyaward JOIN trophy ON trophy.id = trophyaward.trophyId WHERE (trophy.sportId LIKE :sportId) AND (year LIKE :year)")
    public abstract List<TrophyAward> getTrophyAwardsBySportAndYear(long sportId, int year);

    // SELECT s.name as sportName, t.id as trophyid, t.title, t.url, year, player  FROM trophy t join trophyaward a ON a.trophyid = t.id JOIN sport s ON s.id = t.sportid WHERE (a.year = 1976) OR (s.name like 'ba') OR (a.player like 'ba')
    @Transaction
    @Query("SELECT * FROM trophy t join trophyaward a ON a.trophyid = t.id JOIN sport s ON s.id = t.sportid WHERE (a.year = :year) OR (s.name LIKE :sportName) OR (a.player LIKE :player)")
    public abstract List<TrophyWithAwards> getTrophiesWithAwardsByYearORSportORPlayer(int year, String sportName, String player);

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    @Transaction
    public void insert(Sport sport, List<Trophy> trophies) {

        // Save rowId of inserted CompanyEntity as companyId
        final long sportId = insert(sport);

        // Set companyId for all related employeeEntities
        for (Trophy trophy : trophies) {
            trophy.setSportId(sportId);
            insert(trophy);
        }

    }

    // If the @Insert method receives only 1 parameter, it can return a long,
    // which is the new rowId for the inserted item.
    // https://developer.android.com/training/data-storage/room/accessing-data
    @Insert(onConflict = REPLACE)
    public abstract long insert(Sport Sport);

    @Insert
    public abstract long insert(Trophy trophy);

}
