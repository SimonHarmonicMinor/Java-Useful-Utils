package com.kirekov.juu.collection.mutable;

/**
 * Container which allows to hold a value and change it if necessary
 *
 * @param <T> the type of the value
 * @see MutableBoolean
 * @see MutableByte
 * @see MutableChar
 * @see MutableDouble
 * @see MutableFloat
 * @see MutableInt
 * @see MutableLong
 * @see MutableShort
 */
public class MutableValue<T> {

  private T value;

  public MutableValue(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }
}
