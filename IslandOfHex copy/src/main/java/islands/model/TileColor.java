package islands.model;

import java.awt.*;

/**
 * A representation of the colors of board positions and tiles in Islands of
 * Hex.
 */
public enum TileColor {
    /**
     * An empty position
     */
    NONE("none", Color.GRAY), // color not used

    /**
     * A white tile or a position with a white tile
     */
    WHITE("White", Color.WHITE),

    /**
     * A black tile or a position with a black tile
     */
    BLACK("Black", Color.BLACK);

    private final String name;
    private final Color color;

    TileColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Gets the capitalized name of this tile color.
     *
     * @return the capitalized name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the display color of this tile color.
     *
     * @return the display color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the opposite of this color, where {@link #BLACK} and
     * {@link #WHITE} are opposites.
     *
     * @return the opposite of this color
     * @throws IllegalArgumentException if this is {@link #NONE}
     */
    public TileColor getOpposite() {
        return switch (this) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
            case NONE ->
                    throw new IllegalArgumentException("Cannot toggle EMPTY");
        };
    }
}
