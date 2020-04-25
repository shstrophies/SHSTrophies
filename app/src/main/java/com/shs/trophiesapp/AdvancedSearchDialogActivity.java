package com.shs.trophiesapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.shs.trophiesapp.databinding.AdvancedSearchBinding;
import com.shs.trophiesapp.search.SearchParameters;
import com.shs.trophiesapp.utils.ValidationHelper;

public class AdvancedSearchDialogActivity extends AppCompatActivity {

    Context context;
    private Activity activity;
    private AlertDialog alertDialog;
    private AdvancedSearchBinding binding;

    //EditText variables
    private EditText editTextTrophies;
    private EditText editTextSports;
    private EditText editTextYears;
    private EditText editTextPlayers;

    //Button
    private Button button;


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
        initViews(binding.getRoot());
        initListeners();
    }

    void dismissDialog() {
        alertDialog.dismiss();
    }


    /**
     * method to initialize views objects
     */
    private void initViews(View parent) {
        //TextInputLayout variables
        TextInputLayout textInputLayoutTrophies = parent.findViewById(R.id.textInputLayoutTrophies);
        TextInputLayout textInputLayoutSports = parent.findViewById(R.id.textInputLayoutSports);
        TextInputLayout textInputLayoutYears = parent.findViewById(R.id.textInputLayoutYears);
        TextInputLayout textInputLayoutPlayers = parent.findViewById(R.id.textInputLayoutPlayers);

        editTextTrophies = parent.findViewById(R.id.edit_text_trophies);
        editTextSports = parent.findViewById(R.id.edit_text_sports);
        editTextYears = parent.findViewById(R.id.edit_text_years);
        editTextPlayers = parent.findViewById(R.id.edit_text_players);

        button = parent.findViewById(R.id.button_search);

        ValidationHelper validation = new ValidationHelper(parent.getContext());
    }

    /**
     * method to initialize listeners
     */
    private void initListeners() {
        button.setOnClickListener(v -> {

            String trophies = editTextTrophies.getText().toString().trim();
            String sports = editTextSports.getText().toString().trim();
            String years = editTextYears.getText().toString().trim();
            String players = editTextPlayers.getText().toString().trim();

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


