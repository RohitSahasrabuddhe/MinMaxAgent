package com.example.android.minmaxagent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.minmaxagent.db.DBHandler;
import com.example.android.minmaxagent.db.Profile;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {

    // TODO Bug when default settings used: grid doesn't match
    // TODO Crash when Settings -> game -> BACK -> Settings -> game
    // TODO Settings must have a back button

    EditText valuePlayerName;
    TextView tvFruitTypeProgressIndicator, tvGridSizeProgressIndicator;
    SeekBar valueFruitTypesSeekbar, valueGridSizeSeekbar;
    String userName;
    String PLAYER_NAME ="Dummy";
    private int fruitTypeProgress = 0, gridSizeProgress = 0;
    private static final int FRUITS_SEEK_MAX = 6, GRID_SEEK_MAX = 6;
    private Intent receivedIntent;

    /**
     * Allow Calligraphy to set its default font.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        receivedIntent = getIntent();
        userName = receivedIntent.getStringExtra("UserName");
        valuePlayerName = findViewById(R.id.valueUserName);

        if(receivedIntent.getStringExtra("UserName") != null) {
            valuePlayerName.setText(userName);
        }

        DBHandler db = new DBHandler(this);
        Profile currentUserProfile = db.getProfileWithName(userName);
        Toast toast=Toast.makeText(getApplicationContext(),"CurrentUserProfile: " + currentUserProfile,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();

        fruitTypeProgress = currentUserProfile.getFruitType();
        gridSizeProgress = currentUserProfile.getGridSize();

        valueFruitTypesSeekbar = findViewById(R.id.valueFruitType);
        valueFruitTypesSeekbar.setProgress(fruitTypeProgress-3);
        valueFruitTypesSeekbar.setMax(FRUITS_SEEK_MAX);

        tvFruitTypeProgressIndicator = findViewById(R.id.fruitTypeValueIndicator);
        tvFruitTypeProgressIndicator.setText(currentUserProfile.getFruitType() + "");



        valueFruitTypesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                // int indicatorValue = progressValue/10 + 4;
                int indicatorValue = progressValue + 3;
                tvFruitTypeProgressIndicator.setText(""+indicatorValue);
                fruitTypeProgress = indicatorValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        valueGridSizeSeekbar = findViewById(R.id.valueGridSize);
        valueGridSizeSeekbar.setProgress(gridSizeProgress-3);

        tvGridSizeProgressIndicator = findViewById(R.id.gridSizeValueIndicator);
        tvGridSizeProgressIndicator.setText(currentUserProfile.getGridSize() + "X" + currentUserProfile.getGridSize() );
        valueGridSizeSeekbar.setMax(GRID_SEEK_MAX);

        valueGridSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                int indicatorValue = progressValue + 3;
                tvGridSizeProgressIndicator.setText(String.format(Locale.getDefault(), "%d×%d", indicatorValue, indicatorValue));
                gridSizeProgress = indicatorValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        Button startActivityButton = findViewById(R.id.startActivityButton);
        startActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentGame = new Intent(getApplicationContext(),GameActivity.class);
                PLAYER_NAME = valuePlayerName.getText().toString();

                intentGame.putExtra("UserName" , userName);
                intentGame.putExtra("PlayerName" , PLAYER_NAME);

                if(fruitTypeProgress == 0){
                    fruitTypeProgress = 3;
                }
                if(gridSizeProgress == 0){
                    gridSizeProgress = 3;
                }

                updateProfileTable(userName,fruitTypeProgress,gridSizeProgress);

                startActivity(intentGame);
                finish();
            }
        });

    }



    private void updateProfileTable(String userName, int fruitTypeProgress, int gridSizeProgress) {
        DBHandler db = new DBHandler(this);

        db.updateProfile(userName,fruitTypeProgress,gridSizeProgress);

        Toast toast=Toast.makeText(getApplicationContext(),"Update Complete"+"\n"+userName+"-"+ fruitTypeProgress + "-" + gridSizeProgress,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentMainMenu = new Intent(getApplicationContext(),MainMenuActivity.class);
        intentMainMenu.putExtra("UserName" , userName);
        intentMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMainMenu);
        finish();
    }*/
}
