package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    Button buttonNewGame, buttonSettings, buttonInstruction, buttonCredits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonInstruction = findViewById(R.id.buttonInstructions);
        buttonCredits = findViewById(R.id.buttonCredits);

        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New Game button is Clicked

                Intent intentNewGame = new Intent(getApplicationContext() , SettingsActivity.class);
                startActivity(intentNewGame);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Settings button is clicked

                Intent intentSettings = new Intent(getApplicationContext() , SettingsActivity.class);
                startActivity(intentSettings);
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instruction button is clicked
                Intent intentInstruction = new Intent(getApplicationContext() , InstructionActivity.class);
                startActivity(intentInstruction);
            }
        });

        buttonCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Credits button is clicked
                Intent intentCredits = new Intent(getApplicationContext() , CreditActivity.class);
                startActivity(intentCredits);
            }
        });

    }
}
