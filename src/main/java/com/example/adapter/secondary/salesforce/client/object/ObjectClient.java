package com.example.adapter.secondary.salesforce.client.object;

import com.example.adapter.secondary.salesforce.client.object.dto.request.ObjectDetailsRequest;
import com.example.adapter.secondary.salesforce.client.object.dto.request.QueryObjectsRequest;
import com.example.adapter.secondary.salesforce.client.object.dto.response.ObjectDetailsResponse;
import com.example.adapter.secondary.salesforce.client.object.dto.response.ObjectQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * The web client used to query and fetch Salesforce objects.
 *
 * @author Saša Bolić
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class ObjectClient {

  @Qualifier("salesforce-client")
  private final WebClient client;

  private final ClientProperties properties;

  /**
   * Query the modified Salesforce objects.
   *
   * @param request the request
   * @return the objects
   */
  public Flux<ObjectQueryResponse> getObjects(QueryObjectsRequest request) {
    return queryObjects(request.url(), request.token())
        .expand(response -> {
          if (response.getNextRecordsUrl() == null) {
            return Mono.empty();
          }
          return queryObjects(request.baseUrl() + response.getNextRecordsUrl(),
              request.token());
        });
  }

  /**
   * Gets object details.
   *
   * @param request the request
   * @return the object details
   */
  public Mono<ObjectDetailsResponse> getObjectDetails(ObjectDetailsRequest request) {
    return client
        .get()
        .uri(request.url())
        .header(HttpHeaders.AUTHORIZATION, request.token())
        .retrieve()
        .bodyToMono(ObjectDetailsResponse.class)
        .log()
        .onErrorResume(WebClientResponseException.class,
            ex -> {
              log.error(
                  "Error when trying to fetch object details. Request={}, StatusCode={}, Body={}",
                  ex.getRequest() != null ? ex.getRequest().getURI() : "NO_REQUEST_URI", ex.getRawStatusCode(), ex.getResponseBodyAsString());
              return Mono.error(ex);
            })
        .retryWhen(Retry.backoff(
            properties.getRetry().getMaxAttempts(),
            properties.getRetry().getMinBackoff()
        ));
  }

  private Mono<ObjectQueryResponse> queryObjects(String url, String tokenValue) {
    return client
        .get()
        .uri(url)
        .header(HttpHeaders.AUTHORIZATION, tokenValue)
        .retrieve()
        .bodyToMono(ObjectQueryResponse.class)
        .retryWhen(Retry.backoff(
            properties.getRetry().getMaxAttempts(),
            properties.getRetry().getMinBackoff()
        ));
  }
}

