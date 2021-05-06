package com.kirekov.juu.measure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test cases for {@linkplain MeasureConverter}.
 *
 * @see MeasureConverter
 */
class MeasureConverterTest {

  @Test
  void millisToNanos() {
    final long millis = 23123;
    final long nanos = millis * 1000 * 1000;
    long convertedNanos = MeasureConverter.millisToNanos(millis);
    assertEquals(nanos, convertedNanos);
  }

  @Test
  void nanosToMillis() {
    final long nanos = 124124243469L;
    final long millis = nanos / 1000 / 1000;
    long convertedMillis = MeasureConverter.nanosToMillis(nanos);
    assertEquals(millis, convertedMillis);
  }

  @Test
  void millisToSeconds() {
    final long millis = 12314124L;
    final long seconds = millis / 1000;
    long convertedSeconds = MeasureConverter.millisToSeconds(millis);
    assertEquals(seconds, convertedSeconds);
  }

  @Test
  void nanosToSeconds() {
    final long nanos = 123123135235235L;
    final long seconds = nanos / 1000 / 1000 / 1000;
    long convertedSeconds = MeasureConverter.nanosToSeconds(nanos);
    assertEquals(seconds, convertedSeconds);
  }

  @Test
  void secondsToMillis() {
    final long seconds = 6906456L;
    final long millis = seconds * 1000;
    long convertedMillis = MeasureConverter.secondsToMillis(seconds);
    assertEquals(millis, convertedMillis);
  }

  @Test
  void secondsToNanos() {
    final long seconds = 123;
    final long nanos = seconds * 1000 * 1000 * 1000;
    long convertedNanos = MeasureConverter.secondsToNanos(seconds);
    assertEquals(nanos, convertedNanos);
  }

}