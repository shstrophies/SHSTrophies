package com.shs.trophiesapp;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.shs.trophiesapp.utils.Constants;

import java.lang.ref.WeakReference;

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_full_clean:
                SetupActivity.clean(new WeakReference<>(getApplicationContext()));
                Intent intent = new Intent(this, SetupActivity.class);
                intent.putExtra("Clean", true);
                startActivity(intent);
                return true;
            case R.id.action_about:
                new AboutDialogActivity(this).startAboutDialogActivity();
                return true;
            case R.id.action_advanced_search:
                new AdvancedSearchDialogActivity(this).startAdvancedSearchDialogActivity();
                return true;
            case R.id.action_report_bug:
                Intent emailIntent =
                        new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Constants.BUG_REPORT_EMAIL, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Trophy App Kiosk Bug Report");
                startActivity(Intent.createChooser(emailIntent, "Sending Email..."));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
