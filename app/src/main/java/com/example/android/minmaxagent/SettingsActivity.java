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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {

    EditText valuePlayerName;
    TextView tvFruitTypeProgressIndicator, tvGridSizeProgressIndicator;

    SeekBar valueFruitTypesSeekbar;
    TextView valueFruitTypesMin, valueFruitTypesMax;

    SeekBar valueGridSizeSeekbar;
    TextView valueGridSizeMin, valueGridSizeMax;

    String userName;
    String PLAYER_NAME;
    private int fruitTypeProgress = 0, gridSizeProgress = 0;

    private static final int FRUITTYPES_MIN = 2, FRUITTYPES_MAX = 9;
    private static final int GRIDSIZE_MIN = 3, GRIDSIZE_MAX = 9;

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

        /*Toast toast=Toast.makeText(getApplicationContext(),"CurrentUserProfile: " + currentUserProfile,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();*/

        // Fruit Types
        fruitTypeProgress = currentUserProfile.getFruitType();

        valueFruitTypesMin = findViewById(R.id.valueFruitTypeMin);
        valueFruitTypesMin.setText(String.valueOf(FRUITTYPES_MIN));

        valueFruitTypesMax = findViewById(R.id.valueFruitTypeMax);
        valueFruitTypesMax.setText(String.valueOf(FRUITTYPES_MAX));

        valueFruitTypesSeekbar = findViewById(R.id.valueFruitType);
        valueFruitTypesSeekbar.setProgress(fruitTypeProgress-3);
        valueFruitTypesSeekbar.setMax(FRUITTYPES_MAX - FRUITTYPES_MIN);

        tvFruitTypeProgressIndicator = findViewById(R.id.fruitTypeValueIndicator);
        tvFruitTypeProgressIndicator.setText(String.valueOf(currentUserProfile.getFruitType()));

        valueFruitTypesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                // int indicatorValue = progressValue/10 + 4;
                int indicatorValue = progressValue + FRUITTYPES_MIN;
                tvFruitTypeProgressIndicator.setText(String.valueOf(indicatorValue));
                fruitTypeProgress = indicatorValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Grid Size
        gridSizeProgress = currentUserProfile.getGridSize();

        valueGridSizeMin = findViewById(R.id.valueGridSizeMin);
        valueGridSizeMin.setText(String.valueOf(GRIDSIZE_MIN));

        valueGridSizeMax = findViewById(R.id.valueGridSizeMax);
        valueGridSizeMax.setText(String.valueOf(GRIDSIZE_MIN));

        valueGridSizeSeekbar = findViewById(R.id.valueGridSize);
        valueGridSizeSeekbar.setProgress(gridSizeProgress-3);

        tvGridSizeProgressIndicator = findViewById(R.id.gridSizeValueIndicator);
        // String gridDisplayString = String.valueOf(currentUserProfile.getGridSize()) + R.string.settings_activity_grid_size + String.valueOf(currentUserProfile.getGridSize());
        String gridDisplayString = getResources().getString(R.string.settings_gridsize_placeholder, currentUserProfile.getGridSize());
        tvGridSizeProgressIndicator.setText(gridDisplayString);
        valueGridSizeSeekbar.setMax(GRIDSIZE_MAX - GRIDSIZE_MIN);

        valueGridSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                int indicatorValue = progressValue + GRIDSIZE_MIN;
                tvGridSizeProgressIndicator.setText(getResources().getString(R.string.settings_gridsize_placeholder, indicatorValue));
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

                // TODO What does this do? is it reqd
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
