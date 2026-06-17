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
        Assert.assertTrue(logger.isDebugEnabled());
        Assert.assertTrue(logger.isErrorEnabled());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        logger.setPrintOut(ps);
        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        String result = baos.toString("UTF-8");
        Assert.assertThat(result, Matchers.containsString("DEBUG"));
        Assert.assertThat(result, Matchers.containsString("How're you doing, John?"));
    }
    @Test
    public void testErrorLevelDebugLogging() throws UnsupportedEncodingException {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.ERROR);
        Assert.assertFalse(logger.isDebugEnabled());
        Assert.assertTrue(logger.isErrorEnabled());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        logger.setPrintOut(ps);
        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        String result = baos.toString("UTF-8");
        Assert.assertThat(result, Matchers.not(Matchers.containsString("DEBUG")));
        Assert.assertThat(result, Matchers.not(Matchers.containsString("How're you doing, John?")));
    }
    @Test
    public void testErrorLevelErrorLogging() throws UnsupportedEncodingException {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.ERROR);
        Assert.assertFalse(logger.isDebugEnabled());
        Assert.assertTrue(logger.isErrorEnabled());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        logger.setPrintOut(ps);
        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        logger.error("Quite well, %s.", "Matt");
        String result = baos.toString("UTF-8");
        Assert.assertThat(result, Matchers.not(Matchers.containsString("DEBUG")));
        Assert.assertThat(result, Matchers.not(Matchers.containsString("How're you doing, John?")));
        Assert.assertThat(result, Matchers.containsString("Quite well, Matt."));
    }
}
