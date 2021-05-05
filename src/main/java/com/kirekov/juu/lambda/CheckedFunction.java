package com.kirekov.juu.lambda;

/**
 * Represents a function that accepts one argument and produces a result. May throw an exception.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception
 * @since 1.0
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Throwable> {

  R apply(T t) throws E;
}
