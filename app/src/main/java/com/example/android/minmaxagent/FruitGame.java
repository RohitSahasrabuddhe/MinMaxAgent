package com.example.android.minmaxagent;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * A single INSTANCE of the FruitRage game.
 *
 * @author Siddhesh Karekar
 */
public class FruitGame
{

    private float timeAlloted;
    private FruitNode initNode;
    private int turnPlayer = 0;
    private int players = 2;
    private boolean[] isAI = {false, true};

    /** If true, prints node information to the console. */
    static final boolean DEBUG_MODE = true;

    // private static String DEPTH_SEPARATOR = ".";

    /**
     * If the search space is sufficiently small, you might not require a
     * cutoff.<br>
     * <br>
     * For the IDS variant, this keeps on changing.
     */
    private int depth = 3;

    /**
     * Never explore beyond these many levels.
     */
    static final int MAX_DEPTH = 8;

    static String inputFileName = "input.txt";
    static String outputFileName = "output.txt";

    // Time variables

    /** System.nanoTime() values. */
    private long timeStart, timeCurrent;

    /**
     * The time at which we start iterative deepening.
     */
    private long timeMovesearchStart;

    /** Convert floating value read from input into precise-r nanoseconds */
    private long durAllotted;

    /**
     * These values get updated every time you check how much time has elapsed.
     */
    private long durElapsedSinceStart, durRemaining;

    private long durAllotToMove;

    // What if this happened when depth was 1?
    /** Must be initialized */
    private boolean timeLimitExceeded;

    /**
     * When these many nanoseconds (seconds * 10^9) are left, force the program
     * to return the move already selected or a random one.
     */
    // private static long durLimit = (long) (1.0f * 1000 * 1000000);

    // Other

    /** The initial values for alpha and beta */
    static final int INF = Integer.MAX_VALUE - 1;

    /** The move (child of root) that is finally picked as the go-to move. */
    private FruitNode bestChildSaved = null;

    /**
     * The move (child of root) that is picked if you're running out of time. A
     * legal child will be assigned to it..
     */
    private FruitNode fallbackChild = null;

    /** The utility of the finally picked go-to move. */
    private int bestChildUtilitySaved = -INF;



    /**
     * Reads the input from the text files in the format specified, and returns
     * a byte array corresponding to the initial grid.
     */
    FruitGame() {

        // TODO (2) These values shouldn't be fixed

        // Start reading input
        FruitNode.n = 6;
        // System.out.println("Grid size (n) is " + FruitNode.n + ".");

        FruitNode.p = 3;
        // System.out.println("Fruit types (p) are " + FruitNode.p + ".");

        float durSecondsAllotted = 5.0f;
        durAllotted = FruitUtils.secondsToNanoseconds(durSecondsAllotted);

        // System.out.println("Time remaining is " + durSecondsAllotted + " seconds.");

        // Randomly generate the Grid
        byte[][] gridInitial = FruitUtils.createTestCase(FruitNode.n, FruitNode.p);

        timeStart = System.nanoTime();

        timeLimitExceeded = false;

        // Create starting node
        initNode = new FruitNode(gridInitial);

        // System.out.println("\nStarting configuration: \n" + initNode + "\n");

        initNode.gravitate();
    }

    /**
     * Computes the utility value for any node.
     *
     * @return
     */
    private int minimaxValue(FruitNode node, int alpha, int beta, int cutoff) {

        int v;

        if (node.isTerminalNode()) {
			
			/*if (FruitGame.DEBUG_MODE) {
				for (int i = 0; i < node.depth; i++)
					System.out.print(DEPTH_SEPARATOR);

				System.out.format("%s value (terminal node) computed to be %d\n", (node.isMaxNode()) ? "Max" : "Min",
						node.utilityPassedDown);
			}*/

            return node.utilityPassedDown;
        }
        else {
            // Not a terminal node

            // Time-Cutoff condition
            long durElapsedOnMoveSoFar = System.nanoTime() - timeMovesearchStart;
            if (timeLimitExceeded || durElapsedOnMoveSoFar > durAllotToMove) {

                if (!timeLimitExceeded) {
					
					/*System.out.printf("Time limit exceeded mid-iteration - %.3f > %.3f.\n",
							nanosecondsToSeconds(durElapsedOnMoveSoFar), nanosecondsToSeconds(durAllotToMove));*/

                    timeLimitExceeded = true;

                    if(depth == 1)
                    {
                        emergencyExit();
                    }
                }
                return node.utilityPassedDown;
            }

            // Compute children
            List<FruitNode> children = node.generateChildren();

            // Sort children greedily to maximize cutoff
            if (node.isMaxNode())
                Collections.sort(children, Collections.reverseOrder());
            else
                Collections.sort(children);

            // Evaluation procedure for depth-cutoff
            if (node.depth >= cutoff) {
				
				/*if (FruitGame.DEBUG_MODE) {

					for (int i = 0; i < node.depth; i++)
						System.out.print(DEPTH_SEPARATOR);

					System.out.format("%s value (cutoff node) computed to be %d\n", (node.isMaxNode()) ? "Max" : "Min",
							node.utilityPassedDown);
				}*/

                return node.utilityPassedDown;
            } else {

                if (node.isMaxNode()) // Max node
                {
                    v = -INF;

                    for (FruitNode child : children) {
                        int result = minimaxValue(child, alpha, beta, cutoff);
                        v = Math.max(v, result);
                        if (v >= beta)
                        {
							
							/*if (FruitGame.DEBUG_MODE) {
								for (int i = 0; i < node.depth; i++)
									System.out.print(DEPTH_SEPARATOR);

								System.out.println("Max value (pruned) computed to be " + v);
							}*/

                            return v;
                        }
                        alpha = Math.max(v, alpha);
                    }

                } else // Min node
                {
                    v = +INF;

                    for (FruitNode child : children) {
                        int result = minimaxValue(child, alpha, beta, cutoff);
                        v = Math.min(v, result);
                        if (v <= alpha)
                        {
							/*if (FruitGame.DEBUG_MODE) {

								for (int i = 0; i < node.depth; i++)
									System.out.print(DEPTH_SEPARATOR);

								System.out.println("Min value (pruned) computed to be " + v);
							}*/

                            return v;
                        }
                        beta = Math.min(v, beta);
                    }
                }
            }

        }

		/*if (FruitGame.DEBUG_MODE) {

			for (int i = 0; i < node.depth; i++)
				System.out.print(DEPTH_SEPARATOR);

			System.out.println("Minimax value computed to be " + v);
		}*/

        return v;
    }

