package com.example.android.minmaxagent.fruit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siddh on 12/16/2017.
 */

public class FruitNode implements Comparable<FruitNode> {

    /** Width and height of the square board (0 < n <= 26) */
    public static int n;

    /** Number of fruit types (0 < p <= 9) */
    public static int p;

    /** The value used for empty spaces on the grid */
    public static final byte EMPTY = -1;

    /** How the empty spaces are displayed */
    public static final char EMPTY_CHAR = '*';

    // Now begin the instance variables.

    /** Stores all the fruit positions. */
    public byte[][] grid;

    /**
     * Measures how deep the tree has become. Also specifies whether it is a MIN
     * or a MAX node; for a MAX node, the value of depth will be even (since it
     * starts from 0).
     */
    int depth;

    /**
     * Records the move that the parent node played to result in this child.
     */
    String moveFromParent;

    /**
     * Records the score gained by parent while generating this child.
     */
    int moveFromParentScore;

    /**
     * The utility value of the node.<br>
     * <br>
     * This value is only valid for terminal nodes. For any non-terminal node,
     * the utility value is always calculated by using Minimax.
     */
    int utilityPassedDown;

	/*
	 * FruitNode() { for(int i = 0; i < n; i++) for(int j = 0; j < n;
	 * j++) grid[i][j] = EMPTY;
	 *
	 * depth = 0; }
	 */

    FruitNode(byte[][] gridParam) {
        this.grid = gridParam;
        this.depth = 0;
        this.utilityPassedDown = 0;
        this.moveFromParent = "";
        this.moveFromParentScore = 0;
    }

    FruitNode(byte[][] gridParam, int depth, int utilityGain, String move, int score) {
        this.grid = gridParam;
        this.depth = depth;
        this.utilityPassedDown += utilityGain;
        // System.out.println("Utility increased by "+utilityGain + " -> " +
        // utilityPassedDown);
        this.moveFromParent = move;
        this.moveFromParentScore = score;
    }

    /**
     * Alters the current grid in such a way that all empty spaces rise to the
     * top.
     */
    void gravitate() {

        // Go column-wise
        for (int j = 0; j < n; j++)
        {

            for (int i = (n-1)-1; i >= 0; i--)
            {
                // No need to swap if the top element already contains EMPTY
                if (grid[i][j] == EMPTY)
                {
                    for(int k = i; k < n-1; k++)
                    {
                        grid[k][j] = grid[k+1][j];
                    }
                    grid[n-1][j] = EMPTY;
                }
            }
        }
    }

    /**
     * True if that node is a max node.<br>
     * <br>
     * This is decided by the depth of the game tree at that point; if it is
     * even, it's a max node.
     */
    boolean isMaxNode() {
        return depth % 2 == 0;
    }

    /**
     * Allow the use of Collections.sort to sort node objects.<br>
     * <br>
     * The sort order is used descending or max-nodes, ascending for min-nodes.
     */
    @Override
    public int compareTo(FruitNode otherNode) {
        //noinspection UnnecessaryBoxing
        return Integer.valueOf(this.moveFromParentScore).compareTo(Integer.valueOf(otherNode.moveFromParentScore));
    }

    /** String representation that contains the grid. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // sb.append("The grid looks like: \n");
        sb.append(this.gridString());

        return sb.toString();
    }

    /** Check if all spaces are empty. */
    boolean isTerminalNode() {

        // Check if any of the grid spaces contains a fruit
        for (int i = 0; i < FruitNode.n; i++) {
            for (int j = 0; j < FruitNode.n; j++) {
                if (this.grid[i][j] != FruitNode.EMPTY)
                    return false;
            }
        }

        return true;
    }

