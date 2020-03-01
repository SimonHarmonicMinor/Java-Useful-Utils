package com.github.simonharmonicminor.juu.collection.immutable;

import com.github.simonharmonicminor.juu.collection.mutable.MutableInt;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableTreeMapTest {

    @Test
    void ofMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableTreeMap<String, Integer> immutableTreeMap =
                ImmutableTreeMap.of(map);

        assertEquals(3, immutableTreeMap.size());
        MutableInt idx1 = new MutableInt(1);
        immutableTreeMap.forEach((k, v) -> {
            assertEquals(String.valueOf(idx1.getValue()), k);
            assertEquals(idx1.getValue(), v);
            idx1.setValue(idx1.getValue() + 1);
        });

        map.clear();

        assertEquals(3, immutableTreeMap.size());
        MutableInt idx2 = new MutableInt(1);
        immutableTreeMap.forEach((k, v) -> {
            assertEquals(String.valueOf(idx2.getValue()), k);
            assertEquals(idx2.getValue(), v);
            idx2.setValue(idx2.getValue() + 1);
        });
    }

    @Test
    void ofMapWithComparator() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableTreeMap<String, Integer> immutableTreeMap =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        assertEquals(3, immutableTreeMap.size());
        MutableInt idx1 = new MutableInt(3);
        immutableTreeMap.forEach((k, v) -> {
            assertEquals(String.valueOf(idx1.getValue()), k);
            assertEquals(idx1.getValue(), v);
            idx1.setValue(idx1.getValue() - 1);
        });

        MutableInt idx2 = new MutableInt(3);
        immutableTreeMap.forEach((k, v) -> {
            assertEquals(String.valueOf(idx2.getValue()), k);
            assertEquals(idx2.getValue(), v);
            idx2.setValue(idx2.getValue() - 1);
        });
    }

    @Test
    void ofSortedMap() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("2", 2);
        map.put("1", 1);
        map.put("3", 3);

        ImmutableTreeMap<String, Integer> immutableTreeMap =
                ImmutableTreeMap.ofSortedMap(map);

        assertEquals(3, immutableTreeMap.size());
        MutableInt idx1 = new MutableInt(1);
        immutableTreeMap.forEach((k, v) -> {
            assertEquals(String.valueOf(idx1.getValue()), k);
            assertEquals(idx1.getValue(), v);
            idx1.setValue(idx1.getValue() + 1);
        });

        map.clear();

        assertEquals(3, immutableTreeMap.size());
        MutableInt idx2 = new MutableInt(1);
        immutableTreeMap.forEach((k, v) -> {
            assertEquals(String.valueOf(idx2.getValue()), k);
            assertEquals(idx2.getValue(), v);
            idx2.setValue(idx2.getValue() + 1);
        });
    }

    @Test
    void lowerPair() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<Pair<String, Integer>> pair =
                immutableMapNaturalOrdering.lowerPair("2");
        assertTrue(pair.isPresent());
        assertEquals("1", pair.get().getKey());
        assertEquals(1, pair.get().getValue());

        pair = immutableMapNaturalOrdering.lowerPair("1");
        assertFalse(pair.isPresent());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        pair = immutableMapReversedOrdering.lowerPair("2");
        assertTrue(pair.isPresent());
        assertEquals("3", pair.get().getKey());
        assertEquals(3, pair.get().getValue());

        pair = immutableMapReversedOrdering.lowerPair("3");
        assertFalse(pair.isPresent());
    }

    @Test
    void lowerKey() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<String> key = immutableMapNaturalOrdering.lowerKey("2");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapNaturalOrdering.lowerKey("1");
        assertFalse(key.isPresent());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Comparator.reverseOrder());

        key = immutableMapReversedOrdering.lowerKey("2");
        assertTrue(key.isPresent());
        assertEquals("3", key.get());

        key = immutableMapReversedOrdering.lowerKey("3");
        assertFalse(key.isPresent());
    }

    @Test
    void floorPair() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("4", 4);
        map.put("7", 7);

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<Pair<String, Integer>> pair =
                immutableMapNaturalOrdering.floorPair("2");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("1", 1), pair.get());

        pair = immutableMapNaturalOrdering.floorPair("4");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("4", 4), pair.get());

        pair = immutableMapNaturalOrdering.floorPair("1");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("1", 1), pair.get());

        pair = immutableMapNaturalOrdering.floorPair("0");
        assertFalse(pair.isPresent());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());
        pair = immutableMapReversedOrdering.floorPair("5");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("7", 7), pair.get());

        pair = immutableMapReversedOrdering.floorPair("4");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("4", 4), pair.get());

        pair = immutableMapReversedOrdering.floorPair("7");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("7", 7), pair.get());

        pair = immutableMapReversedOrdering.floorPair("8");
        assertFalse(pair.isPresent());
    }

    @Test
    void floorKey() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("4", 4);
        map.put("7", 7);

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<String> key = immutableMapNaturalOrdering.floorKey("3");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapNaturalOrdering.floorKey("4");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapNaturalOrdering.floorKey("1");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapNaturalOrdering.floorKey("0");
        assertFalse(key.isPresent());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        key = immutableMapReversedOrdering.floorKey("5");
        assertTrue(key.isPresent());
        assertEquals("7", key.get());

        key = immutableMapReversedOrdering.floorKey("4");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapReversedOrdering.floorKey("7");
        assertTrue(key.isPresent());
        assertEquals("7", key.get());

        key = immutableMapReversedOrdering.floorKey("8");
        assertFalse(key.isPresent());
    }

    @Test
    void ceilingPair() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("4", 4);
        map.put("7", 7);

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<Pair<String, Integer>> pair =
                immutableMapNaturalOrdering.ceilingPair("0");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("1", 1), pair.get());

        pair = immutableMapNaturalOrdering.ceilingPair("1");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("1", 1), pair.get());

        pair = immutableMapNaturalOrdering.ceilingPair("3");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("4", 4), pair.get());

        pair = immutableMapNaturalOrdering.ceilingPair("4");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("4", 4), pair.get());

        pair = immutableMapNaturalOrdering.ceilingPair("8");
        assertFalse(pair.isPresent());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        pair = immutableMapReversedOrdering.ceilingPair("8");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("7", 7), pair.get());

        pair = immutableMapReversedOrdering.ceilingPair("7");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("7", 7), pair.get());

        pair = immutableMapReversedOrdering.ceilingPair("5");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("4", 4), pair.get());

        pair = immutableMapReversedOrdering.ceilingPair("4");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("4", 4), pair.get());

        pair = immutableMapReversedOrdering.ceilingPair("0");
        assertFalse(pair.isPresent());
    }

    @Test
    void ceilingKey() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("4", 4);
        map.put("7", 7);

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<String> key = immutableMapNaturalOrdering.ceilingKey("0");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapNaturalOrdering.ceilingKey("1");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapNaturalOrdering.ceilingKey("2");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapNaturalOrdering.ceilingKey("4");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapNaturalOrdering.ceilingKey("7");
        assertTrue(key.isPresent());
        assertEquals("7", key.get());

        key = immutableMapNaturalOrdering.ceilingKey("8");
        assertFalse(key.isPresent());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        key = immutableMapReversedOrdering.ceilingKey("8");
        assertTrue(key.isPresent());
        assertEquals("7", key.get());

        key = immutableMapReversedOrdering.ceilingKey("7");
        assertTrue(key.isPresent());
        assertEquals("7", key.get());

        key = immutableMapReversedOrdering.ceilingKey("5");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapReversedOrdering.ceilingKey("4");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapReversedOrdering.ceilingKey("1");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapReversedOrdering.ceilingKey("0");
        assertFalse(key.isPresent());
    }

    @Test
    void higherPair() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("4", 4);
        map.put("7", 7);

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<Pair<String, Integer>> pair =
                immutableMapNaturalOrdering.higherPair("0");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("1", 1), pair.get());

        pair = immutableMapNaturalOrdering.higherPair("1");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("4", 4), pair.get());

        pair = immutableMapNaturalOrdering.higherPair("4");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("7", 7), pair.get());

        pair = immutableMapNaturalOrdering.higherPair("7");
        assertFalse(pair.isPresent());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        pair = immutableMapReversedOrdering.higherPair("8");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("7", 7), pair.get());

        pair = immutableMapReversedOrdering.higherPair("7");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("4", 4), pair.get());

        pair = immutableMapReversedOrdering.higherPair("4");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("1", 1), pair.get());

        pair = immutableMapReversedOrdering.higherPair("3");
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("1", 1), pair.get());

        pair = immutableMapReversedOrdering.higherPair("1");
        assertFalse(pair.isPresent());
    }

    @Test
    void higherKey() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("4", 4);
        map.put("7", 7);

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<String> key = immutableMapNaturalOrdering.higherKey("0");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapNaturalOrdering.higherKey("1");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapNaturalOrdering.higherKey("3");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapNaturalOrdering.higherKey("4");
        assertTrue(key.isPresent());
        assertEquals("7", key.get());

        key = immutableMapNaturalOrdering.higherKey("7");
        assertFalse(key.isPresent());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        key = immutableMapReversedOrdering.higherKey("8");
        assertTrue(key.isPresent());
        assertEquals("7", key.get());

        key = immutableMapReversedOrdering.higherKey("7");
        assertTrue(key.isPresent());
        assertEquals("4", key.get());

        key = immutableMapReversedOrdering.higherKey("4");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapReversedOrdering.higherKey("3");
        assertTrue(key.isPresent());
        assertEquals("1", key.get());

        key = immutableMapReversedOrdering.higherKey("1");
        assertFalse(key.isPresent());
    }

    @Test
    void firstPair() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("4", 4);
        map.put("7", 7);

        // natural ordering map

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<Pair<String, Integer>> firstPair =
                immutableMapNaturalOrdering.firstPair();
        assertTrue(firstPair.isPresent());
        assertEquals(Pair.of("1", 1), firstPair.get());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        firstPair = immutableMapReversedOrdering.firstPair();
        assertTrue(firstPair.isPresent());
        assertEquals(Pair.of("7", 7), firstPair.get());

        // empty map

        ImmutableTreeMap<String, Integer> emptyMap =
                ImmutableTreeMap.ofSortedMap(new TreeMap<>());

        firstPair = emptyMap.firstPair();
        assertFalse(firstPair.isPresent());
    }

    @Test
    void lastPair() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("4", 4);
        map.put("7", 7);

        // natural ordering map

        ImmutableTreeMap<String, Integer> immutableMapNaturalOrdering =
                ImmutableTreeMap.of(map);

        Optional<Pair<String, Integer>> pair =
                immutableMapNaturalOrdering.lastPair();
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("7", 7), pair.get());

        // reversed ordering map

        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        pair = immutableMapReversedOrdering.lastPair();
        assertTrue(pair.isPresent());
        assertEquals(Pair.of("1", 1), pair.get());

        // empty map

        ImmutableTreeMap<String, Integer> emptyMap =
                ImmutableTreeMap.ofSortedMap(new TreeMap<>());

        pair = emptyMap.lastPair();
        assertFalse(pair.isPresent());
    }

    @Test
    void reversedOrderMap() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableNavigableMap<String, Integer> immutableMap =
                ImmutableTreeMap.of(map);

        ImmutableNavigableMap<String, Integer> reversedMap =
                immutableMap.reversedOrderMap();

        MutableInt index = new MutableInt(3);
        reversedMap.forEach((key, value) -> {
            assertEquals(index.getValue(), value);
            assertEquals(String.valueOf(index.getValue()), key);
            index.setValue(index.getValue() - 1);
        });
    }

    @Test
    void navigableKeySet() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableNavigableMap<String, Integer> immutableMap =
                ImmutableTreeMap.of(map);

        ImmutableNavigableSet<String> keys = immutableMap.navigableKeySet();

        MutableInt index = new MutableInt(1);
        keys.forEach(key -> {
            assertEquals(String.valueOf(index.getValue()), key);
            index.setValue(index.getValue() + 1);
        });
    }

    @Test
    void reversedOrderKeySet() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableNavigableMap<String, Integer> immutableMap =
                ImmutableTreeMap.of(map);

        ImmutableNavigableSet<String> keys = immutableMap.reversedOrderKeySet();

        MutableInt index = new MutableInt(3);
        keys.forEach(key -> {
            assertEquals(String.valueOf(index.getValue()), key);
            index.setValue(index.getValue() - 1);
        });
    }

    @Test
    void containsPair() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableNavigableMap<String, Integer> immutableMap =
                ImmutableTreeMap.of(map);

        assertTrue(immutableMap.containsPair(Pair.of("1", 1)));
        assertFalse(immutableMap.containsPair(Pair.of("2", 3)));
    }

    @Test
    void notContainsPair() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ImmutableNavigableMap<String, Integer> immutableMap =
                ImmutableTreeMap.of(map);

        assertFalse(immutableMap.notContainsPair(Pair.of("1", 1)));
        assertTrue(immutableMap.notContainsPair(Pair.of("2", 3)));
    }
}