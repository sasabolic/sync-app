package com.example.domain.model;

import java.util.stream.Stream;

/**
 * Represents the supported data object type .
 *
 * @author Saša Bolić
 */
public enum DataObjectType {

  ACCOUNT("Account"),

  CONTACT("Contact"),

  LEAD("Lead");

  private final String objectName;

  DataObjectType(String objectName) {
    this.objectName = objectName;
  }

  /**
   * Retrieve object name used by Salesforce.
   *
   * @return the string
   */
  public String objectName() {
    return this.objectName;
  }

  public static DataObjectType fromName(String objectName) {
    return Stream
        .of(values())
        .filter(v -> v.objectName.equalsIgnoreCase(objectName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            "Could not determine type based on object name: " + objectName));
  }
}
