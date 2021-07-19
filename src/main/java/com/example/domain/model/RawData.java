package com.example.domain.model;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;

import lombok.EqualsAndHashCode;

/**
 * Raw data represents a json value of an object in Salesforce.
 *
 * @author Saša Bolić
 */
@EqualsAndHashCode
public final class RawData {

  private final String value;

  public static RawData of(String value) {
    return new RawData(value);
  }

  private RawData(String value) {
    hasText(value, "Raw data cannot be empty.");
    isTrue(value.startsWith("{") && value.endsWith("}"), "Raw data format is incorrect.");

    this.value = value;
  }

  /**
   * Gets value of a raw data.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }
}
