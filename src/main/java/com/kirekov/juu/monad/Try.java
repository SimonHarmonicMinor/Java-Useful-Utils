package com.kirekov.juu.monad;

import com.kirekov.juu.collection.Streaming;
import com.kirekov.juu.lambda.CheckedFunction;
import com.kirekov.juu.lambda.CheckedSupplier;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Monad for retrieving values just like {@link Optional}, but instead a container considered as an
 * empty one if an exception has been thrown during calculations.
 * <br>
 * The monad acts <b>lazily</b>. So, all methods build a pipeline which execution is triggered on
 * any terminal operation.
 * <br>
 * List of terminal operations.
 * <ul>
 *   <li>{@linkplain Try#orElse(Object)}</li>
 *   <li>{@linkplain Try#orElseGet(Supplier)}</li>
 *   <li>{@linkplain Try#orElseGet(Function)}</li>
 *   <li>{@linkplain Try#orElseThrow()}</li>
 *   <li>{@linkplain Try#orElseThrow(Supplier)}</li>
 *   <li>{@linkplain Try#stream()}</li>
 * </ul>
 * <p>The class only catches exceptions of type {@link Exception}. It means that all {@linkplain
 * Throwable} instances shall be skipped. The motivation is that {@link Error} extends from
 * {@linkplain Throwable} but this exceptions should not be caught manually.</p>
 * <br>
 * The class is thread-safe if the pipeline is thread-safe too.
 *
 * @param <T> the type of the return value
 * @since 1.0
 */
public final class Try<T> implements Streaming<T> {

  private final CheckedSupplier<? extends T, ? extends Exception> valueSupplier;

  private Try(CheckedSupplier<? extends T, ? extends Exception> value) {
    this.valueSupplier = value;
  }

  /**
   * Create a monad with successful result.
   *
   * @param value the value to retrieve
   * @param <T>   the type of the value
   * @return monad with successful execution
   */
  public static <T> Try<T> success(T value) {
    return new Try<>(() -> value);
  }

  /**
   * Create a monad with error result. The execution throws {@linkplain NoSuchElementException}.
   *
   * @param <T> the type of the return value
   * @return monad with error result
   */
  public static <T> Try<T> error() {
    return new Try<>(() -> {
      throw new NoSuchElementException("'Try' container is empty");
    });
  }

  /**
   * Create a monad with error result. The execution throws {@code exceptionToThrow}.
   *
   * @param <T>              the type of the return value
   * @param exceptionToThrow the exception that is ought to be thrown
   * @return monad with error result
   * @throws NullPointerException if {@code exceptionToThrow} is null
   */
  public static <T> Try<T> error(Exception exceptionToThrow) {
    Objects.requireNonNull(exceptionToThrow, "exceptionToThrow cannot be null");
    return new Try<>(() -> {
      throw exceptionToThrow;
    });
  }


  /**
   * Create a monad of the given supplier. This is an intermediate operation.
   *
   * @param supplier supplier that returns value
   * @param <T>      the type of the return value
   * @return monad that holds that given supplier
   * @throws NullPointerException if suppliers parameter is null
   */
  public static <T> Try<T> of(CheckedSupplier<? extends T, ? extends Exception> supplier) {
    Objects.requireNonNull(supplier, "supplier cannot be null");
    return new Try<>(supplier);
  }

  /**
   * Map the value from one to another and return new monad. This is an intermediate operation.
   *
   * @param mapper mapping function
   * @param <U>    the type of the new value
   * @return a monad with mapped function
   * @throws NullPointerException if {@code mapper} is null
   */
  public <U> Try<U> map(CheckedFunction<? super T, ? extends U, ? extends Exception> mapper) {
    Objects.requireNonNull(mapper, "mapper cannot be null");
    return Try.of(() -> mapper.apply(valueSupplier.get()));
  }

  /**
   * Map the value from one to another and returns new monad. The mapping function must return
   * another {@link Try} container. This is an intermediate operation.
   * <br>
   * For instance,
   * <pre>{@code
   * Try.of(() -> 1)
   *    .flatMap(v -> Try.of(() -> v + 1))
   *    .orElse(-1)
   * }</pre>
   * returns 2, while
   * <pre>{@code
   * Try.of(() -> 1)
   *    .flatMap(v -> Try.of(() -> v / 0))
   *    .orElse(-1)
   * }</pre>
   * returns -1
   *
   * @param mapper mapping function
   * @param <U>    the type of the new value
   * @return monad with new value or an empty one
   * @throws NullPointerException if {@code mapper} is null
   * @see Try#map(CheckedFunction)
   */
  public <U> Try<U> flatMap(
      CheckedFunction<? super T, ? extends Try<? extends U>, ? extends Exception> mapper) {
    Objects.requireNonNull(mapper, "mapper cannot be null");
    return Try.of(() -> mapper.apply(valueSupplier.get()).orElseThrow());
  }

  /**
   * Filter the value with the given {@code predicate}. If {@code predicate} returns {@code false},
   * the {@linkplain NoSuchElementException} is thrown. This is an intermediate operation.
   *
   * @param predicate predicate function
   * @return the container itself or an empty one
   * @throws NullPointerException if predicate is null
   */
  public Try<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate cannot be null");
    return Try.of(() -> {
      final T value = valueSupplier.get();
      if (predicate.test(value)) {
        return value;
      }
      throw new NoSuchElementException("The filter does not pass");
    });
  }

  /**
   * Calculate the value and return the new monad that holds it. Otherwise, return the monad of the
   * default value. This is an intermediate operation.
   * <br>
   * <pre>{@code
   * Try.of(() -> "str")
   *    .orElseTry(() -> "newStr")
   *    .orElse("null")
   * }</pre>
   * returns {@code "str"}.
   * <pre>{@code
   * Try.<String>of(() -> {throw new Exception();})
   *    .orElseTry(() -> "newStr")
   *    .orElse("null")
   * }</pre>
   * returns {@code "newStr"}
   * <pre>{@code
   * Try.<String>of(() -> {throw new Exception();})
   *    .orElseTry(() -> {throw new Exception();})
   *    .orElse("null")
   * }</pre>
   * returns {@code "null"}.
   *
   * @param defaultValueSupplier supplier that provides the default value
   * @return the monad of the calculated value or the default one
   * @throws NullPointerException if {@code defaultValueSupplier} is null
   */
  public Try<T> orElseTry(CheckedSupplier<? extends T, ? extends Exception> defaultValueSupplier) {
    Objects.requireNonNull(defaultValueSupplier, "defaultValueSupplier cannot be null");
    return new Try<>(() -> {
      try {
        return valueSupplier.get();
      } catch (Exception e) {
        return defaultValueSupplier.get();
      }
    });
  }

  /**
   * Calculate the value and return it, if the calculation does not fail. Otherwise, return the
   * default value. This is a terminal operation.
   *
   * @param other the default value
   * @return the calculated value or the default one
   */
  public T orElse(T other) {
    try {
      return valueSupplier.get();
    } catch (Exception e) {
      return other;
    }
  }

  /**
   * Calculate the value and return it, if the calculation does not fail. Otherwise, return the
   * defaultValue. This is a terminal operation.
   *
   * @param defaultValueSupplier supplier the provides the default value
   * @return the calculated value or the default one
   * @throws NullPointerException if {@code defaultValueSupplier} is null
   */
  public T orElseGet(Supplier<? extends T> defaultValueSupplier) {
    Objects.requireNonNull(defaultValueSupplier, "defaultValueSupplier cannot be null");
    try {
      return valueSupplier.get();
    } catch (Exception e) {
      return defaultValueSupplier.get();
    }
  }

  /**
   * Calculate the value and return it, if the calculation does not fail. Otherwise, return the
   * default value. This is a terminal operation.
   *
   * @param defaultValueFunction function that accepts the exception that occurred and returns the
   *                             default value
   * @return the calculated value or the default one
   * @throws NullPointerException if {@code defaultValueFunction} is null
   */
  public T orElseGet(Function<? super Exception, ? extends T> defaultValueFunction) {
    Objects.requireNonNull(defaultValueFunction, "defaultValueFunction cannot be null");
    try {
      return valueSupplier.get();
    } catch (Exception e) {
      return defaultValueFunction.apply(e);
    }
  }

  /**
   * Calculate the value and return it, if the calculation does not fail. Otherwise, throws the
   * exception that led to error.
   *
   * @return the value of the container
   */
  public T orElseThrow() {
    try {
      return valueSupplier.get();
    } catch (Exception e) {
      return throwException(e);
    }
  }

  /**
   * Calculate the value and return it, if the calculation does not fail. Otherwise, throw the
   * supplied exception. This is a terminal operation.
   *
   * @param exceptionSupplier supplier, that returns exception
   * @param <E>               the type of the exception
   * @return the value of the container
   * @throws E                    if the value calculation fails
   * @throws NullPointerException if {@code exceptionSupplier} is null
   */
  public <E extends Exception> T orElseThrow(Supplier<? extends E> exceptionSupplier) throws E {
    Objects.requireNonNull(exceptionSupplier);
    try {
      return valueSupplier.get();
    } catch (Exception e) {
      throw exceptionSupplier.get();
    }
  }

  /**
   * Transform a monad to {@linkplain Stream}. This is a terminal operation.
   *
   * @return stream of container's value, if the calculation passes. Otherwise, returns {@link
   * Stream#empty()}
   */
  @Override
  public Stream<T> stream() {
    try {
      return Stream.of(valueSupplier.get());
    } catch (Exception e) {
      return Stream.empty();
    }
  }

  @SuppressWarnings("unchecked")
  private static <E extends Exception, T> T throwException(Exception exception) throws E {
    throw (E) exception;
  }
}
