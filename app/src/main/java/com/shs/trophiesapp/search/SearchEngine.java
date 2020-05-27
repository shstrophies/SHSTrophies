package com.shs.trophiesapp.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;

import java.lang.ref.WeakReference;
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
    public static final String delimiter = "#";

    private WeakReference<Context> context;
    private SportRepository sportRepository;
    private TrophyRepository trophyRepository;

    // Singleton code
    private static SearchEngine single_instance = null;

    // private constructor restricted to this class itself
    private SearchEngine(Context context) {
        this.context = new WeakReference<>(context);
        sportRepository = DataManager.getSportRepository(context);
        trophyRepository = DataManager.getTrophyRepository(context);
    }

    // static method to create instance of Singleton class
    public static SearchEngine getInstance(Context context) {
        if (single_instance == null)
            single_instance = new SearchEngine(context);
        return single_instance;
    }

    public static SearchParameters getSearchParameters(Intent intent) {
        Bundle extras = intent.getExtras();
        return new SearchParameters(
                extras.getString(SearchParameters.ALL),
                extras.getString(SearchParameters.PLAYERNAMES),
                extras.getString(SearchParameters.SPORTNAMES),
                extras.getString(SearchParameters.YEARS),
                extras.getString(SearchParameters.TROPHYTITLES)
        );
    }

    // Find list of numbers in the the string
    // Get all digit strings separated by space delimiter (use replaceAll method)
    // Example: "Shaq, 1982, Glen, 1976, Most Inspirational" should return "1982 1976"
    // This will be used in query as follows:
    // select * from trophyaward where year in (1976, 1991)
    String getAllDigitsString(String input) {
        return input.replaceAll("[^0-9]+", " ").trim();
    }

    // Convert string containing digits separated by space delimiter to list of digit strings (use Arrays.asList())
    // if input is empty, return empty list (use Collections.emptyList())
    // Example: input: "1982 1076" should return a list of "1982" and "1976"
    List<String> convertToDigitsList(String input) {
        return input.isEmpty() ? Collections.emptyList() : Arrays.asList(input.trim().split(" "));
    }

    // Convert list of strings to list of integers (use stream().map(inputElem -> /* conversion */)
    // if input is empty, return empty list (use Collections.emptyList())
    // if input element is not an integer string then covert it to 0
    List<Integer> convertToListOfIntegers(List<String> input) {
        return input.isEmpty() ? Collections.emptyList() : input.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    // Find list of non-digit strings in the the string
    // Get all non-digit strings separated by # delimiter (use replaceAll method)
    // Example: "Shaq, 1982, Glen, 1976, Most Inspirational" should return "Shaq#Glen#Most Ispirational"
    // If there is more than one space replace it with one space (example: "Shaq      Glen" should replace it with "Shaq Glen"
    // Example: "Shaq, 1982, Glen, 1976, Most Inspirational" should return "Shaq#Glen#Most Inspirational"
    // This will be used in query as follows:
    // select * from trophyaward where (player like '%Shaq%') OR (player like '%Glen%') OR (player like '%Most Inspirational%')
    String getAllNonDigitsString(String input, String delimiter) {
        return input.replaceAll("[0-9]+\\s*[,]*", " ").replaceAll(",", delimiter).replaceAll("\\s+", " ");
    }

    // Convert strings separated by delimiter to list of strings
    // input is one string which contains strings separated by delimiter (example: "Shaq#Glen#Most Inspirational")
    // if input is empty, return empty list (use Collections.emptyList())
    List<String> convertToListOfStrings(String input, String delimiter) {
        return input.isEmpty() ? Collections.emptyList() : Arrays.asList(input.trim().split(delimiter));
    }

    // Convert list of strings to list of sport ids (use stream().map(inputElem -> /* conversion */)
    // if input is empty, return empty list (use Collections.emptyList())
    List<Long> convertToListOfSportIds(List<String> input) {
        List<Long> list =  input.isEmpty() ? Collections.emptyList() :
                input.stream().map(inputElem -> {
                    List<Sport> sports = sportRepository.searchSportByName(inputElem);
                    if(sports.size() > 0) return sports.get(0).getId();
                    else return null;
                }).collect(Collectors.toList());
        if(list.get(0) != null) return list; //This is a shitty solution, but works for the time being TODO: Fix this
        else return Collections.emptyList();
    }

    // Convert input list of TrophyAward objects to list of TrophyWithAwards
    // Use a HashMap where the key is TrophyAward trophyIds and the value is the TrophyAward
    // Then iterate through the hashMap and for each trophyId key, get the Trophy object (use trophyRepository.getTrophyById(trophyId) method).
    // Construct the TrophyAward objects and add it to the result list.
    List<TrophyWithAwards> converttoListOfTrophyWithAwards(List<TrophyAward> input) {
        ArrayList<TrophyWithAwards> result = new ArrayList<>();

        HashMap<Long, List<TrophyAward>> map = new HashMap<>();
        for (TrophyAward award : input) {
            Log.d(TAG, "converttoListOfTrophyWithAwards: " + award);
            long trophyId = award.getTrophyId();
            if (!map.containsKey(trophyId)) map.put(trophyId, new ArrayList<>());
            List<TrophyAward> awardList = map.get(trophyId);
            awardList.add(award);
        }
        for (Map.Entry<Long, List<TrophyAward>> entry : map.entrySet()) {
            Log.d(TAG, "converttoListOfTrophyWithAwards: " + entry.getKey() + " = " + entry.getValue());
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

    // Get list of sport ids from search parameter sportsStr
    public List<Long> getSportIds(String sportNames, boolean allIdsIfEmpty) {
        String nonDigitTitlesString = getAllNonDigitsString(sportNames, delimiter);
        List<String> titlesList = convertToListOfStrings(nonDigitTitlesString, delimiter);
        return (titlesList.isEmpty() && allIdsIfEmpty) ? sportRepository.getSports().stream().map(Sport::getId).collect(Collectors.toList())
                : convertToListOfSportIds(titlesList);
    }

    public Long getSportId(String sportNames) {
        List<Long> sportIds = getSportIds(sportNames, false);
        return sportIds.get(0);
    }

    // Get list of sport ids from search parameter sportsStr
    public List<Long> getSportIds(SearchParameters searchParameters) {
        return getSportIds(searchParameters.getSportNames(), searchParameters.getAll().isEmpty() || searchParameters.getSportNames().isEmpty());
    }

    // Get list of years from search parameter yearsStr
    // Example: input: "1982 1076" should return a list of "1982" and "1976"
    public List<Integer> getYears(SearchParameters searchParameters) {
        String digitsString = getAllDigitsString(searchParameters.getYears());
        List<String> digitsList = convertToDigitsList(digitsString);
        return convertToListOfIntegers(digitsList);
    }

    // Find list of non-digit strings in the titlesStr string
    // Get all non-digit strings separated by # delimiter (use replaceAll method)
    // Example: "Most Inspirational, Most Valuable" should return "Most Inspirational#Most Valuable"
    public List<String> getTitles(SearchParameters searchParameters) {
        String titlesStr = searchParameters.getTrophyTitles();
        String nonDigitTitlesString = getAllNonDigitsString(titlesStr, delimiter);
        return convertToListOfStrings(nonDigitTitlesString, delimiter);
    }

    // Find list of non-digit strings in the playersStr string
    // Get all non-digit strings separated by # delimiter (use replaceAll method)
    // Example: "Glen, Anna Smith" should return "Glen#Anna Smith"
    public List<String> getPlayers(SearchParameters searchParameters) {
        String nonDigitPlayersString = getAllNonDigitsString(searchParameters.getPlayerNames(), delimiter);
        return convertToListOfStrings(nonDigitPlayersString, delimiter);
    }

    public ArrayList<TrophyWithAwards> search(String searchStr) {
        List<Long> sportids = sportRepository.getSports().stream().map(Sport::getId).collect(Collectors.toList());
        return searchInSports(sportids, searchStr);
    }

    // Search for player, year, or trophy title. For example, '1976', or 'Williamson', or 'Williamson, 1976', or 'Most Inspirational'
    // another example: "Shaq, 1982, Glen, 1976, Most Inspirational"
    // "1961, Glen, 1983, 1992, Joy"
    // This should result in the following query:
    // select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (year IN (1961, 1983, 1992)) AND ((player LIKE '%glen%') OR (player like '%Joy%'));
    public ArrayList<TrophyWithAwards> searchInSports(List<Long> sportIds, String searchStr) {

        // Get list of years from search string searchStr
        // Example: input: "1982 1076" should return a list of "1982" and "1976"
        String digitsString = getAllDigitsString(searchStr);
        List<String> digitsList = convertToDigitsList(digitsString);
        ArrayList<Integer> years = new ArrayList<>(convertToListOfIntegers(digitsList));

        // Find list of non-digit strings in the the string
        // Get all non-digit strings separated by # delimiter (use replaceAll method)
        // Example: "Shaq, 1982, Glen, 1976, Most Inspirational" should return "Shaq#Glen#Most Ispirational"
        String nonDigitString = getAllNonDigitsString(searchStr, delimiter);
        List<String> stringsList = convertToListOfStrings(nonDigitString, delimiter);
        ArrayList<String> titles = new ArrayList<>(stringsList);
        ArrayList<String> players = new ArrayList<>(stringsList);

        List<TrophyAward> list = DataManager.getTrophyRepository(context.get()).getTrophyAwardsBySportsAndTitlesAndYearsAndPlayers(sportIds, titles, years, players);
        return new ArrayList<>(converttoListOfTrophyWithAwards(list));
    }


    public ArrayList<TrophyWithAwards> advancedSearch(SearchParameters searchParameters) {

        // Get list of sport ids from search parameter sportsStr
        List<Long> sportIds = new ArrayList<>(getSportIds(searchParameters));

        // Get list of years from search parameter yearsStr
        // Example: input: "1982 1076" should return a list of "1982" and "1976"
        ArrayList<Integer> years = new ArrayList<>(getYears(searchParameters));

        // Find list of non-digit strings in the titlesStr string
        // Get all non-digit strings separated by # delimiter (use replaceAll method)
        // Example: "Most Inspirational, Most Valuable" should return "Most Inspirational#Most Valuable"
        ArrayList<String> titles = new ArrayList<>(getTitles(searchParameters));

        // Find list of non-digit strings in the playersStr string
        // Get all non-digit strings separated by # delimiter (use replaceAll method)
        // Example: "Glen, Anna Smith" should return "Glen#Anna Smith"
        ArrayList<String> players = new ArrayList<>(getPlayers(searchParameters));

        if(sportIds.size() == 0 && years.size() == 0 && titles.size() == 0 && players.size() == 0) return new ArrayList<>();

        List<TrophyAward> list = DataManager.getTrophyRepository(context.get()).getTrophyAwardsBySportsAndTitlesAndYearsAndPlayers(sportIds, titles, years, players);
        Log.d(TAG, "TrophyAward List Size: " + list.size());
        return new ArrayList<>(converttoListOfTrophyWithAwards(list));
    }
}
