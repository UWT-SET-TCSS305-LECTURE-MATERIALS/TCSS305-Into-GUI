package edu.uw.tcss.bouncingballs.app;

import edu.uw.tcss.bouncingballs.view.SimulationFrame;
import javax.swing.SwingUtilities;

/**
 * Entry point for the Bouncing Balls simulation application.
 * Creates and displays the main simulation frame on the
 * Event Dispatch Thread.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public final class BouncingBallsApp {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BouncingBallsApp() {
        // utility class — do not instantiate
    }

    /**
     * Launches the Bouncing Balls simulation.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(BouncingBallsApp::createAndShowGui);
    }

    /**
     * Creates the simulation frame and shows it. Runs on the Event Dispatch Thread.
     */
    private static void createAndShowGui() {
        final SimulationFrame frame = new SimulationFrame();
        frame.setVisible(true);
    }
}
