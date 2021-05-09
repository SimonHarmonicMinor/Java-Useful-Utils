package com.kirekov.juu.collection.immutable;

import static com.kirekov.juu.collection.immutable.ImmutableCollectionUtils.pairEquals;

import java.util.Objects;

/**
 * Simple implementation of {@link Pair}.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @since 1.0
 */
class PairImpl<K, V> implements Pair<K, V> {

  private final K key;
  private final V value;

  PairImpl(K key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    return pairEquals(this, o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    return String.format("(key=%s; value=%s)", key, value);
  }
}
