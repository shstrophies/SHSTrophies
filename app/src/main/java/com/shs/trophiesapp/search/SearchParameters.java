package com.shs.trophiesapp.search;

public class SearchParameters {

    public static final String ALL = "ALL";
    public static final String PLAYERS = "PLAYERS";
    public static final String SPORTID = "SPORTID";
    public static final String SPORTS = "SPORTS";
    public static final String TROPHIES = "TROPHIES";
    public static final String YEARS = "YEARS";
    
    
    Long sportid;
    String mixed;
    String players;
    String sports;
    String years;
    String trophies;


    public SearchParameters(Long sportid, String mixed, String players, String sports, String years, String trophies) {
        this.sportid = sportid;
        this.mixed = mixed;
        this.players = players;
        this.sports = sports;
        this.years = years;
        this.trophies = trophies;
    }

    public Long getSportid() {
        return sportid;
    }

    public String getMixed() {
        return mixed;
    }

    public String getPlayers() {
        return players;
    }

    public String getSports() {
        return sports;
    }

    public String getYears() {
        return years;
    }

    public String getTrophies() {
        return trophies;
    }
}
