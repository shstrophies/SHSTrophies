package com.shs.trophiesapp.database;

import com.shs.trophiesapp.database.daos.TrophyAwardDao;
import com.shs.trophiesapp.database.daos.TrophyDao;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.utils.Constants;

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
        return trophyDao.getSportWithTrophies();
    }

    public List<SportWithTrophies> getSportWithTrophiesLimited(int limit, int page) {
        return trophyDao.getSportWithTrophiesLimited(limit, page);
    }

    public List<SportWithTrophies> getSportWithTrophiesBySportName(String sportName) {
        // Gets List holding @Relation object
        return trophyDao.getSportWithTrophiesBySportName(sportName);
    }

    public List<SportWithTrophies> getSportWithTrophiesBySportNameLimited(String sportName, int limit, int page) {
        return trophyDao.getSportWithTrophiesBySportNameLimited(sportName, limit, page);
    }

    public List<TrophyWithAwards> getTrophiesWithAwards() {
        return trophyDao.getTrophiesWithAwards();
    }

    public List<TrophyAward> getTrophyAwardsByYear(int year) {
        return trophyDao.getTrophyAwardsByYear(year);
    }

    public List<TrophyAward> getTrophyAwardsByPlayer(String player) {
        return trophyDao.getTrophyAwardsByPlayer("%" + player + "%");
    }

    public List<TrophyAward> getTrophyAwardsByPlayerLimited(String player, int page) {
        return trophyAwardDao.findByPlayerLimited(player, Constants.TROPHIES_PER_PAGE, page);
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
        return trophyDao.getTrophiesWithAwardsByYearORSportORPlayer(year, sportName, player);
    }

    public Trophy getTropyById(long id) {
        return trophyDao.getTrophyById(id).get(0);
    }

    public List<TrophyWithAwards> getTrophyWithAwardsByTrophyId(long trophyId) {
        return trophyAwardDao.getAwardsByTrophyId(trophyId);
    }

    public List<TrophyAward> getTrophyWithAwardsByTrophyIdLimited(long trophyId, int limit, int page) {
        return trophyAwardDao.getAwardsByTrophyIdLimited(trophyId, limit, page);
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