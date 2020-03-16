package com.shs.trophiesapp.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;

import java.util.List;

public class TrophyWithAwards {
    @Embedded
    public Trophy trophy;

    @Relation(parentColumn = "id", entityColumn = "trophyId", entity = TrophyAward.class)
    public List<TrophyAward> awards;
}
