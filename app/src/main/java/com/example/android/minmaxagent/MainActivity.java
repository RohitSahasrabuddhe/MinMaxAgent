package com.example.android.minmaxagent;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public String PLAYER_NAME = "dummy";
    public int BOARD_SIZE = 7;
    public int NUMBER_OF_FRUITS = 9;
    private final int[] fruitColor = {
            R.color.colorFruitApple,
            R.color.colorFruitBanana,
            R.color.colorFruitGuava,
            R.color.colorFruitBlueberry,
            R.color.colorFruitStrawberry,
            R.color.colorFruitCyan,
            R.color.colorFruitLime,
            R.color.colorFruitOrange,
            R.color.colorFruitGrape};
    private final int[] fruitImageResource = {
            R.drawable.fruit_strawberry,
            R.drawable.fruit_bananas,
            R.drawable.fruit_grapes,
            R.drawable.fruit_pear,
            R.drawable.fruit_orange,
            R.drawable.fruit_apple,
            R.drawable.fruit_cherry,
            R.drawable.fruit_watermelon,
            R.drawable.fruit_lemon, };

    private final int fruitImageResourceEmpty =R.drawable.fruit_empty;

    private final int fruitColorEmpty = R.color.colorWhite;


    private FruitGame game;
    private ImageView btnGrid[][];
    private GridLayout baseGrid;
    private TextView tvTurnPlayer;
    private TextView tvScorePlayers[];
    private TextView tvPlayerName;

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
        // Refresh the score for all players
        for(int i = 0; i < game.players; i++)
        {
            tvScorePlayers[i].setText(String.valueOf(game.scores[i]));
        }

        // Refresh the turn player display
        tvTurnPlayer.setText(String.format(Locale.getDefault(),"Turn of P%d (%s)", game.turnPlayer, (game.isAI[game.turnPlayer])?"AI":PLAYER_NAME));

        // Refresh the fruit Grid - use the board of the game
        byte[][] board = game.node.grid;

        for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0; j < BOARD_SIZE; j++)
            {
                ImageView btnCurrent = findViewById(buttonLocationToID(i,j));

                int value = board[i][j];

                // Label and Color this button based on its fruit
                if(value == FruitNode.EMPTY) {
                    // btnCurrent.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, fruitColorEmpty)));
                    btnCurrent.setImageResource(fruitImageResourceEmpty);
                    // btnCurrent.setText(String.valueOf(FruitNode.EMPTY_CHAR));
                    btnCurrent.setOnClickListener(null);

                    // TODO should this be invisible or disabled? idk
                    btnCurrent.setVisibility(View.INVISIBLE);

                }
                else {
                    btnCurrent.setImageResource(fruitImageResource[value]);
                    // btnCurrent.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, fruitColor[value])));
                    // btnCurrent.setText(String.valueOf(value));
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent receivedIntent = getIntent();

        PLAYER_NAME = receivedIntent.getStringExtra("valuePlayerName");
        NUMBER_OF_FRUITS = Integer.parseInt(receivedIntent.getStringExtra("valueFruitTypes"));
        BOARD_SIZE = Integer.parseInt(receivedIntent.getStringExtra("valueGridSize"));


        tvPlayerName = findViewById(R.id.playerName);
        tvPlayerName.setText(PLAYER_NAME + " Score");

        // GridLayout object
        baseGrid = findViewById(R.id.baseGrid);

        // Start a new game.
        game = new FruitGame(BOARD_SIZE, NUMBER_OF_FRUITS);

        // Set rowCount and columnCount for GridLayout
        baseGrid.setRowCount(BOARD_SIZE);
        baseGrid.setColumnCount(BOARD_SIZE);

        tvTurnPlayer = findViewById(R.id.turnPlayer);

        tvScorePlayers = new TextView[game.players];
        tvScorePlayers[0] = findViewById(R.id.scoreP1);
        tvScorePlayers[1] = findViewById(R.id.scoreP2);

        // Buttons stored in a 2D grid allows for easy indexing
        btnGrid = new ImageView[BOARD_SIZE][BOARD_SIZE];

        for(int i = BOARD_SIZE-1 ; i >= 0 ; i--)
        // for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0 ; j < BOARD_SIZE ; j++)
            {

                // Creating new Button
                btnGrid[i][j] = new ImageView(this);

                final ImageView btnCurrent = btnGrid[i][j];

                // Creating and setting Button ID for row and column using formula
                // rowID = row*10 + column
                int btnId = buttonLocationToID(i, j);
                btnCurrent.setId(btnId);

                // Adding onclick Listener for buttons
                btnCurrent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        int x = (int)btnCurrent.getId() / 10 - 1;
                        int y = (int)btnCurrent.getId() % 10 - 1;

                        new GamePlayTask().execute(x, y);
                    }
                });

                // Below defines LayoutParameters as not specified Column and Row
                // with grid:layout_columnWeight="1" and grid:layout_rowWeight="1"
                GridLayout.LayoutParams layoutParams   = new GridLayout.LayoutParams(
                        GridLayout.spec( GridLayout.UNDEFINED, 1f), GridLayout.spec( GridLayout.UNDEFINED, 1f));

                layoutParams.width = 0;    // Setting width to "0dp" so weight is applied instead
                layoutParams.height = 0;   // Setting height to "0dp" so height is applied instead
                btnCurrent.setLayoutParams(layoutParams);
                baseGrid.addView(btnCurrent);

                /*gridLayout.addView(btnCurrent, new GridLayout.LayoutParams(
                        GridLayout.spec(1, GridLayout.CENTER),
                        GridLayout.spec(1, GridLayout.CENTER)));*/
            }
        }

        refreshFruits();
    }

    class GamePlayTask extends AsyncTask<Integer, Void, Boolean>
    {

        @Override
        protected void onPreExecute() {

            baseGrid.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Integer... ints) {

            // ButtonView gets selected for clicked button
            int x = ints[0];
            int y = ints[1];

            // Human takes a move
            if(FruitGame.DEBUG_MODE)
                System.out.printf("The selected cell is (%d, %d)\n", x, y);

            FruitNode humanResult = game.playHumanMove(x, y);
            if(humanResult != null) // only update game if move was valid.
            {
                game.node = humanResult;
            }
            else {
                System.out.println("Human made an invalid move?!?!");
            }

            // If the game can be stopped, stop it
            if(!game.advanceTurn())
                return false;

            publishProgress();

            // Sleep for a while
            SystemClock.sleep(1000);

            // AI automatically takes a move
            if (game.isAI[game.turnPlayer])
            {
                FruitNode aiResult = game.playAIMove();

                if(aiResult != null) // only update game if move was valid.
                {
                    game.node = aiResult;
                }
                else {
                    if(FruitGame.DEBUG_MODE) System.out.println("AI made an invalid move? TF!");
                }
            }

            publishProgress();

            return game.advanceTurn();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // Refresh the display
            refreshFruits();
        }

        @Override
        protected void onPostExecute(Boolean advanceable)
        {

            if(!advanceable)
            {
                int pWinner = game.winner();
                tvTurnPlayer.setText(String.format("P%d won the game!", pWinner));
            }
            else {
                baseGrid.setEnabled(true);
            }
        }
    }
}
