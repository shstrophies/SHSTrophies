package com.shs.trophiesapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.database.DataManager;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;



public class TrophiesWithAwardsAdapter extends RecyclerView.Adapter<TrophiesWithAwardsAdapter.HomeViewHolder> implements Filterable {

    private static final String TAG = "TrophiesWithAwardsAdapter";

    private Context context;
    private List<TrophyWithAwards> data;
    private List<TrophyWithAwards> dataFiltered;

    private TrophyWithAwardsAdapter horizontalAdapter;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public TrophiesWithAwardsAdapter(Context context, List<TrophyWithAwards> data) {
        this.context = context;
        this.data = data;
        this.dataFiltered = data;

        recycledViewPool = new RecyclerView.RecycledViewPool();

    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View theView = LayoutInflater.from(context).inflate(R.layout.trophies_with_awards_row_layout_horizontal, parent, false);


        return new HomeViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, final int position) {

        Sport sport = DataManager.getSportRepository(context).getSportById(data.get(position).trophy.getSportId());
        Trophy trophy = data.get(position).trophy;
        holder.textViewSport.setText(trophy.getTitle() + " - " + sport.getName() + " - " + " Trophy Award(s)");
        holder.tvTitle.setText(trophy.getTitle());
        Utils.imageFromUrl(holder.img, trophy.getUrl());
        holder.trophyView.setBackgroundColor(trophy.getColor());

        horizontalAdapter = new TrophyWithAwardsAdapter(context, data.get(position).awards, data.get(position).trophy.getColor());
        holder.recyclerViewHorizontal.setAdapter(horizontalAdapter);
        holder.recyclerViewHorizontal.setLayoutManager(new GridLayoutManager(context, 5));

        holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);


    }


    @Override
    public int getItemCount() {
        return data.size();

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewSport;
        private TextView tvTitle;
        private ImageView img;
        private View trophyView;

        private LinearLayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        public HomeViewHolder(View itemView) {
            super(itemView);

            recyclerViewHorizontal = itemView.findViewById(R.id.trophies_with_awards_recycler_view_horizontal);
            recyclerViewHorizontal.setHasFixedSize(true);
            recyclerViewHorizontal.setNestedScrollingEnabled(false);
            recyclerViewHorizontal.setLayoutManager(horizontalManager);
            recyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());

            textViewSport = itemView.findViewById(R.id.tv_sport_and_trophy);
            tvTitle = itemView.findViewById(R.id.trophies_with_awards_title);
            img = itemView.findViewById(R.id.trophies_with_awards_thumbnail);
            trophyView = itemView.findViewById(R.id.trophies_with_awards_trophy);

        }
    }

    private class SportsWithTrophiesDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: constraint=" + constraint);
            String charString = constraint.toString().toLowerCase();
            if (charString.isEmpty()) {
                dataFiltered = data;
            } else {
                Log.d(TAG, "performFiltering: charString=" + charString);
                List<TrophyWithAwards> filteredList = new ArrayList<>();
                for (TrophyWithAwards row : data) {
                    // name match condition. this might differ depending on your requirement

                }
                dataFiltered = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = dataFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataFiltered = (ArrayList<TrophyWithAwards>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new SportsWithTrophiesDataFilter();
    }
}