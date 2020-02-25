package com.shs.trophiesapp.adapters;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.entities.Sport;
import com.shs.trophiesapp.utils.Constants;

import java.io.File;

class SportViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private ImageView imgView;

    SportViewHolder(View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.sport_title_id);
        imgView = itemView.findViewById(R.id.my_image_view);
    }

    void setDetails(Sport sport) {
        txtTitle.setText(sport.sport_name);
        String imageUrl = sport.image_url;
        imageFromUrl(imgView, imageUrl);
    }

    private static void imageFromUrl(ImageView view, String imageUrl) {
        if (!imageUrl.isEmpty()) {
            String imgName = imageUrl.replace(Constants.DRIVE_IMAGE_PREFIX, "").split("/")[0] + MimeTypeMap.getFileExtensionFromUrl(imageUrl);
            String filepath = Environment.getExternalStorageDirectory() + File.separator + Constants.DATA_DIRECTORY_SPORT_IMAGES + imgName;
            Log.d("SportViewHolder", "Setting image bitmap: " + filepath);
            view.setImageBitmap(BitmapFactory.decodeFile(filepath));
            //Glide.with(view.getContext()).load(imageLink).into(view);
        }
    }
}

