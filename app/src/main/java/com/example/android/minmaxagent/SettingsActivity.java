package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    // TODO (7) Persistent preferences - next new game has these default settings

    EditText valuePlayerName, valueFruitTypes, valueGridSize;
    SeekBar valueSeekBar;
    private int seekBarValueInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        valueSeekBar = findViewById(R.id.inputSeekBar);

        SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext() ,  "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        };

        valueSeekBar.setOnSeekBarChangeListener(customSeekBarListener);

        Button startActivityButton = findViewById(R.id.startActivityButton);
        startActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                valuePlayerName = findViewById(R.id.valueUserName);
                valueFruitTypes = findViewById(R.id.valueFruitType);
                valueGridSize = findViewById(R.id.valueGridSize);





                Intent intent = new Intent(getApplicationContext() , GameActivity.class);

                intent.putExtra("valuePlayerName" , valuePlayerName.getText().toString());
                intent.putExtra("valueFruitTypes" , valueFruitTypes.getText().toString());
                intent.putExtra("valueGridSize" , valueGridSize.getText().toString());

                startActivity(intent);
            }
        });

    }
}
