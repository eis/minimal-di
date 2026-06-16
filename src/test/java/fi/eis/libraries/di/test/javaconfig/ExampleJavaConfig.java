package fi.eis.libraries.di.test.javaconfig;

import fi.eis.libraries.di.testhelpers.DependencyMockInterface;

public class ExampleJavaConfig {

    public MockClassInNeedOfConstructorDependency mockClassInNeedOfConstructorDependency(DependencyMockInterface dependencyMockInterface) {
        return new MockClassInNeedOfConstructorDependency(dependencyMockInterface);
    }

    public DependencyMockInterface dependencyMock() {
        return new DependencyMock();
    }
}
