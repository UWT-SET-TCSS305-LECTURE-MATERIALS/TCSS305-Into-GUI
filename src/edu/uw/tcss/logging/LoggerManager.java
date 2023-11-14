package edu.uw.tcss.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class to centralize Logging.
 *
 * @author Charles Bryan
 * @version 1.0
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
     * @param theMessage the message to log.
     */
    public static void info(final Object theMessage) {
        LOGGER.info(theMessage.toString());
    }

    /**
     * Log a WARNING level message to the current Logger.
     * @param theMessage the message to log.
     */
    public static void warning(final Object theMessage) {
        LOGGER.warning(theMessage.toString());
    }

    /**
     * Log an ERROR level message to the current Logger.
     * @param theMessage the message to log.
     */
    public static void error(final Object theMessage) {
        LOGGER.severe(theMessage.toString());
    }

}
