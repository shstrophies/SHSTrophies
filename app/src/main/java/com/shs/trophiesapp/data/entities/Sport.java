package com.shs.trophiesapp.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Fts4
@Entity(tableName = "sport") public class Sport {
//    @PrimaryKey @ColumnInfo(name = "id") public int id;

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") public long id = 0;
    @ColumnInfo(name = "Sports") public String sport_name;
    @ColumnInfo(name = "Image_URL") public String image_url;

    public Sport(String sport_name, String image_url) {
        this.sport_name = sport_name;
        this.image_url = image_url;
    }
}
