package com.kirekov.juu.monad;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kirekov.juu.lambda.CheckedSupplier;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

/**
 * Test cases for {@linkplain Try}.
 *
 * @see Try
 */
class TryTest {

  @Test
  void shouldReturnTheSuccessfulMonad() {
    Try<String> t = Try.success("value");
    assertEquals("value", t.orElseThrow());
  }

  @Test
  void shouldThrowExceptionIfContainerIsEmpty() {
    Try<Integer> t = Try.error();
    assertThrows(NoSuchElementException.class, t::orElseThrow);
  }

  @Test
  void shouldActLazily() {
    Runnable runnable = mock(Runnable.class);
    Try<Integer> t =
        Try.of(() -> {
          runnable.run();
          return 1;
        }).map(val -> {
          runnable.run();
          return val + 10;
        }).flatMap(val -> {
          runnable.run();
          return Try.success(val + 12);
        }).filter(val -> {
          runnable.run();
          return val > 0;
        });
    verify(runnable, times(0)).run();
    assertDoesNotThrow((ThrowingSupplier<Integer>) t::orElseThrow);
    verify(runnable, times(4)).run();
  }

  @Test
  void shouldCalculateTheValueIfAllMappingsSucceed() {
    Try<String> t = Try.of(() -> 1)
        .map(val -> val + 10)
        .map(Object::toString)
        .map(val -> val + "12");
    assertEquals("1112", t.orElseThrow());
  }

  @Test
  void shouldThrowTheExceptionIfAnyMappingFails() {
    Try<String> t = Try.of(() -> 1)
        .map(val -> val + 10)
        .map(Object::toString)
        .<String>map(val -> {
          throw new Exception();
        })
        .map(val -> val + "12");
    assertThrows(
        IllegalArgumentException.class,
        () -> t.orElseThrow(IllegalArgumentException::new)
    );
  }

  @Test
  void shouldCalculateTheValueIfAllFlatMappingsSucceed() {
    Try<String> t = Try.of(() -> 1)
        .flatMap(val -> Try.success(val + 10))
        .flatMap(val -> Try.of(val::toString))
        .flatMap(val -> Try.success(val + "12"));
    assertEquals(
        "1112",
        t.orElseThrow()
    );
  }

  @Test
  void shouldThrowTheExceptionIfAnyFlatMappingFails() {
    Try<String> t = Try.of(() -> 1)
        .flatMap(val -> Try.success(val + 10))
        .flatMap(val -> Try.of(val::toString))
        .<String>flatMap(val -> Try.of(() -> {
          throw new IllegalStateException();
        }))
        .flatMap(val -> Try.success(val + "12"));
    String result = t.orElseGet((reason) -> {
      assertTrue(reason instanceof IllegalStateException);
      return "default";
    });
    assertEquals("default", result);
  }

  @Test
  void shouldCalculateTheValueIfAllFilteringSucceed() {
    Try<Integer> t = Try.success(56)
        .filter(val -> val > 0)
        .filter(val -> val < 100)
        .filter(val -> val % 2 == 0);
    assertEquals(
        56,
        t.orElseGet(() -> 1)
    );
  }

  @Test
  void shouldThrowTheExceptionIfAnyFilteringFails() {
    Try<Integer> t = Try.success(56)
        .filter(val -> val > 0)
        .filter(val -> val < 100)
        .filter(val -> val % 2 == 0)
        .filter(val -> val < 30);
    assertEquals(
        1,
        t.orElse(1)
    );
  }

  @Test
  void shouldCalculateTheOrElseTryClauseIfThePreviousOneFails() {
    Try<Integer> t = Try.<Integer>of(() -> {
      throw new Exception();
    }).orElseTry(() -> 56);
    assertEquals(56, t.orElseThrow());
  }

  @Test
  void streamReturnsNotEmptyOne() {
    final int val = 444;
    Optional<Integer> opt =
        Try.of(() -> val)
            .stream()
            .findFirst();
    assertTrue(opt.isPresent());
    assertEquals(val, opt.get());
  }

  @Test
  void streamReturnsEmptyOne() {
    Optional<Integer> opt =
        Try.of((CheckedSupplier<Integer, Exception>) () -> {
          throw new Exception();
        }).stream().findFirst();
    assertFalse(opt.isPresent());
  }
}