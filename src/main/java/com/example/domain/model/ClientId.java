package com.example.domain.model;

import static org.springframework.util.Assert.hasText;

import lombok.EqualsAndHashCode;

/**
 * Client ID represents a Client Key crated customer's Salesforce instance for connecting the
 * sync-app.
 *
 * @author Saša Bolić
 */
@EqualsAndHashCode
public final class ClientId {

  private final String value;

  public static ClientId of(String value) {
    return new ClientId(value);
  }

  private ClientId(String value) {
    hasText(value, "Client ID cannot be empty.");

    this.value = value;
  }

  /**
   * Gets value of a client ID.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }
}
