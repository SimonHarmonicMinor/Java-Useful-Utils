package com.kirekov.juu.collection.immutable;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableCollectorsTest {

    @Test
    void toList() {
        ImmutableList<String> list =
                Stream.of("1", "2", "3", "4")
                        .collect(ImmutableCollectors.toList());
        assertEquals(4, list.size());
        assertEquals("1", list.get(0));
        assertEquals("2", list.get(1));
        assertEquals("3", list.get(2));
        assertEquals("4", list.get(3));
    }

    @Test
    void toSet() {
        ImmutableSet<String> set =
                Stream.of("1", "2", "3", "4", "3", "2")
                        .collect(ImmutableCollectors.toSet());
        assertEquals(4, set.size());
        assertTrue(set.contains("1"));
        assertTrue(set.contains("2"));
        assertTrue(set.contains("3"));
        assertTrue(set.contains("4"));
    }

    @Test
    void toMap() {
        ImmutableMap<Integer, String> map =
                Stream.of(
                        Pair.of(1, "1"),
                        Pair.of(2, "2"),
                        Pair.of(3, "3"),
                        Pair.of(4, "4")
                ).collect(ImmutableCollectors.toMap(
                        Pair::getKey,
                        Pair::getValue
                ));
        assertEquals(4, map.size());
        assertEquals("1", map.get(1));
        assertEquals("2", map.get(2));
        assertEquals("3", map.get(3));
        assertEquals("4", map.get(4));

        assertThrows(IllegalStateException.class, () -> Stream.of(
                Pair.of(1, "1"),
                Pair.of(2, "2"),
                Pair.of(2, "3"),
                Pair.of(4, "4")
        ).collect(ImmutableCollectors.toMap(
                Pair::getKey,
                Pair::getValue
        )));
    }
}