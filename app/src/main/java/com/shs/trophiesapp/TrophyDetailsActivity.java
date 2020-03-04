package com.shs.trophiesapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.shs.trophiesapp.utils.Utils;

public class TrophyDetailsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView img;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_trophy_details);

        cardView = findViewById(R.id.cardview_trophy_details_id);
        tvTitle = findViewById(R.id.txt_title);

        img = findViewById(R.id.trophythumbnail);

        //Receive data
        Intent intent = getIntent();
        String sport_name = intent.getExtras().getString("name");
        int year = intent.getExtras().getInt("year");
        String trophy_title = intent.getExtras().getString("trophy_title");
        String tr_image_url = intent.getExtras().getString("tr_image_url");
        int color = intent.getExtras().getInt("color");

        tvTitle.setText(trophy_title);

        Utils.imageFromUrl(img, tr_image_url);
        cardView.setBackgroundColor(color);

    }


}

