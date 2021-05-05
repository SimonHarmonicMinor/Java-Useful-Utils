package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.lambda.TriFunction;
import com.kirekov.juu.monad.Try;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * An immutable implementation of java native {@link TreeMap}
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @see ImmutableNavigableMap
 * @see ImmutableMap
 * @see Serializable
 * @see TreeMap
 * @since 1.1
 */
public class ImmutableTreeMap<K, V> implements ImmutableNavigableMap<K, V>, Serializable {

  private final TreeMap<K, V> treeMap;
  private final ImmutableSet<K> keys;
  private final ImmutableList<V> values;
  private final ImmutableSet<Pair<K, V>> pairs;

  public static <K, V> ImmutableTreeMap<K, V> of(Map<K, V> map, Comparator<? super K> comparator) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(comparator);
    return new ImmutableTreeMap<>(map, comparator);
  }

  public static <K extends Comparable<K>, V> ImmutableTreeMap<K, V> of(Map<K, V> map) {
    Objects.requireNonNull(map);
    return new ImmutableTreeMap<>(map, null);
  }

  public static <K, V> ImmutableTreeMap<K, V> ofSortedMap(SortedMap<K, V> sortedMap) {
    Objects.requireNonNull(sortedMap);
    return new ImmutableTreeMap<>(sortedMap, true);
  }

  ImmutableTreeMap(Map<K, V> map, Comparator<? super K> comparator) {
    Objects.requireNonNull(map);
    treeMap = new TreeMap<>(comparator);
    map.forEach(treeMap::put);
    keys = Immutable.setOf(treeMap.keySet());
    values = Immutable.listOf(treeMap.values());
    pairs = ImmutableMapUtils.toPairSet(treeMap.entrySet());
  }

  ImmutableTreeMap(SortedMap<K, V> sortedMap, boolean needsCloning) {
    if (sortedMap instanceof TreeMap) {
      this.treeMap = needsCloning ? new TreeMap<>(sortedMap) : (TreeMap<K, V>) sortedMap;
    } else {
      this.treeMap = new TreeMap<>(sortedMap);
    }
    keys = Immutable.setOf(treeMap.keySet());
    values = Immutable.listOf(treeMap.values());
    pairs = ImmutableMapUtils.toPairSet(treeMap.entrySet());
  }

  @Override
  public Optional<Pair<K, V>> lowerPair(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeMap.lowerEntry(key))
        .map(Pair::of);
  }

  @Override
  public Optional<K> lowerKey(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeMap.lowerKey(key));
  }

  @Override
  public Optional<Pair<K, V>> floorPair(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeMap.floorEntry(key))
        .map(Pair::of);
  }

  @Override
  public Optional<K> floorKey(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeMap.floorKey(key));
  }

  @Override
  public Optional<Pair<K, V>> ceilingPair(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeMap.ceilingEntry(key))
        .map(Pair::of);
  }

  @Override
  public Optional<K> ceilingKey(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeMap.ceilingKey(key));
  }

  @Override
  public Optional<Pair<K, V>> higherPair(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeMap.higherEntry(key))
        .map(Pair::of);
  }

  @Override
  public Optional<K> higherKey(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> treeMap.higherKey(key));
  }

  @Override
  public Optional<Pair<K, V>> firstPair() {
    return ImmutableCollectionUtils.tryGetElement(treeMap::firstEntry)
        .map(Pair::of);
  }

  @Override
  public Optional<Pair<K, V>> lastPair() {
    return ImmutableCollectionUtils.tryGetElement(treeMap::lastEntry)
        .map(Pair::of);
  }

  @Override
  public ImmutableNavigableMap<K, V> reversedOrderMap() {
    return new ImmutableTreeMap<>(treeMap.descendingMap(), false);
  }

  @Override
  public ImmutableNavigableSet<K> navigableKeySet() {
    return new ImmutableTreeSet<>(treeMap.navigableKeySet(), false);
  }

  @Override
  public ImmutableNavigableSet<K> reversedOrderKeySet() {
    return new ImmutableTreeSet<>(treeMap.descendingKeySet(), false);
  }

  private ImmutableNavigableMap<K, V> tryGetSubMap(Supplier<ImmutableNavigableMap<K, V>> supplier) {
    return Try.of(supplier::get)
        .orElse(new ImmutableTreeMap<>(Collections.emptyMap(), treeMap.comparator()));
  }

  @Override
  public ImmutableNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey,
      boolean toInclusive) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            treeMap.subMap(fromKey, fromInclusive, toKey, toInclusive),
            false
        ));
  }

  @Override
  public ImmutableNavigableMap<K, V> headMap(K toKey, boolean inclusive) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            treeMap.headMap(toKey, inclusive),
            false
        ));
  }

  @Override
  public ImmutableNavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            treeMap.tailMap(fromKey, inclusive),
            false
        ));
  }

  @Override
  public NavigableMap<K, V> toMutableNavigableMap() {
    return new TreeMap<>(treeMap);
  }

  @Override
  public Comparator<? super K> comparator() {
    return treeMap.comparator();
  }

  @Override
  public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            treeMap.subMap(fromKey, toKey),
            false
        ));
  }

  @Override
  public ImmutableSortedMap<K, V> headMap(K toKey) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            treeMap.headMap(toKey),
            false
        ));
  }

  @Override
  public ImmutableSortedMap<K, V> tailMap(K fromKey) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            treeMap.tailMap(fromKey),
            false
        ));
  }

  @Override
  public Optional<K> firstKey() {
    return ImmutableCollectionUtils.tryGetElement(treeMap::firstKey);
  }

  @Override
  public Optional<K> lastKey() {
    return ImmutableCollectionUtils.tryGetElement(treeMap::lastKey);
  }

  @Override
  public SortedMap<K, V> toMutableSortedMap() {
    return toMutableNavigableMap();
  }

  @Override
  public int size() {
    return treeMap.size();
  }

  @Override
  public boolean isEmpty() {
    return treeMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return Try.of(() -> treeMap.containsKey(key))
        .orElse(false);
  }

  @Override
  public boolean containsValue(Object value) {
    return Try.of(() -> treeMap.containsValue(value))
        .orElse(false);
  }

  @Override
  public boolean containsPair(Pair<K, V> pair) {
    Objects.requireNonNull(pair);
    return pairSet().contains(pair);
  }

  @Override
  public ImmutableMap<K, V> concatWithOverride(ImmutableMap<K, V> map) {
    Objects.requireNonNull(map);
    return ImmutableMapUtils.concatenationWithOverride(this.treeMap, map);
  }

  @Override
  public ImmutableMap<K, V> concatWithoutOverride(ImmutableMap<K, V> map) {
    Objects.requireNonNull(map);
    return ImmutableMapUtils.concatenationWithoutOverride(this.treeMap, map);
  }

  @Override
  public ImmutableMap<K, V> concatWith(ImmutableMap<K, V> map,
      TriFunction<K, V, V, V> overrideBehaviour) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(overrideBehaviour);
    return ImmutableMapUtils.concatenation(this.treeMap, map, overrideBehaviour);
  }

  @Override
  public V get(Object key) {
    return Try.of(() -> treeMap.get(key))
        .orElse(null);
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
    return new HashMap<>(this.treeMap);
  }
}
