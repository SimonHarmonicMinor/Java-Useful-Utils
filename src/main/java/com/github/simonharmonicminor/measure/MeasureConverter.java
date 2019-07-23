package com.github.simonharmonicminor.measure;

/**
 * A class which converts one measure unit to another
 */
public class MeasureConverter {
    private static final long MEGA_COEFFICIENT = 1000 * 1000;
    private static final long KILO_COEFFICIENT = 1000;

    /**
     * Convert millis to nanos
     *
     * @param millis time in millis
     * @return time in nanos
     */
    public static long millisToNanos(long millis) {
        return millis * MEGA_COEFFICIENT;
    }

    /**
     * Convert nanos to millis
     *
     * @param nanos time in nanos
     * @return time in millis
     */
    public static long nanosToMillis(long nanos) {
        return nanos / MEGA_COEFFICIENT;
    }

    /**
     * Converts millis to seconds
     *
     * @param millis time in millis
     * @return time in seconds
     */
    public static long millisToSeconds(long millis) {
        return millis / KILO_COEFFICIENT;
    }

    /**
     * Converts nanos to seconds
     *
     * @param nanos time in nanos
     * @return time in seconds
     */
    public static long nanosToSeconds(long nanos) {
        return nanos / KILO_COEFFICIENT / MEGA_COEFFICIENT;
    }

    /**
     * Converts seconds to millis
     *
     * @param seconds time in seconds
     * @return time in millis
     */
    public static long secondsToMillis(long seconds) {
        return seconds * KILO_COEFFICIENT;
    }

    /**
     * Converts seconds to nanos
     *
     * @param seconds time in seconds
     * @return time in nanos
     */
    public static long secondsToNanos(long seconds) {
        return seconds * KILO_COEFFICIENT * MEGA_COEFFICIENT;
    }
}
