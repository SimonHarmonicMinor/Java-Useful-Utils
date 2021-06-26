package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.collection.ParallelStreaming;
import com.kirekov.juu.monad.Try;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Defines an immutable collection. Unlike native {@link java.util.Collection} this interface does
 * not have any methods that can mutate its content. So it can be safely injected to any methods or
 * objects. <br> It is strongly recommended to put only immutable objects to this class. For
 * instance.
 *
 * <pre>{@code
 * ImmutableCollection<Person> collection = getSome();
 * for (Person p : collection) {
 *     p.setName("David");
 * }
 * }</pre>
 *
 * <p>Although the collection itself does not have any methods to remove or add new elements, its
 * content has been changed. So, pay attention to this.</p>
 *
 * @param <T> the type of the object, that collection contains
 * @see java.util.Collection
 * @since 1.0
 */
public interface ImmutableCollection<T> extends ParallelStreaming<T>, Iterable<T> {

  /**
   * Get size of the collection.
   *
   * @return the size of the collection
   */
  int size();

  /**
   * Whether the collection is empty.
   *
   * @return true if collection size is zero, otherwise false
   */
  default boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Whether the collection is NOT empty.
   *
   * @return true if collection size is not zero, otherwise false
   */
  default boolean isNotEmpty() {
    return !isEmpty();
  }

  /**
   * Whether the collection contains the element.
   *
   * @param element the element whose presence in this collection is to be tested
   * @return true if collection contains the element, otherwise false
   */
  boolean contains(Object element);

  /**
   * Whether the collection NOT contains the element.
   *
   * @param element the element whose presence in this collection is to be tested
   * @return true if collection NOT contains the element, otherwise false
   */
  default boolean notContains(Object element) {
    return !contains(element);
  }

