package com.github.simonharmonicminor.juu.collection.immutable;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static com.github.simonharmonicminor.juu.collection.immutable.Immutable.setOf;

/**
 * An immutable implementation of java native {@link ArrayList}.
 *
 * @param <T> the type of the content
 * @see ImmutableList
 * @see List
 * @see ArrayList
 * @see Serializable
 * @since 1.0
 */
public class ImmutableArrayList<T> implements ImmutableList<T>, Serializable {
    private final ArrayList<T> arrayList;

    public ImmutableArrayList(Iterable<T> iterable) {
        this(iterable, true);
    }

    ImmutableArrayList(Iterable<T> iterable, boolean needCloning) {
        Objects.requireNonNull(iterable);
        if (iterable instanceof ArrayList) {
            this.arrayList = needCloning ? new ArrayList<>((ArrayList<T>) iterable) : (ArrayList<T>) iterable;
        } else if (iterable instanceof ImmutableArrayList) {
            ImmutableArrayList<T> immutableArrayList = (ImmutableArrayList<T>) iterable;
            this.arrayList = immutableArrayList.arrayList;
        } else {
            arrayList = new ArrayList<>();
            for (T element : iterable) {
                arrayList.add(element);
            }
        }
    }

    private static <R> ImmutableList<R> newImmutableList(List<R> list, boolean needsCloning) {
        if (list.isEmpty())
            return Immutable.emptyList();
        return new ImmutableArrayList<>(list, needsCloning);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds", index));
        return arrayList.get(index);
    }

    @Override
    public int indexOf(T element) {
        return arrayList.indexOf(element);
    }

    @Override
    public int lastIndexOf(T element) {
        return arrayList.lastIndexOf(element);
    }

    @Override
    public ImmutableList<T> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex)
            return Immutable.emptyList();
        fromIndex = Math.max(0, fromIndex);
        toIndex = Math.min(size() - 1, toIndex);
        return newImmutableList(arrayList.subList(fromIndex, toIndex), true);
    }

    @Override
    public ImmutableList<T> concatWith(Iterable<T> iterable) {
        Objects.requireNonNull(iterable);
        ArrayList<T> copy = new ArrayList<>(this.arrayList);
        for (T t : iterable) {
            copy.add(t);
        }
        return newImmutableList(copy, false);
    }

    private static <R> R getValByIndex(ImmutableList<R> immutableList, int index) {
        if (index < immutableList.size())
            return immutableList.get(index);
        return null;
    }

    @Override
    public <R> ImmutableList<Pair<T, R>> zipWith(ImmutableList<R> list) {
        int maxSize = Math.max(size(), list.size());
        ArrayList<Pair<T, R>> newArrayList = new ArrayList<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            T left = getValByIndex(this, i);
            R right = getValByIndex(list, i);
            newArrayList.add(Pair.of(left, right));
        }
        return newImmutableList(newArrayList, false);
    }

    @Override
    public ImmutableList<Pair<T, T>> zipWithNext() {
        ArrayList<Pair<T, T>> newArrayList = new ArrayList<>(size());
        for (int i = 0; i < size() - 1; i++) {
            newArrayList.add(Pair.of(get(i), get(i + 1)));
        }
        return newImmutableList(newArrayList, false);
    }

    @Override
    public <R> ImmutableList<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        ArrayList<R> newList = new ArrayList<>(arrayList.size());
        for (T t : arrayList) {
            newList.add(mapper.apply(t));
        }
        return newImmutableList(newList, false);
    }

    @Override
    public <R> ImmutableList<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        ArrayList<R> newList = new ArrayList<>(arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            newList.add(mapper.apply(i, arrayList.get(i)));
        }
        return newImmutableList(newList, false);
    }

    @Override
    public <R> ImmutableList<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper) {
        Objects.requireNonNull(mapper);
        ArrayList<R> newList = new ArrayList<>(arrayList.size());
        for (T t : arrayList) {
            ImmutableArrayList<R> listElement = new ImmutableArrayList<>(mapper.apply(t));
            newList.addAll(listElement.arrayList);
        }
        return newImmutableList(newList, false);
    }

    @Override
    public <R> ImmutableList<R> flatMapIndexed(BiFunction<Integer, ? super T, ? extends Iterable<R>> mapper) {
        Objects.requireNonNull(mapper);
        ArrayList<R> newList = new ArrayList<>(arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            ImmutableArrayList<R> listElement =
                    new ImmutableArrayList<>(mapper.apply(i, arrayList.get(i)));
            newList.addAll(listElement.arrayList);
        }
        return newImmutableList(newList, false);
    }

    @Override
    public ImmutableList<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        ArrayList<T> newList = new ArrayList<>(arrayList.size());
        for (T t : arrayList) {
            if (predicate.test(t))
                newList.add(t);
        }
        return newImmutableList(newList, false);
    }

    @Override
    public ImmutableList<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
        Objects.requireNonNull(predicate);
        ArrayList<T> newList = new ArrayList<>(arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            if (predicate.test(i, arrayList.get(i)))
                newList.add(arrayList.get(i));
        }
        return newImmutableList(newList, false);
    }

    @Override
    public void forEachIndexed(BiConsumer<Integer, ? super T> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < arrayList.size(); i++) {
            action.accept(i, arrayList.get(i));
        }
    }

    @Override
    public ImmutableList<T> sorted(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        ArrayList<T> copy = new ArrayList<>(arrayList);
        copy.sort(comparator);
        return newImmutableList(copy, false);
    }

    @Override
    public ImmutableList<T> limit(int size) {
        if (size < 0)
            throw new IllegalArgumentException(
                    String.format("Limit size is less than zero: %s", size)
            );
        ArrayList<T> newList = new ArrayList<>(arrayList.size());
        for (int i = 0; i < size; i++) {
            newList.add(arrayList.get(i));
        }
        return newImmutableList(newList, false);
    }

    @Override
    public ImmutableList<T> skip(int size) {
        if (size < 0)
            throw new IllegalArgumentException(
                    String.format("Skip size is less than zero: %s", size)
            );
        ArrayList<T> newList = new ArrayList<>(arrayList.size());
        for (int i = size; i < arrayList.size(); i++) {
            newList.add(arrayList.get(i));
        }
        return newImmutableList(newList, false);
    }

    @Override
    public ImmutableList<T> reversed() {
        ArrayList<T> newArrayList = new ArrayList<>(size());
        for (int i = size() - 1; i >= 0; i--) {
            newArrayList.add(get(i));
        }
        return newImmutableList(newArrayList, false);
    }

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public boolean contains(T element) {
        return arrayList.contains(element);
    }

    @Override
    public ImmutableList<T> toList() {
        return this;
    }

    @Override
    public ImmutableSet<T> toSet() {
        return setOf(arrayList);
    }

    @Override
    public Stream<T> parallelStream() {
        return arrayList.parallelStream();
    }

    @Override
    public Stream<T> stream() {
        return arrayList.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return arrayList.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableArrayList<?> that = (ImmutableArrayList<?>) o;
        return arrayList.equals(that.arrayList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrayList);
    }
}
