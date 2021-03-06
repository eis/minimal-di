package fi.eis.libraries.di;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        logger.debug("class-instance module: " + DependencyInjection.classesWithInstances(classObjectMap));
        super.modules.add(DependencyInjection.classesWithInstances(classObjectMap)) ;
    }

    private static Map.Entry<Object,Method> tuple(Object object, Method method) {
        return new HashMap.SimpleEntry<>(object, method);
    }
    private static final Comparator<Map.Entry<Object,Method>> ConfClassCreationMethodTuplesComparator = new Comparator<Map.Entry<Object, Method>>() {
        @Override
        public int compare(Map.Entry<Object, Method> o1, Map.Entry<Object, Method> o2) {
            return Integer.compare(o1.getValue().getParameterTypes().length, o2.getValue().getParameterTypes().length);
        }
    };

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
