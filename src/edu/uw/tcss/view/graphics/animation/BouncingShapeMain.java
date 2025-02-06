/*
 * BouncingShapeMain.java
 * 
 * TCSS 305
 */

package edu.uw.tcss.view.graphics.animation;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * Starts the BouncingShape program which is a demonstration of Swing Timers and
 * other features including KeyListener.
 * 
 * @author Alan Fowler acfowler@uw.edu
 * @version 1.2
 */
public final class BouncingShapeMain {

    /**
     * A private constructor to inhibit instantiation.
     */
    private BouncingShapeMain() {
    }

    /**
     * Creates a BouncingShapePanel in a frame and starts the animation.
     * 
     * @param theArgs Command line parameters - ignored in this program
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame();
                final BouncingShapePanel panel = new BouncingShapePanel(frame);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(panel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                panel.start(); // starts the timer(s)
            }
        });
    }

}
