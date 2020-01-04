package com.github.simonharmonicminor.juu.util;

import com.github.simonharmonicminor.juu.lambda.TriFunction;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * An immutable implementation of java native {@link ArrayList}.
 *
 * @param <T> the type of the content
 * @see ImmutableList
 * @see List
 * @see ArrayList
 * @since 1.0
 */
public class ImmutableArrayList<T> implements ImmutableList<T> {
    private final ArrayList<T> arrayList;

    public ImmutableArrayList(Iterable<T> iterable) {
        this(iterable, true);
    }

    private ImmutableArrayList(Iterable<T> iterable, boolean needCloning) {
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

    private static <R> ImmutableList<R> newImmutableListWithoutCloning(List<R> list) {
        if (list.isEmpty())
            return ImmutableCollections.emptyList();
        return new ImmutableArrayList<>(list, false);
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
    public ListIterator<T> listIterator() {
        return arrayList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return arrayList.listIterator(index);
    }

    @Override
    public ImmutableList<T> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex)
            return ImmutableCollections.emptyList();
        fromIndex = Math.max(0, fromIndex);
        toIndex = Math.min(size() - 1, toIndex);
        return newImmutableListWithoutCloning(arrayList.subList(fromIndex, toIndex));
    }

    @Override
    public ImmutableList<T> concatWith(Iterable<T> iterable) {
        Objects.requireNonNull(iterable);
        ArrayList<T> copy = new ArrayList<>(this.arrayList);
        for (T t : iterable) {
            copy.add(t);
        }
        return newImmutableListWithoutCloning(copy);
    }

    private static <R> R getValByIndex(ImmutableList<R> immutableList, int index) {
        if (index < immutableList.size())
            return immutableList.get(index);
        return null;
    }

    @Override
    public <R, U> ImmutableList<R> mergeWith(ImmutableList<U> list, BiFunction<T, U, R> mergingFunction) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(mergingFunction);
        int i = 0;
        ArrayList<R> newList = new ArrayList<>(Math.max(size(), list.size()));
        while (i < size() || i < list.size()) {
            T curr = getValByIndex(this, i);
            U other = getValByIndex(list, i);
            newList.add(mergingFunction.apply(curr, other));
            i++;
        }
        return newImmutableListWithoutCloning(newList);
    }

    @Override
    public <R, U> ImmutableList<R> mergeWithIndexed(ImmutableList<U> list, TriFunction<Integer, T, U, R> mergingFunction) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(mergingFunction);
        int i = 0;
        ArrayList<R> newList = new ArrayList<>(Math.max(size(), list.size()));
        while (i < size() || i < list.size()) {
            T curr = getValByIndex(this, i);
            U other = getValByIndex(list, i);
            newList.add(mergingFunction.apply(i, curr, other));
            i++;
        }
        return newImmutableListWithoutCloning(newList);
    }

    @Override
    public <R> ImmutableList<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        ArrayList<R> newList = new ArrayList<>(arrayList.size());
        for (T t : arrayList) {
            newList.add(mapper.apply(t));
        }
        return newImmutableListWithoutCloning(newList);
    }

    @Override
    public <R> ImmutableList<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        ArrayList<R> newList = new ArrayList<>(arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            newList.add(mapper.apply(i, arrayList.get(i)));
        }
        return newImmutableListWithoutCloning(newList);
    }

    @Override
    public <R> ImmutableList<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper) {
        Objects.requireNonNull(mapper);
        ArrayList<R> newList = new ArrayList<>(arrayList.size());
        for (T t : arrayList) {
            ImmutableArrayList<R> listElement = new ImmutableArrayList<>(mapper.apply(t));
            newList.addAll(listElement.arrayList);
        }
        return newImmutableListWithoutCloning(newList);
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
        return newImmutableListWithoutCloning(newList);
    }

    @Override
    public ImmutableList<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        ArrayList<T> newList = new ArrayList<>(arrayList.size());
        for (T t : arrayList) {
            if (predicate.test(t))
                newList.add(t);
        }
        return newImmutableListWithoutCloning(newList);
    }

    @Override
    public ImmutableList<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
        Objects.requireNonNull(predicate);
        ArrayList<T> newList = new ArrayList<>(arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            if (predicate.test(i, arrayList.get(i)))
                newList.add(arrayList.get(i));
        }
        return newImmutableListWithoutCloning(newList);
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
        return newImmutableListWithoutCloning(copy);
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
        return newImmutableListWithoutCloning(newList);
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
        return newImmutableListWithoutCloning(newList);
    }

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    public boolean contains(T element) {
        return arrayList.contains(element);
    }

    @Override
    public boolean notContains(T element) {
        return !contains(element);
    }

    @Override
    public boolean containsAll(Iterable<T> elements) {
        Objects.requireNonNull(elements);
        ImmutableArrayList<T> immutableList = new ImmutableArrayList<>(elements);
        return arrayList.containsAll(immutableList.arrayList);
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
        for (T t : arrayList) {
            if (!predicate.test(t))
                return false;
        }
        return true;
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        for (T t : arrayList) {
            if (predicate.test(t))
                return true;
        }
        return false;
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        for (T t : arrayList) {
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
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return (T[]) array;
    }

    @Override
    public ImmutableList<T> toList() {
        return this;
    }

    @Override
    public ImmutableSet<T> toSet() {
        return new ImmutableHashSet<>(arrayList);
    }

    @Override
    public List<T> toMutableList() {
        return new ArrayList<>(this.arrayList);
    }

    @Override
    public Set<T> toMutableSet() {
        return new HashSet<>(arrayList);
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
}
