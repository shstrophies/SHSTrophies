package com.shs.trophiesapp.data.entities;

import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Fts4
@Entity(tableName = "trophy")
public class Trophy {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") public long id = 0;
    @ColumnInfo(name = "Sport") public String sport_name;
    @ColumnInfo(name = "Year") public int year;
    @ColumnInfo(name = "Trophy_Title") public String trophy_title;
    @ColumnInfo(name = "Trophy_Image_URI") public String tr_image_url;
    @ColumnInfo(name = "Player_Name") public String player;
    @ColumnInfo(name = "Category") public String category;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    int color = Color.RED;

    public Trophy(String sport_name, int year, String trophy_title, String tr_image_url, String player, String category) {
        this.sport_name = sport_name;
        this.year = year;
        this.trophy_title = trophy_title;
        this.tr_image_url = tr_image_url;
        this.player = player;
        this.category = category;
    }

    public String getSport_name() {
        return sport_name;
    }

    public int getYear() {
        return year;
    }

    public String gettrophy_title() {
        return trophy_title;
    }

    public String getTr_image_url() {
        return tr_image_url;
    }

    public String getPlayer() {
        return player;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (this.id == ((Trophy)obj).id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = hash + (this.gettrophy_title() != null ? this.gettrophy_title().hashCode() : 0);
        hash = hash + (int) (this.id ^ (this.id >>> 32));
        hash = hash +(this.getPlayer() != null ? this.getPlayer().hashCode() : 0);
        return hash;
    }


}
