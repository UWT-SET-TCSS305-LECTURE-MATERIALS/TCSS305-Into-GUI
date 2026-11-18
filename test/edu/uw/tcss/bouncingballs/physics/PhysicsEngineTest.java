package edu.uw.tcss.bouncingballs.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.uw.tcss.bouncingballs.model.Ball;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for the {@link PhysicsEngine} class.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class PhysicsEngineTest {

    /** Tolerance for floating-point comparisons. */
    private static final double TOLERANCE = 1e-6;

    /** Larger tolerance for physics results that accumulate rounding. */
    private static final double PHYSICS_TOLERANCE = 1e-3;

    /** Standard test arena width. */
    private static final int WIDTH = 800;

    /** Standard test arena height. */
    private static final int HEIGHT = 450;

    /** Speed of each ball in the equal-mass head-on collision. */
    private static final double HEAD_ON_SPEED = 5.0;

    /** Expected x after one move from (100, 200) with vx 5. */
    private static final double MOVED_X = 105.0;

    /** Expected y after one move from (100, 200) with vy -3. */
    private static final double MOVED_Y = 197.0;

    /** X position of the ball at rest in the move test. */
    private static final double STILL_X = 50.0;

    /** Y position of the ball at rest in the move test. */
    private static final double STILL_Y = 75.0;

    /** Expected x after one move from (100, 100) with vx -4. */
    private static final double MOVED_BACK_X = 96.0;

    /** Expected y after one move from (100, 100) with vy -6. */
    private static final double MOVED_BACK_Y = 94.0;

    /** Downward speed of the ball hitting the floor in gravity mode. */
    private static final double FALL_SPEED = 4.0;

    /** Number of friction ticks in the multiple-tick test. */
    private static final int FRICTION_TICKS = 100;

    /** Starting velocity component in the multiple-tick friction test. */
    private static final double FRICTION_START_VELOCITY = 10.0;

    /** Horizontal velocity that gravity must leave unchanged. */
    private static final double GRAVITY_TEST_VX = 5.0;

    /** X position of the shockwave origin. */
    private static final double SHOCK_X = 100;

    /** Y position of the shockwave origin. */
    private static final double SHOCK_Y = 100;

    /** Force of the shockwave. */
    private static final double SHOCK_FORCE = 50.0;

    /** The physics engine under test. */
    private PhysicsEngine myEngine;

    /** Creates a fresh physics engine before each test. */
    @BeforeEach
    void setUp() {
        myEngine = new PhysicsEngine();
    }

    // ===============================================================
    // isColliding
    // ===============================================================

    /** Checks that balls whose radii overlap are colliding. */
    @Test
    void isCollidingOverlappingBalls() {
        // Two balls at distance 15, each with radius 10 -> overlap
        final Ball a = new Ball(100, 100, 0, 0, 10.0, Color.RED);
        final Ball b = new Ball(115, 100, 0, 0, 10.0, Color.BLUE);
        assertTrue(myEngine.isColliding(a, b),
                "Balls with overlapping radii should be colliding");
    }

    /** Checks that balls far apart are not colliding. */
    @Test
    void isCollidingSeparatedBalls() {
        // Two balls at distance 100, each with radius 10 -> no collision
        final Ball a = new Ball(100, 100, 0, 0, 10.0, Color.RED);
        final Ball b = new Ball(200, 100, 0, 0, 10.0, Color.BLUE);
        assertFalse(myEngine.isColliding(a, b),
                "Separated balls should not be colliding");
    }

    /** Checks that balls exactly touching are not colliding. */
    @Test
    void isCollidingExactlyTouchingReturnsFalse() {
        // Two balls with radius 10, centers exactly 20 apart -> touching but not overlapping
        // distance == sum of radii, not < sum of radii
        final Ball a = new Ball(100, 100, 0, 0, 10.0, Color.RED);
        final Ball b = new Ball(120, 100, 0, 0, 10.0, Color.BLUE);
        assertFalse(myEngine.isColliding(a, b),
                "Exactly touching balls (distance == sum of radii) should NOT be colliding");
    }

    /** Checks that balls overlapping by 0.01 are colliding. */
    @Test
    void isCollidingJustBarelOverlapping() {
        // Distance = 19.99, sum of radii = 20 -> overlap by 0.01
        final Ball a = new Ball(100, 100, 0, 0, 10.0, Color.RED);
        final Ball b = new Ball(119.99, 100, 0, 0, 10.0, Color.BLUE);
        assertTrue(myEngine.isColliding(a, b),
                "Barely overlapping balls should be colliding");
    }

    /** Checks that balls at the same position are colliding. */
    @Test
    void isCollidingSamePosition() {
        final Ball a = new Ball(100, 100, 0, 0, 10.0, Color.RED);
        final Ball b = new Ball(100, 100, 0, 0, 10.0, Color.BLUE);
        assertTrue(myEngine.isColliding(a, b),
                "Balls at the same position should be colliding");
    }

    /** Checks that overlapping balls with different radii are colliding. */
    @Test
    void isCollidingDifferentRadii() {
        // Ball A: radius 10 at (100,100), Ball B: radius 20 at (125, 100)
        // distance = 25, sum of radii = 30 -> overlapping
        final Ball a = new Ball(100, 100, 0, 0, 10.0, Color.RED);
        final Ball b = new Ball(125, 100, 0, 0, 20.0, Color.BLUE);
        assertTrue(myEngine.isColliding(a, b),
                "Balls with different radii that overlap should be colliding");
    }

    // ===============================================================
    // resolveCollision
    // ===============================================================

    /** Checks that equal-mass balls colliding head-on swap velocities. */
    @Test
    void resolveCollisionEqualMassHeadOn() {
        // Equal mass head-on: velocities should swap
        final double radius = 10.0;
        final Ball a = new Ball(90, 100, HEAD_ON_SPEED, 0, radius, Color.RED);
        final Ball b = new Ball(109, 100, -HEAD_ON_SPEED, 0, radius, Color.BLUE);

        myEngine.resolveCollision(a, b);

        assertEquals(-HEAD_ON_SPEED, a.getVX(), PHYSICS_TOLERANCE,
                "Equal-mass head-on: A's vx should reverse");
        assertEquals(HEAD_ON_SPEED, b.getVX(), PHYSICS_TOLERANCE,
                "Equal-mass head-on: B's vx should reverse");
    }

    /** Checks that a collision conserves momentum in x and y. */
    @Test
    void resolveCollisionConservesMomentum() {
        final Ball a = new Ball(95, 100, 4.0, 1.0, 15.0, Color.RED);
        final Ball b = new Ball(115, 105, -2.0, 3.0, 10.0, Color.BLUE);

        final double momentumXBefore = a.getMass() * a.getVX() + b.getMass() * b.getVX();
        final double momentumYBefore = a.getMass() * a.getVY() + b.getMass() * b.getVY();

        myEngine.resolveCollision(a, b);

        final double momentumXAfter = a.getMass() * a.getVX() + b.getMass() * b.getVX();
        final double momentumYAfter = a.getMass() * a.getVY() + b.getMass() * b.getVY();

        assertEquals(momentumXBefore, momentumXAfter, PHYSICS_TOLERANCE,
                "X momentum should be conserved after collision");
        assertEquals(momentumYBefore, momentumYAfter, PHYSICS_TOLERANCE,
                "Y momentum should be conserved after collision");
    }

    /** Checks that a heavy ball barely slows when it hits a light ball. */
    @Test
    void resolveCollisionHeavyHitsLight() {
        // Heavy ball (radius 30) barely changes when hitting light ball (radius 10)
        final Ball heavy = new Ball(90, 100, 3.0, 0, 30.0, Color.RED);
        final Ball light = new Ball(130, 100, -1.0, 0, 10.0, Color.BLUE);

        final double heavyVXBefore = heavy.getVX();
        myEngine.resolveCollision(heavy, light);

        // Heavy ball should barely change velocity
        final double heavyVXChange = Math.abs(heavy.getVX() - heavyVXBefore);
        assertTrue(heavyVXChange < 2.0,
                "Heavy ball velocity should change only slightly when hitting light ball");
    }

    /** Checks that overlapping balls no longer overlap after a collision. */
    @Test
    void resolveCollisionSeparatesBalls() {
        // Place overlapping balls
        final Ball a = new Ball(100, 100, 2.0, 0, 10.0, Color.RED);
        final Ball b = new Ball(115, 100, -2.0, 0, 10.0, Color.BLUE);

        myEngine.resolveCollision(a, b);

        final double dx = b.getX() - a.getX();
        final double dy = b.getY() - a.getY();
        final double dist = Math.sqrt(dx * dx + dy * dy);
        assertTrue(dist >= a.getRadius() + b.getRadius() - TOLERANCE,
                "Balls should be separated (no overlap) after collision resolution");
    }

    /** Checks that a collision conserves kinetic energy. */
    @Test
    void resolveCollisionConservesKineticEnergy() {
        // Elastic collision should conserve kinetic energy
        final Ball a = new Ball(95, 100, 4.0, 0, 12.0, Color.RED);
        final Ball b = new Ball(117, 100, -3.0, 0, 12.0, Color.BLUE);

        final double energyBefore = a.getKineticEnergy() + b.getKineticEnergy();
        myEngine.resolveCollision(a, b);
        final double energyAfter = a.getKineticEnergy() + b.getKineticEnergy();

        assertEquals(energyBefore, energyAfter, PHYSICS_TOLERANCE,
                "Kinetic energy should be conserved in elastic collision");
    }

    // ===============================================================
    // move
    // ===============================================================

    /** Checks that move adds the velocity to the position. */
    @Test
    void moveUpdatesPositionByVelocity() {
        final Ball ball = new Ball(100, 200, 5.0, -3.0, 10.0, Color.RED);
        myEngine.move(ball);
        assertEquals(MOVED_X, ball.getX(), TOLERANCE,
                "X should increase by vx after move");
        assertEquals(MOVED_Y, ball.getY(), TOLERANCE,
                "Y should increase by vy after move");
    }

    /** Checks that move leaves a ball at rest where it is. */
    @Test
    void moveZeroVelocityNoChange() {
        final Ball ball = new Ball(STILL_X, STILL_Y, 0, 0, 10.0, Color.RED);
        myEngine.move(ball);
        assertEquals(STILL_X, ball.getX(), TOLERANCE,
                "X should not change with zero velocity");
        assertEquals(STILL_Y, ball.getY(), TOLERANCE,
                "Y should not change with zero velocity");
    }

    /** Checks that move with negative velocity decreases the position. */
    @Test
    void moveNegativeVelocity() {
        final Ball ball = new Ball(100, 100, -4.0, -6.0, 10.0, Color.RED);
        myEngine.move(ball);
        assertEquals(MOVED_BACK_X, ball.getX(), TOLERANCE,
                "X should decrease with negative vx");
        assertEquals(MOVED_BACK_Y, ball.getY(), TOLERANCE,
                "Y should decrease with negative vy");
    }

    // ===============================================================
    // bounceOffWalls
    // ===============================================================

    /** Checks that a ball hitting the right wall reverses vx. */
    @Test
    void bounceOffRightWall() {
        // Ball at right edge, moving right -> vx should reverse
        final Ball ball = new Ball(WIDTH - 5, 200, 3.0, 0, 10.0, Color.RED);
        myEngine.bounceOffWalls(ball, WIDTH, HEIGHT, false);
        assertTrue(ball.getVX() < 0,
                "Ball hitting right wall should have negative vx");
    }

    /** Checks that the left wall reverses vx, boosts speed and reports the hit. */
    @Test
    void bounceOffLeftWallReversesVXAndBoostsSpeed() {
        // Ball at left edge, moving left -> vx reverses AND speed boosted
        final Ball ball = new Ball(5, 200, -4.0, 3.0, 10.0, Color.RED);
        final double speedBefore = ball.getSpeed();

        final boolean hitLeft = myEngine.bounceOffWalls(ball, WIDTH, HEIGHT, false);

        assertTrue(hitLeft, "Should return true when hitting left wall");
        assertTrue(ball.getVX() > 0,
                "Ball hitting left wall should have positive vx");
        assertEquals(speedBefore * PhysicsEngine.LEFT_WALL_BOOST, ball.getSpeed(),
                PHYSICS_TOLERANCE,
                "Speed should be multiplied by LEFT_WALL_BOOST on left wall hit");
    }

    /** Checks that a ball hitting the top wall reverses vy. */
    @Test
    void bounceOffTopWall() {
        // Ball at top edge, moving up -> vy should reverse
        final Ball ball = new Ball(400, 5, 0, -3.0, 10.0, Color.RED);
        myEngine.bounceOffWalls(ball, WIDTH, HEIGHT, false);
        assertTrue(ball.getVY() > 0,
                "Ball hitting top wall should have positive vy");
    }

    /** Checks that a ball hitting the bottom wall reverses vy. */
    @Test
    void bounceOffBottomWall() {
        // Ball at bottom edge, moving down -> vy should reverse
        final Ball ball = new Ball(400, HEIGHT - 5, 0, 4.0, 10.0, Color.RED);
        myEngine.bounceOffWalls(ball, WIDTH, HEIGHT, false);
        assertTrue(ball.getVY() < 0,
                "Ball hitting bottom wall should have negative vy");
    }

    /** Checks that a ball inside the bounds keeps its velocity. */
    @Test
    void noBounceWhenInsideBounds() {
        // Ball well inside bounds -> nothing should change
        final Ball ball = new Ball(400, 225, 3.0, 4.0, 10.0, Color.RED);
        final double vxBefore = ball.getVX();
        final double vyBefore = ball.getVY();

        final boolean hitLeft = myEngine.bounceOffWalls(ball, WIDTH, HEIGHT, false);

        assertFalse(hitLeft, "Should not report left wall hit when inside bounds");
        assertEquals(vxBefore, ball.getVX(), TOLERANCE,
                "VX should not change when ball is inside bounds");
        assertEquals(vyBefore, ball.getVY(), TOLERANCE,
                "VY should not change when ball is inside bounds");
    }

    /** Checks that a floor bounce in gravity mode applies BOUNCE_DAMPING. */
    @Test
    void bounceOffBottomWallGravityModeAppliesDamping() {
        // In gravity mode, floor bounce should apply damping
        final Ball ball = new Ball(400, HEIGHT - 5, 0, FALL_SPEED, 10.0, Color.RED);
        myEngine.bounceOffWalls(ball, WIDTH, HEIGHT, true);
        // vy should be reversed and damped
        assertTrue(ball.getVY() < 0,
                "Ball should bounce up from floor in gravity mode");
        assertEquals(FALL_SPEED * PhysicsEngine.BOUNCE_DAMPING, Math.abs(ball.getVY()),
                PHYSICS_TOLERANCE,
                "Floor bounce in gravity mode should apply BOUNCE_DAMPING");
    }

    // ===============================================================
    // applyFriction
    // ===============================================================

    /** Checks that friction reduces speed. */
    @Test
    void applyFrictionReducesSpeed() {
        final Ball ball = new Ball(400, 225, 10.0, 10.0, 15.0, Color.RED);
        final double speedBefore = ball.getSpeed();
        myEngine.applyFriction(ball);
        assertTrue(ball.getSpeed() < speedBefore,
                "Speed should decrease after friction is applied");
    }

    /** Checks that a heavier ball keeps more speed under friction. */
    @Test
    void applyFrictionHeavierBallRetainsMoreSpeed() {
        // Friction formula: v *= FRICTION_COEFFICIENT ^ (1/mass)
        // Heavier ball -> smaller exponent -> less friction
        final Ball heavy = new Ball(400, 225, 10.0, 0, 30.0, Color.RED);
        final Ball light = new Ball(400, 225, 10.0, 0, 10.0, Color.BLUE);

        myEngine.applyFriction(heavy);
        myEngine.applyFriction(light);

        assertTrue(heavy.getSpeed() > light.getSpeed(),
                "Heavier ball should retain more speed after friction");
    }

    /** Checks that friction keeps the speed of a ball at rest at zero. */
    @Test
    void applyFrictionZeroVelocityStaysZero() {
        final Ball ball = new Ball(400, 225, 0, 0, 15.0, Color.RED);
        myEngine.applyFriction(ball);
        assertEquals(0.0, ball.getSpeed(), TOLERANCE,
                "Zero velocity should remain zero after friction");
    }

    /** Checks that many friction ticks slow the ball. */
    @Test
    void applyFrictionMultipleTicksSlowsBall() {
        final Ball ball = new Ball(400, 225, FRICTION_START_VELOCITY, FRICTION_START_VELOCITY,
                15.0, Color.RED);
        for (int i = 0; i < FRICTION_TICKS; i++) {
            myEngine.applyFriction(ball);
        }
        assertTrue(ball.getSpeed() < FRICTION_START_VELOCITY,
                "Ball should be slower after many friction ticks");
    }

    // ===============================================================
    // applyGravity
    // ===============================================================

    /** Checks that one gravity tick adds GRAVITY_ACCELERATION to vy. */
    @Test
    void applyGravityIncreasesVY() {
        final Ball ball = new Ball(400, 225, 0, 0, 15.0, Color.RED);
        myEngine.applyGravity(ball);
        assertEquals(PhysicsEngine.GRAVITY_ACCELERATION, ball.getVY(), TOLERANCE,
                "vy should increase by GRAVITY_ACCELERATION after one tick");
    }

    /** Checks that gravity adds up over several ticks. */
    @Test
    void applyGravityAccumulatesOverTicks() {
        final Ball ball = new Ball(400, 225, 0, 0, 15.0, Color.RED);
        final int ticks = 10;
        for (int i = 0; i < ticks; i++) {
            myEngine.applyGravity(ball);
        }
        assertEquals(PhysicsEngine.GRAVITY_ACCELERATION * ticks, ball.getVY(), TOLERANCE,
                "vy should accumulate gravity over multiple ticks");
    }

    /** Checks that gravity leaves vx unchanged. */
    @Test
    void applyGravityDoesNotAffectVX() {
        final Ball ball = new Ball(400, 225, GRAVITY_TEST_VX, 0, 15.0, Color.RED);
        myEngine.applyGravity(ball);
        assertEquals(GRAVITY_TEST_VX, ball.getVX(), TOLERANCE,
                "Gravity should not affect horizontal velocity");
    }

    // ===============================================================
    // applyShockwave
    // ===============================================================

    /** Checks that a closer ball gets more impulse from a shockwave. */
    @Test
    void applyShockwaveCloserBallGetsMoreImpulse() {
        final Ball close = new Ball(110, 100, 0, 0, 10.0, Color.RED);
        final Ball far = new Ball(300, 100, 0, 0, 10.0, Color.BLUE);
        final List<Ball> balls = new ArrayList<>();
        balls.add(close);
        balls.add(far);

        myEngine.applyShockwave(balls, SHOCK_X, SHOCK_Y, SHOCK_FORCE);

        assertTrue(close.getSpeed() > far.getSpeed(),
                "Closer ball should receive more impulse from shockwave");
    }

    /** Checks that a ball right of the shockwave is pushed right. */
    @Test
    void applyShockwaveDirectionAwayFromClick() {
        // Ball to the right of click should get positive vx
        final Ball ball = new Ball(200, 100, 0, 0, 10.0, Color.RED);
        final List<Ball> balls = new ArrayList<>();
        balls.add(ball);

        myEngine.applyShockwave(balls, SHOCK_X, SHOCK_Y, SHOCK_FORCE);

        assertTrue(ball.getVX() > 0,
                "Ball to the right of shockwave origin should get positive vx");
    }

    /** Checks that a ball above the shockwave is pushed up. */
    @Test
    void applyShockwaveDirectionBallAboveClick() {
        // Ball above click should get negative vy (pushed upward)
        final Ball ball = new Ball(100, 50, 0, 0, 10.0, Color.RED);
        final List<Ball> balls = new ArrayList<>();
        balls.add(ball);

        myEngine.applyShockwave(balls, SHOCK_X, SHOCK_Y, SHOCK_FORCE);

        assertTrue(ball.getVY() < 0,
                "Ball above shockwave origin should get negative vy");
    }

    /** Checks that a shockwave returns a positive max impulse. */
    @Test
    void applyShockwaveReturnsMaxImpulse() {
        final Ball close = new Ball(105, 100, 0, 0, 10.0, Color.RED);
        final Ball far = new Ball(500, 100, 0, 0, 10.0, Color.BLUE);
        final List<Ball> balls = new ArrayList<>();
        balls.add(close);
        balls.add(far);

        final double maxImpulse = myEngine.applyShockwave(balls, SHOCK_X, SHOCK_Y, SHOCK_FORCE);

        assertTrue(maxImpulse > 0,
                "Max impulse should be positive when balls are present");
    }

    /** Checks that a shockwave on an empty list returns zero. */
    @Test
    void applyShockwaveEmptyListReturnsZeroOrNoError() {
        final List<Ball> balls = new ArrayList<>();
        final double maxImpulse = myEngine.applyShockwave(balls, SHOCK_X, SHOCK_Y, SHOCK_FORCE);
        assertEquals(0.0, maxImpulse, TOLERANCE,
                "Shockwave on empty list should return 0 max impulse");
    }

    // ===============================================================
    // calculateTotalEnergy
    // ===============================================================

    /** Checks the total energy of one ball against 0.5 * m * v squared. */
    @Test
    void calculateTotalEnergySingleBall() {
        final double radius = 10.0;
        final double vx = 3.0;
        final double vy = 4.0;
        final Ball ball = new Ball(100, 100, vx, vy, radius, Color.RED);
        final List<Ball> balls = new ArrayList<>();
        balls.add(ball);

        final double mass = Ball.DENSITY * Math.PI * radius * radius;
        final double expected = 0.5 * mass * (vx * vx + vy * vy);

        assertEquals(expected, myEngine.calculateTotalEnergy(balls), TOLERANCE,
                "Total energy of one ball should match 0.5 * m * v^2");
    }

    /** Checks that total energy is the sum of each ball's kinetic energy. */
    @Test
    void calculateTotalEnergyMultipleBalls() {
        final Ball a = new Ball(100, 100, 3.0, 4.0, 10.0, Color.RED);
        final Ball b = new Ball(300, 300, 1.0, 2.0, 20.0, Color.BLUE);
        final List<Ball> balls = new ArrayList<>();
        balls.add(a);
        balls.add(b);

        final double expected = a.getKineticEnergy() + b.getKineticEnergy();
        assertEquals(expected, myEngine.calculateTotalEnergy(balls), TOLERANCE,
                "Total energy should be sum of individual kinetic energies");
    }

    /** Checks that an empty list has zero total energy. */
    @Test
    void calculateTotalEnergyEmptyList() {
        final List<Ball> balls = new ArrayList<>();
        assertEquals(0.0, myEngine.calculateTotalEnergy(balls), TOLERANCE,
                "Total energy of empty list should be 0");
    }

    /** Checks that balls at rest have zero total energy. */
    @Test
    void calculateTotalEnergyStationaryBalls() {
        final Ball a = new Ball(100, 100, 0, 0, 10.0, Color.RED);
        final Ball b = new Ball(300, 300, 0, 0, 20.0, Color.BLUE);
        final List<Ball> balls = new ArrayList<>();
        balls.add(a);
        balls.add(b);

        assertEquals(0.0, myEngine.calculateTotalEnergy(balls), TOLERANCE,
                "Total energy of stationary balls should be 0");
    }
}
