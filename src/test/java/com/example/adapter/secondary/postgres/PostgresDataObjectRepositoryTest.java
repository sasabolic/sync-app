package com.example.adapter.secondary.postgres;

import static com.example.domain.model.DataObjectState.PENDING;

import com.example.adapter.secondary.postgres.repository.PostgresDataObjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

class PostgresDataObjectRepositoryTest  extends TestDBConfig {

  @Autowired
  PostgresDataObjectRepository repository;

  @Test
  void whenFindAllPending_thenComplete() {
    executeSQL(
        "INSERT INTO customer(id, username, client_id) VALUES (1, 'test@test.com', 'test_client_id')");
    executeSQL(
        "INSERT INTO data_object(id, customer_id, created_on, modified_on, state, type) VALUES ('id', 1, '2021-02-24T13:20:45Z', '2021-02-24T13:20:45Z', 'PENDING', 'ACCOUNT')");

    repository.findAllForCustomerByState(1L, PENDING)
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
  }
}