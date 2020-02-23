package com.shs.trophiesapp.workers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.DirectoryHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageDownloadThread extends Thread {

    private static final String TAG = "ImageDownloadThread";
    private String mUrl;
    private boolean mSport;

    ImageDownloadThread(String url, boolean sport) {
        this.mUrl = url;
        this.mSport = sport;
    }

    @Override
    public void run() {
        String img_name = mUrl.replace(Constants.DRIVE_IMAGE_PREFIX, "").split("/")[0]; //Just the ID
        Log.d(TAG, "Image Name: " + img_name);
        try {
            InputStream is = new URL(Constants.DRIVE_URL + img_name).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            writeBitmapToFile(bitmap, img_name + ".jpg");
            is.close();
        } catch (Exception e) { e.printStackTrace(); }
        super.run();
    }

    private void writeBitmapToFile(Bitmap bmp, String filename) {
        File dir;
        if(mSport) dir = new File(Environment.getExternalStorageDirectory() + Constants.DATA_DIRECTORY_SPORT_IMAGES);
        else dir = new File(Environment.getExternalStorageDirectory() + Constants.DATA_DIRECTORY_TROPHY_IMAGES);
        if(!dir.exists()) dir.mkdirs();
        File file = new File(dir, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException io) { io.printStackTrace(); }

    }
}
