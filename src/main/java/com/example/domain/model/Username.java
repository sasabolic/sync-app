package com.example.domain.model;

import static org.springframework.util.Assert.hasText;

import lombok.EqualsAndHashCode;

/**
 * Username represents a customer's username in Salesforce.
 *
 * @author Saša Bolić
 */
@EqualsAndHashCode
public final class Username {

  private final String value;

  public static Username of(String value) {
    return new Username(value);
  }

  private Username(String value) {
    hasText(value, "Username cannot be empty.");

    this.value = value;
  }

  /**
   * Gets value of a username.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }
}
