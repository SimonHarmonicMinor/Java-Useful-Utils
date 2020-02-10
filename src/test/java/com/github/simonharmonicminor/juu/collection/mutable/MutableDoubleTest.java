package com.github.simonharmonicminor.juu.collection.mutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableDoubleTest {

    @Test
    void testMutability() {
        MutableDouble mutableDouble = new MutableDouble(7.0);
        assertEquals(7.0, mutableDouble.getValue(), 0.0001);

        mutableDouble.setValue(10.0);
        assertEquals(10.0, mutableDouble.getValue(), 0.0001);
    }
}