package com.example.adapter.primary.rest;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

import com.example.common.JsonFixtureTest;
import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import com.example.domain.service.error.CustomerNotUniqueException;
import com.example.port.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = CustomerController.class)
class CustomerControllerTest extends JsonFixtureTest {

  @MockBean
  CustomerService customerService;

  @Autowired
  WebTestClient webClient;

  @Test
  void whenCreateCustomer_thenStatusCreatedWithLocationHeader() {
    Customer data = spy(Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    ));

    given(data.getId()).willReturn(1L);
    given(customerService.create(isA(Customer.class))).willReturn(Mono.just(data));

    webClient.post()
        .uri("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(readJSON("/fixtures/create_customer_request.json"))
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().location("/customers/1");
  }

  @Test
  void givenInvalidRequest_whenCreateCustomer_thenStatusBadRequest() {
    webClient.post()
        .uri("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(readJSON("/fixtures/create_customer_request_invalid.json"))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void givenNotUniqueCustomer_whenCreateCustomer_thenStatusBadRequest() {
    given(customerService.create(isA(Customer.class))).willThrow(new CustomerNotUniqueException("Error"));

    webClient.post()
        .uri("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(readJSON("/fixtures/create_customer_request.json"))
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.CONFLICT);
  }
}