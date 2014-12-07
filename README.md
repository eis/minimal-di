Minimal DI
==========

This is a minimal dependency injection framework.

Reasoning
---------

This was created out of need for simple DI. Guice is 600kt, Weld
CDI is 3M of files, and Spring is even more. That's not what I
needed - I needed simple DI system that would just implement the
basic features and nothing more, with few simple classes. This is it.

"Minimal DI" currently weighs 17kt as a .jar, which is simple enough.
It doesn't need any additional dependencies which was one of
its main goals.

Usage
-----

Inject your dependencies to constructors:

```java
import fi.eis.libraries.di.Inject;

public class CurrentApp {
    private MyDependency dependency;
    @Inject
    public CurrentApp(MyDependency dependency) {
        this.dependency = dependency;
    }
}
```

Or directly to your private variables:

```java
import fi.eis.libraries.di.Inject;

public class CurrentApp {
    @Inject
    private MyDependency dependency;
}
```

and fire up the system using either all classes in the deployment unit:

```java
public static void main(String args[]) {
    Context diContext = DependencyInjection.deploymentUnitContext(CurrentApp.class);
    CurrentApp app = diContext.get(CurrentApp.class);
    app.run();
}
```

or listing all implementation classes part of DI:

```java
public static void main(String args[]) {
    Module module = DependencyInjection.classes(CurrentApp.class, ClassImplementingDependency.class);
    Context diContext = DependencyInjection.context(module);
    CurrentApp app = diContext.get(CurrentApp.class);
    app.run();
}
```

That's it.