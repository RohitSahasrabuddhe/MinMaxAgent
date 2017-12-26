package com.example.android.minmaxagent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    // TODO (7) Persistent preferences - next new game has these default settings

    EditText valuePlayerName, valueFruitTypes, valueGridSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
