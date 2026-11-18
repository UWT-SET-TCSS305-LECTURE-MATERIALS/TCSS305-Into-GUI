package edu.uw.tcss.bouncingballs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the collection of balls and all simulation-level flags.
 * This is the single source of truth that the view reads and the
 * physics engine updates.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class SimulationState {

    /** The default simulation width in pixels. */
    public static final int DEFAULT_WIDTH = 800;

    /** The default simulation height in pixels. */
    public static final int DEFAULT_HEIGHT = 450;

    /** The list of balls in the simulation. */
    private final List<Ball> myBalls;

    /** Whether the simulation is paused. */
    private boolean myPaused;

    /** Whether gravity is enabled. */
    private boolean myGravityOn;

    /** The width of the simulation bounds. */
    private int myWidth;

    /** The height of the simulation bounds. */
    private int myHeight;

    /**
     * Creates a new simulation state with an empty ball list,
     * not paused, gravity off, and default dimensions.
     */
    public SimulationState() {
        myBalls = new ArrayList<>();
        myPaused = false;
        myGravityOn = false;
        myWidth = DEFAULT_WIDTH;
        myHeight = DEFAULT_HEIGHT;
    }

    /**
     * Returns an unmodifiable view of the ball list.
     *
     * @return an unmodifiable list of balls
     */
    public List<Ball> getBalls() {
        return Collections.unmodifiableList(myBalls);
    }

    /**
     * Adds a ball to the simulation.
     *
     * @param ball the ball to add
     */
    public void addBall(final Ball ball) {
        myBalls.add(ball);
    }

    /**
     * Removes all balls from the simulation.
     */
    public void clearBalls() {
        myBalls.clear();
    }

    /**
     * Returns whether the simulation is paused.
     *
     * @return true if paused, false otherwise
     */
    public boolean isPaused() {
        return myPaused;
    }

    /**
     * Sets the paused state of the simulation.
     *
     * @param paused true to pause, false to resume
     */
    public void setPaused(final boolean paused) {
        myPaused = paused;
    }

    /**
     * Returns whether gravity is enabled.
     *
     * @return true if gravity is on, false otherwise
     */
    public boolean isGravityOn() {
        return myGravityOn;
    }

    /**
     * Sets whether gravity is enabled.
     *
     * @param gravityOn true to enable gravity, false to disable
     */
    public void setGravityOn(final boolean gravityOn) {
        myGravityOn = gravityOn;
    }

    /**
     * Returns the width of the simulation bounds.
     *
     * @return the width in pixels
     */
    public int getWidth() {
        return myWidth;
    }

    /**
     * Returns the height of the simulation bounds.
     *
     * @return the height in pixels
     */
    public int getHeight() {
        return myHeight;
    }

    /**
     * Sets the simulation bounds dimensions.
     *
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    public void setSize(final int width, final int height) {
        myWidth = width;
        myHeight = height;
    }

    /**
     * Returns the sum of kinetic energy of all balls in the simulation.
     *
     * @return the total kinetic energy
     */
    public double getTotalEnergy() {
        double total = 0;
        for (final Ball ball : myBalls) {
            total += ball.getKineticEnergy();
        }
        return total;
    }

    /**
     * Clears all balls and resets flags to defaults
     * (not paused, gravity off, default dimensions).
     */
    public void reset() {
        myBalls.clear();
        myPaused = false;
        myGravityOn = false;
        myWidth = DEFAULT_WIDTH;
        myHeight = DEFAULT_HEIGHT;
    }
}
