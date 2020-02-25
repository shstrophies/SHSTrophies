package com.shs.trophiesapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.shs.trophiesapp.utils.Constants;

import java.io.IOException;
import java.io.InputStream;

public class TrophyDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvPlayer, tvCategory;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trophy_details_activity);

        tvTitle = findViewById(R.id.txt_title);
        tvPlayer = findViewById(R.id.txt_player);
        tvCategory = findViewById(R.id.txt_cat);
        img = findViewById(R.id.trophythumbnail);

        //Receive data
        Intent intent = getIntent();
        String sport_name = intent.getExtras().getString("sport_name");
        int year = intent.getExtras().getInt("year");
        String tr_title = intent.getExtras().getString("tr_title");
        String player = intent.getExtras().getString("player");
        String category = intent.getExtras().getString("category");
        String tr_image_url = intent.getExtras().getString("tr_image_url");

        tvTitle.setText(tr_title);
        tvPlayer.setText(player);
        tvCategory.setText(category);
        imageFromUrl(img, tr_image_url);

    }

    private static void imageFromUrl(ImageView view, String imageUrl) {
        String[] p=imageUrl.split("/");
        if(p.length > 5) {
            //Create the new image link
            String imageLink= Constants.DRIVE_URL+p[5];

            if ((imageUrl !=  null) && !imageUrl.isEmpty()) {
                Glide.with(view.getContext()).load(imageLink).into(view);

            }
        }

    }
}

