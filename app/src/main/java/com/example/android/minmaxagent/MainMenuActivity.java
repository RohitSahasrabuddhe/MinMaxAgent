package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    Button buttonNewGame, buttonSettings, buttonInstruction, buttonCredits;
    Intent receivedIntent;
    String PLAYER_NAME = "Dummy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        receivedIntent = getIntent();

        if(receivedIntent != null) {

            String userName = receivedIntent.getStringExtra("UserName");

            if (!TextUtils.isEmpty(userName))
                PLAYER_NAME = userName;
        }



        buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonInstruction = findViewById(R.id.buttonInstructions);
        buttonCredits = findViewById(R.id.buttonCredits);

        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New Game button is Clicked

                Intent intentNewGame = new Intent(getApplicationContext() , GameActivity.class);
                intentNewGame.putExtra("UserName" , PLAYER_NAME);
                startActivity(intentNewGame);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Settings button is clicked

                Intent intentSettings = new Intent(getApplicationContext() , SettingsActivity.class);
                intentSettings.putExtra("UserName" , PLAYER_NAME);
                startActivity(intentSettings);
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instruction button is clicked
                Intent intentInstruction = new Intent(getApplicationContext() , InstructionActivity.class);
                intentInstruction.putExtra("UserName" , PLAYER_NAME);
                startActivity(intentInstruction);
            }
        });

        buttonCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Credits button is clicked
                Intent intentCredits = new Intent(getApplicationContext() , CreditActivity.class);
                intentCredits.putExtra("UserName" , PLAYER_NAME);
                startActivity(intentCredits);
            }
        });

    }
}
