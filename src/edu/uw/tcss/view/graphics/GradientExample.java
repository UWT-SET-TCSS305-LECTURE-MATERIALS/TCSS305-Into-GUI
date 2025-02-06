/*
 * A first Swing timer example.
 *
 * TCSS 305
 */

package edu.uw.tcss.view.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

/**
 * A program to demonstrate the use of a Swing timer.
 *
 * @author Charles Bryan
 * @version 1.3
 */
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
    private static final long serialVersionUID = -6257548485239497170L;

    // Instance fields

    /** The timer that controls the movement of the shape. */
    private final Timer myMoveTimer;

    /** The timer that controls the color of the Shape. */
    private final Timer myColorTimer;

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
        myColorTimer.start();
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        final Shape s = new Rectangle2D.Double(0, 0, 400, 400);

//        final Point2D start = new Point2D.Double(myX + 100, myX + 100);
//        final Point2D end = new Point2D.Double(myX + 200, myX + 200);
//
//        final GradientPaint gradient =
//                        new GradientPaint(start, new Color(30, 100, 100), end, Color.WHITE, true);
//
//        g2d.setPaint(gradient);
//        g2d.fill(s);


        final Point2D center = new Point2D.Float(200, 200);
        final float radius = 200;
        final float[] dist = {0.0f, myInner, 1.0f};
        final Color[] colors = {Color.RED, Color.YELLOW, Color.BLACK};


        final RadialGradientPaint p =
                new RadialGradientPaint(center, radius, dist, colors);

        g2d.setPaint(p);
        g2d.fill(s);
//
        g2d.setPaint(Color.BLUE);
        g2d.fillOval(300, 300, 5, 5);

    }

    private Point2D myCenter = new Point2D.Float(200, 100);


    private static final int BAR_WIDTH = 75;

    private double myXDir = 1;
    private double myX = 0;

    private float myInner = 0.1f;
    private float myOuter = 0.51f;
    private float myDir = 0.01f;

    private double myAngle = 0;

    private Color[] myColors = {Color.YELLOW, Color.RED, Color.YELLOW, Color.RED, Color.BLACK};


    // ************* MAIN ************************************************

    /**
     * Creates a MovingShapePanel in a frame and starts the animation.
     *
     * @param theArgs Command line parameters - ignored in this program
     */
    public static void main(final String[] theArgs) {
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
    private class MoveListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {

            myX += myXDir;
            if (myX > getWidth() - BAR_WIDTH) {
                myXDir = -1;
            } else if (myX < 0) {
                myXDir = 1;
            }

            //Uncomment for radial gradient movement

            myCenter.setLocation(myCenter.getX(), myCenter.getY());
            pointOnCircle(2.0, myAngle, myCenter);
            myAngle += 1;

            repaint();
        }

        public void pointOnCircle(double radius, double angleInDegrees, Point2D origin)
        {
            // Convert from degrees to radians via multiplication by PI/180
            double x = (radius * Math.cos(angleInDegrees * Math.PI / 180F)) + origin.getX();
            double y = (radius * Math.sin(angleInDegrees * Math.PI / 180F)) + origin.getY();

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
        public void actionPerformed(ActionEvent e) {
            myInner += myDir;

            if (myInner >= .90F || myInner <= 0.1) {
                myDir = myDir * -1;

                //myColors = inColors;
            }


            repaint();
        }
    } // end of RadialGradiantChanger

} // end of MovingShapePanel
