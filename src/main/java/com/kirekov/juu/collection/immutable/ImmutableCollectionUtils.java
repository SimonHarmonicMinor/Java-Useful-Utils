package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.monad.Try;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class ImmutableCollectionUtils {

  private ImmutableCollectionUtils() {
  }

  private static <T> String baseCollectionString(ImmutableCollection<T> collection) {
    return collection.stream().map(Objects::toString).collect(Collectors.joining(", "));
  }

  static <T> String setToString(ImmutableCollection<T> collection) {
    return String.format("{%s}", baseCollectionString(collection));
  }

  static <T> String listToString(ImmutableCollection<T> collection) {
    return String.format("[%s]", baseCollectionString(collection));
  }

  static boolean listEquals(ImmutableList<?> current, Object other) {
    if (current == other) {
      return true;
    }
    if (!(other instanceof ImmutableList)) {
      return false;
    }
    ImmutableList<?> otherList = (ImmutableList<?>) other;
    if (otherList.size() != current.size()) {
      return false;
    }
    for (int i = 0; i < current.size(); i++) {
      if (!Objects.equals(current.get(i), otherList.get(i))) {
        return false;
      }
    }
    return true;
  }

  static boolean setEquals(ImmutableSet<?> current, Object other) {
    if (current == other) {
      return true;
    }
    if (!(other instanceof ImmutableSet)) {
      return false;
    }
    ImmutableSet<?> otherSet = (ImmutableSet<?>) other;
    if (current.size() != otherSet.size()) {
      return false;
    }

    BiConsumer<ImmutableSet<?>, ImmutableSet<?>> checkForEquality =
        (set1, set2) -> {
          for (Object obj : set1) {
            if (set2.notContains(obj)) {
              throw new RuntimeException("not equal");
            }
          }
        };
    checkForEquality.accept(current, otherSet);
    checkForEquality.accept(otherSet, current);
    return true;
  }

  static boolean pairEquals(Pair<?, ?> current, Object other) {
    if (current == other) {
      return true;
    }
    if (!(other instanceof Pair)) {
      return false;
    }
    Pair<?, ?> otherPair = (Pair<?, ?>) other;
    return Objects.equals(current.getKey(), otherPair.getKey())
        && Objects.equals(current.getValue(), otherPair.getValue());
  }

  static boolean mapEquals(ImmutableMap<?, ?> current, Object other) {
    if (current == other) {
      return true;
    }
    if (!(other instanceof ImmutableMap)) {
      return false;
    }
    ImmutableMap<?, ?> otherMap = (ImmutableMap<?, ?>) other;
    if (current.size() != otherMap.size()) {
      return false;
    }

    Consumer<ImmutableMap<?, ?>> checkForEquality = map ->
        map.forEach((k, v) -> {
          if (current.notContainsKey(k)
              || otherMap.notContainsKey(k)
              || current.get(k) != otherMap.get(k)) {
            throw new RuntimeException("not equal");
          }
        });

    return Try.of(() -> {
      checkForEquality.accept(current);
      checkForEquality.accept(otherMap);
      return true;
    }).orElse(false);
  }

  static <T> Optional<T> tryGetElement(Supplier<T> supplier) {
    Objects.requireNonNull(supplier);
    return Try.of(supplier::get)
        .map(Optional::ofNullable)
        .orElse(Optional.empty());
  }
}
