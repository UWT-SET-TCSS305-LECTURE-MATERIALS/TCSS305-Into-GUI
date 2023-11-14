package edu.uw.tcss.view.layouts;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.Serial;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Demonstrates all three Layouts working together!
 * Use the Panels built earlier to save having to re-write code. 
 * 
 * @author Charles Bryan
 * @version Autumn 2023
 */
public class CompositeLayout extends JPanel {

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = 3726560300045331301L;

    /** The keypad panel. */
    private GridLayoutDemo myKeypad;
    
    /** The button panel. */
    private FlowLayoutDemo myButtons;
    
    /** Output field. */
    private JTextField myTextField;

    /**
     * Initializes the Panel.
     */
    public CompositeLayout() {
        super();
        buildComponents();
        layoutComponents();
    }

    /**
     * Instantiate the graphical components (frame, image label, buttons).
     */
    private void buildComponents() {
        myTextField = new JTextField();

        //instantiate a new GridLayoutDemo Panel but don't lay it out.
        //don't forget to call start() later! What happens if you do forget?
        myKeypad = new GridLayoutDemo();

        //instantiate a new FlowLayoutDemoAsPanel Panel but don't lay it out.
        //don't forget to call start() later! What happens if you do forget?
        //what happens if you add more Strings to the initializer list below?
        myButtons = new FlowLayoutDemo(new String[] {"Cancel", "Send"});
    }
    
    /**
     * Layout the components and makes this frame visible.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        final JPanel temp = new JPanel();
               
        add(myTextField, BorderLayout.NORTH); 

        temp.add(myKeypad);
        add(temp, BorderLayout.CENTER);
 
        add(myButtons, BorderLayout.SOUTH);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final CompositeLayout mainPanel = new CompositeLayout();

        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("Demo Composite Layouts");

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
     * Creates a JFrame to demonstrate Composite Layouts.
     * It is OK, even typical to include a main method 
     * in the same class file as a GUI for testing purposes. 
     * 
     * @param theArgs Command line arguments, ignored.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(CompositeLayout::createAndShowGui);
    }
}
