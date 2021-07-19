package com.example.adapter.secondary.postgres;

import com.example.adapter.secondary.postgres.entity.CustomerEntity;
import com.example.adapter.secondary.postgres.repository.PostgresCustomerRepository;
import com.example.domain.model.Customer;
import com.example.domain.model.Username;
import com.example.port.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class CustomerRepositoryAdapter implements CustomerRepository {

  private final PostgresCustomerRepository repository;

  @Override
  public Mono<Customer> findByUsername(Username username) {
    return repository.findByUsername(username.getValue()).map(CustomerEntity::toCustomer);
  }

  @Override
  public Flux<Customer> findByLeastSyncedOn(int limit) {
    return repository.findByLeastSyncedOn(limit).map(CustomerEntity::toCustomer);
  }

  @Override
  public Mono<Customer> save(Customer customer) {
    return repository.save(new CustomerEntity(customer)).map(CustomerEntity::toCustomer);
  }
}
