package com.github.simonharmonicminor.juu.measure;


import java.util.Objects;

/**
 * A class for measuring time of function execution with retrieving result.
 *
 * @param <T> type of function return result
 * @see ExecutionResult
 * @since 0.1
 */
public class Measure<T> {

    private final Supplier<T> supplier;

    private Measure(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Creates an instance of {@link Measure} class with function that will be executed
     * Function will not be executed until {@link Measure#inMillis()} or {@link Measure#inNanos()}
     * wil be called
     *
     * @param supplier lambda function which needs to be executed. Cannot be null
     * @param <T>      function return type
     * @return an instance of class with given function
     * @throws NullPointerException if supplier is null
     */
    public static <T> Measure<T> executionTime(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return new Measure<>(supplier);
    }

    /**
     * Creates an instance of {@link Measure} class with procedure that will be executed.
     * Procedure will not be executed until {@link Measure#inMillis()} or {@link Measure#inNanos()}
     * wil be called
     *
     * @param action lambda procedure which needs to be executed. Cannot be {@code null}
     * @return an instance of class with given procedure
     * @throws NullPointerException if action is null
     */
    public static Measure<Void> executionTime(Action action) {
        Objects.requireNonNull(action);
        return new Measure<>(() -> {
            action.execute();
            return null;
        });
    }

    /**
     * @return {@link ExecutionResult} measured in millis.
     * If fail will return {@link ExecutionResult#failed()}
     */
    public ExecutionResult<T> inMillis() {
        try {
            long time = System.currentTimeMillis();
            T result = supplier.get();
            return new ExecutionResult<>(result, System.currentTimeMillis() - time, MeasureUnit.MILLIS);
        } catch (Exception e) {
            return ExecutionResult.failed();
        }
    }

    /**
     * @return {@link ExecutionResult} measured in nanos.
     * If fail will return {@link ExecutionResult#failed()}
     */
    public ExecutionResult<T> inNanos() {
        try {
            long time = System.nanoTime();
            T result = supplier.get();
            return new ExecutionResult<>(result, System.nanoTime() - time, MeasureUnit.NANOS);
        } catch (Exception e) {
            return ExecutionResult.failed();
        }
    }

    /**
     * A variant of java {@link java.util.function.Supplier} interface which allows to throw
     * an {@link Exception}
     *
     * @param <T> return type
     */
    @FunctionalInterface
    public interface Supplier<T> {
        T get() throws Exception;
    }

    /**
     * An interface with one method that takes nothing and returns nothing but allows
     * to throw an {@link Exception}
     */
    @FunctionalInterface
    public interface Action {
        void execute() throws Exception;
    }
}
