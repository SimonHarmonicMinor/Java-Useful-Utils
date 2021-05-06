package com.kirekov.juu.collection.mutable;

/**
 * Container which allows to hold boolean value and change it if necessary.
 *
 * @see MutableValue
 */
public class MutableBoolean {

  private boolean value;

  public MutableBoolean(boolean value) {
    this.value = value;
  }

  public boolean isValue() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }
}
