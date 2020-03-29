package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.shs.trophiesapp.adapters.PersonalPlayerAwardsAdapter;
import com.shs.trophiesapp.adapters.SportWithTrophiesAdapter;
import com.shs.trophiesapp.adapters.TrophyWithAwardsAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.search.SearchEngine;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.*;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PersonalPlayerAwardsActivity extends AppCompatActivity {

    private static final String TAG = "PersonalPlayerAwardsAct";
    private PersonalPlayerAwardsAdapter adapter;
    private ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_player_awards_activity);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.personal_trophies_with_awards_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        trophiesWithAwards = new ArrayList<>();
        adapter = new PersonalPlayerAwardsAdapter(this, trophiesWithAwards);
        Log.d(TAG, "onCreate: started");
        Intent intent = getIntent();
        int color = intent.getExtras().getInt("color");
        String thePlayerName = getIntent().getStringExtra("playerName");
        String url = intent.getExtras().getString("url");
        String lastLetter = thePlayerName.substring(thePlayerName.length() - 1);
        if (lastLetter.equals("s")) {
            setPlayerNameText(thePlayerName + "\' awards");
        } else {
            setPlayerNameText(thePlayerName + "\'s awards");
        }

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

        // get data and notify adapter
        getData(thePlayerName);

    }

    private void setPlayerNameText(String thePlayerName) {
        Log.d(TAG, "setPlayerNameText: setting the name of the player on the top of screen");
        TextView playerName = findViewById(R.id.playersName);
        playerName.setText(thePlayerName);
    }

    private void getData(String searchString) {
        Log.d(TAG, "getData: getData");
        Context context = this;
        Long currentSportId = getIntent().getExtras().getLong(Constants.CURRENT_SPORT_ID);
        ArrayList<Long> sportids = new ArrayList();
        if (currentSportId != 0)
            sportids.add(currentSportId);
        else
            sportids.addAll(DataManager.getSportRepository(context).getSports().stream().map(e -> e.getId()).collect(Collectors.toList()));

        trophiesWithAwards.addAll(SearchEngine.getInstance(context).searchInSports(sportids, searchString));
        Log.d(TAG, "getData: recyclerview trophiesWithAwards size=" + trophiesWithAwards.size());
        adapter.notifyDataSetChanged();
    }


}
