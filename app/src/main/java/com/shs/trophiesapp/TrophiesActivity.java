package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.shs.trophiesapp.R;
import com.shs.trophiesapp.adapters.TrophyAdapter;
import com.shs.trophiesapp.data.DataManager;
import com.shs.trophiesapp.data.TrophyRepository;
import com.shs.trophiesapp.data.entities.Trophy;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class TrophiesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {
    private static final String TAG = "TrophiesActivity";
    public static final String TROPHIES_BY_SPORT_NAME = "Sport";

    private MaterialSearchBar searchBar;

    private TrophyAdapter adapter;
    private ArrayList<Trophy> trophies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        // create trophies_activity layout object
        setContentView(R.layout.trophies_activity);

        //Receive data
        Intent intent = getIntent();
        String sport = intent.getExtras().getString(TROPHIES_BY_SPORT_NAME);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.trophies_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trophies = new ArrayList<>();
        adapter = new TrophyAdapter(this, trophies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

        TextView sport_trophies = findViewById(R.id.trophies_sport_title);
        sport_trophies.setText(sport);

        getData(sport);

        searchBar = findViewById(R.id.trophies_search);
        searchBar.setOnSearchActionListener(this);
        searchBar.inflateMenu(R.menu.main);
        searchBar.setHint("Search for trophy, player, year...");
        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + searchBar.getText());
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: text changed " + searchBar.getText());
                doSearch(searchBar.getText());

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d(TAG, "afterTextChanged: editable=" + editable.toString());
                doSearch(searchBar.getText());

            }

        });

//        final FloatingActionButton searchButton = findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchBar.openSearch();
//            }
//        });
    }

//    @Override
//    public void onBackPressed() {
//        Log.d(TAG, "onBackPressed: ");
//    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

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
        TrophyRepository trophyRepository = DataManager.getTrophyRepository(context);
        List<Trophy> _trophies = trophyRepository.getTrophiesBySport(sport.toLowerCase());
        trophies.addAll(_trophies);
        Log.d(TAG, "getData: recyclerview trophies size=" + trophies.size());
        adapter.notifyDataSetChanged();

    }


    // search data
    private void doSearch(String searchText) {
        Log.d(TAG, "doSearch: " + searchBar.getText());

        adapter.getFilter().filter(searchText);

    }
}
