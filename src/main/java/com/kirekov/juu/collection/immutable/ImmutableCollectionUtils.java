package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.monad.Try;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ImmutableCollectionUtils {

  private ImmutableCollectionUtils() {
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
