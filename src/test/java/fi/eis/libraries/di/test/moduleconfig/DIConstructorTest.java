package fi.eis.libraries.di.test.moduleconfig;

/**
 *
 * @author eis
 */


import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.context.Context;
import fi.eis.libraries.di.context.Module;
import fi.eis.libraries.di.logger.SimpleLogger.LogLevel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

public class DIConstructorTest {

    @Test
    public void testDi() {
        Module mSuppliers = DependencyInjection.module(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.module(ConstClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ConstClassToInit instance = diContext.get(ConstClassToInit.class);
        assertNotNull("was not initialized: " + instance, instance.getDependency());
    }

    @Test
    public void testDiSkippingModuleAbstraction() {
        Context diContext = DependencyInjection.context(ConstDependency.class,
                ConstClassToInit.class);
        ConstClassToInit instance = diContext.get(ConstClassToInit.class);
        assertNotNull("was not initialized: " + instance, instance.getDependency());
    }

    @Test
    public void testDiWithInheritance() {
        Module mSuppliers = DependencyInjection.module(
            AnotherConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.module(ConstClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ConstClassToInit instance = diContext.get(ConstClassToInit.class);
        assertNotNull("was not initialized: " + instance, instance.getDependency());
    }

    // below is to test logging

    private PrintStream originalPrintStream;
    private ByteArrayOutputStream loggingOutputStream;
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
    public void testDiWithLoggingEnabled() throws UnsupportedEncodingException {
        Module mSuppliers = DependencyInjection.module(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.module(ConstClassToInit.class);
        mClasses.setLogLevel(LogLevel.DEBUG);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        diContext.setLogLevel(LogLevel.DEBUG);
        diContext.get(ConstClassToInit.class);
        
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        // there should be at least a context.get call
        assertThat(loggedStuff, containsString("context.get"));
    }
    @Test
    public void testDiWithLoggingDisabled() throws UnsupportedEncodingException {
        Module mSuppliers = DependencyInjection.module(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.module(ConstClassToInit.class);
        mClasses.setLogLevel(LogLevel.NONE);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        diContext.setLogLevel(LogLevel.NONE);
        diContext.get(ConstClassToInit.class);
        
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        assertThat(loggedStuff, not(containsString("context.get")));
    }
    @Test
    public void testDiWithLoggingDefault() throws UnsupportedEncodingException {
        Module mSuppliers = DependencyInjection.module(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.module(ConstClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        diContext.get(ConstClassToInit.class);
        
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        assertThat(loggedStuff, not(containsString("context.get")));
    }
}