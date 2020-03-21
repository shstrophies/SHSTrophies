package com.shs.trophiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.shs.trophiesapp.adapters.TrophyWithAwardsAdapter;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.TrophyRepository;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrophyWithAwardsActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TrophyPlayersAndYearsAc";

    @BindView(R.id.trophy_swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.trophy_with_awards_recycleview) RecyclerView mRecyclerView;
    @BindView(R.id.trophies_search) MaterialSearchBar searchBar;

    private TrophyWithAwardsAdapter adapter;
    private ArrayList<TrophyAward> awards;
    private GridLayoutManager gridLayoutManager;

    private int currentPage = TrophyWithAwardsAdapter.TrophyPaginationListener.PAGE_START;
    private int totalPages = 0;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trophy_with_awards_activity);
        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        awards = new ArrayList<>();
        adapter = new TrophyWithAwardsAdapter(this, awards, color);
        gridLayoutManager = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // set adapter for recyclerview
        mRecyclerView.setAdapter(adapter);
        setTotalPages(intent);
        getData(intent);

        searchBar.setOnSearchActionListener(this);
//        searchBar.inflateMenu(R.menu.main);
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
                doSearch(searchBar.getText());

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d(TAG, "afterTextChanged: editable=" + editable.toString());
                doSearch(searchBar.getText());

            }

        });*/

        img.setOnClickListener(view -> {
            long trophyId1 = intent.getExtras().getLong("trophyId");

            Intent intent1 = new Intent(getApplicationContext(), TrophyDetailsActivity.class);

            // passing data
            intent1.putExtra("trophyId", trophyId1);
            intent1.putExtra("color", color);

            // start activity
            startActivity(intent1);
        });

        mRecyclerView.addOnScrollListener(new TrophyWithAwardsAdapter.TrophyPaginationListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                getData(intent);
                Log.d(TAG, "Awards Size: " + awards.size());
            }

            @Override
            public boolean isLastPage() { return isLastPage; }

            @Override
            public boolean isLoading() { return isLoading; }
        });

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {}

    @Override
    public void onSearchConfirmed(CharSequence text) {}

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
        new Handler().postDelayed(() -> {
            Log.d(TAG, "getData: getData");
            long trophyId = intent.getExtras().getLong("trophyId");

            TrophyRepository trophyRepository = DataManager.getTrophyRepository(this);
            List<TrophyAward> _awards = trophyRepository.getTrophyWithAwardsByTrophyIdLimited(trophyId, 29, currentPage);
            Log.d(TAG, "_awards size: " + _awards.size() + ", currentPage: " + currentPage);
            itemCount += _awards.size();

            if(currentPage != TrophyWithAwardsAdapter.TrophyPaginationListener.PAGE_START) adapter.removeLoading();
            awards.addAll(_awards);

            swipeRefreshLayout.setRefreshing(false);

            if(currentPage < totalPages) _awards.forEach(trophyAward -> adapter.addLoading(trophyAward));
            else if(currentPage == totalPages) {_awards.forEach(trophyAward -> adapter.addLoading(trophyAward)); isLastPage = true;}
            else isLastPage = true;

            Log.d(TAG, "getData: recyclerview awards size=" + awards.size());
            isLoading = false;
        }, 1000);
    }

    private void setTotalPages(Intent intent) {
        Log.d(TAG, "setTotalPages: setTotalPages");
        long trophyId = intent.getExtras().getLong("trophyId");

        TrophyRepository trophyRepository = DataManager.getTrophyRepository(this);
        List<TrophyWithAwards> trophyWithAwards = trophyRepository.getTrophyWithAwardsByTrophyId(trophyId);
        List<TrophyAward> _awards = trophyWithAwards.get(0).awards;
        Log.d(TAG, "Total Awards Size: " + _awards.size());
        int totalPage = 29;
        double pageNum = (double) _awards.size() / (double) totalPage;
        totalPages = (int) Math.ceil(pageNum);
    }

    @Override
    public void onRefresh() {
        itemCount = gridLayoutManager.getChildCount();
        currentPage = TrophyWithAwardsAdapter.TrophyPaginationListener.PAGE_START;
        isLastPage = false;
        adapter.clear();
        getData(getIntent());
    }
}

