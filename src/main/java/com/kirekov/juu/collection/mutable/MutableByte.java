package com.kirekov.juu.collection.mutable;

/**
 * Container which allows to hold byte value and change it if necessary.
 *
 * @see MutableValue
 */
public final class MutableByte {

  private byte value;

  public MutableByte(byte value) {
    this.value = value;
  }

  public byte getValue() {
    return value;
  }

  public void setValue(byte value) {
    this.value = value;
  }
}
