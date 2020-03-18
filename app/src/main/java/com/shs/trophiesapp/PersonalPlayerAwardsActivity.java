package com.shs.trophiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.shs.trophiesapp.adapters.SportWithTrophiesAdapter;
import com.shs.trophiesapp.database.relations.SportWithTrophies;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalPlayerAwardsActivity extends AppCompatActivity {

    private static final String TAG = "PersonalPlayerAwardsAct";
    private SportWithTrophiesAdapter adapter;
    private SportWithTrophies sportWithTrophies;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_player_awards_activity);
        Log.d(TAG, "onCreate: started");


        getIncomingIntent();


    }
    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent:checking for incoming intents ");
        if(getIntent().hasExtra("playerName")){

            String thePlayerName = getIntent().getStringExtra("playerName");
            setPlayerNameText(thePlayerName+ "\'s awards");

        }
        
    }
    private void setPlayerNameText(String thePlayerName){
        Log.d(TAG, "setPlayerNameText: setting the name of the player on the top of screen");
        TextView playerName = findViewById(R.id.playersName);
       playerName.setText(thePlayerName);


    }


}
