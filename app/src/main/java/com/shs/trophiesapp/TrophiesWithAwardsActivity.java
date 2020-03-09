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
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TrophiesWithAwardsActivity extends AppCompatActivity {
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
        TrophyRepository trophyRepository = DataManager.getTrophyRepository(this);


        // CAROLINA HERE
        // Search for player, year, or trophy title. For example, '1976', or 'Williamson', or 'Williamson, 1976', or 'Most Inspirational'

        HashMap<Long, List<TrophyAward>> map = new HashMap<Long, List<TrophyAward>>();
        for (int i = 0; i < searchStrings.length; i++) {
            String searchString = searchStrings[i];

            // search by year
            if (searchString.matches("-?(0|[1-9]\\d*)")) {
                int year = Integer.parseInt(searchString);
                List<TrophyAward> list = DataManager.getTrophyRepository(context).getTrophyAwardsByYear(year);

                for (TrophyAward a : list) {
                    Log.d(TAG, "getData: " + a);
                    long trophyId = a.getTrophyId();
                    if (!map.containsKey(trophyId)) {
                        map.put(trophyId, new ArrayList());
                    }
                    List<TrophyAward> awardList = map.get(trophyId);
                    awardList.add(a);
                }
            }
            else {
                // search by player
                List<TrophyAward> list = DataManager.getTrophyRepository(context).getTrophyAwardsByPlayer(searchString);

                for (TrophyAward a : list) {
                    Log.d(TAG, "getData: " + a);
                    long trophyId = a.getTrophyId();
                    if (!map.containsKey(trophyId)) {
                        map.put(trophyId, new ArrayList());
                    }
                    List<TrophyAward> awardList = map.get(trophyId);
                    awardList.add(a);
                }
            }
        }

        for (Map.Entry<Long, List<TrophyAward>> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
            Log.d(TAG, "getData: " + entry.getKey() + " = " + entry.getValue());
            long trophyId = entry.getKey();
            Trophy trophy = trophyRepository.getTropyById(trophyId);
            List<TrophyAward> awardList = entry.getValue();
            TrophyWithAwards trophyWithAwards = new TrophyWithAwards();
            trophyWithAwards.trophy = trophy;
            trophyWithAwards.awards = awardList;
            trophiesWithAwards.add(trophyWithAwards);
        }


        /////////////////////////////////////////////////////////////////////////////////


//        TrophyRepository tropyRepository = DataManager.getTrophyRepository(context);
//        List<List<Trophy>> listOfTrophies = new ArrayList();
//        for(int i = 0; i< searchStrings.length; i++) {
//            String searchString = searchStrings[i];
//            List<Trophy> sportTrophies  = null;
//            List<Trophy> playerTrophies = null;
//
//
//            List<Trophy> trophies = sportTrophies;
//            trophies.addAll(playerTrophies);
//            if(searchString.matches("-?(0|[1-9]\\d*)")) {
//                int year = Integer.parseInt(searchString);
//                List<Trophy> yearTrophies = null;
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