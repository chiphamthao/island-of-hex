package islands.model;

/**
 * Constructs a potential move.
 *
 * @param row the row
 * @param col the column
 * @param value the expected value of making this move
 */
public record Move(int row, int col, double value)  {
    private static final int INVALID_COORD = -1;

    /**
     * Constructs a move without a row or position.
     *
     * @param value the value
     */
    public Move(double value) {
        this(INVALID_COORD, INVALID_COORD, value);
    }

    /**
     * Gets the position of this move.
     *
     * @return the position
     */
    public RowColPair getPosition() {
        if (row == INVALID_COORD || col == INVALID_COORD) {
            throw new IllegalArgumentException("No position available");
        }
        return new RowColPair(row, col);
    }
}
