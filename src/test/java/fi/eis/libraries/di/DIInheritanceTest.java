package fi.eis.libraries.di;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 21:21
 *
 * @author eis
 */


interface DependencyInterface {
    void sayHello();
}
class Dependency implements DependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}
class ClassToInit {
    @Inject
    DependencyInterface dependency;

    @Override
    public String toString() {
        return "ClassToInit{" +
                "dependency=" + dependency +
                '}';
    }

    public DependencyInterface getDependency() {
        return this.dependency;
    }
}

abstract class AbstractParentForDependency implements DependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}

class AnotherDependency extends AbstractParentForDependency {}

public class DIInheritanceTest {

    @Test
    public void testDi() {
        Module mSuppliers = DependencyInjection.classes(
            Dependency.class
        );
        Module mClasses = DependencyInjection.classes(ClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ClassToInit instance = diContext.get(ClassToInit.class);
        assertNotNull(instance.getDependency(), "was not initialized: " + instance);
    }

    @Test
    public void testDiWithInheritance() {
        Module mSuppliers = DependencyInjection.classes(
            AnotherDependency.class
        );
        Module mClasses = DependencyInjection.classes(ClassToInit.class);
        Context diContext = DependencyInjection.context(mClasses, mSuppliers);
        ClassToInit instance = diContext.get(ClassToInit.class);
        assertNotNull(instance.getDependency(), "was not initialized: " + instance);
    }
}