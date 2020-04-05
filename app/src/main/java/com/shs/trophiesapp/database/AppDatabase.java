package com.shs.trophiesapp.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.shs.trophiesapp.database.daos.SportDao;
import com.shs.trophiesapp.database.daos.TrophyAwardDao;
import com.shs.trophiesapp.database.daos.TrophyDao;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.workers.SeedDatabaseWorker;

@Database(entities = {Sport.class, Trophy.class, TrophyAward.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String TAG = "AppDatabase";
    abstract public SportDao sportDao();
    abstract public TrophyDao trophyDao();
    abstract public TrophyAwardDao trophyAwardDao();

    private static AppDatabase mInstance = null;
    private static RoomDatabase.Callback mCallback = new RoomDatabase.Callback() {
        public void onCreate (@NonNull SupportSQLiteDatabase db) {
            Log.d(TAG, "Room.databaseBuilder, ... onCreate: ");
            super.onCreate(db);
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SeedDatabaseWorker.class).build();
            WorkManager workManager = WorkManager.getInstance();
            workManager.enqueue(workRequest);
            //UUID id = workRequest.getId();
        }
    };

    public synchronized static AppDatabase getInstance(Context context, RoomDatabase.Callback callback) {
        if(mInstance == null) {
            Log.d(TAG, "getInstance: calling buildDatabase");
            mInstance = buildDatabase(context, callback);
        }
        return mInstance;
    }

    public synchronized static AppDatabase getInstance(Context context) {
        if(mInstance == null) {
            Log.d(TAG, "getInstance: calling buildDatabase");
            mInstance = buildDatabase(context, mCallback);
        }
        return mInstance;
    }

    public synchronized static AppDatabase prepopulateDatabase(Context context) {
        if(mInstance == null) {
            Log.d(TAG, "getInstance: creating database from pre-existing file");
            mInstance = Room.databaseBuilder(context, AppDatabase.class, Constants.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .createFromFile(context.getDatabasePath(Constants.DATABASE_NAME))
                    .build();
        }
        return mInstance;
    }

    private static AppDatabase buildDatabase(final Context context, RoomDatabase.Callback callback) {

        Log.d(TAG, "buildDatabase: in buildDatabase, context=" + context);
        return Room.databaseBuilder(context, AppDatabase.class, Constants.DATABASE_NAME)
                .allowMainThreadQueries()
                .addCallback(callback).build();
    }

    public void cleanUp(){
        mInstance = null;
    }
}
