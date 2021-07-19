package com.example.domain.model;

import lombok.Value;

/**
 * Authentication details for specific customer.
 *
 * @author Saša Bolić
 */
@Value
public class Authentication {

  String token;
  String baseUrl;
}
