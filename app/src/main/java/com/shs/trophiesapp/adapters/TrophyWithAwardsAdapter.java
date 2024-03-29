package com.shs.trophiesapp.adapters;

import android.content.Context;
import android.content.Intent;
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

import com.shs.trophiesapp.PersonalPlayerAwardsActivity;
import com.shs.trophiesapp.R;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.generators.ColorGeneratorByYear;
import com.shs.trophiesapp.search.SearchParameters;

import java.util.ArrayList;
import java.util.List;


public class TrophyWithAwardsAdapter extends RecyclerView.Adapter<TrophyWithAwardsAdapter.TrophyWithAwardsViewHolder> implements Filterable {
    private static final String TAG = "TrophyWithAwardsAdapter";
    private Context context;
    private List<TrophyAward> awards;
    private List<TrophyAward> awardsFiltered;
    private String playerName;
    private int trophyColor;


    public TrophyWithAwardsAdapter(Context context, List<TrophyAward> awards, int trophyColor) {
        this.context = context;
        this.awards = awards;
        this.awardsFiltered = awards;
        this.trophyColor = trophyColor;
    }

    public TrophyWithAwardsAdapter(Context context, List<TrophyAward> awards, int trophyColor, String playerName) {
        this.context = context;
        this.awards = awards;
        this.awardsFiltered = awards;
        this.trophyColor = trophyColor;
        this.playerName = playerName;
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


        View.OnClickListener listener = view -> {


            Intent intent = new Intent(context, PersonalPlayerAwardsActivity.class);
//            Intent intent = new Intent(context, TrophiesWithAwardsActivity.class);

            // passing data
            intent.putExtra("playerName", trophyAward.getPlayer());
            intent.putExtra("color", trophyAward.getColor());
            intent.putExtra("trophyId", trophyAward.getTrophyId());

            intent.putExtra(SearchParameters.ALL, "");
            intent.putExtra(SearchParameters.TROPHYTITLES, "");
            intent.putExtra(SearchParameters.SPORTNAMES, "");
            intent.putExtra(SearchParameters.YEARS, "");
            intent.putExtra(SearchParameters.PLAYERNAMES, trophyAward.getPlayer());

            // start activity
            context.startActivity(intent);
        };

        // set click listener
        if(playerName == null || !playerName.equals(trophyAward.getPlayer())) holder.cardView.setOnClickListener(listener);
    }

    static class TrophyWithAwardsViewHolder extends RecyclerView.ViewHolder {
        private TextView txtPlayer;
        private TextView txtYear;
        CardView cardView;


        TrophyWithAwardsViewHolder(View itemView) {
            super(itemView);
            txtPlayer = itemView.findViewById(R.id.txtPlayer);
            txtYear = itemView.findViewById(R.id.txtYear);
            cardView = itemView.findViewById(R.id.cardview_trophy_player_and_year_id);
        }

        public interface OnPlayerListener{
            void onPlayerClick(int position);

        }

        void setDetails(TrophyAward trophyAward) {
            txtPlayer.setText(trophyAward.getPlayer());
            txtYear.setText(String.valueOf(trophyAward.getYear()));


            int color = ColorGeneratorByYear.getInstance().getColorForYear(trophyAward.getYear());
            trophyAward.setColor(color);
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
                awardsFiltered = new ArrayList<>();
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