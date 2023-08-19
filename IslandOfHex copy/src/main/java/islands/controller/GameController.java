package islands.controller;

import islands.model.*;
import islands.model.GameModelImplementation;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Control the game flow
 */
public class GameController implements MouseListener {
    private static final TileColor START_COLOR = TileColor.WHITE;
    private static final int MS_BETWEEN_SIMULATED_MOVES = 10;

    private final GameModel model;
    private Timer timer;
    private ViewDelegate viewDelegate;
    private TileColor turn = START_COLOR;
    private boolean gameOver = false;

    private SimulatedPlayer whiteSimulatedPlayer;
    private SimulatedPlayer blackSimulatedPlayer;

    /**
     * The required interface to display the game
     */
    public interface ViewDelegate {
        /**
         * Display the turn for the given colour
         *
         * @param color the player whose turn it is
         */
        void displayTurn(TileColor color);

        /**
         * play a piece with a given color in a given position
         *
         * @param row       the row to play the piece into
         * @param col       the column to play the piece into
         * @param tileColor the color of the piece
         */
        void setColor(int row, int col, TileColor tileColor);

        /**
         * Listen for Mouse clicks
         *
         * @param ml the mouse listener
         */
        void listen(MouseListener ml);

        /**
         * Convert y and x screen positions into a Point (x,y) on the game board (specific tile)
         *
         * @param y y screen coordinate
         * @param x screen coordinate
         * @return the specific game tile specified as an x,y Point object
         */
        Optional<RowColPair> getHexDim(int y, int x);

        /**
         * The winner of the game
         *
         * @param winner "BLACK", "WHITE" or "TIE"
         */
        void setWinner(TileColor winner);

        /**
         * The current score in the game
         *
         * @param whiteScore white's score
         * @param blackScore black's score
         */
        void setScore(int whiteScore, int blackScore);
    }

    public static SimulatedPlayer constructSimulatedPlayer(
            Class<? extends SimulatedPlayer> clazz,
            GameModel model,
            TileColor tileColor
    ) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format("Error trying to call constructor %s()"));
        }
    }

    public GameController(
            int size,
            Class<? extends SimulatedPlayer> whiteClass,
            Class<? extends SimulatedPlayer> blackClass,
            ViewDelegate vd) {
        model = new GameModelImplementation(size);

        // Create simulators if requested.
        if (whiteClass != null) {
            this.whiteSimulatedPlayer = constructSimulatedPlayer(whiteClass, model, TileColor.WHITE);
        }
        if (blackClass != null) {
            this.blackSimulatedPlayer = constructSimulatedPlayer(blackClass, model, TileColor.WHITE);
        }
        if (whiteClass != null || blackClass != null && MS_BETWEEN_SIMULATED_MOVES > 0) {
            timer = new Timer(MS_BETWEEN_SIMULATED_MOVES, (e) -> runSimulatedPlayers());
            timer.start();
        }

        setDelegate(vd);
    }

    /**
     * Change the turn from white to black or vice versa.
     */
    public void toggleTurn() {
        turn = turn.getOpposite();
        viewDelegate.displayTurn(turn);
    }


    private void runSimulatedPlayers() {
        if (turn == TileColor.WHITE && whiteSimulatedPlayer != null) {
            tryRowColFromPoint(whiteSimulatedPlayer.timeAndChooseNextMove(model, turn));
        } else if (turn == TileColor.BLACK && blackSimulatedPlayer != null) {
            tryRowColFromPoint(blackSimulatedPlayer.timeAndChooseNextMove(model, turn));
        }
    }

    /**
     * Set the view delegate where output is sent
     *
     * @param vd the view delegate
     */
    public void setDelegate(ViewDelegate vd) {
        viewDelegate = vd;
        vd.displayTurn(START_COLOR);
        vd.listen(this);
    }

    /**
     * Play a game piece at a point on the screen
     *
     * @param p the row and col position to play into
     */
    public void tryRowColFromPoint(RowColPair p) {
        int col = p.column();
        int row = p.row();
        tryRowCol(row, col);
    }

    // The caller is responsible for ensuring that the game has ended.
    private TileColor getWinner() {
        int whiteScore = model.getScore(TileColor.WHITE);
        int blackScore = model.getScore(TileColor.BLACK);
        return switch (Integer.signum(whiteScore - blackScore)) {
            case -1 -> TileColor.BLACK;
            case 1 -> TileColor.WHITE;
            default -> TileColor.NONE;  // tie
        };
    }

    /**
     * attempt to play a game piece in row, col
     *
     * @param row the row to play into
     * @param col the col to play into
     */
    public void tryRowCol(int row, int col) {
        if (model.canPlay(row, col)) {
            // I'm sometimes getting an NPE after trying to start a new game
            viewDelegate.setColor(row, col, turn);
            model.makePlay(row, col, turn);
            if (model.isGameOver()) {
                stopAll();
                gameOver = true;
                viewDelegate.setWinner(getWinner());
                if (whiteSimulatedPlayer != null) {
                    System.out.printf(whiteSimulatedPlayer.getTimeSummary(TileColor.WHITE));
                }
                if (blackSimulatedPlayer != null) {
                    System.out.printf(blackSimulatedPlayer.getTimeSummary(TileColor.BLACK));
                }
            } else {
                toggleTurn();
            }
            viewDelegate.setScore(model.getScore(TileColor.WHITE), model.getScore(TileColor.BLACK));
        }
    }

    /**
     * stop the timer
     */
    public void stopAll() {
        if (timer != null) {
            timer.stop();
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Ignore clicks for simulated moves
        if (turn == TileColor.WHITE && whiteSimulatedPlayer != null
                || turn == TileColor.BLACK && blackSimulatedPlayer != null) {
            return;
        }
        if (!gameOver) {
            Optional<RowColPair> point = viewDelegate.getHexDim(e.getY(), e.getX());
            point.ifPresent(this::tryRowColFromPoint);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
