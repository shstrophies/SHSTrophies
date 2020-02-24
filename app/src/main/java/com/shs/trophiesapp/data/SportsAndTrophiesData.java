package com.shs.trophiesapp.data;

import com.shs.trophiesapp.data.entities.Trophy;

import java.util.List;

public class SportsAndTrophiesData {
    List<Trophy> trophies;
    String sport;

    public SportsAndTrophiesData(List<Trophy> trophies, String sport) {
        this.trophies = trophies;
        this.sport = sport;
    }

    public List<Trophy> getTrophies() {
        return trophies;
    }

    public String getSport() {
        return sport;
    }
}
