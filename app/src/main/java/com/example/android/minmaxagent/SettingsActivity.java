package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    // TODO (7) Persistent preferences - next new game has these default settings

    EditText valuePlayerName;
    TextView tvFruitTypeProgressIndicator, tvGridSizeProgressIndicator;
    SeekBar valueFruitTypes, valueGridSize;
    private int fruitTypeProgress = 0, gridSizeProgress = 0;
    private static final int FRUITS_SEEK_MAX = 5, GRID_SEEK_MAX = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        valuePlayerName = findViewById(R.id.valueUserName);

        valueFruitTypes = findViewById(R.id.valueFruitType);
        tvFruitTypeProgressIndicator = findViewById(R.id.fruitTypeValueIndicator);

        valueFruitTypes.setProgress(fruitTypeProgress);
        valueFruitTypes.setMax(FRUITS_SEEK_MAX);

        valueFruitTypes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                // int indicatorValue = progressValue/10 + 4;
                int indicatorValue = progressValue + 4;
                tvFruitTypeProgressIndicator.setText(""+indicatorValue);
                fruitTypeProgress = indicatorValue;;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        valueGridSize = findViewById(R.id.valueGridSize);
        tvGridSizeProgressIndicator = findViewById(R.id.gridSizeValueIndicator);

        valueGridSize.setProgress(gridSizeProgress);
        valueGridSize.setMax(GRID_SEEK_MAX);

        valueGridSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                int indicatorValue = progressValue + 4;
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

                Intent intent = new Intent(getApplicationContext() , GameActivity.class);

                intent.putExtra("valuePlayerName" , valuePlayerName.getText().toString());
                intent.putExtra("valueFruitTypes" , ""+fruitTypeProgress);
                intent.putExtra("valueGridSize" , ""+gridSizeProgress);

                startActivity(intent);
            }
        });

    }
}
