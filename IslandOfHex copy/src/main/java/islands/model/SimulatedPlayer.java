package islands.model;

/**
 * A simulated player of Islands of Hex.
 */
public abstract class SimulatedPlayer {
    private static final int NS_PER_US = 1000;    // microseconds
    private static final int NS_PER_MS = 1000000;
    private static final int NS_PER_S = 1000000000;

    private long timeUsed = 0; // nanoseconds

    /**
     * Chooses the next move, keeping track of how long it took.
     *
     * @param model the model
     * @param tileColor the tile color of the current player
     * @return the move
     */
    public final RowColPair timeAndChooseNextMove(GameModel model, TileColor tileColor) {
        long startTime = System.nanoTime();
        RowColPair result = chooseNextMove(model, tileColor);
        timeUsed += System.nanoTime() - startTime;
        return result;
    }

    /**
     * Gets a string showing the amount of time taken by this simulator.
     *
     * @param color the color played by the simulator
     * @return a string suitable for display
     */
    public String getTimeSummary(TileColor color) {
        return String.format(
                "Time used by %s (%s): %,d μs\t%,d ms\t%,d s%n",
                getName(),
                color.getName(),
                timeUsed / NS_PER_US,
                timeUsed / NS_PER_MS,
                timeUsed / NS_PER_S);
    }

    /**
     * Chooses the next move to play from the current game position.
     *
     * @param model the model
     * @param tileColor the color played by the simulator
     * @return the move
     */
    public abstract RowColPair chooseNextMove(GameModel model, TileColor tileColor);

    /**
     * Gets a short unique name for this simulator, suitable for display in a
     * dropdown menu.
     *
     * @return a short name
     */
    public abstract String getName();
}
