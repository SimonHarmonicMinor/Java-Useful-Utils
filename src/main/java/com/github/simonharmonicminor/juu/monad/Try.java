package com.github.simonharmonicminor.juu.monad;

import com.github.simonharmonicminor.juu.collection.Streaming;
import com.github.simonharmonicminor.juu.lambda.Action;
import com.github.simonharmonicminor.juu.lambda.CheckedFunction;
import com.github.simonharmonicminor.juu.lambda.CheckedSupplier;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Monad for retrieving values just like {@link java.util.Optional}, but instead a container
 * considered as an empty one if an exception has been thrown during calculations.
 * <p>
 * <br/>
 * Class overrides {@link Object#equals(Object)} and {@link Object#hashCode()} methods.
 *
 * @param <T> the type of the return value
 * @since 1.0
 */
public class Try<T> implements Streaming<T> {
    private static final Try<?> EMPTY = new Try<>(null, true);

    private final T value;
    private final boolean exceptionHasBeenThrown;

    private Try(T value) {
        this.value = value;
        this.exceptionHasBeenThrown = false;
    }

    private Try(T value, boolean exceptionHasBeenThrown) {
        this.value = value;
        this.exceptionHasBeenThrown = exceptionHasBeenThrown;
    }

    /**
     * Instantiates a container by calculating the value of the supplier.
     * If supplier fails with an exception, returns {@link Try#empty()}
     *
     * @param supplier function that returns value
     * @param <T>      the type of the return value
     * @param <E>      the type of the exception that supplier may throw
     * @return a container with the retrieved value or an empty one, if calculation failed with an exception
     * @throws NullPointerException if suppliers parameter is null
     */
    public static <T, E extends Throwable> Try<T> of(CheckedSupplier<T, E> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return new Try<>(supplier.get());
        } catch (Throwable e) {
            return empty();
        }
    }

    /**
     * Returns a container with the value of the first calculated supplier
     * that didn't fail. If all suppliers fail, returns {@link Try#empty()}
     *
     * @param suppliers collection of suppliers
     * @param <T>       the type of the return value
     * @param <E>       the type of the exception that supplier may throw
     * @return a container with the value of the succeeded supplier or {@link Try#empty()}
     * @throws NullPointerException if suppliers parameter is null
     * @see Try#of(CheckedSupplier)
     */
    public static <T, E extends Throwable> Try<T> getFirst(Iterable<CheckedSupplier<T, E>> suppliers) {
        Objects.requireNonNull(suppliers);
        for (CheckedSupplier<T, E> supplier : suppliers) {
            Try<T> t = Try.of(supplier);
            if (t.isPresent())
                return t;
        }
        return empty();
    }

    /**
     * Proxy method for {@link Try#getFirst(Iterable)}
     *
     * @throws NullPointerException if suppliers parameter is null
     */
    @SafeVarargs
    public static <T, E extends Throwable> Try<T> getFirst(CheckedSupplier<T, E>... suppliers) {
        Objects.requireNonNull(suppliers);
        return getFirst(Arrays.stream(suppliers).collect(Collectors.toList()));
    }

    /**
     * Returns the empty container. Does not create the new one, returns the same instance every time
     *
     * @param <T> the type of the return value
     * @return empty container
     */
    public static <T> Try<T> empty() {
        @SuppressWarnings("unchecked")
        Try<T> t = (Try<T>) EMPTY;
        return t;
    }

    /**
     * @return <code>true</code> if value is present, otherwise <code>false</code>
     */
    public boolean isPresent() {
        return !isEmpty();
    }

    /**
     * @return <code>true</code> if container is empty, otherwise <code>false</code>
     */
    public boolean isEmpty() {
        return exceptionHasBeenThrown;
    }

    /**
     * Maps the value from one to another and returns new container.
     * If mapper throws an exception, returns {@link Try#empty()}
     *
     * @param mapper mapping function
     * @param <U>    the type of the new value
     * @param <E>    the type of the exception that mapper may throw
     * @return a container with the new value or an empty one
     */
    public <U, E extends Throwable> Try<U> map(CheckedFunction<? super T, ? extends U, E> mapper) {
        return Try.of(() -> mapper.apply(value));
    }

    /**
     * Maps the value from one to another and returns new container.
     * The mapping function must return another {@link Try} container.
     * If calculation does not fail, returns the new container, otherwise returns an empty one.
     * <br/>
     * <br/>
     * For instance,
     * <br />
     * <br />
     * <code>
     * Try.of(() -> 1) <br/>
     * &nbsp;&nbsp;&nbsp;.flatMap(v -> Try.of(() -> v + 1)) <br/>
     * &nbsp;&nbsp;&nbsp;.orElse(-1)
     * </code>
     * <br />
     * <br />
     * returns 2, while
     * <br/>
     * <br/>
     * <code>
     * Try.of(() -> 1) <br/>
     * &nbsp;&nbsp;&nbsp;.flatMap(v -> Try.of(() -> v / 0)) <br/>
     * &nbsp;&nbsp;&nbsp;.orElse(-1)
     * </code>
     * <br/>
     * <br/>
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
    public <U, E extends Throwable> Try<U> flatMap(CheckedFunction<? super T, ? extends Try<? extends U>, E> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return (Try<U>) mapper.apply(value);
        } catch (Throwable e) {
            return empty();
        }
    }

    /**
     * If container is empty or predicate's test returns false, returns {@link Try#empty()},
     * otherwise returns the container itself
     *
     * @param predicate predicate function
     * @return the container itself or an empty one
     * @throws NullPointerException if predicate is null
     */
    public Try<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (isEmpty() || !predicate.test(value)) {
            return empty();
        }
        return this;
    }

    /**
     * @return the value of the container
     * @throws NoSuchElementException if container is empty
     */
    public T get() {
        if (isPresent())
            return value;
        throw new NoSuchElementException("Container is empty");
    }

    /**
     * @param other the default value
     * @return the value of the container if it is not empty, otherwise returns default value
     */
    public T orElse(T other) {
        return isPresent() ? value : other;
    }

    /**
     * If container is not empty, returns itself, otherwise returns the new one
     * with the given supplier
     *
     * @param supplier supplier for the new container
     * @param <E>      the type of the exception that container may throw
     * @return the container itself or the new one
     */
    public <E extends Throwable> Try<T> orElseTry(CheckedSupplier<T, E> supplier) {
        return isPresent() ? this : Try.of(supplier);
    }

    /**
     * If container is not empty, returns its value, otherwise returns the result
     * of the given supplier
     *
     * @param supplier supplier which returns default value
     * @return the value of the container or the default one
     */
    public T orElseGet(Supplier<? extends T> supplier) {
        return isPresent() ? value : supplier.get();
    }

    /**
     * If container is not empty, returns its value, otherwise throws given exception
     *
     * @param exceptionSupplier supplier, that returns exception
     * @param <E>               the type of the exception
     * @return the value of the container
     * @throws E                    if container is empty
     * @throws NullPointerException if exceptionSupplier is null
     */
    public <E extends Throwable> T orElseThrow(Supplier<? extends E> exceptionSupplier) throws E {
        Objects.requireNonNull(exceptionSupplier);
        if (isPresent())
            return value;
        throw exceptionSupplier.get();
    }

    /**
     * If container is not empty, calls consumer with its value
     *
     * @param consumer consumer that will be called, if container is not empty
     * @throws NullPointerException if consumer is null
     */
    public void ifPresent(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        if (isPresent())
            consumer.accept(value);
    }

    /**
     * If container is empty, executes the action
     *
     * @param action action that will be executed, if container is empty
     * @throws NullPointerException if action is null
     */
    public void ifEmpty(Action action) {
        Objects.requireNonNull(action);
        if (isEmpty())
            action.execute();
    }

    /**
     * @return stream of container's value if it is not empty, otherwise {@link Stream#empty()}
     */
    @Override
    public Stream<T> stream() {
        return isPresent() ? Stream.of(value) : Stream.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Try<?> aTry = (Try<?>) o;
        return exceptionHasBeenThrown == aTry.exceptionHasBeenThrown &&
                value.equals(aTry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, exceptionHasBeenThrown);
    }
}
