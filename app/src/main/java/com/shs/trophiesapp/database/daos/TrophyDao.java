package com.shs.trophiesapp.database.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

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
    @Query("SELECT * FROM sport ORDER BY name ASC LIMIT :limit OFFSET ((:page - 1) * :limit)")
    public abstract List<SportWithTrophies> getSportWithTrophiesLimited(int limit, int page);

    @Transaction
    @Query("SELECT * FROM sport WHERE name LIKE :sportName")
    public abstract List<SportWithTrophies> getSportWithTrophiesBySportName(String sportName);

    @Transaction
    @Query("SELECT * FROM sport INNER JOIN trophy ON trophy.sportId=sport.id WHERE name LIKE :sportName ORDER BY trophy.title ASC LIMIT :limit OFFSET ((:page - 1) * :limit)")
    public abstract List<SportWithTrophies> getSportWithTrophiesBySportNameLimited(String sportName, int limit, int page);

    @Transaction
    @Query("SELECT * FROM trophy")
    public abstract List<TrophyWithAwards> getTrophiesWithAwards();

    @Query("SELECT * FROM trophyaward")
    public abstract List<TrophyAward> getAllTrophyAwards();

    @Query("SELECT * FROM trophyaward WHERE (year = :year)")
    public abstract List<TrophyAward> getTrophyAwardsByYear(int year);

    @Transaction
    @Query("SELECT * FROM trophyaward INNER JOIN trophy ON trophy.id = trophyId WHERE (year LIKE :year) ORDER BY trophy.title ASC LIMIT :limit OFFSET ((:page - 1) * :limit)")
    public abstract List<TrophyAward> getTrophyAwardsByYearLimited(int year, int limit, int page);

    @Query("SELECT * FROM trophyaward WHERE (player LIKE :player)")
    public abstract List<TrophyAward> getTrophyAwardsByPlayer(String player);

    @Transaction
    @Query("SELECT * FROM trophyaward WHERE (player LIKE :player) ORDER BY year ASC LIMIT :limit OFFSET ((:page - 1) * :limit)")
    public abstract List<TrophyAward> getTrophyAwardsByPlayerLimited(String player, int limit, int page);

    @Query("SELECT * FROM trophyaward JOIN trophy ON trophy.id = trophyId WHERE (trophy.sportId LIKE :sportId) AND (player LIKE :player)")
    public abstract List<TrophyAward> getTrophyAwardsBySportAndPlayer(long sportId, String player);

    @Transaction
    @Query("SELECT * FROM TrophyAward INNER JOIN trophy ON trophy.id = trophyId WHERE ((trophy.sportId=:sportId) AND (player LIKE :player)) ORDER BY year ASC LIMIT :limit OFFSET ((:page - 1) * :limit)")
    public abstract List<TrophyAward> getTrophyAwardsBySportAndPlayerLimited(long sportId, String player, int limit, int page);

    @Query("SELECT * FROM trophyaward JOIN trophy ON trophy.id = trophyId WHERE (trophy.sportId LIKE :sportId) AND (year LIKE :year)")
    public abstract List<TrophyAward> getTrophyAwardsBySportAndYear(long sportId, int year);

// select * from trophyaward where year in (1970,1971);
//select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6));
//select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (year IN (1961, 1983, 1992));
//select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (year IN (1961, 1983, 1992)) AND ((player LIKE '%glen%') OR (player like '%Joy%'));
//select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (year IN (1961, 1983, 1992)) AND ((player LIKE '%%'));
//select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (title LIKE '%inspirational%')
    @RawQuery
    public abstract List<TrophyAward> getTrophyAwardsByExpression(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE ((trophy.sportId LIKE :sportId) AND (year LIKE :year)) ORDER BY trophy.title ASC LIMIT :limit OFFSET ((:page - 1) * :limit)")
    public abstract List<TrophyAward> getTrophyAwardsBySportAndYearLimited(long sportId, int year, int limit, int page);

    // SELECT s.name as sportName, t.id as trophyid, t.title, t.url, year, player  FROM trophy t join trophyaward a ON a.trophyid = t.id JOIN sport s ON s.id = t.sportid WHERE (a.year = 1976) OR (s.name like 'ba') OR (a.player like 'ba')
    @Transaction
    @Query("SELECT * FROM trophy t join trophyaward a ON a.trophyid = t.id JOIN sport s ON s.id = t.sportid WHERE (a.year = :year) OR (s.name LIKE :sportName) OR (a.player LIKE :player)")
    public abstract List<TrophyWithAwards> getTrophiesWithAwardsByYearORSportORPlayer(int year, String sportName, String player);

    @Query("SELECT title FROM Trophy WHERE (title LIKE :title) LIMIT :limit")
    public abstract List<String> searchTrophyTitle(String title, int limit);

    @Query("SELECT title FROM trophy t INNER JOIN sport s ON s.id=t.sportId WHERE (t.title LIKE :title) AND (s.name LIKE :sportName) LIMIT :limit")
    public abstract List<String> searchTrophyTitleInSport(String title, String sportName, int limit);

    @Transaction
    @Query("SELECT * FROM trophy t INNER JOIN trophyaward ta ON ta.trophyId=t.id INNER JOIN sport s ON s.id=t.sportId WHERE ((ta.year = :year) OR (s.name LIKE :sportName) OR (ta.player LIKE :player)) ORDER BY ta.id ASC LIMIT :limit OFFSET ((:page - 1) * :limit)")
    public abstract List<TrophyWithAwards> getTrophiesWithAwardsByYearORSportORPlayerLimited(int year, String sportName, String player, int limit, int page);


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
