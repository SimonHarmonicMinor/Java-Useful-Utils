package com.github.simonharmonicminor.juu.measure;

import com.github.simonharmonicminor.juu.lambda.Action;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.github.simonharmonicminor.juu.measure.MeasureConverter.nanosToSeconds;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @see Measure
 */
class MeasureTest {
    private static final int DELTA_MILLIS = 100;
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
        await().atLeast(Duration.ONE_SECOND)
                .atMost(ONE_SECOND_AND_A_HALF)
                .until(() -> Measure.executionTime(() -> {
                    await().pollDelay(Duration.ONE_SECOND).until(() -> true);
                    return true;
                }).inMillis().getResult());
    }

    @Test
    void inNanosMeasuresCorrectSupplier() {
        await().atLeast(Duration.ONE_SECOND)
                .atMost(ONE_SECOND_AND_A_HALF)
                .until(() -> Measure.executionTime(() -> {
                    await().pollDelay(Duration.ONE_SECOND).until(() -> true);
                    return true;
                }).inNanos().getResult());
    }

    @Test
    void inSecondsMeasureCorrectSupplier() {
        await().atLeast(Duration.ONE_SECOND)
                .atMost(ONE_SECOND_AND_A_HALF)
                .until(() -> Measure.executionTime(() -> {
                    await().pollDelay(Duration.ONE_SECOND).until(() -> true);
                    return true;
                }).inSeconds().getResult());
    }

    @Test
    void inMillisMeasuresCorrectAction() {
        await().atLeast(Duration.ONE_SECOND)
                .atMost(ONE_SECOND_AND_A_HALF)
                .until(() -> {
                    long time = Measure.executionTime(
                            () -> await().pollDelay(Duration.ONE_SECOND).until(() -> true)
                    ).inSeconds().getTime();
                    assertEquals(time, 1);
                    return true;
                });
    }

    @Test
    void inNanosMeasuresCorrectAction() {
        await().atLeast(Duration.ONE_SECOND)
                .atMost(ONE_SECOND_AND_A_HALF)
                .until(() -> {
                    long time = Measure.executionTime(
                            () -> await().pollDelay(Duration.ONE_SECOND).until(() -> true)
                    ).inNanos().getTime();
                    assertEquals(nanosToSeconds(time), 1);
                    return true;
                });
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