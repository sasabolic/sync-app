package com.example.domain.service.sync;

import com.example.domain.model.Customer;
import com.example.domain.model.DataObjectType;
import com.example.domain.model.SyncDataObjectCommand;
import com.example.port.AuthenticationService;
import com.example.port.CustomerService;
import com.example.port.CustomerSyncService;
import com.example.port.SyncDataObjectProcessor;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Service
public class DefaultCustomerSyncService implements CustomerSyncService {

  private final SyncDataObjectProcessor syncDataObjectProcessor;
  private final AuthenticationService authenticationService;
  private final CustomerService customerService;
  private final SyncProperties properties;

  @Override
  public Mono<Customer> sync(Customer customer) {
    Instant lastSyncDate = Instant.now().minusMillis(properties.getSyncOnOffset().toMillis());

    return authenticationService.authenticate(customer)
        .flatMapMany(authentication -> Flux.fromArray(DataObjectType.values())
            .flatMap(objectType -> syncDataObjectProcessor.execute(new SyncDataObjectCommand(
                    customer.getId(),
                    authentication,
                    objectType,
                    customer.getSyncedOn()
                ))
            )
        )
        .then(Mono.defer(() -> Mono.just(customer.complete(lastSyncDate))))
        .onErrorResume(throwable -> {
          log.error("Error occurred when syncing", throwable);
          return Mono.just(customer.postpone(properties.getPostponeOffset()));
        })
        .flatMap(customerService::save);
  }
}
