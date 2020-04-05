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

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.shs.trophiesapp.adapters.CustomSuggestionsAdapter;
import com.shs.trophiesapp.adapters.SportsAdapter;
import com.shs.trophiesapp.data.Suggestion;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.SportRepository;
import com.shs.trophiesapp.database.entities.Sport;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shs.trophiesapp.databinding.SportsActivityBinding;
import com.shs.trophiesapp.search.SearchParameters;
import com.shs.trophiesapp.search.SearchSuggestions;

import java.util.ArrayList;
import java.util.List;

public class SportsActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener, MaterialSearchBar.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "SportsActivity";

    private SportsActivityBinding binding;

    private List<Suggestion> suggestions = new ArrayList<>();
    private CustomSuggestionsAdapter customSuggestionsAdapter;

    private SportsAdapter adapter;
    private ArrayList<Sport> sports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SportsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBar.appBar);

        sports = new ArrayList<>();
        adapter = new SportsAdapter(this, this.sports);
        binding.sportRecycleview.setLayoutManager(new GridLayoutManager(this, 3));

        binding.sportRecycleview.setAdapter(adapter);

        getData();

        binding.sportsSearch.setOnSearchActionListener(this);
        binding.sportsSearch.setMaxSuggestionCount(3);
        binding.sportsSearch.setHint(getResources().getString(R.string.search_info));


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        customSuggestionsAdapter = new CustomSuggestionsAdapter(inflater);
        suggestions = SearchSuggestions.getInstance(getApplicationContext(), suggestions).getDefaultSuggestions();
        customSuggestionsAdapter.setSuggestions(suggestions);
        binding.sportsSearch.setCustomSuggestionAdapter(customSuggestionsAdapter);

        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + binding.sportsSearch.getText());
        binding.sportsSearch.setCardViewElevation(10);
        binding.sportsSearch.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: parameters: charSequence " + charSequence.toString() + ", i=" + i + ", i1=" + i1 + ", i2=" + i2);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: parameters: charSequence " + charSequence.toString() + ", i=" + i + ", i1=" + i1 + ", i2=" + i2);

                Log.d(TAG, "onTextChanged: text changed " + binding.sportsSearch.getText());
                List<Suggestion> generatedSuggestions = SearchSuggestions.getInstance(getApplicationContext(), suggestions).getSuggestions(binding.sportsSearch.getText());
                generatedSuggestions.forEach(e -> Log.d(TAG, "onTextChanged: suggestion=" + e.toString()));
                suggestions.clear();
                suggestions.addAll(generatedSuggestions);
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
                binding.sportsSearch.setText(s.getTitle());
                customSuggestionsAdapter.clearSuggestions();
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {
                // TODO
            }
        });
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        Log.d(TAG, "onSearchStateChanged: ");
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
                binding.sportsSearch.closeSearch();
                break;
        }
    }


    private void getData() {
        Log.d(TAG, "getData: getData");
        Context context = this;
        SportRepository sportRepository = DataManager.getSportRepository(context);
        List<Sport> _sports = sportRepository.getSports();
        sports.addAll(_sports);
        Log.d(TAG, "getData: recyclerview sports size=" + sports.size());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    /*public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.main);
        popup.show();
    }*/

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


}



