package islands.model.student;

import islands.model.*;

/**
 * A player applying alpha-beta pruning to the minimax algorithm.
 */
public class AlphaBetaPlayer extends MinimaxPlayer {
    private final TranspositionTable table = new TranspositionTable();

    @Override
    public String getName() {
        return "Alpha-Beta";
    }

    @Override
    public RowColPair chooseNextMove(GameModel model, TileColor tileColor) {
        Move move = getMyMove(model, MAX_DEPTH, -Double.MAX_VALUE, Double.MAX_VALUE, tileColor);
        return move.getPosition();
    }

    // Enables TournamentPlayer to select depth of search.
    public RowColPair chooseNextMove(GameModel model, int depth, TileColor tileColor) {
        Move move = getMyMove(model, depth, -Double.MAX_VALUE, Double.MAX_VALUE, tileColor);
        return move.getPosition();
    }

    // This doesn't override the ordinary getMyMove() method because it adds
    // alpha and beta parameters.
    private Move getMyMove(GameModel model, int depth, double alpha, double beta, TileColor tileColor) {
        if (depth < 0) {
            throw new IllegalArgumentException();
        }
        if (depth == 0 || model.isGameOver()) {
            return new Move(getValue(model, tileColor));
        }
        Move bestMove = null;
        double childValue;

        for (RowColPair position : getLegalPositions(model)) {
            GameModel newModel = model.deepCopy();
            newModel.makePlay(position.row(), position.column(), tileColor);
            childValue = getOpponentValue(newModel, depth - 1, alpha, beta,
                    tileColor.getOpposite());
            if (bestMove == null || childValue > bestMove.value()) {
                bestMove = new Move(position.row(), position.column(), childValue);
            }
            alpha = Math.max(alpha, childValue);
            if (childValue >= beta) {
                break;
            }
        }
        return bestMove;
    }

    // This doesn't override the ordinary getMyMove() method because it adds
    // alpha and beta parameters.
    private double getOpponentValue(GameModel model, int depth, double alpha, double beta, TileColor tileColor) {
        if (depth < 0) {
            throw new IllegalArgumentException();
        }
        if (depth == 0 || model.isGameOver()) {
            return getValue(model, tileColor.getOpposite());
        }
        double minValue = Double.MAX_VALUE;
        for (RowColPair position : getLegalPositions(model)) {
            GameModel childModel = model.deepCopy();
            childModel.makePlay(position.row(), position.column(), tileColor);
            Move childMove = getMyMove(childModel, depth - 1, alpha, beta, tileColor.getOpposite());
            minValue = Math.min(minValue, childMove.value());
            beta = Math.min(beta, minValue);
            if (minValue <= alpha) {
                break;
            }
        }
        return minValue;
    }
}

