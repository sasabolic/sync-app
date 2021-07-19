package com.example.adapter.primary.rest.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Value;

/**
 * Create customer request DTO.
 *
 * @author Saša Bolić
 */
@Value
public class CreateCustomerRequest {

  @NotEmpty(message = "Username cannot be empty")
  String username;

  @NotEmpty(message = "Client id cannot be empty")
  String clientId;
}
