package edu.uw.tcss.bouncingballs.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * A thin bar at the bottom of the simulation window that displays
 * the total kinetic energy of the system. The bar fill width is
 * proportional to energy and the color shifts from green (low)
 * through yellow (medium) to red (high).
 *
 * @author Charles Bryan
 * @version 1.0
 */
public class StatusBar extends JPanel {

    /** The preferred height of the status bar. */
    private static final int BAR_HEIGHT = 25;

    /** The maximum energy value for scaling the bar fill. Auto-scales upward. */
    private static final double INITIAL_MAX_ENERGY = 10000.0;

    /** How much headroom to leave when the energy passes the current maximum. */
    private static final double SCALE_HEADROOM = 1.2;

    /** The bar's background color. */
    private static final Color BACKGROUND = new Color(40, 40, 40);

    /** Left margin for the energy text, in pixels. */
    private static final int TEXT_X = 6;

    /** Distance from the bottom of the bar to the text baseline, in pixels. */
    private static final int TEXT_BASELINE_OFFSET = 7;

    /** The ratio at which the bar color is pure yellow. */
    private static final double MIDPOINT = 0.5;

    /** Font for the energy text display. */
    private static final Font ENERGY_FONT = new Font("SansSerif", Font.PLAIN, 11);

    /** The current total energy value. */
    private double myEnergy;

    /** The maximum energy seen so far, used for auto-scaling. */
    private double myMaxEnergy;

    /**
     * Creates a new status bar with zero energy displayed.
     */
    public StatusBar() {
        myEnergy = 0;
        myMaxEnergy = INITIAL_MAX_ENERGY;
        setPreferredSize(new Dimension(0, BAR_HEIGHT));
    }

    /**
     * Updates the displayed energy value and triggers a repaint.
     *
     * @param totalEnergy the new total kinetic energy
     */
    public void updateEnergy(final double totalEnergy) {
        myEnergy = totalEnergy;
        if (totalEnergy > myMaxEnergy) {
            myMaxEnergy = totalEnergy * SCALE_HEADROOM;
        }
        repaint();
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        final int w = getWidth();
        final int h = getHeight();

        // Background
        g2.setColor(BACKGROUND);
        g2.fillRect(0, 0, w, h);

        // Energy bar fill
        final double ratio = Math.min(myEnergy / myMaxEnergy, 1.0);
        final int fillWidth = (int) (ratio * w);
        g2.setColor(energyColor(ratio));
        g2.fillRect(0, 0, fillWidth, h);

        // Energy text
        g2.setColor(Color.WHITE);
        g2.setFont(ENERGY_FONT);
        final String text = String.format("Energy: %.0f", myEnergy);
        g2.drawString(text, TEXT_X, h - TEXT_BASELINE_OFFSET);
    }

    /**
     * Returns a color interpolated from green to yellow to red based on
     * the given ratio (0.0 = green, 0.5 = yellow, 1.0 = red).
     *
     * @param ratio the energy ratio between 0.0 and 1.0
     * @return the interpolated color
     */
    private Color energyColor(final double ratio) {
        final float r;
        final float g;
        if (ratio < MIDPOINT) {
            r = (float) (ratio * 2.0);
            g = 1.0f;
        } else {
            r = 1.0f;
            g = (float) ((1.0 - ratio) * 2.0);
        }
        return new Color(r, g, 0.0f);
    }
}
