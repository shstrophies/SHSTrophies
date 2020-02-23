package com.shs.trophiesapp.ui;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.AppDatabase;
import com.shs.trophiesapp.data.DataManager;
import com.shs.trophiesapp.data.entities.Sport;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.ui.sports.SportsActivity;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.DirectoryHelper;
import com.shs.trophiesapp.workers.SeedDatabaseWorker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.shs.trophiesapp.utils.Constants.DOWNLOAD_URL;
import static com.shs.trophiesapp.utils.Constants.GIDS;
import static com.shs.trophiesapp.utils.Constants.titles;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener, LifecycleOwner {
    private static final String TAG = "SetupActivity";

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 54654;
    private static final int IDSNUM = GIDS.length;
    long[] downloadIds = new long[IDSNUM];
    ArrayList downloadedIds = new ArrayList();
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
        try {
            for (int i = 0; i < GIDS.length; i++) {
                String url = DOWNLOAD_URL.replace("YOURGID", GIDS[i]);
                String directory = titles[i];
                downloadIds[i] = startDownload(url, DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/").concat(directory));

                File[] files = DirectoryHelper.listFilesInDirectory(directory);
                for(i = 0; i<files.length; i++) {
                    Log.d(TAG, "downloadData: file[" + i + "]=" );
                }
                DirectoryHelper.deleteOlderFiles(directory, 5);
                Toast.makeText(SetupActivity.this, "Download Started for id=" + downloadIds[i], Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            downloadedIds.add(id);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (Arrays.stream(downloadIds).anyMatch(n -> n == id)) {
                Toast.makeText(SetupActivity.this, "Download Completed for id=" + id, Toast.LENGTH_LONG).show();
            }
            if (downloadedIds.size() >= IDSNUM) {
                if(downloadButton != null) downloadButton.setEnabled(true);
                downloadedIds.clear();
                loadDatabase();
            }
        }
    };


    private void loadDatabase() {
        try {

            Context context = getApplicationContext();
            context.deleteDatabase(Constants.DATABASE_NAME);

            RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
                public void onCreate (@NonNull SupportSQLiteDatabase db) {
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

            if((sports.size() != 0) && (trophies.size() != 0)) {
                Toast.makeText(SetupActivity.this, "sports size=" + sports.size(), Toast.LENGTH_LONG).show();
                Toast.makeText(SetupActivity.this, "trophies size=" + trophies.size(), Toast.LENGTH_LONG).show();

                startActivity(new Intent(SetupActivity.this, SportsActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private long startDownload(String downloadPath, String destinationPath) {
        Uri uri = Uri.parse(downloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading a file"); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(destinationPath, uri.getLastPathSegment());  // Storage directory path
        DownloadManager downloadManager = ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE));
        long downloadId = downloadManager.enqueue(request); // This will start downloading
        return downloadId;
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