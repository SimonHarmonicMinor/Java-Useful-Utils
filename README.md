# Java Useful Utils
Truly immutable collections, functional errors handling, laziness and measurement utilities.

This library has no dependencies (except the test scope).

If you want to contribute, check out the [Contributing Guide](./CONTRIBUTING.md).

`master` branch provides the latest `DEV-SNAPSHOT` documentation.
You can find the specific version info by git tags.

### Table of contents
* [Quick start](#quick-start)
* [Status](#status)
* [Usage](#usage)
* [Authors](#authors)

### Quick start

You need Java 8+ to use the library.

Maven:

```xml
<dependency>
    <groupId>com.kirekov</groupId>
    <artifactId>java-useful-utils</artifactId>
</dependency>
```
Gradle:
```groovy
implementation 'com.kirekov:java-useful-utils' 
```

### Status
[![Maven Central](https://img.shields.io/maven-central/v/com.kirekov/java-useful-utils)](https://mvnrepository.com/artifact/com.kirekov/java-useful-utils)
[![Javadoc](https://javadoc.io/badge2/com.kirekov/java-useful-utils/javadoc.svg)](https://javadoc.io/doc/com.kirekov/java-useful-utils)
[![Build Status](https://travis-ci.com/SimonHarmonicMinor/Java-Useful-Utils.svg?branch=master)](https://travis-ci.com/SimonHarmonicMinor/Java-Useful-Utils)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SimonHarmonicMinor_Java-Useful-Utils&metric=alert_status)](https://sonarcloud.io/dashboard?id=SimonHarmonicMinor_Java-Useful-Utils)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SimonHarmonicMinor_Java-Useful-Utils&metric=coverage)](https://sonarcloud.io/dashboard?id=SimonHarmonicMinor_Java-Useful-Utils)
[![Hits-of-Code](https://hitsofcode.com/github/SimonHarmonicMinor/Java-Useful-Utils)](https://hitsofcode.com/github/SimonHarmonicMinor/Java-Useful-Utils/view)
[![checkstyle](https://img.shields.io/badge/checkstyle-intergrated-informational)](https://checkstyle.sourceforge.io/)
[![PMD](https://img.shields.io/badge/PMD-intergrated-informational)](https://pmd.github.io/pmd-6.35.0/pmd_rules_java.html)
[![MIT License](https://img.shields.io/apm/l/atomic-design-ui.svg?)](https://github.com/SimonHarmonicMinor/Java-Useful-Utils/blob/master/LICENSE)

### Usage
The library consists of three big parts.
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
units, just call the appropriate method 
(`inMillis()`, `inNanos()` or `inSeconds()`).

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

This library has two monads: [`Try`](#try) and [`Lazy`](#lazy).

###### Try

`Try` allows you to work with methods that may throw an exception
in the same way as `Optional` in a **lazy** way. For instance, suppose we have such code:

```java
int num;
try {
    String value = getStringValue();
    num = Integer.parseInt(value);
}
catch (NumberFormatException e) {
    num = getDefaultIntValue();
}
...
try {
    return executeRpc(num);
}
catch (RPCException e) {
    return SOME_DEFAULT_VALUE;
}
```

In this case we don't care about the exception type but the error fact itself.
JUU allows to rewrite this snippet as two equations:

```java
int num = Try.of(() -> Integer.parseInt(getStringValue()))
             .orElseGet(() -> getDefaultIntValue());
return Try.of(() -> executeRpc(num))
          .orElse(SOME_DEFAULT_VALUE);
```

Also we can use `map`, `flatMap` and `filter`
functions.

```java
int value = Try.of(this::getStringValue)
               .map(this::reverseString)
               .flatMap(str -> Try.of(() -> Integer.parseInt(str)))
               .filter(num -> num > 0)
               .orElseThrow(IllegalArgumentException::new);
```

Let's deconstruct this monad step by step:
1) Receives some string value from `getStringValue()` method.
2) Reverses string.
3) Converts string to integer.
4) Checks if number is positive.
5) If any of previous steps fails, throws `IllegalArgumentException`.

And here is the same code written in pure java:

```java
int value;
try {
    String val = getStringValue();
    String reversed = reverseString(val);
    value = Integer.parseInt(reversed);
    if (value <= 0) {
        throw new RuntimeException("no no no");
    }
}
catch (Exception e) {
    throw new IllegalArgumentException();
}
```

If you need the exception that led to the error, you can use
`orElseGet` variation.

```java
Try.of(() -> Integer.parseInt(getStringValue))
   .map(x -> 2 % x)
   .orElseGet((Exception reason) -> {
       log.error("Something went wrong", reason);
       return 0;
   })
```

`reason` has the type of `Exception` and it's the instance of that very
exception that broke the chain. 
For instance, if `Integer.parseInt` threw an exception, 
the `reason` would be the type of `NumberFormatException`.
On the other hand, if `x == 0` in the `map` callback,
the reason would be the type of `ArithmeticException`.

The class only catches `Exception` type. 
It means that all `Throwable` instances are skipped.
The motivation is that `Error` extends from `Throwable` but these exceptions should not be caught manually.

The fact that `Try` monad acts *lazily* means
that you build a pipeline of execution that triggers on any *terminal* operation.

```java
Try<Integer> t = Try.of(() -> {
      println("First step");
      return 1;
    }).map(val -> {
      println("Second step");
      return val + 1;
    }).filter(val -> {
      println("Third step");
      return val > 0;
    });
// nothing prints here
    
assert 2 == t.orElseThrow();
// First step
// Second step
// Third step
```

All terminal operations are listed in the [javadoc](./src/main/java/com/kirekov/juu/monad/Try.java).

###### Lazy

The name of the monad defines its purpose.

```java
Lazy<String> lazy = Lazy.of(this::executeRpc)
                        .map(String::valueOf);
                        .map(this::reverseString);

String result = lazy.calculate();
```

The thing is that all declared steps won't be executed
until `calculate` method call. This behaviour is similar
to laziness of Haskell language. `Lazy` can be used to measure 
execution time of different chains.

```java
ImmutableList<Lazy<Integer>> list = getList();
ExecutionResult<Integer> minTimeResult =
        list.map(lazy -> Measure.executionTime(lazy::calculate)
                                .inMillis())
            .min(Comparator.comparingLong(ExecutionResult::getTime))
            .orElseThrow(IllegalStateException::new);       
```

You can also combine `Lazy` and `Try` monads to prevent unexpected errors.

##### Collections

The "Collections" part consists of two subparts: 
["Immutable collections"](#immutable-collections) and 
["Mutable containers"](#mutable-containers).

###### Immutable collections

One of the most irritating thing for me in Java 
is total absence of immutable collections. 
For instance, suppose we have such code:

```java
List<Integer> numbers = getNumbers();
List<Integer> result = doSomething(numbers);
```

Have this list been changed? What have been returned by
`doSomething()`? The same list or the new one?
If we delete an element from `numbers`, 
will it have an effect on `result`? 

Well, we can implement our own "immutable" list 
that inherits `java.util.List` and make mutating
methods (`add`, `clear`, `set`...) throw an exception.
But how do we know what implementation has been
passed as a parameter? It is not convenient to write
`try {} catch (Exception e) {}` or use [`Try`](#try)
on every `add` call.

JUU library provides new collections, which interfaces
do not inherits from java native Collections. 
Here is the scheme ![scheme](./juu%202.0.0.svg)

The recommended way to instantiate immutable collections is to use `Immutable` class.
```java
// lists

ImmutableList<Integer> list1 = 
        Immutable.listOf(1, 2, 3)   // accepts T...
ImmutableList<String> list2 = 
        Immutable.listOf(Arrays.asList("1", "2", "3"))  // accepts Iterable<T>

// sets

ImmutableSet<Integer> set1 =
        Immutable.setOf(1, 2, 3);
ImmutableSet<String> set2 =
        Immutable.setOf(Arrays.asList("1", "2", "3"));

// maps

ImmutableMap<String, Integer> map1 =
        Immutable.mapOf("1", 1, "2", 2, "3", 3);
ImmutableMap<String, Integer> map2 =
        Immutable.mapOf(Arrays.asList(
            Pair.of("1", 2),
            Pair.of("2", 2),
            Pair.of("3", 3)
        ))
```

You can also use collectors from `ImmutableCollectors` to create immutable collections
from `Stream`.

```java
ImmutableList<String> list = 
    getNumbersList().stream()
                    .map(String::valueOf)
                    .collect(ImmutableCollectors.toList());

ImmutableSet<String> set = 
    getNumbersList().stream()
                    .map(String::valueOf)
                    .collect(ImmutableCollectors.toSet());

ImmutableMap<String, Integer> map = 
    getNumbersList().stream()
                    .collect(ImmutableCollectors.toMap(
                        String::valueOf,
                        value -> value
                    ));
```

You can user Stream API with immutable collections as well,
but `ImmutableList` and `ImmutableSet` provides kotlin-like methods:
`map`, `flatMap`, `filter`, `min`, `max` and `zip` (for lists).
`ImmutableList` also has `sorted`, `zipWith`, `zipWithNext` and indexed methods 
`mapIndexed`, `flatMapIndexed`, `filterIndexed`.

```java
ImmutableList<Integer> mappedList = list.map(Integer::parseInt);
ImmutableSet<String> mappedSet = set.flatMap(str -> Arrays.asList(str, str + "1"));

ImmutableList<Integer> filtered1 = mappedList.filter(x -> x > 0);
ImmutableList<Integer> filtered2 = 
        mappedList.filterIndexed((index, val) -> index % 2 == 0);
```

`ImmutableList` provides Python-like Slice API. 
Which means, that you can use negative indices, steps and negative steps.

```java
ImmutableList<Integer> list = getList();          // [1, 2, 3, 4, 5, 6]
assert list.get(list.size() - 1) == list.get(-1)  // 6

// startIndex (inclusively), endIndex (exclusively), stepSize
list.slice(0, 3, 1);                              // [1, 2, 3]
list.slice(-1, 2, -1);                            // [6, 5, 4]
list.slice(0, 6, 2);                              // [1, 3, 5]
list.step(3)                                      // [1, 4]
// if stepSize is negative, startIndex is -1
list.step(-2)                                     // [6, 4, 2]
```

###### Mutable containers

Sometimes we need to return several values from method. 
Problem can be solved by creating values wrapper. 
But it may produce tons of "infrastructural code", especially if types vary.
JUU has special mutable containers that can be passed as a parameter.
For instance

```java
List<Row> someList = ...;
MutableValue<Row> biggest = new MutableValue<>(null);
// suppose to call `biggest.setValue(row)`
int affectedRows = fillWithMagic(someList, biggest);
...
return biggest.getValue();
```
Also lib has implementations for each primitive type.
`MutableInt`, `MutableDouble`, `MutableShort`,
`MutableLong`, `MutableFloat`, `MutableChar`,
`MutableByte`, `MutableBoolean`.

### Authors
- [@SimonHarmonicMinor](https://github.com/SimonHarmonicMinor)