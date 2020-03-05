package com.github.simonharmonicminor.juu.collection.immutable;

import java.util.*;

/**
 * @param <T>
 * @see NavigableSet
 */
public interface ImmutableNavigableSet<T> extends ImmutableSortedSet<T> {
    /**
     * Returns the greatest element in this set strictly less than the given element, or {@code null}
     * if there is no such element.
     *
     * @param t the value to match
     * @return the greatest element less than {@code t}, or {@code null} if there is no such element
     */
    Optional<T> lower(T t);

    /**
     * Returns the greatest element in this set less than or equal to the given element, or {@code
     * null} if there is no such element.
     *
     * @param t the value to match
     * @return the greatest element less than or equal to {@code t}, or {@code null} if there is no
     * such element
     */
    Optional<T> floor(T t);

    /**
     * Returns the least element in this set greater than or equal to the given element, or {@code
     * null} if there is no such element.
     *
     * @param t the value to match
     * @return the least element greater than or equal to {@code t}, or {@code null} if there is no
     * such element
     */
    Optional<T> ceiling(T t);

    /**
     * Returns the least element in this set strictly greater than the given element, or {@code null}
     * if there is no such element.
     *
     * @param t the value to match
     * @return the least element greater than {@code t}, or {@code null} if there is no such element
     */
    Optional<T> higher(T t);

    /**
     * Returns a reverse order view of the elements contained in this set.
     *
     * <p>The returned set has an ordering equivalent to {@link Collections#reverseOrder(Comparator)
     * Collections.reverseOrder}{@code (comparator())}. The expression {@code
     * s.reversedOrderSet().reversedOrderSet()} returns a view of {@code s} essentially equivalent to {@code
     * s}.
     *
     * @return a reverse order view of this set
     */
    ImmutableNavigableSet<T> reversedOrderSet();

    /**
     * Returns an iterator over the elements in this set, in descending order. Equivalent in effect to
     * {@code reversedOrderSet().iterator()}.
     *
     * @return an iterator over the elements in this set, in descending order
     */
    Iterator<T> reversedOrderIterator();

    /**
     * Returns a view of the portion of this set whose elements range from {@code fromElement} to
     * {@code toElement}. If {@code fromElement} and {@code toElement} are equal, the returned set is
     * empty unless {@code fromInclusive} and {@code toInclusive} are both true.
     *
     * @param fromElement   low endpoint of the returned set
     * @param fromInclusive {@code true} if the low endpoint is to be included in the returned view
     * @param toElement     high endpoint of the returned set
     * @param toInclusive   {@code true} if the high endpoint is to be included in the returned view
     * @return a view of the portion of this set whose elements range from {@code fromElement},
     * inclusive, to {@code toElement}, exclusive
     */
    ImmutableNavigableSet<T> subSet(
            T fromElement, boolean fromInclusive, T toElement, boolean toInclusive);

    /**
     * Returns a view of the portion of this set whose elements are less than (or equal to, if {@code
     * inclusive} is true) {@code toElement}.
     *
     * @param toElement high endpoint of the returned set
     * @param inclusive {@code true} if the high endpoint is to be included in the returned view
     * @return a view of the portion of this set whose elements are less than (or equal to, if {@code
     * inclusive} is true) {@code toElement}
     */
    ImmutableNavigableSet<T> headSet(T toElement, boolean inclusive);

    /**
     * Returns a view of the portion of this set whose elements are greater than (or equal to, if
     * {@code inclusive} is true) {@code fromElement}.
     *
     * @param fromElement low endpoint of the returned set
     * @param inclusive   {@code true} if the low endpoint is to be included in the returned view
     * @return a view of the portion of this set whose elements are greater than or equal to {@code
     * fromElement}
     */
    ImmutableNavigableSet<T> tailSet(T fromElement, boolean inclusive);

    /**
     * Converts this immutable navigable set to mutable one. Creates new object, so its mutations does
     * not affect the immutable one.
     *
     * @return new mutable navigable set
     */
    NavigableSet<T> toMutableNavigableSet();
}
