package com.example.adapter.primary.rest.config;

import com.example.adapter.primary.rest.dto.ErrorResponse;
import com.example.domain.service.error.CustomerNotUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handling errors in REST resources.
 *
 * @author Saša Bolić
 */
@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(CustomerNotUniqueException.class)
  public ResponseEntity<ErrorResponse> handleNotUniqueException(CustomerNotUniqueException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
  }
}
