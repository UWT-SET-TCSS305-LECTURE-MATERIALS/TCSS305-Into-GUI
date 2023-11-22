/*
 * Java2D Ellipse and Rectangle Demo - TCSS 305
 */

package edu.uw.tcss.view.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serial;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Demonstrates the painting of ellipses (and rectangles).
 * 
 * @author Alan Fowler
 * @author Charles Bryan
 * @version 1.2
 */
public class EllipseAndRectanglePanel extends JPanel {

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
    private static final int STROKE_WIDTH = 9;

    /** The width for the rectangle. */
    private static final int RECTANGLE_WIDTH = 50;

    /** The height for the rectangle. */
    private static final int RECTANGLE_HEIGHT = 50;

    /**
     * Constructs a new ellipse panel.
     */
    public EllipseAndRectanglePanel() {
        super();
        layoutComponents();
    }

    /**
     * Lay out the components and makes this frame visible.
     */
    private void layoutComponents() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    /**
     * Paints some ellipses.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    @SuppressWarnings("CommentedOutCode") //suppressed for the demo.
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);


        final Shape rectangle =
                        new Rectangle2D.Double((getWidth() - RECTANGLE_WIDTH) / 2.0,
                                               (getHeight() - RECTANGLE_HEIGHT) / 2.0,
                                               RECTANGLE_WIDTH * 2, RECTANGLE_HEIGHT * 2);
        g2d.setPaint(Color.RED);
        g2d.setStroke(new BasicStroke(STROKE_WIDTH));
//        g2d.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_BUTT,
//                                      BasicStroke.JOIN_MITER, 1.5f));
        g2d.draw(rectangle);

        final Shape ellipse2 = new Ellipse2D.Double(5, 5, 40, 40);

        g2d.setStroke(new BasicStroke(STROKE_WIDTH));
        g2d.setPaint(Color.BLUE);
        g2d.fill(ellipse2);

        g2d.setPaint(Color.MAGENTA);
        g2d.draw(ellipse2);

        final Shape ellipse = new Ellipse2D.Double(50, 50, 50, 100);
        g2d.setPaint(Color.BLACK);
        g2d.draw(ellipse);

    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final EllipseAndRectanglePanel mainPanel =
                new EllipseAndRectanglePanel();

        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("Draw some shapes!");

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
        javax.swing.SwingUtilities.invokeLater(EllipseAndRectanglePanel::createAndShowGui);
    }
}
