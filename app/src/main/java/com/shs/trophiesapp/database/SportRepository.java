package com.shs.trophiesapp.database;

import com.shs.trophiesapp.database.daos.SportDao;
import com.shs.trophiesapp.database.entities.Sport;

import java.util.List;

public class SportRepository {
    private SportDao sportDao;
    private static volatile SportRepository instance;

    private SportRepository(SportDao SportDao) {
        this.sportDao = SportDao;
    }

    public List<Sport> getSports() {
        return sportDao.getSports();
    }

    public Sport getSportById(long id) {
        return sportDao.getSportById(id).get(0);
    }

    public List<String>  searchSportName(String str, int limit) {
        return sportDao.searchSportName("%" + str + "%", limit);
    }

    static SportRepository getInstance(SportDao SportDao) {
        if (instance == null) {
            synchronized(SportRepository.class) {
                if (instance == null)
                    instance = new SportRepository(SportDao);
            }
        }
        return instance;
    }
}