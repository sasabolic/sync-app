package com.example.adapter.secondary.postgres.repository;

import com.example.adapter.secondary.postgres.entity.CustomerEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive customer repository.
 *
 * @author Saša Bolić
 */
public interface PostgresCustomerRepository extends ReactiveCrudRepository<CustomerEntity, Long> {

  @Query("""
      SELECT *
      FROM customer
      WHERE username = :username
      """)
  Mono<CustomerEntity> findByUsername(String username);

  @Query("""
      SELECT *
      FROM customer
      WHERE sync_postponed_until IS NULL
         OR NOW() > sync_postponed_until
      ORDER BY synced_on NULLS FIRST
      LIMIT :limit
      """)
  Flux<CustomerEntity> findByLeastSyncedOn(int limit);
}
