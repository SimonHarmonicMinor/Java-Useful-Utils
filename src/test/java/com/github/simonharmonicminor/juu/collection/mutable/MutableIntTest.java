package com.github.simonharmonicminor.juu.collection.mutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableIntTest {
    @Test
    void testMutability() {
        MutableInt mutableInt = new MutableInt(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, mutableInt.getValue());

        mutableInt.setValue(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, mutableInt.getValue());
    }
}