package com.example.adapter.secondary.salesforce.client.authentication;

import com.example.adapter.secondary.salesforce.client.authentication.dto.AuthenticationResponse;
import com.example.adapter.secondary.salesforce.client.authentication.jwt.JwtBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * The web client used to authenticate the app against Salesforce and retrieve access token.
 *
 * @author Saša Bolić
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationClient {

  private static final String OAUTH_GRANT_TYPE = "grant_type";
  private static final String OAUTH_ASSERTION = "assertion";

  @Qualifier("salesforce-client")
  private final WebClient client;

  private final JwtBuilder jwtBuilder;

  private final OAuthProperties properties;

  /**
   * Logs in app with given clientId to the instance of the Salesforce account owner.
   *
   * @param clientId the client id
   * @param subject  the username of the Salesforce account owner
   * @return the mono
   */
  public Mono<AuthenticationResponse> login(String clientId, String subject) {
    String jwt = jwtBuilder.build(clientId, subject);

    return client
        .post()
        .uri(properties.getTokenUrl())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters
            .fromFormData(OAUTH_GRANT_TYPE, properties.getGrantType())
            .with(OAUTH_ASSERTION, jwt))
        .retrieve()
        .bodyToMono(AuthenticationResponse.class)
        .log()
        .onErrorResume(WebClientResponseException.class,
            ex -> {
              log.error(
                  "Error when trying to authenticate client app. Request={}, StatusCode={}, Body={}",
                  ex.getRequest() != null ? ex.getRequest().getURI() : "NO_REQUEST_URI", ex.getRawStatusCode(), ex.getResponseBodyAsString());
              return Mono.error(ex);
            });
  }
}
