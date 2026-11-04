package edu.uw.tcss.app;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * The same payroll job as {@link PayrollBatch}, run from an interactive terminal.
 *
 * <p>A person now sits in the loop and types each answer, so a mistake can be fixed on the
 * spot. The program still controls the conversation. It asks for the name, then the rate,
 * then the hours, always in that order and one question at a time. The user cannot go back
 * to fix the rate after typing the hours, and nothing else can happen while the program
 * waits for a line of input. Event-driven GUIs remove that limit by letting the user act
 * in any order.
 *
 * @author Charles Bryan
 * @version Autumn 2026
 */
public final class PayrollTerminal {

    private PayrollTerminal() {
        super();
    }

    /**
     * Prompts for one employee at a time until the user says there are no more.
     *
     * @param args command line arguments, ignored
     */
    public static void main(final String[] args) {
        final Scanner console = new Scanner(System.in);
        BigDecimal total = BigDecimal.ZERO;
        int employees = 0;
        boolean another = true;

        ConsoleOutput.println("PAYROLL ENTRY");
        while (another) {
            // The program decides what comes next. The user can only answer the current question.
            ConsoleOutput.print("Employee name: ");
            final String name = console.nextLine().trim();
            final BigDecimal rate = promptForAmount(console, "Hourly rate: ");
            final BigDecimal hours = promptForAmount(console, "Hours worked: ");

            // Same pay rule as the batch run. Only the way input arrives has changed.
            final BigDecimal pay = PayrollBatch.grossPay(rate, hours);
            ConsoleOutput.printf("Gross pay for %s: $%s%n", name, pay);
            total = total.add(pay);
            employees++;

            ConsoleOutput.print("Another employee? (y/n): ");
            another = console.nextLine().trim().startsWith("y");
        }

        ConsoleOutput.printf("%nPaid %d employees, total gross $%s%n", employees, total);
    }

    /**
     * Asks for a non-negative number, asking again until the user types one.
     *
     * @param console the scanner reading the keyboard
     * @param prompt the question to print
     * @return the number the user typed
     */
    private static BigDecimal promptForAmount(final Scanner console, final String prompt) {
        BigDecimal amount = null;
        while (amount == null) {
            ConsoleOutput.print(prompt);
            // The program blocks here, and only here, until the user presses Enter.
            final String line = console.nextLine().trim();
            try {
                amount = new BigDecimal(line);
                if (amount.signum() < 0) {
                    ConsoleOutput.println("  Must not be negative. Try again.");
                    amount = null;
                }
            } catch (final NumberFormatException exception) {
                // A person is here to fix the mistake, so ask again instead of rejecting it.
                ConsoleOutput.println("  \"" + line + "\" is not a number. Try again.");
            }
        }
        return amount;
    }
}
