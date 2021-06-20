package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.collection.immutable.abstraction.AbstractImmutableMap;
import com.kirekov.juu.monad.Try;
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
 * An immutable implementation of java native {@link TreeMap}.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @see ImmutableNavigableMap
 * @see ImmutableMap
 * @see TreeMap
 * @since 1.1
 */
public final class ImmutableTreeMap<K, V> extends AbstractImmutableMap<K, V> implements
    ImmutableNavigableMap<K, V> {

  private final NavigableMap<K, V> navigableMap;
  private final ImmutableSet<K> keys;
  private final ImmutableList<V> values;
  private final ImmutableSet<Pair<K, V>> pairs;

  /**
   * Creates new {@linkplain ImmutableTreeMap} from the given {@linkplain Map} and {@linkplain
   * Comparator}.
   *
   * @param map        the source map
   * @param comparator the comparator
   * @param <K>        the type of the key
   * @param <V>        the type of the value
   * @return immutable tree map
   * @throws NullPointerException if {@code map} or {@code comparator} is null
   */
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
    return new ImmutableTreeMap<>(sortedMap);
  }

  ImmutableTreeMap(Map<K, V> map, Comparator<? super K> comparator) {
    Objects.requireNonNull(map);
    navigableMap = new TreeMap<>(comparator);
    map.forEach(navigableMap::put);
    keys = Immutable.setOf(navigableMap.keySet());
    values = Immutable.listOf(navigableMap.values());
    pairs = ImmutableMapUtils.toPairSet(navigableMap.entrySet());
  }

  ImmutableTreeMap(SortedMap<K, V> sortedMap) {
    navigableMap = new TreeMap<>(sortedMap);
    keys = Immutable.setOf(navigableMap.keySet());
    values = Immutable.listOf(navigableMap.values());
    pairs = ImmutableMapUtils.toPairSet(navigableMap.entrySet());
  }

  @Override
  public Optional<Pair<K, V>> lowerPair(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableMap.lowerEntry(key))
        .map(Pair::of);
  }

  @Override
  public Optional<K> lowerKey(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableMap.lowerKey(key));
  }

  @Override
  public Optional<Pair<K, V>> floorPair(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableMap.floorEntry(key))
        .map(Pair::of);
  }

  @Override
  public Optional<K> floorKey(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableMap.floorKey(key));
  }

  @Override
  public Optional<Pair<K, V>> ceilingPair(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableMap.ceilingEntry(key))
        .map(Pair::of);
  }

  @Override
  public Optional<K> ceilingKey(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableMap.ceilingKey(key));
  }

  @Override
  public Optional<Pair<K, V>> higherPair(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableMap.higherEntry(key))
        .map(Pair::of);
  }

  @Override
  public Optional<K> higherKey(K key) {
    return ImmutableCollectionUtils.tryGetElement(() -> navigableMap.higherKey(key));
  }

  @Override
  public Optional<Pair<K, V>> firstPair() {
    return ImmutableCollectionUtils.tryGetElement(navigableMap::firstEntry)
        .map(Pair::of);
  }

  @Override
  public Optional<Pair<K, V>> lastPair() {
    return ImmutableCollectionUtils.tryGetElement(navigableMap::lastEntry)
        .map(Pair::of);
  }

  @Override
  public ImmutableNavigableMap<K, V> reversedOrderMap() {
    return new ImmutableTreeMap<>(navigableMap.descendingMap());
  }

  @Override
  public ImmutableNavigableSet<K> navigableKeySet() {
    return new ImmutableTreeSet<>(navigableMap.navigableKeySet());
  }

  @Override
  public ImmutableNavigableSet<K> reversedOrderKeySet() {
    return new ImmutableTreeSet<>(navigableMap.descendingKeySet());
  }

  private ImmutableNavigableMap<K, V> tryGetSubMap(Supplier<ImmutableNavigableMap<K, V>> supplier) {
    return Try.of(supplier::get)
        .orElse(new ImmutableTreeMap<>(Collections.emptyMap(), navigableMap.comparator()));
  }

  @Override
  public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            navigableMap.subMap(fromKey, toKey)
        ));
  }

  @Override
  public ImmutableNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey,
      boolean toInclusive) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            navigableMap.subMap(fromKey, fromInclusive, toKey, toInclusive)
        ));
  }

  @Override
  public ImmutableSortedMap<K, V> headMap(K toKey) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            navigableMap.headMap(toKey)
        ));
  }

  @Override
  public ImmutableNavigableMap<K, V> headMap(K toKey, boolean inclusive) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            navigableMap.headMap(toKey, inclusive)
        ));
  }

  @Override
  public ImmutableSortedMap<K, V> tailMap(K fromKey) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            navigableMap.tailMap(fromKey)
        ));
  }

  @Override
  public ImmutableNavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
    return tryGetSubMap(() ->
        new ImmutableTreeMap<>(
            navigableMap.tailMap(fromKey, inclusive)
        ));
  }

  @Override
  public NavigableMap<K, V> toMutableNavigableMap() {
    return new TreeMap<>(navigableMap);
  }

  @Override
  public Comparator<? super K> comparator() {
    return navigableMap.comparator();
  }

  @Override
  public Optional<K> firstKey() {
    return ImmutableCollectionUtils.tryGetElement(navigableMap::firstKey);
  }

  @Override
  public Optional<K> lastKey() {
    return ImmutableCollectionUtils.tryGetElement(navigableMap::lastKey);
  }

  @Override
  public SortedMap<K, V> toMutableSortedMap() {
    return toMutableNavigableMap();
  }

  @Override
  public int size() {
    return navigableMap.size();
  }

  @Override
  public boolean isEmpty() {
    return navigableMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return Try.of(() -> navigableMap.containsKey(key))
        .orElse(false);
  }

  @Override
  public boolean containsValue(Object value) {
    return Try.of(() -> navigableMap.containsValue(value))
        .orElse(false);
  }

  @Override
  public V get(Object key) {
    return Try.of(() -> navigableMap.get(key))
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
    return new HashMap<>(this.navigableMap);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImmutableTreeMap<?, ?> that = (ImmutableTreeMap<?, ?>) o;
    return navigableMap.equals(that.navigableMap);
  }

  @Override
  public int hashCode() {
    return navigableMap.hashCode();
  }
}
