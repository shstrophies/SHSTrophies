package com.shs.trophiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.shs.trophiesapp.utils.Utils;

public class TrophyPlayersAndYearsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView img;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trophy_players_and_years_activity);

        tvTitle = findViewById(R.id.trophy_players_and_years_title);
        img = findViewById(R.id.trophy_players_and_years_thumbnail);

        //Receive data
        Intent intent = getIntent();
        String sport_name = intent.getExtras().getString("sport_name");
        String tr_title = intent.getExtras().getString("tr_title");
        String tr_image_url = intent.getExtras().getString("tr_image_url");
        int color = intent.getExtras().getInt("color");

        tvTitle.setText(tr_title);
        Utils.imageFromUrl(img, tr_image_url);
        cardView.setBackgroundColor(color);

    }


}

