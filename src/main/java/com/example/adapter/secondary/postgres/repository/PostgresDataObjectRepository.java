package com.example.adapter.secondary.postgres.repository;

import com.example.adapter.secondary.postgres.entity.DataObjectEntity;
import com.example.domain.model.DataObjectState;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive data object repository.
 *
 * @author Saša Bolić
 */
public interface PostgresDataObjectRepository extends ReactiveCrudRepository<DataObjectEntity, String> {

  @Query("""
      SELECT *
      FROM data_object
      WHERE customer_id = :customerId
        AND state = :state
      """)
  Flux<DataObjectEntity> findAllForCustomerByState(Long customerId, DataObjectState state);

  @Query("""
      SELECT *
      FROM data_object
      WHERE id = :id
        AND state = :state
      """)
  Mono<DataObjectEntity> findByIdAndState(String id, DataObjectState state);
}
