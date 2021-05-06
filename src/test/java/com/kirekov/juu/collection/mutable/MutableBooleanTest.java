package com.kirekov.juu.collection.mutable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MutableBooleanTest {

  @Test
  void testMutability() {
    MutableBoolean mutableBoolean = new MutableBoolean(false);
    assertFalse(mutableBoolean.isValue());

    mutableBoolean.setValue(true);
    assertTrue(mutableBoolean.isValue());
  }
}