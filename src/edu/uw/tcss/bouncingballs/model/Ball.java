package edu.uw.tcss.bouncingballs.model;

import java.awt.Color;

/**
 * Mutable data class representing a single ball in the simulation.
 * Position represents the center of the ball. Radius and mass are
 * immutable after construction; mass is derived from radius using
 * a density constant proportional to area.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class Ball {

    /** Density used to derive mass from area: mass = DENSITY * PI * radius^2. */
    public static final double DENSITY = 1.0;

    /** The one half in the kinetic energy formula, (1/2) m v^2. */
    private static final double HALF = 0.5;

    /** The center x position of this ball. */
    private double myX;

    /** The center y position of this ball. */
    private double myY;

    /** The x component of velocity (pixels per tick). */
    private double myVX;

    /** The y component of velocity (pixels per tick). */
    private double myVY;

    /** The radius of this ball. */
    private final double myRadius;

    /** The mass of this ball, derived from radius at construction. */
    private final double myMass;

    /** The color of this ball. */
    private Color myColor;

    /**
     * Creates a ball with the given center position, velocity, radius, and color.
     * Mass is calculated automatically from radius as DENSITY * PI * radius^2.
     *
     * @param x the center x position
     * @param y the center y position
     * @param vx the x component of velocity
     * @param vy the y component of velocity
     * @param radius the radius of the ball
     * @param color the color of the ball
     */
    public Ball(final double x, final double y,
                final double vx, final double vy,
                final double radius, final Color color) {
        myX = x;
        myY = y;
        myVX = vx;
        myVY = vy;
        myRadius = radius;
        myMass = DENSITY * Math.PI * radius * radius;
        myColor = color;
    }

    /**
     * Returns the center x position.
     *
     * @return the center x position
     */
    public double getX() {
        return myX;
    }

    /**
     * Returns the center y position.
     *
     * @return the center y position
     */
    public double getY() {
        return myY;
    }

    /**
     * Returns the x component of velocity.
     *
     * @return the x velocity
     */
    public double getVX() {
        return myVX;
    }

    /**
     * Returns the y component of velocity.
     *
     * @return the y velocity
     */
    public double getVY() {
        return myVY;
    }

    /**
     * Returns the radius of this ball.
     *
     * @return the radius
     */
    public double getRadius() {
        return myRadius;
    }

    /**
     * Returns the mass of this ball.
     *
     * @return the mass
     */
    public double getMass() {
        return myMass;
    }

    /**
     * Returns the color of this ball.
     *
     * @return the color
     */
    public Color getColor() {
        return myColor;
    }

    /**
     * Sets the center x position.
     *
     * @param x the new center x position
     */
    public void setX(final double x) {
        myX = x;
    }

    /**
     * Sets the center y position.
     *
     * @param y the new center y position
     */
    public void setY(final double y) {
        myY = y;
    }

    /**
     * Sets the x component of velocity.
     *
     * @param vx the new x velocity
     */
    public void setVX(final double vx) {
        myVX = vx;
    }

    /**
     * Sets the y component of velocity.
     *
     * @param vy the new y velocity
     */
    public void setVY(final double vy) {
        myVY = vy;
    }

    /**
     * Sets the color of this ball.
     *
     * @param color the new color
     */
    public void setColor(final Color color) {
        myColor = color;
    }

    /**
     * Returns the kinetic energy of this ball: 0.5 * mass * (vx^2 + vy^2).
     *
     * @return the kinetic energy
     */
    public double getKineticEnergy() {
        return HALF * myMass * (myVX * myVX + myVY * myVY);
    }

    /**
     * Returns the speed of this ball: sqrt(vx^2 + vy^2).
     *
     * @return the speed
     */
    public double getSpeed() {
        return Math.sqrt(myVX * myVX + myVY * myVY);
    }

    @Override
    public String toString() {
        return "Ball[x=" + myX + ", y=" + myY
                + ", vx=" + myVX + ", vy=" + myVY
                + ", radius=" + myRadius + ", mass=" + myMass
                + ", color=" + myColor + "]";
    }
}
