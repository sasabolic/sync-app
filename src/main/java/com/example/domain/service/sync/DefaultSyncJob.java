package com.example.domain.service.sync;

import com.example.port.CustomerService;
import com.example.port.CustomerSyncService;
import com.example.port.SyncJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class DefaultSyncJob implements SyncJob {

  private final CustomerSyncService customerSyncService;
  private final CustomerService customerService;
  private final SyncProperties properties;

  @Override
  public void run() {
    customerService.findByLeastSyncedOn(properties.getCustomerSyncLimit())
        .doOnNext(customer -> {
          log.debug("Syncing customer: {}", customer);

          customerSyncService.sync(customer)
              .doOnError(throwable -> log.error("Sync error occurred", throwable))
              .subscribe();
        }).blockLast();
  }
}
