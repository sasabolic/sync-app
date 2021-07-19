package com.example.port;

import com.example.domain.model.Customer;
import reactor.core.publisher.Mono;

/**
 * Sync data objects for given customer.
 *
 * @author Saša Bolić
 */
public interface CustomerSyncService {

  /**
   * Sync data objects for customer.
   *
   * @param customer the customer
   * @return the mono
   */
  Mono<Customer> sync(Customer customer);
}
