package com.github.simonharmonicminor.juu.collection.immutable;

import com.github.simonharmonicminor.juu.lambda.TriFunction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.simonharmonicminor.juu.collection.immutable.Immutable.listOf;
import static com.github.simonharmonicminor.juu.collection.immutable.Immutable.setOf;
import static com.github.simonharmonicminor.juu.collection.immutable.ImmutableCollectionUtils.mapEquals;

/**
 * An immutable implementation of java native {@link HashMap}
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @see ImmutableMap
 * @see HashMap
 * @see Serializable
 * @since 1.0
 */
public class ImmutableHashMap<K, V> implements ImmutableMap<K, V>, Serializable {
    private static final TriFunction<?, ?, ?, ?> KEEP_OLD = (key, oldVal, newVal) -> oldVal;
    private static final TriFunction<?, ?, ?, ?> KEEP_NEW = (key, oldVal, newVal) -> newVal;

    private final HashMap<K, V> hashMap;
    private final ImmutableSet<K> keys;
    private final ImmutableList<V> values;
    private final ImmutableSet<Pair<K, V>> pairs;

    public ImmutableHashMap(Map<K, V> map) {
        this(map, true);
    }

    ImmutableHashMap(Map<K, V> map, boolean needsCloning) {
        if (needsCloning || !(map instanceof HashMap)) this.hashMap = new HashMap<>(map);
        else this.hashMap = (HashMap<K, V>) map;
        this.keys = setOf(hashMap.keySet());
        this.values = listOf(hashMap.values());
        this.pairs =
                setOf(hashMap.entrySet().stream()
                        .map(e -> Pair.of(e.getKey(), e.getValue()))
                        .collect(Collectors.toList()));
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return hashMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return hashMap.containsValue(value);
    }

    private static <K, V> ImmutableMap<K, V> concatenation(
            ImmutableMap<K, V> immutableMap,
            HashMap<K, V> oldHashMap,
            TriFunction<K, V, V, V> overrideBehaviour) {
        HashMap<K, V> newHashMap = new HashMap<>(oldHashMap);
        immutableMap.forEach(
                (k, v) -> {
                    if (oldHashMap.containsKey(k)) {
                        newHashMap.put(k, overrideBehaviour.apply(k, oldHashMap.get(k), v));
                    } else {
                        newHashMap.put(k, v);
                    }
                });
        return new ImmutableHashMap<>(newHashMap, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ImmutableMap<K, V> concatWithOverride(ImmutableMap<K, V> map) {
        Objects.requireNonNull(map);
        return concatenation(map, this.hashMap, (TriFunction<K, V, V, V>) KEEP_NEW);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ImmutableMap<K, V> concatWithoutOverride(ImmutableMap<K, V> map) {
        Objects.requireNonNull(map);
        return concatenation(map, this.hashMap, (TriFunction<K, V, V, V>) KEEP_OLD);
    }

    @Override
    public ImmutableMap<K, V> concatWith(
            ImmutableMap<K, V> map, TriFunction<K, V, V, V> overrideBehaviour) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(overrideBehaviour);
        return concatenation(map, this.hashMap, overrideBehaviour);
    }

    @Override
    public V get(Object key) {
        return hashMap.get(key);
    }

    @Override
    public ImmutableSet<K> keySet() {
        return keys;
    }

    @Override
    public ImmutableList<V> values() {
        return values;
    }

    @Override
    public ImmutableSet<Pair<K, V>> pairSet() {
        return pairs;
    }

    @Override
    public Map<K, V> toMutableMap() {
        return new HashMap<>(hashMap);
    }

    @Override
    public boolean equals(Object o) {
        return mapEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashMap);
    }
}
