package com.kirekov.juu.collection;

import java.util.stream.Stream;

/**
 * Allows to convert an object to {@link Stream}.
 *
 * @param <T> the type of the stream content
 * @since 1.0
 */
@FunctionalInterface
public interface Streaming<T> {

  /**
   * Get {@linkplain Stream} from the object.
   *
   * @return stream of object content
   */
  Stream<T> stream();
}
