package com.github.simonharmonicminor.juu.collection.immutable;

import com.github.simonharmonicminor.juu.monad.Try;

import java.util.Objects;
import java.util.stream.Collectors;

class ImmutableCollectionUtils {
    private ImmutableCollectionUtils() {
    }

    private static <T> String baseCollectionString(ImmutableCollection<T> collection) {
        return collection
                .stream()
                .map(Objects::toString)
                .collect(Collectors.joining(", "));
    }

    static <T> String setToString(ImmutableCollection<T> collection) {
        return String.format(
                "{%s}", baseCollectionString(collection)
        );
    }

    static <T> String listToString(ImmutableCollection<T> collection) {
        return String.format(
                "[%s]", baseCollectionString(collection)
        );
    }

    static boolean listEquals(ImmutableList<?> current, Object other) {
        if (current == other)
            return true;
        if (!(other instanceof ImmutableList))
            return false;
        ImmutableList<?> otherList = (ImmutableList<?>) other;
        if (otherList.size() != current.size())
            return false;
        for (int i = 0; i < current.size(); i++) {
            if (!Objects.equals(current.get(i), otherList.get(i)))
                return false;
        }
        return true;
    }

    static boolean setEquals(ImmutableSet<?> current, Object other) {
        if (current == other)
            return true;
        if (!(other instanceof ImmutableSet))
            return false;
        ImmutableSet<?> otherSet = (ImmutableSet<?>) other;
        if (current.size() != otherSet.size())
            return false;
        for (Object obj : otherSet) {
            if (Try.of(() -> current.notContains(obj)).orElse(false))
                return false;
        }
        return true;
    }

    static boolean pairEquals(Pair<?, ?> current, Object other) {
        if (current == other)
            return true;
        if (!(other instanceof Pair))
            return false;
        Pair<?, ?> otherPair = (Pair<?, ?>) other;
        return Objects.equals(current.getKey(), otherPair.getKey())
                && Objects.equals(current.getValue(), otherPair.getValue());
    }
}
