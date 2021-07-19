package com.example.adapter.secondary.salesforce.client.authentication.jwt;

/**
 * Exception thrown in case a JWT token creation failed.
 *
 * @author Saša Bolić
 */
public class JwtBuilderException extends RuntimeException {

  public JwtBuilderException(String message, Throwable cause) {
    super(message, cause);
  }
}