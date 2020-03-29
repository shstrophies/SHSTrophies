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
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.search.SearchEngine;
import com.shs.trophiesapp.search.SearchParameters;
import com.shs.trophiesapp.utils.Constants;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class TrophiesWithAwardsActivity extends AppCompatActivity {
    private static final String TAG = "TrophiesWithAwardsActivity";

    Context context;
    private TrophiesWithAwardsAdapter adapter;
    private ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();

    public static final String AWARDS_SEARCH_STRING_ALL = "AWARDS_SEARCH_STRING_ALL";
    public static final String AWARDS_SEARCH_STRING_PLAYERS = "AWARDS_SEARCH_STRING_PLAYERS";
    public static final String AWARDS_SEARCH_STRING_SPORTS = "AWARDS_SEARCH_STRING_SPORTS";
    public static final String AWARDS_SEARCH_STRING_TROPHIES = "AWARDS_SEARCH_STRING_TROPHIES";
    public static final String AWARDS_SEARCH_STRING_YEARS = "AWARDS_SEARCH_STRING_YEARS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        // create sports_activity layout object
        setContentView(R.layout.trophies_with_awards_activity);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.trophies_with_awards_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        trophiesWithAwards = new ArrayList<>();
        adapter = new TrophiesWithAwardsAdapter(this, trophiesWithAwards);

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);
        getData(getIntent());
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

    private SearchParameters getSearchParameters(Intent intent) {
        Bundle extras = intent.getExtras();
        return new SearchParameters(
                extras.getLong(SearchParameters.SPORTID),
                extras.getString(SearchParameters.ALL),
                extras.getString(SearchParameters.PLAYERS),
                extras.getString(SearchParameters.SPORTS),
                extras.getString(SearchParameters.YEARS),
                extras.getString(SearchParameters.TROPHIES)
        );
    }

    private void getData(Intent intent) {
        Log.d(TAG, "getData: getData");

        SearchParameters searchParams = getSearchParameters(intent);
        if (!searchParams.getMixed().isEmpty()) {
            ArrayList<Long> sportids = new ArrayList();
            if (searchParams.getSportid() != 0)
                sportids.add(searchParams.getSportid());
            else
                sportids.addAll(DataManager.getSportRepository(context).getSports().stream().map(e -> e.getId()).collect(Collectors.toList()));

            // do the actual search now
            trophiesWithAwards.addAll(SearchEngine.getInstance(context).searchInSports(sportids, searchParams.getMixed()));
        } else {
            // advanced search
            trophiesWithAwards.addAll(SearchEngine.getInstance(context).advancedSearch(searchParams.getSports(), searchParams.getTrophies(), searchParams.getYears(), searchParams.getPlayers()));
        }
        Log.d(TAG, "getData: recyclerview trophiesWithAwards size=" + trophiesWithAwards.size());

        setSearchResultsHeader(searchParams);
        adapter.notifyDataSetChanged();
    }

    void setSearchResultsHeader(SearchParameters searchParams) {
        Log.d(TAG, "getSearchResultsSummary: ");
        TextView searchHeader = findViewById(R.id.HeaderWithSearchResults);
        int result = 0;
        for (TrophyWithAwards item : trophiesWithAwards) result += item.awards.size();
        Long currentSportId = searchParams.getSportid();
        String searchResultsSummary = result + " result(s) for " +
                "\"" + ((currentSportId != 0) ? DataManager.getSportRepository(context).getSportById(currentSportId).getName() : searchParams.getMixed()) + "\"";
        searchHeader.setText(searchResultsSummary);

    }
}