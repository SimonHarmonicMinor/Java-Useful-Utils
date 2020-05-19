package com.github.simonharmonicminor.juu.lambda;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class CachedResultSupplierTest {

    @Test
    void testGetCalculatesValueOnlyOnce() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int i = 1;

            @Override
            public Integer get() {
                return i++;
            }
        };

        Supplier<Integer> cachedSupplier = new CachedResultSupplier<>(supplier);

        assertEquals(1, cachedSupplier.get());
        assertEquals(1, cachedSupplier.get());
        assertEquals(1, cachedSupplier.get());
        assertEquals(1, cachedSupplier.get());
        assertEquals(1, cachedSupplier.get());
        assertEquals(1, cachedSupplier.get());
        assertEquals(1, cachedSupplier.get());
        assertEquals(1, cachedSupplier.get());
    }
}