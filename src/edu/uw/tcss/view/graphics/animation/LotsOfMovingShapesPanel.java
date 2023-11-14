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
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

/*
 * NOTE: This example is all in a single class which is NOT ideal. This class
 * could be broken into multiple smaller classes to improve the structure and
 * maintainability of the code.
 */

/**
 * A program to demonstrate the use of a Swing timer.
 * 
 * @author Alan Fowler
 * @author Charles Bryan added the Lots part
 * @version 1.3
 */
public final class LotsOfMovingShapesPanel extends JPanel {

    // Constants
    
    /** A random number generator. */
    public static final Random RANDOM_GEN = new Random();

    /** The side length (in pixels) of the bounding box of the shape. */
    public static final int BOUNDING_BOX_SIDE = 40;

    /** The preferred window size. */
    public static final Dimension PREFERRED_SIZE = new Dimension(800, 450);

    /** The default delay (in milliseconds) for the move timer. */
    public static final int MOVE_DELAY = 10;

    /** The initial delay (in milliseconds) for the move timer. */
    public static final int INITIAL_DELAY = 0;

    /** The animation step (in pixels). */
    public static final int ANIMATION_STEP = 1;
    
    /** The name says it all, Go Huskeys! */
    private static final String UW_PURPLE = "#4B2E83";
    
    /** The name says it all, Go Huskeys! */
    private static final String UW_GOLD = "#B7A57A";

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = -6257548485239497170L;

    // Instance fields
    
    /** The moving shapes. */
    private final List<Ball> myMovingShapes;

    /** The timer that controls the movement of the shape. */
    private final Timer myMoveTimer;

    // Constructor

    /**
     * Constructs a new MovingShapePanel; the initial location of the shape is
     * the upper-left corner, initially it is moving diagonally downward and to
     * the right, and its color is blue.
     */
    public LotsOfMovingShapesPanel() {
        super(true); // use double buffering

        myMovingShapes = new ArrayList<>();
        
        myMovingShapes.add(new Ball(
                new Ellipse2D.Double(0, 0, BOUNDING_BOX_SIDE, BOUNDING_BOX_SIDE)
        ));

        myMoveTimer = new Timer(MOVE_DELAY, new MoveListener());
        
        addMouseListener(new MouseClickListener());
        
        // wait before the first timer event
        myMoveTimer.setInitialDelay(INITIAL_DELAY); 

        setupAppearance();
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
        
        for (final Ball b : myMovingShapes) {
            g2d.setPaint(b.myColor);
            g2d.fill(b.getMyMovingShape());
        }
    }

    // ************* MAIN ************************************************

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final LotsOfMovingShapesPanel mainPanel =
                new LotsOfMovingShapesPanel();

        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("FlowLayout Demo");

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
        javax.swing.SwingUtilities.invokeLater(LotsOfMovingShapesPanel::createAndShowGui);
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


            for (final Ball ball : myMovingShapes) {
                // if the shape bounds touch the window bounds, we need to change
                // direction
                updateHorizontalMove(ball);
                updateVerticalMove(ball);
                
                ball.getMyMovingShape().setFrame(
                              ball.getMyMovingShape().getX() + ball.myHorizontalMove,
                              ball.getMyMovingShape().getY() + ball.myVerticalMove,
                                   BOUNDING_BOX_SIDE,
                                   BOUNDING_BOX_SIDE);
            }
            
            for (int i = 0; i < myMovingShapes.size(); i++) {
                final Ball ball = myMovingShapes.get(i);
                for (int j = i + 1; j < myMovingShapes.size();  j++) {
                    final Ball otherBall = myMovingShapes.get(j);
                    if (ball.collide(otherBall)) {
                        if (ball.getMyHorizontalMove() == otherBall.getMyHorizontalMove()) {
                            ball.setMyVerticalMove(-ball.getMyVerticalMove());
                            otherBall.setMyVerticalMove(-otherBall.getMyVerticalMove());
                        } else if (ball.getMyVerticalMove() == otherBall.getMyVerticalMove()) {
                            ball.setMyHorizontalMove(-ball.getMyHorizontalMove()); 
                            otherBall.setMyHorizontalMove(-otherBall.getMyHorizontalMove()); 
                        } else {
                            ball.setMyHorizontalMove(-ball.getMyHorizontalMove()); 
                            ball.setMyVerticalMove(-ball.getMyVerticalMove());
                            otherBall.setMyHorizontalMove(-otherBall.getMyHorizontalMove()); 
                            otherBall.setMyVerticalMove(-otherBall.getMyVerticalMove());
                        }
                    }
                    
                }
            }

