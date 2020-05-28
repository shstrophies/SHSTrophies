package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.adapters.TrophiesWithAwardsAdapter;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.search.SearchEngine;
import com.shs.trophiesapp.search.SearchParameters;

import java.util.ArrayList;
import java.util.List;

public class TrophiesWithAwardsActivity extends BaseActivity {
    private static final String TAG = "TrophiesWithAwardsActivity";

    Context context;
    private TrophiesWithAwardsAdapter adapter;
    private ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();

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

    private void getData(Intent intent) {
        Log.d(TAG, "TrophiesWithAwardsActivity: getData");

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
        String searchResultsSummary = searchResultNumber + " result(s) for \'" + searchParams.toString()+"\'";
        searchHeader.setText(searchResultsSummary);
    }
}