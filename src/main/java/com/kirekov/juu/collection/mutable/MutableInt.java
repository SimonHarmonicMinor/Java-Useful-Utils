package com.kirekov.juu.collection.mutable;

/**
 * Container which allows to hold int value and change it if necessary.
 *
 * @see MutableValue
 */
public class MutableInt {

  private int value;

  public MutableInt(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
