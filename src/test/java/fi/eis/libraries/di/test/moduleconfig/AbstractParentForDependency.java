package fi.eis.libraries.di.test.moduleconfig;

public abstract class AbstractParentForDependency implements DependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}
