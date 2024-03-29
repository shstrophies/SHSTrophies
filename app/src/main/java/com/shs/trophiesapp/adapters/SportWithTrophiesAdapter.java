package com.shs.trophiesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.TrophyWithAwardsActivity;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.generators.ColorGeneratorByTrophyTitle;
import com.shs.trophiesapp.utils.Utils;

public class SportWithTrophiesAdapter extends RecyclerView.Adapter<SportWithTrophiesAdapter.TrophyViewHolder> {
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


        View.OnClickListener listener = view -> {

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
        };

        holder.txtTitle.setOnClickListener(listener);
        // set click listener
        holder.cardView.setOnClickListener(listener);
    }


    static class TrophyViewHolder extends RecyclerView.ViewHolder {
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
            Utils.imageFromCache(imgView, imageUrl);

            int color = ColorGeneratorByTrophyTitle.getInstance().getColorForTrophyTitle(trophy.getTitle());
            trophy.setColor(color);
            this.cardView.setBackgroundColor(trophy.getColor());
        }
    }
}
