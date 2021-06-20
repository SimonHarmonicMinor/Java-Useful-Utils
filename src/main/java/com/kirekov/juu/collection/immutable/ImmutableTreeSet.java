package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.collection.immutable.abstraction.AbstractImmutableSet;
import com.kirekov.juu.monad.Try;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * An immutable implementation of java native {@linkplain TreeSet}.
 *
 * @param <T> the type of the value
 * @see ImmutableNavigableSet
 * @see Set
 * @see TreeSet
 */
public final class ImmutableTreeSet<T> extends AbstractImmutableSet<T> implements ImmutableNavigableSet<T> {

  private final TreeSet<T> treeSet;

  public static <R extends Comparable<R>> ImmutableTreeSet<R> of(Iterable<R> iterable) {
    Objects.requireNonNull(iterable);
    return new ImmutableTreeSet<>(iterable, null);
  }

  public static <R> ImmutableTreeSet<R> of(Iterable<R> iterable, Comparator<R> comparator) {
    Objects.requireNonNull(iterable);
    return new ImmutableTreeSet<>(iterable, comparator);
  }

  public static <R> ImmutableTreeSet<R> ofSortedSet(SortedSet<R> sortedSet) {
    Objects.requireNonNull(sortedSet);
    return new ImmutableTreeSet<>(sortedSet);
  }

  ImmutableTreeSet(Iterable<T> iterable, Comparator<? super T> comparator) {
    Objects.requireNonNull(iterable);
    treeSet = new TreeSet<>(comparator);
    for (T t : iterable) {
      treeSet.add(t);
    }
  }

  ImmutableTreeSet(SortedSet<T> sortedSet) {
    this.treeSet = new TreeSet<>(sortedSet);
  }

  @Override
  public Optional<T> lower(T t) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeSet.lower(t));
  }

  @Override
  public Optional<T> floor(T t) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeSet.floor(t));
  }

  @Override
  public Optional<T> ceiling(T t) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeSet.ceiling(t));
  }

  @Override
  public Optional<T> higher(T t) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeSet.higher(t));
  }

  @Override
  public ImmutableNavigableSet<T> reversedOrderSet() {
    return new ImmutableTreeSet<>(treeSet.descendingSet());
  }

  @Override
  public Iterator<T> reversedOrderIterator() {
    return new UnmodifiableIterator<>(treeSet.descendingIterator());
  }

  @Override
  public ImmutableSortedSet<T> subSet(T fromElement, T toElement) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            treeSet.subSet(fromElement, toElement)
        ));
  }

  @Override
  public ImmutableNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement,
      boolean toInclusive) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            treeSet.subSet(fromElement, fromInclusive, toElement, toInclusive)
        ));
  }

  @Override
  public ImmutableSortedSet<T> headSet(T toElement) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            treeSet.headSet(toElement)
        ));
  }

  @Override
  public ImmutableNavigableSet<T> headSet(T toElement, boolean inclusive) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            treeSet.headSet(toElement, inclusive)
        ));
  }

  @Override
  public ImmutableSortedSet<T> tailSet(T fromElement) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            treeSet.tailSet(fromElement)
        ));
  }

  @Override
  public ImmutableNavigableSet<T> tailSet(T fromElement, boolean inclusive) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            treeSet.tailSet(fromElement, inclusive)
        ));
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
  public Optional<T> first() {
    return ImmutableCollectionUtils.tryGetElement(treeSet::first);
  }

  @Override
  public Optional<T> last() {
    return ImmutableCollectionUtils.tryGetElement(treeSet::last);
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
    return new ImmutableTreeSet<>(newTreeSet);
  }

  @Override
  public <R> ImmutableSet<R> map(Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(mapper);
    HashSet<R> hashSet = new HashSet<>(size());
    for (T t : this) {
      hashSet.add(mapper.apply(t));
    }
    return new ImmutableHashSet<>(hashSet);
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
    return new ImmutableHashSet<>(hashSet);
  }

  @Override
  public ImmutableSet<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate);
    TreeSet<T> newTreeSet = new TreeSet<>(comparator());
    for (T t : this) {
      if (predicate.test(t)) {
        newTreeSet.add(t);
      }
    }
    return new ImmutableTreeSet<>(newTreeSet);
  }

  @Override
  public int size() {
    return treeSet.size();
  }

  @Override
  public boolean contains(Object element) {
    return Try.of(() -> treeSet.contains(element))
        .orElse(false);
  }

  @Override
  public ImmutableList<T> toList() {
    return Immutable.listOf(treeSet);
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
    return new UnmodifiableIterator<>(treeSet.iterator());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImmutableTreeSet<?> that = (ImmutableTreeSet<?>) o;
    return treeSet.equals(that.treeSet);
  }

  @Override
  public int hashCode() {
    return treeSet.hashCode();
  }

  private ImmutableNavigableSet<T> tryGetSubSet(Supplier<ImmutableNavigableSet<T>> supplier) {
    return Try.of(supplier::get)
        .orElse(new ImmutableTreeSet<>(Immutable.emptyList(), treeSet.comparator()));
  }
}
