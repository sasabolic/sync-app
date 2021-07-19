package com.example.adapter.secondary.postgres.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.domain.model.ClientId;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CustomerEntityTest {

  @Test
  void whenCreateDefault_thenNewInstance() {
    CustomerEntity result = new CustomerEntity();

    assertThat(result).isNotNull();
  }

  @Test
  void whenCreate_thenNewInstance() {
    Customer customer = new Customer(
        1L,
        Username.of("mock_username"),
        ClientId.of("mock_client_id"),
        Instant.parse("2020-02-01T11:00:00Z"),
        Instant.parse("2020-02-01T12:00:00Z")
    );

    CustomerEntity result = new CustomerEntity(customer);

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", 1L)
        .hasFieldOrPropertyWithValue("username", "mock_username")
        .hasFieldOrPropertyWithValue("clientId", "mock_client_id")
        .hasFieldOrPropertyWithValue("syncedOn", Instant.parse("2020-02-01T11:00:00Z"))
        .hasFieldOrPropertyWithValue("postponedUntil", Instant.parse("2020-02-01T12:00:00Z"));
  }

  @Test
  void whenToCustomer_thenNewInstance() {
    Customer customer = new Customer(
        1L,
        Username.of("mock_username"),
        ClientId.of("mock_client_id"),
        Instant.parse("2020-02-01T11:00:00Z"),
        Instant.parse("2020-02-01T12:00:00Z")
    );

    Customer result = new CustomerEntity(customer).toCustomer();

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", 1L)
        .hasFieldOrPropertyWithValue("username", Username.of("mock_username"))
        .hasFieldOrPropertyWithValue("clientId", ClientId.of("mock_client_id"))
        .hasFieldOrPropertyWithValue("syncedOn", Instant.parse("2020-02-01T11:00:00Z"))
        .hasFieldOrPropertyWithValue("postponedUntil", Instant.parse("2020-02-01T12:00:00Z"));
  }

  @Test
  void givenUsernameAndClientIdAreNull_whenToCustomer_thenNewInstance() {
    Customer customer = new Customer(
        1L,
        null,
        null,
        Instant.parse("2020-02-01T11:00:00Z"),
        Instant.parse("2020-02-01T12:00:00Z")
    );

    Customer result = new CustomerEntity(customer).toCustomer();

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", 1L)
        .hasFieldOrPropertyWithValue("username", null)
        .hasFieldOrPropertyWithValue("clientId", null)
        .hasFieldOrPropertyWithValue("syncedOn", Instant.parse("2020-02-01T11:00:00Z"))
        .hasFieldOrPropertyWithValue("postponedUntil", Instant.parse("2020-02-01T12:00:00Z"));
  }
}