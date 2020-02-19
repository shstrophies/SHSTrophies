package com.shs.trophiesapp.data;

import com.shs.trophiesapp.data.daos.SportDao;
import com.shs.trophiesapp.data.entities.Sport;

import java.util.List;



public class SportRepository {
    private SportDao sportDao;
    private static volatile SportRepository instance;

    SportRepository(SportDao SportDao) {
        this.sportDao = SportDao;
    }

    public List<Sport> getSports() {
        return sportDao.getSports();
    }

    public static SportRepository getInstance(SportDao SportDao) {
        if (instance == null) {
            synchronized(SportRepository.class) {
                if (instance == null)
                    instance = new SportRepository(SportDao);
            }
        }
        return instance;
    }
}