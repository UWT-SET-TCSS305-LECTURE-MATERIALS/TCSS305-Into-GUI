package edu.uw.tcss.view.layouts;

import java.awt.EventQueue;
import java.io.Serial;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Demonstrates all three Layouts working together!
 * Use the Panels built earlier to save having to re-write code. 
 * 
 * @author Charles Bryan
 * @version Autumn 2023
 */
public class TabbedPaneLayout extends JPanel {

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = 3726560300045331301L;

    /** The GridLayout panel. */
    private final GridLayoutDemo myGridDemo;
    
    /** The FLowLayout panel. */
    private final FlowLayoutDemo myFlowDemo;
    
    /** The BorderLayout panel. */
    private final BorderLayoutDemo myBorderDemo;
    

    /**
     * Initializes the Panel.
     */
    public TabbedPaneLayout() {
        super();
        //instantiate a new GridLayoutDemo Panel but don't lay it out.
        //don't forget to call start() later! What happens if you do forget?
        myGridDemo = new GridLayoutDemo();
        
        //instantiate a new FlowLayoutDemoAsPanel Panel but don't lay it out.
        //don't forget to call start() later! What happens if you do forget?
        //what happens if you add more Strings to the initializer list below? 
        myFlowDemo = new FlowLayoutDemo();
        
        myBorderDemo = new BorderLayoutDemo();
        layoutComponents();
    }
    
    
    /**
     * Lay out the components and makes this frame visible.
     */
    private void layoutComponents() {
        final JTabbedPane tabbedPane = new JTabbedPane(); 
        
        tabbedPane.addTab("Flow Demo", myFlowDemo);
        
        tabbedPane.addTab("Border Demo", myBorderDemo);

        tabbedPane.addTab("Grid Demo", myGridDemo);
        
        add(tabbedPane);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final TabbedPaneLayout mainPanel = new TabbedPaneLayout();

        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("Demo Tabbed Panes");

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
        EventQueue.invokeLater(TabbedPaneLayout::createAndShowGui);
    }
}
