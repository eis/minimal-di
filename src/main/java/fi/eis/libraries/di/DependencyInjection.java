package fi.eis.libraries.di;

import java.io.File;
import java.util.List;
import java.util.Map;

import fi.eis.libraries.di.SimpleLogger.LogLevel;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 22:54
 *
 * @author eis
 */
public class DependencyInjection {
    static Module classes(Class... classes) {
        return new Module(classes);
    }
    static Module classes(List<Class> classes) {
        return new Module(classes);
    }
    static Module classesWithInstances(Map<Class,Object> classesWithInstaces) {
        return new Module(classesWithInstaces);
    }
    public static Context context(Module... modules) {
        // add all other modules to combined one
        Module combinedModule = new Module();
        for (Module module: modules) {
            combinedModule.add(module);
        }
        // create a new module based on combined one
        return new Context(combinedModule);
    }

    public static Context deploymentUnitContext(Class sourceClass) {
        return new DeploymentUnitContext(sourceClass);
    }
    public static Context deploymentUnitContext(File jarFile) {
        return new DeploymentUnitContext(jarFile);
    }
    public static Context deploymentUnitContext(Class sourceClass, LogLevel logLevel) {
        return new DeploymentUnitContext(sourceClass, logLevel);
    }
    public static Context deploymentUnitContext(File jarFile, LogLevel logLevel) {
        return new DeploymentUnitContext(jarFile, logLevel);
    }

    public static Context configurationClasses(Class... exampleJavaConfigClass) {
        return new ConfigurationClassContext(exampleJavaConfigClass);
    }
    public static Context configurationClasses(LogLevel logLevel, Class... exampleJavaConfigClass) {
        return new ConfigurationClassContext(logLevel, exampleJavaConfigClass);
    }
}
