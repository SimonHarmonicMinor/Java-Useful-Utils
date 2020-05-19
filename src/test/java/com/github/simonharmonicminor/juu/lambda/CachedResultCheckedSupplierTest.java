package com.github.simonharmonicminor.juu.lambda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CachedResultCheckedSupplierTest {

    @Test
    void testGetCalculatesValueOnlyOnce() {
        CheckedSupplier<Integer, IllegalArgumentException> checkedSupplier =
                new CheckedSupplier<Integer, IllegalArgumentException>() {
                    private int i = 1;

                    @Override
                    public Integer get() throws IllegalArgumentException {
                        return i++;
                    }
                };

        CheckedSupplier<Number, RuntimeException> cachedResultCheckedSupplier =
                new CachedResultCheckedSupplier<>(checkedSupplier);

        assertEquals(1, cachedResultCheckedSupplier.get());
        assertEquals(1, cachedResultCheckedSupplier.get());
        assertEquals(1, cachedResultCheckedSupplier.get());
        assertEquals(1, cachedResultCheckedSupplier.get());
        assertEquals(1, cachedResultCheckedSupplier.get());
        assertEquals(1, cachedResultCheckedSupplier.get());
    }
}