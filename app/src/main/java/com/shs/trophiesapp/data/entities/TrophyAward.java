package com.shs.trophiesapp.data.entities;

import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Fts4
@Entity(tableName = "TrophyAward")
public class TrophyAward {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") public long id = 0;
    @ColumnInfo(name = "SportName") public String sportName;
    @ColumnInfo(name = "Year") public int year;
    @ColumnInfo(name = "Title") public String title;
    @ColumnInfo(name = "url") public String url;
    @ColumnInfo(name = "Player") public String player;
    @ColumnInfo(name = "Category") public String category;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    int color = Color.RED;

    public TrophyAward(String sportName, int year, String title, String url, String player, String category) {
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

    @Override
    public boolean equals(@Nullable Object obj) {
        return (this.id == ((TrophyAward)obj).id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = hash + (this.getTitle() != null ? this.getTitle().hashCode() : 0);
        hash = hash + (int) (this.id ^ (this.id >>> 32));
        hash = hash +(this.getPlayer() != null ? this.getPlayer().hashCode() : 0);
        return hash;
    }


}
