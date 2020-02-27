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

class TrophyPlayerAndYearViewHolder extends RecyclerView.ViewHolder {
    private TextView txtPlayer;
    private TextView txtYear;
    CardView cardView;
    static ColorGenerator newColor = new ColorGenerator(new int[]{Color.parseColor("#009A28"), Color.parseColor("#FF3232"), Color.parseColor("#FF8900"),  Color.parseColor("#00CB0C"), Color.parseColor("#FF5C00"), Color.parseColor("#009A95"), Color.parseColor("#006E9A"), Color.parseColor("#004CCB"), Color.parseColor("#A8C100")     });


    TrophyPlayerAndYearViewHolder(View itemView) {
        super(itemView);
        txtPlayer = itemView.findViewById(R.id.txtPlayer);
        txtYear = itemView.findViewById(R.id.txtYear);
        cardView = itemView.findViewById(R.id.cardview_trophy_player_and_year_id);
    }

    void setDetails(Trophy trophy) {
        txtPlayer.setText(trophy.getPlayer());
        txtYear.setText(String.valueOf(trophy.getYear()));


        newColor = newColor.getNextColor();
        trophy.setColor(newColor.getColor());
        this.cardView.setBackgroundColor(trophy.getColor());
    }
}