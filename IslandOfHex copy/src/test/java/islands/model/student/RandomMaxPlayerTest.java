package islands.model.student;

import islands.model.GameModel;
import islands.model.GameModelImplementation;
import islands.model.Move;
import islands.model.TileColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomMaxPlayerTest {
    RandomMaxPlayer player;

    @BeforeEach
    public void setup() {
        player = new RandomMaxPlayer();
    }

    @Test
    public void getMakeMoveSize2Empty() {
        GameModel model = new GameModelImplementation(2);
        Move move = player.getMyMove(model, 4, TileColor.WHITE);
        double expectedValue = 8.0 / 3;
        assertEquals(expectedValue, move.value());
    }
}