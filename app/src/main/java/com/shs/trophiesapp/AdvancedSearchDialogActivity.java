package com.shs.trophiesapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class AdvancedSearchDialogActivity {

    private Activity activity;
    private AlertDialog alertDialog;

    AdvancedSearchDialogActivity(Activity myActivity){
        activity = myActivity;
    }

    void startAdvancedSearchDialogActivity(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.advanced_search, null));

        alertDialog = builder.create();
        alertDialog.show();

    }

    void dismissDialog(){
        alertDialog.dismiss();

    }


}


