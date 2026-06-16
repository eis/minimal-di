package fi.eis.libraries.di.test.deploymentunit;

import java.io.File;

import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.context.Context;
import fi.eis.libraries.di.test.deploymentunit.MockClassInNeedOfDependency;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author eis
 */
public class DIClassScanningJarLoadingTest {

    @Test
    public void testDi() {
        Context diContext = DependencyInjection.deploymentUnitContext(
                new File(this.getClass().getResource("/minimal-di-1.2.0-SNAPSHOT-tests.jar").getPath()));
        MockClassInNeedOfDependency instance = diContext.get(MockClassInNeedOfDependency.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.dependency);
    }
    @Test(expected=IllegalArgumentException.class)
    public void testDiWithNonExistingFile() {
        DependencyInjection.deploymentUnitContext(new File("does-not-exist"));
    }
}
