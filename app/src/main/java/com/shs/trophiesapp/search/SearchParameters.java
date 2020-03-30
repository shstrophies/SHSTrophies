package com.shs.trophiesapp.search;

public class SearchParameters {

    public static final String ALL = "ALL";
    public static final String PLAYERNAMES = "PLAYERNAMES";
    public static final String SPORTNAMES = "SPORTNAMES";
    public static final String TROPHYTITLES = "TROPHYTITLES";
    public static final String YEARS = "YEARS";


    String all;
    String playerNames;
    String sportNames;
    String years;
    String trophyTitles;


    public SearchParameters(String all, String playerNames, String sportNames, String years, String trophyTitles) {
        this.all = all;
        this.playerNames = playerNames;
        this.sportNames = sportNames;
        this.years = years;
        this.trophyTitles = trophyTitles;
    }

    public String getAll() {
        return all;
    }

    public String getPlayerNames() {
        return playerNames;
    }

    public String getSportNames() {
        return sportNames;
    }

    public String getYears() {
        return years;
    }

    public String getTrophyTitles() {
        return trophyTitles;
    }

    @Override
    public String toString() {
        return all.isEmpty() ?
                (playerNames.isEmpty() ? "" : "players='" + playerNames + '\'') +
                        (sportNames.isEmpty() ? "" : " sports='" + sportNames + '\'') +
                        (years.isEmpty() ? "" : " years='" + years + '\'') +
                        (trophyTitles.isEmpty() ? "" : " trophies='" + trophyTitles + '\'')
                :
                all
                ;
    }
}
