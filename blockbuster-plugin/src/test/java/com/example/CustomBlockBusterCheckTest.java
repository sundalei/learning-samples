package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
