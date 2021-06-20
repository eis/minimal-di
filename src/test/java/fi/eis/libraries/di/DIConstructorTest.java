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

import fi.eis.libraries.di.SimpleLogger.LogLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


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
        assertNotNull(instance.getDependency(), "was not initialized: " + instance);
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
        assertNotNull( instance.getDependency(), "was not initialized: " + instance);
    }

    // below is to test logging

    private PrintStream originalPrintStream;
    private ByteArrayOutputStream loggingOutputStream;
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
        assertThat(loggedStuff, Matchers.containsString("context.get"));
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
        assertThat(loggedStuff, Matchers.not(
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
        assertThat(loggedStuff, Matchers.not(
                Matchers.containsString("context.get")));
    }
}