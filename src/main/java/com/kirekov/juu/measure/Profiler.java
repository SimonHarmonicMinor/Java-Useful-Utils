package com.kirekov.juu.measure;

import static com.kirekov.juu.measure.MeasureConverter.millisToSeconds;

import java.util.function.Supplier;

/**
 * Measures time between object instantiating and stopping measuring. The class is not thread-safe.
 *
 * @since 0.1
 */
public class Profiler {

  private static final long STILL_MEASURING = -1L;

  private final Supplier<Long> stopMeasuringSupplier;
  private final MeasureUnit measureUnit;

  private long measuringResult = STILL_MEASURING;

  /**
   * Instantiates new {@link Profiler} object and starts measuring in millis.
   *
   * @return new object with millis measuring
   */
  public static Profiler startMeasuringInMillis() {
    final long startPoint = System.currentTimeMillis();
    return new Profiler(
        () -> System.currentTimeMillis() - startPoint,
        MeasureUnit.MILLIS
    );
  }

  /**
   * Instantiates new {@link Profiler} object and starts measuring in nanos.
   *
   * @return new object with nanos measuring
   */
  public static Profiler startMeasuringInNanos() {
    final long startPoint = System.nanoTime();
    return new Profiler(
        () -> System.nanoTime() - startPoint,
        MeasureUnit.NANOS
    );
  }

  /**
   * Instantiates new {@link Profiler} object and starts measuring in seconds.
   *
   * @return new object with seconds measuring
   * @since 1.1
   */
  public static Profiler startMeasuringInSeconds() {
    final long startPoint = System.currentTimeMillis();
    return new Profiler(
        () -> millisToSeconds(System.currentTimeMillis() - startPoint),
        MeasureUnit.SECONDS
    );
  }

  private Profiler(Supplier<Long> stopMeasuringSupplier, MeasureUnit measureUnit) {
    this.stopMeasuringSupplier = stopMeasuringSupplier;
    this.measureUnit = measureUnit;
  }

  /**
   * Gets {@linkplain MeasureUnit}.
   *
   * @return units of measuring
   */
  public MeasureUnit getMeasureUnit() {
    return measureUnit;
  }

  /**
   * Stops measuring and returns time. Multiple calls don't affect the result.
   *
   * @return measured time
   */
  public long stopMeasuring() {
    if (measuringResult == STILL_MEASURING) {
      measuringResult = stopMeasuringSupplier.get();
    }
    return measuringResult;
  }
}
