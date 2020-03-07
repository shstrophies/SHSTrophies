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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shs.trophiesapp.adapters.TrophyPlayersAndYearsAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
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

    private ArrayList<TrophyAward> awards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trophy_players_and_years_activity);

        tvTitle = findViewById(R.id.trophy_players_and_years_title);
        img = findViewById(R.id.trophy_players_and_years_thumbnail);
        trophyView = findViewById(R.id.trophy_players_and_years_trophy);

        //Receive data
        Intent intent = getIntent();
        long trophyId = intent.getExtras().getLong("trophyId");
        String title = intent.getExtras().getString("title");
        String url = intent.getExtras().getString("url");
        int color = intent.getExtras().getInt("color");




        tvTitle.setText(title);
        Utils.imageFromUrl(img, url);
        trophyView.setBackgroundColor(color);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.trophy_players_and_years_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        awards = new ArrayList<>();
        adapter = new TrophyPlayersAndYearsAdapter(this, awards);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);
        getData(intent);

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


    private void getData(Intent intent) {
        Log.d(TAG, "getData: getData");

        //Receive data
        long trophyId = intent.getExtras().getLong("trophyId");

        Context context = this;
        TrophyRepository trophyRepository = DataManager.getTrophyRepository(context);
        List<TrophyWithAwards> trophyWithAwards = trophyRepository.getAwardsByTrophyId(trophyId);
        List<TrophyAward> _awards = trophyWithAwards.get(0).awards;
        awards.addAll(_awards);
        Log.d(TAG, "getData: recyclerview awards size=" + awards.size());
        adapter.notifyDataSetChanged();

    }


    // search data
    private void doSearch(String searchText) {
        Log.d(TAG, "doSearch: " + searchBar.getText());
        adapter.getFilter().filter(searchText);

    }


}

