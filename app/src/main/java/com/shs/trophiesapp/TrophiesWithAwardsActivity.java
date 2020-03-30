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

import java.util.ArrayList;
import java.util.List;
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

        setSearchResultsHeader(searchParams);
        adapter.notifyDataSetChanged();
    }

    int getSearchResultNumber() {
        int result = 0;
        for (TrophyWithAwards item : trophiesWithAwards) result += item.awards.size();
        return result;
    }

    void setSearchResultsHeader(SearchParameters searchParams) {
        Log.d(TAG, "getSearchResultsSummary: ");
        int searchResultNumber = getSearchResultNumber();
        TextView searchHeader = findViewById(R.id.HeaderWithSearchResults);
        String searchResultsSummary = searchResultNumber + " result(s) for " + searchParams.toString();
        searchHeader.setText(searchResultsSummary);
    }
}