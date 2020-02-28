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


        ImmutableTreeMap<String, Integer> immutableMapReversedOrdering =
                ImmutableTreeMap.of(map, Collections.reverseOrder());

        pair = immutableMapReversedOrdering.lowerPair("2");
        assertTrue(pair.isPresent());
        assertEquals("3", pair.get().getKey());
        assertEquals(3, pair.get().getValue());

        pair = immutableMapReversedOrdering.lowerPair("3");
        assertFalse(pair.isPresent());
    }
}