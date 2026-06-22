package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CustomBlockBusterCheckTest {

  private final CustomBlockBusterCheck check = new CustomBlockBusterCheck();

  @Test
  void marksDecimalGrossAboveIntegerThresholdAsBlockbuster() {
    Map<String, Object> source = new HashMap<>();
    source.put("boxoffice_gross_in_millions", 134.8);

    check.execute(source, Map.of("gross_earnings_threshold", 125));

    assertEquals(true, source.get("blockbuster"));
  }

  @Test
  void marksGrossAtThresholdAsNotBlockbuster() {
    Map<String, Object> source = new HashMap<>();
    source.put("boxoffice_gross_in_millions", 125);

    check.execute(source, Map.of("gross_earnings_threshold", 125.0));

    assertEquals(false, source.get("blockbuster"));
  }

  @Test
  void usesDefaultThresholdWhenParamsAreNull() {
    Map<String, Object> source = new HashMap<>();
    source.put("boxoffice_gross_in_millions", 134.8);

    check.execute(source, null);

    assertEquals(true, source.get("blockbuster"));
  }

  @Test
  void usesDefaultThresholdWhenParamsAreEmpty() {
    Map<String, Object> source = new HashMap<>();
    source.put("boxoffice_gross_in_millions", 124.9);

    check.execute(source, Map.of());

    assertEquals(false, source.get("blockbuster"));
  }

  @Test
  void rejectsNullSource() {
    assertThrows(IllegalArgumentException.class, () -> check.execute(null, Map.of()));
  }

  @Test
  void rejectsMissingGross() {
    assertThrows(
        IllegalArgumentException.class,
        () -> check.execute(new HashMap<>(), Map.of("gross_earnings_threshold", 125)));
  }

  @Test
  void rejectsNonNumericThreshold() {
    Map<String, Object> source = new HashMap<>();
    source.put("boxoffice_gross_in_millions", 134.8);

    assertThrows(
        IllegalArgumentException.class,
        () -> check.execute(source, Map.of("gross_earnings_threshold", "125")));
  }
}
