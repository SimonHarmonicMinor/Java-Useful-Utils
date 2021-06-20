package com.kirekov.juu.collection.immutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

  @Test
  void equalsShouldReturnFalseIfKeysAreNotTheSame() {
    int key = 24124;
    String value = "fsdaf";
    Pair<Integer, String> pair1 = new PairImpl<>(key, value);
    Pair<Integer, String> pair2 = new PairImpl<>(key + 1, value);
    assertNotEquals(pair1, pair2);
  }

  @Test
  void equalsShouldReturnFalseIfValuesAreNotTheSame() {
    int key = 24124;
    String value = "fsdaf";
    Pair<Integer, String> pair1 = new PairImpl<>(key, value);
    Pair<Integer, String> pair2 = new PairImpl<>(key, value + "414");
    assertNotEquals(pair1, pair2);
  }

  @Test
  void equalsShouldReturnFalseIfKeysAndValuesAreNotTheSame() {
    int key = 24124;
    String value = "fsdaf";
    Pair<Integer, String> pair1 = new PairImpl<>(key, value);
    Pair<Integer, String> pair2 = new PairImpl<>(key + 123, value + "414");
    assertNotEquals(pair1, pair2);
  }

  @Test
  void equalsShouldReturnFalseIfComparingWithDifferentClass() {
    assertNotEquals(new PairImpl<>("1","341"), "1");
  }

  @Test
  void shouldReturnCorrectToStringRepresentation() {
    int key = 24124;
    String value = "fsdfasdfdsfaf";
    Pair<Integer, String> pair = new PairImpl<>(key, value);
    assertEquals(String.format("(key=%s; value=%s)", key, value), pair.toString());
  }
}