
package com.example.android.minmaxagent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Pits one FruitGame program against another.
 * 
 * @author Siddhesh Karekar
 */
public class AgentPlayer {

	private static final boolean REQUIRE_KEY_PRESS = false;
	
	/** If it is not 0 or 1, starting player is random. */
	private static int startPlayerOverride = 1;
	
	/**
	 * Player scores. Determines who wins the game.
	 */
	private static int scores[] = {0, 0};
	private static float times[] = {0, 0};
	
	private static final String names[] = {"Siddhesh1", "Siddhesh2" };

	/** System.nanoTime() values. */
	private static long timeCurrent;

	private static long durNanosecondsLastMove;

	/** Convert above floating value into precise-r nanoseconds */
	private static long durNanosecondsAllotted;

	/** Width and height of the square board (0 < n <= 26) */
	private static int n;

	/** Number of fruit types (0 < p <= 9) */
	private static int p;

	/** The copy of the grid that the referee keeps to check score changes. */
	private static byte[][] gridReferee;
	
	/** The copy of the grid that is used to restore the grid when the program terminates. */
	private static byte[][] gridBackup;

	/** Is it player 1 or player 2 (0 or 1 in implementation) */
	private static int turn;

	/**
	 * Since the original grid gets butchered in the game process, this function
	 * restores it to the one before the program began execution.
	 */
	private static void restoreBackup()
	{
		PrintWriter writerInput = null;
		try {

			writerInput = new PrintWriter(FruitGame.inputFileName, "UTF-8");

			System.out.println("\nRestoring old input backup to file:");

			System.out.println(n);
			writerInput.println(n);

			System.out.println(p);
			writerInput.println(p);

			float durSecondsAllotted = FruitUtils.nanosecondsToSeconds(durNanosecondsAllotted);
			System.out.format("%.3f\n", durSecondsAllotted);
			writerInput.format("%.3f", durSecondsAllotted);
			writerInput.println();

			printGridToFileAndConsole(gridBackup, writerInput);
			
			System.out.println("Restore finished.");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(writerInput != null)
				writerInput.close();
		}
	}
	
	/**
	 * Prints a 2D byte array to the input file and also displays it in the console
	 * @param grid
	 * @param writer
	 */
	private static void printGridToFileAndConsole(byte[][] grid, PrintWriter writer)
	{
		// Print this grid to the file and console
		for(int i = n-1; i >= 0 ; i--)
		{
			for(int j = 0; j < n; j++)
			{
				if(grid[i][j] == FruitNode.EMPTY)
				{
					System.out.print(FruitNode.EMPTY_CHAR);
					writer.print(FruitNode.EMPTY_CHAR);
				}
				else {
					System.out.print(grid[i][j]);
					writer.print(grid[i][j]);
				}
			}
			System.out.println();
			writer.println();
		}
	}

	/**
	 * Initializes the values of n, p, duration alloted and the starting grid.
	 */
	private static void readInputInitialize(Scanner inInput) {

		n = Integer.parseInt(inInput.nextLine());
		p = Integer.parseInt(inInput.nextLine());
		float durSecondsAllotted = Float.parseFloat(inInput.nextLine());
		durNanosecondsAllotted = FruitUtils.secondsToNanoseconds(durSecondsAllotted);
		
		// System.out.format("%d, %d, %f\n", n, p, durSecondsAllotted);

		// Read the grid
		gridReferee = new byte[n][n];
		gridBackup = new byte[n][n];
		
		for(int i = n - 1; i >=0 ; i--)
		{
			String row = inInput.nextLine();
			for(int j = 0; j < n; j++)
			{
				char ch = row.charAt(j);

				if(ch == '*')
					gridBackup[i][j] = gridReferee[i][j] = FruitNode.EMPTY;
				else
					gridBackup[i][j] = gridReferee[i][j] = (byte)(ch - '0');
			}
		}
	}

