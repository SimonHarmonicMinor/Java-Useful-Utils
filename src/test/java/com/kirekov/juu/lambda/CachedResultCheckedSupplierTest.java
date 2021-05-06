package com.kirekov.juu.lambda;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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

    CheckedSupplier<Integer, IllegalArgumentException> cachedResultCheckedSupplier =
        new CachedResultCheckedSupplier<>(checkedSupplier);

    assertEquals(1, cachedResultCheckedSupplier.get());
    assertEquals(1, cachedResultCheckedSupplier.get());
    assertEquals(1, cachedResultCheckedSupplier.get());
    assertEquals(1, cachedResultCheckedSupplier.get());
    assertEquals(1, cachedResultCheckedSupplier.get());
    assertEquals(1, cachedResultCheckedSupplier.get());
  }
}