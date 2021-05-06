package com.kirekov.juu.collection.immutable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ImmutableHashSetTest {

  @Test
  void instantiateWithCloning() {
    HashSet<Integer> hashSet = new HashSet<>();
    int item1 = 1;
    int item2 = 2;
    int item3 = 3;
    int item4 = 4;

    hashSet.add(item1);
    hashSet.add(item2);
    hashSet.add(item3);
    hashSet.add(item4);
    hashSet.add(item4);

    ImmutableSet<Integer> immutableSet = new ImmutableHashSet<>(hashSet, true);

    assertEquals(4, immutableSet.size());
    assertTrue(immutableSet.contains(item1));
    assertTrue(immutableSet.contains(item2));
    assertTrue(immutableSet.contains(item3));
    assertTrue(immutableSet.contains(item4));

    hashSet.clear();

    assertEquals(4, immutableSet.size());
  }

  @Test
  void instantiateWithoutCloning() {
    HashSet<Integer> hashSet = new HashSet<>();
    int item1 = 1;
    int item2 = 2;
    int item3 = 3;
    int item4 = 4;

    hashSet.add(item1);
    hashSet.add(item2);
    hashSet.add(item3);
    hashSet.add(item4);
    hashSet.add(item4);

    ImmutableSet<Integer> immutableSet = new ImmutableHashSet<>(hashSet, false);

    assertEquals(4, immutableSet.size());
    assertTrue(immutableSet.contains(item1));
    assertTrue(immutableSet.contains(item2));
    assertTrue(immutableSet.contains(item3));
    assertTrue(immutableSet.contains(item4));

    hashSet.clear();

    assertTrue(immutableSet.isEmpty());
  }

  @Test
  void concatWith() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "123afda14fsdffg";
    String item4 = "123adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    String newItem1 = "1";
    String newItem2 = "2";
    String newItem3 = "3";

    ImmutableSet<String> concat = immutableSet.concatWith(
        Arrays.asList(newItem1, newItem2, newItem3)
    );

    assertEquals(7, concat.size());
    assertTrue(concat.contains(item1));
    assertTrue(concat.contains(item2));
    assertTrue(concat.contains(item3));
    assertTrue(concat.contains(item4));
    assertTrue(concat.contains(newItem1));
    assertTrue(concat.contains(newItem2));
    assertTrue(concat.contains(newItem3));
  }

  @Test
  void map() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    ImmutableSet<String> mapped = immutableSet.map(x -> x.substring(0, 1));

    assertEquals(4, mapped.size());
    assertTrue(mapped.contains(item1.substring(0, 1)));
    assertTrue(mapped.contains(item2.substring(0, 1)));
    assertTrue(mapped.contains(item3.substring(0, 1)));
    assertTrue(mapped.contains(item4.substring(0, 1)));
  }

  private String reverseString(String str) {
    StringBuilder builder = new StringBuilder(str.length());
    for (int i = str.length() - 1; i >= 0; i--) {
      builder.append(str.charAt(i));
    }
    return builder.toString();
  }

  @Test
  void flatMap() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    ImmutableSet<String> flatMapped =
        immutableSet.flatMap(x -> Arrays.asList(x, reverseString(x)));

    assertEquals(8, flatMapped.size());
    assertTrue(flatMapped.contains(item1));
    assertTrue(flatMapped.contains(reverseString(item1)));
    assertTrue(flatMapped.contains(item2));
    assertTrue(flatMapped.contains(reverseString(item2)));
    assertTrue(flatMapped.contains(item3));
    assertTrue(flatMapped.contains(reverseString(item3)));
    assertTrue(flatMapped.contains(item4));
    assertTrue(flatMapped.contains(reverseString(item4)));
  }

  @Test
  void filter() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    ImmutableSet<String> filtered = immutableSet.filter(
        x -> x.startsWith("d") || x.startsWith("3")
    );

    assertEquals(2, filtered.size());
    assertTrue(filtered.contains(item1));
    assertTrue(filtered.contains(item4));
  }

  @Test
  void toList() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    ImmutableList<String> list = immutableSet.toList();

    assertEquals(4, list.size());
    assertTrue(list.contains(item1));
    assertTrue(list.contains(item2));
    assertTrue(list.contains(item3));
    assertTrue(list.contains(item4));
  }

  @Test
  void toSet() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    ImmutableSet<String> set = immutableSet.toSet();

    assertEquals(immutableSet, set);
  }

  @Test
  void parallelStream() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    Stream<String> stream = immutableSet.parallelStream();

    assertTrue(stream.isParallel());
  }

  @Test
  void stream() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    Stream<String> stream = immutableSet.stream();

    assertFalse(stream.isParallel());
  }

  @Test
  void hashCodeTest() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet1 = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );
    ImmutableSet<String> immutableSet2 = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );

    assertEquals(immutableSet1.hashCode(), immutableSet2.hashCode());
  }

  @Test
  void toStringTest() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );
    assertNotNull(immutableSet.toString());
  }

  @Test
  void equalsAndNotEquals() {
    String item1 = "dasgsdfg";
    String item2 = "123afdasgsdfg";
    String item3 = "223afda14fsdffg";
    String item4 = "323adfgdfgfda14fsdffg";

    ImmutableSet<String> immutableSet1 = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );
    ImmutableSet<String> immutableSet2 = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3, item4).collect(Collectors.toSet())
    );
    ImmutableSet<String> immutableSet3 = new ImmutableHashSet<>(
        Stream.of(item1, item2, item3).collect(Collectors.toSet())
    );

    assertEquals(immutableSet1, immutableSet2);
    assertNotEquals(immutableSet1, immutableSet3);
  }
}