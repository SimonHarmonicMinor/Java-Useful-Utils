package com.github.simonharmonicminor.juu.collection.immutable;

import java.util.Objects;
import java.util.stream.Collectors;

class StringUtils {
    private StringUtils() {
    }

    private static <T> String baseCollectionString(ImmutableCollection<T> collection) {
        return collection
                .stream()
                .map(Objects::toString)
                .collect(Collectors.joining(", "));
    }

    static <T> String setToString(ImmutableCollection<T> collection) {
        return String.format(
                "[%s]", baseCollectionString(collection)
        );
    }

    static <T> String listToString(ImmutableCollection<T> collection) {
        return String.format(
                "{%s}", baseCollectionString(collection)
        );
    }
}
