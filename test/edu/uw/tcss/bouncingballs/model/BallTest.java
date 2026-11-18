package edu.uw.tcss.bouncingballs.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Color;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for the {@link Ball} class.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class BallTest {

    /** Tolerance for floating-point comparisons. */
    private static final double TOLERANCE = 1e-6;

    /** Starting x position passed to the constructor. */
    private static final double START_X = 100.0;

    /** Starting y position passed to the constructor. */
    private static final double START_Y = 200.0;

    /** Horizontal leg of a 3-4-5 velocity triangle. */
    private static final double LEG_VX = 3.0;

    /** Vertical leg of a 3-4-5 velocity triangle. */
    private static final double LEG_VY = 4.0;

    /** Speed of a 3-4-5 velocity triangle (its hypotenuse). */
    private static final double LEG_SPEED = 5.0;

    /** Radius passed to the constructor in the radius test. */
    private static final double RADIUS = 25.0;

    /** Horizontal velocity for the horizontal-only speed test. */
    private static final double HORIZONTAL_SPEED = 7.0;

    /** Vertical speed for the vertical-only speed test. */
    private static final double VERTICAL_SPEED = 5.0;

    /** New x position given to setX. */
    private static final double NEW_X = 42.0;

    /** New y position given to setY. */
    private static final double NEW_Y = 99.0;

    /** New horizontal velocity given to setVX. */
    private static final double NEW_VX = -5.0;

    /** New vertical velocity given to setVY. */
    private static final double NEW_VY = 8.0;

    /** Far-off position given to the setters in the mass test. */
    private static final int FAR_POSITION = 999;

    /** Large velocity given to the setters in the mass test. */
    private static final int FAST_VELOCITY = 100;

    /** Updated horizontal leg of a 6-8-10 velocity triangle. */
    private static final double UPDATED_VX = 6.0;

    /** Updated vertical leg of a 6-8-10 velocity triangle. */
    private static final double UPDATED_VY = 8.0;

    /** Speed of a 6-8-10 velocity triangle (its hypotenuse). */
    private static final double UPDATED_SPEED = 10.0;

    // ---------------------------------------------------------------
    // Construction & Getters
    // ---------------------------------------------------------------

    /** Checks that the constructor stores the x and y position. */
    @Test
    void constructorSetsPosition() {
        final Ball ball = new Ball(START_X, START_Y, 3.0, 4.0, 15.0, Color.RED);
        assertEquals(START_X, ball.getX(), TOLERANCE, "X position should match constructor arg");
        assertEquals(START_Y, ball.getY(), TOLERANCE, "Y position should match constructor arg");
    }

    /** Checks that the constructor stores the x and y velocity. */
    @Test
    void constructorSetsVelocity() {
        final Ball ball = new Ball(0, 0, LEG_VX, -LEG_VY, 10.0, Color.BLUE);
        assertEquals(LEG_VX, ball.getVX(), TOLERANCE, "VX should match constructor arg");
        assertEquals(-LEG_VY, ball.getVY(), TOLERANCE, "VY should match constructor arg");
    }

    /** Checks that the constructor stores the radius. */
    @Test
    void constructorSetsRadius() {
        final Ball ball = new Ball(0, 0, 0, 0, RADIUS, Color.GREEN);
        assertEquals(RADIUS, ball.getRadius(), TOLERANCE, "Radius should match constructor arg");
    }

    /** Checks that the constructor stores the color. */
    @Test
    void constructorSetsColor() {
        final Ball ball = new Ball(0, 0, 0, 0, 10.0, Color.MAGENTA);
        assertEquals(Color.MAGENTA, ball.getColor(), "Color should match constructor arg");
    }

    // ---------------------------------------------------------------
    // Mass derivation: mass = DENSITY * PI * radius^2
    // ---------------------------------------------------------------

    /** Checks that a radius-10 ball has mass DENSITY * PI * radius squared. */
    @Test
    void massDerivationWithRadius10() {
        final double radius = 10.0;
        final Ball ball = new Ball(0, 0, 0, 0, radius, Color.RED);
        final double expectedMass = Ball.DENSITY * Math.PI * radius * radius;
        assertEquals(expectedMass, ball.getMass(), TOLERANCE,
                "Mass should equal DENSITY * PI * radius^2");
    }

    /** Checks that a radius-25 ball has mass DENSITY * PI * radius squared. */
    @Test
    void massDerivationWithRadius25() {
        final double radius = 25.0;
        final Ball ball = new Ball(0, 0, 0, 0, radius, Color.RED);
        final double expectedMass = Ball.DENSITY * Math.PI * radius * radius;
        assertEquals(expectedMass, ball.getMass(), TOLERANCE,
                "Mass should equal DENSITY * PI * radius^2 for radius 25");
    }

    /** Checks that a larger radius gives a larger mass. */
    @Test
    void biggerRadiusProducesLargerMass() {
        final Ball small = new Ball(0, 0, 0, 0, 10.0, Color.RED);
        final Ball large = new Ball(0, 0, 0, 0, 30.0, Color.RED);
        assertNotEquals(small.getMass(), large.getMass(),
                "Different radii should produce different masses");
        assertEquals(true, large.getMass() > small.getMass(),
                "Larger radius should yield larger mass");
    }

    // ---------------------------------------------------------------
    // Kinetic energy: 0.5 * mass * (vx^2 + vy^2)
    // ---------------------------------------------------------------

    /** Checks kinetic energy against 0.5 * mass * speed squared. */
    @Test
    void kineticEnergyWithKnownValues() {
        final double radius = 10.0;
        final double vx = 3.0;
        final double vy = 4.0;
        final Ball ball = new Ball(0, 0, vx, vy, radius, Color.RED);
        final double mass = Ball.DENSITY * Math.PI * radius * radius;
        final double expected = 0.5 * mass * (vx * vx + vy * vy);
        assertEquals(expected, ball.getKineticEnergy(), TOLERANCE,
                "Kinetic energy should be 0.5 * mass * speed^2");
    }

    /** Checks that a ball at rest has zero kinetic energy. */
    @Test
    void kineticEnergyZeroVelocity() {
        final Ball ball = new Ball(50, 50, 0, 0, 15.0, Color.RED);
        assertEquals(0.0, ball.getKineticEnergy(), TOLERANCE,
                "Kinetic energy should be 0 when velocity is zero");
    }

    // ---------------------------------------------------------------
    // Speed: sqrt(vx^2 + vy^2)
    // ---------------------------------------------------------------

    /** Checks that velocity (3, 4) gives speed 5. */
    @Test
    void speedWithKnownValues() {
        final Ball ball = new Ball(0, 0, LEG_VX, LEG_VY, 10.0, Color.RED);
        assertEquals(LEG_SPEED, ball.getSpeed(), TOLERANCE,
                "Speed should be sqrt(3^2 + 4^2) = 5");
    }

    /** Checks that a ball at rest has zero speed. */
    @Test
    void speedZeroVelocity() {
        final Ball ball = new Ball(0, 0, 0, 0, 10.0, Color.RED);
        assertEquals(0.0, ball.getSpeed(), TOLERANCE,
                "Speed should be 0 when velocity is zero");
    }

    /** Checks that speed equals |vx| when vy is zero. */
    @Test
    void speedOnlyHorizontal() {
        final Ball ball = new Ball(0, 0, HORIZONTAL_SPEED, 0, 10.0, Color.RED);
        assertEquals(HORIZONTAL_SPEED, ball.getSpeed(), TOLERANCE,
                "Speed should equal |vx| when vy is zero");
    }

    /** Checks that speed equals |vy| when vx is zero. */
    @Test
    void speedOnlyVertical() {
        final Ball ball = new Ball(0, 0, 0, -VERTICAL_SPEED, 10.0, Color.RED);
        assertEquals(VERTICAL_SPEED, ball.getSpeed(), TOLERANCE,
                "Speed should equal |vy| when vx is zero");
    }

    // ---------------------------------------------------------------
    // Setters
    // ---------------------------------------------------------------

    /** Checks that setX changes the x position. */
    @Test
    void setXUpdatesPosition() {
        final Ball ball = new Ball(0, 0, 0, 0, 10.0, Color.RED);
        ball.setX(NEW_X);
        assertEquals(NEW_X, ball.getX(), TOLERANCE, "setX should update x position");
    }

    /** Checks that setY changes the y position. */
    @Test
    void setYUpdatesPosition() {
        final Ball ball = new Ball(0, 0, 0, 0, 10.0, Color.RED);
        ball.setY(NEW_Y);
        assertEquals(NEW_Y, ball.getY(), TOLERANCE, "setY should update y position");
    }

    /** Checks that setVX changes the x velocity. */
    @Test
    void setVXUpdatesVelocity() {
        final Ball ball = new Ball(0, 0, 0, 0, 10.0, Color.RED);
        ball.setVX(NEW_VX);
        assertEquals(NEW_VX, ball.getVX(), TOLERANCE, "setVX should update vx");
    }

    /** Checks that setVY changes the y velocity. */
    @Test
    void setVYUpdatesVelocity() {
        final Ball ball = new Ball(0, 0, 0, 0, 10.0, Color.RED);
        ball.setVY(NEW_VY);
        assertEquals(NEW_VY, ball.getVY(), TOLERANCE, "setVY should update vy");
    }

    /** Checks that setColor changes the color. */
    @Test
    void setColorUpdatesColor() {
        final Ball ball = new Ball(0, 0, 0, 0, 10.0, Color.RED);
        ball.setColor(Color.CYAN);
        assertEquals(Color.CYAN, ball.getColor(), "setColor should update color");
    }

    /** Checks that no setter changes the mass. */
    @Test
    void settersDoNotAffectMass() {
        final Ball ball = new Ball(0, 0, 1.0, 1.0, 10.0, Color.RED);
        final double massBefore = ball.getMass();
        ball.setX(FAR_POSITION);
        ball.setY(FAR_POSITION);
        ball.setVX(FAST_VELOCITY);
        ball.setVY(FAST_VELOCITY);
        ball.setColor(Color.BLACK);
        assertEquals(massBefore, ball.getMass(), TOLERANCE,
                "Mass should not change when setters are called");
    }

    // ---------------------------------------------------------------
    // Speed and energy update after velocity change
    // ---------------------------------------------------------------

    /** Checks that speed follows a velocity change made through the setters. */
    @Test
    void speedUpdatesAfterVelocityChange() {
        final Ball ball = new Ball(0, 0, 0, 0, 10.0, Color.RED);
        ball.setVX(UPDATED_VX);
        ball.setVY(UPDATED_VY);
        assertEquals(UPDATED_SPEED, ball.getSpeed(), TOLERANCE,
                "Speed should reflect updated velocity");
    }

    /** Checks that kinetic energy follows a velocity change made through the setters. */
    @Test
    void energyUpdatesAfterVelocityChange() {
        final Ball ball = new Ball(0, 0, 0, 0, 15.0, Color.RED);
        ball.setVX(LEG_VX);
        ball.setVY(LEG_VY);
        final double mass = ball.getMass();
        final double expected = 0.5 * mass * (9.0 + 16.0);
        assertEquals(expected, ball.getKineticEnergy(), TOLERANCE,
                "Kinetic energy should reflect updated velocity");
    }
}
