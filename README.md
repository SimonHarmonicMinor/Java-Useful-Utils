# Java Useful Utils
Just some useful classes for everyday coding that might make your code shorter and cleaner

### Table of contents
* [Quick start](#quick-start)
* [Status](#status)
* [Usage](#usage)

### Quick start

Maven:
```xml
<dependency>
    <groupId>com.github.simonharmonicminor</groupId>
    <artifactId>java-useful-utils</artifactId>
    <version>1.0</version>
</dependency>
```
Gradle:
```groovy
implementation 'com.github.simonharmonicminor:java-useful-utils:1.0' 
```

### Status
[![Build Status](https://travis-ci.com/SimonHarmonicMinor/Java-Useful-Utils.svg?branch=master)](https://travis-ci.com/SimonHarmonicMinor/Java-Useful-Utils)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SimonHarmonicMinor_Java-Useful-Utils&metric=alert_status)](https://sonarcloud.io/dashboard?id=SimonHarmonicMinor_Java-Useful-Utils)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SimonHarmonicMinor_Java-Useful-Utils&metric=coverage)](https://sonarcloud.io/dashboard?id=SimonHarmonicMinor_Java-Useful-Utils)

### Usage
The library contains of three big parts.
* [Measurement](#measurement)
* [Monads](#monads) (functional style programming)
* [Collections](#collections)

##### Measurement
Sometimes we all face with the problem when we need to measure time of execution of one function or method.
Usually it suppose to be done like this:

```java
long before = System.currentTimeMillis();
int result = doSomething();
long after = System.currentTimeMillis();
long measurementResult = after - before;
```

It might be OK, if we need to do this once. 
But if there are several functions that have to be measured, 
writing the same code snippets every time is gonna be exhausting.

So here is the solution:
```java
ExecutionResult<Integer> exec =
    Measure.executionTime(this::doSomething)
           .inMillis();
int res = exec.getResult();
long time = exec.getTime();
assert exec.getMeasureUnit() == MeasureUnit.MILLIS;
```

Simple, right? If you need to measure something in different
units, just call the appropriate method (`inMillis()` in this case).

And what if you need to measure code block from one point to another? 
Well, `Profiler` is what you need.
```java
Profiler profiler = Profiler.startMeasuringInMillis();
// do some useful stuff
...
long time = profiler.stopMeasuring();
```

Such code style is much clearer than putting `System.currentTimeInMillis()` everywhere.

##### Monads
I think every java developer used at least one monad - `java.util.Optional`.
This class allows to work with nullable values much more efficiently.
```java
return Optional.ofNullable(getNullableString())
               .map(str -> str + "I've just edited it")
               .orElse("Oh my god. It is empty");
```

This library has two monads: `Try` and `Lazy`.

`Try` allows you to work with methods that may throw an exception
in the same way as `Optional`. For instance, suppose we have such code:

```java

```

