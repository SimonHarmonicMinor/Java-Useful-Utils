package com.github.simonharmonicminor.juu.collection.immutable;

import java.util.Comparator;
import java.util.Optional;
import java.util.SortedMap;

/**
 * Defines an immutable sorted map.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @see ImmutableMap
 * @see java.util.SortedMap
 * @see java.util.Map
 * @see ImmutableCollection
 * @since 1.0
 */
public interface ImmutableSortedMap<K, V> extends ImmutableMap<K, V> {
    /**
     * @return the comparator used to order the keys in this map, or
     * {@code null} if this map uses the {@linkplain Comparable
     * natural ordering} of its keys.
     */
    Comparator<? super K> comparator();

    /**
     * Returns a view of the portion of this map whose keys range from
     * {@code fromKey}, inclusive, to {@code toKey}, exclusive.  (If
     * {@code fromKey} and {@code toKey} are equal, the returned map
     * is empty.)  The returned map is backed by this map, so changes
     * in the returned map are reflected in this map, and vice-versa.
     * The returned map supports all optional map operations that this
     * map supports.
     *
     * <p>The returned map will throw an {@code IllegalArgumentException}
     * on an attempt to insert a key outside its range.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @param toKey   high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys range from
     * {@code fromKey}, inclusive, to {@code toKey}, exclusive
     * @throws ClassCastException       if {@code fromKey} and {@code toKey}
     *                                  cannot be compared to one another using this map's comparator
     *                                  (or, if the map has no comparator, using natural ordering).
     *                                  Implementations may, but are not required to, throw this
     *                                  exception if {@code fromKey} or {@code toKey}
     *                                  cannot be compared to keys currently in the map.
     * @throws NullPointerException     if {@code fromKey} or {@code toKey}
     *                                  is null and this map does not permit null keys
     * @throws IllegalArgumentException if {@code fromKey} is greater than
     *                                  {@code toKey}; or if this map itself has a restricted
     *                                  range, and {@code fromKey} or {@code toKey} lies
     *                                  outside the bounds of the range
     */
    ImmutableSortedMap<K, V> subMap(K fromKey, K toKey);

    /**
     * Returns a view of the portion of this map whose keys are
     * strictly less than {@code toKey}.  The returned map is backed
     * by this map, so changes in the returned map are reflected in
     * this map, and vice-versa.  The returned map supports all
     * optional map operations that this map supports.
     *
     * <p>The returned map will throw an {@code IllegalArgumentException}
     * on an attempt to insert a key outside its range.
     *
     * @param toKey high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are strictly
     * less than {@code toKey}
     * @throws ClassCastException       if {@code toKey} is not compatible
     *                                  with this map's comparator (or, if the map has no comparator,
     *                                  if {@code toKey} does not implement {@link Comparable}).
     *                                  Implementations may, but are not required to, throw this
     *                                  exception if {@code toKey} cannot be compared to keys
     *                                  currently in the map.
     * @throws NullPointerException     if {@code toKey} is null and
     *                                  this map does not permit null keys
     * @throws IllegalArgumentException if this map itself has a
     *                                  restricted range, and {@code toKey} lies outside the
     *                                  bounds of the range
     */
    ImmutableSortedMap<K, V> headMap(K toKey);

    /**
     * Returns a view of the portion of this map whose keys are
     * greater than or equal to {@code fromKey}.  The returned map is
     * backed by this map, so changes in the returned map are
     * reflected in this map, and vice-versa.  The returned map
     * supports all optional map operations that this map supports.
     *
     * <p>The returned map will throw an {@code IllegalArgumentException}
     * on an attempt to insert a key outside its range.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are greater
     * than or equal to {@code fromKey}
     * @throws ClassCastException       if {@code fromKey} is not compatible
     *                                  with this map's comparator (or, if the map has no comparator,
     *                                  if {@code fromKey} does not implement {@link Comparable}).
     *                                  Implementations may, but are not required to, throw this
     *                                  exception if {@code fromKey} cannot be compared to keys
     *                                  currently in the map.
     * @throws NullPointerException     if {@code fromKey} is null and
     *                                  this map does not permit null keys
     * @throws IllegalArgumentException if this map itself has a
     *                                  restricted range, and {@code fromKey} lies outside the
     *                                  bounds of the range
     */
    ImmutableSortedMap<K, V> tailMap(K fromKey);

    /**
     * Returns the first (lowest) key currently in this map.
     *
     * @return the first (lowest) key currently in this map
     */
    Optional<K> firstKey();

    /**
     * Returns the last (highest) key currently in this map.
     *
     * @return the last (highest) key currently in this map
     */
    Optional<K> lastKey();

    /**
     * Converts this immutable sorted map to mutable one.
     * Creates new object, so its mutations does not affect the immutable one
     *
     * @return new mutable sorted map
     */
    SortedMap<K, V> toMutableSortedMap();
}
