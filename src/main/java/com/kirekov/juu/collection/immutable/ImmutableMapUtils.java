package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.lambda.TriFunction;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class ImmutableMapUtils {

  private static final TriFunction<?, ?, ?, ?> KEEP_OLD = (key, oldVal, newVal) -> oldVal;
  private static final TriFunction<?, ?, ?, ?> KEEP_NEW = (key, oldVal, newVal) -> newVal;

  private ImmutableMapUtils() {
  }

  static <K, V> ImmutableSet<Pair<K, V>> toPairSet(Set<Map.Entry<K, V>> entrySet) {
    Objects.requireNonNull(entrySet);
    return Immutable.setOf(entrySet.stream()
        .map(e -> Pair.of(e.getKey(), e.getValue()))
        .collect(Collectors.toList()));
  }

  @SuppressWarnings("unchecked")
  static <K, V> ImmutableMap<K, V> concatenationWithOverride(
      Map<K, V> oldMutableMap,
      ImmutableMap<K, V> mapToConcat) {
    return concatenation(oldMutableMap, mapToConcat, (TriFunction<K, V, V, V>) KEEP_NEW);
  }


  @SuppressWarnings("unchecked")
  static <K, V> ImmutableMap<K, V> concatenationWithoutOverride(
      Map<K, V> oldMutableMap,
      ImmutableMap<K, V> mapToConcat) {
    return concatenation(oldMutableMap, mapToConcat, (TriFunction<K, V, V, V>) KEEP_OLD);
  }

  static <K, V> ImmutableMap<K, V> concatenation(
      Map<K, V> oldMutableMap,
      ImmutableMap<K, V> mapToConcat,
      TriFunction<K, V, V, V> overrideBehaviour) {
    HashMap<K, V> newHashMap = new HashMap<>(oldMutableMap);
    mapToConcat.forEach(
        (k, v) -> {
          if (oldMutableMap.containsKey(k)) {
            newHashMap.put(k, overrideBehaviour.apply(k, oldMutableMap.get(k), v));
          } else {
            newHashMap.put(k, v);
          }
        });
    return new ImmutableHashMap<>(newHashMap);
  }
}
