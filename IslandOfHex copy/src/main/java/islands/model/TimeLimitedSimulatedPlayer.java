package islands.model;

/**
 * A simulated player operating under time constraints.
 */
public abstract class TimeLimitedSimulatedPlayer extends SimulatedPlayer {
    /**
     * The number of milliseconds the player has to make its final move.
     * This may get changed.
     */
    public static final int MAX_MOVE_TIME_MS = 1000;

    /**
     * A listener to moves provided by a linked {@link TimeLimitedSimulatedPlayer}.
     */
    public static class Listener {
        private RowColPair move;

        /**
         * Receives the move provided by a {@link TimeLimitedSimulatedPlayer}.
         * The player can provide multiple moves in response to a single
         * call to {@link #makeMove(GameModel, TileColor, Listener)} The last
         * one before time runs out will be played.
         *
         * @param pair the board position of the move
         */
        public void receiveMove(RowColPair pair) {
            this.move = pair;
        }
    }

    /**
     * Decides the next move and reports it through
     * {@link Listener#receiveMove(RowColPair)}. The execution of this method
     * will be terminated after a specified amount of time {@link #MAX_MOVE_TIME_MS},
     * so it is important to report a move quickly, although improvements may be
     * made as long as time is available.
     *
     * @param model     the model describing the current game state
     * @param tileColor the color to be played
     * @param listener  the listener to provide moves to
     */
    public abstract void makeMove(GameModel model, TileColor tileColor, Listener listener);

    @Override
    public final RowColPair chooseNextMove(GameModel model, TileColor tileColor) {
        Listener listener = new Listener();
        Thread player = new Thread(() -> makeMove(model.deepCopy(), tileColor, listener));
        player.start();
        try {
            player.join(MAX_MOVE_TIME_MS);

            // Ask player to stop. It needs to check Thread.interrupted().
            // It doesn't benefit from ignoring an interrupt, since we will
            // no longer check the listener.
            player.interrupt();

            // Deprioritizes the thread, in case it is still running.
            player.setPriority(Thread.MIN_PRIORITY);
        } catch (InterruptedException e) {
            // cannot happen
        }
        if (listener.move == null) {
            throw new RuntimeException(
                    "Player failed to provide move in allotted time.");
        }
        System.out.println("Playing " + listener.move);
        return listener.move;
    }
}
