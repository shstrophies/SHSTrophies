package com.shs.trophiesapp.data;

import com.shs.trophiesapp.data.daos.TrophyDao;
import com.shs.trophiesapp.data.entities.Trophy;

import java.util.List;


public class TrophyRepository {
    private TrophyDao TrophyDao;
    private static volatile TrophyRepository instance;

    private TrophyRepository(TrophyDao TrophyDao) {
        this.TrophyDao = TrophyDao;
    }

    public List<Trophy> getTrophies() {
        return TrophyDao.getTrophies();
    }

    public List<Trophy>  getTrophiesBySport(String sport_name) {
        return TrophyDao.findBySport(sport_name);
    }

    static TrophyRepository getInstance(TrophyDao TrophyDao) {
        if (instance == null) {
            synchronized(TrophyRepository.class) {
                if (instance == null)
                    instance = new TrophyRepository(TrophyDao);
            }
        }
        return instance;
    }
}