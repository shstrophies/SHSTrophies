package com.shs.trophiesapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.Utils;

public class TrophyDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvPlayer, tvCategory;
    private ImageView img;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_trophy_details_activity);

        cardView = findViewById(R.id.cardview_trophy_details_id);
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
        int color = intent.getExtras().getInt("color");

        tvTitle.setText(tr_title);
        tvPlayer.setText(player);
        tvCategory.setText(category);
        Utils.imageFromUrl(img, tr_image_url);
        cardView.setBackgroundColor(color);

    }


}
