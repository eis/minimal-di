package fi.eis.libraries.di.logger;

import java.io.PrintStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple logger that can delegate to java.util.logging or System.out.
 *
 * By default, JUL is used, but it can be overridden with a property.
 *
 * JUL support is due to fact that it's available in all java environments without additional dependencies,
 * and with jul bridge, can be redirected to wherever if needed.
 */
public class SimpleLogger {

    public static final String PROPERTY_NAME_USE_SYSTEM_OUT_LOGGING = "minimal-di.logging.systemout";
    public enum LoggingBackend {
        USE_SYSTEM_OUT,
        USE_JUL_LOGGING
    }
    private volatile LogLevel logLevel = LogLevel.NONE;

    private final String className;
    private final Logger logger;

    private transient PrintStream printStream = System.out;

    private final LoggingBackend loggingBackend;

    public SimpleLogger(Class<?> targetClass) {
        this(targetClass,
                Boolean.parseBoolean(System.getProperty(PROPERTY_NAME_USE_SYSTEM_OUT_LOGGING, "false"))
                        ? LoggingBackend.USE_SYSTEM_OUT : LoggingBackend.USE_JUL_LOGGING);
    }
    public SimpleLogger(Class<?> targetClass, LoggingBackend loggingBackend) {
        this.className = targetClass.getName();
        this.loggingBackend = loggingBackend;
        
        if (loggingBackend == LoggingBackend.USE_JUL_LOGGING) {
            this.logger = Logger.getLogger(targetClass.getSimpleName());
        } else {
            this.logger = null;
        }
    }
    public void setLogLevel(LogLevel value) {
        this.logLevel = value;
    }
    public boolean isDebugEnabled() {
        return this.logLevel.ordinal() >= LogLevel.DEBUG.ordinal();
    }
    public boolean isErrorEnabled() {
        return this.logLevel.ordinal() >= LogLevel.ERROR.ordinal();
    }
    
    public void debug(String message) {
        if (!this.isDebugEnabled()) {
            return;
        }
        if (loggingBackend == LoggingBackend.USE_SYSTEM_OUT) {
            printStream.printf("%s DEBUG %s: %s%n",
                    new Date(), className, message);
        } else {
            this.logger.log(Level.FINE, message);
        }
    }
    public void debug(String message, Object... parameters) {
        if (this.isDebugEnabled()) {
            debug(String.format(message, parameters));
        }
    }
    public void error(String message) {
        if (!this.isErrorEnabled()) {
            return;
        }
        if (loggingBackend == LoggingBackend.USE_SYSTEM_OUT) {
            printStream.printf("%s ERROR %s: %s%n",
                    new Date(), className, message);
        } else {
            this.logger.log(Level.SEVERE, message);
        }
    }
    public void error(String message, Object... parameters) {
        if (this.isErrorEnabled()) {
            error(String.format(message, parameters));
        }
    }

    /** package-private to facilitate testing - we don't need to set this from everywhere */
    void setPrintOut(PrintStream stream) {
        this.printStream = stream;
    }
}
