package edu.uw.tcss.view.layouts;


import java.awt.BorderLayout;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Demonstrates Border layout.
 * 
 * @author Charles Bryan
 * @version Autumn 2023
 */
public class BorderLayoutDemo extends JPanel {


    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = 8557044779441785202L;

    /**
     * Initializes all of the fields.
     */
    public BorderLayoutDemo() {
        super();
        layoutComponents();
    }

    /**
     * Lay out the components and makes this frame visible.
     */
    @SuppressWarnings("CommentedOutCode") // Suppressed for the example
    private void layoutComponents() {
        
        setLayout(new BorderLayout());
        
        //What happens if you don't add one or two of the buttons
        //to this panel using BorderLayout? Try a couple to get
        //a good idea of the rules behind BorderLayout.
        //Try to resize the window with your mouse when running the
        //program. What happens to the components? 
        
        final JPanel temp = new JPanel();
        final JButton north = new JButton("North");
        temp.add(north);
//
        add(temp, BorderLayout.NORTH);
        
        
        final JButton south = new JButton("South");
        add(south, BorderLayout.SOUTH);
        
        final JButton west = new JButton("West");
        add(west, BorderLayout.WEST);        
        
        final JButton east = new JButton("East");
        add(east, BorderLayout.EAST);

        //What happens if you add the JButton center to 
        //the this Panel object instead of nesting it inside of
        //centerPanel. Why do they look different?
        final JButton center = new JButton("Center");        
//        final JPanel centerPanel = new JPanel();
//        centerPanel.add(center);

        add(center, BorderLayout.CENTER);  
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final BorderLayoutDemo mainPanel = new BorderLayoutDemo();

        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("Demo BorderLayout");

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
     * Creates a JFrame to demonstrate BorderLayout.
     * It is OK, even typical to include a main method 
     * in the same class file as a GUI for testing purposes. 
     * 
     * @param theArgs Command line arguments, ignored.
     */
    public static void main(final String[] theArgs) {
        javax.swing.SwingUtilities.invokeLater(BorderLayoutDemo::createAndShowGui);
    }

}
