package islands.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class SimulatedMinimaxPlayerTest {
    GameModel model;
    SimulatedPlayer player;

    abstract SimulatedGameTreePlayer buildPlayer(GameModel model, TileColor tileColor, int maxDepth);

    @BeforeEach
    public void setup() {
    }

    private void getMaxMoveTest3(TileColor tileColor) {
        model = new GameModelImplementation(3);
        player = buildPlayer(model, tileColor, 6);
        RowColPair move = player.chooseNextMove(model, tileColor);
       // assertEquals(0, move.row());
       // assertEquals(0, move.column());
    }

    @Test
    public void test1() {
        getMaxMoveTest3(TileColor.WHITE);
    }

    @Test
    public void test3() {
        getMaxMoveTest3(TileColor.BLACK);
    }

}
