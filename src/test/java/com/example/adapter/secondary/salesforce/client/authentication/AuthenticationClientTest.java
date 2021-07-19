package com.example.adapter.secondary.salesforce.client.authentication;

import static org.mockito.Mockito.mock;

import com.example.adapter.secondary.salesforce.client.authentication.dto.AuthenticationResponse;
import com.example.adapter.secondary.salesforce.client.authentication.jwt.JwtBuilder;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AuthenticationClientTest {

  AuthenticationClient client;

  MockWebServer mockWebServer;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    WebClient webClient = WebClient.create(mockWebServer.url("/").toString());

    client = new AuthenticationClient(
        webClient,
        mock(JwtBuilder.class),
        new OAuthProperties(
            "/mock_token_url",
            "mock_grant_type"
        )
    );
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void givenSuccessfulResponse_whenLogin_thenReturnAccessToken() {
    mockWebServer.enqueue(new MockResponse()
        .setBody("""
                        {
                            "access_token": "mock_access_token",
                            "scope": "web api",
                            "instance_url": "https://mock_domain.my.salesforce.com",
                            "id": "https://login.salesforce.com/id/mock_id",
                            "token_type": "Bearer"
                        }""")
        .addHeader("Content-Type", "application/json"));

    Mono<AuthenticationResponse> result = client.login(
        "mock_client_id",
        "test@email.com");

    StepVerifier.create(result)
        .expectNext(new AuthenticationResponse(
            "mock_access_token",
            "Bearer",
            "web api",
            "https://mock_domain.my.salesforce.com"
        ))
        .verifyComplete();
  }

  @Test
  void givenUnSuccessfulResponse_whenLogin_thenReturnError() {
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(400)
        .addHeader("Content-Type", "application/json")
        .setBody("""
                        {"error":"invalid_grant","error_description":"user hasn't approved this consumer"}
                        """)
    );

    Mono<AuthenticationResponse> result = client.login(
        "mock_client_id",
        "test@email.com");

    StepVerifier.create(result)
        .expectError(WebClientResponseException.class)
        .verify();
  }
}