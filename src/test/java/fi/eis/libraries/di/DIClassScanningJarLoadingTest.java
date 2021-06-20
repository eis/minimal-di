package fi.eis.libraries.di;

import java.io.File;

import fi.eis.libraries.di.testhelpers.MockClassInNeedOfDependency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertNotNull;


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
        assertNotNull(instance.dependency, "was not initialized: " + instance);
    }
    @Test
    public void testDiWithNonExistingFile() {
        Assertions.assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        DependencyInjection.deploymentUnitContext(new File("does-not-exist"));
                    }
                }
        );
    }
}
