package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.shs.trophiesapp.adapters.CustomSuggestionsAdapter;
import com.shs.trophiesapp.adapters.SportsAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.entities.Sport;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SportsActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {
    private static final String TAG = "SportsActivity";

    private MaterialSearchBar searchBar;
    private List<String> suggestions = new ArrayList<>();
    private CustomSuggestionsAdapter customSuggestionsAdapter;


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
        sports = new ArrayList<Sport>();
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
        customSuggestionsAdapter = new CustomSuggestionsAdapter(inflater);
        getSuggestions();
        customSuggestionsAdapter.setSuggestions(suggestions);
        searchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);


        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + searchBar.getText());
//        searchBar.setCardViewElevation(1);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: text changed " + searchBar.getText());
                // send the entered text to our filter and let it manage everything
                customSuggestionsAdapter.getFilter().filter(searchBar.getText());
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
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.action1) {
//            // Handle the action1 action
//        } else if (id == R.id.action2) {
//
//        }

        return true;
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
                "Basketball, Glenn, Most Inspirational",
                "Most Inspirational",
                "1976",
                "Football, 1976"
        };
//        this.suggestions = Arrays.asList(suggestions);
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
        customSuggestionsAdapter.addSuggestion("Hi");
    }

}
