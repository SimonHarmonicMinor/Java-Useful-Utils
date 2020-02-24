package com.github.simonharmonicminor.juu.collection.immutable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Static class for retrieving empty sets and lists or instantiating immutable collections. The
 * class caches empty lists and sets in order not to create new objects unless it is necessary. So,
 * it is recommended to use methods from this class, but not to create immutable collections via
 * their constructors.
 *
 * @see Collections
 * @see ImmutableCollection
 * @see ImmutableList
 * @see ImmutableSet
 * @see ImmutableMap
 * @see Pair
 * @since 1.0
 */
public class Immutable {
    private static final ImmutableArrayList<?> EMPTY_ARRAY_LIST =
            new ImmutableArrayList<>(Collections.emptyList());
    private static final ImmutableHashSet<?> EMPTY_HASH_SET =
            new ImmutableHashSet<>(Collections.emptyList());
    private static final ImmutableHashMap<?, ?> EMPTY_HASH_MAP =
            new ImmutableHashMap<>(Collections.emptyMap());

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private Immutable() {
    }

    /**
     * Returns empty immutable list. Does not create the new one, returns the same instance every
     * time.
     *
     * @param <T> the type of the list content
     * @return empty list
     */
    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> emptyList() {
        return (ImmutableList<T>) EMPTY_ARRAY_LIST;
    }

    /**
     * Returns empty immutable set. Does not create the new one, returns the same instance every time.
     *
     * @param <T> the type of the set content
     * @return empty set
     */
    @SuppressWarnings("unchecked")
    public static <T> ImmutableSet<T> emptySet() {
        return (ImmutableSet<T>) EMPTY_HASH_SET;
    }

    /**
     * Returns empty map. Does not create the new one, returns the same instance every time
     *
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @return empty map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> ImmutableMap<K, V> emptyMap() {
        return (ImmutableMap<K, V>) EMPTY_HASH_MAP;
    }


    private static <T> ImmutableList<T> listOf(Iterable<T> elements, boolean needsCloning) {
        Objects.requireNonNull(elements);
        if (!elements.iterator().hasNext()) return emptyList();
        return new ImmutableArrayList<>(elements, needsCloning);
    }

    /**
     * Creates new immutable list from given elements. If array is empty, returns {@link
     * Immutable#emptyList()}. This is the preferred way of creating immutable lists, unless you need
     * particular implementation.
     *
     * @param elements array of elements
     * @param <T>      the type of the element
     * @return immutable list
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> ImmutableList<T> listOf(T... elements) {
        return listOf(Arrays.stream(elements).collect(Collectors.toList()), false);
    }

    /**
     * Creates new immutable list from given elements. If iterable has no elements, returns {@link
     * Immutable#emptyList()}. This is the preferred way of creating immutable lists, unless you need
     * particular implementation.
     *
     * @param elements iterable elements
     * @param <T>      the type of the element
     * @return immutable list
     * @throws NullPointerException if {@code elements} is null
     */
    public static <T> ImmutableList<T> listOf(Iterable<T> elements) {
        return listOf(elements, true);
    }

    static <T> ImmutableList<T> listOfWithoutCloning(Iterable<T> elements) {
        return listOf(elements, false);
    }

    private static <T> ImmutableSet<T> setOf(Iterable<T> elements, boolean needsCloning) {
        Objects.requireNonNull(elements);
        if (!elements.iterator().hasNext()) return emptySet();
        return new ImmutableHashSet<>(elements, needsCloning);
    }

    /**
     * Creates new immutable set from given elements. If array is empty, returns {@link
     * Immutable#emptySet()}. This is the preferred way of creating immutable sets, unless you need
     * particular implementation.
     *
     * @param elements array of elements
     * @param <T>      the type of the element
     * @return immutable set
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> ImmutableSet<T> setOf(T... elements) {
        Objects.requireNonNull(elements);
        return setOf(Arrays.stream(elements).collect(Collectors.toSet()), false);
    }

    /**
     * Creates new immutable set from given elements. If iterable has no elements, returns {@link
     * Immutable#emptySet()}. This is the preferred way of creating immutable sets, unless you need
     * particular implementation.
     *
     * @param elements iterable elements
     * @param <T>      the type of the element
     * @return immutable set
     * @throws NullPointerException if {@code elements} is null
     */
    public static <T> ImmutableSet<T> setOf(Iterable<T> elements) {
        return setOf(elements, true);
    }

    static <T> ImmutableSet<T> setOfWithoutCloning(Iterable<T> elements) {
        return setOf(elements, false);
    }

    public static <K, V> ImmutableMap<K, V> mapOf(Map<K, V> map) {
        Objects.requireNonNull(map);
        return new ImmutableHashMap<>(map);
    }

    public static <K, V> ImmutableMap<K, V> mapOf(Iterable<Pair<K, V>> pairs) {
        Objects.requireNonNull(pairs);
        if (!pairs.iterator().hasNext()) return emptyMap();
        HashMap<K, V> hashMap = new HashMap<>();
        for (Pair<K, V> p : pairs) {
            hashMap.put(p.getKey(), p.getValue());
        }
        return new ImmutableHashMap<>(hashMap, false);
    }

    public static <K, V> ImmutableMap<K, V> mapOf(K k, V v) {
        return mapOf(Collections.singletonList(Pair.of(k, v)));
    }

    public static <K, V> ImmutableMap<K, V> mapOf(K k1, V v1, K k2, V v2) {
        return mapOf(Arrays.asList(Pair.of(k1, v1), Pair.of(k2, v2)));
    }

    public static <K, V> ImmutableMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        return mapOf(Arrays.asList(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3)));
    }

    public static <K, V> ImmutableMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return mapOf(Arrays.asList(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3), Pair.of(k4, v4)));
    }

    public static <K, V> ImmutableMap<K, V> mapOf(
            K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return mapOf(
                Arrays.asList(
                        Pair.of(k1, v1),
                        Pair.of(k2, v2),
                        Pair.of(k3, v3),
                        Pair.of(k4, v4),
                        Pair.of(k5, v5)));
    }
}
