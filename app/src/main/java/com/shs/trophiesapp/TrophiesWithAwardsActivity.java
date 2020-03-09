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
import com.shs.trophiesapp.adapters.TrophiesWithAwardsAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;

import java.util.ArrayList;
import java.util.List;


public class TrophiesWithAwardsActivity extends AppCompatActivity
{
    private static final String TAG = "TrophiesWithAwardsActivity";

    public static final String AWARDS_SEARCH_STRING = "AWARDS_SEARCH_STRING";
    String[] searchStrings;

//    private MaterialSearchBar searchBar;

    private TrophiesWithAwardsAdapter adapter;
    private ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receive data
        Intent intent = getIntent();
        String searchString = intent.getExtras().getString(AWARDS_SEARCH_STRING);
        searchStrings = searchString.split(",");

        // create sports_activity layout object
        setContentView(R.layout.trophies_with_awards_activity);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.trophies_with_awards_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        trophiesWithAwards = new ArrayList<TrophyWithAwards>();
        adapter = new TrophiesWithAwardsAdapter(this, trophiesWithAwards);

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
        int year = 1976;
        String sport = "ba";
        String player = "ba";

        List<TrophyWithAwards> list = DataManager.getTrophyRepository(context).getTrophiesWithAwards();
//        List<TrophyWithAwards> list = DataManager.getTrophyRepository(context).getTrophiesWithAwardsByYearORSportORPlayer(year, sport, player);

        for(TrophyWithAwards s: list) {
            Log.d(TAG, "getData: " + s);
        }
        trophiesWithAwards.addAll(list);
        // CAROLINA HERE
        // Search for a sport name, trophy name, player name, or year...
//        TrophyRepository tropyRepository = DataManager.getTrophyRepository(context);
//        List<List<Trophy>> listOfTrophies = new ArrayList();
//        for(int i = 0; i< searchStrings.length; i++) {
//            String searchString = searchStrings[i];
//            List<Trophy> sportTrophies  = tropyRepository.getSportWithTrophiesBySportName("%" + searchString + "%");
//            List<Trophy> playerTrophies = tropyRepository.getTrophiesByPlayer("%" + searchString + "%");
//
//
//            List<Trophy> trophies = sportTrophies;
//            trophies.addAll(playerTrophies);
//            if(searchString.matches("-?(0|[1-9]\\d*)")) {
//                int year = Integer.parseInt(searchString);
//                List<Trophy> yearTrophies = tropyRepository.getTrophiesByYear(year);
//                trophies.addAll(yearTrophies);
//            }
//
//            List<Trophy> distinctList = trophies.stream().distinct().collect(Collectors.toList());
//            for(Trophy t : distinctList) {
//                List<Trophy> l = new ArrayList();
//                l.add(t);
//                listOfTrophies.add(l);
//            }
//        }
//
//        Map<String, List<Trophy>> map =
//                listOfTrophies.stream().collect(Collectors.groupingBy(e -> e.get(0).sport_name,
//                        Collectors.mapping(e -> e.get(0),
//                                Collectors.toList())));
//
//        for (Map.Entry<String, List<Trophy>> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//            Log.d(TAG, "getData: " + entry.getKey() + " = " + entry.getValue());
//            String sport_name = entry.getKey();
//            List<Trophy> trophyList = entry.getValue();
//            List<Trophy> distinctTrophiesList = trophyList.stream().distinct().collect(Collectors.toList());
//
//            SportsAndTrophiesData data = new SportsAndTrophiesData(distinctTrophiesList, sport_name.substring(0,1).toUpperCase() + sport_name.substring(1).toLowerCase());
//            sportsAndTrophies.add(data);
//        }

        Log.d(TAG, "getData: recyclerview trophiesWithAwards size=" + trophiesWithAwards.size());
        adapter.notifyDataSetChanged();

    }
}