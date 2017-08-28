package fi.eis.libraries.di.testhelpers;

public class ExampleJavaConfig {

    public MockClassInNeedOfConstructorDependency mockClassInNeedOfConstructorDependency(DependencyMockInterface dependencyMockInterface) {
        return new MockClassInNeedOfConstructorDependency(dependencyMockInterface);
    }

    public DependencyMockInterface dependencyMock() {
        return new DependencyMock();
    }
}
