package com.shs.trophiesapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.shs.trophiesapp.database.AppDatabase;
import com.shs.trophiesapp.databinding.ActivitySetupBinding;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.DirectoryHelper;
import com.shs.trophiesapp.utils.ThreadDownloader;
import com.shs.trophiesapp.utils.Utils;
import com.shs.trophiesapp.workers.SeedDatabaseWorker;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.shs.trophiesapp.utils.CSVUtils.parseLine;
import static com.shs.trophiesapp.utils.Constants.DATA_FILENAME_NAME;
import static com.shs.trophiesapp.utils.Constants.DOWNLOAD_URL;
import static com.shs.trophiesapp.utils.Constants.SPORTS_GID;
import static com.shs.trophiesapp.utils.Constants.SPORTS_DIRECTORY_NAME;

public class SetupActivity extends BaseActivity implements View.OnClickListener, LifecycleOwner {
    private static final String TAG = "SetupActivity";
    public static final String SHARED_PREFERENCES_TITLE = "Trophy_Shared_Preferences";
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 54654;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors(); //TODO: Test app on kiosk to see what optimal pool size is

    private static final boolean startClean = false; //Changed so that the pictures don't get removed every successive reload

    private ActivitySetupBinding binding;
    private ExecutorService executor;
    private List<String> directoryPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loadDatabaseButton.setOnClickListener(this);
        binding.cleanButton.setOnClickListener(this);

