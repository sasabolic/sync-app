package com.example.adapter.secondary.postgres;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import com.example.adapter.secondary.postgres.entity.CustomerEntity;
import com.example.adapter.secondary.postgres.repository.PostgresCustomerRepository;
import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryAdapterTest {

  @Mock
  PostgresCustomerRepository postgresCustomerRepository;

  CustomerRepositoryAdapter customerRepositoryAdapter;

  @BeforeEach
  void setUp() {
    customerRepositoryAdapter = new CustomerRepositoryAdapter(
        postgresCustomerRepository
    );
  }

  @Test
  void whenFindByUsername_thenComplete() {
    given(postgresCustomerRepository.findByUsername(anyString()))
        .willReturn(Mono.just(new CustomerEntity(
            Customer.of(
                Username.of("mock_username"),
                ClientId.of("mock_client_id"))
        )));

    Mono<Customer> result = customerRepositoryAdapter.findByUsername(Username.of("mock_username"));

    StepVerifier.create(result)
        .expectNextMatches(customer -> customer.getUsername().equals(Username.of("mock_username")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenFindByUsername_thenError() {
    given(postgresCustomerRepository.findByUsername(anyString()))
        .willReturn(Mono.error(new RuntimeException("Error")));

    Mono<Customer> result = customerRepositoryAdapter.findByUsername(Username.of("mock_username"));

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void whenFindByLeastSyncedOn_thenComplete() {
    given(postgresCustomerRepository.findByLeastSyncedOn(anyInt()))
        .willReturn(Flux.just(new CustomerEntity(
            Customer.of(
                Username.of("mock_username"),
                ClientId.of("mock_client_id"))
        )));

    Flux<Customer> result = customerRepositoryAdapter.findByLeastSyncedOn(1);

    StepVerifier.create(result)
        .expectNextMatches(customer -> customer.getUsername().equals(Username.of("mock_username")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenFindByLeastSyncedOn_thenError() {
    given(postgresCustomerRepository.findByLeastSyncedOn(anyInt()))
        .willReturn(Flux.error(new RuntimeException("Error")));

    Flux<Customer> result = customerRepositoryAdapter.findByLeastSyncedOn(1);

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void whenSave_thenComplete() {
    Customer customer = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );
    given(postgresCustomerRepository.save(isA(CustomerEntity.class)))
        .willReturn(Mono.just(new CustomerEntity(
            customer
        )));

    Mono<Customer> result = customerRepositoryAdapter.save(customer);

    StepVerifier.create(result)
        .expectNextMatches(c -> c.getUsername().equals(Username.of("mock_username")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenSave_thenError() {
    Customer customer = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );
    given(postgresCustomerRepository.save(isA(CustomerEntity.class)))
        .willReturn(Mono.error(new RuntimeException("Error")));

    Mono<Customer> result = customerRepositoryAdapter.save(customer);

    StepVerifier.create(result)
        .verifyError();
  }
}