package edu.uw.tcss.app;

/**
 * Prints a console program's output: a report, a prompt, a result.
 *
 * <p>This is not for logging or debugging. Use LoggerManager for those. Keeping System.out in
 * one class means the programs that print never touch it directly.
 *
 * @author Charles Bryan
 * @version Autumn 2026
 */
// The one place System.out is used on purpose, so the inspection is silenced here only.
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public final class ConsoleOutput {

    private ConsoleOutput() {
        super();
    }

    /**
     * Prints text with no line break, so a prompt and the user's answer share a line.
     *
     * @param text the text to print
     */
    public static void print(final String text) {
        System.out.print(text);
    }

    /**
     * Prints text followed by a line break.
     *
     * @param text the text to print
     */
    public static void println(final String text) {
        System.out.println(text);
    }

    /**
     * Prints formatted text, the same way String.format builds it.
     *
     * @param format the format string
     * @param values the values to fill into the format
     */
    public static void printf(final String format, final Object... values) {
        System.out.printf(format, values);
    }
}
