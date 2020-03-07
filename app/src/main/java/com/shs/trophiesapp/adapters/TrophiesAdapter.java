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
import com.shs.trophiesapp.TrophyPlayersAndYearsActivity;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.entities.Trophy;

public class TrophiesAdapter extends RecyclerView.Adapter<TrophyViewHolder> implements Filterable {
    private static final String TAG = "TrophiesAdapter";
    private Context context;
    private SportWithTrophies sportWithTrophies;
    private SportWithTrophies sportWithTrophiesFiltered;




    public TrophiesAdapter(Context context, SportWithTrophies sportWithTrophies) {
        this.context = context;
        this.sportWithTrophies = sportWithTrophies;
        this.sportWithTrophiesFiltered = sportWithTrophies;

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: sportWithTrophiesFiltered.size()=" + sportWithTrophiesFiltered.trophies.size());
        return sportWithTrophiesFiltered.trophies.size();
    }

    @Override
    @NonNull
    public TrophyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_trophy, parent, false);
        return new TrophyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrophyViewHolder holder, int position) {
        Trophy trophy = sportWithTrophiesFiltered.trophies.get(position);
        holder.setDetails(trophy);


        // set click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TrophyPlayersAndYearsActivity.class);

                // passing data
                intent.putExtra("sportId", sportWithTrophiesFiltered.sport.getId());
                intent.putExtra("sportName", sportWithTrophiesFiltered.sport.getName());
                intent.putExtra("trophyId", sportWithTrophiesFiltered.trophies.get(position).getId());
                intent.putExtra("title", sportWithTrophiesFiltered.trophies.get(position).getTitle());
                intent.putExtra("url", sportWithTrophiesFiltered.trophies.get(position).getUrl());
                intent.putExtra("color", sportWithTrophiesFiltered.trophies.get(position).getColor());

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
                sportWithTrophiesFiltered = sportWithTrophies;
            }
//            else {
//                Log.d(TAG, "performFiltering: charString=" + charString);
//                List<Trophy> filteredList = new ArrayList<>();
//                for (Trophy row : trophies) {
//                    // name match condition. this might differ depending on your requirement
//                    // here we are looking for title or description match
//                    // TODO: search for trophies by
//                    if (row.getTitle().toLowerCase().contains(charString)
////                            || row.player.toLowerCase().contains(charString)
//                    ) {
//                        filteredList.add(row);
//                    }
//                }
//                trophiesFiltered = filteredList;
//            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = sportWithTrophiesFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sportWithTrophiesFiltered = (SportWithTrophies) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new TrophyFilter();
    }

}
