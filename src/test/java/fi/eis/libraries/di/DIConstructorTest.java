package fi.eis.libraries.di;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 21:21
 *
 * @author eis
 */


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fi.eis.libraries.di.SimpleLogger.LogLevel;


interface ConstDependencyInterface {
    void sayHello();
}

class ConstDependency implements ConstDependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}

class ConstClassToInit {
    private final ConstDependencyInterface dependency;

    @Inject
    public ConstClassToInit(ConstDependencyInterface dependency) {
        this.dependency = dependency;
    }


    @Override
    public String toString() {
        return "ConstClassToInit{" +
                "dependency=" + dependency +
                '}';
    }

    public ConstDependencyInterface getDependency() {
        return this.dependency;
    }
}

abstract class ConstAbstractParentForDependency implements ConstDependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}

class AnotherConstDependency extends ConstAbstractParentForDependency {}

public class DIConstructorTest {

    @Test
    public void testDi() {
        Module mSuppliers = DependencyInjection.classes(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.classes(ConstClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ConstClassToInit instance = diContext.get(ConstClassToInit.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.getDependency());
    }

    @Test
    public void testDiWithInheritance() {
        Module mSuppliers = DependencyInjection.classes(
            AnotherConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.classes(ConstClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ConstClassToInit instance = diContext.get(ConstClassToInit.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.getDependency());
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
        Module mSuppliers = DependencyInjection.classes(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.classes(ConstClassToInit.class);
        mClasses.setLogLevel(LogLevel.DEBUG);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        diContext.setLogLevel(LogLevel.DEBUG);
        diContext.get(ConstClassToInit.class);
        
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        // there should be at least a context.get call
        Assert.assertThat(loggedStuff, Matchers.containsString("context.get"));
    }
    @Test
    public void testDiWithLoggingDisabled() throws UnsupportedEncodingException {
        Module mSuppliers = DependencyInjection.classes(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.classes(ConstClassToInit.class);
        mClasses.setLogLevel(LogLevel.NONE);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        diContext.setLogLevel(LogLevel.NONE);
        diContext.get(ConstClassToInit.class);
        
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        Assert.assertThat(loggedStuff, Matchers.not(
                Matchers.containsString("context.get")));
    }
    @Test
    public void testDiWithLoggingDefault() throws UnsupportedEncodingException {
        Module mSuppliers = DependencyInjection.classes(
            ConstDependency.class,
            ConstClassToInit.class
        );
        Module mClasses = DependencyInjection.classes(ConstClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        diContext.get(ConstClassToInit.class);
        
        String loggedStuff = loggingOutputStream.toString("UTF-8");
        Assert.assertThat(loggedStuff, Matchers.not(
                Matchers.containsString("context.get")));
    }
}