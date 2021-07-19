package com.example.domain.service.sync.step;

import com.example.domain.model.FetchCandidatesCommand;
import com.example.domain.model.SyncDataObjectCommand;
import com.example.port.DataObjectService;
import com.example.port.FetchSyncCandidatesService;
import com.example.port.SyncStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Step used to fetch data objects eligible for sync i.e. the ones which were update at Salesforce
 * and save those objects.
 *
 * @author Saša Bolić
 */
@RequiredArgsConstructor
@Component
public class FetchObjectsEligibleForSyncStep implements SyncStep {

  private final FetchSyncCandidatesService service;
  private final DataObjectService dataObjectService;

  @Override
  public Mono<Void> execute(SyncDataObjectCommand command) {
    return service.handle(new FetchCandidatesCommand(command))
        .flatMap(dataObjectService::save)
        .then();
  }
}
