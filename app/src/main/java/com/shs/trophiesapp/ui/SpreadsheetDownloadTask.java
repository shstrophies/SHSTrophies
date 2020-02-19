package com.shs.trophiesapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.shs.trophiesapp.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.shs.trophiesapp.utils.Constants.*;

public class SpreadsheetDownloadTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "SpreadsheetDownloadTask";
    private static String[] fileHashes = new String[GIDS.length];
    private WeakReference<Activity> foregroundActivity;


    SpreadsheetDownloadTask(Activity activity) {
        this.foregroundActivity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(foregroundActivity.get() != null) {
            Log.d(TAG, "onPreExecute: about to download spreadsheets");
            TextView progress = foregroundActivity.get().findViewById(R.id.progress_text);
            progress.setText(R.string.progress_bar_asynctask_dialog);
        }
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            for(int i = 0; i < GIDS.length; i++) {
                String url = DOWNLOAD_URL.replace("YOURGID", GIDS[i]);
                saveCSVs(url, titles[i], i);
            }
        } catch (Exception e) {e.printStackTrace();}

        boolean changes = false;
        SharedPreferences prefs = foregroundActivity.get().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> newHashes = new HashSet<>();
        Set<String> hashes = prefs.getStringSet("hashes", null);
        // CAROLINA TODO
        if(false) {
//            if(hashes != null) {
            int index = 0;
            for(Iterator<String> it = hashes.iterator(); it.hasNext(); index++) {
                if(!it.next().equals(fileHashes[index])) changes = true;
                newHashes.add(fileHashes[index]);
            }
        } else {
            // CAROLINA
            changes = true;
            Collections.addAll(newHashes, fileHashes);
        }
        editor.putStringSet("hashes", newHashes);
        editor.apply();
        return changes;
    }

    private void saveCSVs(String string, String title, int iteration) throws IOException, NoSuchAlgorithmException {
        Log.d(TAG, "saveCSVs: saving " + title);
        URL url = new URL(string);
        url.openConnection().connect();

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        File csv = new File(Environment.getExternalStorageDirectory().toString() + "/" + title + ".csv");
        InputStream input = new BufferedInputStream(url.openStream(),8192);
        OutputStream output = new FileOutputStream(csv);

        byte[] data = new byte[1024]; int count;
        while ((count = input.read(data)) != -1) {
            output.write(data, 0, count);
            md.update(data, 0, count);
        }
        output.flush(); output.close(); input.close();

        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for(byte by : bytes) {
            sb.append(Integer.toString((by & 0xff) + 0x100, 16).substring(1));
        }
        fileHashes[iteration] = sb.toString();
        //filePaths[iteration] = csv.getAbsolutePath(); //Filepaths not necessary since they stay constant
    }
}
