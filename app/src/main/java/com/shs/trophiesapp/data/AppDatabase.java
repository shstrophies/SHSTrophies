package com.shs.trophiesapp.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.shs.trophiesapp.data.daos.SportDao;
import com.shs.trophiesapp.data.daos.TrophyDao;
import com.shs.trophiesapp.data.entities.Sport;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.ui.SetupActivity;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.workers.SeedDatabaseWorker;

import java.util.UUID;

@Database(entities = {Sport.class, Trophy.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String TAG = "AppDatabase";
    abstract public SportDao sportDao();
    abstract public TrophyDao trophyDao();

    private static AppDatabase mInstance = null;
    private static RoomDatabase.Callback mCallback = new RoomDatabase.Callback() {
        public void onCreate (SupportSQLiteDatabase db) {
            Log.d(TAG, "Room.databaseBuilder, ... onCreate: ");
            super.onCreate(db);
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SeedDatabaseWorker.class).build();
            WorkManager workManager = WorkManager.getInstance();
            workManager.enqueue(workRequest);
            UUID id = workRequest.getId();
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
