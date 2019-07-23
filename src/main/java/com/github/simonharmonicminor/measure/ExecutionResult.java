package com.github.simonharmonicminor.measure;

/**
 * A class which contains the result of function execution and time spent for it
 *
 * @param <T> function result type
 * @see Measure
 * @see MeasureUnit
 */
public class ExecutionResult<T> {
    private static final ExecutionResult<?> FAILED = new ExecutionResult<>(null, -1, null);

    private final T result;
    private final long time;
    private final MeasureUnit measureUnit;

    /**
     * Returns an immutable instance which defines that measurement has failed
     * @param <T> function return type
     * @return a failed result instance
     */
    public static <T> ExecutionResult<T> failed() {
        return (ExecutionResult<T>) FAILED;
    }

    public ExecutionResult(T result, long time, MeasureUnit measureUnit) {
        this.result = result;
        this.time = time;
        this.measureUnit = measureUnit;
    }

    /**
     * Check whether execution result was failed or not
     * @return true if measurement has failed
     */
    public boolean isFailed() {
        return this == FAILED;
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
}
