package com.example.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CustomerTest {

  @Test
  void whenCreate_thenRequiredFieldsSet() {
    Customer result = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("username", Username.of("mock_username"))
        .hasFieldOrPropertyWithValue("clientId", ClientId.of("mock_client_id"));
  }

  @Test
  void givenUsernameEmpty_whenCreate_thenThrowException() {
    ClientId clientId = ClientId.of("mock_client_id");

    assertThatThrownBy(() -> Customer.of(null, clientId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Username cannot be null.");
  }

  @Test
  void givenClientIdEmpty_whenCreate_thenThrowException() {
    Username username = Username.of("mock_username");

    assertThatThrownBy(() -> Customer.of(username, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ClientId cannot be null.");
  }

  @Test
  void whenCreateFull_thenRequiredFieldsSet() {
    Customer result = new Customer(
        1L,
        Username.of("mock_username"),
        ClientId.of("mock_client_id"),
        Instant.parse("2020-02-01T11:00:00Z"),
        Instant.parse("2020-02-01T12:00:00Z")
    );

    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", 1L)
        .hasFieldOrPropertyWithValue("username", Username.of("mock_username"))
        .hasFieldOrPropertyWithValue("clientId", ClientId.of("mock_client_id"))
        .hasFieldOrPropertyWithValue("syncedOn", Instant.parse("2020-02-01T11:00:00Z"))
        .hasFieldOrPropertyWithValue("postponedUntil", Instant.parse("2020-02-01T12:00:00Z"));
  }

  @Test
  void givenNoLastSyncDate_whenComplete_thenThrowException() {
    Customer customer = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );

    assertThatThrownBy(() -> customer.complete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("LastSyncDate cannot be null.");
  }

  @Test
  void whenComplete_thenSyncedOnUpdated() {
    Customer customer = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );

    Customer result = customer.complete(Instant.parse("2020-02-01T05:00:00Z"));
    assertThat(result)
        .isNotNull()
        .hasFieldOrPropertyWithValue("syncedOn", Instant.parse("2020-02-01T05:00:00Z"));
  }

  @Test
  void givenNoOffset_whenPostpone_thenThrowException() {
    Customer customer = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );

    assertThatThrownBy(() -> customer.postpone(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Offset duration cannot be null.");
  }

  @Test
  void whenPostpone_thenPostponedUntilUpdated() {
    Customer customer = Customer.of(
        Username.of("mock_username"),
        ClientId.of("mock_client_id")
    );

    Customer result = customer.postpone(Duration.ofSeconds(5));
    assertThat(result)
        .isNotNull()
        .hasFieldOrProperty("postponedUntil");
  }
}