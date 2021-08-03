package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.collection.immutable.abstraction.AbstractImmutableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
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
public final class ImmutableArrayList<T> extends AbstractImmutableList<T> {

  private final List<T> arrayList;

  /**
   * Constructor.
   *
   * @param iterable the source of elements
   */
  public ImmutableArrayList(Iterable<T> iterable) {
    super();
    arrayList = new ArrayList<>();
    for (final T element : iterable) {
      arrayList.add(element);
    }
  }

  @Override
  public T get(int index) {
    final int normalized = normalizeIndex(index);
    checkIndex(normalized);
    return arrayList.get(normalized);
  }

  @Override
  public OptionalInt indexOf(T element) {
    final int index = arrayList.indexOf(element);
    if (index == -1) {
      return OptionalInt.empty();
    }
    return OptionalInt.of(index);
  }

  @Override
  public OptionalInt lastIndexOf(T element) {
    final int index = arrayList.lastIndexOf(element);
    if (index == -1) {
      return OptionalInt.empty();
    }
    return OptionalInt.of(index);
  }

  @Override
  public ImmutableList<T> slice(int fromIndex) {
    return slice(fromIndex, size(), 1);
  }

  @Override
  public ImmutableList<T> slice(int fromIndex, int toIndex) {
    final int fromNorm = normalizeIndex(fromIndex);
    final int toNorm = normalizeIndex(toIndex);
    return slice(fromNorm, toNorm, fromNorm < toNorm ? 1 : -1);
  }

  @Override
  public ImmutableList<T> slice(int fromIndex, int toIndex, int stepSize) {
    int fromNorm = normalizeIndex(fromIndex);
    final int toNorm = normalizeIndex(toIndex);
    checkIndex(fromNorm);
    checkStepSize(stepSize);
    final BiFunction<Integer, Integer, Boolean> condition =
        fromNorm <= toNorm
            ? (from, to) -> from < to && from < size()
            : (from, to) -> from > to && from >= 0;
    final Function<Integer, Integer> nextValueFunc = index -> index + stepSize;
    final ArrayList<T> newArrayList = new ArrayList<>();
    while (condition.apply(fromNorm, toNorm)) {
      newArrayList.add(get(fromNorm));
      fromNorm = nextValueFunc.apply(fromNorm);
    }
    return new ImmutableArrayList<>(newArrayList);
  }

  @Override
  public ImmutableList<T> step(int fromIndex, int stepSize) {
    checkStepSize(stepSize);
    checkIndex(normalizeIndex(fromIndex));
    if (stepSize > 0) {
      return slice(fromIndex, size(), stepSize);
    } else {
      return slice(fromIndex, -size() - 1, stepSize);
    }
  }

  @Override
  public ImmutableList<T> concatWith(Iterable<T> iterable) {
    Objects.requireNonNull(iterable, "iterable to concat with cannot be null");
    final ArrayList<T> copy = new ArrayList<>(this.arrayList);
    for (final T t : iterable) {
      copy.add(t);
    }
    return new ImmutableArrayList<>(copy);
  }

  @Override
  public <R> ImmutableList<Pair<T, R>> zipWith(ImmutableList<R> list) {
    Objects.requireNonNull(list, "list to zip with cannot be null");
    final int maxSize = Math.max(size(), list.size());
    final ArrayList<Pair<T, R>> newArrayList = new ArrayList<>(maxSize);
    for (int i = 0; i < maxSize; i++) {
      final T left = getValByIndex(this, i);
      final R right = getValByIndex(list, i);
      newArrayList.add(Pair.of(left, right));
    }
    return new ImmutableArrayList<>(newArrayList);
  }

  @Override
  public ImmutableList<Pair<T, T>> zipWithNext() {
    final ArrayList<Pair<T, T>> newArrayList = new ArrayList<>(size());
    for (int i = 0; i < size() - 1; i++) {
      newArrayList.add(Pair.of(get(i), get(i + 1)));
    }
    return new ImmutableArrayList<>(newArrayList);
  }

  @Override
  public <R> ImmutableList<R> map(Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(mapper, "mapper function cannot be null");
    final ArrayList<R> newList = new ArrayList<>(arrayList.size());
    for (final T t : arrayList) {
      newList.add(mapper.apply(t));
    }
    return new ImmutableArrayList<>(newList);
  }

