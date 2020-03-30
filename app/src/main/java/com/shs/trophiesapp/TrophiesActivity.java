package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.shs.trophiesapp.adapters.SportWithTrophiesAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shs.trophiesapp.search.SearchParameters;

import java.util.List;
import java.util.Objects;

public class TrophiesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {
    private static final String TAG = "TrophiesActivity";
    public static final String TROPHIES_BY_SPORT_NAME = "Sport";

    private MaterialSearchBar searchBar;

    private SportWithTrophiesAdapter adapter;
    private SportWithTrophies sportWithTrophies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trophies_activity);



        //Receive data
        Intent intent = getIntent();
        String sport = Objects.requireNonNull(intent.getExtras()).getString(TROPHIES_BY_SPORT_NAME);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.trophies_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sportWithTrophies = new SportWithTrophies();
        adapter = new SportWithTrophiesAdapter(this, sportWithTrophies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

        TextView sport_trophies = findViewById(R.id.trophy_with_awards_title);
        String sport_trophies_text = sport + " Trophies";
        sport_trophies.setText(sport_trophies_text);

        getData(Objects.requireNonNull(sport));

        searchBar = findViewById(R.id.trophies_search);
        searchBar.setOnSearchActionListener(this);
        searchBar.setHint(getResources().getString(R.string.search_info));
        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + searchBar.getText());
        searchBar.setCardViewElevation(1);
        /*searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: text changed " + searchBar.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: editable=" + editable.toString());
            }

        });*/
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.d(TAG, "onSearchConfirmed: ");
        Sport sport = sportWithTrophies.sport;
        Intent intent = new Intent(this, TrophiesWithAwardsActivity.class);
        String searchString = text.toString();
        intent.putExtra(SearchParameters.ALL, searchString);
        intent.putExtra(SearchParameters.TROPHYTITLES, "");
        intent.putExtra(SearchParameters.SPORTNAMES, sport.getName());
        intent.putExtra(SearchParameters.YEARS, "");
        intent.putExtra(SearchParameters.PLAYERNAMES, "");
        startActivity(intent);

    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_SPEECH:
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.closeSearch();
                break;
        }
    }


    private void getData(String sport) {
        Log.d(TAG, "getData: getData");
        Context context = this;
        List<SportWithTrophies> list = DataManager.getTrophyRepository(context).getSportWithTrophiesBySportName(sport.toLowerCase());
        sportWithTrophies.sport = list.get(0).sport;
        sportWithTrophies.trophies = list.get(0).trophies;

        Log.d(TAG, "getData: recyclerview trophies size=" + sportWithTrophies.trophies.size());
        adapter.notifyDataSetChanged();

    }
}
