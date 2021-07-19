package com.example.domain.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import com.example.port.CustomerRepository;
import com.example.port.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class DefaultCustomerServiceTest {

  @Mock
  CustomerRepository repository;

  CustomerService service;

  @BeforeEach
  void setUp() {
    service = new DefaultCustomerService(
        repository
    );
  }

  @Test
  void whenFindByLeastSyncedOn_thenExpectNextAndComplete() {
    given(repository.findByLeastSyncedOn(anyInt()))
        .willReturn(Flux.just(Customer.of(Username.of("mock_username"),
            ClientId.of("mock_client_id"))));

    Flux<Customer> result = service.findByLeastSyncedOn(1);

    StepVerifier.create(result)
        .expectNextMatches(customer -> customer.getUsername().equals(Username.of("mock_username")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenFindByLeastSyncedOn_thenError() {
    given(repository.findByLeastSyncedOn(anyInt()))
        .willReturn(Flux.error(new RuntimeException("Error")));

    Flux<Customer> result = service.findByLeastSyncedOn(1);

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void whenSave_thenExpectNextAndComplete() {
    Customer c = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );
    given(repository.save(isA(Customer.class))).willReturn(Mono.just(c));

    Mono<Customer> result = service.save(Customer.of(Username.of("mock_username"),
        ClientId.of("mock_client_id")));

    StepVerifier.create(result)
        .expectNextMatches(customer -> customer.getUsername().equals(Username.of("mock_username")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenSave_thenError() {
    given(repository.save(isA(Customer.class))).willReturn(Mono.error(new RuntimeException("Error")));

    Mono<Customer> result = service.save(Customer.of(Username.of("mock_username"),
        ClientId.of("mock_client_id")));

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void whenCreate_thenExpectNextAndComplete() {
    Customer c = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );
    given(repository.findByUsername(isA(Username.class))).willReturn(Mono.empty());
    given(repository.save(isA(Customer.class))).willReturn(Mono.just(c));

    Mono<Customer> result = service.create(Customer.of(Username.of("mock_username"),
        ClientId.of("mock_client_id")));

    StepVerifier.create(result)
        .expectNextMatches(customer -> customer.getUsername().equals(Username.of("mock_username")))
        .verifyComplete();
  }

  @Test
  void givenUsernameExits_whenCreate_thenError() {
    given(repository.findByUsername(isA(Username.class))).willReturn(Mono.just(Customer.of(Username.of("mock_username"),
        ClientId.of("mock_client_id"))));

    Mono<Customer> result = service.create(Customer.of(Username.of("mock_username"),
        ClientId.of("mock_client_id")));

    StepVerifier.create(result)
        .verifyError();
  }
}