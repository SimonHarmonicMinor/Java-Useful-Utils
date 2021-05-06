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
   * Maps the content of the set from one type to another.
   *
   * @param mapper mapping function
   * @param <R>    the result type
   * @return new set
   * @throws NullPointerException if {@code mapper} is null
   */
  <R> ImmutableSet<R> map(Function<? super T, ? extends R> mapper);

  /**
   * Joins {@link Iterable} objects that mapper returns.
   *
   * <pre>{@code
   * class Job {
   *     String name;
   *     List<Person> people;
   *     ...
   * }
   * ...
   * ImmutableSet<Job> jobs = getJobs();
   * ImmutableSet<Person> people =
   *      jobs.flatMap(j -&gt; j.getPeople());
   * }</pre>
   *
   * @param mapper mapping function, that returns {@link Iterable}&lt;{@code R}&gt;
   * @param <R>    the type of the return set
   * @return new set
   * @throws NullPointerException if {@code mapper} is null
   */
  <R> ImmutableSet<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper);

  /**
   * Returns new set which values match provided predicate.
   *
   * @param predicate predicate to apply to each element to determine if it should be included
   * @return new set
   * @throws NullPointerException if {@code predicate} is null
   */
  ImmutableSet<T> filter(Predicate<? super T> predicate);
}
