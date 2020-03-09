package com.shs.trophiesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.TrophyDetailsActivity;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.utils.ColorGenerator;

import java.util.ArrayList;
import java.util.List;


public class TrophyWithAwardsAdapter extends RecyclerView.Adapter<TrophyWithAwardsAdapter.TrophyWithAwardsViewHolder> implements Filterable {
    private static final String TAG = "SportWithTrophiesAdapter";
    private Context context;
    private List<TrophyAward> awards;
    private List<TrophyAward> awardsFiltered;
    private int trophyColor;


    public TrophyWithAwardsAdapter(Context context, List<TrophyAward> awards, int trophyColor) {
        this.context = context;
        this.awards = awards;
        this.awardsFiltered = awards;
        this.trophyColor = trophyColor;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: awardsFiltered.size()=" + awardsFiltered.size());
        return awardsFiltered.size();
    }

    @Override
    @NonNull
    public TrophyWithAwardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_trophy_player_and_year, parent, false);
        return new TrophyWithAwardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrophyWithAwardsViewHolder holder, int position) {
        TrophyAward trophyAward = awardsFiltered.get(position);
        holder.setDetails(trophyAward);


        // set click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TrophyDetailsActivity.class);

                // passing data
                intent.putExtra("trophyId", awardsFiltered.get(position).getTrophyId());
                intent.putExtra("year", awardsFiltered.get(position).getYear());
                intent.putExtra("player", awardsFiltered.get(position).getPlayer());
                intent.putExtra("category", awardsFiltered.get(position).getCategory());
                intent.putExtra("color", trophyColor);

                // start activity
                context.startActivity(intent);
            }
        });
    }

    static ColorGenerator newColor = new ColorGenerator(new int[]{Color.parseColor("#009A28"), Color.parseColor("#FF3232"), Color.parseColor("#FF8900"),  Color.parseColor("#00CB0C"), Color.parseColor("#FF5C00"), Color.parseColor("#009A95"), Color.parseColor("#006E9A"), Color.parseColor("#004CCB"), Color.parseColor("#A8C100")     });

    class TrophyWithAwardsViewHolder extends RecyclerView.ViewHolder {
        private TextView txtPlayer;
        private TextView txtYear;
        CardView cardView;


        TrophyWithAwardsViewHolder(View itemView) {
            super(itemView);
            txtPlayer = itemView.findViewById(R.id.txtPlayer);
            txtYear = itemView.findViewById(R.id.txtYear);
            cardView = itemView.findViewById(R.id.cardview_trophy_player_and_year_id);
        }

        void setDetails(TrophyAward trophyAward) {
            txtPlayer.setText(trophyAward.getPlayer());
            txtYear.setText(String.valueOf(trophyAward.getYear()));


            newColor = newColor.getNextColor();
            trophyAward.setColor(newColor.getColor());
            this.cardView.setBackgroundColor(trophyAward.getColor());
        }
    }

    private class TrophyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "performFiltering: constraint=" + constraint);
            String charString = constraint.toString().toLowerCase();
            if (charString.isEmpty()) {
                awardsFiltered = awards;
            } else {
                Log.d(TAG, "performFiltering: charString=" + charString);
                List<TrophyAward> filteredList = new ArrayList<>();
//                for (TrophyAward row : awards) {
//                    // name match condition. this might differ depending on your requirement
//                    // here we are looking for title or description match
//                    if (row.title.toLowerCase().contains(charString) || row.player.toLowerCase().contains(charString)) {
//                        filteredList.add(row);
//                    }
//                }


                awardsFiltered = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = awardsFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            awardsFiltered = (ArrayList<TrophyAward>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new TrophyFilter();
    }

}
