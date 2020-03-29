package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.adapters.TrophiesWithAwardsAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.search.SearchEngine;
import com.shs.trophiesapp.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TrophiesWithAwardsActivity extends AppCompatActivity {
    private static final String TAG = "TrophiesWithAwardsActivity";

    private TrophiesWithAwardsAdapter adapter;
    private ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();

    public static final String AWARDS_SEARCH_STRING = "AWARDS_SEARCH_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create sports_activity layout object
        setContentView(R.layout.trophies_with_awards_activity);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.trophies_with_awards_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        trophiesWithAwards = new ArrayList<>();
        adapter = new TrophiesWithAwardsAdapter(this, trophiesWithAwards);

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);
        TextView searchHeader = findViewById(R.id.HeaderWithSearchResults);

        String searchString = getIntent().getExtras().getString(AWARDS_SEARCH_STRING);
        getData(searchString);
        searchHeader.setText(getSearchResultsSummary(searchString));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    String getSearchResultsSummary(String searchString) {
        Log.d(TAG, "getSearchResultsSummary: ");
        Context context = this;
        int result = 0;
        for (TrophyWithAwards item : trophiesWithAwards) { result += item.awards.size(); }
        Long currentSportId = getIntent().getExtras().getLong(Constants.CURRENT_SPORT_ID);
        String searchResultsSummary = result + " result(s) for " +
                "\"" + ((currentSportId != 0) ? DataManager.getSportRepository(context).getSportById(currentSportId).getName() : searchString) + "\"";
        return searchResultsSummary;
    }
}