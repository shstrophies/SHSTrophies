package com.shs.trophiesapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.SportsAndTrophiesData;
import com.shs.trophiesapp.data.entities.Trophy;

import java.util.ArrayList;
import java.util.List;


public class SportsAndTrophiesAdapter extends RecyclerView.Adapter<SportsAndTrophiesAdapter.HomeViewHolder> implements Filterable {

    private static final String TAG = "SportsAndTrophiesAdapte";

    private Context context;
    private List<SportsAndTrophiesData> data;
    private List<SportsAndTrophiesData> dataFiltered;

    private SportsAndTrophiesHorizontalAdapter horizontalAdapter;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public SportsAndTrophiesAdapter(Context context, List<SportsAndTrophiesData> data) {
        this.context = context;
        this.data = data;
        this.dataFiltered = data;

        recycledViewPool = new RecyclerView.RecycledViewPool();

    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View theView = LayoutInflater.from(context).inflate(R.layout.sports_and_trophies_row_layout, parent, false);


        return new HomeViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, final int position) {

        holder.textViewSport.setText(data.get(position).getSport());

        horizontalAdapter = new SportsAndTrophiesHorizontalAdapter(context, data.get(position).getTrophies());
        holder.recyclerViewHorizontal.setAdapter(horizontalAdapter);

        holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);


    }


    @Override
    public int getItemCount() {
        return data.size();

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewSport;

        private LinearLayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        public HomeViewHolder(View itemView) {
            super(itemView);

            recyclerViewHorizontal = itemView.findViewById(R.id.home_recycler_view_horizontal);
            recyclerViewHorizontal.setHasFixedSize(true);
            recyclerViewHorizontal.setNestedScrollingEnabled(false);
            recyclerViewHorizontal.setLayoutManager(horizontalManager);
            recyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());

            textViewSport = itemView.findViewById(R.id.tv_sport);


        }
    }

    private class SportsAndTrophiesDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: constraint=" + constraint);
            String charString = constraint.toString().toLowerCase();
            if (charString.isEmpty()) {
                dataFiltered = data;
            } else {
                Log.d(TAG, "performFiltering: charString=" + charString);
                List<SportsAndTrophiesData> filteredList = new ArrayList<>();
                for (SportsAndTrophiesData row : data) {
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
            dataFiltered = (ArrayList<SportsAndTrophiesData>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new SportsAndTrophiesDataFilter();
    }
}