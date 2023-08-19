package islands.model.student;

import islands.model.GameModel;
import islands.model.Move;
import islands.model.TileColor;

/**
 * A player that uses caching to improve on the minimax algorithm.
 *
 * @see TranspositionTable
 */
public class CachingMinimaxPlayer extends MinimaxPlayer {
    private TranspositionTable table;

    public CachingMinimaxPlayer() {
        this.table = new TranspositionTable();
    }

    @Override
    public String getName() {
        return "Caching Minimax";
    }

    @Override
    public Move getMyMove(GameModel model, int depth, TileColor tileColor) {
        // if the move is in the table, it is returned
        if (table.hasMove(model, depth)) {
            return table.getMove(model, depth);
        } else {
            //if not, it calls MimimaxPlayer's getMyMove() method, adds the result to the table, and returns the result
            // to the caller.
            Move newMove = super.getMyMove(model, depth, tileColor);
            table.putMove(model, depth, newMove);
            return newMove;
        }
    }
}
