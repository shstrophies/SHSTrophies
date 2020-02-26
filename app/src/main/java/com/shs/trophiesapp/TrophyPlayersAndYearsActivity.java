package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shs.trophiesapp.adapters.TrophiesAdapter;
import com.shs.trophiesapp.adapters.TrophyPlayersAndYearsAdapter;
import com.shs.trophiesapp.data.DataManager;
import com.shs.trophiesapp.data.TrophyRepository;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TrophyPlayersAndYearsActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener  {
    private static final String TAG = "TrophyPlayersAndYearsAc";

    private TextView tvTitle;
    private ImageView img;
    private View trophyView;

    private MaterialSearchBar searchBar;

    private TrophyPlayersAndYearsAdapter adapter;
    private ArrayList<Trophy> trophies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trophy_players_and_years_activity);

        tvTitle = findViewById(R.id.trophy_players_and_years_title);
        img = findViewById(R.id.trophy_players_and_years_thumbnail);
        trophyView = findViewById(R.id.trophy_players_and_years_trophy);

        //Receive data
        Intent intent = getIntent();
        String sport_name = intent.getExtras().getString("sport_name");
        String tr_title = intent.getExtras().getString("tr_title");
        String tr_image_url = intent.getExtras().getString("tr_image_url");
        int color = intent.getExtras().getInt("color");

        tvTitle.setText(tr_title);
        Utils.imageFromUrl(img, tr_image_url);
        trophyView.setBackgroundColor(color);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.trophy_players_and_years_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trophies = new ArrayList<>();
        adapter = new TrophyPlayersAndYearsAdapter(this, trophies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);
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

