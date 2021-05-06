package com.kirekov.juu.collection.mutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MutableValueTest {

  @Test
  void testMutability() {
    MutableValue<String> stringMutable = new MutableValue<>("aa");
    assertEquals("aa", stringMutable.getValue());

    stringMutable.setValue("bb");
    assertEquals("bb", stringMutable.getValue());
  }
}