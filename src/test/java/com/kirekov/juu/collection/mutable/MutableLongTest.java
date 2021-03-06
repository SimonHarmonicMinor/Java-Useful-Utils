package com.kirekov.juu.collection.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MutableLongTest {

  @Test
  void testMutability() {
    MutableLong mutableLong = new MutableLong(Long.MAX_VALUE);
    assertEquals(Long.MAX_VALUE, mutableLong.getValue());

    mutableLong.setValue(Long.MAX_VALUE - 1);
    assertEquals(Long.MAX_VALUE - 1, mutableLong.getValue());
  }
}