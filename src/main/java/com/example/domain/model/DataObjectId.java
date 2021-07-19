package com.example.domain.model;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Data object id represents a value of data object in Salesforce.
 *
 * @author Saša Bolić
 */
@EqualsAndHashCode
@ToString
public final class DataObjectId {

  public static final int DATA_OBJECT_ID_SIZE = 18;

  private final String value;

  public DataObjectId(String value) {
    hasText(value, "Data object id cannot be empty.");
    isTrue(value.length() == DATA_OBJECT_ID_SIZE, "Data object id size is incorrect.");

    this.value = value;
  }

  /**
   * Gets value of a data object id.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }
}
