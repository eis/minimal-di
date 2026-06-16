package fi.eis.libraries.di;

import java.io.File;
import java.util.List;
import java.util.Map;

import fi.eis.libraries.di.context.configclass.ConfigurationClassContext;
import fi.eis.libraries.di.logger.SimpleLogger.LogLevel;
import fi.eis.libraries.di.context.Context;
import fi.eis.libraries.di.context.deployment.DeploymentUnitContext;
import fi.eis.libraries.di.context.Module;

/**
 * 
 * Main entrypoint class for the dependency injection. It supports three forms of dependency
 * injection: you can
 * 1) ask it to create a context from the deployment unit (e.g. jar) in question (deploymentUnitContext())
 * 2) ask it to create a context based on spring-style configuration class (configurationClassContext())
 * 3) list the classes and their instances explicitly as modules and ask it to create a context based on that (module() + context())
 *
 * @author eis
 */
public class DependencyInjection {
    public static Module module(Class... classes) {
        return new Module(classes);
    }
    public static Module module(List<Class> classes) {
        return new Module(classes);
    }
    public static Module module(Map<Class,Object> classesWithInstances) {
        return new Module(classesWithInstances);
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

    public static Context configurationClassContext(Class... exampleJavaConfigClass) {
        return new ConfigurationClassContext(exampleJavaConfigClass);
    }
    public static Context configurationClassContext(LogLevel logLevel, Class... exampleJavaConfigClass) {
        return new ConfigurationClassContext(logLevel, exampleJavaConfigClass);
    }
}
