package com.kirekov.juu.monad;

import com.kirekov.juu.collection.Streaming;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Monad for lazy calculations. Allows to build a chain of operations with deferred execution.
 * Operations will NOT be executed until {@link Lazy#calculate()} method will be called.
 *
 * @param <T> the type of the return parameter
 * @since 1.0
 */
public final class Lazy<T> implements Streaming<T> {

  private final Supplier<T> supplier;

  private Lazy(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  /**
   * Instantiates new Lazy object.
   *
   * @param supplier supplier which will be called by {@link Lazy#calculate()} method
   * @param <T>      the type of the return parameter
   * @return lazy object
   * @throws NullPointerException if supplier is null
   */
  public static <T> Lazy<T> of(Supplier<T> supplier) {
    return new Lazy<>(Objects.requireNonNull(supplier));
  }

  /**
   * Makes the chain with input parameter of previous execution. <br>
   * <b>NB:</b> map does not trigger execution.
   *
   * @param mapper mapping function
   * @param <U>    the return type
   * @return lazy object
   * @throws NullPointerException if mapper is null
   */
  public <U> Lazy<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    return Lazy.of(() -> mapper.apply(supplier.get()));
  }

  /**
   * Makes the chain with input parameter of previous execution.
   * <br>
   * <b>NB:</b> flatMap does not trigger the execution.
   * <br>
   * After triggering the execution flatMap returns not the lazy object, but the result of its inner
   * calculation.
   * <pre> {@code
   * Lazy.of(() -> 1)
   *     .flatMap(v -> Lazy.of(() -> v + 1))
   *     .calculate()
   * }</pre>
   * returns 2
   *
   * @param mapper mapping function
   * @param <U>    the return type
   * @return lazy object
   * @throws NullPointerException if mapper is null
   */
  public <U> Lazy<U> flatMap(Function<? super T, ? extends Lazy<? extends U>> mapper) {
    Objects.requireNonNull(mapper);
    return Lazy.of(() -> mapper.apply(supplier.get()).calculate());
  }

  /**
   * Triggers calculation and returns the result.
   *
   * @return the result of calculation
   */
  public T calculate() {
    return supplier.get();
  }

  /**
   * Triggers calculation and returns the result as {@link Stream}.
   *
   * @return the stream of calculation result
   * @see Lazy#calculate()
   */
  @Override
  public Stream<T> stream() {
    return Stream.of(calculate());
  }
}
