package com.example.android.minmaxagent;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.minmaxagent.db.DBHandler;
import com.example.android.minmaxagent.db.Profile;
import com.example.android.minmaxagent.fruit.FruitGame;
import com.example.android.minmaxagent.fruit.FruitNode;

import java.util.Locale;


public class GameActivity extends AppCompatActivity {

    public String PLAYER_NAME = "Dummy";
    public String userName;
    public int BOARD_SIZE = 6;
    public int NUMBER_OF_FRUITS = 4;

    Intent receivedIntent;

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

    // TODO Back button; modify to go to appropriate screen

    // COMPLETED Empty grid cells don't seem to display correctly
    private final int fruitImageResourceEmpty = R.drawable.fruit_empty;

    private FruitGame game;
    private ImageView ivFruitGrid[][];
    private GridLayout glBaseGrid;
    private TextView tvTurnPlayer;
    private TextView tvScorePlayers[];
    private TextView tvPlayerName;

    private MediaPlayer mpSound;

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

    // COMPLETED Bug (B1) AI score updated after a delay

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
        refreshFruitGridOnly();
    }

    private void refreshFruitGridOnly()
    {

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

    private int gethScoreDifference(FruitGame game) {
        int difference;

        difference = game.scores[1] - game.scores[0];

        if(difference < 0){
            difference *= -1;
        }
        return  difference;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        receivedIntent = getIntent();

        if(receivedIntent != null) {
            String pName = receivedIntent.getStringExtra("UserName");

            if(!TextUtils.isEmpty(pName)) {
                userName = pName;

                DBHandler dbHandler= new DBHandler(this);
                Profile profile = dbHandler.getProfileWithName(userName);


                PLAYER_NAME = userName;
                NUMBER_OF_FRUITS = profile.getFruitType();
                BOARD_SIZE = profile.getGridSize();

                Toast toast=Toast.makeText(getApplicationContext(),"Profile: " + profile,Toast.LENGTH_LONG);
                toast.setMargin(50,50);
                toast.show();
            }

            String playerNameIfChanged = receivedIntent.getStringExtra("PlayerName");

            if(!TextUtils.isEmpty(playerNameIfChanged))
                PLAYER_NAME = playerNameIfChanged;


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

        // Initialize sound
        mpSound = MediaPlayer.create(this, R.raw.fruit);

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

                // Add frame and background
                ivFruitCurrent.setBackgroundResource(R.drawable.grid_frame);

                /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(4, 4, 4, 4);
                ivFruitCurrent.setLayoutParams(lp);*/

                // TODO Bug (B3) padding doesn't work properly same value returned
                ivFruitCurrent.measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                int padding = ivFruitCurrent.getMeasuredWidth() / 6;

                if(FruitGame.DEBUG_MODE)
                    System.out.printf("Dimensions = %dx%d, .: Padding = %d.\n",
                            ivFruitCurrent.getMeasuredWidth(),
                            ivFruitCurrent.getMeasuredHeight(),
                            padding);

                ivFruitCurrent.setPadding(padding,padding,padding,padding);

                final Context c = this;

                // Adding onclick Listener for buttons
                ivFruitCurrent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        int x = (int)ivFruitCurrent.getId() / 10 - 1;
                        int y = (int)ivFruitCurrent.getId() % 10 - 1;

                        // COMPLETED (1) Sound can be played when fruit is selected
                        // Play a sound
                        mpSound.start();

                        new GamePlayTask(c).execute(x, y);
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

    /**
     * Sets the enabled property for a View AND all its descendants<br>
     * (Android does not do this by default.)<br>
     * <br>
     * Reference:
     * stackoverflow.com/questions/19224560/view-setenabledfalse-does-not-work-quite-right-in-android
     */
    public static void setEnabledAll(View v, boolean enabled) {
        v.setEnabled(enabled);
        v.setFocusable(enabled);

        if(v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++)
                setEnabledAll(vg.getChildAt(i), enabled);
        }
    }

    // TODO Bug Score calculation buggy

    class GamePlayTask extends AsyncTask<Integer, Void, Boolean>
    {

        Context context;

        GamePlayTask(Context c)
        {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            setEnabledAll(glBaseGrid, false);
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

            // Continue game if further playable
            if(game.advanceTurnIfPossible()) {

                publishProgress();

                // TODO (12) AI "thinking" alloted time, avoid hardcoded sleeping
                SystemClock.sleep(1000);

                // AI automatically takes a move
                if (game.isAI[game.turnPlayer])
                {
                    FruitNode aiResult = game.playAIMove();

                    // Play a sound - cancel currently playing sound if it already is playing
                    /*if (mpSound.isPlaying()) {
                        mpSound.stop();
                        mpSound.release();
                        mpSound = MediaPlayer.create(context, R.raw.fruit);
                    }*/
                    mpSound.start();

                    if (aiResult != null) // only update game if move was valid.
                    {
                        game.node = aiResult;
                    } else {
                        if (FruitGame.DEBUG_MODE)
                            System.out.println("AI made an invalid move? TF!");
                    }
                }
            }


            return game.advanceTurnIfPossible();


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

                refreshFruits();

                int scoreDifference = gethScoreDifference(game);

                tvTurnPlayer.setText(String.format(Locale.getDefault(), "%s won the game!", playerWinner));


                Intent intentGameOver = new Intent(getApplicationContext() , GameOverActivity.class);
                intentGameOver.putExtra("UserName", userName);
                intentGameOver.putExtra("Winner",playerWinner);
                intentGameOver.putExtra("ScoreDifference",""+scoreDifference);
                intentGameOver.putExtra("PlayerName",PLAYER_NAME);
                startActivity(intentGameOver);


            }
            else {

                publishProgress();

                setEnabledAll(glBaseGrid, true);


            }
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentMainMenu = new Intent(getApplicationContext(),MainMenuActivity.class);
        intentMainMenu.putExtra("UserName" , userName);
        intentMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMainMenu);
        finish();
    }
}
