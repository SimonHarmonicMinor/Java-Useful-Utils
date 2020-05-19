package com.github.simonharmonicminor.juu.measure;

import com.github.simonharmonicminor.juu.lambda.Action;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.github.simonharmonicminor.juu.measure.MeasureConverter.*;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see Measure
 */
class MeasureTest {
    private static Duration ONE_SECOND_AND_A_HALF = new Duration(1500, TimeUnit.MILLISECONDS);

    @Test
    void throwsNullPointerIfActionIsNull() {
        assertThrows(NullPointerException.class, () -> Measure.executionTime((Supplier<Integer>) null));
    }

    @Test
    void throwsNullPointerIfSupplierIsNull() {
        assertThrows(NullPointerException.class, () -> Measure.executionTime((Action) null));
    }

    @Test
    void executionTimeReturnsNotNull() {
        Measure<Void> measure1 = Measure.executionTime(() -> {
        });
        Measure<Integer> measure2 = Measure.executionTime(() -> 1);
        assertNotNull(measure1);
        assertNotNull(measure2);
    }

    @Test
    void inMillisMeasuresCorrectSupplier() {
        long millis = Measure.executionTime(() -> {
            await().pollDelay(Duration.ONE_SECOND).until(() -> true);
            return true;
        }).inMillis().getTime();
        assertTrue(millis >= 1000);
    }

    @Test
    void inNanosMeasuresCorrectSupplier() {
        long nanos = Measure.executionTime(() -> {
            await().pollDelay(Duration.ONE_SECOND).until(() -> true);
            return true;
        }).inNanos().getTime();
        assertTrue(nanosToMillis(nanos) >= 1000);
    }

    @Test
    void inSecondsMeasureCorrectSupplier() {
        long seconds = Measure.executionTime(() -> {
            await().pollDelay(Duration.ONE_SECOND).until(() -> true);
            return true;
        }).inSeconds().getTime();
        assertTrue(secondsToMillis(seconds) >= 1000);
    }

    @Test
    void inMillisMeasuresCorrectAction() {
        long millis = Measure.executionTime(() -> await().pollDelay(Duration.ONE_SECOND).until(() -> true)).inMillis().getTime();
        assertTrue(millis >= 1000);
    }

    @Test
    void inNanosMeasuresCorrectAction() {
        long nanos = Measure.executionTime(() -> await().pollDelay(Duration.ONE_SECOND).until(() -> true)).inNanos().getTime();
        assertTrue(nanosToMillis(nanos) >= 1000);
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

    @Test
    void inSecondsReturnsCorrectResult() {
        final int result = 12314;
        ExecutionResult<Integer> executionResult =
                Measure.executionTime(() -> result)
                        .inSeconds();
        assertEquals(result, executionResult.getResult());
    }

    @Test
    void inSecondsSetsCorrectMeasureUnit() {
        ExecutionResult<Void> executionResult =
                Measure.executionTime(() -> {
                }).inSeconds();
        assertEquals(MeasureUnit.SECONDS, executionResult.getMeasureUnit());
    }
}