package com.example;

import java.util.Map;

public class CustomBlockBusterCheck {

  public void execute(Map<String, Object> source, Map<String, Object> params) {
    if (source == null) {
      throw new IllegalArgumentException("Source must not be null!");
    }

    Map<String, Object> safeParams = params == null ? Map.of() : params;

    Object rawGross = source.get("boxoffice_gross_in_millions");
    Object rawThreshold = safeParams.getOrDefault("gross_earnings_threshold", 125);

    if (!(rawGross instanceof Number gross)) {
      throw new IllegalArgumentException("[boxoffice_gross_in_millions] must be numeric");
    }

    if (!(rawThreshold instanceof Number threshold)) {
      throw new IllegalArgumentException("[gross_earnings_threshold] must be numeric");
    }

    source.put("blockbuster", gross.doubleValue() > threshold.doubleValue());
  }
}
