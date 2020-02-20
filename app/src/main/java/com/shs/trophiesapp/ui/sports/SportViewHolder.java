package com.shs.trophiesapp.ui.sports;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.entities.Sport;
import com.shs.trophiesapp.utils.Constants;

public class SportViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private ImageView imgView;

    public SportViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.sport_title_id);
        imgView = itemView.findViewById(R.id.my_image_view);
    }

    public void setDetails(Sport sport) {
        txtTitle.setText(sport.sport_name);
        String imageUrl = sport.image_url;
        imageFromUrl(imgView, imageUrl);
    }

    public static void imageFromUrl(ImageView view, String imageUrl) {

        String[] p=imageUrl.split("/");
        //Create the new image link
        String imageLink= Constants.DRIVE_URL+p[5];

        if (imageUrl !=  null && !imageUrl.isEmpty()) {
            Glide.with(view.getContext()).load(imageLink).into(view);

        }
    }
}

