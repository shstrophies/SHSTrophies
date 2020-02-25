package com.shs.trophiesapp.data.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Fts4
@Entity(tableName = "trophy")
public class Trophy {
//    @PrimaryKey @ColumnInfo(name = "id") public int id;

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") public long id = 0;

    @ColumnInfo(name = "Sport") public String sport_name;
    @ColumnInfo(name = "Year") public int year;
    @ColumnInfo(name = "Trophy_Title") public String tr_title;
    @ColumnInfo(name = "Trophy_Image_URI") public String tr_image_url;
    @ColumnInfo(name = "Player_Name") public String player;
    @ColumnInfo(name = "Category") public String category;

    public Trophy(String sport_name, int year, String tr_title, String tr_image_url, String player, String category) {
        this.sport_name = sport_name;
        this.year = year;
        this.tr_title = tr_title;
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

    public String getTr_title() {
        return tr_title;
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
        hash = hash + (this.getTr_title() != null ? this.getTr_title().hashCode() : 0);
        hash = hash + (int) (this.id ^ (this.id >>> 32));
        hash = hash +(this.getPlayer() != null ? this.getPlayer().hashCode() : 0);
        return hash;
    }


}
