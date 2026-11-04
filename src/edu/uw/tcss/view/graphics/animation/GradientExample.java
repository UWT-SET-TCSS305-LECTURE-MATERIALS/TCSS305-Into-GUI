/*
 * A first Swing timer example.
 *
 * TCSS 305
 */

package edu.uw.tcss.view.graphics.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serial;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * A program to demonstrate the use of a Swing timer.
 *
 * @author Charles Bryan
 * @version Autumn 2026
 */
// The timers stay fields so start() can switch them back on.
@SuppressWarnings("FieldCanBeLocal")
public class GradientExample extends JPanel {

    // Constants

    /** The side length (in pixels) of the bounding box of the shape. */
    public static final int BOUNDING_BOX_SIDE = 200;

    /** The preferred window size. */
    public static final Dimension PREFERRED_SIZE = new Dimension(800, 400);

    /** The default delay (in milliseconds) for the move timer. */
    public static final int MOVE_DELAY = 10;

    /** The delay (in milliseconds) for the color timer. */
    public static final int COLOR_DELAY = 100;

    /** The initial delay (in milliseconds) for the move timer. */
    public static final int INITIAL_DELAY = 0;

    /** The animation step (in pixels). */
    public static final int ANIMATION_STEP = 1;

    /** A generated version ID for Serialization. */
    @Serial
    private static final long serialVersionUID = -6257548485239497170L;

    /** The width (in pixels) the gradient keeps clear of the right edge. */
    private static final int BAR_WIDTH = 75;

    /** The starting x coordinate of the radial gradient's center. */
    private static final float CENTER_X = 200;

    /** The starting y coordinate of the radial gradient's center. */
    private static final float CENTER_Y = 100;

    /** The radius of the circle the radial gradient's center travels. */
    private static final double CIRCLE_RADIUS = 2.0;

    /** The starting middle percentage of the radial gradient. */
    private static final float INNER_START = 0.1f;

    /** The smallest middle percentage of the radial gradient. */
    private static final double INNER_MIN = 0.1;

    /** The largest middle percentage of the radial gradient. */
    private static final float INNER_MAX = 0.9f;

    /** How much the middle percentage changes on each color timer tick. */
    private static final float INNER_STEP = 0.01f;

    // Instance fields

    /** The timer that controls the movement of the shape. */
    private final Timer myMoveTimer;

    /** The timer that controls the color of the Shape. */
    private final Timer myColorTimer;

    /** The center of the radial gradient. */
    private final Point2D myCenter = new Point2D.Float(CENTER_X, CENTER_Y);

    /** The direction the linear gradient moves along the x axis. */
    private double myXDir = 1;

    /** The offset of the linear gradient along the x axis. */
    private double myX;

    /** The middle percentage of the radial gradient. */
    private float myInner = INNER_START;

    /** The change applied to the middle percentage on each color timer tick. */
    private float myDir = INNER_STEP;

    /** The angle (in degrees) of the radial gradient's center on its circle. */
    private double myAngle;

    // Constructor

    /**
     * Constructs a new MovingShapePanel; the initial location of the shape is
     * the upper-left corner, initially it is moving diagonally downward and to
     * the right, and its color is blue.
     */
    public GradientExample() {
        super(true); // use double buffering

        myMoveTimer = new Timer(MOVE_DELAY, new MoveListener());
        myColorTimer = new Timer(COLOR_DELAY, new RadialGradiantChanger());

        setupAppearance();
    }

    /** Sets up the appearance of the panel and frame. */
    private void setupAppearance() {

        setBackground(Color.WHITE);
        setPreferredSize(PREFERRED_SIZE);
    }

    /** Starts the timer. */
    public void start() {
//        myMoveTimer.start();
//        myColorTimer.start();
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        final Shape s = new Rectangle2D.Double(0, 0, 400, 400);

        final Point2D start = new Point2D.Double(myX + 100, myX + 100);
        final Point2D end = new Point2D.Double(myX + 200, myX + 200);

        final GradientPaint gradient =
                        new GradientPaint(start, new Color(30, 100, 100), end, Color.WHITE, true);

        g2d.setPaint(gradient);
        g2d.fill(s);


//        final Point2D center = new Point2D.Float(200, 200);
//        final float radius = 200;
//        final float[] dist = {0.0f, myInner, 1.0f};
//        final Color[] colors = {Color.RED, Color.YELLOW, Color.BLACK};
//
//
//        final RadialGradientPaint p =
//                new RadialGradientPaint(center, radius, dist, colors);
//
//        g2d.setPaint(p);
//        g2d.fill(s);
//
//        g2d.setPaint(Color.BLUE);
//        g2d.fillOval(300, 300, 5, 5);

    }

    // ************* MAIN ************************************************

    /**
     * Creates a MovingShapePanel in a frame and starts the animation.
     *
     * @param args Command line parameters - ignored in this program
     */
    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        final GradientExample panel = new GradientExample();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        panel.start(); // starts the timer


    }

    // *********** Inner Class Listener   *********************************

    /**
     * A class that listens for timer events and moves the shape, checking for
     * the window boundaries and changing direction as appropriate.
     */
    private final class MoveListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {

            myX += myXDir;
            if (myX > getWidth() - BAR_WIDTH) {
                myXDir = -1;
            } else if (myX < 0) {
                myXDir = 1;
            }

            //Uncomment for radial gradient movement

            myCenter.setLocation(myCenter.getX(), myCenter.getY());
            pointOnCircle(CIRCLE_RADIUS, myAngle, myCenter);
            myAngle += 1;

            repaint();
        }

        /**
         * Moves a point to the given angle on a circle around it.
         *
         * @param radius the radius of the circle
         * @param angleInDegrees the angle on the circle, in degrees
         * @param origin the point to move
         */
        public void pointOnCircle(final double radius, final double angleInDegrees,
                                  final Point2D origin) {
            final double angle = Math.toRadians(angleInDegrees);
            final double x = (radius * Math.cos(angle)) + origin.getX();
            final double y = (radius * Math.sin(angle)) + origin.getY();

            origin.setLocation(x, y);
        }

    } // end of MoveListener

    /**
     * A class that listens for timer events and changes the middle
     * percentage of a radial gradient, checking for
     * the boundaries .99 and .01.
     */
    class RadialGradiantChanger implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent event) {
            myInner += myDir;

            if (myInner >= INNER_MAX || myInner <= INNER_MIN) {
                myDir = -myDir;
            }


            repaint();
        }
    } // end of RadialGradiantChanger

} // end of MovingShapePanel
