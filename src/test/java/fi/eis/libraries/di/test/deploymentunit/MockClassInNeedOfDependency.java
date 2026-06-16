package fi.eis.libraries.di.test.deploymentunit;

import fi.eis.libraries.di.Inject;
import fi.eis.libraries.di.testhelpers.DependencyMockInterface;

/**
 * @author eis
 */
public class MockClassInNeedOfDependency {
    @Inject
    public DependencyMockInterface dependency;

}