	/**
	 * Values of n, p, and gridReferee must be initialized first.
	 * 
	 * @return true if the game is over (i.e., output is blank).
	 */
	private static boolean playUntilGameOver()
	{	
		try {
			play();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		PrintWriter writerInput = null;
		try {

			// condition for an empty board
			if(numberOfEmptySquares(gridReferee) == n*n)
			{
				System.out.println("\nTHE GAME IS ALREADY OVER!");
				System.out.format("\nScoreboard: %s - %d (%.3f) | %s - %d (%.3f)\n\n",
						names[0], scores[0], times[0], names[1],
						scores[1], times[1]);

				return false;
			} 

			else 
			{
				writerInput = new PrintWriter(FruitGame.inputFileName, "UTF-8");

				System.out.println("\nNew input printed to file:");

				System.out.println(n);
				writerInput.println(n);

				System.out.println(p);
				writerInput.println(p);
				
				long timeCurrentOld = timeCurrent;
				timeCurrent = System.nanoTime();
				durNanosecondsLastMove = timeCurrent - timeCurrentOld;
				// The total time taken by turn player so far
				times[turn] += FruitUtils.nanosecondsToSeconds(durNanosecondsLastMove);

				// Print remaining time
				System.out.format("%.3f\n", FruitUtils.nanosecondsToSeconds(durNanosecondsAllotted) - times[turn]);
				writerInput.format("%.3f", FruitUtils.nanosecondsToSeconds(durNanosecondsAllotted) - times[turn]);
				writerInput.println();

				// read the grid from output file
				Scanner inOutput = new Scanner(new File(FruitGame.outputFileName));

				// Consume the line containing the winning move
				inOutput.nextLine();

				// The next n lines contain the grid
				byte[][] gridNew = new byte[n][n];
				for(int i = n-1; i >= 0 ; i--)
				{
					String row = inOutput.nextLine();
					for(int j = 0; j < n; j++)
					{
						char ch = row.charAt(j);

						if(ch == FruitNode.EMPTY_CHAR)
							gridNew[i][j] = FruitNode.EMPTY;
						else
							gridNew[i][j] = (byte)(ch - '0');
					}
				}

				printGridToFileAndConsole(gridNew, writerInput);

				// Update the score for player
				int newEmpty = numberOfEmptySquares(gridNew);
				int oldEmpty = numberOfEmptySquares(gridReferee);
				int scoreDifference = (int)(Math.pow(newEmpty-oldEmpty, 2));
				if(newEmpty < oldEmpty)
				{
					System.err.println("Player "+names[turn]+" might be adding new squares!");
					scoreDifference = -scoreDifference;
				}
				scores[turn] += scoreDifference;

				// Make this the new referee grid
				gridReferee = gridNew;

				inOutput.close();
				
				// Switch turn
				turn = (turn + 1)%2;
			}


		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(writerInput != null)
				writerInput.close();
		}

		return true;
	}

	/**
	 * Counts the number of squares with value EMPTY in an nxn byte grid.<br>
	 * <br>
	 * Helps compute the score difference.
	 */
	private static int numberOfEmptySquares(byte[][] grid)
	{
		int count = 0;
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				if(grid[i][j] == FruitNode.EMPTY)
				{
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Your selected move, represented as two characters: A letter from A to Z
	 * representing the column number (where A is the leftmost column, B is the
	 * next one to the right, etc), and A number from 1 to 26 representing the
	 * row number (where 1 is the top row, 2 is the row below it, etc).
	 */
	
	/*private static FruitGridPoint moveStringToPoint(String move)
	{
		char column = move.charAt(0);
		int y = column - 'A';

		String row = ""+move.charAt(1);
		int x = FruitNode.n - Integer.parseInt(row);

		// By default, let it be empty
		int val = -1;

		return new FruitGridPoint(x, y, val);
	}*/
	
	private static void play()
	{
		System.out.println("\n----------[ Player "+names[turn]+" ]----------\n");
		
		if(turn == 0) {
			FruitGame.main(new String[] {});
		}
		else {
			FruitGame.main(new String[] {});
		}
	}

	/**
	 * The main method.
	 */
	public static void main(String args[])
	{
		try
		{
			Scanner in = new Scanner(System.in);
			Scanner inInput = new Scanner(new File(FruitGame.inputFileName));

			// Randomize first player
			if(startPlayerOverride == 0 || startPlayerOverride == 1)
			{
				turn = startPlayerOverride;
			}
			else
			{
				if(Math.random() > 0.5)
					turn = 1;
				else
					turn = 0;
			}
			
			System.out.format("Player %s goes first.\n", (names[turn]));

			// update n, p, durNanosecondsAllotted, gridReferee
			readInputInitialize(inInput);
			
			timeCurrent = System.nanoTime();
			

			// Update scores
			while(playUntilGameOver())
			{
				
				System.out.format("\nScoreboard: %s - %d (%.3f) | %s - %d (%.3f)\n",
						names[0], 
						scores[0], times[0], names[1],
						scores[1], times[1]);

				if(REQUIRE_KEY_PRESS)
				{
					System.out.print("\nPress Enter to continue: ");
					in.nextLine();
					System.out.println();
				}
				
			}		
			
			restoreBackup();

			in.close();
		}
		catch(FileNotFoundException fne)
		{
			fne.printStackTrace();
		}
	}

}

