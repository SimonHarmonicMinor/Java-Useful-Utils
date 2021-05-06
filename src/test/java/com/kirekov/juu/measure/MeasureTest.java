package com.kirekov.juu.measure;

import static com.kirekov.juu.measure.MeasureConverter.nanosToMillis;
import static com.kirekov.juu.measure.MeasureConverter.secondsToMillis;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kirekov.juu.lambda.Action;
import java.util.function.Supplier;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@linkplain Measure}.
 *
 * @see Measure
 */
class MeasureTest {

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
    long millis = Measure
        .executionTime(() -> await().pollDelay(Duration.ONE_SECOND).until(() -> true)).inMillis()
        .getTime();
    assertTrue(millis >= 1000);
  }

  @Test
  void inNanosMeasuresCorrectAction() {
    long nanos = Measure
        .executionTime(() -> await().pollDelay(Duration.ONE_SECOND).until(() -> true)).inNanos()
        .getTime();
    assertTrue(nanosToMillis(nanos) >= 1000);
  }

  @Test
  void inNanosReturnsCorrectResult() {
    final int result = 1103123;
    ExecutionResult<Integer> executionResult =
        Measure.executionTime(() -> result)
            .inNanos();
    assertEquals(result, executionResult.getResult());
  }

  @Test
  void inMillisReturnsCorrectResult() {
    final String result = "SOME STRING RESULT";
    ExecutionResult<String> executionResult =
        Measure.executionTime(() -> result)
            .inMillis();
    assertEquals(result, executionResult.getResult());
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