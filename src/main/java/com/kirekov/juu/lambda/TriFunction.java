package com.kirekov.juu.lambda;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents the function that accepts three arguments and produces a result.
 *
 * @param <T1> the type of the first argument
 * @param <T2> the type of the second argument
 * @param <T3> the type of the third argument
 * @param <R>  the type of the result
 * @see Function
 * @see BiFunction
 */
@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {

  R apply(T1 t1, T2 t2, T3 t3);
}
