package islands.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimulatedGameTreePlayerTest {
    // Create stub methods so we can instantiate the class.
    // We will only call getValue().
    SimulatedGameTreePlayer player = new SimulatedGameTreePlayer() {
        @Override
        public RowColPair chooseNextMove(GameModel model, TileColor tileColor) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Move getMyMove(GameModel model, int depth, TileColor tileColor) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getOpponentValue(GameModel model, int depth, TileColor tileColor) {
            throw new UnsupportedOperationException();
        }
    };

    private void testValue(double expectedWhiteValue, String... pattern) {
        GameModel model = new GameModelImplementation(pattern.length);
        TestHelperMethods.fill(model, pattern.length, pattern);
        double whiteValue = player.getValue(model, TileColor.WHITE);
        double blackValue = player.getValue(model, TileColor.BLACK);
        // assertEquals() would do the wrong thing for 0
        // see: https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html#equals%28java.lang.Object%29
        assertTrue(whiteValue == -blackValue);
        assertEquals(expectedWhiteValue, whiteValue);
    }

    @Test
    public void getValueOnSize2Board() {
        testValue(0, "nn", "nn");
        testValue(1, "Wn", "nn");
        testValue(0, "WB", "nn");
        testValue(0, "WB", "Wn"); // game over
        testValue(0, "WB", "WB");
        testValue(-4, "WB", "BW"); // game over
    }
}
