package fi.eis.libraries.di;

import fi.eis.libraries.di.testhelpers.ExampleJavaConfig;
import fi.eis.libraries.di.testhelpers.MockClassInNeedOfConstructorDependency;
import org.junit.Assert;
import org.junit.Test;

public class JavaConfigTest {
    @Test
    public void testJavaConfig() {
        Context diContext = DependencyInjection.configurationClasses(SimpleLogger.LogLevel.DEBUG, ExampleJavaConfig.class);
        MockClassInNeedOfConstructorDependency instance = diContext.get(MockClassInNeedOfConstructorDependency.class);
        Assert.assertTrue("was not initialized: " + instance, instance.hasDependency());
    }
}

