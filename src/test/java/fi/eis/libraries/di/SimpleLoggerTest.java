package fi.eis.libraries.di;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.hamcrest.Matchers;

import fi.eis.libraries.di.SimpleLogger.LogLevel;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleLoggerTest {
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
        assertThat(result, Matchers.not(Matchers.containsString("DEBUG")));
        assertThat(result, Matchers.not(Matchers.containsString("How're you doing, John?")));
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
        assertThat(result, Matchers.not(Matchers.containsString("DEBUG")));
        assertThat(result, Matchers.not(Matchers.containsString("How're you doing, John?")));
        assertThat(result, Matchers.containsString("Quite well, Matt."));
    }
}
