package com.github.simonharmonicminor.juu.measure;

import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

import static com.github.simonharmonicminor.juu.measure.MeasureConverter.millisToNanos;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class ProfilerTest {

    @Test
    void ifDefinedMeasureUnitIsCorrect() {
        Profiler profilerMillis = Profiler.startMeasuringInMillis();
        Profiler profilerNanos = Profiler.startMeasuringInNanos();

        assertEquals(MeasureUnit.MILLIS, profilerMillis.getMeasureUnit());
        assertEquals(MeasureUnit.NANOS, profilerNanos.getMeasureUnit());
    }

    @Test
    void doesReturnSpecialConstantWhenStillMeasuring() {
        Profiler profilerMillis = Profiler.startMeasuringInMillis();
        Profiler profilerNanos = Profiler.startMeasuringInNanos();

        assertEquals(Profiler.STILL_MEASURING, profilerMillis.getTime());
        assertEquals(Profiler.STILL_MEASURING, profilerNanos.getTime());
    }

    @Test
    void whenMeasuringIsCompleteStopAndGetReturnTheSame() {
        Profiler profilerMillis = Profiler.startMeasuringInMillis();
        Profiler profilerNanos = Profiler.startMeasuringInNanos();

        long stoppedMillisResult = profilerMillis.stopMeasuring();
        long stoppedNanosResult = profilerNanos.stopMeasuring();

        assertEquals(stoppedMillisResult, profilerMillis.getTime());
        assertEquals(stoppedNanosResult, profilerNanos.getTime());
    }

    @Test
    void doMultiplyCallsStopReturnTheSame() {
        Profiler profilerMillis = Profiler.startMeasuringInMillis();
        long millisTime1 = profilerMillis.stopMeasuring();
        long millisTime2 = profilerMillis.stopMeasuring();

        Profiler profilerNanos = Profiler.startMeasuringInNanos();
        long nanosTime1 = profilerNanos.stopMeasuring();
        long nanosTime2 = profilerNanos.stopMeasuring();

        assertEquals(millisTime1, millisTime2);
        assertEquals(nanosTime1, nanosTime2);
    }

    @Test
    void ifMeasuringInMillisIsCorrect() {
        Profiler profilerMillis = Profiler.startMeasuringInMillis();
        await().pollDelay(Duration.ONE_SECOND).until(() -> true);
        profilerMillis.stopMeasuring();
        assertEquals(1000, profilerMillis.getTime(), 50);
    }

    @Test
    void ifMeasuringInNanosIsCorrect() {
        Profiler profilerMillis = Profiler.startMeasuringInNanos();
        await().pollDelay(Duration.ONE_SECOND).until(() -> true);
        profilerMillis.stopMeasuring();
        assertEquals(millisToNanos(1000), profilerMillis.getTime(), millisToNanos(50));
    }
}