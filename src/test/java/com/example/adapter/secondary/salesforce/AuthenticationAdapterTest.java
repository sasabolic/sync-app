package com.example.adapter.secondary.salesforce;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.adapter.secondary.salesforce.client.authentication.AuthenticationClient;
import com.example.adapter.secondary.salesforce.client.authentication.dto.AuthenticationResponse;
import com.example.domain.model.Authentication;
import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AuthenticationAdapterTest {

  @Mock
  AuthenticationClient authenticationClient;

  AuthenticationAdapter authenticationAdapter;

  @BeforeEach
  void setUp() {
    authenticationAdapter = new AuthenticationAdapter(
        authenticationClient
    );
  }

  @Test
  void whenAuthenticate_thenExpectAuthenticationAndComplete() {
    given(authenticationClient.login(anyString(), anyString()))
        .willReturn(Mono.just(new AuthenticationResponse(
            "mock_access_token",
            "Bearer",
            "web api",
            "mock_instance_url"
        )));

    Mono<Authentication> result = authenticationAdapter.authenticate(Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    ));

    StepVerifier.create(result)
        .expectNext(new Authentication("Bearer mock_access_token", "mock_instance_url"))
        .verifyComplete();
  }

  @Test
  void givenLoginFailed_whenAuthenticate_thenError() {
    given(authenticationClient.login(anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException("Error")));

    Mono<Authentication> result = authenticationAdapter.authenticate(Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    ));

    StepVerifier.create(result)
        .verifyError();
  }
}