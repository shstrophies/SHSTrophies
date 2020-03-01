package com.shs.trophiesapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.shs.trophiesapp.data.AppDatabase;
import com.shs.trophiesapp.data.DataManager;
import com.shs.trophiesapp.data.entities.Sport;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.DirectoryHelper;
import com.shs.trophiesapp.utils.Downloader;
import com.shs.trophiesapp.workers.SeedDatabaseWorker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.shs.trophiesapp.utils.Constants.DOWNLOAD_URL;
import static com.shs.trophiesapp.utils.Constants.sportsGID;
import static com.shs.trophiesapp.utils.Constants.titleSports;
import static com.shs.trophiesapp.utils.Constants.titleTrophies;
import static com.shs.trophiesapp.utils.Constants.trophiesGID;


public class SetupActivity extends AppCompatActivity implements View.OnClickListener, LifecycleOwner {
    private static final String TAG = "SetupActivity";

    class DownloadInfo {
        long id;
        Downloader downloader;
        String downloadPath;
        String destinationPath;


        public DownloadInfo(long id, Downloader downloader, String downloadPath, String destinationPath) {
            this.id = id;
            this.downloader = downloader;
            this.downloadPath = downloadPath;
            this.destinationPath = destinationPath;
        }
    }

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 54654;
    HashMap downloadInfoMap = new HashMap();
    ArrayList downloadInfoList = new ArrayList();
    Button downloadButton = null;


    Button loadDatabaseButton = null;
    private LifecycleOwner lifecycleOwner = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        downloadButton = findViewById(R.id.downloadDataButton);
        downloadButton.setOnClickListener(this);
        loadDatabaseButton = findViewById(R.id.loadDatabaseButton);
        loadDatabaseButton.setOnClickListener(this);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            return;
        }
        DirectoryHelper.createDirectory(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downloadDataButton: {
                downloadData();
                downloadButton = findViewById(view.getId());
                downloadButton.setEnabled(false);
                break;
            }
            case R.id.loadDatabaseButton: {
                loadDatabase();
                break;
            }

        }
    }

    private void downloadData() {
        downloadDataFromURL(DOWNLOAD_URL.replace("YOURGID", sportsGID), titleSports);
        downloadDataFromURL(DOWNLOAD_URL.replace("YOURGID", trophiesGID), titleTrophies);
    }

    private void downloadDataFromURL(String url, String directoryName) {
        Log.d(TAG, "downloadDataFromURL: download data for " + directoryName);
        String directory = Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/").concat(directoryName);
        File[] files = DirectoryHelper.listFilesInDirectory(directory);
        DirectoryHelper.deleteOlderFiles(directory, 5);

        DownloadInfo downloadInfo = startDownload(url, directory);
        downloadInfoMap.put(downloadInfo.id, downloadInfo);
        downloadInfoList.add(downloadInfo.id);

        Toast.makeText(SetupActivity.this, "Download Started for id=" + downloadInfo.id, Toast.LENGTH_LONG).show();
        Log.d(TAG, "downloadDataFromURL: Download Started for id=" + downloadInfo.id + " downloadDataFromURL: downloadInfoMap=" + Arrays.asList(downloadInfoMap));
    }

    private DownloadInfo startDownload(String url, String directory) {
        Log.d(TAG, "startDownload: url=" + url + ", directory=" + directory);
        Uri uri = Uri.parse(url);
        Downloader downloader = new Downloader(this);
        DownloadManager.Request request = downloader.createRequest(url, DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/").concat(directory), uri.getLastPathSegment());
        long downloadId = downloader.queueDownload(request);// This will start downloading
        return new DownloadInfo(downloadId, downloader, url, directory);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d(TAG, "onReceive: downloaded id=" + id);
            DownloadInfo downloadInfo = (DownloadInfo) downloadInfoMap.get(id);
            downloadInfoList.remove(downloadInfo.id);

            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadInfo != null) {
                Log.d(TAG, "onReceive: found downloadInfo, downloadInfo.id=" + downloadInfo.id + " downloadInfo.downloadPath=" + downloadInfo.downloadPath + " downloadInfo.destinationPath=" + downloadInfo.destinationPath);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadInfo.downloader.getDownloadManager().query(query);
                cursor.moveToFirst();
                HashMap downloadStatus = downloadInfo.downloader.checkDownloadStatus(downloadInfo.id);
                String status = downloadStatus.get("status").toString();
                switch (status) {
                    case "STATUS_SUCCESSFUL":
                        Toast.makeText(context, "Download Completed for id=" + id, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onReceive: Download Completed for id=" + id + " downloadInfo.id=" + downloadInfo.id + " downloadInfo.destinationPath=" + downloadInfo.destinationPath);
                        DirectoryHelper.listFilesInDirectory(downloadInfo.destinationPath);
                        break;

                    default:
                        String reason = downloadStatus.get("reason").toString();
                        Toast.makeText(context, "Download NOT SUCCESSFUL because " + reason, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "**** onReceive: Download failed because " + reason);
                        break;
                }
            } else {
                Log.d(TAG, "**** onReceive: downloadInfo is null, id=" + id);
            }

            if (downloadInfoList.isEmpty()) {
                if (downloadButton != null) downloadButton.setEnabled(true);
                Log.d(TAG, "onReceive: DOWNLOADS complete");
                loadDatabase();
            }

        }
    };


    private void loadDatabase() {
        try {

            Log.d(TAG, "loadDatabase: ");
            Context context = getApplicationContext();
            context.deleteDatabase(Constants.DATABASE_NAME);

            RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    Log.d(TAG, "Room.databaseBuilder, ... onCreate: ");
                    super.onCreate(db);
                    OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SeedDatabaseWorker.class).build();
                    WorkManager workManager = WorkManager.getInstance(context);
                    Operation operation = workManager.enqueue(workRequest);
                    UUID id = workRequest.getId();
                    WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.getId())
                            .observe(lifecycleOwner, workInfo -> {
                                if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                    Toast.makeText(SetupActivity.this, "DONE Loading database...", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SetupActivity.this, SportsActivity.class));
                                }
                            });
                }
            };

            AppDatabase db = AppDatabase.getInstance(context, rdc);
            Toast.makeText(SetupActivity.this, "Loading database...", Toast.LENGTH_LONG).show();
            List<Sport> sports = DataManager.getSportRepository(context).getSports();

            Toast.makeText(SetupActivity.this, "Getting trophy repository", Toast.LENGTH_LONG).show();
            List<Trophy> trophies = DataManager.getTrophyRepository(context).getTrophies();

            if ((sports.size() != 0) && (trophies.size() != 0)) {
                Toast.makeText(SetupActivity.this, "sports size=" + sports.size(), Toast.LENGTH_LONG).show();
                Toast.makeText(SetupActivity.this, "trophies size=" + trophies.size(), Toast.LENGTH_LONG).show();

                startActivity(new Intent(SetupActivity.this, SportsActivity.class));
            }
        } catch (Exception e) {
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
}