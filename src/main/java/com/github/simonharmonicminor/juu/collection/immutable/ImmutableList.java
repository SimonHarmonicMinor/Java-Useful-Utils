package com.github.simonharmonicminor.juu.collection.immutable;

import java.util.Comparator;
import java.util.OptionalInt;
import java.util.function.*;

/**
 * Defines an immutable list. Unlike native {@link java.util.List} this interface does not have any
 * methods that can mutate its content. So it can be safely injected to any methods or objects.
 *
 * @param <T> the type of the object, that list contains
 * @see ImmutableCollection
 * @see java.util.List
 * @since 1.0
 */
public interface ImmutableList<T> extends ImmutableCollection<T> {
    /**
     * Returns the element by its index. Supports negative indices in "python-way". For instance,
     *
     * <pre>{@code
     * ImmutableList<Integer> list = getList(); // [1, 2, 3, 4, 5]
     * list.get(1);     // 2
     * list.get(3);     // 4
     * list.get(-1);    // 5
     * list.get(-3);    // 3
     * list.get(-5);    // 1;
     * list.get(0);     // 1;
     * }</pre>
     * <p>
     * If a is less than zero, then {@code list.get(a)} equals to {@code list.get(list.size() -
     * Math.abs(a))}
     *
     * @param index index of the element to return
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if the index is bigger than or equal to {@code list.size()}
     *                                   or {@code list.size() - Math.abs(index)} is less than zero if index is negative
     */
    T get(int index);

    /**
     * Returns the first index of the specified element or {@link OptionalInt#empty()}, if element is
     * not found
     *
     * @param element the searching element
     * @return the first index of the {@link OptionalInt#empty()}
     */
    OptionalInt indexOf(T element);

    /**
     * Returns the last index of the specified element or {@link OptionalInt#empty()}, if element is
     * not found.
     *
     * @param element the searching element
     * @return the first index of the element or {@link OptionalInt#empty()}
     */
    OptionalInt lastIndexOf(T element);

    /**
     * Proxy method for {@code this.slice(fromIndex, size(), 1)}.
     *
     * @param fromIndex start index (inclusively)
     * @return sublist
     * @see ImmutableList#slice(int, int, int)
     */
    ImmutableList<T> slice(int fromIndex);

    /**
     * Proxy method for {@code this.slice(fromIndex, toIndex, 1)} if {@code fromIndex} is before
     * {@code toIndex} and {@code this.slice(fromIndex, toIndex, -1)} otherwise
     *
     * @param fromIndex start index (inclusively)
     * @param toIndex   end index (exclusively)
     * @return sublist
     * @see ImmutableList#slice(int, int, int)
     */
    ImmutableList<T> slice(int fromIndex, int toIndex);

    /**
     * Returns sublist from {@code fromIndex} inclusively to {@code toIndex} exclusively with given
     * step size.<br>
     * Supports negative indices. If step size is negative, then the list will be traversed backwards.
     * <br>
     * For instance,
     *
     * <pre>{@code
     * ImmutableList<Integer> list = getList(); // [1, 2, 3, 4, 5, 6]
     * list.slice(0, 3, 1);         // [1, 2, 3]
     * list.slice(-1, 2, -1);       // [6, 5, 4]
     * list.slice(0, 6, 2);         // [0, 3, 5]
     * }</pre>
     *
     * @param fromIndex start index (inclusively)
     * @param toIndex   end index (exclusively)
     * @return result sublist
     * @throws IndexOutOfBoundsException if fromIndex is out of bounds
     * @throws IllegalArgumentException  if stepSize is zero
     * @see ImmutableList#get(int)
     */
    ImmutableList<T> slice(int fromIndex, int toIndex, int stepSize);

    /**
     * Proxy method for {@code this.step(0, stepSize)} if stepSize is bigger than zero, otherwise
     * {@code this.step(-1, stepSize)}
     *
     * @param stepSize the size of step traversing
     * @see ImmutableList#step(int, int)
     */
    ImmutableList<T> step(int stepSize);

    /**
     * Returns sublist traversed with given step starting from the given index. For instance,
     *
     * <pre>{@code
     * ImmutableList<Integer> list = createList(); // [1, 2, 3, 4, 5, 6, 7]
     * ImmutableList<Integer> newOne = list.step(0, 2) // [1, 3, 5, 7]
     * ImmutableList<Integer> newTwo = list.step(1, 3) // [2, 5]
     * }</pre>
     * <p>
     * Step size might be negative as well. It means backwards traversing.
     *
     * <pre>{@code
     * ImmutableList<Integer> list = createList(); [1, 2, 3, 4, 5, 6, 7]
     * ImmutableList<Integer> newOne = list.step(-1, -2) // [7, 5, 3, 1]
     * }</pre>
     *
     * @param fromIndex start index (might be negative)
     * @param stepSize  the size of the step traversing
     * @return stepped list
     * @throws IndexOutOfBoundsException if fromIndex is out of bounds
     * @throws IllegalArgumentException  if stepSize is zero
     * @see ImmutableList#get(int)
     */
    ImmutableList<T> step(int fromIndex, int stepSize);

