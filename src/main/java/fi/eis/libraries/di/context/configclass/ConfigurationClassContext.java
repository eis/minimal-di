package fi.eis.libraries.di.context.configclass;

import fi.eis.libraries.di.DependencyInjection;
import fi.eis.libraries.di.context.Context;
import fi.eis.libraries.di.logger.SimpleLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Configuration Class is a Spring-style class that contains the required configuration
 * to do dependency injection. It contains the definition of dependency associations so that
 * it can be used to figure out which class depends on what.
 *
 * This class contains the logic to handle such classes, so we create a dependency injection context
 * based on such class or several classes. This class goes through all the methods in a
 * configuration class, creating the needed dependencies and instantiating the configured classes
 * with their dependencies.
 */
public class ConfigurationClassContext extends Context {
    private final Map<Class, Object> classObjectMap = new HashMap<>();

    public ConfigurationClassContext(Class... configurationClasses) {
        this(SimpleLogger.LogLevel.NONE, configurationClasses);
    }
    public ConfigurationClassContext(SimpleLogger.LogLevel logLevel, Class... configurationClassInstances) {
        setLogLevel(logLevel);

        try {
            List<Map.Entry<Object, Method>> confClassCreationMethodTuples = new ArrayList<>();
            for (Class configurationClass: configurationClassInstances){
                Object configurationClassInstance = configurationClass.newInstance();

                for (Method m : Arrays.asList(configurationClass.getMethods())) {
                    if (m.getDeclaringClass() != Object.class) {
                        logger.debug("Got method " + m);
                        confClassCreationMethodTuples.add(tuple(configurationClassInstance, m));
                    }
                }
            }
            Collections.sort(confClassCreationMethodTuples, ConfClassCreationMethodTuplesComparator);
            logger.debug("tuples: " + confClassCreationMethodTuples);

            for (Map.Entry<Object,Method> confClassCreationMethodTuple: confClassCreationMethodTuples) {
                Method method = confClassCreationMethodTuple.getValue();
                Object instance = newInstance(confClassCreationMethodTuple.getKey(), method);
                classObjectMap.put(method.getReturnType(), instance);
                logger.debug("instantiated and stored instance for class " + method.getReturnType());
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
        logger.debug("class-instance module: " + DependencyInjection.module(classObjectMap));
        super.modules.add(DependencyInjection.module(classObjectMap)) ;
    }

    private static Map.Entry<Object,Method> tuple(Object object, Method method) {
        return new HashMap.SimpleEntry<>(object, method);
    }
    private static final Comparator<Map.Entry<Object,Method>> ConfClassCreationMethodTuplesComparator =
            Comparator.comparingInt(o -> o.getValue().getParameterTypes().length);

    private Object newInstance(Object configurationClass, Method method) throws InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class[] parameterTypes = method.getParameterTypes();
        Object[] paramArr = new Object[parameterTypes.length];
        int i = 0;

        for(Class c : parameterTypes) {
            paramArr[i++] = classObjectMap.get(c);
        }
        return method.invoke(configurationClass, paramArr);
    }
}
