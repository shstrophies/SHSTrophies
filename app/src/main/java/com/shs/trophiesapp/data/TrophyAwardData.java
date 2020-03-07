package com.shs.trophiesapp.data;



public class TrophyAwardData {
    public String sportName;
    public int year;
    public String title;
    public String url;
    public String player;
    public String category;

    public TrophyAwardData(String sportName, int year, String title, String url, String player, String category) {
        this.sportName = sportName;
        this.year = year;
        this.title = title;
        this.url = url;
        this.player = player;
        this.category = category;
    }

    public String getSportName() {
        return sportName;
    }

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPlayer() {
        return player;
    }

    public String getCategory() {
        return category;
    }

}

