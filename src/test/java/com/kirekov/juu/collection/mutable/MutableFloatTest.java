package com.kirekov.juu.collection.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MutableFloatTest {

  @Test
  void testMutability() {
    MutableFloat mutableFloat = new MutableFloat(2.0F);
    assertEquals(2.0F, mutableFloat.getValue(), 0.0001);

    mutableFloat.setValue(3.0F);
    assertEquals(3.0F, mutableFloat.getValue(), 0.0001);
  }
}