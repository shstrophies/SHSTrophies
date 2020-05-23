package com.shs.trophiesapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class Utils {

    public static void imageFromCache(ImageView view, String imageUrl) {
        if(imageUrl.matches("DEFAULT IMAGE")) {
            view.setImageBitmap(imageViaAssets(view.getContext(), Constants.DEFAULT_TROPHY));
        }
        else {
            String[] p = imageUrl.split("/");
            if (p.length > 5) {
                String imageLink = Constants.DRIVE_URL + p[5];
                if(!imageUrl.isEmpty() && !imageUrl.trim().equals("")) {
                    Glide.with(view.getContext())
                            .load(imageLink)
                            .onlyRetrieveFromCache(true)
                            .into(view);
                }
            }
        }
    }

    private static Bitmap imageViaAssets(Context context, String fileName){
        AssetManager assetmanager = context.getAssets();
        InputStream is = null;
        try{
            is = assetmanager.open(fileName);
        }catch(IOException e){
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is);
    }

    public static void synchronousImageDownload(Context context, String imageUrl) {
        synchronized (new Object()) {
            if(imageUrl.matches("DEFAULT IMAGE")) return;
            String[] p = imageUrl.split("/");
            if (p.length > 5) {
                String imageLink = Constants.DRIVE_URL + p[5];
                if(!imageUrl.isEmpty() && !imageUrl.trim().equals("")) {
                    try {
                        Glide.with(context)
                                .downloadOnly()
                                .load(imageLink)
                                .timeout(60000)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                //.get();
                    } catch (Exception e) {e.printStackTrace();}
                }
            }
        }
    }

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
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }
}
