/*
 * A first Swing timer example.
 * 
 * TCSS 305
 */

package edu.uw.tcss.view.graphics.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.Serial;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * NOTE: This example is all in a single class which is NOT ideal. This class
 * could be broken into multiple smaller classes to improve the structure and
 * maintainability of the code.
 */

/**
 * A program to demonstrate the use of a Swing timer.
 * 
 * @author Alan Fowler
 * @author Charles Bryan
 * @version Autumn 2016
 */
public class MovingShapePanel extends JPanel {

    // Constants

    /** The side length (in pixels) of the bounding box of the shape. */
    public static final int BOUNDING_BOX_SIDE = 40;

    /** The preferred window size. */
    public static final Dimension PREFERRED_SIZE = new Dimension(800, 450);

    /** The default delay (in milliseconds) for the move timer. */
    public static final int MOVE_DELAY = 1;

    /** The initial delay (in milliseconds) for the move timer. */
    public static final int INITIAL_DELAY = 0;

    /** The animation step (in pixels). */
    public static final int ANIMATION_STEP = 4;

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = -6257548485239497170L;

    // Instance fields

    /** The moving shape. */
    private final Ellipse2D myMovingShape;

    /** The timer that controls the movement of the shape. */
    private final Timer myMoveTimer;

    /** An integer that determines the horizontal movement at each animation step. */
    private int myHorizontalMove;

    /** An integer that determines the vertical movement at each animation step. */
    private int myVerticalMove;

    // Constructor

    /**
     * Constructs a new MovingShapePanel; the initial location of the shape is
     * the upper-left corner, initially it is moving diagonally downward and to
     * the right, and its color is blue.
     */
    public MovingShapePanel() {
        super(true); // use double buffering

        myHorizontalMove = ANIMATION_STEP;
        myVerticalMove = ANIMATION_STEP;

        myMovingShape = new Ellipse2D.Double(0, 0, BOUNDING_BOX_SIDE, BOUNDING_BOX_SIDE);

        myMoveTimer = new Timer(MOVE_DELAY, new MoveListener());
        
        // wait before the first timer event
        myMoveTimer.setInitialDelay(INITIAL_DELAY); 

        setupAppearance();
        myMoveTimer.start();
    }

    /** Sets up the appearance of the panel and frame. */
    private void setupAppearance() {
        setBackground(Color.WHITE);
        setPreferredSize(PREFERRED_SIZE);
    }

    /** Starts the timer. */
    private void start() {
        myMoveTimer.start();
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setPaint(Color.BLUE);
        g2d.fill(myMovingShape);
    }

    // ************* MAIN ************************************************

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final MovingShapePanel mainPanel =
                new MovingShapePanel();

        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("Animation Demo");

        // Needed to Close the window with the OS controls.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // This class is the Panel that represents the content.
        // Add it to the window
        window.setContentPane(mainPanel);
        //window.setSize(frameSize);

        // Resize the window down to the size of it's contents.
        window.pack();

        // Display the window
        window.setVisible(true);
        mainPanel.start();
    }

    /**
     * Creates a MovingShapePanel in a frame and starts the animation.
     * 
     * @param theArgs Command line parameters - ignored in this program
     */
    public static void main(final String[] theArgs) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(MovingShapePanel::createAndShowGui);

    }

    // *********** Inner Class Listener   *********************************

    /**
     * A class that listens for timer events and moves the shape, checking for
     * the window boundaries and changing direction as appropriate.
     */
    private final class MoveListener implements ActionListener {
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            // we don't really care what the event is; we know this will only be
            // called by the timer

            // if the shape bounds touch the window bounds, we need to change
            // direction
            updateHorizontalMove();
            updateVerticalMove();

            myMovingShape.setFrame(myMovingShape.getX() + myHorizontalMove,
                                   myMovingShape.getY() + myVerticalMove,
                                   BOUNDING_BOX_SIDE,
                                   BOUNDING_BOX_SIDE);

            repaint();
        }

        /** Updates the horizontal move to keep the shape on screen. */
        private void updateHorizontalMove() {
            if (myMovingShape.getFrame().getMinX() < 0) {
                // change direction
                myHorizontalMove = ANIMATION_STEP;
            } else if (myMovingShape.getFrame().getMaxX()
                      > MovingShapePanel.this.getWidth()) {
                // change direction
                myHorizontalMove = -ANIMATION_STEP;
            }
        }

        /** Updates the vertical move to keep the shape on screen. */
        private void updateVerticalMove() {
            if (myMovingShape.getFrame().getMinY() < 0) {
                // change direction
                myVerticalMove = ANIMATION_STEP;
            } else if (myMovingShape.getFrame().getMaxY()
                     > MovingShapePanel.this.getHeight()) {
                // change direction
                myVerticalMove = -ANIMATION_STEP;
            }
        }
    } // end of MoveListener

} // end of MovingShapePanel
