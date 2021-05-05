package com.kirekov.juu.lambda;

/**
 * Represents a supplier of results. May throw an exception
 *
 * @param <T> the type of the result
 * @param <E> the type of the exception
 * @since 1.0
 */
@FunctionalInterface
public interface CheckedSupplier<T, E extends Throwable> {

  T get() throws E;
}
