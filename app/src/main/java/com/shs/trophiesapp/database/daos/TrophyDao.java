package com.shs.trophiesapp.database.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.entities.Trophy;

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
    @Query("SELECT * FROM sport WHERE name LIKE :sportName")
    public abstract List<SportWithTrophies> getTrophiesBySportName(String sportName);

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
