package com.shs.trophiesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.TrophyDetailsActivity;
import com.shs.trophiesapp.database.entities.TrophyAward;

import java.util.ArrayList;
import java.util.List;


public class TrophyPlayersAndYearsAdapter extends RecyclerView.Adapter<TrophyPlayerAndYearViewHolder> implements Filterable {
    private static final String TAG = "TrophiesAdapter";
    private Context context;
    private List<TrophyAward> trophies;
    private List<TrophyAward> trophiesFiltered;




    public TrophyPlayersAndYearsAdapter(Context context, List<TrophyAward> trophies) {
        this.context = context;
        this.trophies = trophies;
        this.trophiesFiltered = trophies;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: trophiesFiltered.size()=" + trophiesFiltered.size());
        return trophiesFiltered.size();
    }

    @Override
    @NonNull
    public TrophyPlayerAndYearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_trophy_player_and_year, parent, false);
        return new TrophyPlayerAndYearViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrophyPlayerAndYearViewHolder holder, int position) {
        TrophyAward trophyAward = trophiesFiltered.get(position);
        holder.setDetails(trophyAward);


        // set click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TrophyDetailsActivity.class);

                // passing data
                intent.putExtra("trophyId", trophiesFiltered.get(position).getTrophyId());
                intent.putExtra("year", trophiesFiltered.get(position).getYear());
                intent.putExtra("player", trophiesFiltered.get(position).getPlayer());
                intent.putExtra("category", trophiesFiltered.get(position).getCategory());
                intent.putExtra("color", trophiesFiltered.get(position).getColor());

                // start activity
                context.startActivity(intent);
            }
        });
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
                List<TrophyAward> filteredList = new ArrayList<>();
//                for (TrophyAward row : trophies) {
//                    // name match condition. this might differ depending on your requirement
//                    // here we are looking for title or description match
//                    if (row.title.toLowerCase().contains(charString) || row.player.toLowerCase().contains(charString)) {
//                        filteredList.add(row);
//                    }
//                }
                trophiesFiltered = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = trophiesFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            trophiesFiltered = (ArrayList<TrophyAward>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new TrophyFilter();
    }

}
