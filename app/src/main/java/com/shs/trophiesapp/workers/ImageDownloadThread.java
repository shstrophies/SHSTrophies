package com.shs.trophiesapp.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shs.trophiesapp.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class ImageDownloadThread extends Thread {

    private static final String TAG = "ImageDownloadThread";
    private String mUrl;
    private boolean mSport;
    private WeakReference<Context> contextWeakReference;

    ImageDownloadThread(Context context, String url, boolean sport) {
        this.contextWeakReference = new WeakReference<>(context);
        this.mUrl = url;
        this.mSport = sport;
    }

    @Override
    public void run() {
        String img_name = mUrl.replace(Constants.DRIVE_IMAGE_PREFIX, "").split("/")[0]; //Just the ID
        Log.d(TAG, "Image Name: " + img_name);
        Log.d(TAG, "Url: " + mUrl);

        String[] p = mUrl.split("/");
        if (p.length > 5) {
            String imageLink = Constants.DRIVE_URL + p[5];
            Log.d(TAG, "Download URL: " + imageLink);
            Glide.with(contextWeakReference.get())
                    .setDefaultRequestOptions(new RequestOptions().timeout(300000))
                    .asBitmap()
                    .load(imageLink)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            writeBitmapToFile(resource, img_name);
                        }
                    });
        }
        super.run();
    }

    private void writeBitmapToFile(Bitmap bmp, String filename) {
        File dir;
        if(mSport) dir = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.DATA_DIRECTORY_SPORT_IMAGES);
        else dir = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.DATA_DIRECTORY_TROPHY_IMAGES);
        if(!dir.exists()) dir.mkdirs();
        File file = new File(dir, filename +".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException io) { io.printStackTrace(); }

    }
}
