package edu.uw.tcss.view.layouts;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Demonstrates Grid layout.
 * 
 * @author Charles Bryan
 * @version Autumn 2023
 */
public class GridLayoutDemo extends JPanel {

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = -4024144513823395712L;

    /** The number of rows. */
    private static final int ROW = 4;
    
    /** The number of columns. */
    private static final int COL = 3;   
    
    /** The recommended size of the buttons. */
    private static final Dimension KEY_SIZE = new Dimension(50, 50);
    
    /** What should be on the buttons. */
    private static final String[] KEYS = {
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        "*", "0", "#" };

    /**
     * Initializes all of the fields.
     */
    public GridLayoutDemo() {
        super();
        layoutComponents();
    }

    /**
     * Lay out the components and makes this frame visible.
     */
    private void layoutComponents() {
        setLayout(new GridLayout(ROW, COL));
        
        final JButton[] keyButtons = new JButton[KEYS.length];
        
        for (int i = 0; i < keyButtons.length; i++) {
            keyButtons[i] = new JButton(KEYS[i]);
            keyButtons[i].setPreferredSize(KEY_SIZE);
            add(keyButtons[i]);
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final GridLayoutDemo mainPanel = new GridLayoutDemo();

        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("Demo GridLayout");

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
     * Creates a JFrame to demonstrate GridLayout.
     * It is OK, even typical to include a main method 
     * in the same class file as a GUI for testing purposes. 
     * 
     * @param theArgs Command line arguments, ignored.
     */
    public static void main(final String[] theArgs) {
        javax.swing.SwingUtilities.invokeLater(GridLayoutDemo::createAndShowGui);
    }

}
