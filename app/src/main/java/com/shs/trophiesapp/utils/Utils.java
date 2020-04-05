package com.shs.trophiesapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;

public class Utils {





    /*public static HashMap<String, String> linkedImages = new HashMap<>();

    public static void imageFromUrl(ImageView view, String imageUrl) {
        String[] p = imageUrl.split("/");
        if (p.length > 5) {
            //Create the new image link
            String imageLink = Constants.DRIVE_URL + p[5];

            if ((imageUrl != null) && !imageUrl.isEmpty()) {
                Glide.with(view.getContext()).load(imageLink).into(view);

            }
        }
    }*/

    public static void imageFromCache(ImageView view, String imageUrl) {
        if(imageUrl.matches("DEFAULT IMAGE")) {
            view.setImageBitmap(imageViaAssets(view.getContext(), view, Constants.DEFAULT_TROPHY));
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

    public static Bitmap imageViaAssets(Context context, ImageView view, String fileName){
        AssetManager assetmanager = context.getAssets();
        InputStream is = null;
        try{
            is = assetmanager.open(fileName);
        }catch(IOException e){
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    public static void synchronousImageDownload(Context context, String imageUrl) {
        String[] p = imageUrl.split("/");
        if (p.length > 5) {
            String imageLink = Constants.DRIVE_URL + p[5];
            if(!imageUrl.isEmpty() && !imageUrl.trim().equals("")) {
                try {
                    /*Glide.with(context)
                            .load(imageLink)
                            .downloadOnly(500, 500).get();*/
                    Glide.with(context)
                            .load(imageLink)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (Exception e) {e.printStackTrace();}
            }
        }
    }
}