    /**
     * Pick a certain (random?) move if you're running out of time fast.
     */
    private void emergencyExit() {

        System.out.println("Emergency exit! A random move will be chosen.\n");
        bestChildSaved = fallbackChild;
        bestChildUtilitySaved = fallbackChild.moveFromParentScore;

    }

    /**
     * Updates timeCurrent and durElapsedSinceStart.<br>
     * <br>
     * Also prints the elapsed time in seconds.
     *
     */
    private void updateRemainingTime() {
        timeCurrent = System.nanoTime();

        durElapsedSinceStart = timeCurrent - timeStart;
        durRemaining = durAllotted - durElapsedSinceStart;

		/*if (DEBUG_MODE)
			System.out.println("Elapsed time: " + nanosecondsToSeconds(durElapsedSinceStart) + " seconds."); */
    }

    /**
     * Goes forward to the next turn.
     */
    private void advanceTurn() {
        turnPlayer = (turnPlayer+1)%players;

        if(DEBUG_MODE) System.out.printf("Player %d [%s]'s turn.\n", turnPlayer, ((isAI[turnPlayer])?"AI":"Human"));
    }

    /**
     * Play a turn of the game using the AI.
     */
    private void playAITurn()
    {

        List<FruitNode> children = initNode.generateChildren();

        // Assign a random legal move in case time runs out - this appears to be slower
        // fallbackChild = children.get(new Random().nextInt(children.size()));

        // Maximize pruning?
        Collections.sort(children, Collections.reverseOrder());

        // Assign the greediest legal move in case time runs out.
        fallbackChild = children.get(0);

        FruitNode bestChild;
        int bestChildUtility;

        // Check if no children exist!
        if (!children.isEmpty()) {

            // Check time remaining
            updateRemainingTime();
            // System.out.printf("Remaining time is %.3f seconds.\n", nanosecondsToSeconds(durRemaining));

            // Approximately calculate how much time the depth-searching in
            // TOTAL should take.
            durAllotToMove = durRemaining / children.size() * 2;

                /*System.out.printf("The move search for all depths is allowed to be %.3f seconds.\n",
                        nanosecondsToSeconds(durAllotToMove));*/

            timeMovesearchStart = System.nanoTime();

            // depth is global
            for (depth = 1; depth < MAX_DEPTH; depth++) {
                // System.out.println("\nGoing to depth " + depth + ".");

                bestChild = null;
                bestChildUtility = -INF;

                for (int i = 0; i < children.size(); i++) {
                    FruitNode currentChild = children.get(i);

                    int currentChildUtility = minimaxValue(currentChild, -INF, +INF, depth);

                    if (currentChildUtility > bestChildUtility) {
                        bestChild = currentChild;
                        bestChildUtility = currentChildUtility;
                    }
                }

                if (!timeLimitExceeded)
                {
                    bestChildSaved = bestChild;
                    bestChildUtilitySaved = bestChildUtility;

                        /*System.out.println("At this depth, max value (root) is " + bestChildUtilitySaved + " given by "
                                + bestChildSaved.moveFromParent + " (" + bestChildSaved.moveFromParentScore
                                + " pts).\n");*/

                    long durIterationsSoFar = System.nanoTime() - timeMovesearchStart;
                        /*System.out.printf("So far, depth searching took %.3f seconds.\n",
                                nanosecondsToSeconds(durIterationsSoFar));*/

                    if (durIterationsSoFar * (depth) > durAllotToMove) {
                        // System.out.println("Searching the next depth will be too expensive.");
                        break;
                    }

                }
                else {
                    break;
                }

            }

            if (timeLimitExceeded && depth > 1)
                depth -= 1;

            System.out.println("Final max value (root) is " + bestChildUtilitySaved + " given by "
                    + bestChildSaved.moveFromParent + " (" + bestChildSaved.moveFromParentScore
                    + " pts) for cutoff " + depth + ".");
        }
        // Find the move to perform
        else {
            // moveToPlay = "";
            // Handle this in finish(FruitNode)
        }

        // The solution is in BestChildSaved
        initNode = bestChildSaved;

        if(DEBUG_MODE) System.out.println("\nCurrently board is:\n"+initNode);

        advanceTurn();

    }


    /**
     * Plays the game until someone wins. start with random player
     * @return
     */
    int play()
    {
        int winner = -1;

        while (!initNode.isTerminalNode()) {

            if (isAI[turnPlayer]) {
                playAITurn();
            }

            // If it's a human player
            else {
                Scanner in = new Scanner(System.in);

                System.out.print("Enter the move you want to make (x y): ");
                // int x = in.nextInt();
                // int y = in.nextInt();

                // TODO (1) Add code to finish this move

                in.close();

                advanceTurn();
            }

        }

        return winner;
    }

    public static void main(String[] args) {

        FruitGame game = new FruitGame();

        System.out.println(game.play()+" won the game.");

    }

}

