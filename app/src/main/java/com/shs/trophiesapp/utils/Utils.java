package com.shs.trophiesapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shs.trophiesapp.TrophiesActivity;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.AppDatabase;
import com.shs.trophiesapp.database.entities.Sport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;

public class Utils {

    private static List<Sport> sports;

    public static void imageFromCache(ImageView view, String imageUrl) {
        if (imageUrl.matches("DEFAULT IMAGE")) {
            view.setImageBitmap(imageViaAssets(view.getContext(), Constants.DEFAULT_TROPHY));

        } else {
            String[] p = imageUrl.split("/");
            if (p.length > 5) {
                String imageLink = Constants.DRIVE_URL + p[5];
                if (!imageUrl.isEmpty() && !imageUrl.trim().equals("")) {
                    Glide.with(view.getContext())
                            .load(imageLink)
                            .into(view);
                }
            }
        }
    }

    private static Bitmap imageViaAssets(Context context, String fileName) {
        AssetManager assetmanager = context.getAssets();
        InputStream is = null;
        try {
            is = assetmanager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is);
    }

    public static void synchronousImageDownload(Context context, String imageUrl) {
        synchronized (new Object()) {
            if (imageUrl.matches("DEFAULT IMAGE")) return;
            String[] p = imageUrl.split("/");
            if (p.length > 5) {
                String imageLink = Constants.DRIVE_URL + p[5];
                if (!imageUrl.isEmpty() && !imageUrl.trim().equals("")) {
                    try {
                        Glide.with(context)
                                .downloadOnly()
                                .load(imageLink)
                                .listener(new LoggingListener<>())
                                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static String searchSportNameEnhancement(Context context, String text) {
        List<Sport> sports = AppDatabase.getInstance(context).sportDao().getSports();
        for (Sport sport : sports) {
            if (sport.name.trim().toLowerCase().equals(text.trim().toLowerCase())) {
                return sport.name;
            }
        }
        return null;
    }

    /*public static void frescoImgDownload(Context context, String imageUrl) {
        if(imageUrl.matches("DEFAULT IMAGE")) return;
        String[] p = imageUrl.split("/");
        if(p.length > 5) {
            Uri imageLink = Uri.parse(Constants.DRIVE_URL + p[5]);
            if(!imageUrl.isEmpty() && !imageUrl.trim().equals("")) {
                ImagePipeline pipeline = Fresco.getImagePipeline();
                pipeline.prefetchToDiskCache(ImageRequest.fromUri(imageLink), null);

            }
        }
    }*/

    public static String getFileHash(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
            fis.close();

            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte bit : bytes) {
                sb.append(Integer.toString((bit & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent searchKeywordRerouting(Context context, String query) {
        if (sports == null) {
            SportRepository sportRepository = DataManager.getSportRepository(context);
            sports = sportRepository.getSports();
        }
        for (Sport sport : sports) {
            if (query.trim().toLowerCase().equals(sport.name.trim().toLowerCase())) {
                Intent intent = new Intent(context, TrophiesActivity.class);
                intent.putExtra(TrophiesActivity.TROPHIES_BY_SPORT_NAME, sport.name);
                return intent;
            }
        }
        return null;
    }

    public static class LoggingListener<T> implements RequestListener<T> {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<T> target, boolean isFirstResource) {
            e.logRootCauses("ASDF");
            return false; // Allow calling onLoadFailed on the Target.
        }

        @Override
        public boolean onResourceReady(T resource, Object model, Target<T> target, DataSource dataSource, boolean isFirstResource) {
            // Log successes here or use DataSource to keep track of cache hits and misses.
            return false; // Allow calling onResourceReady on the Target.
        }
    }
}
