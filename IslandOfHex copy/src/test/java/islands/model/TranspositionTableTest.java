package islands.model;

import islands.model.student.TranspositionTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static islands.model.TestHelperMethods.fill;

public class TranspositionTableTest {
    private static final int SAMPLE_DEPTH = 3;
    private static final int SAMPLE_VALUE = 20;
    GameModel model3a, model3b, model3c, model3d;
    List<GameModel> equivalentModels;
    List<GameModel> nonequivalentModels;
    TranspositionTable table;
    Move sampleMove;

    @BeforeEach
    public void setup() {
        // TODO (Ellen): Create helper method to make this code less repetitive.
        model3a = new GameModelImplementation(3);
        fill(model3a, 3, "WWn", "Bnn", "nnB");
        model3b = new GameModelImplementation(3);
        fill(model3b, 3, "Bnn", "nnB", "nWW");
        model3c = new GameModelImplementation(3);
        fill(model3c, 3, "Bnn", "nnW", "nBW");
        model3d = new GameModelImplementation(3);
        fill(model3d, 3, "WBn", "Wnn", "nnB");
        equivalentModels = List.of(model3a, model3b);
        nonequivalentModels = List.of(model3c, model3d);
        table = new TranspositionTable();
        sampleMove = new Move(2, 1, SAMPLE_VALUE);
        table.putMove(model3a, SAMPLE_DEPTH, sampleMove);
    }

    @Test
    public void testHasMove() {
        // If we add an entry with a depth of 3, it should satisfy requests
        // for depths of 0-2 also (but not depths greater than 3).
        for (int i = 0; i <= SAMPLE_DEPTH; i++) {
            assertTrue(table.hasMove(model3a, i));
        }
        assertFalse(table.hasMove(model3a, SAMPLE_DEPTH + 1));
    }

    @Test
    public void testGetMove() {
        // If we add an entry with a depth of 3, it should satisfy requests
        // for depths of 0-2 also (but not depths greater than 3).
        for (int i = 0; i <= SAMPLE_DEPTH; i++) {
            assertEquals(sampleMove, table.getMove(model3a, i));
        }
    }

    @Test
    public void testTransformations() {
        assertEquals(new Move(0, 1, SAMPLE_VALUE), table.getMove(model3b, 3));
        for (GameModel model : nonequivalentModels) {
            assertFalse(table.hasMove(model, 1));
        }
    }
}
