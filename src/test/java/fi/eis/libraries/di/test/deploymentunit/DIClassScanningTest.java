package fi.eis.libraries.di.test.deploymentunit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.logger.SimpleLogger.LogLevel;
import fi.eis.libraries.di.context.Context;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author eis
 */
public class DIClassScanningTest {

    @Test
    public void testDi() {
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass());
        MockClassInNeedOfDependency instance = diContext.get(MockClassInNeedOfDependency.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.dependency);
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
    }
    
    @After
    public void resetSystemOutRedirection() {
        System.setOut(originalPrintStream);
    }
    @Test
    public void testDiLoggingEnabled() throws UnsupportedEncodingException {
        // actual test
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass(), LogLevel.DEBUG);
        diContext.get(MockClassInNeedOfDependency.class);
        
        // after tests, we check what was logged
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        
        // there should be at least a context.get call
        Assert.assertThat(loggedStuff, Matchers.containsString("context.get"));
        
        // and path handling
        Assert.assertThat(loggedStuff, Matchers.containsString("Handle path"));
    }
    @Test
    public void testDiLoggingDisabled() throws UnsupportedEncodingException {
        // actual test
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass(), LogLevel.NONE);
        diContext.get(MockClassInNeedOfDependency.class);
        
        // after tests, we check what was logged
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        
        Assert.assertThat(loggedStuff, Matchers.not(Matchers.containsString("context.get")));
        Assert.assertThat(loggedStuff, Matchers.not(Matchers.containsString("Handle path")));
    }
    @Test
    public void testDiLoggingDefault() throws UnsupportedEncodingException {
        // actual test
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass());
        diContext.get(MockClassInNeedOfDependency.class);
        
        // after tests, we check what was logged
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        
        Assert.assertThat(loggedStuff, Matchers.not(Matchers.containsString("context.get")));
        Assert.assertThat(loggedStuff, Matchers.not(Matchers.containsString("Handle path")));
    }
}
