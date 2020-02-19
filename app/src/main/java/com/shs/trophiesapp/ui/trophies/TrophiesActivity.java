package com.shs.trophiesapp.ui.trophies;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.DataManager;
import com.shs.trophiesapp.data.TrophyRepository;
import com.shs.trophiesapp.data.entities.Trophy;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class TrophiesActivity extends AppCompatActivity {
    private static final String TAG = "TrophiesActivity";

    private MaterialSearchBar searchBar;

    private RecyclerView recyclerView;
    private TrophyAdapter adapter;
    private ArrayList<Trophy> trophies;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private final boolean fromExternalSource = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        // create trophies_activity layout object
        setContentView(R.layout.trophies_activity);

        //Receive data
        Intent intent = getIntent();
        String sport = intent.getExtras().getString("Sport");

        // set recyclerview layout manager
        recyclerView = (RecyclerView) findViewById(R.id.trophies_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trophies = new ArrayList<>();
        adapter = new TrophyAdapter(this, trophies);

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

        // Request for permission to read external storage
        if (fromExternalSource && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            // get data and notify adapter
//            getTrophyData();
            getData();


        }


        searchBar = findViewById(R.id.searchBar);

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: " + searchBar.getText());
                doSearch(searchBar.getText());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: " + searchBar.getText());
                doSearch(searchBar.getText());

            }

        });

        searchBar.enableSearch();

    }

    // - create data and notify data
    private void getTrophyData() {
        Log.d(TAG, "getTrophyData: getTrophyData");
        Trophy trophy;
        trophy = new Trophy(
                "Basketball",
                1983,
                "CCS Champ",
                "https://drive.google.com/file/d/1vD-ZeOkH0zRdT0V08p9Eh7f8O7QBl6Bh/view",
                "Glenn Mcfarlane",
                "JV");
        trophies.add(trophy);
        trophy = new Trophy(
                "Baseball",
                1982,
                "Champion for Champion",
                "https://drive.google.com/file/d/1vD-ZeOkH0zRdT0V08p9Eh7f8O7QBl6Bh/view?usp=sharing",
                "Dante Mackenzie",
                "JV");
        trophies.add(trophy);

        adapter.notifyDataSetChanged();


    }


    private void getData() {
        Log.d(TAG, "getData: getData");
        Context context = this;
        TrophyRepository trophyRepository = DataManager.getTrophyRepository(context);
        List<Trophy> _trophies = trophyRepository.getTrophies();
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
