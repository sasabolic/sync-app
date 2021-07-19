package com.example.port;

import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Data object service.
 *
 * @author Saša Bolić
 */
public interface DataObjectService {

  /**
   * Finds completed data object by id.
   *
   * @param id the id
   * @return the mono
   */
  Mono<DataObject> findCompletedById(DataObjectId id);

  /**
   * Find all pending data object for given customer.
   *
   * @param customerId the customer id
   * @return the flux
   */
  Flux<DataObject> findAllPendingForCustomer(Long customerId);

  /**
   * Save data object.
   *
   * @param dataObject the data object
   * @return the mono
   */
  Mono<DataObject> save(DataObject dataObject);
}
