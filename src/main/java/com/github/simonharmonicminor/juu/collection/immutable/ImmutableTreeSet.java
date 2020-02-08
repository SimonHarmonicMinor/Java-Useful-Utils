package com.github.simonharmonicminor.juu.collection.immutable;

import com.github.simonharmonicminor.juu.monad.Try;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.github.simonharmonicminor.juu.collection.immutable.Immutable.*;
import static com.github.simonharmonicminor.juu.collection.immutable.ImmutableCollectionUtils.setEquals;
import static com.github.simonharmonicminor.juu.collection.immutable.ImmutableCollectionUtils.setToString;

public class ImmutableTreeSet<T> implements ImmutableNavigableSet<T>, Serializable {
    private final TreeSet<T> treeSet;

    public static <R extends Comparable<R>> ImmutableTreeSet<R> of(Iterable<R> iterable) {
        Objects.requireNonNull(iterable);
        return new ImmutableTreeSet<>(iterable, null);
    }

    public static <R> ImmutableTreeSet<R> ofSortedSet(SortedSet<R> sortedSet) {
        Objects.requireNonNull(sortedSet);
        return new ImmutableTreeSet<>(sortedSet, true);
    }

    public static <R> ImmutableTreeSet<R> of(Iterable<R> iterable, Comparator<R> comparator) {
        Objects.requireNonNull(iterable);
        return new ImmutableTreeSet<>(iterable, comparator);
    }

    ImmutableTreeSet(Iterable<T> iterable, Comparator<T> comparator) {
        Objects.requireNonNull(iterable);
        treeSet = new TreeSet<>(comparator);
        for (T t : iterable) {
            treeSet.add(t);
        }
    }

    ImmutableTreeSet(SortedSet<T> sortedSet, boolean needsCloning) {
        if (sortedSet instanceof TreeSet) {
            this.treeSet = needsCloning ? new TreeSet<>(sortedSet) : (TreeSet<T>) sortedSet;
        } else {
            this.treeSet = new TreeSet<>(sortedSet);
        }
    }

    private static <T> ImmutableTreeSet<T> newImmutableTreeSet(
            SortedSet<T> treeSet, boolean needsCloning) {
        if (treeSet.isEmpty()) return emptyTreeSet();
        return new ImmutableTreeSet<>(treeSet, needsCloning);
    }

    private static <T> ImmutableHashSet<T> newImmutableHashSetWithoutCloning(HashSet<T> hashSet) {
        if (hashSet.isEmpty()) return emptyHashSet();
        return new ImmutableHashSet<>(hashSet, false);
    }

    private Optional<T> tryGetElement(Supplier<T> supplier) {
        return Try.of(supplier::get)
                .map(Optional::ofNullable)
                .orElse(Optional.empty());
    }

    @Override
    public Optional<T> lower(T t) {
        return tryGetElement(() -> treeSet.lower(t));
    }

    @Override
    public Optional<T> floor(T t) {
        return tryGetElement(() -> treeSet.floor(t));
    }

    @Override
    public Optional<T> ceiling(T t) {
        return tryGetElement(() -> treeSet.ceiling(t));
    }

    @Override
    public Optional<T> higher(T t) {
        return tryGetElement(() -> treeSet.higher(t));
    }

    @Override
    public ImmutableNavigableSet<T> reversedOrderSet() {
        return newImmutableTreeSet(treeSet.descendingSet(), true);
    }

    @Override
    public Iterator<T> reversedOrderIterator() {
        return treeSet.descendingIterator();
    }

    private ImmutableNavigableSet<T> tryGetSubSet(Supplier<ImmutableNavigableSet<T>> supplier) {
        return Try.of(supplier::get)
                .orElse(emptyTreeSet());
    }

    @Override
    public ImmutableNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        return tryGetSubSet(() ->
                newImmutableTreeSet(
                        treeSet.subSet(fromElement, fromInclusive, toElement, toInclusive),
                        true));
    }

    @Override
    public ImmutableNavigableSet<T> headSet(T toElement, boolean inclusive) {
        return tryGetSubSet(() ->
                newImmutableTreeSet(
                        treeSet.headSet(toElement, inclusive),
                        true));
    }

    @Override
    public ImmutableNavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        return tryGetSubSet(() ->
                newImmutableTreeSet(
                        treeSet.tailSet(fromElement, inclusive),
                        true));
    }

    @Override
    public NavigableSet<T> toMutableNavigableSet() {
        return new TreeSet<>(treeSet);
    }

    @Override
    public Comparator<? super T> comparator() {
        return treeSet.comparator();
    }

    @Override
    public ImmutableSortedSet<T> subSet(T fromElement, T toElement) {
        return tryGetSubSet(() ->
                newImmutableTreeSet(
                        treeSet.subSet(fromElement, toElement),
                        true));
    }

    @Override
    public ImmutableSortedSet<T> headSet(T toElement) {
        return tryGetSubSet(() ->
                newImmutableTreeSet(
                        treeSet.headSet(toElement),
                        true));
    }

    @Override
    public ImmutableSortedSet<T> tailSet(T fromElement) {
        return tryGetSubSet(() ->
                newImmutableTreeSet(
                        treeSet.tailSet(fromElement),
                        true));
    }

    @Override
    public Optional<T> first() {
        return tryGetElement(treeSet::first);
    }

    @Override
    public Optional<T> last() {
        return tryGetElement(treeSet::last);
    }

    @Override
    public SortedSet<T> toMutableSortedSet() {
        return toMutableNavigableSet();
    }

    @Override
    public ImmutableSet<T> concatWith(Iterable<T> iterable) {
        TreeSet<T> newTreeSet = new TreeSet<>(treeSet);
        for (T t : iterable) {
            newTreeSet.add(t);
        }
        return newImmutableTreeSet(newTreeSet, false);
    }

    @Override
    public <R> ImmutableSet<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        HashSet<R> hashSet = new HashSet<>(size());
        for (T t : this) {
            hashSet.add(mapper.apply(t));
        }
        return newImmutableHashSetWithoutCloning(hashSet);
    }

    @Override
    public <R> ImmutableSet<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper) {
        Objects.requireNonNull(mapper);
        HashSet<R> hashSet = new HashSet<>();
        for (T t : this) {
            for (R r : mapper.apply(t)) {
                hashSet.add(r);
            }
        }
        return newImmutableHashSetWithoutCloning(hashSet);
    }

    @Override
    public ImmutableSet<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        TreeSet<T> newTreeSet = new TreeSet<>(comparator());
        for (T t : this) {
            if (predicate.test(t)) newTreeSet.add(t);
        }
        return newImmutableTreeSet(newTreeSet, false);
    }

    @Override
    public int size() {
        return treeSet.size();
    }

    @Override
    public boolean contains(Object element) {
        return treeSet.contains(element);
    }

    @Override
    public ImmutableList<T> toList() {
        return listOf(treeSet);
    }

    @Override
    public ImmutableSet<T> toSet() {
        return this;
    }

    @Override
    public Stream<T> parallelStream() {
        return treeSet.parallelStream();
    }

    @Override
    public Stream<T> stream() {
        return treeSet.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return treeSet.iterator();
    }

    @Override
    public boolean equals(Object o) {
        return setEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treeSet);
    }

    @Override
    public String toString() {
        return setToString(this);
    }
}
