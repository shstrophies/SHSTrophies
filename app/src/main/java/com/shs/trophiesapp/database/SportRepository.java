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