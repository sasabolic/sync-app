package com.example.adapter.secondary.postgres;

import com.example.adapter.secondary.postgres.repository.PostgresCustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

class PostgresCustomerRepositoryTest extends TestDBConfig {

  @Autowired
  PostgresCustomerRepository repository;

  @Test
  void whenFindByUsername_thenComplete() {
    executeSQL(
        "INSERT INTO customer(id, username, client_id) VALUES (1, 'test1@test.com', 'test1_client_id')");
    executeSQL(
        "INSERT INTO customer(id, username, client_id, synced_on) VALUES (2, 'test2@test.com', 'test2_client_id', '2021-02-24T13:20:45Z')");

    repository.findByUsername("test1@test.com")
        .as(StepVerifier::create)
        .expectNextMatches(customer -> customer.getId().equals(1L))
        .verifyComplete();
  }

  @Test
  void whenFindByLeastSyncedOn_thenComplete() {
    executeSQL(
        "INSERT INTO customer(id, username, client_id) VALUES (1, 'test1@test.com', 'test1_client_id')");
    executeSQL(
        "INSERT INTO customer(id, username, client_id, synced_on) VALUES (2, 'test2@test.com', 'test2_client_id', '2021-02-24T13:20:45Z')");

    repository.findByLeastSyncedOn(1)
        .as(StepVerifier::create)
        .expectNextMatches(customer -> customer.getId().equals(1L))
        .verifyComplete();
  }
}