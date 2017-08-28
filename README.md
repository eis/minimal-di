Minimal DI
==========

[![Build Status](http://img.shields.io/travis/eis/minimal-di/master.svg)](https://travis-ci.org/eis/minimal-di)
[![Coverage Status](http://img.shields.io/coveralls/eis/minimal-di/master.svg)](https://coveralls.io/github/eis/minimal-di?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.eis.libraries/minimal-di/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.eis.libraries/minimal-di/)
[![Dependency Status](https://www.versioneye.com/java/com.github.eis.libraries:minimal-di/badge.svg?style=flat)](https://www.versioneye.com/java/com.github.eis.libraries:minimal-di/)
[![Codebeat Analysis](https://codebeat.co/badges/8e124ee5-7860-4551-b13f-64a2d109222c)](https://codebeat.co/projects/github-com-eis-minimal-di)

This is a minimal dependency injection framework.

Reasoning
---------

This was created out of need for simple DI. Guice weighs 600kt, Weld
CDI 3M, and Spring even more. Even PicoContainer weighs 317kt.
That's not what I needed - I needed a very simple DI system
that would just implement the basic features and nothing more, with
few simple classes. This is it.

"Minimal DI" currently weighs 23k as a .jar, which is simple enough.
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

or use a Spring-style javaconfig class:

```java
public static void main(String args[]) {
    Context diContext = DependencyInjection.configurationClasses(ExampleJavaConfig.class);
    CurrentApp app = diContext.get(CurrentApp.class);
    app.run();
}
```

That's it.

Maven dependency
----------------

This library can be used from Maven Central:

```xml
<dependency>
  <groupId>com.github.eis.libraries</groupId>
  <artifactId>minimal-di</artifactId>
  <version>1.0</version>
</dependency>
```
