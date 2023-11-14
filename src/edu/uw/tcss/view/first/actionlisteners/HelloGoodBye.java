package edu.uw.tcss.view.first.actionlisteners;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Demonstrating ActionListeners.
 * 
 * @author Charles Bryan
 * @version Autumn 2023
 */
public class HelloGoodBye extends JPanel {

    /*
     * JPanel implements Serializable.
     * We may cover Serializable later this quarter when time allows.
     * For now just have Eclipse generate a serial version UID as shown below.
     * If you would like to learn about Serialization check the following two links:
     *
     * http://www.tutorialspoint.com/java/java_serialization.htm
     *
     * http://www.javapractices.com/topic/TopicAction.do?Id=45
     */

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = -1155574959121887493L;
        
    /** A button to say hello. */
    private JButton myHelloButton;
    
    /** A button to say goodbye. */
    private JButton myGoodbyeButton;
    
    /** A button to say Wait, come back. */
    private JButton myWaitButton;
    
    /** A button to say Lambda. */
    private JButton myLambdaButton;

    /** A button to say Method References. */
    private JButton myMethodReferenceButton;

    /** A label to display the message. */
    private JLabel myMessageLabel;

    /**
     * Initializes all of the fields.
     */
    public HelloGoodBye() {
        super();
        buildComponents();
        layoutComponents();
        addListeners();
    }
    
    /**
     * Instantiate the graphical components (frame, image label, buttons).
     */
    private void buildComponents() {
        myMessageLabel = new JLabel("Message");
        myMessageLabel.setOpaque(true);
        
        myHelloButton = new JButton("Say Hello");
        myGoodbyeButton = new JButton("Say Goodbye");
        myWaitButton = new JButton("Wait...!");
        myLambdaButton = new JButton("Lambda Style");
        myMethodReferenceButton = new JButton("Method Reference");
    }
    
    /**
     * Add actionListeners to the buttons. 
     */
    @SuppressWarnings("Convert2Lambda") //suppresed this warning as this is JUST an example.
    private void addListeners() {
        
        //Add an action listener from an inner class. Page 329
        myHelloButton.addActionListener(new MyHelloListener());
        
        /*
         * This is an example of a Local Inner class. This class
         * is created HERE in THIS method. Think about what/where
         * has access to it. Where are the viable locations that one
         * can instantiate a MyGoodByeListener?
         */
        @SuppressWarnings("LimitedScopeInnerClass") //suppresed this warning as this is JUST an example. Don't use.
        class MyGoodByeListener implements ActionListener {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myMessageLabel.setText("Goodbye");
            }
        }
        myGoodbyeButton.addActionListener(new MyGoodByeListener());
        
        //Add an action listener using an anonymous inner class. Page 342
        myWaitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myMessageLabel.setText("Wait! Don't go yet, there's more...");
            }
        });
        
        //Add an action listener as a Lambda expression. Page 314
        myLambdaButton.addActionListener(theEvent -> { 
                myMessageLabel.setText("Java 8 is fantastic!");
                myMessageLabel.setForeground(Color.BLUE);
                myMessageLabel.setBackground(Color.ORANGE);
            }
        );
        
        //Add an action listener using a method reference. Page 319
        myMethodReferenceButton.addActionListener(this::methodReference);
        
    }
    
    /**
     * A method reference. The method header must match the functional interface's
     * single abstract method signature, not including name. Note the PMD warning here.
     * @param theEvent the Action event is not used here
     */
    private void methodReference(final ActionEvent theEvent) { //NOPMD  (only acceptable here)
        myMessageLabel.setText("Now that's some strange looking syntax.");
    }
    
    /**
     * Lay out the components and makes this frame visible.
     */
    private void layoutComponents() {

        setLayout(new BorderLayout());
        
        final JPanel labelPanel = new JPanel();
        labelPanel.add(myMessageLabel);
        add(labelPanel, BorderLayout.SOUTH);
        
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(myHelloButton);
        buttonPanel.add(myGoodbyeButton);
        buttonPanel.add(myWaitButton);
        buttonPanel.add(myLambdaButton);
        buttonPanel.add(myMethodReferenceButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    
    /**
     * Creates a JFrame to demonstrate this panel.
     * It is OK, even typical to include a main method 
     * in the same class file as a GUI for testing purposes. 
     * 
     * @param theArgs Command line arguments, ignored.
     */
    public static void main(final String[] theArgs) {
//        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGui());
        // -OR-
        javax.swing.SwingUtilities.invokeLater(HelloGoodBye::createAndShowGui);
    }   
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGui() {
        final HelloGoodBye mainPanel = 
                        new HelloGoodBye();
        
        // A size for the JFrame.
        //final Dimension frameSize = new Dimension(400, 400);

        // JFrame is the "window"
        final JFrame window = new JFrame("A Message");

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
     * An Inner class that is an ActionListener for Button clicks. 
     * 
     * @author Charles Bryan
     * @version Autumn 2015
     */
    private final class MyHelloListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myMessageLabel.setText("Hello");
        }  
    }
}