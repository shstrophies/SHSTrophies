package com.shs.trophiesapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shs.trophiesapp.R;
import com.shs.trophiesapp.ui.sports.SportsActivity;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "LaunchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        DownloadTask task = new DownloadTask(this);
        TextView progress = findViewById(R.id.progress_text);
        try {
            if (task.execute().get()) {
                // There are changes to the spreadsheets, so recreate the DB
                progress.setText(R.string.progress_bar_database_recreation);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onCreate: startActivity");
        startActivity(new Intent(LaunchActivity.this, SportsActivity.class));
    }
}
