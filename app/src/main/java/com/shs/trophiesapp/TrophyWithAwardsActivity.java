package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shs.trophiesapp.adapters.CustomSuggestionsAdapter;
import com.shs.trophiesapp.adapters.TrophyWithAwardsAdapter;
import com.shs.trophiesapp.data.Suggestion;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.search.SearchParameters;
import com.shs.trophiesapp.search.SearchSuggestions;
import com.shs.trophiesapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TrophyWithAwardsActivity extends BaseActivity implements MaterialSearchBar.OnSearchActionListener {
    private static final String TAG = "TrophyPlayersAndYearsAc";


    private MaterialSearchBar searchBar;
    private List<Suggestion> suggestions = new ArrayList<>();
    private CustomSuggestionsAdapter customSuggestionsAdapter;

    private TrophyWithAwardsAdapter adapter;
    private ArrayList<TrophyAward> awards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trophy_with_awards_activity);

        TextView tvSportTitle = findViewById(R.id.trophy_with_awards_title);
        TextView tvTitle = findViewById(R.id.trophy_with_awards_title);
        ImageView img = findViewById(R.id.trophy_with_awards_thumbnail);
        //searchHeader = findViewById(R.id.HeaderWithSearchResults);

        //Receive data
        Intent intent = getIntent();
        String sport = intent.getExtras().getString("sportName");
        String title = intent.getExtras().getString("title");
        String url = intent.getExtras().getString("url");
        int color = intent.getExtras().getInt("color");


        String tvSportTitleText = sport + " Trophy Award(s)";
        tvSportTitle.setText(tvSportTitleText);
        tvTitle.setText(title);
        //searchHeader.setText( "{Number} results for" + searchBar.getText());
        Utils.imageFromCache(img, url);
        //trophyView.setBackgroundColor(color);

        // set recyclerview layout manager
        RecyclerView recyclerView = findViewById(R.id.trophy_with_awards_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        awards = new ArrayList<>();
        adapter = new TrophyWithAwardsAdapter(this, awards, color);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);
        getData(intent);

        searchBar = findViewById(R.id.trophies_search);
        searchBar.setOnSearchActionListener(this);
        searchBar.setMaxSuggestionCount(3);
//        searchBar.inflateMenu(R.menu.main);
        searchBar.setHint(getResources().getString(R.string.search_info));

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        customSuggestionsAdapter = new CustomSuggestionsAdapter(inflater);
        suggestions = SearchSuggestions.getInstance(getApplicationContext(), suggestions).getDefaultSuggestions();
        customSuggestionsAdapter.setSuggestions(suggestions);
        searchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);

        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + searchBar.getText());
        searchBar.setCardViewElevation(1);
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
//                Log.d(TAG, "afterTextChanged: editable=" + editable.toString());
//                doSearch(searchBar.getText());
            }
        });

        img.setOnClickListener(view -> {

            long trophyId1 = intent.getExtras().getLong("trophyId");

            Intent intent1 = new Intent(getApplicationContext(), TrophyDetailsActivity.class);

            // passing data
            intent1.putExtra("trophyId", trophyId1);
            intent1.putExtra("color", color);

            // start activity
            startActivity(intent1);
        });

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.d(TAG, "onSearchConfirmed: ");
        String searchString = text.toString();
        if (searchString.isEmpty()) {
            Intent intent = new Intent(this, SportsWithTrophiesActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, TrophiesWithAwardsActivity.class);
            intent.putExtra(SearchParameters.ALL, searchString);
            intent.putExtra(SearchParameters.TROPHYTITLES, "");
            intent.putExtra(SearchParameters.SPORTNAMES, "");
            intent.putExtra(SearchParameters.YEARS, "");
            intent.putExtra(SearchParameters.PLAYERNAMES, "");
            startActivity(intent);
        }
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
        List<TrophyWithAwards> trophyWithAwards = trophyRepository.getTrophyWithAwardsByTrophyId(trophyId);
        List<TrophyAward> _awards = trophyWithAwards.get(0).awards;
        awards.addAll(_awards);
        Log.d(TAG, "getData: recyclerview awards size=" + awards.size());
        adapter.notifyDataSetChanged();

    }


    // CAROLINA TODO:
    // search data
    private void doSearch(String searchText) {
        Log.d(TAG, "doSearch: " + searchBar.getText());
        adapter.getFilter().filter(searchText);

    }


}
