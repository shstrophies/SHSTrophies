package com.shs.trophiesapp.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.HashMap;

public class Utils {

    public static HashMap<String, String> linkedImages = new HashMap<>();

    public static void imageFromUrl(ImageView view, String imageUrl) {
        String[] p = imageUrl.split("/");
        if (p.length > 5) {
            //Create the new image link
            String imageLink = Constants.DRIVE_URL + p[5];

            if ((imageUrl != null) && !imageUrl.isEmpty()) {
                Glide.with(view.getContext()).load(imageLink).into(view);

            }
        }
    }

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

    public static File synchronousImageDownload(Context context, String imageUrl) {
        String[] p = imageUrl.split("/");
        if (p.length > 5) {
            String imageLink = Constants.DRIVE_URL + p[5];
            if(!imageUrl.isEmpty() && !imageUrl.trim().equals("")) {
                try {
                    return Glide.with(context)
                            .load(imageLink)
                            .downloadOnly(500, 500).get();
                } catch (Exception e) {e.printStackTrace();}
            }
        }
        return null;
    }
}
