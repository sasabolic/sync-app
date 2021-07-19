package com.example.domain.service;

import com.example.domain.model.Customer;
import com.example.domain.service.error.CustomerNotUniqueException;
import com.example.port.CustomerRepository;
import com.example.port.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultCustomerService implements CustomerService {

  private final CustomerRepository repository;

  @Override
  public Flux<Customer> findByLeastSyncedOn(int limit) {
    return repository.findByLeastSyncedOn(limit);
  }

  @Override
  public Mono<Customer> save(Customer customer) {
    return repository.save(customer);
  }

  @Override
  public Mono<Customer> create(Customer customer) {
    return repository.findByUsername(customer.getUsername())
        .flatMap(ignore -> Mono.<Customer>error(new CustomerNotUniqueException("Customer already exits.")))
        .switchIfEmpty(Mono.defer(() -> repository.save(customer)));
  }
}
