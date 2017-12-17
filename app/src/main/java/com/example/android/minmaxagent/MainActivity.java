package com.example.android.minmaxagent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public final int BOARD_SIZE = 6;
    public final int NUMBER_OF_FRUITS = 3;



    /**
     * An instance of the game.
     */
    private FruitGame game;

    /**
     * Converts a set of grid-coordinates to a String to use as button ID.
     */
    private int buttonLocationToID(int i, int j)
    {
        /*StringBuilder sb = new StringBuilder();
        sb.append((i+1)*10);
        sb.append(j+1);

        return sb.toString();*/

        return (i+1)*10 + j+1 ;
    }

    /**
     * Updates the text on each button to reflect the current fruit it holds.
     */
    private void refreshFruits()
    {
        // TODO Refresh the score for all players

        // Refresh the fruit Grid
        for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0; j < BOARD_SIZE; j++)
            {
                Button btnCurrent = findViewById(buttonLocationToID(i,j));

                // Use the board of the game
                byte[][] board = game.node.grid;

                // Setting Text
                String text =
                        (board[i][j] == FruitNode.EMPTY)?
                                String.valueOf(FruitNode.EMPTY_CHAR):
                                String.valueOf(board[i][j]);

                btnCurrent.setText(text);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // GridLayout object
        final GridLayout gridLayout = findViewById(R.id.baseGrid);

        // Start a new game.
        game = new FruitGame(BOARD_SIZE, NUMBER_OF_FRUITS);

        // Set rowCount and columnCount for GridLayout
        gridLayout.setRowCount(BOARD_SIZE);
        gridLayout.setColumnCount(BOARD_SIZE);

        // Buttons stored in a 2D grid allows for easy indexing
        Button btnGrid[][] = new Button[BOARD_SIZE][BOARD_SIZE];

        for(int i = BOARD_SIZE-1 ; i >= 0 ; i--)
        // for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0 ; j < BOARD_SIZE ; j++)
            {

                // Creating new Button
                btnGrid[i][j] = new Button(this);

                // TODO Just a temporary reference for convenience
                Button btnCurrent = btnGrid[i][j];

                // Creating and setting Button ID for row and column using formula
                // rowID = row*10 + column
                int btnId = buttonLocationToID(i, j);
                btnCurrent.setId(btnId);

                // Adding onclick Listener for buttons
                btnCurrent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // ButtonView gets selected for clicked button
                        int row = (int)  view.getId()/10;
                        int column = (int) view.getId() % 10;
                        String toastString = "Clicked Button has Row: " + row + " Column: " + column ;

                        //Displaying Row and Column number for clicked button as Toast on screen
                        Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();

                        // TODO Instead of making a toast use this position
                        // TODO Pass clicked position and compute board and display new board on Grid
                        // TODO This new board should will be one after grabbing all similar fruits adjacent to clicked fruit
                        // TODo Apply gravity before displaying new Grid
                    }
                });

                // Below defines LayoutParameters as not specified Column and Row
                // with grid:layout_columnWeight="1" and grid:layout_rowWeight="1"
                GridLayout.LayoutParams layoutParams   = new GridLayout.LayoutParams(
                        GridLayout.spec( GridLayout.UNDEFINED, 1f), GridLayout.spec( GridLayout.UNDEFINED, 1f));

                layoutParams.width = 0;    // Setting width to "0dp" so weight is applied instead
                layoutParams.height = 0;   // Setting height to "0dp" so height is applied instead
                btnCurrent.setLayoutParams(layoutParams);
                gridLayout.addView(btnCurrent);

                /*gridLayout.addView(btnCurrent, new GridLayout.LayoutParams(
                        GridLayout.spec(1, GridLayout.CENTER),
                        GridLayout.spec(1, GridLayout.CENTER)));*/
            }
        }

        refreshFruits();
    }
}
