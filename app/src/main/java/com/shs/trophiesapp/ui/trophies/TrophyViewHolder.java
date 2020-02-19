package com.shs.trophiesapp.ui.trophies;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.entities.Trophy;

public class TrophyViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private ImageView imgView;
    private TextView txtDescription;

    public TrophyViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        imgView = itemView.findViewById(R.id.my_image_view);
        txtDescription = itemView.findViewById(R.id.txtDescription);
    }

    public void setDetails(Trophy trophy) {
        txtTitle.setText(trophy.tr_title);
        String imageUrl = trophy.tr_image_url;
        imageFromUrl(imgView, imageUrl);
        txtDescription.setText(trophy.player);
    }

    public static void imageFromUrl(ImageView view, String imageUrl) {

        String[] p=imageUrl.split("/");
        if(p.length > 5) {
            //Create the new image link
            String imageLink="https://drive.google.com/uc?export=download&id="+p[5];

            if ((imageUrl !=  null) && !imageUrl.isEmpty()) {
                Glide.with(view.getContext()).load(imageLink).into(view);

            }
        }

    }
}
