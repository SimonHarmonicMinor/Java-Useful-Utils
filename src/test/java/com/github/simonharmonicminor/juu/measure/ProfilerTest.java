package com.github.simonharmonicminor.juu.measure;

import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

import static com.github.simonharmonicminor.juu.measure.MeasureConverter.millisToNanos;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfilerTest {
    private static final int DELTA_MILLIS = 100;

    @Test
    void ifDefinedMeasureUnitIsCorrect() {
        Profiler profilerMillis = Profiler.startMeasuringInMillis();
        Profiler profilerNanos = Profiler.startMeasuringInNanos();
        Profiler profilerSeconds = Profiler.startMeasuringInSeconds();

        assertEquals(MeasureUnit.MILLIS, profilerMillis.getMeasureUnit());
        assertEquals(MeasureUnit.NANOS, profilerNanos.getMeasureUnit());
        assertEquals(MeasureUnit.SECONDS, profilerSeconds.getMeasureUnit());
    }

    @Test
    void multipleStopsReturnTheSame() {
        Profiler profilerMillis = Profiler.startMeasuringInMillis();
        long millisTime1 = profilerMillis.stopMeasuring();
        long millisTime2 = profilerMillis.stopMeasuring();

        Profiler profilerNanos = Profiler.startMeasuringInNanos();
        long nanosTime1 = profilerNanos.stopMeasuring();
        long nanosTime2 = profilerNanos.stopMeasuring();

        Profiler profilerSeconds = Profiler.startMeasuringInSeconds();
        long secondsTime1 = profilerSeconds.stopMeasuring();
        long secondsTime2 = profilerSeconds.stopMeasuring();

        assertEquals(millisTime1, millisTime2);
        assertEquals(nanosTime1, nanosTime2);
        assertEquals(secondsTime1, secondsTime2);
    }

    @Test
    void ifMeasuringInMillisIsCorrect() {
        Profiler profilerMillis = Profiler.startMeasuringInMillis();
        await().pollDelay(Duration.ONE_SECOND).until(() -> true);
        long res = profilerMillis.stopMeasuring();
        assertEquals(1000, res, DELTA_MILLIS);
    }

    @Test
    void ifMeasuringInNanosIsCorrect() {
        Profiler profilerMillis = Profiler.startMeasuringInNanos();
        await().pollDelay(Duration.ONE_SECOND).until(() -> true);
        long res = profilerMillis.stopMeasuring();
        assertEquals(millisToNanos(1000), res, millisToNanos(DELTA_MILLIS));
    }

    @Test
    void ifMeasuringInSecondsIsCorrect() {
        Profiler profilerSeconds = Profiler.startMeasuringInSeconds();
        await().pollDelay(Duration.ONE_SECOND).until(() -> true);
        long res = profilerSeconds.stopMeasuring();
        assertEquals(1, res);
    }

}