    /**
     * Return all the children for this node. Check all the possible moves -
     * each child corresponds to one move.
     */
    List<FruitNode> generateChildren()
    {

        List<FruitNode> children = new ArrayList<>();

        /**
         * Stores all the non-duplicated points that form a single group.
         * Initially empty.
         */
        List<List<FruitGridPoint>> groupPoints = new ArrayList<>();

        // Check all possible moves
        boolean[][] visited = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                visited[i][j] = false;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!visited[i][j]) {
                    int value = grid[i][j];
                    List<FruitGridPoint> currentGroup = new ArrayList<>();

                    markGroups(currentGroup, visited, i, j, value);

                    if (value != EMPTY)
                        groupPoints.add(currentGroup);

					/*
					 * if(FruitGame.DEBUG_MODE)
					 * System.out.format("%d square(s) in this group of %d's.\n"
					 * , currentGroup.size(), value);
					 */
                }
            }
        }

        // We now have all the points, upon selection of which a new child is
        // formed
		/*if (FruitGame.DEBUG_MODE)
			System.out.format("%d possible move(s) from this node.\n", groupPoints.size());*/

        // These will be ordered in the minimax call.
        for (List<FruitGridPoint> action : groupPoints)
        {
            // Copy the grid
            byte[][] childGrid = new byte[FruitNode.n][FruitNode.n];
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    childGrid[i][j] = grid[i][j];
                }
            }

            // Blank out this group in the grid
            for (FruitGridPoint point : action)
                childGrid[point.x][point.y] = FruitNode.EMPTY;

            /**
             * Record the score of this move by increasing utility - it should
             * be increased by n^2.
             */
            int utilityIncrease = action.size() * action.size();

            // if it is a Min-Node, move is opponent's, so make this negative
            if (!this.isMaxNode())
                utilityIncrease = -utilityIncrease;

            // Record which move was played
            String movePlayed = FruitGridPoint.pointToMoveString(action.get(0).x, action.get(0).y);

            // Create a new node with this configuration
            FruitNode child = new FruitNode(childGrid, this.depth + 1,
                    (this.utilityPassedDown + utilityIncrease), movePlayed, utilityIncrease);

            // Apply gravity
            child.gravitate();

            // add it to children! whoopee!
            children.add(child);
        }

        return children;
    }



    /**
     * Marks all the groups of matching fruits with a given 'value' as visited.<br>
     *     <br>
     *         Checks the grid value at current location - if it is equal to the value
     *         specified, and the cell has not been visited, the point is added to the
     *         current group, and the cell is marked as visited. The process is repeated
     *         for the surrounding 4 squares, recursively.
     */
    void markGroups(List<FruitGridPoint> currentGroup, boolean[][] visited, int i, int j, int value) {

        if (!visited[i][j] && grid[i][j] == value) {
            visited[i][j] = true;
            currentGroup.add(new FruitGridPoint(i, j, value));

            if (i < n - 1)
                markGroups(currentGroup, visited, i + 1, j, value);
            if (i > 0)
                markGroups(currentGroup, visited, i - 1, j, value);
            if (j < n - 1)
                markGroups(currentGroup, visited, i, j + 1, value);
            if (j > 0)
                markGroups(currentGroup, visited, i, j - 1, value);
        }

    }

    /** Returns a string representation required in the output. */
    String gridString() {
        StringBuilder sb = new StringBuilder();

        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                if (this.grid[i][j] == EMPTY)
                    sb.append(EMPTY_CHAR);
                else
                    sb.append(this.grid[i][j]);
            }

            if (i > 0)
                sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Returns a string representation like the one specified in the examples.
     */
	/*
	 * String gridStringPretty() { StringBuilder sb = new
	 * StringBuilder();
	 *
	 * for(int j = 0; j < 2*n+1; j++) sb.append("-"); sb.append("\n");
	 *
	 * for(int i = n-1; i >= 0; i--) {
	 *
	 * for(int j = 0; j < n; j++) { if(j == 0) sb.append("|");
	 * if(this.grid[i][j] == EMPTY) sb.append(EMPTY_CHAR); else
	 * sb.append(this.grid[i][j]); sb.append("|"); }
	 *
	 * sb.append("\n"); }
	 *
	 * for(int j = 0; j < 2*n+1; j++) sb.append("-");
	 *
	 * return sb.toString(); }
	 */
}