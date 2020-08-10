package com.kirekov.juu.collection;

import java.util.stream.Stream;

/**
 * Allows to convert object to parallel stream
 *
 * @param <T> the type of the stream content
 * @since 1.0
 */
public interface ParallelStreaming<T> extends Streaming<T> {
    /**
     * @return parallel stream of object content
     */
    Stream<T> parallelStream();
}
