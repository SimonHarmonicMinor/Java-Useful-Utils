package com.kirekov.juu.collection.mutable;

/**
 * Container which allows to hold short value and change it if necessary.
 */
public class MutableShort {

  private short value;

  public MutableShort(short value) {
    this.value = value;
  }

  public short getValue() {
    return value;
  }

  public void setValue(short value) {
    this.value = value;
  }
}
