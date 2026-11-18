package edu.uw.tcss.bouncingballs.view;

import edu.uw.tcss.bouncingballs.model.SimulationState;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A panel with control buttons for the bouncing-balls simulation:
 * Pause, Resume, Reset, and a gravity toggle.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class ControlPanel extends JPanel {

    /** Horizontal gap between buttons, in pixels. */
    private static final int H_GAP = 8;

    /** Vertical gap around the buttons, in pixels. */
    private static final int V_GAP = 4;

    /** Gravity button text while gravity is off. */
    private static final String GRAVITY_OFF = "Gravity: OFF";

    /** Gravity button text while gravity is on. */
    private static final String GRAVITY_ON = "Gravity: ON";

    /** The action to run when Reset is pressed. */
    private final Runnable myResetAction;

    /** The simulation state to control. */
    private final SimulationState myState;

    /** The gravity toggle button, stored for text updates. */
    private final JButton myGravityButton;

    /**
     * Creates a control panel wired to the given simulation state.
     *
     * @param state the simulation state
     * @param resetAction the action to execute when Reset is pressed
     */
    public ControlPanel(final SimulationState state,
                        final Runnable resetAction) {
        super(new FlowLayout(FlowLayout.CENTER, H_GAP, V_GAP));
        myState = state;
        myResetAction = resetAction;
        myGravityButton = new JButton(GRAVITY_OFF);
        myGravityButton.addActionListener(e -> toggleGravity());

        add(createButton("Pause", () -> myState.setPaused(true)));
        add(createButton("Resume", () -> myState.setPaused(false)));
        add(createButton("Reset", this::reset));
        add(myGravityButton);
    }

    /**
     * Creates a button that runs the given action when pressed.
     *
     * @param text the button label
     * @param action the action to run
     * @return the button
     */
    private static JButton createButton(final String text, final Runnable action) {
        final JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    /**
     * Resets the simulation and the gravity button text.
     */
    private void reset() {
        myResetAction.run();
        updateGravityText();
    }

    /**
     * Turns gravity on or off and updates the gravity button text.
     */
    private void toggleGravity() {
        myState.setGravityOn(!myState.isGravityOn());
        updateGravityText();
    }

    /**
     * Updates the gravity button text to reflect the current state.
     */
    private void updateGravityText() {
        myGravityButton.setText(
                myState.isGravityOn() ? GRAVITY_ON : GRAVITY_OFF);
    }
}
