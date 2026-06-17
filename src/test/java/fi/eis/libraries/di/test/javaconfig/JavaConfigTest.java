package fi.eis.libraries.di.test.javaconfig;

import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.context.Context;
import fi.eis.libraries.di.logger.LogLevel;
import org.junit.Assert;
import org.junit.Test;

public class JavaConfigTest {
    @Test
    public void testJavaConfig() {
        Context diContext = DependencyInjection.configurationClassContext(LogLevel.DEBUG, ExampleJavaConfig.class);
        MockClassInNeedOfConstructorDependency instance = diContext.get(MockClassInNeedOfConstructorDependency.class);
        Assert.assertTrue("was not initialized: " + instance, instance.hasDependency());
    }
}