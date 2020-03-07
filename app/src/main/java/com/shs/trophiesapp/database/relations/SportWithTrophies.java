package com.shs.trophiesapp.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.Trophy;

import java.util.List;

public class SportWithTrophies {
    @Embedded
    public Sport sport;

    @Relation(parentColumn = "id", entityColumn = "sportId", entity = Trophy.class)
    public List<Trophy> trophies;
}

