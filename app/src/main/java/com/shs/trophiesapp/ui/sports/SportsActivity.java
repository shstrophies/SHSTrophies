package com.shs.trophiesapp.ui.sports;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.DataManager;
import com.shs.trophiesapp.data.SportRepository;
import com.shs.trophiesapp.data.entities.Sport;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class SportsActivity extends AppCompatActivity {
    private static final String TAG = "SportsActivity";

    private MaterialSearchBar searchBar;

    private RecyclerView recyclerView;
    private SportAdapter adapter;
    private ArrayList<Sport> sports;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private final boolean fromExternalSource = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create sports_activity layout object
        setContentView(R.layout.sports_activity);

        // set recyclerview layout manager
        recyclerView = (RecyclerView) findViewById(R.id.sport_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sports = new ArrayList<>();
        adapter = new SportAdapter(this, sports);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // set adapter for recyclerview
        recyclerView.setAdapter(adapter);

            // create data and notify adapter
//            getSportData();
            getData();
    }

    // - create data and notify data
    private void getSportData() {
        Sport Sport;
//        Sport = new Sport("Baseball - Arman Rafati - 2018", "This was an epic year for MVP player Arman Rafati.  He conquered the field with his quick legs.  Congratulations for a game well played by Arman and his mates ... ");
//        sports.add(Sport);
//        Sport = new Sport("Tennis - Shay Sarn - 2019", "Fabulous game! Shay is a lefty who has strengthened his game throughout the year. He has achieved a level of mastery and perfection.  Great job!");
//        sports.add(Sport);
//        Sport = new Sport("Soccer - Johnny Bee - 2018", "Another fabulous game with Johnny Bee. Johnny has legs of steel!  Go Mr. Bee!");
//        sports.add(Sport);
//        Sport = new Sport("Swimming - Anthony Lao - 2017", "Don't let Anthony fool you because he is a sophomore.  He has strong arms and legs and he will not be defated. Good job Mr. Lao!");
//        sports.add(Sport);
//        adapter.notifyDataSetChanged();

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
}
