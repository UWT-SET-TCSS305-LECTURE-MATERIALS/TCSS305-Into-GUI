package edu.uw.tcss.bouncingballs.physics;

import edu.uw.tcss.bouncingballs.model.Ball;
import edu.uw.tcss.bouncingballs.model.SimulationState;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A stateless engine containing all physics computation for the bouncing-balls
 * simulation. Every method takes data in and modifies balls or returns values.
 * No Swing or AWT imports — this class is fully testable with plain JUnit.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class PhysicsEngine {

    // ---- Tuning Constants ----

    /** Per-tick velocity multiplier applied when gravity is off. */
    public static final double FRICTION_COEFFICIENT = 0.995;

    /** Reference mass for friction scaling (mass of a radius-20 ball). */
    public static final double REFERENCE_MASS = Ball.DENSITY * Math.PI * 20.0 * 20.0;

    /** Downward acceleration applied each tick in gravity mode (pixels/tick^2). */
    public static final double GRAVITY_ACCELERATION = 0.15;

    /** Fraction of vertical velocity retained on a floor bounce in gravity mode. */
    public static final double BOUNCE_DAMPING = 0.85;

    /** Speed multiplier applied when a ball hits the left wall. */
    public static final double LEFT_WALL_BOOST = 1.25;

    /** Per-tick air drag multiplier applied to all balls in gravity mode. */
    public static final double AIR_DRAG = 0.9985;

    /** Maximum speed at which the left wall boost still applies. */
    public static final double LEFT_WALL_BOOST_CAP = 12.0;

    /** Speed below which a ball is considered at rest. */
    public static final double REST_THRESHOLD = 0.1;

    /** The one half in the kinetic energy formula, (1/2) m v^2. */
    private static final double HALF = 0.5;

    // ---- Main Update ----

    /**
     * Advances the simulation by one tick.
     * <p>Call order:
     * <ol>
     *   <li>Apply forces (gravity or friction)</li>
     *   <li>Move balls</li>
     *   <li>Handle wall collisions</li>
     *   <li>Handle ball-ball collisions</li>
     * </ol>
     *
     * @param state the current simulation state
     * @return the set of balls that hit the left wall this tick
     */
    public Set<Ball> update(final SimulationState state) {
        final Set<Ball> boostedBalls = new HashSet<>();

        if (state.isPaused()) {
            return boostedBalls;
        }

        final List<Ball> balls = state.getBalls();
        final int width = state.getWidth();
        final int height = state.getHeight();
        final boolean gravityOn = state.isGravityOn();

        // 1. Apply forces
        for (final Ball ball : balls) {
            applyForces(ball, height, gravityOn);
        }

        // 2. Move balls
        for (final Ball ball : balls) {
            move(ball);
        }

        // 3. Handle wall collisions
        for (final Ball ball : balls) {
            final boolean hitLeft = bounceOffWalls(ball, width, height, gravityOn);
            if (hitLeft) {
                boostedBalls.add(ball);
            }
        }

        // 4. Handle ball-ball collisions
        resolveAllCollisions(balls);

        return boostedBalls;
    }

    /**
     * Applies this tick's forces to one ball: gravity, air drag and floor friction
     * when gravity is on, surface friction when it is off. A slow ball resting on
     * the floor is stopped completely.
     *
     * @param ball the ball
     * @param height the height of the simulation area
     * @param gravityOn whether gravity is on
     */
    private void applyForces(final Ball ball, final int height,
                             final boolean gravityOn) {
        if (gravityOn) {
            final boolean onFloor = ball.getY() + ball.getRadius() >= height - 1;
            if (onFloor && ball.getSpeed() < REST_THRESHOLD) {
                // Ball is resting on the floor, so stop it completely
                ball.setVX(0);
                ball.setVY(0);
                ball.setY(height - ball.getRadius());
            } else {
                applyGravity(ball);
                // Air drag slows all airborne balls
                ball.setVX(ball.getVX() * AIR_DRAG);
                ball.setVY(ball.getVY() * AIR_DRAG);
                // Floor friction for balls on the ground
                if (onFloor) {
                    applyFriction(ball);
                }
            }
        } else {
            applyFriction(ball);
        }
    }

    /**
     * Checks every pair of balls once and resolves each pair that is colliding.
     *
     * @param balls the balls
     */
    private void resolveAllCollisions(final List<Ball> balls) {
        for (int i = 0; i < balls.size(); i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                final Ball a = balls.get(i);
                final Ball b = balls.get(j);
                if (isColliding(a, b)) {
                    resolveCollision(a, b);
                }
            }
        }
    }

    // ---- Movement ----

    /**
     * Moves a ball by its velocity for one tick.
     *
     * @param ball the ball to move
     */
    public void move(final Ball ball) {
        ball.setX(ball.getX() + ball.getVX());
        ball.setY(ball.getY() + ball.getVY());
    }

    // ---- Wall Collisions ----

    /**
     * Bounces a ball off the bounding-box walls. In gravity mode the floor
     * bounce applies damping. Left-wall hits apply a speed boost.
     *
     * @param ball      the ball to check
     * @param width     the simulation area width
     * @param height    the simulation area height
     * @param gravityOn whether gravity mode is active
     * @return {@code true} if the ball hit the left wall
     */
    public boolean bounceOffWalls(final Ball ball, final int width,
                                  final int height, final boolean gravityOn) {
        boolean hitLeft = false;
        final double r = ball.getRadius();

        // Left wall
        if (ball.getX() - r <= 0) {
            ball.setVX(Math.abs(ball.getVX()));
            ball.setX(r);
            // Apply speed boost only if below the cap
            final double speed = ball.getSpeed();
            if (speed > 0 && speed < LEFT_WALL_BOOST_CAP) {
                ball.setVX(ball.getVX() * LEFT_WALL_BOOST);
                ball.setVY(ball.getVY() * LEFT_WALL_BOOST);
            }
            hitLeft = true;
        }

        // Right wall
        if (ball.getX() + r >= width) {
            ball.setVX(-Math.abs(ball.getVX()));
            ball.setX(width - r);
        }

        // Top wall
        if (ball.getY() - r <= 0) {
            ball.setVY(Math.abs(ball.getVY()));
            ball.setY(r);
        }

        // Bottom wall (floor)
        if (ball.getY() + r >= height) {
            if (gravityOn) {
                ball.setVY(ball.getVY() * -BOUNCE_DAMPING);
            } else {
                ball.setVY(-Math.abs(ball.getVY()));
            }
            ball.setY(height - r);
        }

        // Rest check: stop if moving too slowly
        if (ball.getSpeed() < REST_THRESHOLD) {
            ball.setVX(0);
            ball.setVY(0);
        }

        return hitLeft;
    }

    // ---- Ball-Ball Collisions ----

    /**
     * Returns {@code true} if two balls overlap (distance between centers is
     * less than the sum of their radii).
     *
     * @param a the first ball
     * @param b the second ball
     * @return {@code true} if the balls are colliding
     */
    public boolean isColliding(final Ball a, final Ball b) {
        final double dx = b.getX() - a.getX();
        final double dy = b.getY() - a.getY();
        final double distSq = dx * dx + dy * dy;
        final double radiiSum = a.getRadius() + b.getRadius();
        return distSq < radiiSum * radiiSum;
    }

    /**
     * Resolves an elastic collision between two balls, accounting for mass.
     * Separates overlapping balls first to prevent sticking, then applies
     * the elastic collision velocity formulas.
     *
     * @param a the first ball
     * @param b the second ball
     */
    public void resolveCollision(final Ball a, final Ball b) {
        final double dx = b.getX() - a.getX();
        final double dy = b.getY() - a.getY();
        final double dist = Math.sqrt(dx * dx + dy * dy);

        // Perfectly overlapping centers have no collision direction, so skip them
        if (dist != 0) {
            // Collision normal (unit vector from A to B)
            final double nx = dx / dist;
            final double ny = dy / dist;

            // 1. Separate overlapping balls
            final double overlap = a.getRadius() + b.getRadius() - dist;
            if (overlap > 0) {
                final double halfOverlap = overlap / 2.0;
                a.setX(a.getX() - halfOverlap * nx);
                a.setY(a.getY() - halfOverlap * ny);
                b.setX(b.getX() + halfOverlap * nx);
                b.setY(b.getY() + halfOverlap * ny);
            }

            // 2. Relative velocity along the collision normal
            final double dvx = a.getVX() - b.getVX();
            final double dvy = a.getVY() - b.getVY();
            final double dotProduct = dvx * nx + dvy * ny;

            // Bounce only if the balls are moving toward each other
            if (dotProduct > 0) {
                applyImpulse(a, b, nx, ny, dotProduct);
            }
        }
    }

    /**
     * Applies the elastic-collision impulse along the collision normal.
     * The heavier ball's velocity changes less than the lighter one's.
     *
     * @param a the first ball
     * @param b the second ball
     * @param nx the x component of the unit normal from A to B
     * @param ny the y component of the unit normal from A to B
     * @param dot the relative velocity of A toward B along the normal
     */
    private void applyImpulse(final Ball a, final Ball b, final double nx,
                              final double ny, final double dot) {
        final double m1 = a.getMass();
        final double m2 = b.getMass();
        final double totalMass = m1 + m2;

        final double impulseA = 2.0 * m2 / totalMass * dot;
        final double impulseB = 2.0 * m1 / totalMass * dot;

        a.setVX(a.getVX() - impulseA * nx);
        a.setVY(a.getVY() - impulseA * ny);
        b.setVX(b.getVX() + impulseB * nx);
        b.setVY(b.getVY() + impulseB * ny);
    }

    // ---- Forces ----

    /**
     * Applies friction by scaling velocity by
     * {@code FRICTION_COEFFICIENT ^ (REFERENCE_MASS / mass)}.
     * A medium ball (radius 20) gets the base friction coefficient applied
     * directly. Lighter balls slow faster, heavier balls slow slower.
     *
     * @param ball the ball to apply friction to
     */
    public void applyFriction(final Ball ball) {
        final double factor =
                Math.pow(FRICTION_COEFFICIENT, REFERENCE_MASS / ball.getMass());
        ball.setVX(ball.getVX() * factor);
        ball.setVY(ball.getVY() * factor);
    }

    /**
     * Applies gravity by adding {@link #GRAVITY_ACCELERATION} to the ball's
     * vertical velocity.
     *
     * @param ball the ball to apply gravity to
     */
    public void applyGravity(final Ball ball) {
        ball.setVY(ball.getVY() + GRAVITY_ACCELERATION);
    }

    // ---- Shockwave ----

    /**
     * Applies a shockwave originating from the given point. Each ball receives
     * a velocity impulse inversely proportional to its distance from the origin.
     *
     * @param balls the list of balls to affect
     * @param x     the x-coordinate of the shockwave origin
     * @param y     the y-coordinate of the shockwave origin
     * @param force the force magnitude of the shockwave
     * @return the maximum impulse applied to any single ball
     */
    public double applyShockwave(final List<Ball> balls,
                                 final double x, final double y,
                                 final double force) {
        double maxImpulse = 0;

        for (final Ball ball : balls) {
            final double dx = ball.getX() - x;
            final double dy = ball.getY() - y;
            final double distance = Math.sqrt(dx * dx + dy * dy);
            final double impulse = force / Math.max(distance, 1.0);

            if (distance > 0) {
                // Unit vector away from click point
                final double ux = dx / distance;
                final double uy = dy / distance;
                ball.setVX(ball.getVX() + impulse * ux);
                ball.setVY(ball.getVY() + impulse * uy);
            } else {
                // Ball is right at the click point — push upward
                ball.setVY(ball.getVY() - impulse);
            }

            if (impulse > maxImpulse) {
                maxImpulse = impulse;
            }
        }

        return maxImpulse;
    }

    // ---- Energy ----

    /**
     * Returns the total kinetic energy of all balls:
     * {@code sum of 0.5 * mass * speed^2}.
     *
     * @param balls the list of balls
     * @return the total kinetic energy
     */
    public double calculateTotalEnergy(final List<Ball> balls) {
        double total = 0;
        for (final Ball ball : balls) {
            final double speed = ball.getSpeed();
            total += HALF * ball.getMass() * speed * speed;
        }
        return total;
    }
}
