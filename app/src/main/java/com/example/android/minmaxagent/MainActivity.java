package com.example.android.minmaxagent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;



public class MainActivity extends AppCompatActivity {

    // Test commit
    // Test Commit by Rohit

    public int BOARD_SIZE = 20;
    public int NUMBER_OF_FRUITS = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final GridLayout grid = findViewById(R.id.baseGrid);

        byte[][] board = FruitUtils.createTestCase(BOARD_SIZE, NUMBER_OF_FRUITS);



        grid.setRowCount(BOARD_SIZE);
        grid.setColumnCount(BOARD_SIZE);


        for(int i = 0 ; i < BOARD_SIZE ; i++){
            for(int j = 0 ; j < BOARD_SIZE ; j++){


                /*
                Button b = new Button(this);
                //int idealChildWidth = (int) ((grid.getWidth()-20*grid.getColumnCount())/grid.getColumnCount());
                int idealChildWidth = (int) (grid.getWidth()/BOARD_SIZE);
                b.setWidth(idealChildWidth);*/


                GridLayout.LayoutParams lParams   = new GridLayout.LayoutParams( GridLayout.spec( GridLayout.UNDEFINED, 1f), GridLayout.spec( GridLayout.UNDEFINED, 1f));
                // The above defines LayoutParameters as not specified Column and Row with grid:layout_columnWeight="1" and grid:layout_rowWeight="1"
                lParams.width = 0;    // Setting width to "0dp" so weight is applied instead
                lParams.height = 0;
                Button b = new Button(this);
                String text = "" + board[i][j];
                b.setText(text);
                b.setLayoutParams(lParams);


                grid.addView(b);
            }
        }
    }
}