    /**
     * Joins current list with provided iterable object and returns new list
     *
     * @param iterable iterable object to join with
     * @return new list that contains current elements and elements provided with {@code iterable}
     * @throws NullPointerException if {@code iterable} is null
     */
    ImmutableList<T> concatWith(Iterable<T> iterable);

    /**
     * Zips current list with provided list and returns pairs, where key contains the element from the
     * current list and value contains the element from the provided list. For instance,
     *
     * <pre>{@code
     * ImmutableList<Person> people = getPeople();
     * ImmutableList<Job> jobs = getJobs();
     *
     * ImmutableList<Pair<Person, Job>> zipped =
     *      people.zipWith(jobs);
     * }</pre>
     * <p>
     * If lists have different length, then at some position the key or the value will become null.
     *
     * @param list provided list
     * @param <R>  the type of the content of the provided list
     * @return new list
     * @throws NullPointerException if {@code list} is null
     */
    <R> ImmutableList<Pair<T, R>> zipWith(ImmutableList<R> list);

    /**
     * Returns two adjacent elements of the list as pairs. If list has length less then two, returns
     * empty list.
     *
     * @return new list
     */
    ImmutableList<Pair<T, T>> zipWithNext();

    /**
     * Maps the content of the list from one type to another
     *
     * @param mapper mapping function
     * @param <R>    the result type
     * @return new list
     * @throws NullPointerException if {@code mapper} is null
     */
    <R> ImmutableList<R> map(Function<? super T, ? extends R> mapper);

    /**
     * Maps the content of the list from one type to another. Mapper accepts current index and current
     * value.
     *
     * @param mapper mapping function
     * @param <R>    the result type
     * @return new list
     * @throws NullPointerException if {@code mapper} is null
     */
    <R> ImmutableList<R> mapIndexed(BiFunction<Integer, ? super T, ? extends R> mapper);

    /**
     * Joins {@link Iterable} objects that mapper returns. For instance,
     *
     * <pre>{@code
     * class Job {
     *     String name;
     *     List<Person> people;
     *     ...
     * }
     * ...
     * ImmutableList<Job> jobs = getJobs();
     * ImmutableList<Person> people =
     *      jobs.flatMap(j -&gt; j.getPeople());
     * }</pre>
     *
     * @param mapper mapping function, that returns {@link Iterable}&lt;{@code R}&gt;
     * @param <R>    the type of the return list
     * @return new list
     * @throws NullPointerException if {@code mapper} is null
     */
    <R> ImmutableList<R> flatMap(Function<? super T, ? extends Iterable<R>> mapper);

    /**
     * This method has exactly the same behaviour as {@link ImmutableList#flatMap(Function)}, but
     * mapper accepts two arguments. The first is the current index and the second is the current
     * value.
     *
     * @throws NullPointerException if {@code mapper} is null
     * @see ImmutableList#flatMap(Function)
     */
    <R> ImmutableList<R> flatMapIndexed(BiFunction<Integer, ? super T, ? extends Iterable<R>> mapper);

    /**
     * Returns new list, which values match provided predicate
     *
     * @param predicate predicate to apply to each element to determine if it should be included
     * @return new list
     * @throws NullPointerException if {@code predicate} is null
     */
    ImmutableList<T> filter(Predicate<? super T> predicate);

    /**
     * Returns new list, which values match provided predicate
     *
     * @param predicate predicate to apply to each element to determine if it should be included. The
     *                  first argument is the current index and the second one is the current value
     * @return new list
     * @throws NullPointerException if {@code predicate} is null
     */
    ImmutableList<T> filterIndexed(BiPredicate<Integer, ? super T> predicate);

    /**
     * Traverses each element like {@link Iterable#forEach(Consumer)}, but consumer accepts two
     * arguments. The first arg is the current index and the second one is the current value
     *
     * @param action consumer
     * @throws NullPointerException if {@code action} is null
     */
    void forEachIndexed(BiConsumer<Integer, ? super T> action);

    /**
     * Sorts the list and returns the new one
     *
     * @param comparator comparator, which defines the sort order
     * @return new sorted list
     * @throws NullPointerException if {@code comparator} is null
     */
    ImmutableList<T> sorted(Comparator<? super T> comparator);

    /**
     * Returns the first {@code size} elements. <br>
     * If {@code size} is bigger than the size of the list, then returns the list itself. <br>
     * If {@code size} is zero, then returns an empty list.
     *
     * @param size the max size of new list, starting from the beginning
     * @return new list
     * @throws IllegalArgumentException if {@code size} is less than zero
     */
    ImmutableList<T> limit(int size);

    /**
     * Skips the first {@code size} elements and returns remaining as a new list. <br>
     * if {@code size} is bigger than the size of the list, then returns an empty list. <br>
     * if {@code size} if zero, then return the list itself.
     *
     * @param size the count of the elements that must be skipped from the beginning
     * @return new list
     * @throws IllegalArgumentException if {@code size} is less than zero
     */
    ImmutableList<T> skip(int size);

    /**
     * Reverses the list and returns its copy
     *
     * @return reversed list
     */
    ImmutableList<T> reversed();
}