        if(!getApplicationContext().getDatabasePath(Constants.DATABASE_NAME).exists()) binding.loadDatabaseButton.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);

        if(startClean) clean(new WeakReference<>(getApplicationContext()));

        DirectoryHelper.createDirectory(Constants.DATA_DIRECTORY_NAME);
        DirectoryHelper.createDirectory(Constants.DATA_DIRECTORY_NAME + "/" + SPORTS_DIRECTORY_NAME);

        executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
            HashMap<String, String> csv_headers = executor.submit(firstDownloadCallable).get();

            for(String sport : csv_headers.keySet()) {
                String directoryPath = Constants.DATA_DIRECTORY_NAME + "/" + sport + "/";
                DirectoryHelper.createDirectory(directoryPath);
                directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + directoryPath;
                Log.d(TAG, "Directory Path for Each Sport: " + directoryPath);
                directoryPaths.add(directoryPath + DATA_FILENAME_NAME);
                executor.submit(
                        new ThreadDownloader(DOWNLOAD_URL.replace("YOURGID",
                                Objects.requireNonNull(csv_headers.get(sport))), directoryPath, getApplicationContext())
                );
            }
            executor.shutdown();
            long timeBeforeTermination = System.currentTimeMillis();
            boolean terminated = executor.awaitTermination(5, TimeUnit.MINUTES);
            long timeAfterTermination = System.currentTimeMillis();
            Log.d(TAG, "Termination time for all Thread processes is " + (timeAfterTermination - timeBeforeTermination) + " millis.");

            if(terminated) {
                Toast.makeText(getApplicationContext(), "Starting Hashing", Toast.LENGTH_LONG).show();
                setupHashingAfterDownload();
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                DirectoryHelper.createDirectory(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadDatabaseButton: {
                executor.shutdownNow();
                binding.loadDatabaseButton.setEnabled(false);
                Log.d(TAG, "Loading Database from DB File");
                Toast.makeText(getApplicationContext(), "Loading DB from Existing DB File", Toast.LENGTH_LONG).show();
                loadDatabaseFromDBFile();
                break;
            }
            case R.id.cleanButton: {
                clean(new WeakReference<>(getApplicationContext()));
                recreate();
                break;
            }
        }
    }

    public static void clean(WeakReference<Context> context) {
        context.get().deleteDatabase(Constants.DATABASE_NAME);
        DirectoryHelper.deleteDirectory(Environment.getExternalStorageDirectory() + "/" + Constants.DATA_DIRECTORY_NAME);
        context.get().getSharedPreferences(SHARED_PREFERENCES_TITLE, Context.MODE_PRIVATE).edit().clear().apply();
        new CacheClearAsyncTask(context).execute();
    }

    private void setupHashingAfterDownload() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_TITLE, Context.MODE_PRIVATE);
        Map<String, String> hashes = new HashMap<>();
        for(Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            hashes.put(entry.getKey(), entry.getValue().toString());
        }

        if(hashes.equals(new HashMap<>()) || hashes.isEmpty()) {
            Log.d(TAG, "No Hashes present");
            loadDatabaseFromFiles();
        } else {
            int sameHashCounter = 0;
            for(String path : directoryPaths) {
                if(hashes.containsKey(path)) {
                    String prevHash = hashes.get(path);
                    String currHash = Utils.getFileHash(path);
                    Log.d(TAG, "Previous Hash: " + prevHash + ", Current Hash:" + currHash);
                    assert currHash != null;
                    if(!currHash.equals(prevHash)) loadDatabaseFromFiles();
                    else {
                        Log.d(TAG, "Hashes are the same");
                        sameHashCounter++;
                    }
                } else {
                    Log.d(TAG, "Hash is new and not in Shared Preferences file");
                    loadDatabaseFromFiles();
                    break;
                }
            }
            Log.d(TAG, "Samehashcounter: " + sameHashCounter + ", dPaths Size: " + directoryPaths.size());
            if((sameHashCounter == directoryPaths.size()) && getApplicationContext().getDatabasePath(Constants.DATABASE_NAME).exists()) {
                Log.d(TAG, "Exact same db, creating from previous DB file");
                AppDatabase.prepopulateDatabase(getApplicationContext());
                startActivity(new Intent(SetupActivity.this, SportsActivity.class));
                finish();
            }
            else {
                Log.d(TAG, "database directory does not exists for some reason...");
                loadDatabaseFromFiles();
            }
        }
    }

    private void setupFutureHashingAfterDownload() {
        Log.d(TAG, "Setup Future Hashing");
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (String path : directoryPaths) {
            String hash = Utils.getFileHash(path);
            Log.d(TAG, "Hash Key: " + path + ", Hash: " + hash);
            editor.putString(path, hash);
        }
        editor.apply();
    }

    private void loadDatabaseFromFiles() {
        Log.d(TAG, "loadDatabaseFromFiles");
        Context currContext = getApplicationContext();
        currContext.deleteDatabase(Constants.DATABASE_NAME);

        AppDatabase db = AppDatabase.getInstance(currContext, new RoomDatabase.Callback() {
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                Log.d(TAG, "Room.databaseBuilder, ... onCreate: ");
                super.onCreate(db);
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SeedDatabaseWorker.class).build();
                WorkManager workManager = WorkManager.getInstance(currContext);
                workManager.enqueue(workRequest);
                WorkManager.getInstance(currContext).getWorkInfoByIdLiveData(workRequest.getId())
                        .observe(SetupActivity.this, workInfo -> {
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                Toast.makeText(SetupActivity.this, "DONE Loading database...", Toast.LENGTH_LONG).show();
                                setupFutureHashingAfterDownload();
                                startActivity(new Intent(SetupActivity.this, SportsActivity.class));
                                finish();
                            }
                        });
            }
        });
        db.sportDao().getAll(); //Just so that the DB is created (B/c of Room Design Pattern)
    }

    private void loadDatabaseFromDBFile() {
        Log.d(TAG, "Exact same db, creating from previous DB file");
        AppDatabase db = AppDatabase.prepopulateDatabase(getApplicationContext());
        db.sportDao().getAll(); //Just so that the DB is created (B/c of Room Design Pattern)
        startActivity(new Intent(SetupActivity.this, SportsActivity.class));
        finish();
    }

    private Callable<HashMap<String, String>> firstDownloadCallable = () -> {
        HashMap<String, String> sport_gid = new HashMap<>();
        String directoryPath = Constants.DATA_DIRECTORY_NAME + "/" + SPORTS_DIRECTORY_NAME + "/";
        DirectoryHelper.createDirectory(directoryPath);
        directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + directoryPath;

        String downloadPath = DOWNLOAD_URL.replace("YOURGID", SPORTS_GID);

        directoryPaths.add(directoryPath + DATA_FILENAME_NAME);
        ThreadDownloader.httpDownload(downloadPath, directoryPath, new WeakReference<>(getApplicationContext()));
        Scanner scanner = new Scanner(new File(directoryPath + DATA_FILENAME_NAME));
        boolean first = true;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            Log.d(TAG, "onReceive: line=" + line);
            List<String> commaSeparatedLine = parseLine(line);
            if (first) first = false;
            else {
                String sport = commaSeparatedLine.get(0);
                String gid = commaSeparatedLine.get(1);
                sport_gid.put(sport, gid);
            }
        }
        return sport_gid;
    };

    static class CacheClearAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> cont;
        CacheClearAsyncTask(WeakReference<Context> cont) {this.cont = cont;}

        @Override
        protected Void doInBackground(Void... params) {
            Glide.get(cont.get()).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute (Void result) {
            Log.d(TAG, "Cleaned Disk Cache");
        }
    }
}