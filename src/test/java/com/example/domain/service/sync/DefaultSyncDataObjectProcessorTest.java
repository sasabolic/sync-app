package com.example.domain.service.sync;

import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.example.domain.model.Authentication;
import com.example.domain.model.SyncDataObjectCommand;
import com.example.domain.service.sync.step.FetchObjectsEligibleForSyncStep;
import com.example.domain.service.sync.step.SyncPendingObjectsStep;
import com.example.port.SyncDataObjectProcessor;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class DefaultSyncDataObjectProcessorTest {

  @Mock
  FetchObjectsEligibleForSyncStep fetchObjectsEligibleForSyncStep;

  @Mock
  SyncPendingObjectsStep syncPendingObjectsStep;

  SyncDataObjectProcessor syncDataObjectProcessor;

  @BeforeEach
  void setUp() {
    syncDataObjectProcessor = new DefaultSyncDataObjectProcessor(
        fetchObjectsEligibleForSyncStep,
        syncPendingObjectsStep
    );
  }

  @Test
  void whenExecute_thenReturnResult() {
    given(fetchObjectsEligibleForSyncStep.execute(isA(SyncDataObjectCommand.class))).willReturn(Mono.empty());
    given(syncPendingObjectsStep.execute(isA(SyncDataObjectCommand.class))).willReturn(Mono.empty());

    Mono<Void> result = syncDataObjectProcessor.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyComplete();
  }

  @Test
  void givenFirstStepFailed_whenExecute_thenErrorAndDoNotCallNextStep() {
    given(fetchObjectsEligibleForSyncStep.execute(isA(SyncDataObjectCommand.class))).willReturn(Mono.error(new RuntimeException("Error")));

    Mono<Void> result = syncDataObjectProcessor.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyError();

    then(syncPendingObjectsStep).should(never()).execute(isA(SyncDataObjectCommand.class));
  }

  @Test
  void givenSecondStepFailed_whenExecute_thenReturnError() {
    given(fetchObjectsEligibleForSyncStep.execute(isA(SyncDataObjectCommand.class))).willReturn(Mono.empty());
    given(syncPendingObjectsStep.execute(isA(SyncDataObjectCommand.class))).willReturn(Mono.error(new RuntimeException("Error")));

    Mono<Void> result = syncDataObjectProcessor.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyError();
  }
}