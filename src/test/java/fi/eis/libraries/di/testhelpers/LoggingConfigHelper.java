package fi.eis.libraries.di.testhelpers;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

import static fi.eis.libraries.di.logger.SimpleLogger.PROPERTY_NAME_USE_SYSTEM_OUT_LOGGING;

public class LoggingConfigHelper {
    private static String systemOutPropertyValueHolder;
    private static String julConfigFilePropertyValueHolder;

    private static final String JUL_CONFIG_FILE_PROPERTY_NAME = "java.util.logging.config.file";

    public static void setJULLoggingConfiguration() {
        julConfigFilePropertyValueHolder = System.getProperty(JUL_CONFIG_FILE_PROPERTY_NAME);

        // Figure out the path to my test file
        String resourceName = "my-test-logging.properties";
        ClassLoader classLoader = LoggingConfigHelper.class.getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        String absolutePath = file.getAbsolutePath();

        // use that path
        System.setProperty(JUL_CONFIG_FILE_PROPERTY_NAME, absolutePath);
        try {
            LogManager.getLogManager().readConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
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
    public static void resetJULLoggingConfiguration() {
        if (julConfigFilePropertyValueHolder == null) {
            System.clearProperty(JUL_CONFIG_FILE_PROPERTY_NAME);
        } else {
            System.setProperty(JUL_CONFIG_FILE_PROPERTY_NAME, julConfigFilePropertyValueHolder);
        }
        try {
            LogManager.getLogManager().readConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
