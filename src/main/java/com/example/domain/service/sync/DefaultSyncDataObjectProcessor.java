package com.example.domain.service.sync;

import com.example.domain.model.SyncDataObjectCommand;
import com.example.domain.service.sync.step.FetchObjectsEligibleForSyncStep;
import com.example.domain.service.sync.step.SyncPendingObjectsStep;
import com.example.port.SyncDataObjectProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Component
public class DefaultSyncDataObjectProcessor implements SyncDataObjectProcessor {

  private final FetchObjectsEligibleForSyncStep fetchObjectsEligibleForSyncStep;
  private final SyncPendingObjectsStep syncPendingObjectsStep;

  @Override
  public Mono<Void> execute(SyncDataObjectCommand syncDataObjectCommand) {
    log.debug("Started {}", syncDataObjectCommand);

    return fetchObjectsEligibleForSyncStep.execute(syncDataObjectCommand)
        .then(Mono.defer(() -> syncPendingObjectsStep.execute(syncDataObjectCommand)));
  }
}
