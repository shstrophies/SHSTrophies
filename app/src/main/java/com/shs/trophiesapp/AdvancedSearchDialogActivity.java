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
import com.shs.trophiesapp.search.SearchParameters;
import com.shs.trophiesapp.utils.ValidationHelper;

public class AdvancedSearchDialogActivity extends AppCompatActivity {

    Context context;
    private Activity activity;
    private AlertDialog alertDialog;

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
    }

    void startAdvancedSearchDialogActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.advanced_search, null);
        builder.setView(dialogLayout);
        alertDialog = builder.create();
        alertDialog.show();
        initViews(dialogLayout);
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

            checkValidation();

            String trophies = editTextTrophies.getText().toString().trim();
            String sports = editTextSports.getText().toString().trim();
            String years = editTextYears.getText().toString().trim();
            String players = editTextPlayers.getText().toString().trim();

            Intent intent = new Intent(context, TrophiesWithAwardsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            intent.putExtra(SearchParameters.ALL, "");
            intent.putExtra(SearchParameters.TROPHYTITLES, trophies);
            intent.putExtra(SearchParameters.SPORTNAMES, sports);
            intent.putExtra(SearchParameters.YEARS, years);
            intent.putExtra(SearchParameters.PLAYERNAMES, players);
            context.startActivity(intent);
        });
    }

    /**
     * method for validation of form on search button click
     */
    private void checkValidation() {
        //TODO
//        if (!validation.isEditTextFilled(editTextTrophies, textInputLayoutTrophies, context.getString(R.string.error_message_trophies))) {
//            return;
//        }
//
//        if (!validation.isEditTextEmail(editTextSports, textInputLayoutSports, context.getString(R.string.error_message_sports))) {
//            return;
//        }
//
//        if (!validation.isEditTextFilled(editTextYears, textInputLayoutYears, context.getString(R.string.error_message_years))) {
//            return;
//        }
//        if (!validation.isEditTextFilled(editTextPlayers, textInputLayoutPlayers, context.getString(R.string.error_message_players))) {
//            return;
//        }

        Toast.makeText(context, context.getString(R.string.success_message), Toast.LENGTH_LONG).show();


    }


}


