package com.example.port;

import com.example.domain.model.Authentication;
import com.example.domain.model.Customer;
import reactor.core.publisher.Mono;

/**
 * Used to authenticate sync-app on customer's Salesforce instance.
 *
 * @author Saša Bolić
 */
public interface AuthenticationService {

  /**
   * Authenticate sync-app for given customer.
   *
   * @param customer the customer
   * @return the mono
   */
  Mono<Authentication> authenticate(Customer customer);
}
