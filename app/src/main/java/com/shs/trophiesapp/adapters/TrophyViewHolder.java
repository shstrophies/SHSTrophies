package com.shs.trophiesapp.adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.utils.Constants;

class TrophyViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private ImageView imgView;
    CardView cardView;
    static int colors[] = {Color.YELLOW, Color.BLUE, Color.RED};
    static int nextColorIndex = 0;


    TrophyViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        imgView = itemView.findViewById(R.id.my_image_view);
        cardView = itemView.findViewById(R.id.cardview_trophy_id);
    }

    void setDetails(Trophy trophy) {
        txtTitle.setText(trophy.tr_title);
        String imageUrl = trophy.tr_image_url;
        imageFromUrl(imgView, imageUrl);

        this.cardView.setBackgroundColor(colors[nextColorIndex++]);
        nextColorIndex %= colors.length;

    }

    private static void imageFromUrl(ImageView view, String imageUrl) {
        String[] p=imageUrl.split("/");
        if(p.length > 5) {
            //Create the new image link
            String imageLink= Constants.DRIVE_URL+p[5];

            if ((imageUrl !=  null) && !imageUrl.isEmpty()) {
                Glide.with(view.getContext()).load(imageLink).into(view);

            }
        }

    }
}
