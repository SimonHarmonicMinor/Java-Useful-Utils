package com.kirekov.juu.collection.immutable.abstraction;

import com.kirekov.juu.collection.immutable.ImmutableList;

/**
 * Abstract immutable list.
 *
 * @param <T> the type of the content
 */
public abstract class AbstractImmutableList<T> implements ImmutableList<T> {

  @Override
  public String toString() {
    if (isEmpty()) {
      return "[]";
    }
    StringBuilder builder = new StringBuilder().append("[");
    for (int i = 0; i < size(); i++) {
      builder.append(get(i));
      if (i == size() - 1) {
        builder.append("]");
      } else {
        builder.append(", ");
      }
    }
    return builder.toString();
  }
}
