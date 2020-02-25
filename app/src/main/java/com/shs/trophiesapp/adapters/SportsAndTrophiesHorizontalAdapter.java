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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.entities.Trophy;

import java.util.ArrayList;
import java.util.List;


public class SportsAndTrophiesHorizontalAdapter extends RecyclerView.Adapter<TrophyViewHolder> implements Filterable {

    private static final String TAG = "SportsAndTrophiesHorizontalAdapter";
    private Context context;
    private List<Trophy> trophies;
    private List<Trophy> trophiesFiltered;
    CardView cardView;

    SportsAndTrophiesHorizontalAdapter(Context context, List<Trophy> trophies) {
        this.context = context;
        this.trophies = trophies;
        this.trophiesFiltered = trophies;

    }


    @Override
    public void onBindViewHolder(TrophyViewHolder holder, int position) {
        Trophy trophy = trophiesFiltered.get(position);
        holder.setDetails(trophy);
    }

    @Override
    public int getItemCount() {
        return trophiesFiltered.size();
    }

    @Override
    @NonNull
    public TrophyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_trophy, parent, false);
        return new TrophyViewHolder(view);
    }

    private class TrophyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: constraint=" + constraint);
            String charString = constraint.toString().toLowerCase();
            if (charString.isEmpty()) {
                trophiesFiltered = trophies;
            } else {
                Log.d(TAG, "performFiltering: charString=" + charString);
                List<Trophy> filteredList = new ArrayList<>();
                for (Trophy row : trophies) {
                    // name match condition. this might differ depending on your requirement
                    // here we are looking for title or description match
                    if (row.tr_title.toLowerCase().contains(charString) || row.player.toLowerCase().contains(charString)) {
                        filteredList.add(row);
                    }
                }
                trophiesFiltered = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = trophiesFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            trophiesFiltered = (ArrayList<Trophy>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new SportsAndTrophiesHorizontalAdapter.TrophyFilter();
    }

}