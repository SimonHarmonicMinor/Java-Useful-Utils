package com.kirekov.juu.monad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@linkplain Lazy}.
 *
 * @see Lazy
 */
class LazyTest {

  @Test
  void ofThrowsNullPointerExceptionIfSupplierIsNull() {
    assertThrows(NullPointerException.class, () -> Lazy.of(null));
  }

  @Test
  void testLazyReallyActsLazy() {
    @SuppressWarnings("unchecked")
    Collection<Integer> collection = (Collection<Integer>) mock(Collection.class);
    Lazy<Integer> lazy =
        Lazy.of(() -> {
          int val = 1;
          collection.add(val);
          return val;
        }).map(v -> {
          int val = v + 1;
          collection.add(val);
          return val;
        }).map(v -> {
          int val = v + 1;
          collection.add(val);
          return val;
        }).flatMap(v -> {
          int val = v + 1;
          collection.add(val);
          return Lazy.of(() -> val);
        });
    verify(collection, times(0)).add(1);
    verify(collection, times(0)).add(2);
    verify(collection, times(0)).add(3);
    verify(collection, times(0)).add(4);

    int result = lazy.calculate();

    assertEquals(4, result);
    verify(collection, times(1)).add(1);
    verify(collection, times(1)).add(2);
    verify(collection, times(1)).add(3);
    verify(collection, times(1)).add(4);
  }

  @Test
  void testStream() {
    Optional<String> opt =
        Lazy.of(() -> "1")
            .map(v -> v + "2")
            .map(v -> v + "3")
            .stream()
            .findFirst();
    assertTrue(opt.isPresent());
    assertEquals("123", opt.get());
  }
}