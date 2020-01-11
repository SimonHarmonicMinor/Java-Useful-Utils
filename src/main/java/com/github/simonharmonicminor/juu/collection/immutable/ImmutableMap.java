package com.github.simonharmonicminor.juu.collection.immutable;

import com.github.simonharmonicminor.juu.lambda.TriFunction;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Defines an immutable map. Unlike native {@link Map} this interface
 * does not have any methods that can mutate its content. So it can be safely injected to
 * any methods or objects.
 * <p>
 * It is strongly recommended to instantiate this map with only immutable keys and values
 * in order not to face with some unexpected mutations.
 * <p>
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @see Map
 * @since 1.0
 */
public interface ImmutableMap<K, V> {
    /**
     * @return size of the map (keys count)
     */
    int size();

    /**
     * @return true if size is zero, otherwise false
     */
    boolean isEmpty();

    /**
     * @return true if size is not zero, otherwise false
     */
    default boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * @param key the key whose presence is to be tested
     * @return true if map contains key, otherwise false
     */
    boolean containsKey(K key);

    /**
     * @param key the key whose presence is to be tested
     * @return true if map does not contain key, otherwise false
     */
    default boolean notContainsKey(K key) {
        return !containsKey(key);
    }

    /**
     * @param value the value whose presence is to be tested
     * @return true if map contains value, otherwise false
     */
    boolean containsValue(V value);

    /**
     * @param value the value whose presence is to be tested
     * @return true if map does not contain value, otherwise false
     */
    default boolean notContainsValue(V value) {
        return !containsValue(value);
    }

    /**
     * In case of occurring the same keys in two maps the value from the given map
     * will be added to final result.
     *
     * @param map the map whose pairs will be added to the current one
     * @return new map with merged pairs
     * @throws NullPointerException if {@code map} is null
     * @see ImmutableMap#concatWith(ImmutableMap, TriFunction)
     */
    ImmutableMap<K, V> concatWithOverride(ImmutableMap<K, V> map);

    /**
     * In case of occurring the same keys in two maps the value from the current map
     * will be added to final result.
     *
     * @param map the map whose pairs will be added to the current one
     * @return new map with merged pairs
     * @throws NullPointerException if {@code map} is null
     * @see ImmutableMap#concatWith(ImmutableMap, TriFunction)
     */
    ImmutableMap<K, V> concatWithoutOverride(ImmutableMap<K, V> map);

    /**
     * Adds all pairs of keys and values from current map and the given map to the new one
     * and returns new instance.
     * <p>
     * If these two maps have any equal keys, {@code overrideBehaviour} function will be called
     * to resolve the conflict.
     *
     * @param map               the map whose pairs will be added to the current one
     * @param overrideBehaviour function is called every time when two same keys in two maps is found.
     *                          The first param is the key.
     *                          The second param is the value from the current map.
     *                          The third param is the value from the given map.
     *                          Function returns the value that will be added to the final result.
     * @return new map with merged pairs
     * @throws NullPointerException if {@code map} is null or {@code overrideBehaviour} is null
     */
    ImmutableMap<K, V> concatWith(ImmutableMap<K, V> map, TriFunction<K, V, V, V> overrideBehaviour);

    /**
     * Same behaviour as {@link ImmutableMap#concatWithOverride(ImmutableMap)}
     */
    ImmutableMap<K, V> concatWithOverride(Map<K, V> map);

    /**
     * Same behaviour as {@link ImmutableMap#concatWithoutOverride(ImmutableMap)}
     */
    ImmutableMap<K, V> concatWithoutOverride(Map<K, V> map);

    /**
     * Same behaviour as {@link ImmutableMap#concatWith(ImmutableMap, TriFunction)}
     */
    ImmutableMap<K, V> concatWith(Map<K, V> map, TriFunction<K, V, V, V> overrideBehaviour);

    /**
     * @param key the key whose associated value is to be found
     * @return the value that associates with the key if it is present, otherwise null
     */
    V get(K key);

    /**
     * @return immutable set of keys, that map contains
     */
    ImmutableSet<K> keySet();

    /**
     * @return immutable set of values, that map contains
     */
    ImmutableList<V> values();

    /**
     * @return immutable set of (key -&gt; value) associations
     */
    ImmutableSet<Pair<K, V>> pairSet();

    /**
     * Converts this immutable map to mutable one.
     * Creates new object, so its mutations does not affect the mutable one.
     *
     * @return new mutable map
     */
    Map<K, V> toMutableMap();

    /**
     * Overrides method from {@link Object#equals(Object)}.
     * Must be implemented.
     */
    boolean equals(Object o);

    /**
     * Overrides method from {@link Object#hashCode()}.
     * Must be implemented.
     */
    int hashCode();

    /**
     * If value contains the key, returns associated value, otherwise returns {@code defaultValue}
     *
     * @param key          the key whose associated value is to be found
     * @param defaultValue the value that returns in case of key absence
     * @return the value or the default one
     */
    default V getOrDefault(K key, V defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }

    /**
     * Traverses all pairs (key -&gt; value) that map contains
     *
     * @param action the procedure that invokes on each iteration
     */
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Pair<K, V> pair : pairSet()) {
            K k = pair.getKey();
            V v = pair.getValue();
            action.accept(k, v);
        }
    }
}
