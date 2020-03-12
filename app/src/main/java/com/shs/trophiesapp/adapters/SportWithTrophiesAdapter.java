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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.TrophyWithAwardsActivity;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.utils.ColorGenerator;
import com.shs.trophiesapp.utils.Utils;

public class SportWithTrophiesAdapter extends RecyclerView.Adapter<SportWithTrophiesAdapter.TrophyViewHolder> implements Filterable {
    private static final String TAG = "SportWithTrophiesAdapter";
    private Context context;
    private SportWithTrophies sportWithTrophies;
    private SportWithTrophies sportWithTrophiesFiltered;




    public SportWithTrophiesAdapter(Context context, SportWithTrophies sportWithTrophies) {
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

                Intent intent = new Intent(context, TrophyWithAwardsActivity.class);

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

    static ColorGenerator newColor = new ColorGenerator(new int[]{Color.parseColor("#FF3232"), Color.parseColor("#FF5C00"), Color.parseColor("#FF8900"),  Color.parseColor("#009A28"), Color.parseColor("#00CB0C"), Color.parseColor("#009A95"), Color.parseColor("#006E9A"), Color.parseColor("#004CCB"), Color.parseColor("#4761FF"), Color.parseColor("#7A2AFF"), Color.parseColor("#6900B2"), Color.parseColor("#910094"), Color.parseColor("#8A0013"), Color.parseColor("#C62DDF") , Color.parseColor("#A8C100")    });

    class TrophyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private ImageView imgView;
        CardView cardView;


        TrophyViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imgView = itemView.findViewById(R.id.my_image_view);
            cardView = itemView.findViewById(R.id.cardview_trophy_id);
        }

        void setDetails(Trophy trophy) {
            txtTitle.setText(trophy.getTitle());
            String imageUrl = trophy.getUrl();
            Utils.imageFromUrl(imgView, imageUrl);

            newColor = newColor.getNextColor();
            trophy.setColor(newColor.getColor());
            this.cardView.setBackgroundColor(trophy.getColor());
        }
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
