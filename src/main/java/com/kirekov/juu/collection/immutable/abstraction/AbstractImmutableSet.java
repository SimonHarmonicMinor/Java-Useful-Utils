package com.kirekov.juu.collection.immutable.abstraction;

import com.kirekov.juu.collection.immutable.ImmutableSet;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract immutable set.
 *
 * @param <T> the type of the content
 */
public abstract class AbstractImmutableSet<T> implements ImmutableSet<T> {

  @Override
  public String toString() {
    if (isEmpty()) {
      return "{}";
    }
    StringBuilder builder = new StringBuilder().append("{");
    Iterator<T> it = iterator();
    while (it.hasNext()) {
      T element = it.next();
      builder.append(element);
      if (!it.hasNext()) {
        builder.append("}");
      } else {
        builder.append(", ");
      }
    }
    return builder.toString();
  }
}
