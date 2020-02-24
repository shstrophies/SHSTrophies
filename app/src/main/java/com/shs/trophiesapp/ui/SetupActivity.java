package com.shs.trophiesapp.ui;

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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.shs.trophiesapp.utils.Constants.DOWNLOAD_URL;
import static com.shs.trophiesapp.utils.Constants.GIDS;
import static com.shs.trophiesapp.utils.Constants.titles;


public class SetupActivity extends AppCompatActivity implements View.OnClickListener, LifecycleOwner {
    private static final String TAG = "SetupActivity";

    class DownloadInfo {
        long id;
        DownloadManager downloadManager;

        public DownloadInfo(long id, DownloadManager downloadManager) {
            this.id = id;
            this.downloadManager = downloadManager;
        }
    }

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 54654;
    private static final int IDSNUM = GIDS.length;
    HashMap downloadInfoMap = new HashMap();
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
            for (int i = 1; i < GIDS.length; i++) {
                String url = DOWNLOAD_URL.replace("YOURGID", GIDS[i]);
                String directoryName = titles[i];
                Log.d(TAG, "downloadData: download data for " + titles[i]);
                String directory = Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/").concat(directoryName);
                DownloadInfo downloadInfo = startDownload(url, directory);
                downloadInfoMap.put(downloadInfo.id, downloadInfo);

                Toast.makeText(SetupActivity.this, "Download Started for id=" + downloadInfo.id, Toast.LENGTH_LONG).show();
                Log.d(TAG, "downloadData: Download Started for id=" + downloadInfo.id);
                File[] files = DirectoryHelper.listFilesInDirectory(directory);
                DirectoryHelper.deleteOlderFiles(directory, 5);
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
            Log.d(TAG, "onReceive: downloaded id=" + id);
            downloadedIds.add(id);
            DownloadInfo downloadInfo = (DownloadInfo) downloadInfoMap.get(id);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadInfo != null) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadInfo.downloadManager.query(query);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);

                int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                int reason = cursor.getInt(columnReason);
                switch (status) {
                    case DownloadManager.STATUS_SUCCESSFUL:

                        Toast.makeText(context, "Download Completed for id=" + id, Toast.LENGTH_LONG).show();
                        break;

                    case DownloadManager.STATUS_FAILED:
                        String failedReason = "";
                        switch (reason) {
                            case DownloadManager.ERROR_CANNOT_RESUME:
                                failedReason = "ERROR_CANNOT_RESUME";
                                break;
                            case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                                failedReason = "ERROR_DEVICE_NOT_FOUND";
                                break;
                            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                                failedReason = "ERROR_FILE_ALREADY_EXISTS";
                                break;
                            case DownloadManager.ERROR_FILE_ERROR:
                                failedReason = "ERROR_FILE_ERROR";
                                break;
                            case DownloadManager.ERROR_HTTP_DATA_ERROR:
                                failedReason = "ERROR_HTTP_DATA_ERROR";
                                break;
                            case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                                failedReason = "ERROR_INSUFFICIENT_SPACE";
                                break;
                            case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                                failedReason = "ERROR_TOO_MANY_REDIRECTS";
                                break;
                            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                                failedReason = "ERROR_UNHANDLED_HTTP_CODE";
                                break;
                            case DownloadManager.ERROR_UNKNOWN:
                                failedReason = "ERROR_UNKNOWN";
                                break;
                        }
                        Toast.makeText(context, "Download failed because " + failedReason, Toast.LENGTH_LONG).show();

                        Log.d(TAG, "onReceive: Download failed because " + failedReason);
                        break;
                }
                ;

                DirectoryHelper.getLatestFilefromDir(Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME + "/" + Constants.titleTrophies + "/");
                DirectoryHelper.getLatestFilefromDir(Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME + "/" + Constants.titleSports + "/");


                if (downloadedIds.size() >= IDSNUM) {
                    if (downloadButton != null) downloadButton.setEnabled(true);
                    downloadedIds.clear();
                    Log.d(TAG, "onReceive: DOWNLOADS complete");
                    loadDatabase();
                }
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

    private DownloadInfo startDownload(String downloadPath, String destinationPath) {
        Log.d(TAG, "startDownload: downloadPath=" + downloadPath + ", destinationPath=" + destinationPath);
        Uri uri = Uri.parse(downloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading a file"); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(destinationPath, uri.getLastPathSegment());  // Storage directory path
        DownloadManager downloadManager = ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE));
        long downloadId = downloadManager.enqueue(request); // This will start downloading
        return new DownloadInfo(downloadId, downloadManager);
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