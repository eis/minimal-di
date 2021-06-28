package fi.eis.libraries.di;

import fi.eis.libraries.di.testhelpers.DependencyMock;
import fi.eis.libraries.di.testhelpers.DependencyMockInterface;
import fi.eis.libraries.di.testhelpers.MockClassInNeedOfConstructorDependency;
import org.junit.Assert;
import org.junit.Test;

public class JavaConfigTest {
    @Test
    public void testJavaConfig() {
        Module module = DependencyInjection.configurationClasses(SimpleLogger.LogLevel.DEBUG, ExampleJavaConfig.class);
        Context diContext = DependencyInjection.context(module);
        MockClassInNeedOfConstructorDependency instance = diContext.get(MockClassInNeedOfConstructorDependency.class);
        Assert.assertTrue("was not initialized: " + instance, instance.hasDependency());
    }
}

class ExampleJavaConfig {

    public MockClassInNeedOfConstructorDependency mockClassInNeedOfConstructorDependency(DependencyMockInterface dependencyMockInterface) {
        return new MockClassInNeedOfConstructorDependency(dependencyMockInterface);
    }

    public DependencyMockInterface dependencyMock() {
        return new DependencyMock();
    }
}