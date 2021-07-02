package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.collection.immutable.abstraction.AbstractImmutableSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * An immutable implementation of java native {@link HashSet}.
 *
 * @param <T> the type of the content
 * @see ImmutableSet
 * @see Set
 * @see HashSet
 * @since 1.0
 */
public final class ImmutableHashSet<T> extends AbstractImmutableSet<T> {

  private final Set<T> hashSet;

  /**
   * Constructor.
   *
   * @param iterable the source of elements
   */
  public ImmutableHashSet(Iterable<T> iterable) {
    super();
    hashSet = new HashSet<>();
    for (final T element : iterable) {
      hashSet.add(element);
    }
  }

  @Override
  public ImmutableSet<T> concatWith(Iterable<T> iterable) {
    Objects.requireNonNull(iterable, "iterable to concat with cannot be null");
    final HashSet<T> newHashSet = new HashSet<>(this.hashSet);
    for (final T t : iterable) {
      newHashSet.add(t);
    }
    return new ImmutableHashSet<>(newHashSet);
  }

  @Override
  public <R> ImmutableSet<R> map(Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(mapper, "mapper function cannot be null");
    final HashSet<R> newHashSet = new HashSet<>(size());
    for (final T t : this) {
      newHashSet.add(mapper.apply(t));
    }
    return new ImmutableHashSet<>(newHashSet);
  }

  @Override
  public <R> ImmutableSet<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper) {
    Objects.requireNonNull(mapper, "flat mapper function cannot be null");
    final HashSet<R> newHashSet = new HashSet<>(size());
    for (final T t : this) {
      final ImmutableHashSet<R> immutableHashSet = new ImmutableHashSet<>(mapper.apply(t));
      newHashSet.addAll(immutableHashSet.hashSet);
    }
    return new ImmutableHashSet<>(newHashSet);
  }

  @Override
  public ImmutableSet<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "filtering predicate cannot be null");
    final HashSet<T> newHashSet = new HashSet<>(size());
    for (final T t : this) {
      if (predicate.test(t)) {
        newHashSet.add(t);
      }
    }
    return new ImmutableHashSet<>(newHashSet);
  }

  @Override
  public int size() {
    return hashSet.size();
  }

  @Override
  public boolean contains(Object element) {
    return hashSet.contains(element);
  }

  @Override
  public ImmutableList<T> toList() {
    return Immutable.listOf(this.hashSet);
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
    return new UnmodifiableIterator<>(hashSet.iterator());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ImmutableHashSet<?> that = (ImmutableHashSet<?>) o;
    return hashSet.equals(that.hashSet);
  }

  @Override
  public int hashCode() {
    return hashSet.hashCode();
  }
}
