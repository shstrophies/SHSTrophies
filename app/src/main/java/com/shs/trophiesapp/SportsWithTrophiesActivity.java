package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.adapters.SportsWithTrophiesAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.relations.SportWithTrophies;

import java.util.ArrayList;
import java.util.List;

public class SportsWithTrophiesActivity extends AppCompatActivity
{
    private static final String TAG = "SportsWithTrophiesActivity";


    private SportsWithTrophiesAdapter adapter;
    private ArrayList<SportWithTrophies> sportsWithTrophies = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receive data
        Intent intent = getIntent();

        // create sports_activity layout object
        setContentView(R.layout.sports_with_trophies_activity);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.sports_with_trophies_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sportsWithTrophies = new ArrayList<SportWithTrophies>();
        adapter = new SportsWithTrophiesAdapter(this, sportsWithTrophies);

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

        // get data and notify adapter
        getData();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    private void getData() {
        Log.d(TAG, "getData: getData");
        Context context = this;
        List<SportWithTrophies> list = DataManager.getTrophyRepository(context).getSportWithTrophies();

        sportsWithTrophies.addAll(list);

        Log.d(TAG, "getData: recyclerview sportsWithTrophies size=" + sportsWithTrophies.size());
        adapter.notifyDataSetChanged();

    }
}