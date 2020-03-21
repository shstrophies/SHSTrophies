package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.shs.trophiesapp.adapters.SportsAdapter;
import com.shs.trophiesapp.database.AppDatabase;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Sport;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.paukov.combinatorics.CombinatoricsFactory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.paukov.combinatorics.cartesian.CartesianProductGenerator;
import org.paukov.combinatorics.util.ComplexCombinationGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import static org.paukov.combinatorics.CombinatoricsFactory.createCartesianProductGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;


public class SportsActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "SportsActivity";

    private MaterialSearchBar searchBar;
    private List<String> suggestions = new ArrayList<>();
    SportRepository sportRepository;
    TrophyRepository trophyRepository;

    private SportsAdapter adapter;
    private ArrayList<Sport> sports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create sports_activity layout object
        setContentView(R.layout.sports_activity);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.sport_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sports = new ArrayList<>();
        adapter = new SportsAdapter(this, this.sports);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

        // get data and notify adapter
        getData();

        searchBar = findViewById(R.id.sports_search);
        searchBar.setOnSearchActionListener(this);
        searchBar.setMaxSuggestionCount(5);
        searchBar.setHint(getResources().getString(R.string.search_info));
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        getSuggestions();
        searchBar.setLastSuggestions(suggestions);
        sportRepository = DataManager.getSportRepository(getApplicationContext());
        trophyRepository = DataManager.getTrophyRepository(getApplicationContext());


        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + searchBar.getText());
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: text changed " + searchBar.getText());
                // CAROLINA TODO
                List<String> sportStrings = sportRepository.searchSportName("%" + searchBar.getText() + "%", 5);
                List<String> trophyTitles = trophyRepository.searchTrophyTitle("%" + searchBar.getText() + "%", 5);
                List<String> playerStrings = trophyRepository.searchPlayerName("%" + searchBar.getText() + "%", 5);

                ArrayList<String> suggestions = new ArrayList<>();

                ICombinatoricsVector<String> set01 = createVector(sportStrings);
                ICombinatoricsVector<String> set02 = createVector(trophyTitles);
                ICombinatoricsVector<String> set03 = createVector(playerStrings);

                Generator<String> generator = createCartesianProductGenerator(set01, set02, set03);
                for (ICombinatoricsVector<String> cartesianProduct : generator) {
                    String str = cartesianProduct.getVector().stream()
                            .collect(Collectors.joining(", "));
                    suggestions.add(str);
                    Log.d(TAG, "onTextChanged: cartesianProduct=" + cartesianProduct);
                    System.out.println(cartesianProduct);
                }
                searchBar.setLastSuggestions(suggestions);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: ");
                doSearch(searchBar.getText());
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        AboutDialogActivity loadingDialog = new AboutDialogActivity(SportsActivity.this);
        switch (item.getItemId()){
            case R.id.action_about:
                loadingDialog.startAboutDialogActivity();
                return true;
            case R.id.action_report_bug:
                return true;
            default:
                return false;
        }


    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        Log.d(TAG, "onSearchStateChanged: ");
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.d(TAG, "onSearchConfirmed: ");
        //HERE
        String searchString = text.toString();
        if (searchString.isEmpty()) {
            Intent intent = new Intent(this, SportsWithTrophiesActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, TrophiesWithAwardsActivity.class);
            intent.putExtra(TrophiesWithAwardsActivity.AWARDS_SEARCH_STRING, searchString);
            startActivity(intent);
        }
    }

    @Override
    public void onButtonClicked(int buttonCode) {
//        switch (buttonCode) {
//            case MaterialSearchBar.BUTTON_SPEECH:
//                break;
//            case MaterialSearchBar.BUTTON_BACK:
//                searchBar.closeSearch();
//                break;
//        }
    }

    private List<String> getSuggestions() {
        // Sample data
        final String[] suggestions = {
                "Basketball, Most Inspirational, Glenn",
                "Most Inspirational",
                "Football, Most Inspirational",
                "Glenn, 1976",
                "1976",
                "Football, 1976"
        };
        Collections.addAll( this.suggestions, suggestions );

        return this.suggestions;
    }

    private void getData() {
        Log.d(TAG, "getData: getData");
        Context context = this;
        SportRepository SportRepository = DataManager.getSportRepository(context);
        List<Sport> _sports = SportRepository.getSports();
        sports.addAll(_sports);
        Log.d(TAG, "getData: recyclerview sports size=" + sports.size());
        adapter.notifyDataSetChanged();

    }

    // search data
    private void doSearch(String searchText) {
        Log.d(TAG, "doSearch: " + searchBar.getText());
        adapter.getFilter().filter(searchText);
    }

    @Override
    public void onClick(View view) {

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.main);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}



