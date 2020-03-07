package com.shs.trophiesapp.adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.utils.ColorGenerator;

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

    void setDetails(TrophyAward trophyAward) {
        txtPlayer.setText(trophyAward.getPlayer());
        txtYear.setText(String.valueOf(trophyAward.getYear()));


        newColor = newColor.getNextColor();
        trophyAward.setColor(newColor.getColor());
        this.cardView.setBackgroundColor(trophyAward.getColor());
    }
}