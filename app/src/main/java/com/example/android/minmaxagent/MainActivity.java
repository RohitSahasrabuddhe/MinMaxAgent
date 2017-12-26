package com.example.android.minmaxagent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    // TODO (9) Multi (>2) player support - maybe show only active player?

    public String PLAYER_NAME = "Dummy";
    public int BOARD_SIZE = 6;
    public int NUMBER_OF_FRUITS = 4;

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

    // COMPLETED (2) Empty grid cells don't seem to display correctly
    private final int fruitImageResourceEmpty = R.drawable.fruit_empty;

    private FruitGame game;
    private ImageView ivFruitGrid[][];
    private GridLayout glBaseGrid;
    private TextView tvTurnPlayer;
    private TextView tvScorePlayers[];
    private TextView tvPlayerName;

    /**
     * Converts a set of grid-coordinates to a String to use as button ID.
     */
    private int fruitLocationToID(int i, int j)
    {
        /*StringBuilder sb = new StringBuilder();
        sb.append((i+1)*10);
        sb.append(j+1);

        return sb.toString();*/

        return (i+1)*10 + j+1 ;
    }

    // TODO Bug (B1) AI score updated after a delay

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
        tvTurnPlayer.setText(String.format(Locale.getDefault(),"%s's Turn", game.playerNames[game.turnPlayer]));

        // Refresh the fruit Grid - use the board of the game
        byte[][] board = game.node.grid;

        for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0; j < BOARD_SIZE; j++)
            {
                ImageView ivFruitCurrent = findViewById(fruitLocationToID(i,j));

                int value = board[i][j];

                // Label and Color this button based on its fruit
                if(value == FruitNode.EMPTY)
                {
                    // btnCurrent.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, fruitColorEmpty)));
                    ivFruitCurrent.setImageResource(fruitImageResourceEmpty);
                    // btnCurrent.setText(String.valueOf(FruitNode.EMPTY_CHAR));
                    ivFruitCurrent.setOnClickListener(null);

                    // Should this be invisible or disabled? idk
                    // ivFruitCurrent.setVisibility(View.INVISIBLE);

                }
                else {
                    ivFruitCurrent.setImageResource(fruitImageResource[value]);
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
        if(receivedIntent != null) {

            String pName = receivedIntent.getStringExtra("valuePlayerName");

            if(!TextUtils.isEmpty(pName))
                PLAYER_NAME = pName;

            String noOfFruits = receivedIntent.getStringExtra("valueFruitTypes");

            if(!TextUtils.isEmpty(noOfFruits))
                NUMBER_OF_FRUITS = Integer.parseInt(noOfFruits);

            String boardSize = receivedIntent.getStringExtra("valueGridSize");

            if(!TextUtils.isEmpty(boardSize))
                BOARD_SIZE = Integer.parseInt(boardSize);
        }


        tvPlayerName = findViewById(R.id.playerName);
        tvPlayerName.setText(PLAYER_NAME);

        // GridLayout object
        glBaseGrid = findViewById(R.id.baseGrid);

        // Start a new game.
        String[] pNames = {PLAYER_NAME , "AI"};
        game = new FruitGame(BOARD_SIZE, NUMBER_OF_FRUITS, pNames);

        // Set rowCount and columnCount for GridLayout
        glBaseGrid.setRowCount(BOARD_SIZE);
        glBaseGrid.setColumnCount(BOARD_SIZE);

        tvTurnPlayer = findViewById(R.id.turnPlayer);

        tvScorePlayers = new TextView[game.players];
        tvScorePlayers[0] = findViewById(R.id.scoreP1);
        tvScorePlayers[1] = findViewById(R.id.scoreP2);

        // Buttons stored in a 2D grid allows for easy indexing
        ivFruitGrid = new ImageView[BOARD_SIZE][BOARD_SIZE];

        for(int i = BOARD_SIZE-1 ; i >= 0 ; i--)
        // for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0 ; j < BOARD_SIZE ; j++)
            {

                // Creating new Button
                ivFruitGrid[i][j] = new ImageView(this);

                final ImageView ivFruitCurrent = ivFruitGrid[i][j];

                // Creating and setting Button ID for row and column using formula
                // rowID = row*10 + column
                int btnId = fruitLocationToID(i, j);
                ivFruitCurrent.setId(btnId);

                // Adding onclick Listener for buttons
                ivFruitCurrent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        int x = (int)ivFruitCurrent.getId() / 10 - 1;
                        int y = (int)ivFruitCurrent.getId() % 10 - 1;

                        // TODO (1) Sound can be played when fruit is selected
                        // TODO (6) Animation, sparkle when fruit is selected
                        // TODO (19) Animation, Gravity when fruits drop

                        new GamePlayTask().execute(x, y);
                    }
                });

                // Below defines LayoutParameters as not specified Column and Row
                // with grid:layout_columnWeight="1" and grid:layout_rowWeight="1"
                GridLayout.LayoutParams layoutParams   = new GridLayout.LayoutParams(
                        GridLayout.spec( GridLayout.UNDEFINED, 1f), GridLayout.spec( GridLayout.UNDEFINED, 1f));

                layoutParams.width = 0;    // Setting width to "0dp" so weight is applied instead
                layoutParams.height = 0;   // Setting height to "0dp" so height is applied instead
                ivFruitCurrent.setLayoutParams(layoutParams);
                glBaseGrid.addView(ivFruitCurrent);

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

            glBaseGrid.setEnabled(false);
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

            // TODO (16) Finish the game, Display Winner and go back to new menu screen
            // Continue game if further playable
            if(game.advanceTurn()) {

                publishProgress();

                // TODO (12) AI "thinking" alloted time, avoid hardcoded sleeping
                SystemClock.sleep(1000);

                // AI automatically takes a move
                if (game.isAI[game.turnPlayer]) {
                    FruitNode aiResult = game.playAIMove();

                    if (aiResult != null) // only update game if move was valid.
                    {
                        game.node = aiResult;
                    } else {
                        if (FruitGame.DEBUG_MODE)
                            System.out.println("AI made an invalid move? TF!");
                    }
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
                String playerWinner = game.winner();

                tvTurnPlayer.setText(String.format(Locale.getDefault(), "%s won the game!", playerWinner));



            }
            else {
                glBaseGrid.setEnabled(true);
            }
        }
    }
}
