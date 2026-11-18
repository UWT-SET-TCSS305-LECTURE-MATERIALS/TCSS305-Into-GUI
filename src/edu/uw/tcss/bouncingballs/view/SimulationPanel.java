package edu.uw.tcss.bouncingballs.view;

import edu.uw.tcss.bouncingballs.model.Ball;
import edu.uw.tcss.bouncingballs.model.SimulationState;
import edu.uw.tcss.bouncingballs.physics.PhysicsEngine;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The main rendering surface for the bouncing-balls simulation.
 * Draws balls, slingshot preview, shockwave effects, and left-wall
 * boost visuals. Handles mouse input for ball creation (slingshot),
 * shockwave triggering (Shift+click), and ball editing (click when paused).
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class SimulationPanel extends JPanel
        implements MouseListener, MouseMotionListener {

    /** UW Purple color used for left-click ball creation. */
    private static final Color UW_PURPLE = new Color(75, 46, 131);

    /** UW Gold color used for right-click ball creation. */
    private static final Color UW_GOLD = new Color(183, 165, 122);

    /** Minimum ball radius for random generation. */
    private static final int MIN_RADIUS = 10;

    /** Maximum ball radius for random generation. */
    private static final int MAX_RADIUS = 30;

    /** Minimum drag distance to be treated as a slingshot (vs. a click). */
    private static final int MIN_DRAG_DISTANCE = 5;

    /** Maximum launch speed in pixels per tick. */
    private static final double MAX_LAUNCH_SPEED = 15.0;

    /** Velocity scale factor from drag pixels to launch speed. */
    private static final double VELOCITY_SCALE = 0.08;

    /** Force magnitude for shockwave. */
    private static final double SHOCKWAVE_FORCE = 500.0;

    /** Number of radial rays in the shockwave visual. */
    private static final int SHOCKWAVE_RAYS = 14;

    /** Duration of the shockwave animation in milliseconds. */
    private static final int SHOCKWAVE_DURATION = 500;

    /** Interval for the shockwave animation timer in milliseconds. */
    private static final int EFFECT_TIMER_DELAY = 16;

    /** Duration of the wall flash animation in milliseconds. */
    private static final int WALL_FLASH_DURATION = 200;

    /** Duration of the ball glow/trail animation in milliseconds. */
    private static final int GLOW_TRAIL_DURATION = 500;

    /** Number of trail afterimages for a boosted ball. */
    private static final int TRAIL_LENGTH = 4;

    /** Gold used for the wall flash and the boost glow and trail. */
    private static final Color GLOW_GOLD = new Color(255, 215, 0);

    /** Pale yellow used for the shockwave rays. */
    private static final Color SHOCKWAVE_COLOR = new Color(255, 255, 200);

    /** Width of the left-wall flash, in pixels. */
    private static final int WALL_FLASH_WIDTH = 6;

    /** Maximum preview line width for the slingshot. */
    private static final float MAX_LINE_WIDTH = 5.0f;

    /** The simulation state providing the balls to draw. */
    private final SimulationState myState;

    /** The physics engine for shockwave calls. */
    private final PhysicsEngine myPhysicsEngine;

    /** The ball editor overlay (glass pane reference). */
    private BallEditorOverlay myEditorOverlay;

    /** Random generator for ball radius. */
    private final Random myRandom;

    /** The point where a mouse press started (for slingshot). */
    private Point myPressPoint;

    /** The current mouse drag point (for slingshot preview). */
    private Point myDragPoint;

    /** Whether the current press used the right mouse button. */
    private boolean myRightClick;

    /** Active shockwave visual effects. */
    private final List<ShockwaveEffect> myShockwaves;

    /** Timer that drives shockwave and glow animations. */
    private final Timer myEffectTimer;

    /** Wall flash alpha (0.0 = invisible, 1.0 = fully visible). */
    private float myWallFlashAlpha;

    /** Active ball glow/trail effects. */
    private final List<GlowTrailEffect> myGlowTrails;

    /**
     * Creates a simulation panel that renders the given state.
     *
     * @param state the simulation state to render
     * @param physicsEngine the physics engine for shockwave calls
     */
    public SimulationPanel(final SimulationState state,
                           final PhysicsEngine physicsEngine) {
        myState = state;
        myPhysicsEngine = physicsEngine;
        myRandom = new Random();
        myShockwaves = new ArrayList<>();
        myGlowTrails = new ArrayList<>();

        setBackground(Color.BLACK);
        addMouseListener(this);
        addMouseMotionListener(this);

        myEffectTimer = new Timer(EFFECT_TIMER_DELAY, e -> tickEffects());
        myEffectTimer.start();
    }

    /**
     * Sets the ball editor overlay reference so that clicking a ball
     * while paused can open the editor.
     *
     * @param overlay the ball editor overlay
     */
    public void setEditorOverlay(final BallEditorOverlay overlay) {
        myEditorOverlay = overlay;
    }

    /**
     * Notifies this panel that the given balls hit the left wall this tick,
     * triggering boost visual effects for each.
     *
     * @param boostedBalls the set of balls that hit the left wall
     */
    public void notifyBoostedBalls(final Set<Ball> boostedBalls) {
        if (!boostedBalls.isEmpty()) {
            myWallFlashAlpha = 1.0f;
            for (final Ball ball : boostedBalls) {
                myGlowTrails.add(new GlowTrailEffect(ball));
            }
        }
    }

    // ---- Rendering ----

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw glow trails behind balls
        drawGlowTrails(g2);

        // Draw balls
        for (final Ball ball : myState.getBalls()) {
            drawBall(g2, ball);
        }

        // Draw left wall flash
        if (myWallFlashAlpha > 0f) {
            drawWallFlash(g2);
        }

        // Draw slingshot preview
        if (myPressPoint != null && myDragPoint != null) {
            drawSlingshotPreview(g2);
        }

        // Draw shockwave effects
        for (final ShockwaveEffect effect : myShockwaves) {
            drawShockwave(g2, effect);
        }
    }

    /**
     * Draws a single ball as a filled circle.
     *
     * @param g2 the graphics context
     * @param ball the ball to draw
     */
    private void drawBall(final Graphics2D g2, final Ball ball) {
        final int r = (int) ball.getRadius();
        final int x = (int) (ball.getX() - r);
        final int y = (int) (ball.getY() - r);
        g2.setColor(ball.getColor());
        g2.fillOval(x, y, r * 2, r * 2);
    }

    /**
     * Draws the slingshot preview line from the press point to the
     * current drag position, with an arrow indicating launch direction.
     *
     * @param g2 the graphics context
     */
    private void drawSlingshotPreview(final Graphics2D g2) {
        final int dx = myDragPoint.x - myPressPoint.x;
        final int dy = myDragPoint.y - myPressPoint.y;
        final double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < MIN_DRAG_DISTANCE) {
            return;
        }

        // Line color indicates ball color
        final Color previewColor = myRightClick ? UW_GOLD : UW_PURPLE;
        final float width = (float) Math.min(dist / 40.0, MAX_LINE_WIDTH);
        g2.setColor(previewColor);
        g2.setStroke(new BasicStroke(width));

        // Draw drag line
        g2.drawLine(myPressPoint.x, myPressPoint.y,
                       myDragPoint.x, myDragPoint.y);

        // Arrow at the launch end (opposite of drag direction)
        final double launchDirX = -dx / dist;
        final double launchDirY = -dy / dist;
        final int arrowLen = 12;
        final int ax = (int) (myPressPoint.x + launchDirX * arrowLen);
        final int ay = (int) (myPressPoint.y + launchDirY * arrowLen);

        // Arrow wings
        final double wingAngle = Math.PI / 6;
        final int wingLen = 8;
        for (final double sign : new double[]{-1, 1}) {
            final double wa = Math.atan2(launchDirY, launchDirX)
                    + sign * (Math.PI - wingAngle);
            final int wx = (int) (ax + wingLen * Math.cos(wa));
            final int wy = (int) (ay + wingLen * Math.sin(wa));
            g2.drawLine(ax, ay, wx, wy);
        }

        g2.setStroke(new BasicStroke(1));
    }

    /**
     * Draws the left wall flash effect.
     *
     * @param g2 the graphics context
     */
    private void drawWallFlash(final Graphics2D g2) {
        final int alpha = (int) (myWallFlashAlpha * 180);
        g2.setColor(withAlpha(GLOW_GOLD, alpha));
        g2.fillRect(0, 0, WALL_FLASH_WIDTH, getHeight());
    }

    /**
     * Draws a single shockwave radial pulse effect.
     *
     * @param g2 the graphics context
     * @param effect the shockwave effect data
     */
    private void drawShockwave(final Graphics2D g2,
                               final ShockwaveEffect effect) {
        final float progress = effect.getProgress();
        final int alpha = (int) ((1.0f - progress) * 200);
        if (alpha <= 0) {
            return;
        }
        g2.setColor(withAlpha(SHOCKWAVE_COLOR, alpha));
        g2.setStroke(new BasicStroke(2.0f));

        final double maxRadius = 120.0 * progress;
        final double angleStep = 2 * Math.PI / SHOCKWAVE_RAYS;
        for (int i = 0; i < SHOCKWAVE_RAYS; i++) {
            final double angle = i * angleStep;
            final double innerR = maxRadius * 0.3;
            final int x1 = (int) (effect.myX + innerR * Math.cos(angle));
            final int y1 = (int) (effect.myY + innerR * Math.sin(angle));
            final int x2 = (int) (effect.myX + maxRadius * Math.cos(angle));
            final int y2 = (int) (effect.myY + maxRadius * Math.sin(angle));
            g2.drawLine(x1, y1, x2, y2);
        }
        g2.setStroke(new BasicStroke(1));
    }

    /**
     * Draws glow and trail effects for boosted balls.
     *
     * @param g2 the graphics context
     */
    private void drawGlowTrails(final Graphics2D g2) {
        for (final GlowTrailEffect effect : myGlowTrails) {
            final float progress = effect.getProgress();
            final int alpha = (int) ((1.0f - progress) * 150);
            if (alpha <= 0) {
                continue;
            }
            final Ball ball = effect.myBall;

            // Glow behind ball
            final int glowR = (int) (ball.getRadius() * 1.6);
            g2.setColor(withAlpha(GLOW_GOLD, alpha));
            g2.fillOval((int) (ball.getX() - glowR),
                           (int) (ball.getY() - glowR),
                           glowR * 2, glowR * 2);

            // Trail afterimages
            final double trailVX = ball.getVX();
            final double trailVY = ball.getVY();
            for (int i = 1; i <= TRAIL_LENGTH; i++) {
                final int trailAlpha = alpha / (i + 1);
                if (trailAlpha <= 0) {
                    break;
                }
                final int tr = (int) (ball.getRadius() * (1.0 - 0.15 * i));
                final int tx = (int) (ball.getX() - trailVX * i * 2 - tr);
                final int ty = (int) (ball.getY() - trailVY * i * 2 - tr);
                g2.setColor(withAlpha(GLOW_GOLD, trailAlpha));
                g2.fillOval(tx, ty, tr * 2, tr * 2);
            }
        }
    }

    // ---- Effect Animation ----

    /**
     * Advances all visual effects by one frame and removes finished ones.
     */
    private void tickEffects() {
        // Advance shockwaves
        final Iterator<ShockwaveEffect> swIter = myShockwaves.iterator();
        while (swIter.hasNext()) {
            if (swIter.next().isFinished()) {
                swIter.remove();
            }
        }

        // Advance glow trails
        final Iterator<GlowTrailEffect> gtIter = myGlowTrails.iterator();
        while (gtIter.hasNext()) {
            if (gtIter.next().isFinished()) {
                gtIter.remove();
            }
        }

        // Fade wall flash
        if (myWallFlashAlpha > 0f) {
            myWallFlashAlpha -= (float) EFFECT_TIMER_DELAY / WALL_FLASH_DURATION;
            if (myWallFlashAlpha < 0f) {
                myWallFlashAlpha = 0f;
            }
        }

        // Only repaint if there are active effects (simulation repaint is
        // driven by the main timer in SimulationFrame)
        if (!myShockwaves.isEmpty() || !myGlowTrails.isEmpty()
                || myWallFlashAlpha > 0f) {
            repaint();
        }
    }

    // ---- Mouse Handling ----

    @Override
    public void mousePressed(final MouseEvent event) {
        if (myState.isPaused()) {
            // Paused: a click on a ball opens the editor
            final Ball clicked = findBallAt(event.getX(), event.getY());
            if (clicked != null && myEditorOverlay != null) {
                myEditorOverlay.editBall(clicked);
            }
        } else if (event.isShiftDown()) {
            // Shift+click: shockwave
            triggerShockwave(event.getX(), event.getY());
        } else {
            // Start a slingshot drag
            myPressPoint = event.getPoint();
            myDragPoint = event.getPoint();
            myRightClick = SwingUtilities.isRightMouseButton(event);
        }
    }

    /**
     * Returns the given color with the given alpha.
     *
     * @param color the base color
     * @param alpha the alpha, from 0 (invisible) to 255 (opaque)
     * @return the color with that alpha
     */
    private static Color withAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        if (myPressPoint != null) {
            myDragPoint = event.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        if (myPressPoint == null) {
            return;
        }

        final int dx = event.getX() - myPressPoint.x;
        final int dy = event.getY() - myPressPoint.y;
        final double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist >= MIN_DRAG_DISTANCE) {
            // Create ball with slingshot velocity (opposite direction of drag)
            double speed = dist * VELOCITY_SCALE;
            if (speed > MAX_LAUNCH_SPEED) {
                speed = MAX_LAUNCH_SPEED;
            }
            final double vx = -dx / dist * speed;
            final double vy = -dy / dist * speed;
            final int radius = MIN_RADIUS
                    + myRandom.nextInt(MAX_RADIUS - MIN_RADIUS + 1);
            final Color color = myRightClick ? UW_GOLD : UW_PURPLE;

            myState.addBall(new Ball(myPressPoint.x, myPressPoint.y,
                                    vx, vy, radius, color));
        } else if (event.isShiftDown()) {
            // Short drag with shift = shockwave
            triggerShockwave(myPressPoint.x, myPressPoint.y);
        }

        myPressPoint = null;
        myDragPoint = null;
        repaint();
    }

    @Override
    public void mouseClicked(final MouseEvent event) { }

    @Override
    public void mouseEntered(final MouseEvent event) { }

    @Override
    public void mouseExited(final MouseEvent event) { }

    @Override
    public void mouseMoved(final MouseEvent event) { }

    // ---- Helpers ----

    /**
     * Triggers a shockwave at the given screen coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void triggerShockwave(final int x, final int y) {
        myPhysicsEngine.applyShockwave(
                myState.getBalls(), x, y, SHOCKWAVE_FORCE);
        myShockwaves.add(new ShockwaveEffect(x, y));
    }

    /**
     * Finds the ball at the given screen coordinates, or null if none.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the ball at the point, or null
     */
    private Ball findBallAt(final int x, final int y) {
        for (final Ball ball : myState.getBalls()) {
            final double dx = ball.getX() - x;
            final double dy = ball.getY() - y;
            if (dx * dx + dy * dy <= ball.getRadius() * ball.getRadius()) {
                return ball;
            }
        }
        return null;
    }

    // ---- Effect Data Classes ----

    /**
     * Tracks the state of a single shockwave visual effect.
     */
    private static final class ShockwaveEffect {

        /** The x coordinate of the shockwave origin. */
        private final int myX;

        /** The y coordinate of the shockwave origin. */
        private final int myY;

        /** The system time when this effect started. */
        private final long myStartTime;

        /**
         * Creates a shockwave effect at the given coordinates.
         *
         * @param x the x coordinate
         * @param y the y coordinate
         */
        ShockwaveEffect(final int x, final int y) {
            myX = x;
            myY = y;
            myStartTime = System.currentTimeMillis();
        }

        /**
         * Returns the animation progress as a value from 0.0 to 1.0.
         *
         * @return the progress ratio
         */
        float getProgress() {
            final long elapsed = System.currentTimeMillis() - myStartTime;
            return Math.min((float) elapsed / SHOCKWAVE_DURATION, 1.0f);
        }

        /**
         * Returns whether this effect has finished its animation.
         *
         * @return true if the effect is complete
         */
        boolean isFinished() {
            return getProgress() >= 1.0f;
        }
    }

    /**
     * Tracks the state of a glow/trail effect for a boosted ball.
     */
    private static final class GlowTrailEffect {

        /** The ball that was boosted. */
        private final Ball myBall;

        /** The system time when this effect started. */
        private final long myStartTime;

        /**
         * Creates a glow trail effect for the given ball.
         *
         * @param ball the boosted ball
         */
        GlowTrailEffect(final Ball ball) {
            myBall = ball;
            myStartTime = System.currentTimeMillis();
        }

        /**
         * Returns the animation progress as a value from 0.0 to 1.0.
         *
         * @return the progress ratio
         */
        float getProgress() {
            final long elapsed = System.currentTimeMillis() - myStartTime;
            return Math.min((float) elapsed / GLOW_TRAIL_DURATION, 1.0f);
        }

        /**
         * Returns whether this effect has finished its animation.
         *
         * @return true if the effect is complete
         */
        boolean isFinished() {
            return getProgress() >= 1.0f;
        }
    }
}