  @Override
  public <R> ImmutableList<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper) {
    Objects.requireNonNull(mapper, "indexed mapper function cannot be null");
    final ArrayList<R> newList = new ArrayList<>(arrayList.size());
    for (int i = 0; i < arrayList.size(); i++) {
      newList.add(mapper.apply(i, arrayList.get(i)));
    }
    return new ImmutableArrayList<>(newList);
  }

  @Override
  public <R> ImmutableList<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper) {
    Objects.requireNonNull(mapper, "flat mapper function cannot be null");
    final ArrayList<R> newList = new ArrayList<>(arrayList.size());
    for (final T t : arrayList) {
      final ImmutableArrayList<R> listElement = new ImmutableArrayList<>(mapper.apply(t));
      newList.addAll(listElement.arrayList);
    }
    return new ImmutableArrayList<>(newList);
  }

  @Override
  public <R> ImmutableList<R> flatMapIndexed(
      BiFunction<Integer, ? super T, ? extends Iterable<R>> mapper
  ) {
    Objects.requireNonNull(mapper, "indexed flat mapper function cannot be null");
    final ArrayList<R> newList = new ArrayList<>(arrayList.size());
    for (int i = 0; i < arrayList.size(); i++) {
      final ImmutableArrayList<R> listElement =
          new ImmutableArrayList<>(mapper.apply(i, arrayList.get(i)));
      newList.addAll(listElement.arrayList);
    }
    return new ImmutableArrayList<>(newList);
  }

  @Override
  public ImmutableList<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "filtering predicate cannot be null");
    final ArrayList<T> newList = new ArrayList<>(arrayList.size());
    for (final T t : arrayList) {
      if (predicate.test(t)) {
        newList.add(t);
      }
    }
    return new ImmutableArrayList<>(newList);
  }

  @Override
  public ImmutableList<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
    Objects.requireNonNull(predicate, "indexed filtering predicate cannot be null");
    final ArrayList<T> newList = new ArrayList<>(arrayList.size());
    for (int i = 0; i < arrayList.size(); i++) {
      if (predicate.test(i, arrayList.get(i))) {
        newList.add(arrayList.get(i));
      }
    }
    return new ImmutableArrayList<>(newList);
  }

  @Override
  public void forEachIndexed(BiConsumer<Integer, ? super T> action) {
    Objects.requireNonNull(action, "indexed for-each consumer cannot be null");
    for (int i = 0; i < arrayList.size(); i++) {
      action.accept(i, arrayList.get(i));
    }
  }

  @Override
  public ImmutableList<T> sorted(Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator, "sorting comparator cannot be null");
    final ArrayList<T> copy = new ArrayList<>(arrayList);
    copy.sort(comparator);
    return new ImmutableArrayList<>(copy);
  }

  @Override
  public ImmutableList<T> limit(int size) {
    if (size < 0) {
      throw new IllegalArgumentException(String.format("Limit size is less than zero: %s", size));
    }
    final ArrayList<T> newList = new ArrayList<>(arrayList.size());
    for (int i = 0; i < Math.min(size(), size); i++) {
      newList.add(arrayList.get(i));
    }
    return new ImmutableArrayList<>(newList);
  }

  @Override
  public ImmutableList<T> skip(int size) {
    if (size < 0) {
      throw new IllegalArgumentException(String.format("Skip size is less than zero: %s", size));
    }
    final ArrayList<T> newList = new ArrayList<>(arrayList.size());
    for (int i = Math.min(size, size()); i < arrayList.size(); i++) {
      newList.add(arrayList.get(i));
    }
    return new ImmutableArrayList<>(newList);
  }

  @Override
  public int size() {
    return arrayList.size();
  }

  @Override
  public boolean contains(Object element) {
    return arrayList.contains(element);
  }

  @Override
  public ImmutableList<T> toList() {
    return this;
  }

  @Override
  public ImmutableSet<T> toSet() {
    return Immutable.setOf(arrayList);
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
    return new UnmodifiableIterator<>(arrayList.iterator());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ImmutableArrayList<?> that = (ImmutableArrayList<?>) o;
    return arrayList.equals(that.arrayList);
  }

  @Override
  public int hashCode() {
    return arrayList.hashCode();
  }

  private int normalizeIndex(int index) {
    return index >= 0 ? index : size() + index;
  }

  private void checkStepSize(int stepSize) {
    if (stepSize == 0) {
      throw new IllegalArgumentException("Step size cannot be zero");
    }
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds", index));
    }
  }

  private static <R> R getValByIndex(ImmutableList<R> immutableList, int index) {
    if (index < immutableList.size()) {
      return immutableList.get(index);
    }
    return null;
  }
}
