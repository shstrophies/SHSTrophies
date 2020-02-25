package com.shs.trophiesapp.adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.utils.ColorGenerator;
import com.shs.trophiesapp.utils.Utils;

class TrophyViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private ImageView imgView;
    CardView cardView;
    static ColorGenerator newColor = new ColorGenerator(new int[]{Color.BLUE, Color.YELLOW, Color.RED});


    TrophyViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        imgView = itemView.findViewById(R.id.my_image_view);
        cardView = itemView.findViewById(R.id.cardview_trophy_id);
    }

    void setDetails(Trophy trophy) {
        txtTitle.setText(trophy.tr_title);
        String imageUrl = trophy.tr_image_url;
        Utils.imageFromUrl(imgView, imageUrl);

        newColor = newColor.getNextColor();
        trophy.setColor(newColor.getColor());
        this.cardView.setBackgroundColor(trophy.getColor());
    }


}
