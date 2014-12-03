package fi.eis.libraries.di;

import java.io.File;

import fi.eis.libraries.di.testhelpers.MockClassInNeedOfDependency;
import org.junit.Assert;
import org.junit.Test;

/**
 * Creation Date: 1.12.2014
 * Creation Time: 21:43
 *
 * @author eis
 */
public class DIClassScanningJarLoadingTest {

    @Test
    public void testDi() {
        Context diContext = DependencyInjection.deploymentUnitContext(
                new File(this.getClass().getResource("/minimal-di-1.0-SNAPSHOT-test-targets.jar").getPath()));
        MockClassInNeedOfDependency instance = diContext.get(MockClassInNeedOfDependency.class);
        Assert.assertNotNull("was not initialized: " + instance, instance.dependency);
    }
    @Test(expected=IllegalArgumentException.class)
    public void testDiWithNonExistingFile() {
        DependencyInjection.deploymentUnitContext(new File("does-not-exist"));
    }
}
