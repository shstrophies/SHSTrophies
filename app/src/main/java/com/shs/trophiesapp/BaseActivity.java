package com.shs.trophiesapp;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                startActivity(new Intent(this, SetupActivity.class));
                return true;
            case R.id.action_about:
                new AboutDialogActivity(this).startAboutDialogActivity();
                return true;
            case R.id.action_advanced_search:
                new AdvancedSearchDialogActivity(this).startAdvancedSearchDialogActivity();
                return true;
            case R.id.action_report_bug:
                //TODO: Report Bug Action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
