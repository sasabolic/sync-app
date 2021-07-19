package com.example.adapter.secondary.salesforce.client.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Properties used to build OAuth access token request.
 *
 * @author Saša Bolić
 */
@ConstructorBinding
@Getter
@RequiredArgsConstructor
@ConfigurationProperties("salesforce.client.oauth")
public class OAuthProperties {

  private final String tokenUrl;
  private final String grantType;
}
