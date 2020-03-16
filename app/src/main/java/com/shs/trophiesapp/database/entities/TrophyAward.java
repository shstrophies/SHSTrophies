package com.shs.trophiesapp.database.entities;

import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

//@Fts4
@Entity(tableName = "TrophyAward",
        foreignKeys = {
                @ForeignKey(entity = Trophy.class,
                        parentColumns = "id",
                        childColumns = "trophyId",
                        onDelete = CASCADE)
        },
        indices = {
                @Index(value = {"trophyId", "year", "player"}, unique = true)
        }
)
public class TrophyAward {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") public long id = 0;
    @ColumnInfo(name = "trophyId") private long trophyId;
    @ColumnInfo(name = "year") public int year;
    @ColumnInfo(name = "player") public String player;
    @ColumnInfo(name = "category") public String category;

    @Ignore
    int color = Color.RED;
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public TrophyAward(long trophyId, int year, String player, String category) {
        this.trophyId = trophyId;
        this.year = year;
        this.player = player;
        this.category = category;
    }


    public int getYear() {
        return year;
    }

    public String getPlayer() {
        return player;
    }

    public String getCategory() {
        return category;
    }

    public long getTrophyId() {
        return trophyId;
    }

    @Override
    public String toString() {
        return "TrophyAward{" +
                "id=" + id +
                ", trophyId=" + trophyId +
                ", year=" + year +
                ", player='" + player + '\'' +
                ", category='" + category + '\'' +
                ", color=" + color +
                '}';
    }
}
