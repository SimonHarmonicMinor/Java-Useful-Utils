package com.github.simonharmonicminor.measure;

import org.junit.jupiter.api.Test;

import static com.github.simonharmonicminor.measure.MeasureConverter.nanosToMillis;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see Measure
 */
class MeasureTest {

    @Test
    void throwsNullPointerIfActionIsNull() {
        Measure.Action action = null;
        assertThrows(NullPointerException.class, () -> Measure.executionTime(action));
    }

    @Test
    void throwsNullPointerIfSupplierIsNull() {
        Measure.Supplier supplier = null;
        assertThrows(NullPointerException.class, () -> Measure.executionTime(supplier));
    }

    @Test
    void executionTimeReturnsNotNull() {
        Measure measure1 = Measure.executionTime(() -> {
        });
        Measure measure2 = Measure.executionTime(() -> 1);
        assertNotNull(measure1);
        assertNotNull(measure2);
    }

    @Test
    void inMillisMeasuresCorrectSupplier() {
        final long SLEEP_TIME_IN_MILLIS = 100;
        ExecutionResult<Integer> executionResult =
                Measure.executionTime(() -> {
                    Thread.sleep(SLEEP_TIME_IN_MILLIS);
                    return 1;
                }).inMillis();
        assertEquals(SLEEP_TIME_IN_MILLIS, executionResult.getTime(), SLEEP_TIME_IN_MILLIS / 50);
    }

    @Test
    void inNanosMeasuresCorrectSupplier() {
        final long SLEEP_TIME_IN_NANOS = 1000000000L;
        ExecutionResult<Integer> executionResult =
                Measure.executionTime(() -> {
                    Thread.sleep(nanosToMillis(SLEEP_TIME_IN_NANOS));
                    return 1;
                }).inNanos();
        assertEquals(SLEEP_TIME_IN_NANOS, executionResult.getTime(), SLEEP_TIME_IN_NANOS / 50);
    }

    @Test
    void inMillisMeasuresCorrectAction() {
        final long SLEEP_TIME_IN_MILLIS = 100;
        ExecutionResult<Void> executionResult =
                Measure.executionTime(() -> Thread.sleep(SLEEP_TIME_IN_MILLIS)).inMillis();
        assertEquals(SLEEP_TIME_IN_MILLIS, executionResult.getTime(), SLEEP_TIME_IN_MILLIS / 50);
    }

    @Test
    void inNanosMeasuresCorrectAction() {
        final long SLEEP_TIME_IN_NANOS = 1000000000L;
        ExecutionResult<Void> executionResult =
                Measure.executionTime(() -> Thread.sleep(nanosToMillis(SLEEP_TIME_IN_NANOS))).inNanos();
        assertEquals(SLEEP_TIME_IN_NANOS, executionResult.getTime(), SLEEP_TIME_IN_NANOS / 50);
    }

    @Test
    void inNanosReturnsCorrectResult() {
        final int RESULT = 1103123;
        ExecutionResult<Integer> executionResult =
                Measure.executionTime(() -> RESULT)
                        .inNanos();
        assertEquals(RESULT, executionResult.getResult());
    }

    @Test
    void inMillisReturnsCorrectResult() {
        final String RESULT = "SOME STRING RESULT";
        ExecutionResult<String> executionResult =
                Measure.executionTime(() -> RESULT)
                        .inMillis();
        assertEquals(RESULT, executionResult.getResult());
    }

    @Test
    void inMillisSetsCorrectMeasureUnit() {
        ExecutionResult<Void> executionResult =
                Measure.executionTime(() -> {
                }).inMillis();
        assertEquals(MeasureUnit.MILLIS, executionResult.getMeasureUnit());
    }

    @Test
    void inNanosSetsCorrectMeasureUnit() {
        ExecutionResult<Integer> executionResult =
                Measure.executionTime(() -> 1)
                        .inNanos();
        assertEquals(MeasureUnit.NANOS, executionResult.getMeasureUnit());
    }
}