package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    Intent receivedIntent;
    TextView tvRestart, tvQuit, tvGameStat;
    ImageView ivGameOverText;

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

        ivGameOverText = findViewById(R.id.imageViewGameOverMessage);

        if (winner.isEmpty()) {
            tvGameStat.setText("Oh well, it's a draw!");
            ivGameOverText.setImageResource(R.drawable.text_game_over_draw);

        } else if (winner.equals("AI")) {
            tvGameStat.setText("The AI beat you by " + scoreDifference + " points!");
            ivGameOverText.setImageResource(R.drawable.text_game_over_lose);
        } else {
            tvGameStat.setText("Congrats, you won by " + scoreDifference + " points!");
            ivGameOverText.setImageResource(R.drawable.text_game_over_win);
        }




        tvRestart = findViewById(R.id.textViewRestartButton);

        tvRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentRestartGame = new Intent(getApplicationContext() , GameActivity.class);
                intentRestartGame.putExtra("UserName",userName);
                intentRestartGame.putExtra("PlayerName",PLAYER_NAME);
                startActivity(intentRestartGame);

                finish();

            }
        });

        tvQuit = findViewById(R.id.textViewQuitButton);

        tvQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentQuitGame = new Intent(getApplicationContext(), MainMenuActivity.class);
                intentQuitGame.putExtra("UserName",userName);
                intentQuitGame.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Clears Activity stack till now
                startActivity(intentQuitGame);

                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentMainMenu = new Intent(getApplicationContext(),MainMenuActivity.class);
        intentMainMenu.putExtra("UserName" , userName);
        startActivity(intentMainMenu);
        finish();
    }
}
