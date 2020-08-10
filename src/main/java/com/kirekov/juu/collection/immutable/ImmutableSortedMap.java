package com.kirekov.juu.collection.immutable;

import java.util.Comparator;
import java.util.Optional;
import java.util.SortedMap;

/**
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @see SortedMap
 * @since 1.1
 */
public interface ImmutableSortedMap<K, V> extends ImmutableMap<K, V> {
    /**
     * Returns the comparator used to order the keys in this map, or
     * {@code null} if this map uses the {@linkplain Comparable
     * natural ordering} of its keys.
     *
     * @return the comparator used to order the keys in this map,
     * or {@code null} if this map uses the natural ordering
     * of its keys
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
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @param toKey   high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys range from
     * {@code fromKey}, inclusive, to {@code toKey}, exclusive
     */
    ImmutableSortedMap<K, V> subMap(K fromKey, K toKey);

    /**
     * Returns a view of the portion of this map whose keys are
     * strictly less than {@code toKey}.  The returned map is backed
     * by this map, so changes in the returned map are reflected in
     * this map, and vice-versa.  The returned map supports all
     * optional map operations that this map supports.
     *
     * @param toKey high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are strictly
     * less than {@code toKey}
     */
    ImmutableSortedMap<K, V> headMap(K toKey);

    /**
     * Returns a view of the portion of this map whose keys are
     * greater than or equal to {@code fromKey}.  The returned map is
     * backed by this map, so changes in the returned map are
     * reflected in this map, and vice-versa.  The returned map
     * supports all optional map operations that this map supports.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are greater
     * than or equal to {@code fromKey}
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
     * Converts immutable sorted map to mutable one.
     *
     * @return new mutable sorted map
     */
    SortedMap<K, V> toMutableSortedMap();
}
