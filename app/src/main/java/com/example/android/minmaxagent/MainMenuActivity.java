package com.example.android.minmaxagent;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    Button buttonNewGame, buttonSettings, buttonInstruction, buttonCredits;
    Intent receivedIntent;
    String userName;
    private boolean exitFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        receivedIntent = getIntent();

        if(receivedIntent != null) {

            String pName = receivedIntent.getStringExtra("UserName");
            //Toast.makeText(getApplicationContext(),"Name: " + pName,Toast.LENGTH_LONG).show();
            if (!TextUtils.isEmpty(pName))
                userName = pName;
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
                intentNewGame.putExtra("UserName" , userName);
                startActivity(intentNewGame);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Settings button is clicked

                Intent intentSettings = new Intent(getApplicationContext() , SettingsActivity.class);
                intentSettings.putExtra("UserName" , userName);
                startActivity(intentSettings);
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instruction button is clicked
                Intent intentInstruction = new Intent(getApplicationContext() , InstructionActivity.class);
                intentInstruction.putExtra("UserName" , userName);
                startActivity(intentInstruction);
            }
        });

        buttonCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Credits button is clicked
                Intent intentCredits = new Intent(getApplicationContext() , CreditActivity.class);
                intentCredits.putExtra("UserName" , userName);
                startActivity(intentCredits);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (exitFlag) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit." , Toast.LENGTH_SHORT).show();
            exitFlag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitFlag = false;
                }
            }, 3 * 1000);

        }
    }
}
