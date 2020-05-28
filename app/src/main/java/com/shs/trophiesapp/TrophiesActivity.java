package com.shs.trophiesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.shs.trophiesapp.adapters.CustomSuggestionsAdapter;
import com.shs.trophiesapp.adapters.SportWithTrophiesAdapter;
import com.shs.trophiesapp.data.Suggestion;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shs.trophiesapp.databinding.TrophiesActivityBinding;
import com.shs.trophiesapp.search.SearchParameters;
import com.shs.trophiesapp.search.SearchSuggestions;
import com.shs.trophiesapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrophiesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        MaterialSearchBar.OnSearchActionListener, MaterialSearchBar.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "TrophiesActivity";
    public static final String TROPHIES_BY_SPORT_NAME = "Sport";

    private TrophiesActivityBinding binding;

    private List<Suggestion> suggestions = new ArrayList<>();
    private CustomSuggestionsAdapter customSuggestionsAdapter;

    private SportWithTrophiesAdapter adapter;
    private SportWithTrophies sportWithTrophies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: TrophiesActivity");
        super.onCreate(savedInstanceState);
        binding = TrophiesActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Receive data
        Intent intent = getIntent();
        String sport = Objects.requireNonNull(intent.getExtras()).getString(TROPHIES_BY_SPORT_NAME);

        // set recyclerview layout manager
        binding.trophiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sportWithTrophies = new SportWithTrophies();
        adapter = new SportWithTrophiesAdapter(this, sportWithTrophies);
        binding.trophiesRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // set adapter for recyclerview
        binding.trophiesRecyclerView.setAdapter(adapter);

        String sport_trophies_text = sport + " Trophies";
        binding.trophyWithAwardsTitle.setText(sport_trophies_text);

        getData(Objects.requireNonNull(sport));

        binding.trophiesSearch.setOnSearchActionListener(this);
        binding.trophiesSearch.setMaxSuggestionCount(3);
        binding.trophiesSearch.setHint(getResources().getString(R.string.search_info));

        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customSuggestionsAdapter = new CustomSuggestionsAdapter(inflater);
        suggestions = SearchSuggestions.getInstance(getApplicationContext(), suggestions).getDefaultSuggestions();
        customSuggestionsAdapter.setSuggestions(suggestions);
        binding.trophiesSearch.setCustomSuggestionAdapter(customSuggestionsAdapter);

        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + binding.trophiesSearch.getText());
        binding.trophiesSearch.setCardViewElevation(10);
        binding.trophiesSearch.addTextChangeListener(new TextWatcher() {
             @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: parameters: charSequence " + charSequence.toString() + ", i=" + i + ", i1=" + i1 + ", i2=" + i2);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: parameters: charSequence " + charSequence.toString() + ", i=" + i + ", i1=" + i1 + ", i2=" + i2);
                Log.d(TAG, "onTextChanged: text changed " + binding.trophiesSearch.getText());
                List<Suggestion> generatedSuggestions = SearchSuggestions.getInstance(getApplicationContext(),
                        suggestions).getSuggestions(binding.trophiesSearch.getText()); //Removed sport addition to getSuggestions
                generatedSuggestions.forEach(e -> Log.d(TAG, "onTextChanged: suggestion=" + e.toString()));
                suggestions = new ArrayList<>(generatedSuggestions);
                customSuggestionsAdapter.setSuggestions(suggestions);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: ");
                suggestions = SearchSuggestions.getInstance(getApplicationContext(), suggestions).getDefaultSuggestions();
            }
        });
        customSuggestionsAdapter.setListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                Suggestion s = (Suggestion) v.getTag();
                binding.trophiesSearch.setText(s.getTitle());
                customSuggestionsAdapter.clearSuggestions();
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {
                // TODO
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {Log.d(TAG, "onSearchStateChanged: ");}

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.d(TAG, "onSearchConfirmed: ");
        Intent intent = new Intent(this, TrophiesWithAwardsActivity.class);
        String searchString = text.toString().trim();

        Intent nextActivity = Utils.searchKeywordRerouting(getApplicationContext(), searchString);
        if(nextActivity != null) startActivity(nextActivity);
        else if (!searchString.isEmpty()) {
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
        if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
            binding.trophiesSearch.closeSearch();
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

    @Override
    public boolean onMenuItemClick(MenuItem item) { return false; }
}
