package islands;

import islands.model.SimulatedRandomPlayer;
import islands.model.SimulatedPlayer;
import islands.model.student.AlphaBetaPlayer;
import islands.model.student.CachingMinimaxPlayer;
import islands.model.student.MinimaxPlayer;
import islands.model.student.RandomMaxPlayer;
import islands.model.student.studentname.TournamentPlayer;
import islands.view.Game;

import java.awt.Dimension;
import javax.swing.*;
import java.util.*;

/**
 * Main launching point for the Hex Game
 */
public class GameDriver {
    // These are the simulators that users can choose among.
    private static List<Class<? extends SimulatedPlayer>> simulators = List.of(
            SimulatedRandomPlayer.class,
            MinimaxPlayer.class,
            RandomMaxPlayer.class,
            CachingMinimaxPlayer.class,
            AlphaBetaPlayer.class,
            TournamentPlayer.class
    );

    public static void main(String[] args) {
        JFrame frame = new JFrame("Islands of Hex");

        Game game = new Game(simulators);
        frame.add(game);
        frame.setSize(new Dimension(Game.BOARD_WIDTH, Game.BOARD_HEIGHT));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
