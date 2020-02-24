package com.github.simonharmonicminor.juu.collection.immutable;

import java.util.Comparator;
import java.util.Optional;
import java.util.SortedSet;

/**
 * @param <T> the type of the objects that set contains
 * @see SortedSet
 */
public interface ImmutableSortedSet<T> extends ImmutableSet<T> {
    /**
     * @return the comparator used to order the keys in this map, or {@code null} if this map uses the
     * {@linkplain Comparable natural ordering} of its keys.
     */
    Comparator<? super T> comparator();

    /**
     * Returns a view of the portion of this set whose elements range from {@code fromElement},
     * inclusive, to {@code toElement}, exclusive.
     *
     * @param fromElement low endpoint (inclusive) of the returned set
     * @param toElement   high endpoint (exclusive) of the returned set
     * @return a view of the portion of this set whose elements range from {@code fromElement},
     * inclusive, to {@code toElement}, exclusive
     */
    ImmutableSortedSet<T> subSet(T fromElement, T toElement);

    /**
     * Returns a view of the portion of this set whose elements are strictly less than {@code
     * toElement}.
     *
     * @param toElement high endpoint (exclusive) of the returned set
     * @return a view of the portion of this set whose elements are strictly less than {@code
     * toElement}
     */
    ImmutableSortedSet<T> headSet(T toElement);

    /**
     * Returns a view of the portion of this set whose elements are greater than or equal to {@code
     * fromElement}.
     *
     * @param fromElement low endpoint (inclusive) of the returned set
     * @return a view of the portion of this set whose elements are greater than or equal to {@code
     * fromElement}
     */
    ImmutableSortedSet<T> tailSet(T fromElement);

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     */
    Optional<T> first();

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     */
    Optional<T> last();

    /**
     * Converts this immutable sorted set to mutable one. Creates new object, so its mutations does
     * not affect the immutable one.
     *
     * @return new mutable sorted set
     */
    SortedSet<T> toMutableSortedSet();
}
