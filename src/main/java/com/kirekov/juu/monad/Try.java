package com.kirekov.juu.monad;

import com.kirekov.juu.collection.Streaming;
import com.kirekov.juu.exception.EmptyContainerException;
import com.kirekov.juu.lambda.CheckedFunction;
import com.kirekov.juu.lambda.CheckedSupplier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Monad for retrieving values just like {@link java.util.Optional}, but instead a container
 * considered as an empty one if an exception has been thrown during calculations.
 * <br>
 * If container is empty, it keeps the reason of emptiness — the exception that led to it. The
 * exception can be retrieved with {@link Try#getReasonOfEmptiness()}.
 * <p>Class overrides {@link Object#equals(Object)} and {@link Object#hashCode()} methods.</p>
 * <p>All monad methods execute calculation <b>eagerly</b>.</p>
 * <p>The class only catches exceptions of type {@link Exception}. It means that all {@linkplain
 * Throwable} instances shall be skipped. The motivation is that {@link Error} extends from
 * {@linkplain Throwable} but this exceptions should not be caught manually.</p>
 *
 * @param <T> the type of the return value
 * @since 1.0
 */
public class Try<T> implements Streaming<T> {

  private static final String CONTAINER_IS_EMPTY_MSG = "Container is empty";
  private static final Try<?> EMPTY = new Try<>(null,
      new EmptyContainerException(CONTAINER_IS_EMPTY_MSG, null));

  private final T value;
  private final Exception reasonOfEmptiness;

  private Try(T value, Exception reasonOfEmptiness) {
    this.value = value;
    this.reasonOfEmptiness = reasonOfEmptiness;
  }

  /**
   * Returns an empty container. Does not create the new one, returns the same instance every time
   * The reason of emptiness defines as {@linkplain EmptyContainerException} with null cause.
   *
   * @param <T> the type of the return value
   * @return empty container
   */
  @SuppressWarnings("unchecked")
  public static <T> Try<T> empty() {
    return (Try<T>) EMPTY;
  }

  /**
   * Returns an empty container with defined reason of emptiness.
   *
   * @param <T>               the type of the return value
   * @param reasonOfEmptiness the reason of emptiness
   * @return empty container
   * @throws NullPointerException if {@code reasonOfEmptiness} is null
   */
  public static <T> Try<T> empty(Exception reasonOfEmptiness) {
    Objects.requireNonNull(reasonOfEmptiness);
    return new Try<>(null, reasonOfEmptiness);
  }

  /**
   * Instantiates a container by calculating the value of the supplier. If supplier fails with an
   * exception, returns {@link Try#empty(Exception)}.
   *
   * @param supplier function that returns value
   * @param <T>      the type of the return value
   * @param <E>      the type of the exception that supplier may throw
   * @return a container with the retrieved value or an empty one, if calculation failed with an
   * exception
   * @throws NullPointerException if suppliers parameter is null
   */
  public static <T, E extends Exception> Try<T> of(CheckedSupplier<T, E> supplier) {
    Objects.requireNonNull(supplier);
    try {
      return new Try<>(supplier.get(), null);
    } catch (Exception e) {
      return empty(e);
    }
  }

  /**
   * Returns a container with the value of the first calculated supplier that didn't fail. If all
   * suppliers fail, returns {@link Try#empty()}.
   *
   * @param suppliers collection of suppliers
   * @param <T>       the type of the return value
   * @param <E>       the type of the exception that supplier may throw
   * @return a container with the value of the succeeded supplier or {@link Try#empty()}
   * @throws NullPointerException if suppliers parameter is null
   * @see Try#of(CheckedSupplier)
   */
  public static <T, E extends Exception> Try<T> getFirst(
      Iterable<CheckedSupplier<T, E>> suppliers) {
    Objects.requireNonNull(suppliers);
    for (CheckedSupplier<T, E> supplier : suppliers) {
      Try<T> t = Try.of(supplier);
      if (t.isPresent()) {
        return t;
      }
    }
    return empty();
  }

  /**
   * Whether the value is present in the container.
   *
   * @return true if value is present and false otherwise
   */
  public boolean isPresent() {
    return !isEmpty();
  }

  /**
   * Whether the container is empty.
   *
   * @return true if container is empty and false otherwise
   */
  public boolean isEmpty() {
    return reasonOfEmptiness != null;
  }

  /**
   * Maps the value from one to another and returns new container. If container is empty already,
   * returns new empty one with the same reason of emptiness. So, {@code mapper} will not be
   * executed on empty container.
   *
   * @param mapper mapping function
   * @param <U>    the type of the new value
   * @param <E>    the type of the exception that mapper may throw
   * @return a container with the new value or an empty one
   * @throws NullPointerException if mapper is null
   */
  public <U, E extends Exception> Try<U> map(CheckedFunction<? super T, ? extends U, E> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty()) {
      return empty(reasonOfEmptiness);
    }
    return Try.of(() -> mapper.apply(value));
  }

  /**
   * Maps the value from one to another and returns new container. The mapping function must return
   * another {@link Try} container. If calculation does not fail, returns the new container,
   * otherwise returns an empty one. If container is empty already, returns new empty one with the
   * same reason of emptiness. So, {@code mapper} will not be executed on empty container.
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
   * @param <E>    the type of the exception that mapper may throw
   * @return a container with new value or an empty one
   * @throws NullPointerException if mapper is null
   * @see Try#map(CheckedFunction)
   */
  @SuppressWarnings("unchecked")
  public <U, E extends Exception> Try<U> flatMap(
      CheckedFunction<? super T, ? extends Try<? extends U>, E> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty()) {
      return empty(reasonOfEmptiness);
    }
    try {
      return (Try<U>) mapper.apply(value);
    } catch (Exception e) {
      return empty(e);
    }
  }

  /**
   * If container is empty or predicate's test returns false, returns {@link Try#empty(Exception)},
   * otherwise returns the container itself.
   *
   * @param predicate predicate function
   * @return the container itself or an empty one
   * @throws NullPointerException if predicate is null
   */
  public Try<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate);
    if (isEmpty()) {
      return empty(reasonOfEmptiness);
    } else if (!predicate.test(value)) {
      return empty(new EmptyContainerException(
          String.format("Predicate %s has returned false", predicate)
      ));
    }
    return this;
  }

  /**
   * Gets value from the container.
   *
   * @return the value of the container
   * @throws EmptyContainerException if container is empty. The cause of the exception is the reason
   *                                 of emptiness
   */
  public T get() {
    if (isPresent()) {
      return value;
    }
    throw new EmptyContainerException(CONTAINER_IS_EMPTY_MSG, reasonOfEmptiness);
  }

  /**
   * Gets value from the container if it is not empty. Otherwise returns {@code other}.
   *
   * @param other the default value
   * @return the value of the container if it is not empty, otherwise returns default value
   */
  public T orElse(T other) {
    return isPresent() ? value : other;
  }

  /**
   * If container is not empty, returns itself, otherwise returns the new one with the given
   * supplier.
   *
   * @param supplier supplier for the new container
   * @param <E>      the type of the exception that container may throw
   * @return the container itself or the new one
   * @throws NullPointerException if supplier is null
   */
  public <E extends Exception> Try<T> orElseTry(CheckedSupplier<T, E> supplier) {
    Objects.requireNonNull(supplier);
    return isPresent() ? this : Try.of(supplier);
  }

  /**
   * If container is not empty, returns its value, otherwise returns the result of the given
   * supplier.
   *
   * @param supplier supplier which returns default value
   * @return the value of the container or the default one
   * @throws NullPointerException if supplier is null
   */
  public T orElseGet(Supplier<? extends T> supplier) {
    Objects.requireNonNull(supplier);
    return isPresent() ? value : supplier.get();
  }

  /**
   * If container is not empty, returns itself, otherwise passes the reason of emptiness to the
   * given provider and returns its result.
   *
   * @param reasonOfEmptinessProvider provider which accepts reason of emptiness and returns the
   *                                  default
   * @return the value of the container or the default one
   * @throws NullPointerException if {@code reasonOfEmptiness} is null
   */
  public T orElseGet(Function<? super Exception, ? extends T> reasonOfEmptinessProvider) {
    Objects.requireNonNull(reasonOfEmptinessProvider);
    return isPresent() ? value : reasonOfEmptinessProvider.apply(reasonOfEmptiness);
  }

  /**
   * If container is not empty, returns its value, otherwise throws given exception.
   *
   * @param exceptionSupplier supplier, that returns exception
   * @param <E>               the type of the exception
   * @return the value of the container
   * @throws E                    if container is empty
   * @throws NullPointerException if exceptionSupplier is null
   */
  public <E extends Exception> T orElseThrow(Supplier<? extends E> exceptionSupplier) throws E {
    Objects.requireNonNull(exceptionSupplier);
    if (isPresent()) {
      return value;
    }
    throw exceptionSupplier.get();
  }

  /**
   * If container is not empty, calls consumer with its value.
   *
   * @param consumer consumer that will be called, if container is not empty
   * @throws NullPointerException if consumer is null
   */
  public void ifPresent(Consumer<? super T> consumer) {
    Objects.requireNonNull(consumer);
    if (isPresent()) {
      consumer.accept(value);
    }
  }

  /**
   * If container is empty, provides reason of emptiness.
   *
   * @param reasonOfEmptinessProvider consumer that accepts reason of emptiness
   * @throws NullPointerException if {@code reasonOfEmptiness} is null
   */
  public void ifEmpty(Consumer<? super Exception> reasonOfEmptinessProvider) {
    Objects.requireNonNull(reasonOfEmptinessProvider);
    if (isEmpty()) {
      reasonOfEmptinessProvider.accept(reasonOfEmptiness);
    }
  }

  /**
   * If container is empty, returns reason of emptiness. Otherwise returns {@link
   * Optional#empty()}.
   *
   * @return reason of emptiness
   */
  public Optional<Exception> getReasonOfEmptiness() {
    if (isEmpty()) {
      return Optional.ofNullable(reasonOfEmptiness);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Transforms container to {@linkplain Stream}.
   *
   * @return stream of container's value if it is not empty, otherwise {@link Stream#empty()}
   */
  @Override
  public Stream<T> stream() {
    return isPresent() ? Stream.of(value) : Stream.empty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Try<?> other = (Try<?>) o;
    return Objects.equals(value, other.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
