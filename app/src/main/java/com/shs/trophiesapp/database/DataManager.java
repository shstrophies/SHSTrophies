package com.shs.trophiesapp.database;

import android.content.Context;

public class DataManager {


    public static SportRepository getSportRepository(Context context) {
        return SportRepository.getInstance(AppDatabase.getInstance(context).sportDao());
    }

    public static TrophyRepository getTrophyRepository(Context context) {
        return TrophyRepository.getInstance(
                AppDatabase.getInstance(context).trophyDao(),
                AppDatabase.getInstance(context).trophyAwardDao()
                );
    }
}
