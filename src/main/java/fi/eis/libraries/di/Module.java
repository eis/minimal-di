package fi.eis.libraries.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creation Date: 30.11.2014
 * Creation Time: 22:49
 *
 * @author eis
 */
public class Module {

    private SimpleLogger logger = new SimpleLogger(this.getClass());
    
    private static final Object NO_INSTANCE = new Object();
    private Map<Class, Object> providers = new HashMap<>();
    public Module(Class... providers) {
        for (Class clazz: providers) {
            this.providers.put(clazz, NO_INSTANCE );
        }
    }
    public Module(List<Class> providers) {
        for (Class clazz: providers) {
            this.providers.put(clazz, NO_INSTANCE );
        }
    }
    public void setDebug(boolean flag) {
        this.logger.setDebug(flag);
    }

    public void add(Module module) {
        this.providers.putAll(module.providers);
    }

    public boolean has(Class type) {
        logger.debugPrint("Has %s? (in %s)%n", type, providers.keySet());
        for (Class clazz: providers.keySet()) {
            logger.debugPrint("Comparing %s with %s%n", clazz, type);
            if (type.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }
    private Class getImplClassFor(Class type) {
        for (Class clazz: providers.keySet()) {
            if (type.isAssignableFrom(clazz)) {
                return clazz;
            }
        }
        throw new IllegalArgumentException("not allowed: " + type);
    }
    private <T> T getInstance(Class <T> type) {
        for (Class clazz: providers.keySet()) {
            if (type.isAssignableFrom(clazz)) {
                return (T)providers.get(clazz);
            }
        }
        throw new IllegalArgumentException("not allowed: " + type);

    }
    public <T>T get(Class<T> type) {
        if (!has(type)) {
            throw new IllegalArgumentException("not supported: " + type);
        }
        Object storedValue = getInstance(type);
        if (storedValue != NO_INSTANCE) {
            return (T)storedValue;
        } else {
            try {
                Class implClass = getImplClassFor(type);
                storedValue = newInstance(implClass);
                providers.put(implClass, storedValue);
                return (T)storedValue;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    // example borrowed from
    // http://java-bytes.blogspot.fi/2010/04/create-your-own-dependency-injection.html
    private Object newInstance(Class implClass) throws InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for(Constructor constructor: implClass.getConstructors()) {
            if(constructor.isAnnotationPresent(Inject.class)) {

                Class[] parameterTypes = constructor.getParameterTypes();
                Object[] objArr = new Object[parameterTypes.length];

                int i = 0;

                for(Class c : parameterTypes) {
                    objArr[i++] = get(c);
                }

                Object resObj = implClass.getConstructor(parameterTypes).newInstance(objArr);

                return resObj;
            }
        }
        return implClass.newInstance();
    }
}
