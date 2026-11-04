package edu.uw.tcss.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class to centralize Logging.
 *
 * @author Charles Bryan
 * @version Autumn 2026
 */
public final class LoggerManager {

    /**
     * A Logger object for all your logging needs.
     * You should move away from using System.out.println as your logging/debugging method.
     */
    public static final Logger LOGGER = Logger.getLogger(LoggerManager.class.getName());

    static {
        // Level.ALL - Display ALL logging messages
        // Level.OFF - Display NO logging messages
        LOGGER.setLevel(Level.ALL);
    }

    private LoggerManager() {
        super();

    }

    /**
     * Log an INFO level message to the current Logger.
     * @param message the message to log.
     */
    public static void info(final Object message) {
        LOGGER.info(message.toString());
    }

    /**
     * Log a WARNING level message to the current Logger.
     * @param message the message to log.
     */
    public static void warning(final Object message) {
        LOGGER.warning(message.toString());
    }

    /**
     * Log an ERROR level message to the current Logger.
     * @param message the message to log.
     */
    public static void error(final Object message) {
        LOGGER.severe(message.toString());
    }

}
