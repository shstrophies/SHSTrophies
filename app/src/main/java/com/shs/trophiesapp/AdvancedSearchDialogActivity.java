package com.shs.trophiesapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shs.trophiesapp.databinding.AdvancedSearchBinding;
import com.shs.trophiesapp.search.SearchParameters;

import java.util.Objects;

public class AdvancedSearchDialogActivity extends AppCompatActivity {

    Context context;
    private Activity activity;
    private AlertDialog alertDialog;
    private AdvancedSearchBinding binding;

    AdvancedSearchDialogActivity(Activity myActivity) {
        activity = myActivity;
        context = activity.getApplicationContext();
        binding = AdvancedSearchBinding.inflate(activity.getLayoutInflater());
    }

    void startAdvancedSearchDialogActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(binding.getRoot());
        alertDialog = builder.create();
        alertDialog.show();
        initListeners();
    }

    void dismissDialog() {
        alertDialog.dismiss();
    }

    /**
     * method to initialize listeners
     */
    private void initListeners() {
        binding.buttonSearch.setOnClickListener(v -> {

            String trophies = Objects.requireNonNull(binding.editTextTrophies.getText()).toString().trim();
            String sports = Objects.requireNonNull(binding.editTextSports.getText()).toString().trim();
            String years = Objects.requireNonNull(binding.editTextYears.getText()).toString().trim();
            String players = Objects.requireNonNull(binding.editTextPlayers.getText()).toString().trim();

            if(checkValidation(trophies, sports, years, players)) {
                Intent intent = new Intent(context, TrophiesWithAwardsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra(SearchParameters.ALL, "");
                intent.putExtra(SearchParameters.TROPHYTITLES, trophies);
                intent.putExtra(SearchParameters.SPORTNAMES, sports);
                intent.putExtra(SearchParameters.YEARS, years);
                intent.putExtra(SearchParameters.PLAYERNAMES, players);
                context.startActivity(intent);
            }
        });
    }

    /**
     * method for validation of form on search button click
     * Only checks if year has numbers and if others have pure text (checking trophy titles vs player names is impossible)
     */
    private boolean checkValidation(String trophies, String sports, String years, String players) {
        if(!trophies.replaceAll(" ", "").chars().allMatch(Character::isLetter)) {
            Toast.makeText(context, "Trophy Title Input Invalid, Please Input Only Text", Toast.LENGTH_LONG).show();
            return false;
        } if(!sports.replaceAll(" ", "").chars().allMatch(Character::isLetter)) {
            Toast.makeText(context, "Sport Names Input Invalid, Please Input Only Text", Toast.LENGTH_LONG).show();
            return false;
        } if(!players.replaceAll(" ", "").chars().allMatch(Character::isLetter)) {
            Toast.makeText(context, "Player Names Input Invalid, Please Input Only Text", Toast.LENGTH_LONG).show();
            return false;
        } if(!years.replaceAll(" ", "").chars().allMatch(Character::isDigit)) {
            Toast.makeText(context, "Years Input Invalid, Please Input Only Digits", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}


