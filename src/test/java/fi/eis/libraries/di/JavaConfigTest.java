package fi.eis.libraries.di;

import fi.eis.libraries.di.testhelpers.DependencyMock;
import fi.eis.libraries.di.testhelpers.DependencyMockInterface;
import fi.eis.libraries.di.testhelpers.MockClassInNeedOfConstructorDependency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class JavaConfigTest {
    @Test
    public void testJavaConfig() {
        Context diContext = DependencyInjection.configurationClasses(SimpleLogger.LogLevel.DEBUG, ExampleJavaConfig.class);
        MockClassInNeedOfConstructorDependency instance = diContext.get(MockClassInNeedOfConstructorDependency.class);
        assertTrue(instance.hasDependency(), "was not initialized: " + instance);
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