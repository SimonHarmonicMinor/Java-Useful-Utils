package com.kirekov.juu.collection.mutable;

/**
 * Container which allows to hold double value and change it if necessary
 *
 * @see MutableValue
 */
public class MutableDouble {

  private double value;

  public MutableDouble(double value) {
    this.value = value;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }
}
