package com.example.android.minmaxagent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public int BOARD_SIZE = 8;
    public int NUMBER_OF_FRUITS = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //GridLayout object
        final GridLayout grid = findViewById(R.id.baseGrid);

        //Random Board
        byte[][] board = FruitUtils.createTestCase(BOARD_SIZE, NUMBER_OF_FRUITS);


        //Set rowCount and columnCount for GridLayout
        grid.setRowCount(BOARD_SIZE);
        grid.setColumnCount(BOARD_SIZE);


        for(int i = 0 ; i < BOARD_SIZE ; i++){
            for(int j = 0 ; j < BOARD_SIZE ; j++){
                // Below defines LayoutParameters as not specified Column and Row
                // with grid:layout_columnWeight="1" and grid:layout_rowWeight="1"
                GridLayout.LayoutParams layoutParams   = new GridLayout.LayoutParams(
                        GridLayout.spec( GridLayout.UNDEFINED, 1f), GridLayout.spec( GridLayout.UNDEFINED, 1f));

                layoutParams.width = 0;    // Setting width to "0dp" so weight is applied instead
                layoutParams.height = 0;   // Setting height to "0dp" so height is applied instead

                //Creating new Button
                Button b = new Button(this);

                b.setLayoutParams(layoutParams);

                //Creating and setting Button ID for row and column using formula
                // rowID = row*10 + column
                int id = (i+1)*10 + (j+1);
                b.setId(id);

                //Setting Text
                String text = "" + board[i][j];
                b.setText(text);

                //Adding onclick Listener for buttons
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ButtonView gets selected for clicked button
                        int row = (int)  view.getId()/10;
                        int column = (int) view.getId() % 10;
                        String toastString = "Clicked Button has Row: " + row + " Column: " + column ;

                        //Displaying Row and Column number for clicked button as Toast on screen
                        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();

                        //TODO Instead of making a toast use this position
                        //TODO Pass clicked position and compute board and display new board on Grid
                        //TODO This new board should will be one after grabbing all similar fruits adjacent to clicked fruit
                        //TODo Apply gravity before displaying new Grid


                    }
                });

                grid.addView(b);
            }
        }
    }
}
