package com.kirekov.juu.collection.mutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableValueTest {
    @Test
    void testMutability() {
        MutableValue<String> stringMutable = new MutableValue<>("aa");
        assertEquals("aa", stringMutable.getValue());

        stringMutable.setValue("bb");
        assertEquals("bb", stringMutable.getValue());
    }
}