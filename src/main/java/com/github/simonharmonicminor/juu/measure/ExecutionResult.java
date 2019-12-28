package com.github.simonharmonicminor.juu.measure;

import java.util.Objects;

/**
 * A class which contains the result of function execution and time spent for it
 *
 * @param <T> function result type
 * @see Measure
 * @see MeasureUnit
 * @since 0.1
 */
public class ExecutionResult<T> {
    private final T result;
    private final long time;
    private final MeasureUnit measureUnit;

    /**
     * Instantiates object
     *
     * @param result      result of calculation, can be null
     * @param time        time spent
     * @param measureUnit measure units, cannot be null
     * @throws NullPointerException if measureUnit is null
     */
    public ExecutionResult(T result, long time, MeasureUnit measureUnit) {
        this.result = result;
        this.time = time;
        this.measureUnit = Objects.requireNonNull(measureUnit);
    }

    /**
     * @return function result
     */
    public T getResult() {
        return result;
    }

    /**
     * @return time spent
     */
    public long getTime() {
        return time;
    }

    /**
     * @return measure unit
     */
    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionResult<?> that = (ExecutionResult<?>) o;
        return time == that.time &&
                Objects.equals(result, that.result) &&
                measureUnit == that.measureUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, time, measureUnit);
    }
}
