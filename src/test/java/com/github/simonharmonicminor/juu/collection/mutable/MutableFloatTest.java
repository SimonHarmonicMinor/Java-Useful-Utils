package com.github.simonharmonicminor.juu.collection.mutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableFloatTest {

    @Test
    void testMutability() {
        MutableFloat mutableFloat = new MutableFloat(2.0F);
        assertEquals(2.0F, mutableFloat.getValue(), 0.0001);

        mutableFloat.setValue(3.0F);
        assertEquals(3.0F, mutableFloat.getValue(), 0.0001);
    }
}