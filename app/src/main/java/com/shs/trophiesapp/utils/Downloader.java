package com.shs.trophiesapp.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.util.HashMap;

import static android.content.Context.DOWNLOAD_SERVICE;

public class Downloader {

    private DownloadManager downloadManager;
    private Context context;

    static class DownloadInfo {
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

    public Downloader(Context ctx) {
        context = ctx;
        downloadManager = (DownloadManager) ctx.getSystemService(DOWNLOAD_SERVICE);
    }

    public DownloadManager.Request createRequest(String url, String directory, String saveAsName) {
        String downloadDescription = "url=" + url + " directory=" + directory + " saveAsName=" + saveAsName;
        Log.d("Downloader", downloadDescription);
        deleteIfExists(directory, saveAsName);
        DirectoryHelper.createDirectory(directory);
        DirectoryHelper.listFilesInDirectory(directory);
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setTitle("Downloading file to directory=" + directory); // Title for notification.
        request.setDescription(downloadDescription);
//        request.setDestinationInExternalPublicDir(DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/").concat(directoryName), uri.getLastPathSegment());  // Storage directory path
        request.setDestinationInExternalPublicDir(directory, saveAsName);
        request.setVisibleInDownloadsUi(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        return request;
    }

    private void deleteIfExists(String directory, String fileName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + directory, fileName);
        Log.d("Downloader checkFilePaths", file.getAbsolutePath());
        if(file.exists()) file.delete();
    }

    public long queueDownload(DownloadManager.Request request) {
        return downloadManager.enqueue(request);
    }

    public HashMap checkDownloadStatus(long downloadId) {

        DownloadManager.Query downloadQuery = new DownloadManager.Query();
        downloadQuery.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(downloadQuery);
        HashMap<String, String> result = new HashMap<>();
        if (cursor.moveToFirst()) {
            result = getDownloadStatus(cursor, downloadId);
        } else {
            result.put("status", "UNKNOWN");
            result.put("reason", "COULD_NOT_FIND");
            result.put("downloadId", String.valueOf(downloadId));
        }
        HashMap wmap = new HashMap();
        for (HashMap.Entry<String, String> entry : result.entrySet()) {
            wmap.put(entry.getKey(), entry.getValue());
        }
        return wmap;
    }

    public int cancelDownload(long downloadId) {
        return downloadManager.remove(downloadId);
    }


    private HashMap<String, String> getDownloadStatus(Cursor cursor, long downloadId) {

        int columnStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int STATUS = cursor.getInt(columnStatusIndex);
        int columnReasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int REASON = cursor.getInt(columnReasonIndex);
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
        String filename = cursor.getString(filenameIndex);

        String statusText = "";
        String reasonText = "";

        switch (STATUS) {
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch (REASON) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    default:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch (REASON) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                    default:
                        reasonText = "UNKNOWN";
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                reasonText = filename;
                break;
            default:
                statusText = "STATUS_UNKNOWN";
                reasonText = String.valueOf(STATUS);
                break;
        }

        HashMap<String, String> result = new HashMap<>();
        result.put("status", statusText);
        result.put("reason", reasonText);
        result.put("downloadId", String.valueOf(downloadId));
        return result;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }
}