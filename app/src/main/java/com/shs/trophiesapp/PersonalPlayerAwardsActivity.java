package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.shs.trophiesapp.adapters.PersonalPlayerAwardsAdapter;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.search.SearchEngine;
import com.shs.trophiesapp.search.SearchParameters;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PersonalPlayerAwardsActivity extends AppCompatActivity {

    private static final String TAG = "PersonalPlayerAwardsAct";
    Context context;
    private PersonalPlayerAwardsAdapter adapter;
    private ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
        getData(intent);

    }

    private void setPlayerNameText(String thePlayerName) {
        Log.d(TAG, "setPlayerNameText: setting the name of the player on the top of screen");
        TextView playerName = findViewById(R.id.playersName);
        playerName.setText(thePlayerName);
    }


    private void getData(Intent intent) {
        Log.d(TAG, "getData: getData");
        SearchParameters searchParams = SearchEngine.getSearchParameters(intent);
        if (!searchParams.getAll().isEmpty()) {
            // do search
            List<Long> sportids = SearchEngine.getInstance(context).getSportIds(searchParams);
            trophiesWithAwards.addAll(SearchEngine.getInstance(context).searchInSports(sportids, searchParams.getAll()));
        } else {
            // do advanced search
            trophiesWithAwards.addAll(SearchEngine.getInstance(context).advancedSearch(searchParams));
        }
        Log.d(TAG, "getData: recyclerview trophiesWithAwards size=" + trophiesWithAwards.size());
        adapter.notifyDataSetChanged();
    }


}
