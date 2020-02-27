package com.shs.trophiesapp.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Utils {

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
}
