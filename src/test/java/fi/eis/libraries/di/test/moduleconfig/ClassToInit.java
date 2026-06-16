package fi.eis.libraries.di.test.moduleconfig;

import fi.eis.libraries.di.Inject;

public class ClassToInit {
    @Inject
    DependencyInterface dependency;

    @Override
    public String toString() {
        return "ClassToInit{" +
                "dependency=" + dependency +
                '}';
    }

    public DependencyInterface getDependency() {
        return this.dependency;
    }
}
