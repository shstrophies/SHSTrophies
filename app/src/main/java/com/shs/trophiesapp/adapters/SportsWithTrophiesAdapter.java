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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.database.relations.SportWithTrophies;

import java.util.ArrayList;
import java.util.List;


public class  SportsWithTrophiesAdapter extends RecyclerView.Adapter<SportsWithTrophiesAdapter.HomeViewHolder> implements Filterable {

    private static final String TAG = "SportsAndTrophiesAdapte";

    private Context context;
    private List<SportWithTrophies> data;
    private List<SportWithTrophies> dataFiltered;

    private RecyclerView.RecycledViewPool recycledViewPool;

    public SportsWithTrophiesAdapter(Context context, List<SportWithTrophies> data) {
        this.context = context;
        this.data = data;
        this.dataFiltered = data;

        recycledViewPool = new RecyclerView.RecycledViewPool();

    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View theView = LayoutInflater.from(context).inflate(R.layout.sports_with_trophies_row_layout_horizontal, parent, false);


        return new HomeViewHolder(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, final int position) {

        holder.textViewSport.setText(data.get(position).sport.name);

        SportWithTrophiesAdapter horizontalAdapter = new SportWithTrophiesAdapter(context, data.get(position));
        holder.recyclerViewHorizontal.setAdapter(horizontalAdapter);
        holder.recyclerViewHorizontal.setLayoutManager(new GridLayoutManager(context, 5));

        holder.recyclerViewHorizontal.setRecycledViewPool(recycledViewPool);


    }


    @Override
    public int getItemCount() {
        return data.size();

    }


    class HomeViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHorizontal;
        private TextView textViewSport;

        HomeViewHolder(View itemView) {
            super(itemView);

            // home_recycler_view_horizontal
            recyclerViewHorizontal = itemView.findViewById(R.id.home_recycler_view_horizontal);
            recyclerViewHorizontal.setHasFixedSize(true);
            recyclerViewHorizontal.setNestedScrollingEnabled(false);
            LinearLayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewHorizontal.setLayoutManager(horizontalManager);
            recyclerViewHorizontal.setItemAnimator(new DefaultItemAnimator());

            textViewSport = itemView.findViewById(R.id.tv_sport);


        }
    }

    private class SportsWithTrophiesDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: constraint=" + constraint);
            String charString = constraint.toString().toLowerCase().trim();
            if (charString.isEmpty()) {
                dataFiltered = data;
            } else {
                Log.d(TAG, "performFiltering: charString=" + charString);
                List<SportWithTrophies> filteredList = new ArrayList<>();
                /*for (SportWithTrophies row : data) {
                    // name match condition. this might differ depending on your requirement
                }*/ //TODO: Filter the Lists Here
                dataFiltered = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = dataFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataFiltered = (ArrayList<SportWithTrophies>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new SportsWithTrophiesDataFilter();
    }
}