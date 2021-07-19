package com.example.port;

import com.example.domain.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Customer service.
 *
 * @author Saša Bolić
 */
public interface CustomerService {

  /**
   * Finds customers having the lowest synced date.
   *
   * @param limit the limit
   * @return the flux
   */
  Flux<Customer> findByLeastSyncedOn(int limit);

  /**
   * Saves customer.
   *
   * @param customer the customer
   * @return the mono
   */
  Mono<Customer> save(Customer customer);

  /**
   * Creates new customer.
   *
   * @param customer the customer
   * @return the mono
   */
  Mono<Customer> create(Customer customer);
}
