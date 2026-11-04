package edu.uw.tcss.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A batch payroll run, the way programs worked before anyone sat at a terminal.
 *
 * <p>All of the input exists before the run starts. It is a deck of timecards, one employee
 * per card, stored in timecards.txt beside this class. The program reads the cards in order,
 * computes each paycheck, and prints a report. No person is in the loop, so a bad card cannot
 * be fixed during the run. It goes on a reject list for someone to read after the job ends.
 * A real payroll run also updated a master file of employees, which this demo leaves out.
 *
 * @author Charles Bryan
 * @version Autumn 2026
 */
public final class PayrollBatch {

    /** The timecard deck, loaded from the classpath relative to this class. */
    private static final String DECK_NAME = "timecards.txt";

    /** Hours past this threshold are paid at the overtime rate. */
    private static final BigDecimal OVERTIME_THRESHOLD = new BigDecimal("40");

    /** Overtime pays time and a half. */
    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");

    /** Money is rounded to whole cents. */
    private static final int CENTS = 2;

    /** Each card holds four fields: id, name, hourly rate, hours worked. */
    private static final int FIELD_COUNT = 4;

    /** Position of the employee id on a card. */
    private static final int ID = 0;

    /** Position of the employee name on a card. */
    private static final int NAME = 1;

    /** Position of the hourly rate on a card. */
    private static final int RATE = 2;

    /** Position of the hours worked on a card. */
    private static final int HOURS = 3;

    /** One row of the payroll register. */
    private static final String ROW_FORMAT = "%-6s %-20s %8s %7s %10s%n";

    private PayrollBatch() {
        super();
    }

    /**
     * Runs the whole payroll job from the first card to the last, then prints the report.
     *
     * @param args command line arguments, ignored
     * @throws IOException if the deck cannot be read
     */
    public static void main(final String[] args) throws IOException {
        final List<String> deck = readDeck();
        final List<String> rejects = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        ConsoleOutput.println("PAYROLL REGISTER");
        ConsoleOutput.printf(ROW_FORMAT, "ID", "NAME", "RATE", "HOURS", "GROSS");

        // One pass over the deck, in deck order. The program never stops to ask anyone.
        for (final String card : deck) {
            final String[] fields = card.split(",");
            if (fields.length != FIELD_COUNT) {
                rejects.add(card + "   <- wrong number of fields");
            } else {
                try {
                    final BigDecimal rate = new BigDecimal(fields[RATE].trim());
                    final BigDecimal hours = new BigDecimal(fields[HOURS].trim());
                    final BigDecimal pay = grossPay(rate, hours);
                    ConsoleOutput.printf(ROW_FORMAT, fields[ID], fields[NAME], rate, hours, pay);
                    total = total.add(pay);
                } catch (final NumberFormatException exception) {
                    // Nobody is at a console to retype the card, so it waits for a human.
                    rejects.add(card + "   <- rate or hours is not a number");
                }
            }
        }

        ConsoleOutput.printf("%nPaid %d employees, total gross $%s%n", deck.size() - rejects.size(), total);
        ConsoleOutput.printf("%nREJECTED CARDS (%d)%n", rejects.size());
        for (final String reject : rejects) {
            ConsoleOutput.println("  " + reject);
        }
    }

    /**
     * Computes gross pay, with hours past the threshold paid at time and a half.
     *
     * @param rate the hourly rate
     * @param hours the hours worked
     * @return the gross pay, rounded to cents
     */
    static BigDecimal grossPay(final BigDecimal rate, final BigDecimal hours) {
        final BigDecimal regularHours = hours.min(OVERTIME_THRESHOLD);
        final BigDecimal overtimeHours = hours.subtract(regularHours);
        return rate.multiply(regularHours)
                .add(rate.multiply(OVERTIME_MULTIPLIER).multiply(overtimeHours))
                .setScale(CENTS, RoundingMode.HALF_UP);
    }

    /**
     * Reads every non-blank card in the deck, in order, before any processing starts.
     *
     * @return the cards, one string per card
     * @throws IOException if the deck cannot be read
     */
    private static List<String> readDeck() throws IOException {
        // A relative name resolves against this class's package, so the deck sits beside it.
        final InputStream deck = PayrollBatch.class.getResourceAsStream(DECK_NAME);
        if (deck == null) {
            throw new IllegalStateException("Cannot find " + DECK_NAME + " on the classpath. "
                    + "It belongs beside PayrollBatch.java in src/edu/uw/tcss/app/.");
        }
        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(deck, StandardCharsets.UTF_8))) {
            return reader.lines().filter(line -> !line.isBlank()).toList();
        }
    }
}
