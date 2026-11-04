/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package edu.uw.tcss.view.menus;

import edu.uw.tcss.logging.LoggerManager;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputAdapter;



/**
 *  A class that demonstrates MouseMotion Event.
 * 
 * @author Oracle and/or its affiliates
 * @author Charles Bryan
 * @version Autumn 2026
 */
public class MenuExample extends JPanel {

    /**
     * A generated serial version UID for object Serialization.
     * <a href="http://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html">...</a>
     */
    @Serial
    private static final long serialVersionUID = 8452917670991316606L; 
    
    /** Saves the systems newline character. */
    private static final String NEWLINE = System.lineSeparator();
    
    /** The default border size. */
    private static final int BORDER_SIZE = 20;
    
    /** The number of rows in the text area. */
    private static final int TEXT_AREA_ROWS = 15;
    
    /** The number of columns in the text area. */
    private static final int TEXT_AREA_COLS = 40;
    
    /** The area for the mouse demonstration. */
    private final BlankArea myBlankArea;
    
    /** Where the output goes. */
    private final JTextArea myTextArea;
    
    /** CheckBox determining if logging is on or off. */
    private final JCheckBoxMenuItem myLogging;
    
    /**
     * Constructs a MouseMotionEventDemo.
     */
    public MenuExample() {
        super(new GridLayout(0, 1));
        
        //Instantiate these Components in the constructor but 
        //we will set the up and lay them out later. 
        myBlankArea = new BlankArea(Color.YELLOW);
        myTextArea = new JTextArea(TEXT_AREA_ROWS , TEXT_AREA_COLS);
        myLogging = new JCheckBoxMenuItem();
        
        setUpComponents();
    }
    
