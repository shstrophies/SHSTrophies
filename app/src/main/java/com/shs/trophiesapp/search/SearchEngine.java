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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchEngine {
    private static final String TAG = "SearchEngine";

    public static final String SPORTID_SEARCH_KEY = "sportId:";

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

    public ArrayList<TrophyWithAwards> search(String searchStr) {
        List<Long> sportids = sportRepository.getSports().stream().map(e -> e.getId()).collect(Collectors.toList());
        return searchInSports(sportids, searchStr);
    }

    // Search for player, year, or trophy title. For example, '1976', or 'Williamson', or 'Williamson, 1976', or 'Most Inspirational'
    // another example: "Shaq, 1982, Glen, 1976, Most Inspirational"
    // "1961, Glen, 1983, 1992, Joy
    // select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (year IN (1961, 1983, 1992)) AND ((player LIKE '%glen%') OR (player like '%Joy%'));

    public ArrayList<TrophyWithAwards> searchInSports(List<Long> sportIds, String searchStr) {

        ArrayList<String> titles = new ArrayList();
        ArrayList<Integer> years = new ArrayList();
        ArrayList<String> players = new ArrayList();

        ArrayList<TrophyWithAwards> result = new ArrayList<>();

        // select * from trophyaward where year in (1976, 1991)
        // find list of numbers in the the string
        // Example: "Shaq, 1982, Glen, 1976, Most Inspirational" should return "1982, 1976"
        String digitsString = searchStr.replaceAll("[^0-9]+", " ").trim();
        List<String> digits = digitsString.isEmpty() ? Collections.emptyList() : Arrays.asList(digitsString.trim().split(" "));
        years.addAll(digits.isEmpty() ? Collections.emptyList() : digits.stream().map(e -> Integer.parseInt(e)).collect(Collectors.toList()));

        String nonDigitString = searchStr.replaceAll("[0-9]+\\s*[,]*", " ").replaceAll(",", " ");
        List<String> strings = nonDigitString.isEmpty() ? Collections.emptyList() : Arrays.asList(nonDigitString.trim().split(" "));
        titles.addAll(strings);
        players.addAll(strings);

        List<TrophyAward> list = DataManager.getTrophyRepository(context).getTrophyAwardsBySportsAndTitlesAndYearsAndPlayers(sportIds, titles, years, players);
        HashMap<Long, List<TrophyAward>> map = new HashMap<>();
        for (TrophyAward a : list) {
            Log.d(TAG, "getData: " + a);
            long trophyId = a.getTrophyId();
            if (!map.containsKey(trophyId)) {
                map.put(trophyId, new ArrayList<>());
            }
            List<TrophyAward> awardList = map.get(trophyId);
            awardList.add(a);
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
            result.add(trophyWithAwards);
        }

        return result;
    }

    public ArrayList<TrophyWithAwards> advancedSearch(String sportsStr, String titlesStr, String yearsStr, String playersStr) {
        ArrayList<TrophyWithAwards> result = null;
        return result;
    }
}
