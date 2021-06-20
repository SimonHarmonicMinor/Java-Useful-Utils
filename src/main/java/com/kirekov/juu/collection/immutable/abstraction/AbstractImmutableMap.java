package com.kirekov.juu.collection.immutable.abstraction;

import com.kirekov.juu.collection.immutable.ImmutableHashMap;
import com.kirekov.juu.collection.immutable.ImmutableMap;
import com.kirekov.juu.collection.immutable.Pair;
import com.kirekov.juu.lambda.TriFunction;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract immutable map.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public abstract class AbstractImmutableMap<K, V> implements ImmutableMap<K, V> {

  @Override
  public boolean containsPair(Pair<K, V> pair) {
    Objects.requireNonNull(pair);
    return pairSet().contains(pair);
  }

  @Override
  public ImmutableMap<K, V> concatWithOverride(ImmutableMap<K, V> map) {
    return concatWith(map, (key, oldVal, newVal) -> newVal);
  }

  @Override
  public ImmutableMap<K, V> concatWithoutOverride(ImmutableMap<K, V> map) {
    return concatWith(map, (key, oldVal, newVal) -> oldVal);
  }

  @Override
  public ImmutableMap<K, V> concatWith(ImmutableMap<K, V> mapToConcatWith,
      TriFunction<K, V, V, V> overrideBehaviour) {
    Map<K, V> resultMutableMap = toMutableMap();
    mapToConcatWith.forEach(
        (k, v) -> {
          if (this.containsKey(k)) {
            resultMutableMap.put(k, overrideBehaviour.apply(k, this.get(k), v));
          } else {
            resultMutableMap.put(k, v);
          }
        });
    return new ImmutableHashMap<>(resultMutableMap);
  }
}
