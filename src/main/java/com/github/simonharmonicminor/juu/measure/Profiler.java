package com.github.simonharmonicminor.juu.measure;

/**
 * Measures time between object instantiating
 * and stopping measuring
 * @since 0.1
 */
public class Profiler {
    /**
     * A special constant that defines that measuring has not been stopped yet
     */
    public static final long STILL_MEASURING = -1L;

    private final long startPoint;
    private final MeasureUnit measureUnit;
    private long endPoint = STILL_MEASURING;

    /**
     * Instantiates new {@link Profiler} object and starts measuring in millis
     *
     * @return new object with millis measuring
     */
    public static Profiler startMeasuringInMillis() {
        return new Profiler(System.currentTimeMillis(), MeasureUnit.MILLIS);
    }

    /**
     * Instantiates new {@link Profiler} object and starts measuring in nanos
     *
     * @return new object with nanos measuring
     */
    public static Profiler startMeasuringInNanos() {
        return new Profiler(System.nanoTime(), MeasureUnit.NANOS);
    }

    private Profiler(long startPoint, MeasureUnit measureUnit) {
        this.startPoint = startPoint;
        this.measureUnit = measureUnit;
    }

    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }


    /**
     * Returns measured time, calculated after {@link Profiler#stopMeasuring()} call.
     * If measure is not calculated, returns {@link Profiler#STILL_MEASURING}
     *
     * @return measured time or {@link Profiler#STILL_MEASURING}
     */
    public long getTime() {
        if (endPoint == STILL_MEASURING)
            return STILL_MEASURING;
        return endPoint - startPoint;
    }


    /**
     * Stops measuring and returns time. Multiply calls don't affect the result
     * @return measured time
     */
    public long stopMeasuring() {
        if (endPoint == STILL_MEASURING) {
            if (measureUnit == MeasureUnit.MILLIS) {
                endPoint = System.currentTimeMillis();
            } else {
                endPoint = System.nanoTime();
            }
        }
        return getTime();
    }

}
