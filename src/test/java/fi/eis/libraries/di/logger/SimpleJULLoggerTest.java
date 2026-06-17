package fi.eis.libraries.di.logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static fi.eis.libraries.di.testhelpers.LoggingConfigHelper.resetJULLoggingConfiguration;
import static fi.eis.libraries.di.testhelpers.LoggingConfigHelper.setJULLoggingConfiguration;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleJULLoggerTest {

    private final List<String> logs = new ArrayList<>();
    private final Filter recordingFilter = record -> {
        logs.add(record.getLevel() + " " + record.getMessage());
        return true;
    };
    private Filter previousFilter;

    private final Logger julLogger = Logger.getLogger(this.getClass().getSimpleName());

    @Before
    public void loggingSetup() {
        previousFilter = julLogger.getFilter();
        julLogger.setFilter(recordingFilter);

        setJULLoggingConfiguration();
    }

    @After
    public void loggingReset() {
        julLogger.setFilter(previousFilter);
        logs.clear();
        
        resetJULLoggingConfiguration();
    }

    @Test
    public void testDebugLevelDebugLogging() {
        // Given
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.DEBUG);
        assertTrue(logger.isDebugEnabled());
        assertTrue(logger.isErrorEnabled());

        // When
        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");

        // Then
        String result = logs.stream().collect(Collectors.joining());
        
        assertThat(result, containsString("Hello"));
        assertThat(result, containsString("How're you doing, John?"));
    }

    @Test
    public void testErrorLevelDebugLogging() {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.ERROR);
        assertFalse(logger.isDebugEnabled());
        assertTrue(logger.isErrorEnabled());

        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");

        String result = logs.stream().collect(Collectors.joining());
        assertThat(result, not(containsString("DEBUG")));
        assertThat(result, not(containsString("How're you doing, John?")));
    }

    @Test
    public void testNoneLevelDebugLogging() {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.NONE);
        assertFalse(logger.isDebugEnabled());
        assertFalse(logger.isErrorEnabled());

        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        logger.error("Some error");

        String result = logs.stream().collect(Collectors.joining());
        assertThat(result, not(containsString("DEBUG")));
        assertThat(result, not(containsString("How're you doing, John?")));
        assertThat(result, not(containsString("ERROR")));
    }

    @Test
    public void testErrorLevelErrorLogging() {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setLogLevel(LogLevel.ERROR);
        assertFalse(logger.isDebugEnabled());
        assertTrue(logger.isErrorEnabled());

        logger.debug("Hello");
        logger.debug("How're you doing, %s?", "John");
        logger.error("Quite well, %s.", "Matt");

        String result = logs.stream().collect(Collectors.joining());
        assertThat(result, not(containsString("DEBUG")));
        assertThat(result, not(containsString("How're you doing, John?")));
        assertThat(result, containsString("Quite well, Matt."));
    }
}
