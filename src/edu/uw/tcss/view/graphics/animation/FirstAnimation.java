/*
 * Java2D Ellipse and Rectangle Demo - TCSS 305
 */

package edu.uw.tcss.view.graphics.animation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Demonstrates simple animation.
 * 
 * @author Charles Bryan
 * @version 1.2
 */
public class FirstAnimation extends JPanel {

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = 8452917670991316606L; 
    
    /** The width of the panel. */
    private static final int WIDTH = 400;

    /** The height of the panel. */
    private static final int HEIGHT = 400;

    /** The stroke width in pixels. */
    private static final int STROKE_WIDTH = 10;

    /** The width for the Shape. */
    private static final int SHAPE_WIDTH = 50;

    /** The height for the Shape. */
    private static final int SHAPE_HEIGHT = 50;

    /** The distance to move the Shape. */
    private static final int DISTANCE_TO_MOVE = 10;

    private final Ellipse2D myShape;

    private final JButton myButt;

    /**
     * Constructs a new ellipse panel.
     */
    public FirstAnimation() {
        super();

        myShape = new Ellipse2D.Double(0, 0, SHAPE_WIDTH, SHAPE_HEIGHT);
        myButt = new JButton("Move");

        setupComponents();
        layoutComponents();
        addListeners();
    }

    private void setupComponents() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
    }

    private void layoutComponents() {
        add(myButt, BorderLayout.SOUTH);
    }

    private void addListeners() {
        myButt.addActionListener(theEvent -> {
            final Rectangle2D bounds = myShape.getBounds2D();
            myShape.setFrame(bounds.getX() + DISTANCE_TO_MOVE,
                    bounds.getY() + DISTANCE_TO_MOVE,
                    bounds.getWidth(),
                    bounds.getHeight());

            repaint();
        });
    }



    /**
     * Paints some ellipses.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setPaint(Color.BLUE);
        g2d.fill(myShape);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final FirstAnimation mainPanel =
                new FirstAnimation();

        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("Move the Shape!");

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
    }

    /**
     * Creates and displays an EllipsePanel.
     * 
     * @param theArgs Command line arguments (ignored).
     */
    public static void main(final String... theArgs) {
        javax.swing.SwingUtilities.invokeLater(FirstAnimation::createAndShowGui);
    }
}
