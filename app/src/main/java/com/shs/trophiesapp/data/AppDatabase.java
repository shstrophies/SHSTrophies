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

@Database(entities = {Sport.class, Trophy.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String TAG = "AppDatabase";
    abstract public SportDao sportDao();
    abstract public TrophyDao trophyDao();

    private static AppDatabase mInstance = null;

    public synchronized static AppDatabase getInstance(Context context) {
        if(mInstance == null) {
            Log.d(TAG, "getInstance: calling buildDatabase");
            mInstance = buildDatabase(context);
        }
        return mInstance;
    }

    private static AppDatabase buildDatabase(final Context context) {

        Log.d(TAG, "buildDatabase: in buildDatabase, context=" + context);
        return Room.databaseBuilder(context, AppDatabase.class, Constants.DATABASE_NAME)
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Log.d(TAG, "Room.databaseBuilder, ... onCreate: ");
                        super.onCreate(db);
                        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SeedDatabaseWorker.class).build();
                        WorkManager workManager = WorkManager.getInstance(context);
                        workManager.enqueue(workRequest);
                    }
                }).build();
    }

    public void cleanUp(){
        mInstance = null;
    }
}
