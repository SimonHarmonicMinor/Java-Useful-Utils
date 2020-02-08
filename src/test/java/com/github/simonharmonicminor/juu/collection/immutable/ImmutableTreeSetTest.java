package com.github.simonharmonicminor.juu.collection.immutable;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableTreeSetTest {
    @Test
    void ofIterable() {
        ImmutableTreeSet<String> set = ImmutableTreeSet.of(
                Arrays.asList("1", "2", "3", "4")
        );
        assertFalse(set.isEmpty());
        assertTrue(set.isNotEmpty());
        assertEquals(4, set.size());

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
    void ofSortedSetWithoutCloning() {
        SortedSet<String> sortedSet = new TreeSet<>(Arrays.asList("1", "2", "3", "4"));
        int i = 1;
        for (String str : sortedSet) {
            assertEquals(String.valueOf(i), str);
            i++;
        }

        ImmutableTreeSet<String> set = new ImmutableTreeSet<>(
                sortedSet,
                false
        );
        assertFalse(set.isEmpty());
        assertTrue(set.isNotEmpty());
        assertEquals(4, set.size());

        i = 1;
        for (String str : set) {
            assertEquals(String.valueOf(i), str);
            i++;
        }

        sortedSet.clear();

        assertTrue(set.isEmpty());
        assertFalse(set.isNotEmpty());
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

}