package com.example.android.minmaxagent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;



public class MainActivity extends AppCompatActivity {

    // Test commit
    // Test Commit by Rohit

    public int BOARD_SIZE = 8;
    public int NUMBER_OF_TREES = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final GridLayout grid = (GridLayout) findViewById(R.id.baseGrid);

        byte[][] board = FruitUtils.createTestCase(8 , 5);


        grid.setRowCount(BOARD_SIZE);
        grid.setColumnCount(BOARD_SIZE);

        for(int i = 0 ; i < BOARD_SIZE ; i++){
            for(int j = 0 ; j < BOARD_SIZE ; j++){
                Button b = new Button(this);
                b.setText(" " + board[i][j]);
                grid.addView(b);
            }
        }
    }
}
