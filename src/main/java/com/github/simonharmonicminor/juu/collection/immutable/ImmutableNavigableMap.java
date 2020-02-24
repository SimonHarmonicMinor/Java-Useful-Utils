package com.github.simonharmonicminor.juu.collection.immutable;

import java.util.*;

/**
 * @param <K> the type of the key
 * @param <V> the type of value
 * @see NavigableMap
 */
public interface ImmutableNavigableMap<K, V> extends ImmutableSortedMap<K, V> {
    /**
     * Returns a key-value mapping associated with the greatest key
     * strictly less than the given key, or {@link Optional#empty()} if there is
     * no such key.
     *
     * @param key the key
     * @return an entry with the greatest key less than {@code key},
     * or {@link Optional#empty()} if there is no such key
     */
    Optional<Pair<K, V>> lowerPair(K key);

    /**
     * Returns the greatest key strictly less than the given key, or
     * {@link Optional#empty()} if there is no such key.
     *
     * @param key the key
     * @return the greatest key less than {@code key},
     * or {@link Optional#empty()} if there is no such key
     */
    Optional<K> lowerKey(K key);

    /**
     * Returns a key-value mapping associated with the greatest key
     * less than or equal to the given key, or {@link Optional#empty()} if there
     * is no such key.
     *
     * @param key the key
     * @return an entry with the greatest key less than or equal to
     * {@code key}, or {@link Optional#empty()} if there is no such key
     */
    Optional<Pair<K, V>> floorEntry(K key);

    /**
     * Returns the greatest key less than or equal to the given key,
     * or {@link Optional#empty()} if there is no such key.
     *
     * @param key the key
     * @return the greatest key less than or equal to {@code key},
     * or {@link Optional#empty()} if there is no such key
     */
    Optional<K> floorKey(K key);

    /**
     * Returns a key-value mapping associated with the least key
     * greater than or equal to the given key, or {@link Optional#empty()} if
     * there is no such key.
     *
     * @param key the key
     * @return an entry with the least key greater than or equal to
     * {@code key}, or {@link Optional#empty()} if there is no such key
     */
    Optional<Pair<K, V>> ceilingEntry(K key);

    /**
     * Returns the least key greater than or equal to the given key,
     * or {@link Optional#empty()} if there is no such key.
     *
     * @param key the key
     * @return the least key greater than or equal to {@code key},
     * or {@link Optional#empty()} if there is no such key
     */
    Optional<K> ceilingKey(K key);

    /**
     * Returns a key-value mapping associated with the least key
     * strictly greater than the given key, or {@link Optional#empty()} if there
     * is no such key.
     *
     * @param key the key
     * @return an entry with the least key greater than {@code key},
     * or {@link Optional#empty()} if there is no such key
     */
    Optional<Pair<K, V>> higherEntry(K key);

    /**
     * Returns the least key strictly greater than the given key, or
     * {@link Optional#empty()} if there is no such key.
     *
     * @param key the key
     * @return the least key greater than {@code key},
     * or {@link null} if there is no such key
     */
    Optional<K> higherKey(K key);

    /**
     * Returns a key-value mapping associated with the least
     * key in this map, or {@link Optional#empty()} if the map is empty.
     *
     * @return an entry with the least key,
     * or {@link Optional#empty()} if this map is empty
     */
    Optional<Pair<K, V>> firstEntry();

    /**
     * Returns a key-value mapping associated with the greatest
     * key in this map, or {@link Optional#empty()} if the map is empty.
     *
     * @return an entry with the greatest key,
     * or {@link Optional#empty()} if this map is empty
     */
    Optional<Pair<K, V>> lastEntry();

    /**
     * Removes and returns a key-value mapping associated with
     * the least key in this map, or {@link Optional#empty()} if the map is empty.
     *
     * @return the removed first entry of this map,
     * or {@link Optional#empty()} if this map is empty
     */
    Optional<Pair<K, V>> pollFirstEntry();

    /**
     * Removes and returns a key-value mapping associated with
     * the greatest key in this map, or {@link Optional#empty()}if the map is empty.
     *
     * @return the removed last entry of this map,
     * or {@link Optional#empty()} if this map is empty
     */
    Optional<Pair<K, V>> pollLastEntry();

    /**
     * Returns a reverse order view of the mappings contained in this map.
     *
     * <p>The returned map has an ordering equivalent to
     * {@link Collections#reverseOrder(Comparator) Collections.reverseOrder}{@code (comparator())}.
     * The expression {@code m.reversedOrderMap().reversedOrderMap()} returns a
     * view of {@code m} essentially equivalent to {@code m}.
     *
     * @return a reverse order view of this map
     */
    ImmutableNavigableMap<K, V> reversedOrderMap();

    /**
     * Returns a {@link NavigableSet} view of the keys contained in this map.
     * The set's iterator returns the keys in ascending order.
     *
     * @return a navigable set view of the keys in this map
     */
    ImmutableNavigableSet<K> navigableKeySet();

    /**
     * Returns a reverse order {@link NavigableSet} view of the keys contained in this map.
     * The set's iterator returns the keys in descending order.
     *
     * @return a reverse order navigable set view of the keys in this map
     */
    ImmutableNavigableSet<K> reversedOrderKeySet();

    /**
     * Returns a view of the portion of this map whose keys range from
     * {@code fromKey} to {@code toKey}.  If {@code fromKey} and
     * {@code toKey} are equal, the returned map is empty unless
     * {@code fromInclusive} and {@code toInclusive} are both true.
     *
     * @param fromKey       low endpoint of the keys in the returned map
     * @param fromInclusive {@code true} if the low endpoint
     *                      is to be included in the returned view
     * @param toKey         high endpoint of the keys in the returned map
     * @param toInclusive   {@code true} if the high endpoint
     *                      is to be included in the returned view
     * @return a view of the portion of this map whose keys range from
     * {@code fromKey} to {@code toKey}
     */
    ImmutableNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive,
                                       K toKey, boolean toInclusive);

    /**
     * Returns a view of the portion of this map whose keys are less than (or
     * equal to, if {@code inclusive} is true) {@code toKey}.
     *
     * @param toKey     high endpoint of the keys in the returned map
     * @param inclusive {@code true} if the high endpoint
     *                  is to be included in the returned view
     * @return a view of the portion of this map whose keys are less than
     * (or equal to, if {@code inclusive} is true) {@code toKey}
     */
    ImmutableNavigableMap<K, V> headMap(K toKey, boolean inclusive);

    /**
     * Returns a view of the portion of this map whose keys are greater than (or
     * equal to, if {@code inclusive} is true) {@code fromKey}.
     *
     * @param fromKey   low endpoint of the keys in the returned map
     * @param inclusive {@code true} if the low endpoint
     *                  is to be included in the returned view
     * @return a view of the portion of this map whose keys are greater than
     * (or equal to, if {@code inclusive} is true) {@code fromKey}
     */
    ImmutableNavigableMap<K, V> tailMap(K fromKey, boolean inclusive);
}
