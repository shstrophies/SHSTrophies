package com.shs.trophiesapp.utils;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class ThreadDownloader implements Runnable {

    private static final String TAG = "ThreadDownloader";

    private WeakReference<Context> context;
    private String downloadPath; //String url
    private String directoryName; //Should be the complete url (including Environment.external storage directory)

    public ThreadDownloader(String downloadPath, String directoryName, Context context) {
        this.downloadPath = downloadPath;
        this.directoryName = directoryName;
        this.context = new WeakReference<>(context);
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        httpDownload(downloadPath, directoryName, context);
    }

    //TODO: Override interrupt so that incomplete files aren't saved
    // (May not be necessary if writing to file is a separate process than just downloading (not downloaded directly to file)

    //TODO: Eventually, make this download process insulated to interruptions (like bad wifi)
    public static void httpDownload(String downloadPath, String directoryAbsPath, WeakReference<Context> context) {
        //TODO: Download to exported.csv in the directory path
        int count;
        try {
            URL url = new URL(downloadPath);
            URLConnection connection = url.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(directoryAbsPath + Constants.DATA_FILENAME_NAME, false);

            byte[] data = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            Log.d(TAG, "CSV finally downloaded");

            output.flush();
            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
