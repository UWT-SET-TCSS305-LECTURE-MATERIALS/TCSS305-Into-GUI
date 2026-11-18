package edu.uw.tcss.bouncingballs.view;

import edu.uw.tcss.bouncingballs.model.Ball;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A transparent overlay panel that appears on top of the simulation area
 * when the simulation is paused and a ball is clicked. Provides sliders
 * for speed and direction and preset color buttons to edit the selected ball.
 *
 * <p>This panel is intended to be used as the glass pane of the main frame.
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class BallEditorOverlay extends JPanel {

    /** UW Purple color preset. */
    private static final Color UW_PURPLE = new Color(75, 46, 131);

    /** UW Gold color preset. */
    private static final Color UW_GOLD = new Color(183, 165, 122);

    /** Maximum speed value for the speed slider. */
    private static final int MAX_SPEED = 20;

    /** Maximum angle value for the direction slider. */
    private static final int MAX_ANGLE = 360;

    /** Gap between the parts of the card, in pixels. */
    private static final int GAP = 6;

    /** Gap between buttons and sliders, in pixels. */
    private static final int SMALL_GAP = 4;

    /** Top and bottom padding inside the card, in pixels. */
    private static final int PAD_V = 12;

    /** Left and right padding inside the card, in pixels. */
    private static final int PAD_H = 16;

    /** The card's background: a dark gray that is slightly see-through. */
    private static final Color CARD_BACKGROUND = new Color(50, 50, 50, 230);

    /** The dark, see-through scrim drawn over the simulation behind the card. */
    private static final Color SCRIM = new Color(0, 0, 0, 120);

    /** Preferred size of the editor card. */
    private static final Dimension CARD_SIZE = new Dimension(320, 260);

    /** The ball currently being edited, or null if none. */
    private Ball myBall;

    /** The speed slider. */
    private final JSlider mySpeedSlider;

    /** The direction slider. */
    private final JSlider myDirectionSlider;

    /** The editor card panel, used for hit-testing click-outside-to-dismiss. */
    private final JPanel myCard;

    /**
     * Creates a new ball editor overlay. Initially invisible.
     */
    public BallEditorOverlay() {
        super(new GridLayout(1, 1)); // center a single child
        setOpaque(false);
        setVisible(false);
        mySpeedSlider = createSlider(MAX_SPEED);
        myDirectionSlider = createSlider(MAX_ANGLE);
        myCard = createCard();

        // Blocks clicks from reaching the simulation underneath, and a click
        // outside the card dismisses the editor.
        addMouseListener(new DismissListener());

        add(createWrapper(myCard));
    }

    /**
     * Wraps the card in a see-through panel, whose FlowLayout centers it.
     *
     * @param card the card
     * @return the wrapper
     */
    private static JPanel createWrapper(final JPanel card) {
        final JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.add(card);
        return wrapper;
    }

    /**
     * Creates a slider from 0 to the given maximum that edits the ball when moved.
     *
     * @param max the slider's maximum value
     * @return the slider
     */
    private JSlider createSlider(final int max) {
        final JSlider slider = new JSlider(0, max, 0);
        slider.setOpaque(false);
        slider.addChangeListener(e -> applySpeedDirection());
        return slider;
    }

    /**
     * Creates the editor card: a title, the two sliders, and the color and
     * Done buttons.
     *
     * @return the card
     */
    private JPanel createCard() {
        final JPanel card = new JPanel(new BorderLayout(GAP, GAP));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createEmptyBorder(PAD_V, PAD_H, PAD_V, PAD_H));
        card.setMaximumSize(CARD_SIZE);
        card.setPreferredSize(CARD_SIZE);

        final JLabel title = new JLabel("Edit Ball", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        card.add(title, BorderLayout.NORTH);
        card.add(createSliderPanel(), BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);
        return card;
    }

    /**
     * Creates the panel holding the labeled speed and direction sliders.
     *
     * @return the slider panel
     */
    private JPanel createSliderPanel() {
        final JPanel sliders = new JPanel(new GridLayout(2, 2, SMALL_GAP, SMALL_GAP));
        sliders.setOpaque(false);
        sliders.add(createWhiteLabel("Speed:"));
        sliders.add(mySpeedSlider);
        sliders.add(createWhiteLabel("Direction:"));
        sliders.add(myDirectionSlider);
        return sliders;
    }

    /**
     * Creates a label with white text.
     *
     * @param text the label text
     * @return the label
     */
    private static JLabel createWhiteLabel(final String text) {
        final JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    /**
     * Creates the bottom of the card: a row of color buttons above a Done button.
     *
     * @return the button panel
     */
    private JPanel createButtonPanel() {
        final JPanel colorRow = new JPanel(new GridLayout(1, 4, SMALL_GAP, SMALL_GAP));
        colorRow.setOpaque(false);
        colorRow.add(createColorButton("Purple", UW_PURPLE));
        colorRow.add(createColorButton("Gold", UW_GOLD));
        colorRow.add(createColorButton("Red", Color.RED));
        colorRow.add(createColorButton("Blue", Color.BLUE));

        final JPanel doneRow = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP, SMALL_GAP));
        doneRow.setOpaque(false);
        final JButton closeButton = new JButton("Done");
        closeButton.addActionListener(e -> dismiss());
        doneRow.add(closeButton);

        final JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setOpaque(false);
        bottom.add(colorRow);
        bottom.add(doneRow);
        return bottom;
    }

    /**
     * Shows the editor for the given ball, populating the sliders
     * with the ball's current speed and direction.
     *
     * @param ball the ball to edit
     */
    public void editBall(final Ball ball) {
        myBall = ball;
        final int speed = (int) Math.round(ball.getSpeed());
        mySpeedSlider.setValue(Math.min(speed, MAX_SPEED));

        final double angle = Math.toDegrees(
                Math.atan2(ball.getVY(), ball.getVX()));
        myDirectionSlider.setValue((int) ((angle + MAX_ANGLE) % MAX_ANGLE));

        setVisible(true);
        repaint();
    }

    /**
     * Hides the editor overlay.
     */
    public void dismiss() {
        myBall = null;
        setVisible(false);
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        // Draw semi-transparent dark background
        graphics.setColor(SCRIM);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(graphics);
    }

    /**
     * Applies the current slider values as speed and direction to the
     * ball being edited.
     */
    private void applySpeedDirection() {
        if (myBall == null) {
            return;
        }
        final double speed = mySpeedSlider.getValue();
        final double angle = Math.toRadians(myDirectionSlider.getValue());
        myBall.setVX(speed * Math.cos(angle));
        myBall.setVY(speed * Math.sin(angle));
    }

    /**
     * Creates a small colored button that sets the ball's color.
     *
     * @param name the button label
     * @param color the color to apply
     * @return the configured button
     */
    private JButton createColorButton(final String name,
                                      final Color color) {
        final JButton button = new JButton(name);
        button.setForeground(color);
        button.addActionListener(e -> applyColor(color));
        return button;
    }

    /**
     * Sets the color of the ball being edited, if there is one.
     *
     * @param color the new color
     */
    private void applyColor(final Color color) {
        if (myBall != null) {
            myBall.setColor(color);
        }
    }

    /**
     * Blocks clicks from passing through to the simulation, and dismisses the
     * editor when the click lands outside the card.
     */
    private final class DismissListener extends MouseAdapter {
        @Override
        public void mousePressed(final MouseEvent event) {
            final Point cardLoc = SwingUtilities.convertPoint(
                    BallEditorOverlay.this, event.getPoint(), myCard);
            if (!myCard.contains(cardLoc)) {
                dismiss();
            }
        }
    }
}
