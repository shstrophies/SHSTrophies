package com.shs.trophiesapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.utils.Utils;

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
        Utils.imageFromUrl(imgView, imageUrl);
    }
}

