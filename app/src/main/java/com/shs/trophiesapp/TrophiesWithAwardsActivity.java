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
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.search.SearchEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TrophiesWithAwardsActivity extends AppCompatActivity {
    private static final String TAG = "TrophiesWithAwardsActivity";

    public static final String AWARDS_SEARCH_STRING = "AWARDS_SEARCH_STRING";
    String searchString;
    String[] searchStrings;

//    private MaterialSearchBar searchBar;

    private TrophiesWithAwardsAdapter adapter;
    private ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receive data
        Intent intent = getIntent();
        searchString = intent.getExtras().getString(AWARDS_SEARCH_STRING);
        searchStrings = searchString.split(",");

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




        // get data and notify adapter
        getData();
        int result = 0;


        for(int i =0;i<trophiesWithAwards.size();i++){

            TrophyWithAwards item = trophiesWithAwards.get(i);
            result+=item.awards.size();

        }

        if(searchString.contains("sportId")){
            String searchResultText = searchString.substring(searchString.indexOf(",")+1);
            searchHeader.setText(result +" result(s) for \""+searchResultText+"\"");
        }
        else{
            searchHeader.setText(result +" result(s) for \""+searchString+"\"");

        }





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

    private void getData() {
        Log.d(TAG, "getData: getData");
        Context context = this;
        trophiesWithAwards.addAll(SearchEngine.getInstance(context).doSearch(searchString));
        Log.d(TAG, "getData: recyclerview trophiesWithAwards size=" + trophiesWithAwards.size());
        adapter.notifyDataSetChanged();

    }
}