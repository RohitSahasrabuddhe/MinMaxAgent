package com.example.android.minmaxagent;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenuActivity extends AppCompatActivity {

    Button buttonNewGame, buttonSettings, buttonInstruction, buttonLogout;
    Intent receivedIntent;
    String userName;
    private boolean exitFlag = false;

    /**
     * Allow Calligraphy to set its default font.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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

        // New Game
        buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New Game button is Clicked
                Intent intentNewGame = new Intent(getApplicationContext() , GameActivity.class);
                intentNewGame.putExtra("UserName" , userName);
                startActivity(intentNewGame);

            }
        });

        // Settings
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Settings button is clicked

                Intent intentSettings = new Intent(getApplicationContext() , SettingsActivity.class);
                intentSettings.putExtra("UserName" , userName);
                startActivity(intentSettings);

            }
        });

        // Instructions
        buttonInstruction = findViewById(R.id.buttonInstructions);
        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Instruction button is clicked
                Intent intentInstruction = new Intent(getApplicationContext() , InstructionActivity.class);

                intentInstruction.putExtra("UserName" , userName);
                startActivity(intentInstruction);
            }
        });

        // Credits
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Logout button is clicked
                Intent intentLogout = new Intent(getApplicationContext() , LoginActivity.class);
                //intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogout);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (exitFlag) {
            Intent intentLogout = new Intent(getApplicationContext() , LoginActivity.class);
            startActivity(intentLogout);
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Log Out." , Toast.LENGTH_SHORT).show();
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
