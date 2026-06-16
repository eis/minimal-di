package fi.eis.libraries.di.test.javaconfig;

import fi.eis.libraries.di.testhelpers.DependencyMockInterface;

public class MockClassInNeedOfConstructorDependency {
    private final DependencyMockInterface dependency;
    public MockClassInNeedOfConstructorDependency(DependencyMockInterface dependency) {
        this.dependency = dependency;
    }
    public boolean hasDependency() {
        return this.dependency != null;
    }
}
