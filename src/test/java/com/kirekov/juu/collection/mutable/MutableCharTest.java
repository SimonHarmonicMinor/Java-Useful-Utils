package com.kirekov.juu.collection.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MutableCharTest {

  @Test
  void testMutability() {
    MutableChar mutableChar = new MutableChar('a');
    assertEquals('a', mutableChar.getValue());

    mutableChar.setValue('b');
    assertEquals('b', mutableChar.getValue());
  }
}