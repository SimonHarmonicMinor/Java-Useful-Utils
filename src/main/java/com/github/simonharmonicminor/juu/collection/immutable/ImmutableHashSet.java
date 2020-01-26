package com.github.simonharmonicminor.juu.collection.immutable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.simonharmonicminor.juu.collection.immutable.Immutable.listOf;

/**
 * An immutable implementation of java native {@link HashSet}
 *
 * @param <T> the type of the content
 * @see ImmutableSet
 * @see Set
 * @see HashSet
 * @see Serializable
 * @since 1.0
 */
public class ImmutableHashSet<T> implements ImmutableSet<T>, Serializable {
    private final HashSet<T> hashSet;

    public ImmutableHashSet(Iterable<T> iterable) {
        this(iterable, true);
    }

    ImmutableHashSet(Iterable<T> iterable, boolean needsCloning) {
        Objects.requireNonNull(iterable);
        if (iterable instanceof HashSet) {
            this.hashSet = needsCloning ? new HashSet<>((HashSet<T>) iterable) : (HashSet<T>) iterable;
        } else if (iterable instanceof ImmutableHashSet) {
            ImmutableHashSet<T> immutableHashSet = (ImmutableHashSet<T>) iterable;
            this.hashSet = immutableHashSet.hashSet;
        } else {
            hashSet = new HashSet<>();
            for (T element : iterable) {
                hashSet.add(element);
            }
        }
    }

    private static <R> ImmutableSet<R> newImmutableHashSetWithoutCloning(Set<R> set) {
        if (set.isEmpty())
            return Immutable.emptySet();
        return new ImmutableHashSet<>(set, false);
    }

    @Override
    public ImmutableSet<T> concatWith(Iterable<T> iterable) {
        Objects.requireNonNull(iterable);
        HashSet<T> newHashSet = new HashSet<>(this.hashSet);
        for (T t : iterable) {
            newHashSet.add(t);
        }
        return newImmutableHashSetWithoutCloning(newHashSet);
    }

    @Override
    public <R> ImmutableSet<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        HashSet<R> newHashSet = new HashSet<>(size());
        for (T t : this) {
            newHashSet.add(mapper.apply(t));
        }
        return newImmutableHashSetWithoutCloning(newHashSet);
    }

    @Override
    public <R> ImmutableSet<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper) {
        Objects.requireNonNull(mapper);
        HashSet<R> newHashSet = new HashSet<>(size());
        for (T t : this) {
            ImmutableHashSet<R> immutableHashSet = new ImmutableHashSet<>(mapper.apply(t));
            newHashSet.addAll(immutableHashSet.hashSet);
        }
        return newImmutableHashSetWithoutCloning(newHashSet);
    }

    @Override
    public ImmutableSet<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        HashSet<T> newHashSet = new HashSet<>(size());
        for (T t : this) {
            if (predicate.test(t))
                newHashSet.add(t);
        }
        return newImmutableHashSetWithoutCloning(newHashSet);
    }

    @Override
    public int size() {
        return hashSet.size();
    }

    @Override
    public boolean contains(T element) {
        return hashSet.contains(element);
    }

    @Override
    public ImmutableList<T> toList() {
        return listOf(this.hashSet);
    }

    @Override
    public ImmutableSet<T> toSet() {
        return this;
    }

    @Override
    public Stream<T> parallelStream() {
        return this.hashSet.parallelStream();
    }

    @Override
    public Stream<T> stream() {
        return this.hashSet.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return hashSet.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableHashSet<?> that = (ImmutableHashSet<?>) o;
        return hashSet.equals(that.hashSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashSet);
    }
}
