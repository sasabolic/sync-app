package com.example.domain.service.sync;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;

import com.example.domain.model.Authentication;
import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.SyncDataObjectCommand;
import com.example.domain.model.Username;
import com.example.port.AuthenticationService;
import com.example.port.CustomerService;
import com.example.port.CustomerSyncService;
import com.example.port.SyncDataObjectProcessor;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class DefaultCustomerSyncServiceTest {

  @Mock
  SyncDataObjectProcessor syncDataObjectProcessor;

  @Mock
  AuthenticationService authenticationService;

  @Mock
  CustomerService customerService;

  CustomerSyncService customerSyncService;

  @BeforeEach
  void setUp() {
    customerSyncService = new DefaultCustomerSyncService(
        syncDataObjectProcessor,
        authenticationService,
        customerService,
        new SyncProperties(
            Duration.ZERO,
            Duration.ZERO,
            1
        )
    );
  }

  @Test
  void whenSync_thenComplete() {
    Customer customer = spy(Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    ));
    given(authenticationService.authenticate(isA(Customer.class)))
        .willReturn(Mono.just(new Authentication("mock_token", "mock_base_url")));
    given(syncDataObjectProcessor.execute(isA(SyncDataObjectCommand.class)))
        .willReturn(Mono.empty());
    given(customerService.save(isA(Customer.class))).willReturn(Mono.just(customer));


    Mono<Customer> result = customerSyncService.sync(customer);

    StepVerifier.create(result)
        .expectNext(customer)
        .verifyComplete();

    then(customer).should().complete(isA(Instant.class));
    then(customer).should(never()).postpone(isA(Duration.class));
  }

  @Test
  void givenAuthenticationFailed_whenSync_thenComplete() {
    Customer customer = spy(Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    ));
    given(authenticationService.authenticate(isA(Customer.class)))
        .willReturn(Mono.error(new RuntimeException("Error")));
    given(customerService.save(isA(Customer.class))).willReturn(Mono.just(customer));


    Mono<Customer> result = customerSyncService.sync(customer);

    StepVerifier.create(result)
        .expectNext(customer)
        .verifyComplete();

    then(customer).should(never()).complete(isA(Instant.class));
    then(customer).should().postpone(isA(Duration.class));
  }

  @Test
  void givenSyncDataObjectProcessorFailed_whenSync_thenComplete() {
    Customer customer = spy(Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    ));
    given(authenticationService.authenticate(isA(Customer.class)))
        .willReturn(Mono.just(new Authentication("mock_token", "mock_base_url")));
    given(syncDataObjectProcessor.execute(isA(SyncDataObjectCommand.class)))
        .willReturn(Mono.error(new RuntimeException("Error")));
    given(customerService.save(isA(Customer.class))).willReturn(Mono.just(customer));


    Mono<Customer> result = customerSyncService.sync(customer);

    StepVerifier.create(result)
        .expectNext(customer)
        .verifyComplete();

    then(customer).should(never()).complete(isA(Instant.class));
    then(customer).should().postpone(isA(Duration.class));
  }
}