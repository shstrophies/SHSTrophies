package com.shs.trophiesapp.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shs.trophiesapp.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        /*try {
            InputStream is = new URL(Constants.DRIVE_URL + img_name).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            writeBitmapToFile(bitmap, img_name + ".jpg");
            is.close();
        } catch (Exception e) { e.printStackTrace(); }*/

        try {
            SimpleTarget<File> f = Glide
                    .with(contextWeakReference.get())
                    .load(mUrl)
                    .downloadOnly(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            try {
                                Log.d(TAG, "Resource filepath: " + resource.getAbsolutePath());
                                String destPath;
                                if (mSport)
                                    destPath = Environment.getExternalStorageDirectory() + File.separator + Constants.DATA_DIRECTORY_SPORT_IMAGES + File.separator;
                                else
                                    destPath = Environment.getExternalStorageDirectory() + File.separator + Constants.DATA_DIRECTORY_TROPHY_IMAGES + File.separator;
                                String ext = MimeTypeMap.getFileExtensionFromUrl(mUrl);
                                File dest = new File(destPath);
                                exportFile(resource, dest, img_name, ext);
                            } catch (Exception e) {e.printStackTrace();}
                        }
                    });
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

    private void exportFile(File src, File dst, String filename, String ext) throws IOException {

        //if folder does not exist
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return;
            }
        }

        File expFile = new File(dst.getPath() + File.separator + filename + ext);
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(expFile).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }

        Log.d(TAG, "Exported filepath: " + expFile.getAbsolutePath());
    }
}
