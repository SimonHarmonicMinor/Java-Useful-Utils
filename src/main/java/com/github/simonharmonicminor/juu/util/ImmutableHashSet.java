package com.github.simonharmonicminor.juu.util;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * An immutable implementation of java native {@link HashSet}
 *
 * @param <T> the type of the content
 * @see ImmutableSet
 * @see Set
 * @see HashSet
 * @since 1.0
 */
public class ImmutableHashSet<T> implements ImmutableSet<T> {
    private final HashSet<T> hashSet;

    public ImmutableHashSet(Iterable<T> iterable) {
        this(iterable, true);
    }

    private ImmutableHashSet(Iterable<T> iterable, boolean needsCloning) {
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
            return ImmutableCollections.emptySet();
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
    public boolean isEmpty() {
        return hashSet.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    public boolean contains(T element) {
        return hashSet.contains(element);
    }

    @Override
    public boolean notContains(T element) {
        return !contains(element);
    }

    @Override
    public boolean containsAll(Iterable<T> elements) {
        Objects.requireNonNull(elements);
        for (T t : elements) {
            if (!contains(t))
                return false;
        }
        return true;
    }

    @Override
    public boolean containsAny(Iterable<T> elements) {
        Objects.requireNonNull(elements);
        for (T t : elements) {
            if (contains(t))
                return true;
        }
        return false;
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        for (T t : this) {
            if (!predicate.test(t))
                return false;
        }
        return true;
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        for (T t : this) {
            if (predicate.test(t))
                return true;
        }
        return false;
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        for (T t : this) {
            if (predicate.test(t))
                return false;
        }
        return true;
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        Objects.requireNonNull(accumulator);
        return stream().reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        Objects.requireNonNull(accumulator);
        return stream().reduce(accumulator);
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return stream().min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return stream().max(comparator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        Object[] array = new Object[size()];
        int i = 0;
        for (T t : this) {
            array[i++] = t;
        }
        return (T[]) array;
    }

    @Override
    public ImmutableList<T> toList() {
        return new ImmutableArrayList<>(this.hashSet);
    }

    @Override
    public ImmutableSet<T> toSet() {
        return this;
    }

    @Override
    public List<T> toMutableList() {
        return new ArrayList<>(this.hashSet);
    }

    @Override
    public Set<T> toMutableSet() {
        return new HashSet<>(this.hashSet);
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
}
