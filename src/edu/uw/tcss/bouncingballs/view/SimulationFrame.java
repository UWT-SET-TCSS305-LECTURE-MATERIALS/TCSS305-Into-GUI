package edu.uw.tcss.bouncingballs.view;

import edu.uw.tcss.bouncingballs.model.Ball;
import edu.uw.tcss.bouncingballs.model.SimulationState;
import edu.uw.tcss.bouncingballs.physics.PhysicsEngine;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The top-level frame for the bouncing-balls simulation.
 * Assembles the simulation panel (center), control panel and status bar
 * (south), and the ball editor overlay (glass pane). Owns the Swing
 * timer that drives the simulation loop.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class SimulationFrame extends JFrame {

    /** Delay in milliseconds between simulation ticks (~100 fps). */
    private static final int TIMER_DELAY = 10;

    /** Initial ball x position. */
    private static final double INITIAL_X = 40.0;

    /** Initial ball y position. */
    private static final double INITIAL_Y = 40.0;

    /** Initial ball x velocity. */
    private static final double INITIAL_VX = 3.0;

    /** Initial ball y velocity. */
    private static final double INITIAL_VY = 2.0;

    /** Initial ball radius. */
    private static final double INITIAL_RADIUS = 20.0;

    /** UW Purple color for the initial ball. */
    private static final Color UW_PURPLE = new Color(75, 46, 131);

    /** The simulation state (model). */
    private final SimulationState myState;

    /** The physics engine. */
    private final PhysicsEngine myPhysicsEngine;

    /** The main simulation rendering panel. */
    private final SimulationPanel mySimulationPanel;

    /** The status bar showing total energy. */
    private final StatusBar myStatusBar;

    /** The simulation timer. */
    private final Timer myTimer;

    /**
     * Creates and lays out the simulation frame with all sub-panels,
     * wires up the simulation timer, and adds an initial ball.
     */
    public SimulationFrame() {
        super("Bouncing Balls");
        myState = new SimulationState();
        myPhysicsEngine = new PhysicsEngine();
        mySimulationPanel = new SimulationPanel(myState, myPhysicsEngine);
        myStatusBar = new StatusBar();
        myTimer = new Timer(TIMER_DELAY, e -> tick());

        layoutComponents();
        addInitialBall();
        myTimer.start();
    }

    /**
     * Lays out the frame: the simulation in the center, the controls and the
     * status bar stacked in the south, and the ball editor on the glass pane.
     */
    private void layoutComponents() {
        mySimulationPanel.setPreferredSize(
                new Dimension(SimulationState.DEFAULT_WIDTH,
                              SimulationState.DEFAULT_HEIGHT));

        // Ball editor overlay on glass pane
        final BallEditorOverlay editorOverlay = new BallEditorOverlay();
        setGlassPane(editorOverlay);
        mySimulationPanel.setEditorOverlay(editorOverlay);

        setLayout(new BorderLayout());
        add(mySimulationPanel, BorderLayout.CENTER);

        final JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(new ControlPanel(myState, this::resetSimulation));
        southPanel.add(myStatusBar);
        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Performs one simulation tick: updates physics, notifies the panel
     * of boosted balls, updates the status bar, and repaints.
     */
    private void tick() {
        if (!myState.isPaused()) {
            final Set<Ball> boosted = myPhysicsEngine.update(myState);
            mySimulationPanel.notifyBoostedBalls(boosted);
            myStatusBar.updateEnergy(myState.getTotalEnergy());
        }
        mySimulationPanel.repaint();
    }

    /**
     * Resets the simulation to its initial state with one ball.
     */
    private void resetSimulation() {
        myState.reset();
        addInitialBall();
    }

    /**
     * Adds the default starting ball at the top-left corner
     * moving diagonally.
     */
    private void addInitialBall() {
        myState.addBall(new Ball(INITIAL_X, INITIAL_Y,
                                INITIAL_VX, INITIAL_VY,
                                INITIAL_RADIUS, UW_PURPLE));
    }
}
