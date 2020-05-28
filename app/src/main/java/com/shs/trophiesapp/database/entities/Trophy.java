package com.shs.trophiesapp.database.entities;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
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
        indices = {
                @Index(value = {"sportId", "title", "url"}, unique = true)

        }
)
public class Trophy {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id = 0;
    @ColumnInfo(name = "sportId")
    private long sportId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "url")
    private String url;

    public Trophy(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Ignore public int color = Color.parseColor("#C72027"); //TODO: Fix this if we change app theme colors in colors.xml


    @NonNull
    @Override
    public String toString() {
        return "Trophy{" +
                "id=" + id +
                ", sportId=" + sportId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", color=" + color +
                '}';
    }
}
