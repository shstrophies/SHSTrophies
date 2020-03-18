package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.shs.trophiesapp.adapters.PersonalPlayerAwardsAdapter;
import com.shs.trophiesapp.adapters.SportWithTrophiesAdapter;
import com.shs.trophiesapp.adapters.TrophyWithAwardsAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.*;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PersonalPlayerAwardsActivity extends AppCompatActivity {

    private static final String TAG = "PersonalPlayerAwardsAct";

    public static final String AWARDS_SEARCH_STRING = "AWARDS_SEARCH_STRING";
    String searchString;
    String[] searchStrings;

    private PersonalPlayerAwardsAdapter adapter;


    private ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_player_awards_activity);



        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.personal_trophies_with_awards_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        trophiesWithAwards = new ArrayList<>();
        adapter = new PersonalPlayerAwardsAdapter(this, trophiesWithAwards);

        Log.d(TAG, "onCreate: started");



        Intent intent = getIntent();
        int color = intent.getExtras().getInt("color");
        String thePlayerName = getIntent().getStringExtra("playerName");
        String url = intent.getExtras().getString("url");
        String lastLetter = thePlayerName.substring(thePlayerName.length() - 1);
        if(lastLetter.equals("s")){
            setPlayerNameText(thePlayerName+"\' awards");
        }
        else{
            setPlayerNameText(thePlayerName+"\'s awards");
        }




        searchString = thePlayerName;

        searchStrings = searchString.split(",");

                // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

        // get data and notify adapter
        getData();

    }

    private void setPlayerNameText(String thePlayerName){
        Log.d(TAG, "setPlayerNameText: setting the name of the player on the top of screen");
        TextView playerName = findViewById(R.id.playersName);
       playerName.setText(thePlayerName);


    }



        private void getData() {
            Log.d(TAG, "getData: getData");
            Context context = this;
            TrophyRepository trophyRepository = DataManager.getTrophyRepository(this);

            // Search for player, year, or trophy title. For example, '1976', or 'Williamson', or 'Williamson, 1976', or 'Most Inspirational'

            int searchStrIndex = 0;
            String keyword = "sportId:";
            int sportId = -1;
            if(searchStrings[0].contains(keyword)) {
                String str = searchStrings[0];
                int index = str.indexOf(keyword) + keyword.length();
                String sportIdstr = str.substring(index);
                sportId = Integer.parseInt(sportIdstr);
                searchStrIndex = 1;
            }

            HashMap<Long, List<TrophyAward>> map = new HashMap<>();
            for (int i = searchStrIndex; i < searchStrings.length; i++) {
                String searchString = searchStrings[i];

                // search by year
                if (searchString.matches("-?(0|[1-9]\\d*)")) {
                    int year = Integer.parseInt(searchString);
                    List<TrophyAward> list = DataManager.getTrophyRepository(context).getTrophyAwardsBySportAndYear(sportId, year);

                    for (TrophyAward a : list) {
                        Log.d(TAG, "getData: " + a);
                        long trophyId = a.getTrophyId();
                        if (!map.containsKey(trophyId)) {
                            map.put(trophyId, new ArrayList<>());
                        }
                        List<TrophyAward> awardList = map.get(trophyId);
                        awardList.add(a);
                    }
                }
                else {
                    // search by player
                    List<TrophyAward> list = DataManager.getTrophyRepository(context).getTrophyAwardsBySportAndPlayer(sportId, searchString);

                    for (TrophyAward a : list) {
                        Log.d(TAG, "getData: " + a);
                        long trophyId = a.getTrophyId();
                        if (!map.containsKey(trophyId)) {
                            map.put(trophyId, new ArrayList<>());
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
                List<TrophyAward> distinctList = awardList.stream().distinct().collect(Collectors.toList());
                TrophyWithAwards trophyWithAwards = new TrophyWithAwards();
                trophyWithAwards.trophy = trophy;
                trophyWithAwards.awards = distinctList;
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
