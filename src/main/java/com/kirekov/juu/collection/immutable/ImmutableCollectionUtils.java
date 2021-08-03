package com.kirekov.juu.collection.immutable;

import com.kirekov.juu.monad.Try;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

class ImmutableCollectionUtils {

  private ImmutableCollectionUtils() {
  }

  static <T> Optional<T> tryGetElement(Supplier<T> supplier) {
    Objects.requireNonNull(supplier);
    return Try.of(supplier::get)
        .map(Optional::ofNullable)
        .orElse(Optional.empty());
  }
}
