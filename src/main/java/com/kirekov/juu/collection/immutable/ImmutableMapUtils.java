package com.kirekov.juu.collection.immutable;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class ImmutableMapUtils {

  private ImmutableMapUtils() {
  }

  static <K, V> ImmutableSet<Pair<K, V>> toPairSet(Set<Map.Entry<K, V>> entrySet) {
    Objects.requireNonNull(entrySet);
    return Immutable.setOf(entrySet.stream()
        .map(e -> Pair.of(e.getKey(), e.getValue()))
        .collect(Collectors.toList()));
  }
}
