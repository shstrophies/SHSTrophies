package com.shs.trophiesapp.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SportWithTrophies {
    @Embedded
    public Sport sport;

    @Relation(parentColumn = "id", entityColumn = "sportId", entity = Trophy.class)
    public List<Trophy> trophies;
}

