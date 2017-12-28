package com.example.android.minmaxagent.fruit;

import java.util.Random;

public class FruitUtils {

    private static final double HOLE_PROBABILITY = 0;

    static final Random RAND = new Random();

    /**
     * Alters the current grid in such a way that
     * all empty spaces rise to the top.
     */
    private static void gravitate(byte[][] grid, int n)
    {

        // Go column-wise
        for (int j = 0; j < n; j++)
        {

            for (int i = (n-1)-1; i >= 0; i--)
            {
                // No need to swap if the top element already contains EMPTY
                if (grid[i][j] == FruitNode.EMPTY)
                {
                    for(int k = i; k < n-1; k++)
                    {
                        grid[k][j] = grid[k+1][j];
                    }
                    grid[n-1][j] = FruitNode.EMPTY;
                }
            }
        }
    }

    static byte[][] createTestCase(int n, int p)
    {

        // Randomize the grid
        byte[][] gridNew = new byte[n][n];
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if(RAND.nextDouble() < HOLE_PROBABILITY)
                {
                    gridNew[i][j] = FruitNode.EMPTY;
                }
                else
                {
                    gridNew[i][j] = (byte)RAND.nextInt(p);
                }
            }
        }

        // Apply gravity
        gravitate(gridNew, n);

        if(FruitGame.DEBUG_MODE)
            System.out.println(gridStringPretty(gridNew));

        return gridNew;
    }

    static float nanosecondsToSeconds(long nanoseconds) {
        return nanoseconds / (1000.0f * 1000000);
    }

    static long secondsToNanoseconds(float seconds) {
        return (long) (seconds * 1000 * 1000000);
    }

    static String gridStringPretty(byte[][] grid)
    {
        int n = grid.length;

        StringBuilder sb = new StringBuilder();

        for(int j = 0; j < 2*n+1; j++) sb.append("-"); sb.append("\n");

        for(int i = n-1; i >= 0; i--) {

            for(int j = 0; j < n; j++) { if(j == 0) sb.append("|");
                if(grid[i][j] == FruitNode.EMPTY) sb.append(FruitNode.EMPTY_CHAR); else
                    sb.append(grid[i][j]); sb.append("|"); }

            sb.append("\n"); }

        for(int j = 0; j < 2*n+1; j++) sb.append("-");

        return sb.toString();
    }

    /**
     * Counts the number of squares with value EMPTY in an nxn byte grid.<br>
     * <br>
     * Helps compute the score difference.
     */
    static int numberOfEmptySquares(byte[][] grid)
    {
        int n = grid.length;

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
}
