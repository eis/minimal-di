package fi.eis.libraries.di;

import fi.eis.libraries.di.SimpleLogger.LogLevel;
import fi.eis.libraries.di.testhelpers.MockClassInNeedOfDependency;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Creation Date: 1.12.2014
 * Creation Time: 21:43
 *
 * @author eis
 */
public class DIClassScanningTest {

    @Test
    public void testDi() {
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass());
        MockClassInNeedOfDependency instance = diContext.get(MockClassInNeedOfDependency.class);
        assertNotNull(instance.dependency, "was not initialized: " + instance);
    }
    private PrintStream originalPrintStream;
    private ByteArrayOutputStream loggingOutputStream;
    
    // redirect System.out to capture any logging
    @BeforeEach
    public void setupSystemOutRedirection() {
        originalPrintStream = System.out;
        
        loggingOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(loggingOutputStream);
        System.setOut(ps);
    }
    
    @AfterEach
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
        assertThat(loggedStuff, Matchers.containsString("context.get"));
        
        // and path handling
        assertThat(loggedStuff, Matchers.containsString("Handle path"));
    }
    @Test
    public void testDiLoggingDisabled() throws UnsupportedEncodingException {
        // actual test
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass(), LogLevel.NONE);
        diContext.get(MockClassInNeedOfDependency.class);
        
        // after tests, we check what was logged
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        
        assertThat(loggedStuff, Matchers.not(Matchers.containsString("context.get")));
        assertThat(loggedStuff, Matchers.not(Matchers.containsString("Handle path")));
    }
    @Test
    public void testDiLoggingDefault() throws UnsupportedEncodingException {
        // actual test
        Context diContext = DependencyInjection.deploymentUnitContext(this.getClass());
        diContext.get(MockClassInNeedOfDependency.class);
        
        // after tests, we check what was logged
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        
        assertThat(loggedStuff, Matchers.not(Matchers.containsString("context.get")));
        assertThat(loggedStuff, Matchers.not(Matchers.containsString("Handle path")));
    }
}
