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

    public List<SportWithTrophies>  getTrophiesBySport(String sport_name) {
        // Gets List holding @Relation object
        List<SportWithTrophies> sportwithTrophiesList = trophyDao.getTrophiesBySportName(sport_name);

//        final List<Trophy> trophies = new ArrayList<>();
//
//        if (!sportwithTrophiesList.isEmpty()) {
//            // Room always returns List or Set when @Relation is used
//            // https://issuetracker.google.com/issues/62905145
//            // So we get first element from it
//            SportWithTrophies relationHolder = sportwithTrophiesList.get(0);
//            List<Trophy> trophyEntities =
//                    relationHolder.trophies;
//            for (Trophy trophy : trophyEntities) {
//                trophies.add(trophy);
//            }
//        }
//        return sportwithTrophiesList;
        return sportwithTrophiesList;
    }

    public Trophy getTropyById(long id) {
        return trophyDao.getTrophyById(id).get(0);
    }

    public List<TrophyWithAwards>  getAwardsByTrophyId(long trophyId) {
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