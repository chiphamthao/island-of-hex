package islands.model;

/**
 * A row-column pair. In Islands of Hex, the upper left cell is considered to
 * be at row 0, column 0.
 *
 * @param row a row
 * @param column a column
 */
public record RowColPair(int row, int column) {
    @Override
    public String toString() {
        return String.format("(%d, %d)", column, row);
    }
}
