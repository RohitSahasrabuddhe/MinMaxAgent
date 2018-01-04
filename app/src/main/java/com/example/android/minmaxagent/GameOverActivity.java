package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    Intent receivedIntent;
    TextView tvRestart, tvQuit, tvGameStat;

    String userName, PLAYER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        receivedIntent = getIntent();

        String winner = receivedIntent.getStringExtra("Winner");
        String scoreDifference = receivedIntent.getStringExtra("ScoreDifference");

        userName = receivedIntent.getStringExtra("UserName");
        PLAYER_NAME = receivedIntent.getStringExtra("PlayerName");

        tvGameStat = findViewById(R.id.textViewGameStat);

        if(winner.equals("AI")){
            tvGameStat.setText("The AI beat you by " + scoreDifference +" points!");
        }
        else{
            tvGameStat.setText("Congrats, you won by " + scoreDifference +" points!");
        }


        tvRestart = findViewById(R.id.textViewRestartButton);

        tvRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentRestartGame = new Intent(getApplicationContext() , GameActivity.class);
                intentRestartGame.putExtra("UserName",userName);
                intentRestartGame.putExtra("PlayerName",PLAYER_NAME);
                startActivity(intentRestartGame);

            }
        });

        tvQuit = findViewById(R.id.textViewQuitButton);

        tvQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentQuitGame = new Intent(getApplicationContext(), MainMenuActivity.class);
                intentQuitGame.putExtra("UserName",userName);
                startActivity(intentQuitGame);

            }
        });


    }
}
