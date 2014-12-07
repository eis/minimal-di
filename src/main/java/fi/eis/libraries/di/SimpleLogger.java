package fi.eis.libraries.di;

import java.io.PrintStream;
import java.util.Date;

public class SimpleLogger {
    private volatile boolean debugFlag = false;
    private final String className;
    private transient PrintStream printStream = System.out;

    public SimpleLogger(Class<?> targetClass) {
        this.className = targetClass.getName();
    }
    public void setDebug(boolean value) {
        this.debugFlag = value;
    }
    public void debugPrint(String message) {
        if (this.debugFlag) {
            printStream.printf("%s DEBUG %s: %s%n",
                    new Date(), className, message);
        }
    }
    public void debugPrint(String message, Object... parameters) {
        if (this.debugFlag) {
            debugPrint(String.format(message, parameters));
        }
    }
    public void setPrintOut(PrintStream stream) {
        this.printStream = stream;
    }
}
