package islands.model.student.studentname;

import islands.model.GameModel;
import islands.model.RowColPair;
import islands.model.TileColor;
import islands.model.TimeLimitedSimulatedPlayer;
import islands.model.student.AlphaBetaPlayer;

/**
 * A simulated player for a timed tournament.
 */
public class TournamentPlayer extends TimeLimitedSimulatedPlayer {
    private final AlphaBetaPlayer player = new AlphaBetaPlayer();

    @Override
    public String getName() {
        return "Ham Chip";
    }

    @Override
    public void makeMove(GameModel model, TileColor tileColor, Listener listener) {
        int depth = 1;
        // For now, make a move into the first empty position.
        while (!Thread.interrupted() && depth < model.getSize() * model.getSize()) {
            RowColPair position = player.chooseNextMove(model, depth, tileColor);
            listener.receiveMove(position);
        }
    }
}