    /**
     * Lay out the components.
     */
    private void setUpComponents() {
        
        add(myBlankArea);

        myTextArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(myTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane);
        
        final MouseInputAdapter mia = new MyMouseInputAdapter();
        //Register for mouse events on blankArea and panel.
        myBlankArea.addMouseListener(mia);
        myBlankArea.addMouseMotionListener(mia);
        
//        addMouseListener(mia);
//        addMouseMotionListener(mia);
//
        setBorder(BorderFactory.createEmptyBorder(
                BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
    }
    
    /**
     * Build the menu bar for this GUI. This method will need
     * to be called where access to a JFrame occurs. You attach
     * a MenuBar to a Frame, not a Panel. 
     * 
     * @param frame the containing JFrame of this menu bar
     * @return the menu bar for this GUI
     */
    private JMenuBar createMenu(final JFrame frame) {
        final JMenuBar menuBar = new JMenuBar();
         
        menuBar.add(buildFileMenu(frame));
        menuBar.add(buildColorButtonMenu());
        menuBar.add(buildLoggingMenu());
        
        return menuBar;
    }
    
    /**
     * Builds a menu with some options. 
     * 
     * @param frame the containing JFrame of this menu bar
     * @return a "file" menu with some menu items
     */
    private JMenu buildFileMenu(final JFrame frame) {
        final JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        
        menu.add(buildSubMenu());
        menu.addSeparator();
        
        /*
         * Scale the image from 32x32 pixels down to 12x12 pixels.
         * https://docs.oracle.com/javase/8/docs/api/java/awt/Image.html
         */
        ImageIcon icon = new ImageIcon("./images/ic_walk.png");
        final Image smallImage = icon.getImage().getScaledInstance(
                                              12, -1, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(smallImage);
        
        final JMenuItem exitItem = new JMenuItem("Exit", icon);
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(event ->
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        
        menu.add(exitItem);
        return menu;
    }
    
    /**
     * Builds a menu to demonstrate sub menus. 
     * @return a menu with several simple menu items. 
     */
    private JMenu buildSubMenu() {
        final JMenu subMenu = new JMenu("Subs!");
        
        subMenu.add(buildSimpleMenuItem("Hello"));
        subMenu.add(buildSimpleMenuItem("World"));
        subMenu.add(buildSimpleMenuItem("Menus!"));
        
        return subMenu;
    }
    
    /**
     * Builds a simple menu item. 
     * 
     * @param text the text to appear on the menu item
     * @return a simple menu item
     */
    private JMenuItem buildSimpleMenuItem(final String text) {
        final JMenuItem item = new JMenuItem(text);
        item.addActionListener(event ->
                JOptionPane.showMessageDialog(MenuExample.this, text));
        return item;
    }
    
    /**
     * Creates a menu that demonstrates JRadioButtonMenuItems.
     * 
     * @return the menu
     */
    private JMenu buildColorButtonMenu() {
        final JMenu colorMenu = new JMenu("Color");
        colorMenu.setMnemonic(KeyEvent.VK_C);
        
        final JMenuItem chooseColorItem = new JMenuItem("Choose Color");
        final ButtonGroup group = new ButtonGroup();
        chooseColorItem.addActionListener(event -> chooseColor(group));
        
        colorMenu.add(chooseColorItem);
        colorMenu.addSeparator();

        colorMenu.add(createColorItems("Red", Color.RED, group));
        colorMenu.add(createColorItems("Blue", Color.BLUE, group));
        colorMenu.add(createColorItems("Green", Color.GREEN, group));
        
        return colorMenu;
    }
    
    /**
     * Creates a JRadioButtonMenuItem with a color action. 
     * 
     * @param name the text to go on this item
     * @param color the color to set the action
     * @param group the ButtonGroup to add this Radio Button to
     * @return a a JRadioButtonMenuItem with a color action
     */
    private JRadioButtonMenuItem createColorItems(final String name, 
            final Color color, final ButtonGroup group) {
        
        final JRadioButtonMenuItem colorItem = new JRadioButtonMenuItem(name);
        group.add(colorItem);
        colorItem.addActionListener(event -> myBlankArea.setBackground(color));
        return colorItem;
    }
    
    /**
     * Shows a color chooser and sets the background to the chosen color.
     *
     * @param group the radio button group to clear when a color is chosen
     */
    private void chooseColor(final ButtonGroup group) {
        final Color result = JColorChooser.showDialog(null, "A Color Chooser", null);
        if (result != null) {
            myBlankArea.setBackground(result);
            group.clearSelection();
        }
    }

    /**
     * Builds a simple menu item. 
     * 
     * @return a simple menu item
     */
    private JMenu buildLoggingMenu() {
        final JMenu loggingMenu = new JMenu("Logging");
        final String onMsg = "Logging ON/off";
        final String offMsg = "Logging on/OFF";
        myLogging.setText(onMsg);
        
        //What does this do...?
        myLogging.setToolTipText("When checked, the TextArea displays all mouse events.");
        
        myLogging.setSelected(true);
        //Why don't I do anything in here with the MouseEvents/Text area?
        //go check out eventOutput()
        myLogging.addActionListener(
            event -> myLogging.setText(myLogging.isSelected() ? onMsg : offMsg));
        
        loggingMenu.add(myLogging);
        return loggingMenu;
    }    
    
    /**
     * Helper method for when one of the demo MouseEvents happens. 
     * 
     * @param eventDescription the description of what happened
     * @param event the event object that called this method
     */
    private void eventOutput(final String eventDescription, final MouseEvent event) {
        //Check to see if the JCheckBoxMenuItem in the menu bar is selected. If it is,
        //display (log) the Events occurring this demo.
        if (myLogging.isSelected()) {
            myTextArea.append(eventDescription
                    + " (" + event.getX() + "," + event.getY()
                    + ") detected on "
                    + event.getComponent().getClass().getName()
                    + NEWLINE);
            myTextArea.setCaretPosition(myTextArea.getDocument().getLength());
        }
    }
    
    /**
     * Creates a JFrame to demonstrate this panel.
     * It is OK, even typical to include a main method 
     * in the same class file as a GUI for testing purposes. 
     * 
     * @param args Command line arguments, ignored.
     */
    @SuppressWarnings("CommentedOutCode") //suppressed to demo different LaFs
    public static void main(final String[] args) {
        /* 
         * Use an appropriate Look and Feel 
         * https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         * 
         */
        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final UnsupportedLookAndFeelException
                       | ClassNotFoundException
                       | InstantiationException
                       | IllegalAccessException ex) {
            LoggerManager.error(ex.getMessage());
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(MenuExample::createAndShowGUI);
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("MouseMotionEventDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        final MenuExample newContentPane = new MenuExample();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        frame.setJMenuBar(newContentPane.createMenu(frame));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                LoggerManager.info("Weeeeeeeeee");
            }
        });
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * A MouseInputAdapter implementation specific to this GUI.
     * 
     * @author Charles Bryan
     * @version Autumn 2026
     *
     */
    class MyMouseInputAdapter extends MouseInputAdapter {
        
//        //these methods are found in MouseListener
        @Override
        public void mousePressed(final MouseEvent event) {
            eventOutput(event.getButton() + " Mouse pressed (# of clicks: "
                    + event.getClickCount() + ')', event);
        }
        
        @Override
        public void mouseReleased(final MouseEvent event) {
            eventOutput("Mouse released (# of clicks: "
                    + event.getClickCount() + ')', event);
        }
        
        @Override
        public void mouseEntered(final MouseEvent event) {
            eventOutput("Mouse entered", event);
        }
        
        @Override
        public void mouseExited(final MouseEvent event) {
            eventOutput("Mouse exited", event);
        }
        
        @Override
        public void mouseClicked(final MouseEvent event) {
            eventOutput("Mouse clicked (# of clicks: "
                    + event.getClickCount() + ')', event);
        }
        
        //These methods are found in MouseMotionListener
        @Override
        public void mouseMoved(final MouseEvent event) {
            eventOutput("Mouse moved", event);
        }
        
        @Override
        public void mouseDragged(final MouseEvent event) {
            eventOutput("Mouse dragged", event);
        }
    }
}