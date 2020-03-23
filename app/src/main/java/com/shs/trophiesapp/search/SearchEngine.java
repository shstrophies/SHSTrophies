package com.shs.trophiesapp.search;

import android.content.Context;
import android.util.Log;

import com.shs.trophiesapp.data.Suggestion;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchEngine {
    private static final String TAG = "SearchEngine";
    Context context;
    SportRepository sportRepository;
    TrophyRepository trophyRepository;

    // Singleton code
    private static SearchEngine single_instance = null;

    // private constructor restricted to this class itself
    private SearchEngine(Context context) {
        this.context = context;
        sportRepository = DataManager.getSportRepository(context);
        trophyRepository = DataManager.getTrophyRepository(context);
    }

    // static method to create instance of Singleton class
    public static SearchEngine getInstance(Context context) {
        if (single_instance == null)
            single_instance = new SearchEngine(context);
        return single_instance;
    }

    // Search for player, year, or trophy title. For example, '1976', or 'Williamson', or 'Williamson, 1976', or 'Most Inspirational'
    public ArrayList<TrophyWithAwards> doSearch(String searchStr) {

        ArrayList<TrophyWithAwards> trophiesWithAwards = new ArrayList<>();

        String[] searchStrings = searchStr.split(",");
        int searchStrIndex = 0;
        String keyword = "sportId:";
        int sportId = -1;
        if (searchStrings[0].contains(keyword)) {
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
            } else {
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

        return trophiesWithAwards;
    }
}
