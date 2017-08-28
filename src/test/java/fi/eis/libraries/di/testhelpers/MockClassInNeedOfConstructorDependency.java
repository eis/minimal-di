package fi.eis.libraries.di.testhelpers;

public class MockClassInNeedOfConstructorDependency {
    private final DependencyMockInterface dependency;
    public MockClassInNeedOfConstructorDependency(DependencyMockInterface dependency) {
        this.dependency = dependency;
    }
    public boolean hasDependency() {
        return this.dependency != null;
    }
}
