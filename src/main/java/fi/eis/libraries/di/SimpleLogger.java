package fi.eis.libraries.di;

import java.io.PrintStream;
import java.util.Date;

public class SimpleLogger {
    public static enum LogLevel {
        NONE, ERROR, DEBUG; 
    }
    private volatile LogLevel logLevel = LogLevel.NONE;

    private final String className;
    private transient PrintStream printStream = System.out;

    public SimpleLogger(Class<?> targetClass) {
        this.className = targetClass.getName();
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
        if (this.isDebugEnabled()) {
            printStream.printf("%s DEBUG %s: %s%n",
                    new Date(), className, message);
        }
    }
    public void debug(String message, Object... parameters) {
        if (this.isDebugEnabled()) {
            debug(String.format(message, parameters));
        }
    }
    public void error(String message) {
        if (this.isErrorEnabled()) {
            printStream.printf("%s ERROR %s: %s%n",
                    new Date(), className, message);
        }
    }
    public void error(String message, Object... parameters) {
        if (this.isErrorEnabled()) {
            error(String.format(message, parameters));
        }
    }
    /** package-private - we don't need to set this from everywhere */
    void setPrintOut(PrintStream stream) {
        this.printStream = stream;
    }
}
