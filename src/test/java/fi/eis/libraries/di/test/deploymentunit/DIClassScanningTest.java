package fi.eis.libraries.di.test.deploymentunit;

import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.context.Context;
import fi.eis.libraries.di.logger.LogLevel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static fi.eis.libraries.di.testhelpers.LoggingConfigHelper.resetSystemOutLogging;
import static fi.eis.libraries.di.testhelpers.LoggingConfigHelper.setSystemOutLogging;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

/**
 * @author eis
 */
public class DIClassScanningTest {

    @Test
    public void testDi() {
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass());
        MockClassInNeedOfDependency instance = diContext.get(MockClassInNeedOfDependency.class);
        assertNotNull("was not initialized: " + instance, instance.dependency);
    }
    private PrintStream originalPrintStream;
    private ByteArrayOutputStream loggingOutputStream;
    
    // redirect System.out to capture any logging
    @Before
    public void setupSystemOutRedirection() {
        originalPrintStream = System.out;
        
        loggingOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(loggingOutputStream);
        System.setOut(ps);

        setSystemOutLogging();
    }
    
    @After
    public void resetSystemOutRedirection() {
        System.setOut(originalPrintStream);

        resetSystemOutLogging();
    }
    @Test
    public void testDiLoggingEnabled() throws UnsupportedEncodingException {
        // actual test
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass(), LogLevel.DEBUG);
        diContext.get(MockClassInNeedOfDependency.class);
        
        // after tests, we check what was logged
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        
        // there should be at least a context.get call
        assertThat(loggedStuff, containsString("context.get"));
        
        // and path handling
        assertThat(loggedStuff, containsString("Handle path"));
        assertThat(loggedStuff, containsString("test-classes"));
    }
    @Test
    public void testDiLoggingDisabled() throws UnsupportedEncodingException {
        // actual test
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass(), LogLevel.NONE);
        diContext.get(MockClassInNeedOfDependency.class);
        
        // after tests, we check what was logged
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        
        assertThat(loggedStuff, not(containsString("context.get")));
        assertThat(loggedStuff, not(containsString("Handle path")));
    }
    @Test
    public void testDiLoggingDefault() throws UnsupportedEncodingException {
        // actual test
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass());
        diContext.get(MockClassInNeedOfDependency.class);
        
        // after tests, we check what was logged
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        
        assertThat(loggedStuff, not(containsString("context.get")));
        assertThat(loggedStuff, not(containsString("Handle path")));
    }
}
