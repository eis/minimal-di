package fi.eis.libraries.di.test.moduleconfig;

/**
 *
 * @author eis
 */

import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.context.Context;
import fi.eis.libraries.di.context.Module;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DIInheritanceTest {

    @Test
    public void testDi() {
        Module mSuppliers = DependencyInjection.module(
            Dependency.class
        );
        Module mClasses = DependencyInjection.module(ClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ClassToInit instance = diContext.get(ClassToInit.class);
        assertNotNull("was not initialized: " + instance, instance.getDependency());
    }

    @Test
    public void testDiWithInheritance() {
        Module mSuppliers = DependencyInjection.module(
            AnotherDependency.class
        );
        Module mClasses = DependencyInjection.module(ClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ClassToInit instance = diContext.get(ClassToInit.class);
        assertNotNull("was not initialized: " + instance, instance.getDependency());
    }
}