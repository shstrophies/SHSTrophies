package com.shs.trophiesapp.data;

import com.shs.trophiesapp.data.daos.TrophyAwardDao;
import com.shs.trophiesapp.data.daos.TrophyDao;
import com.shs.trophiesapp.data.entities.SportWithTrophies;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.data.entities.TrophyAward;

import java.util.ArrayList;
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

    public List<TrophyAward> getTrophies() {
        return trophyAwardDao.getTrophies();
    }

    public List<SportWithTrophies>  getTrophiesBySport(String sport_name) {
        // Gets List holding @Relation object
        List<SportWithTrophies> sportwithTrophiesList = trophyDao.getTrophiesBySportName(sport_name);

        final List<Trophy> trophies = new ArrayList<>();

        if (!sportwithTrophiesList.isEmpty()) {
            // Room always returns List or Set when @Relation is used
            // https://issuetracker.google.com/issues/62905145
            // So we get first element from it
            SportWithTrophies relationHolder = sportwithTrophiesList.get(0);
            List<Trophy> trophyEntities =
                    relationHolder.trophies;
            for (Trophy trophy : trophyEntities) {
                trophies.add(trophy);
            }
        }
        return sportwithTrophiesList;
    }

    public List<TrophyAward>  getTrophiesBySportAndTitle(String sport_name, String trophy_title) {
        return trophyAwardDao.findBySportAndTitle(sport_name, trophy_title);
    }

    public List<TrophyAward>  getTrophiesByYear(int year) {
        return trophyAwardDao.findByYear(year);
    }

    public List<TrophyAward>  getTrophiesByPlayer(String player) {
        return trophyAwardDao.findByName(player);
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