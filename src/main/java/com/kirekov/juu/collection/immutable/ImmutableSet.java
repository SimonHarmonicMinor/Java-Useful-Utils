package com.kirekov.juu.collection.immutable;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Defines an immutable set. Unlike native {@link java.util.Set} this interface does not have any
 * methods that can mutate its content. So it can be safely injected to any methods or objects.
 *
 * @param <T> the type of the object, that set contains
 * @see ImmutableCollection
 * @see java.util.Set
 * @since 1.0
 */
public interface ImmutableSet<T> extends ImmutableCollection<T> {

  /**
   * Concatenates current set with provided iterable object and returns new set.
   *
   * @param iterable iterable object to join with
   * @return new set that contains current elements and elements provided with {@code iterable}
   * @throws NullPointerException if {@code iterable} is null
   */
  ImmutableSet<T> concatWith(Iterable<T> iterable);

  /**
   * {@inheritDoc}
   */
  @Override
  <R> ImmutableSet<R> map(Function<? super T, ? extends R> mapper);

  /**
   * {@inheritDoc}
   */
  @Override
  <R> ImmutableSet<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper);

  /**
   * {@inheritDoc}
   */
  @Override
  ImmutableSet<T> filter(Predicate<? super T> predicate);
}
