package islands.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static islands.model.TestHelperMethods.fill;
import static org.junit.jupiter.api.Assertions.*;

public class GameModelImplementationHW12Test {
    GameModel model5;

    @BeforeEach
    public void setup() {
        model5 = new GameModelImplementation(5);
    }

    @Test
    public void deepCopyCreatesSeparateGameModel() {
        GameModel model5copy = model5.deepCopy();
        assertNotSame(model5, model5copy);
    }

    @Test
    public void deepCopyCreatesSimilarGameModel() {
        model5.makePlay(1, 1, TileColor.BLACK);
        GameModel model5copy = model5.deepCopy();
        assertEquals(model5copy.getScore(TileColor.BLACK), model5copy.getScore(TileColor.BLACK));
        assertEquals(model5copy.getScore(TileColor.WHITE), model5copy.getScore(TileColor.WHITE));
        for (int row = 0; row < model5.getSize(); row++) {
            for (int col = 0; col < model5.getSize(); col++) {
                assertEquals(model5.canPlay(row, col), model5copy.canPlay(row, col));
            }
        }
    }

    @Test
    public void deepCopyDoesNotAffectOriginal() {
        model5.makePlay(1, 1, TileColor.BLACK);
        GameModel model5copy = model5.deepCopy();
        assertEquals(1, model5.getScore(TileColor.BLACK));
        assertEquals(0, model5.getScore(TileColor.WHITE));
        model5copy.makePlay(0, 1, TileColor.WHITE);
        assertEquals(1, model5.getScore(TileColor.BLACK));
        assertEquals(0, model5.getScore(TileColor.WHITE));
    }

    @Test
    public void deepCopyNotAffectedByChangesToOriginal() {
        GameModel model5copy = model5.deepCopy();
        assertEquals(0, model5copy.getScore(TileColor.WHITE));
        assertEquals(0, model5copy.getScore(TileColor.BLACK));
        assertTrue(model5copy.canPlay(0, 0));
        model5.makePlay(0, 0, TileColor.WHITE);
        assertEquals(0, model5copy.getScore(TileColor.WHITE));
        assertEquals(0, model5copy.getScore(TileColor.BLACK));
        assertTrue(model5copy.canPlay(0, 0));
    }

    @Test
    public void deepCopyParentsAreInCopy1() {
        model5.makePlay(0, 0, TileColor.WHITE);
        GameModel model5copy = model5.deepCopy();
        model5.makePlay(1, 0, TileColor.WHITE);
        assertEquals(1, model5copy.getScore(TileColor.WHITE));
    }

    @Test
    public void deepCopyParentsAreInCopy2() {
        GameModel model2 = new GameModelImplementation(2);
        model2.makePlay(0, 1, TileColor.WHITE);
        GameModel model2copy1 = model2.deepCopy();
        model2copy1.makePlay(0, 0, TileColor.BLACK);
        GameModel model2copy2 = model2copy1.deepCopy();
        model2copy2.makePlay(1, 0, TileColor.WHITE);
        assertEquals(2, model2copy2.getScore(TileColor.WHITE));
    }

    @Test
    public void getBoardStringWorks3() {
        GameModel model3 = new GameModelImplementation(3);
        fill(model3, 3, "WBn", "Wnn", "nnB");
        assertEquals("WBn\nWnn\nnnB\n", model3.getBoardString());
    }
}
