package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shs.trophiesapp.adapters.SportsAndTrophiesAdapter;
import com.shs.trophiesapp.data.DataManager;
import com.shs.trophiesapp.data.SportsAndTrophiesData;
import com.shs.trophiesapp.data.TrophyRepository;
import com.shs.trophiesapp.data.entities.Sport;
import com.shs.trophiesapp.adapters.SportAdapter;
import com.shs.trophiesapp.data.entities.Trophy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SportsAndTrophiesActivity extends AppCompatActivity
//        implements MaterialSearchBar.OnSearchActionListener
{
    private static final String TAG = "SportsAndTrophiesActivity";

    public static final String TROPHIES_BY_SPORTS_NAMES = "Sports";
    String[] sportNames;

//    private MaterialSearchBar searchBar;

    private SportsAndTrophiesAdapter adapter;
    private ArrayList<SportsAndTrophiesData> sportsAndTrophies = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receive data
        Intent intent = getIntent();
        String sports = intent.getExtras().getString(TROPHIES_BY_SPORTS_NAMES);
        sportNames = sports.split(",");

        // create sports_activity layout object
        setContentView(R.layout.sports_and_trophies_activity);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.sports_and_trophies_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sportsAndTrophies = new ArrayList<SportsAndTrophiesData>();
        adapter = new SportsAndTrophiesAdapter(this, sportsAndTrophies);

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

        // get data and notify adapter
        getData();

//        searchBar = findViewById(R.id.sports_and_trophies_search);
//        searchBar.setOnSearchActionListener(this);
//        searchBar.inflateMenu(R.menu.main);
//        searchBar.setHint("???...");
//
//        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + searchBar.getText());
//        searchBar.setCardViewElevation(10);
//        searchBar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d(TAG, "beforeTextChanged: ");
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d(TAG, "onTextChanged: text changed " + searchBar.getText());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Log.d(TAG, "afterTextChanged: ");
//                doSearch(searchBar.getText());
//
//            }
//
//        });

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


//    @Override
//    public void onSearchStateChanged(boolean enabled) {
//    }
//
//    @Override
//    public void onSearchConfirmed(CharSequence text) {
//
//    }
//
//    @Override
//    public void onButtonClicked(int buttonCode) {
//        switch (buttonCode) {
//            case MaterialSearchBar.BUTTON_SPEECH:
//                break;
//            case MaterialSearchBar.BUTTON_BACK:
//                searchBar.closeSearch();
//                break;
//        }
//    }

    private void getData() {
        Log.d(TAG, "getData: getData");
        Context context = this;
        TrophyRepository tropyRepository = DataManager.getTrophyRepository(context);
        for(int i=0; i<sportNames.length; i++) {
            String sportName = sportNames[i];
            List<Trophy> trophies = tropyRepository.getTrophiesBySport("%" + sportName + "%");
            List<Trophy> distinctList = trophies.stream().distinct().collect(Collectors.toList());
            List<List<Trophy>> listOfTrophies = new ArrayList();
            for(Trophy t : distinctList) {
                List<Trophy> l = new ArrayList();
                l.add(t);
                listOfTrophies.add(l);
            }
            Map<String, List<Trophy>> map =
                    listOfTrophies.stream().collect(Collectors.groupingBy(e -> e.get(0).sport_name,
                            Collectors.mapping(e -> e.get(0),
                                    Collectors.toList())));

            for (Map.Entry<String, List<Trophy>> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
                String sport_name = entry.getKey();
                List<Trophy> trophyList = entry.getValue();
                SportsAndTrophiesData data = new SportsAndTrophiesData(trophyList, sport_name.substring(0,1).toUpperCase() + sport_name.substring(1).toLowerCase());
                sportsAndTrophies.add(data);
            }

            Log.d(TAG, "getData: recyclerview sportsAndTrophies size=" + sportsAndTrophies.size());
            adapter.notifyDataSetChanged();
        }

    }

//    // search data
//    private void doSearch(String searchText) {
//        Log.d(TAG, "doSearch: " + searchBar.getText());
//        adapter.getFilter().filter(searchText);
//    }
}