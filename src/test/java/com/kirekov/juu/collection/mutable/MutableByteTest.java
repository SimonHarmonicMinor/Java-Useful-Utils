package com.kirekov.juu.collection.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MutableByteTest {

  @Test
  void testMutability() {
    MutableByte mutableByte = new MutableByte((byte) 2);
    assertEquals(2, mutableByte.getValue());

    mutableByte.setValue((byte) 3);
    assertEquals(3, mutableByte.getValue());
  }
}