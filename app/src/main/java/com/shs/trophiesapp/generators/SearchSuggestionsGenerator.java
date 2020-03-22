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
        List<String> sportStrings = sportRepository.searchSportName(searchString, 5);
        List<String> trophyTitles = trophyRepository.searchTrophyTitle(searchString, 5);
        List<String> playerStrings = trophyRepository.searchPlayerName(searchString, 5);

        ArrayList<Suggestion> _suggestions = new ArrayList<Suggestion>();
        _suggestions.addAll(sportStrings.stream().map(e -> new Suggestion(e, "   in \"Sports\"")).collect(Collectors.toList()));
        _suggestions.addAll(trophyTitles.stream().map(e -> new Suggestion(e, "   in \"Trophies\"")).collect(Collectors.toList()));
        _suggestions.addAll(playerStrings.stream().map(e -> new Suggestion(e, "   in \"Players\"")).collect(Collectors.toList()));

        ICombinatoricsVector<String> set01 = createVector(sportStrings);
        ICombinatoricsVector<String> set02 = createVector(trophyTitles);
        ICombinatoricsVector<String> set03 = createVector(playerStrings);

        Generator<String> generator = createCartesianProductGenerator(set01, set02, set03);

        for (ICombinatoricsVector<String> cartesianProduct : generator) {
            String str = cartesianProduct.getVector().stream()
                    .collect(Collectors.joining(", "));
            _suggestions.add(new Suggestion(str, "   in \"Sports\", \"Trophies\", \"Players\""));
            Log.d(TAG, "onTextChanged: cartesianProduct=" + cartesianProduct);
        }
        Collections.shuffle(_suggestions);
        _suggestions.subList(Integer.min(6, _suggestions.size()), _suggestions.size()).clear();
        return _suggestions;
    }


}
