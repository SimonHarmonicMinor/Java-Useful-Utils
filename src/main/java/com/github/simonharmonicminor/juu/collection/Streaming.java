package com.github.simonharmonicminor.juu.collection;

import java.util.stream.Stream;

/**
 * Allows to convert an object to {@link Stream}
 *
 * @param <T> the type of the stream content
 */
@FunctionalInterface
public interface Streaming<T> {
    /**
     * @return stream of object content
     */
    Stream<T> stream();
}
