package com.shs.trophiesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Clean");

                View editTextView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.admin_password_clean,
                        findViewById(android.R.id.content), false);
                final EditText input = editTextView.findViewById(R.id.input);

                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setHint("Enter Admin Password");
                builder.setView(editTextView);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(dialogInterface -> {
                    Button posButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    posButton.setOnClickListener(v -> {
                        if(input.getText().toString().equals(Constants.CLEAN_PASSWORD)) {
                            SetupActivity.clean(new WeakReference<>(getApplicationContext()));
                            dialog.dismiss();
                            Intent intent = new Intent(BaseActivity.this, SetupActivity.class);
                            intent.putExtra("Clean", true);
                            startActivity(intent);
                        } else Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
                    });
                });
                dialog.show();
                return true;
            case R.id.action_about:
                new AboutDialogActivity(this).startAboutDialogActivity();
                return true;
            case R.id.action_advanced_search:
                new AdvancedSearchDialogActivity(this).startAdvancedSearchDialogActivity();
                return true;
            case R.id.action_report_bug:
                startActivity(new Intent(BaseActivity.this, ReportBugActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
