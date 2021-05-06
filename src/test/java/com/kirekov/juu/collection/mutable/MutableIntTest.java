package com.kirekov.juu.collection.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MutableIntTest {

  @Test
  void testMutability() {
    MutableInt mutableInt = new MutableInt(Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, mutableInt.getValue());

    mutableInt.setValue(Integer.MIN_VALUE);
    assertEquals(Integer.MIN_VALUE, mutableInt.getValue());
  }
}