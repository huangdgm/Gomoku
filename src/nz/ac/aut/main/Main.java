package nz.ac.aut.main;

import nz.ac.aut.model.Game;
import nz.ac.aut.gui.GomokuGUI;

/**
 * Welcome to the GOMOKU world
 *
 * Rules:
 *
 * 1. One player plays the BLACK, the other player plays the WHITE;
 *
 * 2. BLACK plays first;
 *
 * 3. The player who first places five points in a line wins the game.
 *
 * 4. The player can terminate the game by clicking the close button on the window or clicking the exit menu item.
 *
 * 5. The player can use the save menu item button to save the current progress of the game.
 *
 * 6. The player can choose to recover a history game by clicking the menu item in the open recent game menu list.
 *
 * This is the Entry point class of the game.
 *
 * @author Dong Huang
 */
public class Main {

    public static void main(String[] args) {
        // create the game object
        final Game game = new Game();
        // create the GUI for the game
        final GomokuGUI gui = new GomokuGUI(game);

        // make the GUI visible
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.setVisible(true);
            }
        });
    }
}
