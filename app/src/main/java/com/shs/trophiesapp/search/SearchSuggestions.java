package com.shs.trophiesapp.search;

import android.content.Context;
import android.util.Log;

import com.shs.trophiesapp.data.Suggestion;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;

import org.paukov.combinatorics.ICombinatoricsVector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.paukov.combinatorics.CombinatoricsFactory.createCartesianProductGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

public class SearchSuggestions {
    private static final String TAG = "SearchSuggestionsGenera";

    private WeakReference<Context> context;
    private List<Suggestion> suggestions;
    private SportRepository sportRepository;
    private TrophyRepository trophyRepository;

    // Singleton code
    private static SearchSuggestions single_instance = null;

    // private constructor restricted to this class itself
    private SearchSuggestions(Context context, List<Suggestion> suggestions) {
        this.context = new WeakReference<>(context);
        this.suggestions = suggestions;
        sportRepository = DataManager.getSportRepository(context);
        trophyRepository = DataManager.getTrophyRepository(context);
    }

    // static method to create instance of Singleton class
    public static SearchSuggestions getInstance(Context context, List<Suggestion> suggestions) {
        if (single_instance == null)
            single_instance = new SearchSuggestions(context, suggestions);
        return single_instance;
    }

    public List<Suggestion> getDefaultSuggestions() {
//        final Suggestion[] _suggestions = {
//                new Suggestion("Shaquille O'Neil", "   in \"Players\""),
//                new Suggestion("Most Inspirational", "   in \"Trophies\""),
//                new Suggestion("1976", "   in \"Years\""),
//                new Suggestion("Shaquille O'Neil, 1976", "   in \"Trophies\", \"Years\""),
//        };

        List<Suggestion> generatedSuggestions = SearchSuggestions.getInstance(context.get(), suggestions).getSuggestions("");
        generatedSuggestions.forEach(e -> Log.d(TAG, "onTextChanged: suggestion=" + e.toString()));
        return generatedSuggestions;
    }


    public List<Suggestion> getFirstSuggestions() {
        ArrayList<Suggestion> allSuggestions = new ArrayList<>();
        List<TrophyAward> trophyAwards = trophyRepository.getRandomTrophies(6);
        Suggestion suggestion;
        Trophy trophy;
        Sport sport;

        TrophyAward award = trophyAwards.get(0);
        suggestion = new Suggestion(award.getPlayer() + " " + award.getYear(), "   in \"Players\", \"Years\"");
        allSuggestions.add(suggestion);

        award = trophyAwards.get(1);
        trophy =  trophyRepository.getTropyById(award.getTrophyId());
        sport = sportRepository.getSportById(trophy.getSportId());
        suggestion = new Suggestion(sport.getName() + " " + trophy.getTitle(), "   in \"Sports\", \"Trophies\"");
        allSuggestions.add(suggestion);

        award = trophyAwards.get(2);
        trophy = trophyRepository.getTropyById(award.getTrophyId());
        suggestion = new Suggestion(award.getPlayer() + " " + award.getYear(), "   in \"Players\", \"Years\"");
        allSuggestions.add(suggestion);

        award = trophyAwards.get(3);
        trophy = trophyRepository.getTropyById(award.getTrophyId());
        sport = sportRepository.getSportById(trophy.getSportId());
        suggestion = new Suggestion(sport.getName() + " " + award.getYear(), "   in \"Sports\", \"Years\"");
        allSuggestions.add(suggestion);

        award = trophyAwards.get(4);
        trophy = trophyRepository.getTropyById(award.getTrophyId());
        suggestion = new Suggestion(award.getPlayer() + " " + trophy.getTitle(), "   in \"Players\", \"Trophies\"");
        allSuggestions.add(suggestion);

        award = trophyAwards.get(5);
        trophy = trophyRepository.getTropyById(award.getTrophyId());
        suggestion = new Suggestion(trophy.getTitle() + " " + award.getYear(), "   in \"Trophies\", \"Years\"");
        allSuggestions.add(suggestion);


        return allSuggestions;
    }

    public List<Suggestion> getSuggestions(String searchString) {
        if(searchString.isEmpty()) return getFirstSuggestions();
        List<String> sports = sportRepository.searchSportName(searchString, 5);
        List<String> trophies = trophyRepository.searchTrophyTitle(searchString, 5);
        List<String> players = trophyRepository.searchPlayerName(searchString, 5);
        YearRange range = getYearRange(searchString);
        List<String> years = trophyRepository.searchYear(range.getYearFrom(), range.getYearTo(), 5);

        ArrayList<Suggestion> allSuggestions = new ArrayList<>();
        allSuggestions.addAll(sports.stream().map(e -> new Suggestion(e, "   in \"Sports\"")).collect(Collectors.toList()));
        allSuggestions.addAll(trophies.stream().map(e -> new Suggestion(e, "   in \"Trophies\"")).collect(Collectors.toList()));
        allSuggestions.addAll(players.stream().map(e -> new Suggestion(e, "   in \"Players\"")).collect(Collectors.toList()));
        allSuggestions.addAll(years.stream().map(e -> new Suggestion(e, "   in \"Years\"")).collect(Collectors.toList()));


        ICombinatoricsVector<String> sportsSet = createVector(sports);
        ICombinatoricsVector<String> trophiesSet = createVector(trophies);
        ICombinatoricsVector<String> playersSet = createVector(players);

        for (ICombinatoricsVector<String> cartesianProduct : createCartesianProductGenerator(sportsSet, trophiesSet)) {
            String str = cartesianProduct.getVector().stream()
                    .collect(Collectors.joining(" "));
            allSuggestions.add(new Suggestion(str, "   in \"Sports\", \"Trophies\""));
            Log.d(TAG, "onTextChanged: cartesianProduct=" + cartesianProduct);
        }

        for (ICombinatoricsVector<String> cartesianProduct : createCartesianProductGenerator(trophiesSet, playersSet)) {
            String str = cartesianProduct.getVector().stream()
                    .collect(Collectors.joining(" "));
            allSuggestions.add(new Suggestion(str, "   in \"Trophies\", \"Players\""));
            Log.d(TAG, "onTextChanged: cartesianProduct=" + cartesianProduct);
        }

        for (ICombinatoricsVector<String> cartesianProduct : createCartesianProductGenerator(sportsSet, playersSet)) {
            String str = cartesianProduct.getVector().stream()
                    .collect(Collectors.joining(" "));
            allSuggestions.add(new Suggestion(str, "   in \"Sports\", \"Players\""));
            Log.d(TAG, "onTextChanged: cartesianProduct=" + cartesianProduct);
        }

        Collections.shuffle(allSuggestions);
        allSuggestions.subList(Integer.min(6, allSuggestions.size()), allSuggestions.size()).clear();
        List<Suggestion> result = new ArrayList<>(new HashSet<>(allSuggestions.stream()
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(Suggestion::getTitle))))));

        return result;
    }


    public YearRange getYearRange(String str){

        int yearFrom, yearTo;
        int x;

        try{
            x = Integer.parseInt(str);
        }catch(Exception e){
            return new YearRange(0, 0);
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
