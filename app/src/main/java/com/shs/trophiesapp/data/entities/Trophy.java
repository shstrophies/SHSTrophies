package com.shs.trophiesapp.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "Trophy",
        foreignKeys = {
                @ForeignKey(entity = Sport.class,
                        parentColumns = "id",
                        childColumns = "sportId",
                        onDelete = CASCADE)
        },
        indices = @Index(value = {"title", "url"}, unique = true)
)
public class Trophy {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id = 0;
    @ColumnInfo(name = "sportId")
    private long sportId;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @NonNull
    @ColumnInfo(name = "url")
    private String url;

    public Trophy(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public long getSportId() {
        return sportId;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setSportId(long sportId) {
        this.sportId = sportId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
