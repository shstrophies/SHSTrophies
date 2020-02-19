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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.DataManager;
import com.shs.trophiesapp.ui.sports.SportsActivity;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.DirectoryHelper;
import com.shs.trophiesapp.utils.DownloadService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static com.shs.trophiesapp.utils.Constants.DOWNLOAD_URL;
import static com.shs.trophiesapp.utils.Constants.GIDS;
import static com.shs.trophiesapp.utils.Constants.titles;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DOWNLOAD_PATH = Constants.DOWNLOAD_URL;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 54654;
    private static final int IDSNUM = GIDS.length;
    long[] downloadIds = new long[IDSNUM];
    ArrayList downloadedIds = new ArrayList();
    Button downloadButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        findViewById(R.id.downloadDataButton).setOnClickListener(this);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            return;
        }
        DirectoryHelper.createDirectory(this);
    }


    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            downloadedIds.add(id);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (Arrays.stream(downloadIds).anyMatch(n -> n == id)) {
                Toast.makeText(DownloadActivity.this, "Download Completed for id=" + id, Toast.LENGTH_LONG).show();
            }
            if(downloadedIds.size() >= IDSNUM) {
//                if(downloadButton != null) downloadButton.setEnabled(true);
                downloadedIds.clear();
                //CAROLINA HERE
                Toast.makeText(DownloadActivity.this, "Getting sports repository", Toast.LENGTH_LONG).show();
                DataManager.getSportRepository(context);
                Toast.makeText(DownloadActivity.this, "Getting trophy repository", Toast.LENGTH_LONG).show();
                DataManager.getTrophyRepository(context);
                startActivity(new Intent(DownloadActivity.this, SportsActivity.class));

            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downloadDataButton: {
                try {
                    for (int i = 0; i < GIDS.length; i++) {
                        String url = DOWNLOAD_URL.replace("YOURGID", GIDS[i]);
                        String directory = titles[i];
                        downloadIds[i] = startDownload(url, DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/").concat(directory));
                        Toast.makeText(DownloadActivity.this, "Download Started for id=" + downloadIds[i], Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                downloadButton = (Button)findViewById(view.getId());
                downloadButton.setEnabled(false);
                break;
            }

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