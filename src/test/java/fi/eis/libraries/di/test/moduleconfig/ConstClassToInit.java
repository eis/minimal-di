package fi.eis.libraries.di.test.moduleconfig;

import fi.eis.libraries.di.Inject;

public class ConstClassToInit {
    private final ConstDependencyInterface dependency;

    @Inject
    public ConstClassToInit(ConstDependencyInterface dependency) {
        this.dependency = dependency;
    }


    @Override
    public String toString() {
        return "ConstClassToInit{" +
                "dependency=" + dependency +
                '}';
    }

    public ConstDependencyInterface getDependency() {
        return this.dependency;
    }
}
