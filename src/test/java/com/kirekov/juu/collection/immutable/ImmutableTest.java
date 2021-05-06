package com.kirekov.juu.collection.immutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ImmutableTest {

  @Test
  void emptyList() {
    ImmutableList<String> list = Immutable.emptyList();
    assertTrue(list.isEmpty());
    assertFalse(list.isNotEmpty());
  }

  @Test
  void emptySet() {
    ImmutableSet<String> set = Immutable.emptySet();
    assertTrue(set.isEmpty());
    assertFalse(set.isNotEmpty());
  }

  @Test
  void listOf() {
    int item1 = 1312;
    int item2 = 1241312;
    int item3 = -211312;
    int item4 = 233;

    ImmutableList<Integer> list1 = Immutable.listOf(item1, item2, item3, item4);
    ImmutableList<Integer> list2 = Immutable.listOf(Arrays.asList(item1, item2, item3, item4));
    ImmutableList<Integer> list3 = Immutable.listOf(Immutable.emptyList());

    assertEquals(list1, list2);
    assertTrue(list3.isEmpty());
    assertEquals(4, list1.size());
    assertEquals(item1, list1.get(0));
    assertEquals(item2, list1.get(1));
    assertEquals(item3, list1.get(2));
    assertEquals(item4, list1.get(3));
  }

  @Test
  void setOf() {
    String item1 = "dasf";
    String item2 = "12313";
    String item3 = "12@@@@313";
    String item4 = "1231!!!!!!3";

    ImmutableSet<String> set1 = Immutable.setOf(item1, item2, item3, item4);
    ImmutableSet<String> set2 = Immutable.setOf(Arrays.asList(item1, item2, item3, item4));
    ImmutableSet<String> set3 = Immutable.setOf(Immutable.emptyList());

    assertEquals(set1, set2);
    assertTrue(set3.isEmpty());
    assertEquals(4, set1.size());
    assertTrue(set1.contains(item1));
    assertTrue(set1.contains(item2));
    assertTrue(set1.contains(item3));
    assertTrue(set1.contains(item4));
  }

  @Test
  void mapOfWithJavaNativeMap() {
    Map<String, Integer> mutable = new HashMap<>();
    mutable.put("1", 1);
    mutable.put("2", 2);
    mutable.put("3", 3);
    mutable.put("4", 4);

    ImmutableMap<String, Integer> map1 = Immutable.mapOf(mutable);
    ImmutableMap<String, Integer> map2 = Immutable.mapOf(Collections.emptyMap());

    assertTrue(map2.isEmpty());

    assertEquals(4, map1.size());
    assertEquals(1, map1.get("1"));
    assertEquals(2, map1.get("2"));
    assertEquals(3, map1.get("3"));
    assertEquals(4, map1.get("4"));

    mutable.clear();

    assertEquals(4, map1.size());
    assertEquals(1, map1.get("1"));
    assertEquals(2, map1.get("2"));
    assertEquals(3, map1.get("3"));
    assertEquals(4, map1.get("4"));
  }

  @Test
  void mapOfWithPairs() {
    ImmutableList<Pair<String, Integer>> list =
        Immutable.listOf(
            Pair.of("1", 1),
            Pair.of("2", 2),
            Pair.of("3", 3),
            Pair.of("4", 4)
        );

    ImmutableMap<String, Integer> map1 = Immutable.mapOf(list);
    ImmutableMap<String, Integer> map2 = Immutable.mapOf(Immutable.emptyList());

    assertTrue(map2.isEmpty());

    assertEquals(4, map1.size());
    assertEquals(1, map1.get("1"));
    assertEquals(2, map1.get("2"));
    assertEquals(3, map1.get("3"));
    assertEquals(4, map1.get("4"));
  }

  @Test
  void mapOf2() {
    ImmutableMap<String, Integer> map = Immutable.mapOf("1", 1);

    assertEquals(1, map.size());
    assertEquals(1, map.get("1"));
  }


  @Test
  void mapOf4() {
    ImmutableMap<String, Integer> map = Immutable.mapOf(
        "1", 1,
        "2", 2
    );

    assertEquals(2, map.size());
    assertEquals(1, map.get("1"));
    assertEquals(2, map.get("2"));
  }

  @Test
  void mapOf6() {
    ImmutableMap<String, Integer> map = Immutable.mapOf(
        "1", 1,
        "2", 2,
        "3", 3
    );

    assertEquals(3, map.size());
    assertEquals(1, map.get("1"));
    assertEquals(2, map.get("2"));
    assertEquals(3, map.get("3"));
  }

  @Test
  void mapOf8() {
    ImmutableMap<String, Integer> map = Immutable.mapOf(
        "1", 1,
        "2", 2,
        "3", 3,
        "4", 4
    );

    assertEquals(4, map.size());
    assertEquals(1, map.get("1"));
    assertEquals(2, map.get("2"));
    assertEquals(3, map.get("3"));
    assertEquals(4, map.get("4"));
  }

  @Test
  void mapOf10() {
    ImmutableMap<String, Integer> map = Immutable.mapOf(
        "1", 1,
        "2", 2,
        "3", 3,
        "4", 4,
        "5", 5
    );

    assertEquals(5, map.size());
    assertEquals(1, map.get("1"));
    assertEquals(2, map.get("2"));
    assertEquals(3, map.get("3"));
    assertEquals(4, map.get("4"));
    assertEquals(5, map.get("5"));
  }
}