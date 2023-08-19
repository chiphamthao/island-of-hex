package islands.model.student;

import islands.model.GameModel;
import islands.model.Move;
import islands.model.RowColPair;
import islands.model.TileColor;

/**
 * A player that chooses the highest-scoring move based on the assumption
 * that the opponent will always choose randomly.
 */
public class RandomMaxPlayer extends MinimaxPlayer {
    @Override
    public String getName() {
        return "RandomMax";
    }

    @Override
    public double getOpponentValue(GameModel model, int depth, TileColor tileColor) {
        if (depth < 0) {
            throw new IllegalArgumentException();
        }
        if (depth == 0 || model.isGameOver()) {
            return getValue(model, tileColor.getOpposite());
        }

        double total = 0.0;
        int numMoves = 0;
        for (RowColPair position : getLegalPositions(model)) {
            GameModel childModel = model.deepCopy();
            numMoves++;
            childModel.makePlay(position.row(), position.column(), tileColor);
            Move childMove = getMyMove(childModel, depth - 1, tileColor.getOpposite());
            total += childMove.value();
        }
        return total / numMoves;
    }
}
