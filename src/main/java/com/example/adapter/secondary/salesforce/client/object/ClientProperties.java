package com.example.adapter.secondary.salesforce.client.object;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Properties used to for Salesforce object web client.
 *
 * @author Saša Bolić
 */
@ConstructorBinding
@Getter
@RequiredArgsConstructor
@ConfigurationProperties("salesforce.client")
public class ClientProperties {

  private final Duration connectionTimoutLimit;
  private final Duration readTimeoutLimit;
  private final boolean connectionKeepAlive;
  private final int bufferLimit;
  private final boolean compressionEnabled;
  private final Retry retry;

  @Getter
  @RequiredArgsConstructor
  public static class Retry {

    private final int maxAttempts;
    private final Duration minBackoff;
  }
}
