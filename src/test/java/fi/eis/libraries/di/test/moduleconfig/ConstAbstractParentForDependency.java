package fi.eis.libraries.di.test.moduleconfig;

public abstract class ConstAbstractParentForDependency implements ConstDependencyInterface {
    public void sayHello() {
        System.out.println("hello");
    }
}
