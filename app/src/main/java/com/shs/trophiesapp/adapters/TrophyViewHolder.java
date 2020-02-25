package com.shs.trophiesapp.adapters;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.utils.Constants;

import java.io.File;

class TrophyViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private ImageView imgView;
    //private TextView txtDescription;

    TrophyViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        imgView = itemView.findViewById(R.id.my_image_view);
        //txtDescription = itemView.findViewById(R.id.txtDescription);//commenting this out since we only need the title of trophy on the trophies page
    }

    void setDetails(Trophy trophy) {
        txtTitle.setText(trophy.tr_title);
        String imageUrl = trophy.tr_image_url;
        imageFromUrl(imgView, imageUrl);
        // txtDescription.setText(trophy.player);
    }

    private static void imageFromUrl(ImageView view, String imageUrl) {
        if (!imageUrl.isEmpty()) {
            String imgName = imageUrl.replace(Constants.DRIVE_IMAGE_PREFIX, "").split("/")[0] + MimeTypeMap.getFileExtensionFromUrl(imageUrl);
            view.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + Constants.DATA_DIRECTORY_TROPHY_IMAGES + imgName));
            //Glide.with(view.getContext()).load(imageLink).into(view);
        }
    }
}
