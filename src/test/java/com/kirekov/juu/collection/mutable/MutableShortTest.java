package com.kirekov.juu.collection.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MutableShortTest {

  @Test
  void testMutability() {
    MutableShort mutableShort = new MutableShort((short) 1);
    assertEquals(1, mutableShort.getValue());

    mutableShort.setValue((short) 2);
    assertEquals(2, mutableShort.getValue());
  }

}