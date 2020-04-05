package com.shs.trophiesapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.adapters.SportsWithTrophiesAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.relations.SportWithTrophies;

import java.util.ArrayList;
import java.util.List;

public class SportsWithTrophiesActivity extends BaseActivity {
    private static final String TAG = "SportsWithTrophiesActivity";
    //private static final int PAGE_LIMIT = 12;

    private SportsWithTrophiesAdapter adapter;
    private ArrayList<SportWithTrophies> sportsWithTrophies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_with_trophies_activity);

        RecyclerView recyclerView = findViewById(R.id.sports_with_trophies_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        sportsWithTrophies = new ArrayList<>();
        recyclerView.setAdapter(adapter = new SportsWithTrophiesAdapter(this, sportsWithTrophies));

        getData();
    }

    private void getData() {
        Log.d(TAG, "getData: getData");

        List<SportWithTrophies> list = DataManager.getTrophyRepository(this).getSportWithTrophies();
        sportsWithTrophies.addAll(list);

        Log.d(TAG, "getData: recyclerview sportsWithTrophies size=" + sportsWithTrophies.size());
        adapter.notifyDataSetChanged();

    }
}