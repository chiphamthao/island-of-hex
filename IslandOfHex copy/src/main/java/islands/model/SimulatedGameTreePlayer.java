package islands.model;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * A simulated player that uses a game tree to select moves.
 */
public abstract class SimulatedGameTreePlayer extends SimulatedPlayer {
    /**
     * Provides the play expected to lead to the result with the highest
     * value (as determined by {@link #getValue(GameModel, TileColor)}
     * for the current player. This usually simulates the opposing player by
     * calling {@link #getOpponentValue(GameModel, int, TileColor)}.
     *
     * @param model     the current game state
     * @param depth     the maximum number of moves ahead to search
     * @param tileColor the color of the player whose score we want to maximize
     * @return the results of making the best play to each board position
     */
    @VisibleForTesting
    public abstract Move getMyMove(GameModel model, int depth, TileColor tileColor);

    /**
     * Gives the expected value of the game state assuming the opponent gets
     * to choose the next move. This usually simulates the maximizing player
     * by calling {@link #getMyMove(GameModel, int, TileColor)}.
     *
     * @param model     the current game state
     * @param depth     the maximum number of moves ahead to search
     * @param tileColor the color of the player whose score we want to maximize
     * @return the expected value of the model for the maximizing player
     */
    @VisibleForTesting
    public abstract double getOpponentValue(GameModel model, int depth, TileColor tileColor);

    /**
     * Estimates the value of the provided game model to the specified player.
     *
     * @param model the model
     * @param tileColor the player color
     * @return the estimated or actual value of the model
     */
    @VisibleForTesting
    public double getValue(GameModel model, TileColor tileColor) {
        int difference = model.getScore(tileColor) - model.getScore(tileColor.getOpposite());
        if (model.isGameOver()) {
            return Integer.signum(difference) * model.getSize() * model.getSize();
        }
        return difference;
    }

    /**
     * Gets positions that moves can be played on (i.e., not covered by
     * a tile).
     *
     * @param model the model
     * @return legal positions
     */
    protected List<RowColPair> getLegalPositions(GameModel model) {
        List<RowColPair> legalPositions = new ArrayList<>();
        for (int row = 0; row < model.getSize(); row++) {
            for (int col = 0; col < model.getSize(); col++) {
                if (model.canPlay(row, col)) {
                   legalPositions.add(new RowColPair(row, col));
                }
            }
        }
        return legalPositions;
    }
}
