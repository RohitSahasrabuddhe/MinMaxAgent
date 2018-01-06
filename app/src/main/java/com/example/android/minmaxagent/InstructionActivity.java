package com.example.android.minmaxagent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InstructionActivity extends AppCompatActivity {

    Button buttonBack, buttonNewGame;
    String userName;
    Intent receivedIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/ComicRelief.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build() );

        setContentView(R.layout.activity_instruction);

        receivedIntent = getIntent();
        userName = receivedIntent.getStringExtra("UserName");

        buttonBack = findViewById(R.id.buttonBack);
        buttonNewGame = findViewById(R.id.buttonNewGame);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainMenu = new Intent(getApplicationContext(),MainMenuActivity.class);
                intentMainMenu.putExtra("UserName",userName);
                intentMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMainMenu);


            }
        });

        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGameActivity = new Intent(getApplicationContext(),GameActivity.class);
                intentGameActivity.putExtra("UserName",userName);
                startActivity(intentGameActivity);
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