  /**
   * Whether the collection contains all elements.
   *
   * @param elements the elements whose presence in this collection is to be tested
   * @return true if collection contains all given elements, otherwise false
   * @throws NullPointerException if "elements" is null
   */
  default boolean containsAll(Iterable<?> elements) {
    Objects
        .requireNonNull(elements, "iterable or elements to test all their presence cannot be null");
    for (Object t : elements) {
      if (notContains(t)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Whether the collection contains any of elements.
   *
   * @param elements the elements whose presence in this collection is to be tested
   * @return true if collection contains any of given elements, otherwise false
   * @throws NullPointerException if "elements" is null
   */
  default boolean containsAny(Iterable<?> elements) {
    Objects.requireNonNull(elements,
        "iterable or elements to test any of their presence cannot be null");
    for (Object t : elements) {
      if (contains(t)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Whether all elements of this collection match the provided predicate.
   *
   * @param predicate predicate to apply to elements of this collection
   * @return true if predicate matches all elements, otherwise false
   * @throws NullPointerException if predicate is null
   */
  default boolean allMatch(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate to match all elements cannot be null");
    for (T t : this) {
      if (!predicate.test(t)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Whether any element of this collection match the provided predicate.
   *
   * @param predicate predicate to apply to elements of this collection
   * @return true if predicate matches any element, otherwise false
   * @throws NullPointerException if predicate is null
   */
  default boolean anyMatch(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate to test any match cannot be null");
    for (T t : this) {
      if (predicate.test(t)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Whether all elements of this collection does NOT match the provided predicate.
   *
   * @param predicate predicate to apply to elements of this collection
   * @return true if predicate does NOT match all elements, otherwise false
   * @throws NullPointerException if predicate is null
   */
  default boolean noneMatch(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate to test none match cannot be null");
    for (T t : this) {
      if (predicate.test(t)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Performs a reduction on the elements of this collection, using the provided identity value and
   * an associative accumulation function, and returns the reduced value.
   *
   * <pre>{@code
   * T result = identity;
   * for (T element : this)
   *     result = accumulator.apply(result, element)
   * return result;
   * }</pre>
   *
   * @param identity    start value
   * @param accumulator accumulation function
   * @return reduction result
   * @throws NullPointerException if accumulator is null
   */
  default T reduce(T identity, BinaryOperator<T> accumulator) {
    Objects.requireNonNull(accumulator, "binary operator to reduce the collection cannot be null");
    return stream().reduce(identity, accumulator);
  }

  /**
   * Performs a reduction on the elements of this collection, using the provided identity value and
   * an associative accumulation function, and returns the reduced value. If collection is empty,
   * returns {@link Optional#empty()}.
   *
   * @param accumulator accumulation function
   * @return reduction result
   * @throws NullPointerException if accumulator is null
   */
  default Optional<T> reduce(BinaryOperator<T> accumulator) {
    Objects.requireNonNull(accumulator, "binary operator to reduce the collection cannot be null");
    return stream().reduce(accumulator);
  }

  /**
   * Returns min value of the collection calculated with provided comparator. If collection is
   * empty, returns {@link Optional#empty()}.
   *
   * @param comparator comparator, which determines min value
   * @return min value
   * @throws NullPointerException if comparator is null
   */
  default Optional<T> min(Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator, "comparator to find the min element cannot be null");
    return Try.of(() -> stream().min(comparator))
        .orElse(Optional.empty());
  }

  /**
   * Returns max value of the collection calculated with provided comparator. If collection is
   * empty, returns {@link Optional#empty()}.
   *
   * @param comparator comparator, which determines max value
   * @return max value
   * @throws NullPointerException if comparator is null
   */
  default Optional<T> max(Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator, "comparator to find the max element cannot be null");
    return Try.of(() -> stream().max(comparator))
        .orElse(Optional.empty());
  }

  /**
   * Returns first element in the collection. If it is empty, returns {@link Optional#empty()}. If
   * collection is unordered, then the returning value may be different from time to time.
   *
   * @return first present value
   */
  default Optional<T> findFirst() {
    return isEmpty() ? Optional.empty() : Optional.ofNullable(iterator().next());
  }

  /**
   * Returns first element in the collection which value matches with the provided predicate. If no
   * such element is found, returns {@link Optional#empty()}. If collection is unordered, then the
   * returning value may be different from time to time.
   *
   * @param predicate predicate which determines the value
   * @return first present value
   * @throws NullPointerException if {@code comparator} is null
   */
  default Optional<T> findFirst(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate to find the first matched element cannot be null");
    for (T t : this) {
      if (predicate.test(t)) {
        return Optional.ofNullable(t);
      }
    }
    return Optional.empty();
  }

  /**
   * Transform the collection to {@linkplain ImmutableList}.
   *
   * @return collection converted to immutable list
   */
  ImmutableList<T> toList();

  /**
   * Transform the collection to {@linkplain ImmutableSet}.
   *
   * @return collection converted to immutable set
   */
  ImmutableSet<T> toSet();

  /**
   * Converts immutable collection to java native {@link List}. Creates new object, so list
   * mutations do not affect the collection. <br>
   * <br>
   * <b>NB:</b> this method does not clone objects the collection contains. <br>
   * <br>
   * For instance,
   *
   * <pre>{@code
   * ImmutableCollection<Person> ic = getSome();
   * List<Person> list1 = ic.toMutableList();
   * list1.clear();
   * assert ic.size() != list1.size();
   *
   * // but
   * List<Person> list2 = ic.toMutableList();
   * for (Person p : list2) {
   *     p.setName("Aaron");
   * }
   * for (Person p : ic) {
   *     assert p.equals("Aaron");
   * }
   * }</pre>
   *
   * @return mutable list that contains elements of the collection
   */
  default List<T> toMutableList() {
    ArrayList<T> arrayList = new ArrayList<>(size());
    for (T t : this) {
      arrayList.add(t);
    }
    return arrayList;
  }

  /**
   * Converts immutable collection to java native {@link Set}. Creates new object, so set mutations
   * do not affect the collection.
   *
   * @return mutable set that contains elements of the collection
   * @see ImmutableCollection#toMutableList()
   */
  default Set<T> toMutableSet() {
    HashSet<T> hashSet = new HashSet<>(size());
    for (T t : this) {
      hashSet.add(t);
    }
    return hashSet;
  }

  /**
   * Overrides method from {@link Object#hashCode()}. Must be implemented.
   *
   * @return hashcode of the collection
   */
  @Override
  int hashCode();

  /**
   * Overrides method from {@link Object#equals(Object)} Must be implemented.
   *
   * @param obj the object whose equality with the collection is to be tested
   * @return true if objects are equal, otherwise false
   */
  @Override
  boolean equals(Object obj);

  /**
   * Maps content from one type to another.
   *
   * @param mapper mapping function
   * @param <R>    the result type
   * @return new collection
   */
  <R> ImmutableCollection<R> map(Function<? super T, ? extends R> mapper);

  /**
   * Joins {@link Iterable} objects that mapper returns. For instance,
   *
   * <pre>{@code
   * class Job {
   *     String name;
   *     Collection<Person> people;
   *     ...
   * }
   * ...
   * ImmutableCollection<Job> jobs = getJobs();
   * ImmutableCollection<Person> people =
   *      jobs.flatMap(j -> j.getPeople());
   * }</pre>
   *
   * @param mapper mapping function, that returns {@link Iterable}
   * @param <R>    the type of the return collection
   * @return new list
   * @throws NullPointerException if {@code mapper} is null
   */
  <R> ImmutableCollection<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper);

  /**
   * Returns new collection which values match provided predicate.
   *
   * @param predicate predicate to apply to each element to determine if it should be included
   * @return new collection
   * @throws NullPointerException if {@code predicate} is null
   */
  ImmutableCollection<T> filter(Predicate<? super T> predicate);

  /**
   * Concatenates current collection with provided iterable object and returns new collections.
   *
   * @param iterable iterable object to join with
   * @return new collection that contains current elements and elements provided with {@code
   * iterable}
   * @throws NullPointerException if {@code iterable} is null
   */
  ImmutableCollection<T> concatWith(Iterable<T> iterable);
}
