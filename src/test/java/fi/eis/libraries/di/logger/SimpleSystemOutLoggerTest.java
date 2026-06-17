package fi.eis.libraries.di.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static fi.eis.libraries.di.testhelpers.LoggingConfigHelper.resetSystemOutLogging;
import static fi.eis.libraries.di.testhelpers.LoggingConfigHelper.setSystemOutLogging;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SimpleSystemOutLoggerTest {

    @BeforeClass
    public static void loggingSetup() {
        setSystemOutLogging();
    }

    @AfterClass
    public static void loggingReset() {
        resetSystemOutLogging();
    }
    @Test
    public void testDebugLevelDebugLogging() throws UnsupportedEncodingException {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.DEBUG);
        assertTrue(logger.isDebugEnabled());
        assertTrue(logger.isErrorEnabled());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        logger.setPrintOut(ps);
        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        String result = baos.toString("UTF-8");
        assertThat(result, Matchers.containsString("DEBUG"));
        assertThat(result, Matchers.containsString("How're you doing, John?"));
    }
    @Test
    public void testErrorLevelDebugLogging() throws UnsupportedEncodingException {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.ERROR);
        assertFalse(logger.isDebugEnabled());
        assertTrue(logger.isErrorEnabled());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        logger.setPrintOut(ps);
        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        String result = baos.toString("UTF-8");
        assertThat(result, not(Matchers.containsString("DEBUG")));
        assertThat(result, not(Matchers.containsString("How're you doing, John?")));
    }

    @Test
    public void testNoneLevelDebugLogging() throws UnsupportedEncodingException {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.NONE);
        assertFalse(logger.isDebugEnabled());
        assertFalse(logger.isErrorEnabled());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        logger.setPrintOut(ps);
        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        logger.error("Some error");
        String result = baos.toString("UTF-8");
        assertThat(result, not(Matchers.containsString("DEBUG")));
        assertThat(result, not(Matchers.containsString("How're you doing, John?")));
        assertThat(result, not(Matchers.containsString("ERROR")));
    }

    @Test
    public void testErrorLevelErrorLogging() throws UnsupportedEncodingException {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.ERROR);
        assertFalse(logger.isDebugEnabled());
        assertTrue(logger.isErrorEnabled());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        logger.setPrintOut(ps);
        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        logger.error("Quite well, %s.", "Matt");
        String result = baos.toString("UTF-8");
        assertThat(result, not(Matchers.containsString("DEBUG")));
        assertThat(result, not(Matchers.containsString("How're you doing, John?")));
        assertThat(result, Matchers.containsString("Quite well, Matt."));
    }
}
