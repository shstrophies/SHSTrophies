package com.shs.trophiesapp.data;

import com.shs.trophiesapp.data.entities.TrophyAward;

import java.util.List;

public class SportsAndTrophiesData {
    List<TrophyAward> trophies;
    String sport;

    public SportsAndTrophiesData(List<TrophyAward> trophies, String sport) {
        this.trophies = trophies;
        this.sport = sport;
    }

    public List<TrophyAward> getTrophies() {
        return trophies;
    }

    public String getSport() {
        return sport;
    }
}
