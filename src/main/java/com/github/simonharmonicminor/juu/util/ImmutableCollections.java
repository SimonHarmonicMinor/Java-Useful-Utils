package com.github.simonharmonicminor.juu.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Static class for retrieving empty sets and lists or creating new
 * collections from arrays
 *
 * @see Collections
 * @since 1.0
 */
public class ImmutableCollections {
    private static final ImmutableList<?> EMPTY_LIST = new ImmutableArrayList<>(Collections.emptyList());
    private static final ImmutableSet<?> EMPTY_SET = new ImmutableHashSet<>(Collections.emptyList());

    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private ImmutableCollections() {
    }

    /**
     * Returns empty immutable list. Does not create the new one,
     * returns the same instance every time.
     *
     * @param <T> the type of the list content
     * @return empty list
     */
    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> emptyList() {
        return (ImmutableList<T>) EMPTY_LIST;
    }

    /**
     * Returns empty immutable set. Does not create the new one,
     * returns the same instance every time
     *
     * @param <T> the type of the set content
     * @return empty set
     */
    @SuppressWarnings("unchecked")
    public static <T> ImmutableSet<T> emptySet() {
        return (ImmutableSet<T>) EMPTY_SET;
    }

    /**
     * Creates new immutable list from given elements
     *
     * @param elements array of elements
     * @param <T>      the type of the element
     * @return new immutable list
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> ImmutableList<T> listOf(T... elements) {
        Objects.requireNonNull(elements);
        return new ImmutableArrayList<>(Arrays.stream(elements).collect(Collectors.toList()));
    }


    /**
     * Creates new immutable set from given elements
     *
     * @param elements array of elements
     * @param <T>      the type of the element
     * @return new immutable set
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> ImmutableSet<T> setOf(T... elements) {
        Objects.requireNonNull(elements);
        return new ImmutableHashSet<>(Arrays.stream(elements).collect(Collectors.toSet()));
    }
}
