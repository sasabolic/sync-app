package com.example.domain.service.error;

/**
 * Exception thrown in case customer is duplicated in sync-app.
 *
 * @author Saša Bolić
 */
public class CustomerNotUniqueException extends RuntimeException {

  public CustomerNotUniqueException(String message) {
    super(message);
  }
}
