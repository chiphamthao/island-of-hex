package islands.model;

import islands.model.student.MinimaxPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MiniMaxPlayerTest {

    @Test
    public void testTree2() {
        // From an empty 2x2 board, all mini-max paths lead to 0.
        for (int depth = 1; depth < 5; depth++) {
            MinimaxPlayer player = new MinimaxPlayer();
            GameModel model = new GameModelImplementation(2);
            Move move = player.getMyMove(model, depth, TileColor.WHITE);
            assertNotNull(move);
            assertNotNull(move.getPosition());
            if (depth == 1) {
                // If there's been only one move, White will be ahead 1-0.
                assertEquals(1, move.value());
            } else {
                assertEquals(0, move.value());
            }
            // Since moves have same expected outcome, the first is chosen.
            assertEquals(0, move.getPosition().row());
            assertEquals(0, move.getPosition().column());
        }
    }

}
