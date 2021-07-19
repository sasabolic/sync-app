package com.example.port;

import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Customer repository.
 *
 * @author Saša Bolić
 */
public interface CustomerRepository {

  /**
   * Find customer by username.
   *
   * @param username the username
   * @return the mono
   */
  Mono<Customer> findByUsername(Username username);

  /**
   * Finds customers with earliest synced date.
   *
   * @param limit the limit
   * @return the flux
   */
  Flux<Customer> findByLeastSyncedOn(int limit);

  /**
   * Save customer.
   *
   * @param customer the customer
   * @return the mono
   */
  Mono<Customer> save(Customer customer);
}
