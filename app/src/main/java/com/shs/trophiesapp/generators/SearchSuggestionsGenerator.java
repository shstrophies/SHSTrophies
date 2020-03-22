package com.shs.trophiesapp.generators;

import android.content.Context;
import android.util.Log;

import com.shs.trophiesapp.data.Suggestion;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Trophy;

import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.paukov.combinatorics.CombinatoricsFactory.createCartesianProductGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

public class SearchSuggestionsGenerator {
    private static final String TAG = "SearchSuggestionsGenera";

    Context context;
    List<Suggestion> suggestions;
    SportRepository sportRepository;
    TrophyRepository trophyRepository;

    // Singleton code
    private static SearchSuggestionsGenerator single_instance = null;

    // private constructor restricted to this class itself
    private SearchSuggestionsGenerator(Context context, List<Suggestion> suggestions) {
        this.context = context;
        this.suggestions = suggestions;
        sportRepository = DataManager.getSportRepository(context);
        trophyRepository = DataManager.getTrophyRepository(context);
    }

    // static method to create instance of Singleton class
    public static SearchSuggestionsGenerator getInstance(Context context, List suggestions) {
        if (single_instance == null)
            single_instance = new SearchSuggestionsGenerator(context, suggestions);
        return single_instance;
    }

    public List<Suggestion> getDefaultSuggestions() {
        final Suggestion[] _suggestions = {
                new Suggestion("Shaquille O'Neil", "   in \"Players\""),
                new Suggestion("Most Inspirational", "   in \"Trophies\""),
                new Suggestion("1976", "   in \"Years\""),
                new Suggestion("Shaquille O'Neil, 1976", "   in \"Trophies\", \"Years\""),
        };
        ArrayList<Suggestion> suggestions = new ArrayList<>();
        suggestions.addAll(this.suggestions);
        Collections.addAll(suggestions, _suggestions);
        return suggestions;
    }


    public List<Suggestion> getSuggestions(String searchString) {
        if (searchString.length() == 0) return getDefaultSuggestions();
        List<String> sports = sportRepository.searchSportName(searchString, 5);
        List<String> trophies = trophyRepository.searchTrophyTitle(searchString, 5);
        List<String> players = trophyRepository.searchPlayerName(searchString, 5);
        YearRange range = getYearRange(searchString);
        List<String> years = trophyRepository.searchYear(range.getYearFrom(), range.getYearTo(), 5);

        ArrayList<Suggestion> allSuggestions = new ArrayList<Suggestion>();
        allSuggestions.addAll(sports.stream().map(e -> new Suggestion(e, "   in \"Sports\"")).collect(Collectors.toList()));
        allSuggestions.addAll(trophies.stream().map(e -> new Suggestion(e, "   in \"Trophies\"")).collect(Collectors.toList()));
        allSuggestions.addAll(players.stream().map(e -> new Suggestion(e, "   in \"Players\"")).collect(Collectors.toList()));
        allSuggestions.addAll(years.stream().map(e -> new Suggestion(e, "   in \"Years\"")).collect(Collectors.toList()));


        ICombinatoricsVector<String> sportsSet = createVector(sports);
        ICombinatoricsVector<String> trophiesSet = createVector(trophies);
        ICombinatoricsVector<String> playersSet = createVector(players);

        for (ICombinatoricsVector<String> cartesianProduct : createCartesianProductGenerator(sportsSet, trophiesSet)) {
            String str = cartesianProduct.getVector().stream()
                    .collect(Collectors.joining(", "));
            allSuggestions.add(new Suggestion(str, "   in \"Sports\", \"Trophies\""));
            Log.d(TAG, "onTextChanged: cartesianProduct=" + cartesianProduct);
        }

        for (ICombinatoricsVector<String> cartesianProduct : createCartesianProductGenerator(trophiesSet, playersSet)) {
            String str = cartesianProduct.getVector().stream()
                    .collect(Collectors.joining(", "));
            allSuggestions.add(new Suggestion(str, "   in \"Trophies\", \"Players\""));
            Log.d(TAG, "onTextChanged: cartesianProduct=" + cartesianProduct);
        }

        for (ICombinatoricsVector<String> cartesianProduct : createCartesianProductGenerator(sportsSet, playersSet)) {
            String str = cartesianProduct.getVector().stream()
                    .collect(Collectors.joining(", "));
            allSuggestions.add(new Suggestion(str, "   in \"Sports\", \"Players\""));
            Log.d(TAG, "onTextChanged: cartesianProduct=" + cartesianProduct);
        }

        Collections.shuffle(allSuggestions);
        allSuggestions.subList(Integer.min(6, allSuggestions.size()), allSuggestions.size()).clear();
        return allSuggestions;
    }


    public YearRange getYearRange(String str){

        int yearFrom = 0 ;
        int yearTo = 0;

        int x = 0;
        try{
            x = Integer.parseInt(str);
        }catch(Exception e){
            return new YearRange(0, Integer.MAX_VALUE);
        }
        // if str is a single digit number, return range for that
        // ex. if x=1, return YearRange(1000, 1999)
        if(x>=0 && x<19){
            yearFrom = x * 1000;
            yearTo = x * 1000 + 999;
        }else if(  x>=10 && x<100 ){
            yearFrom = x * 100;
            yearTo = x * 100 + 99;

        }else if( x>=100 && x<1000){
            yearFrom = x * 10;
            yearTo = x * 10 + 9;

        }else if( x>=1000 && x<10000){
            yearTo = yearFrom = x;
        }else{
            yearFrom = 0;
            yearTo = Integer.MAX_VALUE;
        }







        return new YearRange(yearFrom,yearTo);
    }




}
