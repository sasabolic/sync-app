package com.example.domain.service.sync.step;

import com.example.domain.model.DataObject;
import com.example.domain.model.FetchObjectDetailsCommand;
import com.example.domain.model.SyncDataObjectCommand;
import com.example.port.DataObjectService;
import com.example.port.FetchObjectDetailsService;
import com.example.port.SyncStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Step used to sync {@code PENDING} data objects.
 *
 * @author Saša Bolić
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class SyncPendingObjectsStep implements SyncStep {

  private final FetchObjectDetailsService service;
  private final DataObjectService dataObjectService;

  @Override
  public Mono<Void> execute(SyncDataObjectCommand command) {
    return dataObjectService.findAllPendingForCustomer(command.getCustomerId())
        .flatMap(pendingObject -> service.handle(new FetchObjectDetailsCommand(
                pendingObject.getId(),
                pendingObject.getType(),
                command.getAuthentication()
            ))
            .map(dataObject -> dataObject.assignCustomer(command.getCustomerId()))
            .map(DataObject::complete)
            .flatMap(dataObjectService::save)
            .onErrorResume(throwable -> {
              log.error("Error occurred while fetching details for object={}.",
                  pendingObject.getId(), throwable);
              return Mono.empty();
            })
        )
        .then();
  }
}
