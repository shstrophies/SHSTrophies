package com.shs.trophiesapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.shs.trophiesapp.database.AppDatabase;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.databinding.ActivitySetupBinding;
import com.shs.trophiesapp.utils.Assert;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.DirectoryHelper;
import com.shs.trophiesapp.utils.Downloader;
import com.shs.trophiesapp.utils.Utils;
import com.shs.trophiesapp.workers.SeedDatabaseWorker;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import static com.shs.trophiesapp.utils.CSVUtils.parseLine;
import static com.shs.trophiesapp.utils.Constants.DOWNLOAD_URL;
import static com.shs.trophiesapp.utils.Constants.SPORTS_GID;
import static com.shs.trophiesapp.utils.Constants.SPORTS_DIRECTORY_NAME;


public class SetupActivity extends BaseActivity implements View.OnClickListener, LifecycleOwner {
    private static final String TAG = "SetupActivity";
    public static final String SHARED_PREFERENCES_TITLE = "Trophy_Shared_Preferences";
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 54654;

    private ActivitySetupBinding binding;
    private boolean stopDownloads;

    HashMap<Long, DownloadInfo> downloadInfoMap = new HashMap<>();
    ArrayList<Long> downloadInfoList = new ArrayList<>();
    Set<String> destinationPaths = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stopDownloads = false;
        binding = ActivitySetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loadDatabaseButton.setOnClickListener(this);
        binding.cleanButton.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        getApplication().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        DirectoryHelper.deleteDirectory(Environment.getExternalStorageDirectory() + "/" + Constants.DATA_DIRECTORY_NAME);
        downloadData();
    }

    private void setupHashes() {
        Log.d(TAG, "SetupHashes Method Started");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_TITLE, Context.MODE_PRIVATE);
        Map<String, String> hashes = (Map<String, String>) sharedPreferences.getAll();
        destinationPaths.add(Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME + "/" + SPORTS_DIRECTORY_NAME);

        if(hashes == null || hashes.isEmpty()) {
            Log.d(TAG, "No Hashes present");
            loadDatabase();
        } else {
            int sameHashCounter = 0;
            for(String destPath : destinationPaths) {
                if(hashes.containsKey(destPath)) {
                    // Not the first time booting up the app
                    String prevHash = hashes.get(destPath);
                    String currHash = Utils.getFileHash(destPath + "/" + "exported.csv");
                    Log.d(TAG, "Previous Hash: " + prevHash + ", Current Hash:" + currHash);
                    assert currHash != null;
                    if(!currHash.equals(prevHash)) loadDatabase();
                    else {
                        Log.d(TAG, "Hashes are the same");
                        sameHashCounter++;
                    }
                } else {
                    Log.d(TAG, "Hash is new and not in Shared Preferences file");
                    loadDatabase();
                    break;
                }
            }
            Log.d(TAG, "Samehashcounter: " + sameHashCounter + ", dPaths Size: " + destinationPaths.size());
            if((sameHashCounter == destinationPaths.size()) && getApplicationContext().getDatabasePath(Constants.DATABASE_NAME).exists()) {
                Log.d(TAG, "Exact same db, creating from previous DB file");
                AppDatabase.prepopulateDatabase(getApplicationContext());
                startActivity(new Intent(SetupActivity.this, SportsActivity.class));
            }
            else {
                Log.d(TAG, "database directory does not exists for some reason...");
                loadDatabase();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadDatabaseButton: {
                if(!getIntent().getBooleanExtra("Clean", false)) {
                    Log.d(TAG, "Button Pressed");
                    binding.loadDatabaseButton.setEnabled(false);
                    loadDatabase();
                } else Toast.makeText(getApplicationContext(), "Cannot Load Database, no data present", Toast.LENGTH_LONG).show();
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
    }

    private void downloadData() {
        downloadDataFromURL(DOWNLOAD_URL.replace("YOURGID", SPORTS_GID), SPORTS_DIRECTORY_NAME);
    }


    static class DownloadInfo {
        long id;
        Downloader downloader;
        String downloadPath;
        String destinationPath;

        DownloadInfo(long id, Downloader downloader, String downloadPath, String destinationPath) {
            this.id = id;
            this.downloader = downloader;
            this.downloadPath = downloadPath;
            this.destinationPath = destinationPath;
        }
    }

    private void downloadDataFromURL(String downloadPath, String directoryName) {
        Log.d(TAG, "downloadDataFromURL: ************************************************** ");
        Log.d(TAG, "downloadDataFromURL: ************************************************** ");
        Log.d(TAG, "downloadDataFromURL: download data for " + directoryName + " downloadPath=" + downloadPath);
        Log.d(TAG, "downloadDataFromURL: ************************************************** ");
        Log.d(TAG, "downloadDataFromURL: ************************************************** ");

        String destinationPath = DirectoryHelper.ROOT_DIRECTORY_NAME + "/" + directoryName;
        String fullDirectory = Environment.getExternalStorageDirectory() + "/" + destinationPath;

        DirectoryHelper.listFilesInDirectory(fullDirectory);
        DirectoryHelper.deleteOlderFiles(fullDirectory, 0);
        DirectoryHelper.createDirectory(this);

        DownloadInfo downloadInfo = startDownload(downloadPath, destinationPath);
        downloadInfoMap.put(downloadInfo.id, downloadInfo);
        Log.d(TAG, "downloadDataFromURL: *** downloadInfoList add " + downloadInfo.id);
        downloadInfoList.add(downloadInfo.id);

        Toast.makeText(SetupActivity.this, "Download Started for id=" + downloadInfo.id, Toast.LENGTH_LONG).show();
        Log.d(TAG, "downloadDataFromURL: Download Started for id=" + downloadInfo.id + " downloadDataFromURL: downloadInfoMap=" + Collections.singletonList(downloadInfoMap));
    }

    private DownloadInfo startDownload(String downloadPath, String destinationPath) {
        Log.d(TAG, "startDownload: url=" + downloadPath + ", directory=" + destinationPath);
        Uri.parse(downloadPath);
        Downloader downloader = new Downloader(this);
        DownloadManager.Request request = downloader.createRequest(downloadPath, destinationPath, Constants.DATA_FILENAME_NAME);
        long downloadId = downloader.queueDownload(request);// This will start downloading
        String fullDirectory = Environment.getExternalStorageDirectory() + "/" + destinationPath;
        return new DownloadInfo(downloadId, downloader, downloadPath, fullDirectory);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            if(stopDownloads) {
                // TODO: Check to see if there's actually a directory and corresponding data otherwise don't allow (or you can check if hashes exist)
                return;
            }
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d(TAG, "onReceive: downloaded id=" + id);
            DownloadInfo downloadInfo = downloadInfoMap.get(id);
            if(downloadInfo == null) return;
            //Assert.that(downloadInfo != null, "downloadInfo should not be null");

            //Checking if the received broadcast is for our enqueued download by matching download id

            Log.d(TAG, "onReceive: found downloadInfo, downloadInfo.id=" + downloadInfo.id + " downloadInfo.downloadPath=" + downloadInfo.downloadPath + " downloadInfo.destinationPath=" + downloadInfo.destinationPath);
            HashMap downloadStatus = downloadInfo.downloader.checkDownloadStatus(downloadInfo.id);
            String status = Objects.requireNonNull(downloadStatus.get("status")).toString();
            if(status.equals("STATUS_SUCCESSFUL")) {
                Toast.makeText(context, "Download Completed for id=" + id, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onReceive: Download Completed for id=" + id + " downloadInfo.id=" + downloadInfo.id + " downloadInfo.destinationPath=" + downloadInfo.destinationPath);
                DirectoryHelper.listFilesInDirectory(downloadInfo.destinationPath);
                destinationPaths.add(downloadInfo.destinationPath + "/");

                // if this is the sports spreadsheet, then read it and get all the GIDs from that file
                File destinationPath = new File(downloadInfo.destinationPath);
                if(destinationPath.getName().compareToIgnoreCase(SPORTS_DIRECTORY_NAME) == 0) {
                    try {
                        File file = DirectoryHelper.getLatestFilefromDir(downloadInfo.destinationPath);
                        assert file != null;
                        Scanner scanner = new Scanner(file);
                        boolean first = true;
                        while (scanner.hasNext()) {
                            String line = scanner.nextLine();
                            Log.d(TAG, "onReceive: line=" + line);
                            List<String> commaSeparatedLine = parseLine(line);
                            if (first) first = false;
                            else {
                                String sport = commaSeparatedLine.get(0);
                                String gid = commaSeparatedLine.get(1);
                                downloadDataFromURL(DOWNLOAD_URL.replace("YOURGID", gid), sport);
                            }
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            } else {
                String reason = Objects.requireNonNull(downloadStatus.get("reason")).toString();
                Toast.makeText(context, "Download NOT SUCCESSFUL because " + reason, Toast.LENGTH_LONG).show();
                Log.d(TAG, "**** onReceive: Download failed because " + reason);
            }
            Log.d(TAG, "onReceive: *** downloadInfoList remove " + downloadInfo.id);
            Log.d(TAG, "onReceive: downloadInfoList=" + Collections.singletonList(downloadInfoList));
            downloadInfoList.remove(downloadInfo.id);
            if (downloadInfoList.isEmpty()) {
                Log.d(TAG, "onReceive: DOWNLOADS complete");
                DirectoryHelper.listFilesInDirectoryRecursively(Environment.getExternalStorageDirectory() + "/" + Constants.DATA_DIRECTORY_NAME);
                setupHashes();
            }

        }
    };

    private void loadDatabase() {
//        loadDatabase(true);
        loadDatabase(false);
    }

    private void loadDatabase(boolean fromFiles) {
        try {
            //TODO: Check if downloads all exist
            stopDownloads = true;
            Log.d(TAG, "loadDatabase: ");
            Context context = getApplicationContext();
            context.deleteDatabase(Constants.DATABASE_NAME);

            RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    Log.d(TAG, "Room.databaseBuilder, ... onCreate: ");
                    super.onCreate(db);
                    if(fromFiles) {
                        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SeedDatabaseWorker.class).build();
                        WorkManager workManager = WorkManager.getInstance(context);
                        workManager.enqueue(workRequest);
                        //UUID id = workRequest.getId();
                        WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.getId())
                                .observe(SetupActivity.this, workInfo -> {
                                    if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                        Toast.makeText(SetupActivity.this, "DONE Loading database...", Toast.LENGTH_LONG).show();
                                        setupFutureHashing();
                                        startActivity(new Intent(SetupActivity.this, SportsActivity.class));
                                    }
                                });
                    }
                }
            };

            AppDatabase.getInstance(context, rdc);
            Toast.makeText(SetupActivity.this, "Loading database...", Toast.LENGTH_LONG).show();
            List<Sport> sports = DataManager.getSportRepository(context).getSports();

            Toast.makeText(SetupActivity.this, "Getting trophy repository", Toast.LENGTH_LONG).show();
            List<TrophyAward> awards = DataManager.getTrophyRepository(context).getAwards();

            if ((sports.size() != 0) && (awards.size() != 0)) {
                Toast.makeText(SetupActivity.this, "sports size=" + sports.size(), Toast.LENGTH_LONG).show();
                Toast.makeText(SetupActivity.this, "awards size=" + awards.size(), Toast.LENGTH_LONG).show();

                setupFutureHashing();
                startActivity(new Intent(SetupActivity.this, SportsActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupFutureHashing() {
        Log.d(TAG, "Setup Future Hashing");
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (String path : destinationPaths) {
            String hash = Utils.getFileHash(path + "/" + "exported.csv");
            Log.d(TAG, "Hash Key: " + path + ", Hash: " + hash);
            editor.putString(path, hash);
        }
        editor.apply();
    }

    private static void killmyself(WeakReference<Context> context) {
        try {
            if (context.get() != null) {
                PackageManager pm = context.get().getPackageManager();
                if (pm != null) {
                    Intent mStartActivity = pm.getLaunchIntentForPackage(context.get().getPackageName());
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(context.get(), mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) context.get().getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
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
}