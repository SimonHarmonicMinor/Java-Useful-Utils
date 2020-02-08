package com.github.simonharmonicminor.juu.collection.mutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableBooleanTest {

    @Test
    void testMutability() {
        MutableBoolean mutableBoolean = new MutableBoolean(false);
        assertFalse(mutableBoolean.isValue());

        mutableBoolean.setValue(true);
        assertTrue(mutableBoolean.isValue());
    }
}