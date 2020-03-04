package com.shs.trophiesapp.data;

import com.shs.trophiesapp.data.daos.TrophyAwardDao;
import com.shs.trophiesapp.data.entities.TrophyAward;

import java.util.List;


public class TrophyRepository {
    private TrophyAwardDao TrophyDao;
    private static volatile TrophyRepository instance;

    private TrophyRepository(TrophyAwardDao TrophyDao) {
        this.TrophyDao = TrophyDao;
    }

    public List<TrophyAward> getTrophies() {
        return TrophyDao.getTrophies();
    }

    public List<TrophyAward>  getTrophiesBySport(String sport_name) {
        return TrophyDao.findBySport(sport_name);
    }

    public List<TrophyAward>  getTrophiesBySportAndTitle(String sport_name, String trophy_title) {
        return TrophyDao.findBySportAndTitle(sport_name, trophy_title);
    }

    public List<TrophyAward>  getTrophiesByYear(int year) {
        return TrophyDao.findByYear(year);
    }

    public List<TrophyAward>  getTrophiesByPlayer(String player) {
        return TrophyDao.findByName(player);
    }

    static TrophyRepository getInstance(TrophyAwardDao TrophyDao) {
        if (instance == null) {
            synchronized(TrophyRepository.class) {
                if (instance == null)
                    instance = new TrophyRepository(TrophyDao);
            }
        }
        return instance;
    }
}