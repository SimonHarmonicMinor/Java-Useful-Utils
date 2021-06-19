package com.kirekov.juu.collection.immutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Provides useful reduction operations for java Stream API.
 *
 * @see java.util.stream.Collectors
 * @since 1.0
 */
public class ImmutableCollectors {

  static final Set<Collector.Characteristics> CH_ID =
      Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
  static final Set<Collector.Characteristics> CH_UNORDERED_ID =
      Collections.unmodifiableSet(
          EnumSet.of(
              Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));

  /**
   * Suppresses default constructor, ensuring non-instantiability.
   */
  private ImmutableCollectors() {
  }

  /**
   * Provides collector to {@link ImmutableCollection}.
   *
   * @param collectionFactory function that accepts {@link Iterable} and returns {@link
   *                          ImmutableCollection}
   * @param <T>               the type of the content
   * @param <C>               the type of the return collection
   * @return collector
   * @throws NullPointerException if {@code collectionFactory} is null
   */
  public static <T, C extends ImmutableCollection<T>> Collector<T, List<T>, C> toCollection(
      Function<List<T>, C> collectionFactory) {
    Objects.requireNonNull(collectionFactory);
    return Collector.of(
        ArrayList::new,
        List::add,
        (res1, res2) -> {
          res1.addAll(res2);
          return res1;
        },
        collectionFactory
    );
  }

  /**
   * Provides collector to {@link ImmutableList}.
   *
   * @param <T> the type of the content
   * @return collector
   * @see ImmutableCollectors#toCollection(Function)
   */
  public static <T> Collector<T, List<T>, ImmutableList<T>> toList() {
    return toCollection(list -> new ImmutableArrayList<>(list, false));
  }

  /**
   * Provides collector to {@link ImmutableSet}.
   *
   * @param <T> the type of the content
   * @return collector
   * @see ImmutableCollectors#toCollection(Function)
   */
  public static <T> Collector<T, ?, ImmutableSet<T>> toSet() {
    return toCollection(ImmutableHashSet::new);
  }

  /**
   * Provides collector to {@link ImmutableMap}.
   *
   * @param keyMapper   function that accepts value and produces map key
   * @param valueMapper function that accepts value and produces map value
   * @param <T>         the type of the content
   * @param <K>         the type of the map key
   * @param <V>         the type of the map value
   * @return collector
   * @throws IllegalStateException if keys were duplicated
   * @throws NullPointerException  if {@code keyMapper} of {@code valueMapper} is null
   */
  public static <T, K, V> Collector<T, Map<K, V>, ImmutableMap<K, V>> toMap(
      Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
    Objects.requireNonNull(keyMapper);
    Objects.requireNonNull(valueMapper);
    return Collector.of(
        HashMap::new,
        uniqKeysMapAccumulator(keyMapper, valueMapper),
        uniqKeysMapMerger(),
        map -> new ImmutableHashMap<>(map, false)
    );
  }

  private static <T, K, V> BiConsumer<Map<K, V>, T> uniqKeysMapAccumulator(
      Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
    return (map, element) -> {
      K k = keyMapper.apply(element);
      V v = Objects.requireNonNull(valueMapper.apply(element));
      V u = map.putIfAbsent(k, v);
      if (u != null) {
        throw duplicateKeyException(k, u, v);
      }
    };
  }

  private static <K, V, M extends Map<K, V>> BinaryOperator<M> uniqKeysMapMerger() {
    return (m1, m2) -> {
      for (Map.Entry<K, V> e : m2.entrySet()) {
        K k = e.getKey();
        V v = Objects.requireNonNull(e.getValue());
        V u = m1.putIfAbsent(k, v);
        if (u != null) {
          throw duplicateKeyException(k, u, v);
        }
      }
      return m1;
    };
  }

  private static IllegalStateException duplicateKeyException(Object k, Object u, Object v) {
    return new IllegalStateException(
        String.format("Duplicate key %s (attempted merging values %s and %s)", k, u, v)
    );
  }
}
