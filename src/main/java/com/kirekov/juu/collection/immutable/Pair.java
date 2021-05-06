package com.kirekov.juu.collection.immutable;

import java.util.Map;
import java.util.Objects;

/**
 * Immutable pair that keeps key and value. Used by {@link ImmutableMap}.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @since 1.0
 */
public interface Pair<K, V> {

  K getKey();

  V getValue();

  /**
   * Overrides method from {@link Object#equals(Object)}. Must be implemented.
   */
  boolean equals(Object o);

  /**
   * Overrides method from {@link Object#hashCode()}. Must be implemented.
   */
  int hashCode();

  /**
   * Instantiates new pair.
   *
   * @param key   the key
   * @param value the value
   * @param <K>   the type of the key
   * @param <V>   the type of the value
   * @return new pair
   */
  static <K, V> Pair<K, V> of(K key, V value) {
    return new PairImpl<>(key, value);
  }

  /**
   * Instantiates new pair.
   *
   * @param entry entry which key and value will be used
   * @param <K>   the type of the key
   * @param <V>   the type of the value
   * @return new pair
   * @throws NullPointerException if {@code entry} is null
   */
  static <K, V> Pair<K, V> of(Map.Entry<K, V> entry) {
    Objects.requireNonNull(entry);
    return new PairImpl<>(entry.getKey(), entry.getValue());
  }
}
