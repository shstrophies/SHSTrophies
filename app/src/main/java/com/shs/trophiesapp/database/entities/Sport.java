package com.shs.trophiesapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//@Fts4
@Entity(tableName = "sport", indices = @Index(value = "name", unique = true)) public class Sport {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") public long id = 0;
    @ColumnInfo(name = "name") public String name;
    @ColumnInfo(name = "imageUrl") public String imageUrl;

    public Sport(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
