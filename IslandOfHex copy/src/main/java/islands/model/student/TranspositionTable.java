package islands.model.student;

import islands.model.GameModel;
import islands.model.Move;
import islands.model.TileColor;

import java.util.*;

/**
 * A table for storing and retrieving the results of calls to {@link
 * islands.model.SimulatedGameTreePlayer#getMyMove(GameModel, int, TileColor)}
 */
public class TranspositionTable {
    private record Value(int depth, Move move) {
    }

    private final Map<String, Value> hash = new HashMap<>();

    /**
     * Records that calling {@link
     * islands.model.SimulatedGameTreePlayer#getMyMove(GameModel, int, TileColor)}
     * with the given model and depth produced the specified move.
     *
     * @param model the model
     * @param depth the depth
     * @param move  the move
     */
    public void putMove(GameModel model, int depth, Move move) {
        // Make an addition to the hash table.
        // The key should be the model's board string.
        // The value should be an instance of Value with the depth and move.
        hash.put(model.getBoardString(), new Value(depth, move));

        // Add the equivalent board positions to the hash table.
        putTransformations(model.getSize(), model.getBoardString(), new Value(depth, move));
    }

    // Adds entries for each board representation equivalent (through
    // rotation or reflection) with the passed board string.
    private void putTransformations(int size, String boardString, Value value) {
        // Generate the board strings of the 3 possible transformations of
        // the current model, using its board string. While board strings
        // are not arrays, you can get any row and column position of
        // boardString by calling the helper method getTileChar().

        // You will have to build up the 3 other board strings one character
        // at a time using the translation formulas provided on Canvas:
        // * (row, col) ---> (size - 1 - row, size - 1 - col)
        // * (row, col) ---> (size - 1 - col, size - 1 - row)
        // * (row, col) ---> (col, row)
        // Don't forget to add a newline character at the end of each row.

        StringBuilder transformation = new StringBuilder();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                transformation.append(getTileChar(size, boardString, size - 1 - row, size - 1 - col));
            }
            transformation.append("\n");
        }

        // Transform the move according to the same formulas for each of
        // the board strings you create. Add them to the hash table.
        Move currMove = value.move;
        hash.put(transformation.toString(), new Value(value.depth, new Move(size - 1 - currMove.row(),
                size - 1 - currMove.col(), currMove.value())));
    }

    private static char getTileChar(int size, String boardString, int row, int col) {
        int offset = row * (size + 1) + col; // +1 for the newline
        return boardString.charAt(offset);
    }

    /**
     * Checks whether this table has the move recommended for this model
     * (or one of its transformations) when searching to the specified depth or
     * deeper.
     *
     * @param model the model
     * @param depth the minimum search depth
     * @return true if a move is available, false otherwise
     */
    public boolean hasMove(GameModel model, int depth) {
        // Check if the hash table as an entry with the model's board string
        // as the key, where the value is for a depth greater than or equal
        // to the passed depth.
        return hash.containsKey(model.getBoardString()) && hash.get(model.getBoardString()).depth >= depth;
    }

    /**
     * Gets the stored move for this model computed to the given depth or
     * deeper.
     *
     * @param model the model
     * @param depth the depth
     * @return the stored move
     * @throws NoSuchElementException if this table does not have an entry
     *                                with the specified model with a depth
     *                                greater than or equal to the
     *                                requested depth
     */
    public Move getMove(GameModel model, int depth) {
        if (!hasMove(model, depth)) {
            throw new NoSuchElementException();
        }
        Value currValue = hash.get(model.getBoardString());
        if (currValue.depth >= depth) {
            return currValue.move;
        } else {
            throw new NoSuchElementException();
        }
    }
}
