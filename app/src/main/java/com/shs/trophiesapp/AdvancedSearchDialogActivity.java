package com.shs.trophiesapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AdvancedSearchDialogActivity {

    private Activity activity;
    private AlertDialog alertDialog;

    AdvancedSearchDialogActivity(Activity myActivity){
        activity = myActivity;
    }

    void startAdvancedSearchDialogActivity(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.advanced_search, null);
        builder.setView(dialogLayout);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissDialog(){
        alertDialog.dismiss();

    }


}


