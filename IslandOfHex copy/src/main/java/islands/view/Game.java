package islands.view;

import islands.controller.GameController;
import islands.model.RowColPair;
import islands.model.SimulatedPlayer;
import islands.model.TileColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The GUI components of the game including grid and buttons
 */
public class Game extends JPanel implements GameController.ViewDelegate {

    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;
    public static final int DEFAULT_GRID_DIMENSION = 5;
    private static final String HUMAN_DESIGNATION = "Human";

    private int gridDimension;

    private HexGrid hexGrid;
    private GameController gc;

    private JLabel turnLabel;
    private JLabel sizeLabel;
    private int[] sizes = {2, 3, 5, 11, 25};

    private ButtonGroup group;

    private JComboBox simulateWhiteBox;
    private JComboBox simulateBlackBox;

    private JButton startGame;
    private JLabel winner;

    private JLabel whiteScoreLabel;
    private JLabel blackScoreLabel;

    private Map<String, Class<? extends SimulatedPlayer>> simulatorMap = new HashMap<>();

    public Game(java.util.List<Class<? extends SimulatedPlayer>> simulators) {
        setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        gridDimension = DEFAULT_GRID_DIMENSION;
        initializeSimulatorMap(simulators);
        makeWidgets();

        // Initial game is human vs. human.
        initGame(null, null);
    }

    private void initializeSimulatorMap(java.util.List<Class<? extends SimulatedPlayer>> simulators) {
        for (Class<? extends SimulatedPlayer> clazz : simulators) {
            try {
                Constructor<? extends SimulatedPlayer> constructor = clazz.getConstructor();
                SimulatedPlayer sim = constructor.newInstance();
                simulatorMap.put(sim.getName(), clazz);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("Unable to find constructor %s(G)", clazz.getSimpleName()));
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(String.format("Problem calling constructor %s()", clazz.getSimpleName()));
            }
        }
    }

    /**
     * Generate the labels
     */
    private void makeWidgets() {
        // Upper panel
        JPanel northPan = new JPanel();
        whiteScoreLabel = new JLabel("White: 0");
        blackScoreLabel = new JLabel("Black: 0");
        northPan.add(whiteScoreLabel);
        northPan.add(blackScoreLabel);
        turnLabel = new JLabel("");
        northPan.add(turnLabel);
        winner = new JLabel("");
        northPan.add(winner);
        add(northPan, BorderLayout.NORTH);

        // Lower panel
        JPanel southPan = new JPanel();

        // size selection
        sizeLabel = new JLabel("Board Size: ");
        southPan.add(sizeLabel);
        group = new ButtonGroup();
        for (int size : sizes) {
            JRadioButton sizeButton = new JRadioButton(Integer.toString(size));
            if (size == DEFAULT_GRID_DIMENSION) {
                sizeButton.setSelected(true);
            }
            sizeButton.addActionListener(v -> resetGridDimension(size));
            group.add(sizeButton);
            southPan.add(sizeButton);
        }

        // Lower panel simulator choices
        String[] simulatorChoices = new String[simulatorMap.size() + 1];
        simulatorChoices[0] = HUMAN_DESIGNATION;
        int i = 1;
        for (String name : simulatorMap.keySet()) {
            simulatorChoices[i++] = name;
        }
        startGame = new JButton("new game");
        startGame.addActionListener(v -> createNewGame());

        JLabel simulateWhiteLabel = new JLabel("White: ");
        simulateWhiteBox = new JComboBox<>(simulatorChoices);
        JLabel simulateBlackLabel = new JLabel("Black: ");
        simulateBlackBox = new JComboBox<>(simulatorChoices);

        // Lower panel button
//        startGame = new JButton("new game");
//        startGame.addActionListener(v -> createNewGame());

        // Lower panel placement
        southPan.add(simulateWhiteLabel);
        southPan.add(simulateWhiteBox);
        southPan.add(simulateBlackLabel);
        southPan.add(simulateBlackBox);
        southPan.add(startGame);
        add(southPan, BorderLayout.SOUTH);
    }

    private void resetLabel() {
        turnLabel.setText("Turn: White");
        winner.setText("");
    }

    private void createNewGame() {
        initGame(simulateWhiteBox.getSelectedItem().toString(), simulateBlackBox.getSelectedItem().toString());
    }

    private void initGame(String whiteChoice, String blackChoice) {
        if (gridDimension > 20) {
            Hexagon.hexagonLength = 10;
        } else if (gridDimension > 10)
            Hexagon.hexagonLength = 20;
        else if (gridDimension > 6)
            Hexagon.hexagonLength = 25;
        else
            Hexagon.hexagonLength = 35;
        if (hexGrid != null) {
            remove(hexGrid);
            repaint(); // remove
        }
        hexGrid = new HexGrid(gridDimension);

        add(hexGrid, BorderLayout.CENTER);
        revalidate();
        if (gc != null) {
            gc.stopAll();
        }
        Class<? extends SimulatedPlayer> whiteClass = simulatorMap.get(whiteChoice);
        Class<? extends SimulatedPlayer> blackClass = simulatorMap.get(blackChoice);

        gc = new GameController(gridDimension, whiteClass, blackClass, this);
        resetLabel();
        setScore(0, 0); // Reset score when starting game.
    }

    private void resetGridDimension(int dim) {
        gridDimension = dim;
    }

    @Override
    public void paintComponent(Graphics g) {
        drawBoard(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(new Color(0x33cccc));
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
    }

    @Override
    public void displayTurn(TileColor color) {
        turnLabel.setText("Turn: " + color.getName());
    }

    @Override
    public void setColor(int row, int col, TileColor tileColor) {
        hexGrid.setColor(row, col, tileColor.getColor());
    }

    @Override
    public void listen(MouseListener ml) {
        hexGrid.addMouseListener(ml);
    }

    @Override
    public Optional<RowColPair> getHexDim(int y, int x) {
        return hexGrid.getHexDim(y, x);
    }

    @Override
    public void setWinner(TileColor tileColor) {
        turnLabel.setText("Game Over");
        if (tileColor == TileColor.NONE) {
            winner.setText("TIE");
        } else {
            winner.setText(tileColor.getName() + " wins");
        }
        repaint();
    }

    /**
     * Displays the score.
     *
     * @param whiteScore the white score
     * @param blackScore the black score
     */
    public void setScore(int whiteScore, int blackScore) {
        whiteScoreLabel.setText("White: " + whiteScore);
        blackScoreLabel.setText("Black: " + blackScore);
    }
}
