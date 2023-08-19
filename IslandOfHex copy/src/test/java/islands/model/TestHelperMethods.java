package islands.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestHelperMethods {

    private TestHelperMethods() {
        // A private constructor makes it so this class can't be instantiated.
    }

    // Each string corresponds to a row. Characters other than "B" and "W"
    // are ignored.
    static void fill(GameModel model, int size, String... tiles) {
        int row = 0;
        int col = 0;
        for (char c : String.join("", tiles).toCharArray()) {
            assertTrue(model.canPlay(row, col),
                    String.format("Error when initializing row = %d, col = %d of size %d model", row, col, size));
            if (c == TileColor.WHITE.getName().charAt(0)) {
                model.makePlay(row, col, TileColor.WHITE);
            } else if (c == TileColor.BLACK.getName().charAt(0)) {
                model.makePlay(row, col, TileColor.BLACK);
            }
            if (++col == size) {
                row++;
                if (row > size) {
                    throw new IllegalArgumentException();
                }
                col = 0;
            }
        }
    }

}
