package com.example.domain.service.sync;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import com.example.port.CustomerService;
import com.example.port.CustomerSyncService;
import com.example.port.SyncJob;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class DefaultSynJobTest {

  @Mock
  CustomerSyncService customerSyncService;

  @Mock
  CustomerService customerService;

  SyncJob syncJob;

  @BeforeEach
  void setUp() {
    syncJob = new DefaultSyncJob(
        customerSyncService,
        customerService,
        new SyncProperties(
            Duration.ZERO,
            Duration.ZERO,
            1
        )
    );
  }

  @Test
  void whenRun_thenExecuteSyncForDesignatedCustomers() {
    Customer customer1 = Customer.of(Username.of("mock_username1"), ClientId.of("mock_client_id1"));
    Customer customer2 = Customer.of(Username.of("mock_username2"), ClientId.of("mock_client_id1"));
    given(customerService.findByLeastSyncedOn(anyInt()))
        .willReturn(Flux.just(
            customer1,
            customer2
        ));
    given(customerSyncService.sync(customer1)).willReturn(Mono.just(customer1));
    given(customerSyncService.sync(customer2)).willReturn(Mono.just(customer2));

    syncJob.run();

    then(customerSyncService).should().sync(eq(customer1));
    then(customerSyncService).should().sync(eq(customer2));
  }

  @Test
  void givenNoCandidates_whenRun_thenDoNothing() {
    Customer customer1 = Customer.of(Username.of("mock_username1"), ClientId.of("mock_client_id1"));
    given(customerService.findByLeastSyncedOn(anyInt()))
        .willReturn(Flux.empty());

    syncJob.run();

    then(customerSyncService).should(never()).sync(eq(customer1));
  }

  @Test
  void givenErrorOccurredWhenFetchingCandidates_whenRun_thenThrowException() {
    given(customerService.findByLeastSyncedOn(anyInt()))
        .willReturn(Flux.error(new RuntimeException("Error")));

    assertThatThrownBy(() -> syncJob.run())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error");

    then(customerSyncService).should(never()).sync(isA(Customer.class));
  }
}