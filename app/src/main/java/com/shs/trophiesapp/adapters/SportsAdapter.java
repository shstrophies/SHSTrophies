package com.shs.trophiesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.shs.trophiesapp.R;
import com.shs.trophiesapp.TrophyWithAwardsActivity;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.TrophiesActivity;
import com.shs.trophiesapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportViewHolder> implements Filterable {
    private static final String TAG = "SportsAdapter";

    private Context context;
    private ArrayList<Sport> sports;
    private List<Sport> sportsFiltered;

    public SportsAdapter(Context context, ArrayList<Sport> sports) {
        this.context = context;
        this.sports = sports;
        this.sportsFiltered = sports;
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: sportsFiltered.size=" + sportsFiltered.size());
        return sportsFiltered.size();
    }

    @Override
    @NonNull
    public SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_sport, parent, false);
        return new SportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SportViewHolder holder, int position) {
        Sport sport = sportsFiltered.get(position);
        holder.setDetails(sport);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TrophiesActivity.class);
                // passing data
                intent.putExtra(TrophiesActivity.TROPHIES_BY_SPORT_NAME, sport.name);

                // start activity
                context.startActivity(intent);
            }
        };
        // set click listeners
        holder.itemView.setOnClickListener(listener);
        holder.txtTitle.setOnClickListener(listener);
    }

    class SportViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private ImageView imgView;

        SportViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.sport_title_id);
            imgView = itemView.findViewById(R.id.my_image_view);
        }

        void setDetails(Sport sport) {
            txtTitle.setText(sport.name);
            String imageUrl = sport.imageUrl;
            //Utils.imageFromUrl(imgView, imageUrl);
            Utils.imageFromCache(imgView, imageUrl);
        }
    }

    private class SportFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: constraint=" + constraint);
            String charString = constraint.toString().toLowerCase();
            if (charString.isEmpty()) {
                sportsFiltered = sports;
            } else {
                Log.d(TAG, "performFiltering: charString=" + charString);
                List<Sport> filteredList = new ArrayList<>();
                for (Sport row : sports) {
                    // name match condition. this might differ depending on your requirement
                    // "Search for a sport name, trophy name, player name, or year..."
                    if (row.name.toLowerCase().contains(charString)) {
                        filteredList.add(row);
                    }
                }
                sportsFiltered = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = sportsFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sportsFiltered = (ArrayList<Sport>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new SportFilter();
    }

}
