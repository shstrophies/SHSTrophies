package com.shs.trophiesapp.data;

import com.shs.trophiesapp.data.daos.TrophyAwardDao;
import com.shs.trophiesapp.data.daos.TrophyDao;
import com.shs.trophiesapp.data.entities.SportWithTrophies;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.data.entities.TrophyAward;

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
        return trophyDao.getTrophiesBySportName(sport_name);
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