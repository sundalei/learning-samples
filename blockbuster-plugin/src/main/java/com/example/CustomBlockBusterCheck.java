package com.example;

import java.util.Map;

public class CustomBlockBusterCheck {

  public void execute(Map<String, Object> source, Map<String, Object> params) {
    Number threshold = (Number) params.get("gross_earnings_threshold");
    Number gross = (Number) source.get("boxoffice_gross_in_millions");

    source.put("blockbuster", gross.doubleValue() > threshold.doubleValue());
  }
}
