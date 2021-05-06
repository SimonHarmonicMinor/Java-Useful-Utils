package com.kirekov.juu.measure;

import static com.kirekov.juu.measure.MeasureConverter.millisToSeconds;

import com.kirekov.juu.lambda.Action;
import java.util.Objects;
import java.util.function.Supplier;

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
   * Creates an instance of {@link Measure} class with function that will be executed Function will
   * not be executed until {@link Measure#inMillis()} or {@link Measure#inNanos()} will be called.
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
   * Creates an instance of {@link Measure} class with procedure that will be executed. Procedure
   * will not be executed until {@link Measure#inMillis()} or {@link Measure#inNanos()} will be
   * called.
   *
   * @param action lambda procedure which needs to be executed. Cannot be {@code null}
   * @return an instance of class with given procedure
   * @throws NullPointerException if action is null
   */
  public static Measure<Void> executionTime(Action action) {
    Objects.requireNonNull(action);
    return new Measure<>(
        () -> {
          action.execute();
          return null;
        });
  }

  /**
   * Gets execution result measured in {@linkplain MeasureUnit#MILLIS}.
   *
   * @return execution result measured in millis.
   */
  public ExecutionResult<T> inMillis() {
    long time = System.currentTimeMillis();
    T result = supplier.get();
    return new ExecutionResult<>(result, System.currentTimeMillis() - time, MeasureUnit.MILLIS);
  }

  /**
   * Gets execution result measured in {@linkplain MeasureUnit#NANOS}.
   *
   * @return execution result measured in nanos.
   */
  public ExecutionResult<T> inNanos() {
    long time = System.nanoTime();
    T result = supplier.get();
    return new ExecutionResult<>(result, System.nanoTime() - time, MeasureUnit.NANOS);
  }

  /**
   * Gets execution result measure in {@linkplain MeasureUnit#SECONDS}.
   *
   * @return execution result measured in seconds
   * @since 1.1
   */
  public ExecutionResult<T> inSeconds() {
    long start = System.currentTimeMillis();
    T result = supplier.get();
    long finish = System.currentTimeMillis();
    return new ExecutionResult<>(result, millisToSeconds(finish - start), MeasureUnit.SECONDS);
  }
}
