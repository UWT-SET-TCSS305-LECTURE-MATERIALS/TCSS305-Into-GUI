package edu.uw.tcss.view.layouts;

import java.awt.EventQueue;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Demonstrating FlowLayout.
 * 
 * @author Charles Bryan
 * @version Autumn 2023
 */
public class FlowLayoutDemo extends JPanel {

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = 5105689607757907009L;
    
    /** The default amount of JButtons this window will have, 7. */
    private static final int NUMBER_OF_BUTTONS = 7;
    
    /** The default starting part of text to appear on the JButtons. */
    private static final String BUTTON_TEXT_START = "Button ";

    /** The default text to appear on the JButtons. */
    private static final String[] BUTTON_TEXT_LABELS;

    static {
        BUTTON_TEXT_LABELS = new String[NUMBER_OF_BUTTONS];
        for (int i = 0; i < BUTTON_TEXT_LABELS.length; i++) {
            BUTTON_TEXT_LABELS[i] = BUTTON_TEXT_START + i;
        }
    }

    /** The amount of JButtons this window will have. */
    private final int myNumberOfButtons;
    
    /** The labels on the button in this panel. */
    private final String[] myButtonLabels;
    
    /**
     * Initializes all of the fields.
     * @param theButtonLabels the labels to be used on buttons in this panel.
     */
    public FlowLayoutDemo(final String[] theButtonLabels) {
        super();

        myNumberOfButtons = theButtonLabels.length;
        myButtonLabels = theButtonLabels.clone();

        layoutComponents();
    }

    /**
     * Initializes all of the fields.
     */
    public FlowLayoutDemo() {
        this(BUTTON_TEXT_LABELS);
    }


    /**
     * Lay out the components and makes this frame visible.
     */
    private void layoutComponents() {
        
        final JButton[] buttons = new JButton[myNumberOfButtons];
        
        //Create and add a button for each index in the array.
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(myButtonLabels[i]);
//            buttons[i].setPreferredSize(new Dimension(50, 50));
            add(buttons[i]);
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final FlowLayoutDemo mainPanel =
                new FlowLayoutDemo();

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
    }
    
    /**
     * Creates a JFrame to demonstrate FlowLayout.
     * It is OK, even typical to include a main method 
     * in the same class file as a GUI for testing purposes. 
     * 
     * @param theArgs Command line arguments, ignored.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(FlowLayoutDemo::createAndShowGui);
    }

}
