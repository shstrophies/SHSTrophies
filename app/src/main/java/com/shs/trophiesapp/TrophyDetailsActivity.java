package com.shs.trophiesapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.utils.Utils;

public class TrophyDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_trophy_details);

        CardView cardView = findViewById(R.id.cardview_trophy_details_id);
        TextView tvTitle = findViewById(R.id.txt_title);

        ImageView img = findViewById(R.id.trophythumbnail);

        //Receive data
        Intent intent = getIntent();
        long trophyId = intent.getExtras().getLong("trophyId");

//        String title = intent.getExtras().getString("title");
//        String trophyUrl = intent.getExtras().getString("url");
        int color = intent.getExtras().getInt("color");

        TrophyRepository trophyRepository = DataManager.getTrophyRepository(this);
        Trophy trophy = trophyRepository.getTropyById(trophyId);
        String title = trophy.getTitle();
        String url = trophy.getUrl();

        tvTitle.setText(title);

        Utils.imageFromCache(img, url);
        cardView.setBackgroundColor(color);

        img.setOnTouchListener(new ImageMatrixTouchHandler(this));
    }


}

