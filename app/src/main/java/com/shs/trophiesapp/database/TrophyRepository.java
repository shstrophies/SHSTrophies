package com.shs.trophiesapp.database;

import com.shs.trophiesapp.database.daos.TrophyAwardDao;
import com.shs.trophiesapp.database.daos.TrophyDao;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;

import java.util.List;


public class TrophyRepository {
    private TrophyDao trophyDao;
    private TrophyAwardDao trophyAwardDao;
    private static volatile TrophyRepository instance;

    private TrophyRepository(TrophyDao trophyDao,
                             TrophyAwardDao trophyAwardDao) {
        this.trophyDao = trophyDao;
        this.trophyAwardDao = trophyAwardDao;
    }

    public List<TrophyAward> getAwards() {
        return trophyAwardDao.getAwards();
    }

    public List<SportWithTrophies> getSportWithTrophies() {
        // Gets List holding @Relation object
        List<SportWithTrophies> sportwithTrophiesList = trophyDao.getSportWithTrophies();
        return sportwithTrophiesList;
    }

    public List<SportWithTrophies> getSportWithTrophiesBySportName(String sportName) {
        // Gets List holding @Relation object
        List<SportWithTrophies> sportwithTrophiesList = trophyDao.getSportWithTrophiesBySportName(sportName);
        return sportwithTrophiesList;
    }

    public List<TrophyWithAwards> getTrophiesWithAwards() {
        List<TrophyWithAwards> trophiesWithAwardsList = trophyDao.getTrophiesWithAwards();
        return trophiesWithAwardsList;
    }

    public List<TrophyAward> getTrophyAwardsByYear(int year) {
        List<TrophyAward> trophiesWithAwardsList = trophyDao.getTrophyAwardsByYear(year);
        return trophiesWithAwardsList;
    }

    public List<TrophyAward> getTrophyAwardsByPlayer(String player) {
        List<TrophyAward> trophiesWithAwardsList = trophyDao.getTrophyAwardsByPlayer("%" + player + "%");
        return trophiesWithAwardsList;
    }

    public List<TrophyAward> getTrophyAwardsBySportAndYear(long sportId, int year) {
        if(sportId == -1)
            return trophyDao.getTrophyAwardsByYear(year);
        else
            return trophyDao.getTrophyAwardsBySportAndYear(sportId, year);
    }

    public List<TrophyAward> getTrophyAwardsBySportAndPlayer(long sportId, String player) {
        if(sportId == -1)
            return getTrophyAwardsByPlayer(player);
        else
            return trophyDao.getTrophyAwardsBySportAndPlayer(sportId, "%" + player + "%");
    }

    public List<TrophyWithAwards> getTrophiesWithAwardsByYearORSportORPlayer(int year, String sportName, String player) {
        List<TrophyWithAwards> trophiesWithAwardsList = trophyDao.getTrophiesWithAwardsByYearORSportORPlayer(year, sportName, player);
        return trophiesWithAwardsList;
    }

    public Trophy getTropyById(long id) {
        return trophyDao.getTrophyById(id).get(0);
    }

    public List<TrophyWithAwards> getTrophyWithAwardsByTrophyId(long trophyId) {
        return trophyAwardDao.getAwardsByTrophyId(trophyId);
    }

    public List<TrophyAward>  getTrophiesByYear(int year) {
        return trophyAwardDao.findByYear(year);
    }

    public List<TrophyAward>  getTrophiesByPlayer(String player) {
        return trophyAwardDao.findByPlayer(player);
    }

    static TrophyRepository getInstance(
            TrophyDao trophyDao,
            TrophyAwardDao trophyAwardDao
            ) {
        if (instance == null) {
            synchronized(TrophyRepository.class) {
                if (instance == null)
                    instance = new TrophyRepository(trophyDao, trophyAwardDao);
            }
        }
        return instance;
    }
}