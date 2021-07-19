package com.example.domain.service.sync.step;

import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.example.domain.model.Authentication;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.FetchCandidatesCommand;
import com.example.domain.model.SyncDataObjectCommand;
import com.example.port.DataObjectService;
import com.example.port.FetchSyncCandidatesService;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FetchObjectsEligibleForSyncStepTest {

  @Mock
  FetchSyncCandidatesService fetchSyncCandidatesService;

  @Mock
  DataObjectService dataObjectService;

  FetchObjectsEligibleForSyncStep fetchObjectsEligibleForSyncStep;

  @BeforeEach
  void setUp() {
    fetchObjectsEligibleForSyncStep = new FetchObjectsEligibleForSyncStep(
        fetchSyncCandidatesService,
        dataObjectService
    );
  }

  @Test
  void whenExecute_thenReturnResult() {
    DataObject dataObject = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);
    given(fetchSyncCandidatesService.handle(isA(FetchCandidatesCommand.class))).willReturn(
        Flux.just(dataObject));
    given(dataObjectService.save(isA(DataObject.class))).willReturn(Mono.just(
        dataObject));

    Mono<Void> result = fetchObjectsEligibleForSyncStep.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyComplete();
  }

  @Test
  void givenFetchSyncCandidateFailed_whenExecute_thenReturnError() {
    given(fetchSyncCandidatesService.handle(isA(FetchCandidatesCommand.class))).willReturn(
        Flux.error(new RuntimeException("Error")));

    Mono<Void> result = fetchObjectsEligibleForSyncStep.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyError();

    then(dataObjectService).should(never()).save(isA(DataObject.class));
  }

  @Test
  void givenSaveFailed_whenExecute_thenReturnError() {
    DataObject dataObject = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);
    given(fetchSyncCandidatesService.handle(isA(FetchCandidatesCommand.class))).willReturn(
        Flux.just(dataObject));
    given(dataObjectService.save(isA(DataObject.class))).willReturn(Mono.error(new RuntimeException("Error")));

    Mono<Void> result = fetchObjectsEligibleForSyncStep.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyError();
  }
}