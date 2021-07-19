package com.example.port;

import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.DataObjectState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Data object repository.
 *
 * @author Saša Bolić
 */
public interface DataObjectRepository {

  /**
   * Save data object.
   *
   * @param entity the entity
   * @return the mono
   */
  Mono<DataObject> save(DataObject entity);

  /**
   * Finds all data object for specified customer by given state.
   *
   * @param customerId the customer id
   * @param state      the state
   * @return the flux
   */
  Flux<DataObject> findAllForCustomerByState(Long customerId, DataObjectState state);

  /**
   * Find data object by id and in given state.
   *
   * @param id    the id
   * @param state the state
   * @return the mono
   */
  Mono<DataObject> findByIdAndState(DataObjectId id, DataObjectState state);
}
