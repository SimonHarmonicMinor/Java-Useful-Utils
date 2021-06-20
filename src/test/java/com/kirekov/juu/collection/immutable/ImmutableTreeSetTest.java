package com.kirekov.juu.collection.immutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ImmutableTreeSetTest {

  @Test
  void ofIterable() {
    ImmutableTreeSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4")
    );
    assertFalse(set.isEmpty());
    assertTrue(set.isNotEmpty());
    assertEquals(4, set.size());
    assertNull(set.comparator());

    int i = 1;
    for (String str : set) {
      assertEquals(String.valueOf(i), str);
      i++;
    }
  }

  @Test
  void ofIterableWithComparator() {
    ImmutableTreeSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4"),
        Comparator.reverseOrder()
    );
    assertFalse(set.isEmpty());
    assertTrue(set.isNotEmpty());
    assertEquals(4, set.size());
    assertEquals(Comparator.reverseOrder(), set.comparator());

    int i = 4;
    for (String str : set) {
      assertEquals(String.valueOf(i), str);
      i--;
    }
  }

  @Test
  void ofSortedSet() {
    SortedSet<String> sortedSet = new TreeSet<>(Arrays.asList("1", "2", "3", "4"));
    int i = 1;
    for (String str : sortedSet) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableTreeSet<String> set = ImmutableTreeSet.ofSortedSet(
        sortedSet
    );
    assertFalse(set.isEmpty());
    assertTrue(set.isNotEmpty());
    assertEquals(4, set.size());

    i = 1;
    for (String str : set) {
      assertEquals(String.valueOf(i), str);
      i++;
    }
  }

  @Test
  void lowerHigher() {
    ImmutableTreeSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    assertEquals("2", set.lower("3").get());
    assertFalse(set.lower("1").isPresent());
    assertEquals("2", set.floor("2").get());
    assertEquals("1", set.floor("1").get());

    assertEquals("3", set.higher("2").get());
    assertFalse(set.higher("5").isPresent());
    assertEquals("3", set.ceiling("3").get());
    assertEquals("5", set.ceiling("5").get());
  }

  @Test
  void reversedOrderSet() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );
    int i = 1;
    for (String obj : set) {
      assertEquals(String.valueOf(i), obj);
      i++;
    }

    ImmutableNavigableSet<String> reversedSet = set.reversedOrderSet();

    i = 5;
    for (String obj : reversedSet) {
      assertEquals(String.valueOf(i), obj);
      i--;
    }
  }

  @Test
  void reversedOrderIterator() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );
    int i = 1;
    for (String obj : set) {
      assertEquals(String.valueOf(i), obj);
      i++;
    }

    Iterator<String> reversedSet = set.reversedOrderIterator();

    i = 5;
    while (reversedSet.hasNext()) {
      assertEquals(String.valueOf(i), reversedSet.next());
      i--;
    }
  }

  @Test
  void subset4() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableNavigableSet<String> subset1 =
        set.subSet("1", true, "2", true);
    assertEquals(2, subset1.size());
    int i = 1;
    for (String str : subset1) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableNavigableSet<String> subset2 =
        set.subSet("1", false, "4", false);
    assertEquals(2, subset2.size());
    i = 2;
    for (String str : subset2) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableNavigableSet<String> subset3 =
        set.subSet("5", true, "2", true);
    assertTrue(subset3.isEmpty());
  }

  @Test
  void headSet2() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableNavigableSet<String> subset1 =
        set.headSet("3", true);
    assertEquals(3, subset1.size());
    int i = 1;
    for (String str : subset1) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableNavigableSet<String> subset2 =
        set.headSet("5", false);
    assertEquals(4, subset2.size());
    i = 1;
    for (String str : subset2) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableNavigableSet<String> subset3 =
        set.headSet("0", false);
    assertTrue(subset3.isEmpty());
  }

  @Test
  void tailSet2() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableNavigableSet<String> subset1 =
        set.tailSet("3", true);
    assertEquals(3, subset1.size());
    int i = 3;
    for (String str : subset1) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableNavigableSet<String> subset2 =
        set.tailSet("4", false);
    assertEquals(1, subset2.size());
    i = 5;
    for (String str : subset2) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableNavigableSet<String> subset3 =
        set.tailSet("6", false);
    assertTrue(subset3.isEmpty());
  }

  @Test
  void toMutableNavigableSet() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    NavigableSet<String> mutableSet = set.toMutableNavigableSet();

    assertEquals(5, set.size());
    mutableSet.clear();
    assertEquals(5, set.size());
  }

  @Test
  void subSet2() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableSortedSet<String> subSet1 =
        set.subSet("1", "3");
    assertEquals(2, subSet1.size());
    int i = 1;
    for (String str : subSet1) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableSortedSet<String> subSet2 =
        set.subSet("2", "5");
    assertEquals(3, subSet2.size());
    i = 2;
    for (String str : subSet2) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableSortedSet<String> subSet3 =
        set.subSet("6", "1");
    assertTrue(subSet3.isEmpty());
  }

  @Test
  void headSet1() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableSortedSet<String> subSet1 =
        set.headSet("4");
    assertEquals(3, subSet1.size());
    int i = 1;
    for (String str : subSet1) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableSortedSet<String> subSet2 =
        set.headSet("2");
    assertEquals(1, subSet2.size());
    i = 1;
    for (String str : subSet2) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableSortedSet<String> subSet3 =
        set.headSet("0");
    assertTrue(subSet3.isEmpty());
  }

  @Test
  void tailSet() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableSortedSet<String> subSet1 =
        set.tailSet("2");
    assertEquals(4, subSet1.size());
    int i = 2;
    for (String str : subSet1) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableSortedSet<String> subSet2 =
        set.tailSet("4");
    assertEquals(2, subSet2.size());
    i = 4;
    for (String str : subSet2) {
      assertEquals(String.valueOf(i), str);
      i++;
    }

    ImmutableSortedSet<String> subSet3 =
        set.tailSet("6");
    assertTrue(subSet3.isEmpty());
  }

  @Test
  void first() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );
    ImmutableNavigableSet<String> emptySet = ImmutableTreeSet.of(
        new ArrayList<String>()
    );

    assertEquals("1", set.first().get());
    assertFalse(emptySet.first().isPresent());
  }

  @Test
  void last() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );
    ImmutableNavigableSet<String> emptySet = ImmutableTreeSet.of(
        new ArrayList<String>()
    );

    assertEquals("5", set.last().get());
    assertFalse(emptySet.last().isPresent());
  }

  @Test
  void toMutableSortedSet() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    SortedSet<String> mutableSet = set.toMutableSortedSet();

    assertEquals(5, set.size());
    mutableSet.clear();
    assertEquals(5, set.size());
  }

  @Test
  void concatWith() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableSet<String> concat1 = set.concatWith(Immutable.emptyList());
    ImmutableSet<String> concat2 = set.concatWith(
        Arrays.asList("6", "7", "8")
    );

    assertEquals(5, concat1.size());
    assertEquals(8, concat2.size());
    for (int i = 1; i <= 8; i++) {
      assertTrue(concat2.contains(String.valueOf(i)));
    }
  }

  @Test
  void map() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableSet<Integer> mapped = set.map(Integer::parseInt);
    assertEquals(5, mapped.size());
    for (int i = 1; i <= 5; i++) {
      assertTrue(mapped.contains(i));
    }
  }

  @Test
  void flatMap() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableSet<Integer> mapped =
        set.flatMap(x -> Arrays.asList(
            Integer.parseInt(x),
            Integer.parseInt(x + "0"))
        );

    assertEquals(10, mapped.size());
    for (int i = 1; i <= 5; i++) {
      assertTrue(mapped.contains(i));
    }

    for (int i = 10; i <= 50; i += 10) {
      assertTrue(mapped.contains(i));
    }
  }

  @Test
  void filter() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableSet<String> filtered =
        set.filter(x -> Integer.parseInt(x) % 2 == 0);

    assertEquals(2, filtered.size());
    assertTrue(filtered.contains("2"));
    assertTrue(filtered.contains("4"));
  }

  @Test
  void toList() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableList<String> list = set.toList();

    assertEquals(5, list.size());
    for (int i = 0; i < list.size(); i++) {
      assertEquals(String.valueOf(i + 1), list.get(i));
    }
  }

  @Test
  void toSet() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    ImmutableSet<String> newSet = set.toSet();

    assertEquals(5, newSet.size());
    for (int i = 1; i <= 5; i++) {
      assertTrue(newSet.contains(String.valueOf(i)));
    }
  }

  @Test
  void parallelStream() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    Stream<String> stream = set.parallelStream();
    assertTrue(stream.isParallel());
  }

  @Test
  void stream() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );

    Stream<String> stream = set.stream();
    assertFalse(stream.isParallel());
  }

  @Test
  void equalsAndHashCode() {
    ImmutableNavigableSet<String> set1 = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );
    ImmutableNavigableSet<String> set2 = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );
    ImmutableNavigableSet<String> set3 = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "4", "5")
    );

    assertEquals(set1, set2);
    assertNotEquals(set1, set3);
    assertEquals(set1.hashCode(), set2.hashCode());
    assertNotEquals(set1.hashCode(), set3.hashCode());
  }

  @Test
  void equalsShouldReturnTrueOnTheSameObject() {
    ImmutableNavigableSet<String> set1 = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );
    assertEquals(set1, set1);
  }

  @Test
  void equalsShouldReturnOnNullComparison() {
    ImmutableNavigableSet<String> set1 = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3")
    );
    assertNotEquals(null, set1);
  }

  @Test
  void equalsShouldReturnOnDifferentClassesComparison() {
    ImmutableNavigableSet<String> set1 = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3")
    );
    assertNotEquals(set1, new TreeSet<>());
  }


  @Test
  void toStringTest() {
    ImmutableNavigableSet<String> set = ImmutableTreeSet.of(
        Arrays.asList("1", "2", "3", "4", "5")
    );
    assertNotNull(set.toString());
  }

}