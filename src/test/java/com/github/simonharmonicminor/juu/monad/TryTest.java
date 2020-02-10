package com.github.simonharmonicminor.juu.monad;

import com.github.simonharmonicminor.juu.lambda.Action;
import com.github.simonharmonicminor.juu.lambda.CheckedSupplier;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @see Try
 */
class TryTest {

    @Test
    void ofReturnsContainer() {
        Try<Integer> t = Try.of(() -> 1);
        assertTrue(t.isPresent());
        assertEquals(1, t.get());
    }

    @Test
    void ofReturnsEmptyContainer() {
        Try<Integer> t = Try.of(() -> {
            throw new Exception();
        });
        assertTrue(t.isEmpty());
    }

    @Test
    void getFirstReturnsFirstPresent() {
        Try<Integer> t = Try.getFirst(
                () -> {
                    throw new Exception();
                },
                () -> {
                    throw new Exception();
                },
                () -> 2
        );
        assertTrue(t.isPresent());
        assertEquals(2, t.get());
    }

    @Test
    void getFirstReturnsEmpty() {
        Try<Integer> t = Try.getFirst(
                () -> {
                    throw new Exception();
                },
                () -> {
                    throw new Exception();
                },
                () -> {
                    throw new Exception();
                }
        );
        assertTrue(t.isEmpty());
    }

    @Test
    void getThrowsNoSuchElementException() {
        Try<Integer> t = Try.empty();
        assertThrows(NoSuchElementException.class, t::get);
    }

    @Test
    void mapReturnsNewContainer() {
        Try<String> t = Try.of(() -> 1)
                .map(String::valueOf);
        assertTrue(t.isPresent());
        assertEquals("1", t.get());
    }

    @Test
    void mapReturnsEmptyContainer() {
        Try<String> t = Try.of(() -> 1)
                .map(String::valueOf)
                .map(v -> {
                    throw new Exception();
                });
        assertTrue(t.isEmpty());
    }

    @Test
    void flatMapReturnsNewContainer() {
        Try<String> t = Try.of(() -> 1)
                .flatMap(v -> Try.of(() -> String.valueOf(v + 1)));
        assertTrue(t.isPresent());
        assertEquals("2", t.get());
    }

    @Test
    void flatMapReturnsEmptyContainer() {
        Try<String> t = Try.of(() -> 1)
                .flatMap(v -> Try.of(() -> String.valueOf(v + 1)))
                .flatMap(v -> {
                    throw new Exception();
                });
        assertTrue(t.isEmpty());
    }

    @Test
    void filterReturnsContainerItself() {
        final long val = 123;
        Try<Long> t = Try.of(() -> val)
                .filter(v -> true);
        assertTrue(t.isPresent());
        assertEquals(val, t.get());
    }

    @Test
    void filterReturnsEmptyContainerIfPredicateReturnsFalse() {
        final String str = "12315flsdf";
        Try<String> t = Try.of(() -> str)
                .filter(v -> false);
        assertTrue(t.isEmpty());
    }

    @Test
    void filterReturnsFalseIfContainerIsEmpty() {
        Try<Integer> t = Try.empty();
        t = t.filter(v -> true);
        assertTrue(t.isEmpty());
    }

    @Test
    void orElseReturnsValueOfContainer() {
        final int val = 12314;
        int res = Try.of(() -> val)
                .orElse(val + 1);
        assertEquals(val, res);
    }

    @Test
    void orElseReturnsDefaultValue() {
        final int val = 12314;
        int res = Try.of((CheckedSupplier<Integer, Exception>) () -> {
            throw new Exception();
        }).orElse(val + 1);
        assertEquals(val + 1, res);
    }

    @Test
    void orElseTryReturnsContainerItself() {
        Try<Integer> t1 = Try.of(() -> 1);
        Try<Integer> t2 = t1.orElseTry(() -> 2);
        assertEquals(t1, t2);
    }

    @Test
    void orElseTryReturnsOtherContainer() {
        Try<Integer> t1 = Try.of(() -> {
            throw new Exception();
        });
        Try<Integer> t2 = t1.orElseTry(() -> 2);
        assertNotEquals(t1, t2);
        assertEquals(2, t2.get());
    }

    @Test
    void orElseGetReturnsValueOfContainer() {
        final int val = 555;
        Try<Integer> t = Try.of(() -> val);
        int res = t.orElseGet(() -> val + 1);
        assertEquals(val, res);
    }

    @Test
    void orElseGetReturnsDefaultValue() {
        final int val = 555;
        Try<Integer> t = Try.of(() -> {
            throw new Exception();
        });
        int res = t.orElseGet(() -> val + 1);
        assertEquals(val + 1, res);
    }

    @Test
    void orElseThrowDoesNotThrowException() {
        final String val = "@@casd";
        Try<String> t = Try.of(() -> val);
        String res = t.orElseThrow(RuntimeException::new);
        assertEquals(val, res);
    }

    @Test
    void orElseThrowThrowsException() {
        final String val = "@@casd";
        Try<String> t = Try.of(() -> {
            throw new Exception();
        });
        assertThrows(RuntimeException.class, () -> t.orElseThrow(RuntimeException::new));
    }

    @Test
    void ifPresentCallsConsumer() {
        final String val = "dasdasdgdfg";
        @SuppressWarnings("unchecked")
        final Collection<Object> collection = mock(Collection.class);
        final Consumer<String> consumer = collection::add;

        Try<String> t = Try.of(() -> val);
        t.ifPresent(consumer);
        verify(collection, times(1)).add(val);
    }

    @Test
    void ifPresentDoesNotCallConsumer() {
        final String val = "dasdasdgdfg";
        @SuppressWarnings("unchecked")
        final Collection<Object> collection = mock(Collection.class);
        final Consumer<String> consumer = collection::add;

        Try<String> t = Try.empty();
        t.ifPresent(consumer);
        verify(collection, times(0)).add(val);
    }

    @Test
    void ifPresentDoesNotCallAction() {
        final int val = 123141;
        @SuppressWarnings("unchecked")
        final Collection<Object> collection = mock(Collection.class);
        final Action action = () -> collection.add(val);

        Try<String> t = Try.of(() -> "I'm Mr. Meeseeks! Look at me!");
        t.ifEmpty(action);
        verify(collection, times(0)).add(val);
    }

    @Test
    void ifPresentCallsAction() {
        final int val = 123141;
        @SuppressWarnings("unchecked")
        final Collection<Object> collection = mock(Collection.class);
        final Action action = () -> collection.add(val);

        Try<String> t = Try.empty();
        t.ifEmpty(action);
        verify(collection, times(1)).add(val);
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

    @Test
    void twoObjectsAreEqual() {
        final String val = "dasdg";
        Try<String> t1 = Try.of(() -> val);
        Try<String> t2 = Try.of(() -> val);
        assertEquals(t1, t2);
    }

    @Test
    void twoObjectsAreNotEqual() {
        final String val = "dasdg";
        Try<String> t1 = Try.of(() -> val);
        Try<String> t2 = Try.of(() -> val + "2");
        assertNotEquals(t1, t2);
    }
}