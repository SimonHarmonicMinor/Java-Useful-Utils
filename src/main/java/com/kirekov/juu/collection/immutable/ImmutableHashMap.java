package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.lambda.TriFunction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        this.keys = Immutable.setOf(hashMap.keySet());
        this.values = Immutable.listOf(hashMap.values());
        this.pairs = ImmutableMapUtils.toPairSet(hashMap.entrySet());
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

    @Override
    public boolean containsPair(Pair<K, V> pair) {
        Objects.requireNonNull(pair);
        return pairSet().contains(pair);
    }

    @Override
    public ImmutableMap<K, V> concatWithOverride(ImmutableMap<K, V> map) {
        Objects.requireNonNull(map);
        return ImmutableMapUtils.concatenationWithOverride(this.hashMap, map);
    }

    @Override
    public ImmutableMap<K, V> concatWithoutOverride(ImmutableMap<K, V> map) {
        Objects.requireNonNull(map);
        return ImmutableMapUtils.concatenationWithoutOverride(this.hashMap, map);
    }

    @Override
    public ImmutableMap<K, V> concatWith(
            ImmutableMap<K, V> map, TriFunction<K, V, V, V> overrideBehaviour) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(overrideBehaviour);
        return ImmutableMapUtils.concatenation(this.hashMap, map, overrideBehaviour);
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
        return ImmutableCollectionUtils.mapEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashMap);
    }
}