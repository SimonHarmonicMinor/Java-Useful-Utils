package com.kirekov.juu.collection.immutable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PairImplTest {

  @Test
  void shouldReturnThePassedArguments() {
    String key = "fsdf";
    int value = 5235;
    Pair<String, Integer> pair = new PairImpl<>(key, value);
    assertEquals(key, pair.getKey());
    assertEquals(value, pair.getValue());
  }

  @Test
  void equalsShouldReturnTrueIfKeyAndValueAreTheSame() {
    int key = 24124;
    String value = "fsdaf";
    Pair<Integer, String> pair1 = new PairImpl<>(key, value);
    Pair<Integer, String> pair2 = new PairImpl<>(key, value);
    assertEquals(pair1, pair2);
  }
}