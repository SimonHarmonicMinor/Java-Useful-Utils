package com.kirekov.juu.collection.immutable.abstraction;

import com.kirekov.juu.collection.immutable.ImmutableList;
import java.util.Objects;

/**
 * Abstract immutable list.
 *
 * @param <T> the type of the content
 */
public abstract class AbstractImmutableList<T> implements ImmutableList<T> {

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ImmutableList)) {
      return false;
    }
    ImmutableList<?> other = (ImmutableList<?>) o;
    if (size() != other.size()) {
      return false;
    }
    for (int i = 0; i < size(); i++) {
      if (!Objects.equals(get(i), other.get(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    for (T element : this) {
      if (element == null) {
        hashCode = 31 * hashCode;
      } else {
        hashCode = 31 * hashCode + element.hashCode();
      }
    }
    return hashCode;
  }

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
