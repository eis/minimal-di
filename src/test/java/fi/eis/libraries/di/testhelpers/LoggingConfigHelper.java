package fi.eis.libraries.di.testhelpers;

import static fi.eis.libraries.di.logger.SimpleLogger.PROPERTY_NAME_USE_SYSTEM_OUT_LOGGING;

public class LoggingConfigHelper {
    private static String systemOutPropertyValueHolder;

    public static void setSystemOutLogging() {
        systemOutPropertyValueHolder = System.getProperty(PROPERTY_NAME_USE_SYSTEM_OUT_LOGGING);
        System.setProperty(PROPERTY_NAME_USE_SYSTEM_OUT_LOGGING, "true");
    }

    public static void resetSystemOutLogging() {
        if (systemOutPropertyValueHolder == null) {
            System.clearProperty(PROPERTY_NAME_USE_SYSTEM_OUT_LOGGING);
        } else {
            System.setProperty(PROPERTY_NAME_USE_SYSTEM_OUT_LOGGING, systemOutPropertyValueHolder);
        }
    }
}
