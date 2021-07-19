package com.example.adapter.secondary.salesforce.client.authentication.jwt;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Properties used to build JWT.
 *
 * @author Saša Bolić
 */
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("salesforce.client.jwt")
public class JwtProperties {

  private final String audience;
  private final String privateKey;
  private final Duration expiration;
}
