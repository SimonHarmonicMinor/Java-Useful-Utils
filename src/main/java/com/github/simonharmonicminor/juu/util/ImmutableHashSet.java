package com.github.simonharmonicminor.juu.util;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ImmutableHashSet<T> implements ImmutableSet<T> {
    private final HashSet<T> hashSet;

    public ImmutableHashSet(Iterable<T> iterable) {
        Objects.requireNonNull(iterable);
        if (iterable instanceof HashSet) {
            this.hashSet = (HashSet<T>) iterable;
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

    @Override
    public ImmutableSet<T> concatWith(Iterable<T> iterable) {
        return null;
    }

    @Override
    public <R> ImmutableSet<R> map(Function<? super T, ? extends R> mapper) {
        return null;
    }

    @Override
    public <R> ImmutableSet<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper) {
        return null;
    }

    @Override
    public ImmutableSet<T> filter(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isNotEmpty() {
        return false;
    }

    @Override
    public boolean contains(T element) {
        return false;
    }

    @Override
    public boolean notContains(T element) {
        return false;
    }

    @Override
    public boolean containsAll(Iterable<T> elements) {
        return false;
    }

    @Override
    public boolean containsAny(Iterable<T> elements) {
        return false;
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return null;
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return Optional.empty();
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return Optional.empty();
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return Optional.empty();
    }

    @Override
    public T[] toArray() {
        return null;
    }

    @Override
    public ImmutableList<T> toList() {
        return null;
    }

    @Override
    public ImmutableSet<T> toSet() {
        return null;
    }

    @Override
    public List<T> toMutableList() {
        return null;
    }

    @Override
    public Set<T> toMutableSet() {
        return null;
    }

    @Override
    public Stream<T> parallelStream() {
        return null;
    }

    @Override
    public Stream<T> stream() {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
