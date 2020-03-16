package com.shs.trophiesapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

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
