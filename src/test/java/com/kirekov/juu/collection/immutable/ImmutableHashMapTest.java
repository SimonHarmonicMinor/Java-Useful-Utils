package com.kirekov.juu.collection.immutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ImmutableHashMapTest {

  @Test
  void instantiateWithCloning() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);

    ImmutableMap<String, Integer> immutableMap = new ImmutableHashMap<>(mutable);

    assertEquals(3, immutableMap.size());
    assertEquals(1, immutableMap.get("1"));
    assertEquals(2, immutableMap.get("2"));
    assertEquals(3, immutableMap.get("3"));

    mutable.clear();

    assertEquals(3, immutableMap.size());
    assertEquals(1, immutableMap.get("1"));
    assertEquals(2, immutableMap.get("2"));
    assertEquals(3, immutableMap.get("3"));
  }

  @Test
  void containsKeyAndValue() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);

    ImmutableMap<String, Integer> immutableMap = new ImmutableHashMap<>(mutable);

    assertTrue(immutableMap.containsKey("1"));
    assertTrue(immutableMap.containsKey("2"));
    assertTrue(immutableMap.containsKey("3"));

    assertTrue(immutableMap.containsValue(1));
    assertTrue(immutableMap.containsValue(2));
    assertTrue(immutableMap.containsValue(3));

    assertFalse(immutableMap.containsKey("123"));
    assertFalse(immutableMap.containsKey("1123"));
    assertFalse(immutableMap.containsKey(12331));

    assertFalse(immutableMap.containsValue(3123));
    assertFalse(immutableMap.containsValue(315123));
    assertFalse(immutableMap.containsValue("dasdas"));

    assertTrue(immutableMap.isNotEmpty());
    assertFalse(immutableMap.isEmpty());
  }

  @Test
  void notContainsKeyAndValue() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);

    ImmutableMap<String, Integer> immutableMap = new ImmutableHashMap<>(mutable);

    assertFalse(immutableMap.notContainsKey("1"));
    assertFalse(immutableMap.notContainsKey("2"));
    assertFalse(immutableMap.notContainsKey("3"));

    assertFalse(immutableMap.notContainsValue(1));
    assertFalse(immutableMap.notContainsValue(2));
    assertFalse(immutableMap.notContainsValue(3));

    assertTrue(immutableMap.notContainsKey("123"));
    assertTrue(immutableMap.notContainsKey("1123"));
    assertTrue(immutableMap.notContainsKey(12331));

    assertTrue(immutableMap.notContainsValue(3123));
    assertTrue(immutableMap.notContainsValue(315123));
    assertTrue(immutableMap.notContainsValue("dasdas"));
  }

  @Test
  void concatWithOverride() {
    Map<String, Integer> mutable1 = new HashMap<>();
    mutable1.put("1", 1);
    mutable1.put("2", 2);
    mutable1.put("3", 3);

    ImmutableMap<String, Integer> map1 = new ImmutableHashMap<>(mutable1);

    Map<String, Integer> mutable2 = new HashMap<>();
    mutable2.put("4", 1);
    mutable2.put("5", 2);
    mutable2.put("1", 3);

    ImmutableMap<String, Integer> map2 = new ImmutableHashMap<>(mutable2);

    ImmutableMap<String, Integer> concat = map1.concatWithOverride(map2);
    assertEquals(5, concat.size());
    assertEquals(3, concat.get("1"));
    assertEquals(2, concat.get("2"));
    assertEquals(3, concat.get("3"));
    assertEquals(1, concat.get("4"));
    assertEquals(2, concat.get("5"));

    concat = map2.concatWithOverride(map1);
    assertEquals(5, concat.size());
    assertEquals(1, concat.get("1"));
    assertEquals(2, concat.get("2"));
    assertEquals(3, concat.get("3"));
    assertEquals(1, concat.get("4"));
    assertEquals(2, concat.get("5"));
  }

  @Test
  void concatWithoutOverride() {
    Map<String, Integer> mutable1 = new HashMap<>();
    mutable1.put("1", 1);
    mutable1.put("2", 2);
    mutable1.put("3", 3);

    ImmutableMap<String, Integer> map1 = new ImmutableHashMap<>(mutable1);

    Map<String, Integer> mutable2 = new HashMap<>();
    mutable2.put("4", 1);
    mutable2.put("5", 2);
    mutable2.put("1", 3);

    ImmutableMap<String, Integer> map2 = new ImmutableHashMap<>(mutable2);

    ImmutableMap<String, Integer> concat = map2.concatWithoutOverride(map1);
    assertEquals(5, concat.size());
    assertEquals(3, concat.get("1"));
    assertEquals(2, concat.get("2"));
    assertEquals(3, concat.get("3"));
    assertEquals(1, concat.get("4"));
    assertEquals(2, concat.get("5"));

    concat = map1.concatWithoutOverride(map2);
    assertEquals(5, concat.size());
    assertEquals(1, concat.get("1"));
    assertEquals(2, concat.get("2"));
    assertEquals(3, concat.get("3"));
    assertEquals(1, concat.get("4"));
    assertEquals(2, concat.get("5"));
  }

  @Test
  void concatWith() {
    Map<String, Integer> mutable1 = new HashMap<>();
    mutable1.put("1", 1);
    mutable1.put("2", 2);
    mutable1.put("3", 3);
    mutable1.put("4", 4);

    ImmutableMap<String, Integer> map1 = new ImmutableHashMap<>(mutable1);

    Map<String, Integer> mutable2 = new HashMap<>();
    mutable2.put("1", 5);
    mutable2.put("2", 6);
    mutable2.put("3", 7);
    mutable2.put("4", 8);

    ImmutableMap<String, Integer> map2 = new ImmutableHashMap<>(mutable2);

    ImmutableMap<String, Integer> concat =
        map1.concatWith(map2, (key, oldVal, newVal) -> {
          if (Integer.parseInt(key) % 2 == 0) {
            return oldVal;
          } else {
            return newVal;
          }
        });

    assertEquals(4, concat.size());
    assertEquals(5, concat.get("1"));
    assertEquals(2, concat.get("2"));
    assertEquals(7, concat.get("3"));
    assertEquals(4, concat.get("4"));
  }

  @Test
  void keySet() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map = new ImmutableHashMap<>(mutable);

    ImmutableSet<String> keySet = map.keySet();

    assertEquals(4, keySet.size());
    assertTrue(keySet.contains("1"));
    assertTrue(keySet.contains("2"));
    assertTrue(keySet.contains("3"));
    assertTrue(keySet.contains("4"));
  }

  @Test
  void values() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map = new ImmutableHashMap<>(mutable);
    ImmutableList<Integer> values = map.values();

    assertEquals(4, values.size());
    assertTrue(values.contains(1));
    assertTrue(values.contains(2));
    assertTrue(values.contains(3));
    assertTrue(values.contains(4));
  }

  @Test
  void toMutableMap() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map = new ImmutableHashMap<>(mutable);

    Map<String, Integer> newMutable = map.toMutableMap();

    assertEquals(4, map.size());
    assertEquals(4, newMutable.size());

    newMutable.clear();

    assertEquals(4, map.size());
  }

  @Test
  void equalsAndHashCode() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map1 = new ImmutableHashMap<>(mutable);
    ImmutableMap<String, Integer> map2 = new ImmutableHashMap<>(mutable);
    mutable.remove("1");
    ImmutableMap<String, Integer> map3 = new ImmutableHashMap<>(mutable);

    assertEquals(map1, map2);
    assertEquals(map1.hashCode(), map2.hashCode());
    assertNotEquals(map1, map3);
  }

  @Test
  void shouldNotEqualIfObjectsOfDifferentType() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map = new ImmutableHashMap<>(mutable);
    assertNotEquals(map, mutable);
  }

  @Test
  void shouldEqualIfObjectIsTheSame() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map = new ImmutableHashMap<>(mutable);
    assertEquals(map, map);
  }

  @Test
  void getOrDefault() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map = new ImmutableHashMap<>(mutable);

    assertEquals(1, map.getOrDefault("1", Integer.MAX_VALUE));
    assertEquals(Integer.MAX_VALUE, map.getOrDefault("11", Integer.MAX_VALUE));
  }

  @Test
  void containsPair() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map = new ImmutableHashMap<>(mutable);
    assertTrue(map.containsPair(Pair.of("1", 1)));
    assertFalse(map.containsPair(Pair.of("2", 1)));
  }

  @Test
  void notContainsPair() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map = new ImmutableHashMap<>(mutable);
    assertFalse(map.notContainsPair(Pair.of("1", 1)));
    assertTrue(map.notContainsPair(Pair.of("2", 1)));
  }
}