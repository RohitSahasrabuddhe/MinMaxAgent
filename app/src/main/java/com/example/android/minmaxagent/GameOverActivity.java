package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    TextView tvRestart, tvQuit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        tvRestart = findViewById(R.id.textViewRestartButton);

        tvRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentRestartGame = new Intent(getApplicationContext() , GameActivity.class);
                startActivity(intentRestartGame);

            }
        });

        tvQuit = findViewById(R.id.textViewQuitButton);

        tvQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentQuitGame = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intentQuitGame);

            }
        });


    }
}
