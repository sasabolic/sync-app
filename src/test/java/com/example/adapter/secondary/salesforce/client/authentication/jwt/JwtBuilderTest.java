package com.example.adapter.secondary.salesforce.client.authentication.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class JwtBuilderTest {

  private static final String MOCK_AUDIENCE = "https://login.mock.com";
  private static final String MOCK_ISSUER = "mock_client_id";
  private static final String MOCK_SUBJECT = "test@mock.com";

  static JwtBuilder builder;

  @Disabled
  @Test
  void whenBuild_thenReturnToken() throws IOException {
    String privateKeyContent;
    try (InputStream keyStream = JwtBuilderTest.class.getClassLoader().getResourceAsStream("security/server.key")) {
      assertThat(keyStream).isNotNull();

      privateKeyContent = new BufferedReader(
          new InputStreamReader(keyStream, StandardCharsets.UTF_8))
          .lines()
          .collect(Collectors.joining("\n"));
    }
    builder = new JwtBuilder(new JwtProperties(MOCK_AUDIENCE, privateKeyContent, Duration.ZERO));

    String result = builder.build(MOCK_ISSUER, MOCK_SUBJECT);

    assertThat(result).isNotBlank();
  }

  @Test
  void givenInvalidPrivateKey_whenBuild_thenThrowException() {
    String privateKeyContent = "INVALID_CONTENT";
    builder = new JwtBuilder(new JwtProperties(MOCK_AUDIENCE, privateKeyContent, Duration.ZERO));

    assertThatThrownBy(() -> builder.build(MOCK_ISSUER, MOCK_SUBJECT))
        .isInstanceOf(JwtBuilderException.class)
        .hasMessage("Error creating JWT token");
  }
}