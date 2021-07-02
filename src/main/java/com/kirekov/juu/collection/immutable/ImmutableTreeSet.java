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
import java.util.Arrays;

/**
 * An immutable implementation of java native {@linkplain TreeSet}.
 *
 * @param <T> the type of the value
 * @see ImmutableNavigableSet
 * @see Set
 * @see TreeSet
 */
public final class ImmutableTreeSet<T>
    extends AbstractImmutableSet<T> implements ImmutableNavigableSet<T> {

  private final NavigableSet<T> navigableSet;

  /**
   * Creates new {@linkplain ImmutableTreeSet}.
   *
   * @param iterable source of elements
   * @param <R>      the type of the element
   * @return new {@linkplain ImmutableTreeSet}
   * @throws NullPointerException if {@code iterable} is null
   */
  public static <R extends Comparable<R>> ImmutableTreeSet<R> of(Iterable<R> iterable) {
    Objects.requireNonNull(
        iterable,
        "iterable to create ImmutableTreeSet cannot be null"
    );
    return new ImmutableTreeSet<>(iterable, null);
  }

  /**
   * Creates new {@linkplain ImmutableTreeSet}.
   *
   * @param iterable   source of elements
   * @param comparator element's comparator. Might be null
   * @param <R>        the type of the element
   * @return new {@linkplain ImmutableTreeSet}
   * @throws NullPointerException if {@code iterable} is null
   */
  public static <R> ImmutableTreeSet<R> of(Iterable<R> iterable, Comparator<R> comparator) {
    Objects.requireNonNull(
        iterable,
        "iterable to create ImmutableTreeSet cannot be null"
    );
    return new ImmutableTreeSet<>(iterable, comparator);
  }

  /**
   * Creates new {@linkplain ImmutableTreeSet}.
   *
   * @param sortedSet source of elements
   * @param <R>       the type of the element
   * @return new {@linkplain ImmutableTreeSet}
   * @throws NullPointerException if {@code sortedSet} is null
   */
  public static <R> ImmutableTreeSet<R> ofSortedSet(SortedSet<R> sortedSet) {
    Objects.requireNonNull(
        sortedSet,
        "sortedSet to create ImmutableTreeSet cannot be null"
    );
    return new ImmutableTreeSet<>(sortedSet);
  }

  /**
   * Constructor.
   *
   * @param iterable   source of elements
   * @param comparator element's comparator. Might be null.
   * @throws NullPointerException if {@code iterable} is null
   */
  ImmutableTreeSet(Iterable<T> iterable, Comparator<? super T> comparator) {
    super();
    Objects.requireNonNull(
        iterable,
        "iterable to create ImmutableTreeSet cannot be null"
    );
    navigableSet = new TreeSet<>(comparator);
    for (final T t : iterable) {
      navigableSet.add(t);
    }
  }

  /**
   * Constructor.
   *
   * @param sortedSet source of elements
   * @throws NullPointerException if {@code sortedSet} is null
   */
  ImmutableTreeSet(SortedSet<T> sortedSet) {
    super();
    Objects.requireNonNull(
        sortedSet,
        "sortedSet to create ImmutableTreeSet cannot be null"
    );
    this.navigableSet = new TreeSet<>(sortedSet);
  }

  @Override
  public Optional<T> lower(T t) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableSet.lower(t));
  }

  @Override
  public Optional<T> floor(T t) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableSet.floor(t));
  }

  @Override
  public Optional<T> ceiling(T t) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableSet.ceiling(t));
  }

  @Override
  public Optional<T> higher(T t) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableSet.higher(t));
  }

  @Override
  public ImmutableNavigableSet<T> reversedOrderSet() {
    return new ImmutableTreeSet<>(navigableSet.descendingSet());
  }

  @Override
  public Iterator<T> reversedOrderIterator() {
    return new UnmodifiableIterator<>(navigableSet.descendingIterator());
  }

  @Override
  public ImmutableSortedSet<T> subSet(T fromElement, T toElement) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            navigableSet.subSet(fromElement, toElement)
        ));
  }

  @Override
  public ImmutableNavigableSet<T> subSet(
      T fromElement,
      boolean fromInclusive,
      T toElement,
      boolean toInclusive
  ) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            navigableSet.subSet(fromElement, fromInclusive, toElement, toInclusive)
        ));
  }

  @Override
  public ImmutableSortedSet<T> headSet(T toElement) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            navigableSet.headSet(toElement)
        ));
  }

  @Override
  public ImmutableNavigableSet<T> headSet(T toElement, boolean inclusive) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            navigableSet.headSet(toElement, inclusive)
        ));
  }

  @Override
  public ImmutableSortedSet<T> tailSet(T fromElement) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            navigableSet.tailSet(fromElement)
        ));
  }

  @Override
  public ImmutableNavigableSet<T> tailSet(T fromElement, boolean inclusive) {
    return tryGetSubSet(() ->
        new ImmutableTreeSet<>(
            navigableSet.tailSet(fromElement, inclusive)
        ));
  }

  @Override
  public NavigableSet<T> toMutableNavigableSet() {
    return new TreeSet<>(navigableSet);
  }

  @Override
  public Comparator<? super T> comparator() {
    return navigableSet.comparator();
  }

  @Override
  public Optional<T> first() {
    return ImmutableCollectionUtils.tryGetElement(navigableSet::first);
  }

  @Override
  public Optional<T> last() {
    return ImmutableCollectionUtils.tryGetElement(navigableSet::last);
  }

  @Override
  public SortedSet<T> toMutableSortedSet() {
    return toMutableNavigableSet();
  }

  @Override
  public ImmutableSet<T> concatWith(Iterable<T> iterable) {
    Objects.requireNonNull(iterable, "iterable to concat with cannot be null");
    final TreeSet<T> newTreeSet = new TreeSet<>(navigableSet);
    for (final T t : iterable) {
      newTreeSet.add(t);
    }
    return new ImmutableTreeSet<>(newTreeSet);
  }

  @Override
  public <R> ImmutableSet<R> map(Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(mapper, "mapper function cannot be null");
    final HashSet<R> hashSet = new HashSet<>(size());
    for (final T t : this) {
      hashSet.add(mapper.apply(t));
    }
    return new ImmutableHashSet<>(hashSet);
  }

  @Override
  public <R> ImmutableSet<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper) {
    Objects.requireNonNull(mapper, "flat mapper function cannot be null");
    final HashSet<R> hashSet = new HashSet<>();
    for (final T t : this) {
      for (final R r : mapper.apply(t)) {
        hashSet.add(r);
      }
    }
    return new ImmutableHashSet<>(hashSet);
  }

  @Override
  public ImmutableSet<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "filtering predicate cannot be null");
    final TreeSet<T> newTreeSet = new TreeSet<>(comparator());
    for (final T t : this) {
      if (predicate.test(t)) {
        newTreeSet.add(t);
      }
    }
    return new ImmutableTreeSet<>(newTreeSet);
  }

  @Override
  public int size() {
    return navigableSet.size();
  }

  @Override
  public boolean contains(Object element) {
    return Try.of(() -> navigableSet.contains(element))
        .orElse(false);
  }

  @Override
  public ImmutableList<T> toList() {
    return Immutable.listOf(navigableSet);
  }

  @Override
  public ImmutableSet<T> toSet() {
    return this;
  }

  @Override
  public Stream<T> parallelStream() {
    return navigableSet.parallelStream();
  }

  @Override
  public Stream<T> stream() {
    return navigableSet.stream();
  }

  @Override
  public Iterator<T> iterator() {
    return new UnmodifiableIterator<>(navigableSet.iterator());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ImmutableTreeSet<?> that = (ImmutableTreeSet<?>) o;
    return navigableSet.equals(that.navigableSet);
  }

  @Override
  public int hashCode() {
    return navigableSet.hashCode();
  }

  private ImmutableNavigableSet<T> tryGetSubSet(Supplier<ImmutableNavigableSet<T>> supplier) {
    return Try.of(supplier::get)
        .orElse(new ImmutableTreeSet<>(Immutable.emptyList(), navigableSet.comparator()));
  }
}