            repaint();
        }

        /** 
         * Updates the horizontal move to keep the shape on screen. 
         * @param theBall the object to update 
         */
        private void updateHorizontalMove(final Ball theBall) {
            if (theBall.getMyMovingShape().getFrame().getMinX() < 0) {
                // change direction
                theBall.myHorizontalMove = ANIMATION_STEP;
            } else if (theBall.getMyMovingShape().getFrame().getMaxX()
                      > LotsOfMovingShapesPanel.this.getWidth()) {
                // change direction
                theBall.myHorizontalMove = -ANIMATION_STEP;
            }
        }

        /** 
         * Updates the vertical move to keep the shape on screen. 
         * @param theBall the object to update 
         */
        private void updateVerticalMove(final Ball theBall) {
            if (theBall.getMyMovingShape().getFrame().getMinY() < 0) {
                // change direction
                theBall.myVerticalMove = ANIMATION_STEP;
            } else if (theBall.getMyMovingShape().getFrame().getMaxY()
                     > LotsOfMovingShapesPanel.this.getHeight()) {
                // change direction
                theBall.myVerticalMove = -ANIMATION_STEP;
            }
        }
    } // end of MoveListener
    
    /**
     * A class that listens for mouse click events and adds new
     * shapes to the panel when they happen. 
     */
    class MouseClickListener extends MouseInputAdapter {
        
        @Override
        public void mouseClicked(final MouseEvent theEvent) {            
            final int xDir = getRandomDirection();
            final int yDir = getRandomDirection();
            
            final int centerX = theEvent.getX() - BOUNDING_BOX_SIDE / 2;
            final int centerY = theEvent.getY() - BOUNDING_BOX_SIDE / 2;
            final String color;
            
            if (theEvent.getButton() == MouseEvent.BUTTON1) {
                color = UW_PURPLE;
            } else {
                color = UW_GOLD;                
            }
            final Ball b = new Ball(
                    new Ellipse2D.Double(centerX, centerY,
                            BOUNDING_BOX_SIDE, BOUNDING_BOX_SIDE),
                    xDir, yDir,
                    Color.decode(color));
            
            //look to see if this new ball is on top of an existing ball
            boolean collide = false;
            for (final Ball ball : myMovingShapes) {
                if (b.collide(ball)) {
                    collide = true;
                    break;
                }
            }
            if (!collide) {
                myMovingShapes.add(b);
            }
        }
        
        /**
         * Provides a 1 or -1 randomly. 
         * 
         * @return a 1 or -1, randomly. 
         */
        private int getRandomDirection() {
            int result = 1;
            if (RANDOM_GEN.nextBoolean()) {
                result =  -1;
            } 
            return result;
        }
    }
    
    /**
     * A class that holds all needed fields for one ball to bounce. 
     *
     */
    static class Ball {
        
        /** The moving shape. */
        private final Ellipse2D myMovingShape;

        /** An integer that determines the horizontal movement at each animation step. */
        private int myHorizontalMove;

        /** An integer that determines the vertical movement at each animation step. */
        private int myVerticalMove;
        
        /** A Color for the ball. */
        private final Color myColor;
        
        /**
         * Initialize the fields.
         *
         * @param theMovingShape the shape to move
         */
        Ball(final Ellipse2D theMovingShape) {
            super();
            myMovingShape = theMovingShape;
            myHorizontalMove = LotsOfMovingShapesPanel.ANIMATION_STEP;
            myVerticalMove = LotsOfMovingShapesPanel.ANIMATION_STEP;
            myColor = Color.decode(UW_PURPLE);
        }
        
        /**
         * Initialize the fields. 
         * 
         * @param theMovingShape the shape to move
         * @param theHorizontalMove the initial horizontal direction
         * @param theVerticalMove the initial vertical direction
         * @param theColor the color of this ball 
         */
        Ball(final Ellipse2D theMovingShape, final int theHorizontalMove,
             final int theVerticalMove, final Color theColor) {
            super();
            myMovingShape = theMovingShape;
            myHorizontalMove = theHorizontalMove;
            myVerticalMove = theVerticalMove;
            myColor = theColor;
        }        

        /**
         * Returns the shape object that represents this ball. 
         * @return the shape
         */
        private Ellipse2D getMyMovingShape() {
            return myMovingShape;
        }
        
        /**
         * Gets the horizontal vector.
         * @return the horizontal vector
         */
        public int getMyHorizontalMove() {
            return myHorizontalMove;
        }

        /**
         * Sets the horizontal vector. 
         * @param theHorizontalMove the horizontal vector must be a 1 or -1
         * @throws IllegalArgumentException when theHorizontalMove is not 1 or -1
         */
        public void setMyHorizontalMove(final int theHorizontalMove) {
            if (theHorizontalMove != 1 && theHorizontalMove != -1) {
                throw new IllegalArgumentException(
                        "Horizontal Vector must be 1 or -1. Was: " + theHorizontalMove);
            }
            myHorizontalMove = theHorizontalMove;
        }

        /**
         * Gets the vertical vector.
         * @return the vertical vector
         */
        public int getMyVerticalMove() {
            return myVerticalMove;
        }

        /**
         * Sets the vertical vector. 
         * @param theVerticalMove the vertical vector must be a 1 or -1
         * @throws IllegalArgumentException when theVerticalMove is not 1 or -1
         */
        public void setMyVerticalMove(final int theVerticalMove) {
            if (theVerticalMove != 1 && theVerticalMove != -1) {
                throw new IllegalArgumentException(
                        "Vertical Vector must be 1 or -1. Was: " + theVerticalMove);
            }
            myVerticalMove = theVerticalMove;
        }
        
        /**
         * Returns true when the distance between this ball and theOtherBall is 
         * less than or equal to the diameter of the ball. This works only because
         * the balls have same size. 
         * 
         * @param theOtherBall the Ball object to check for collision with
         * @return true if the balls collide. 
         */
        public boolean collide(final Ball theOtherBall) {
            final double distance = Math.hypot(
                myMovingShape.getCenterX() - theOtherBall.myMovingShape.getCenterX(), 
                myMovingShape.getCenterY() - theOtherBall.myMovingShape.getCenterY());       
            return distance <= myMovingShape.getWidth();
            
        }
    }

} // end of MovingShapePanel
