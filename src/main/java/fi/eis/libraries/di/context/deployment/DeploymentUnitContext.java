package fi.eis.libraries.di.context.deployment;

import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.context.Context;
import fi.eis.libraries.di.logger.SimpleLogger.LogLevel;

import java.io.File;
import java.util.List;

/**
 * <p>
 * A "Deployment Unit" is a .jar file or a directory path of the class given as a parameter.
 * </p>
 *
 * @author eis
 */
public class DeploymentUnitContext extends Context {

    public DeploymentUnitContext(Class sourceClass) {
        super();
        // a jar or a directory path of whoever initiated us
        initFrom(new BeanArchive(sourceClass.getProtectionDomain().getCodeSource().getLocation()));
    }

    public DeploymentUnitContext(Class sourceClass, LogLevel logLevel) {
        super();
        setLogLevel(logLevel);
        BeanArchive beanArchive = new BeanArchive(
                sourceClass.getProtectionDomain().getCodeSource().getLocation(),
                logLevel);
        initFrom(beanArchive);
    }

    public DeploymentUnitContext(File sourceJar) {
        super();
        initFrom(new BeanArchive(sourceJar));
    }

    public DeploymentUnitContext(File sourceJar, LogLevel logLevel) {
        super();
        setLogLevel(logLevel);
        BeanArchive archive = new BeanArchive(sourceJar, logLevel);
        initFrom(archive);
    }

    private void initFrom(BeanArchive builder) {
        List<Class> classes = builder.getClasses();
        logger.debug("got classes " + classes);
        super.modules.add(DependencyInjection.module(classes));
    }

}
