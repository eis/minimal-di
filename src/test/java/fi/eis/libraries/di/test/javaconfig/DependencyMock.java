package fi.eis.libraries.di.test.javaconfig;

import fi.eis.libraries.di.testhelpers.DependencyMockInterface;

/**
 *
 * @author eis
 */
public class DependencyMock implements DependencyMockInterface {
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}
