package com.example.domain.service.sync.step;

import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;

import com.example.domain.model.Authentication;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.FetchObjectDetailsCommand;
import com.example.domain.model.SyncDataObjectCommand;
import com.example.port.DataObjectService;
import com.example.port.FetchObjectDetailsService;
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
class SyncPendingObjectsStepTest {

  @Mock
  FetchObjectDetailsService fetchObjectDetailsService;

  @Mock
  DataObjectService dataObjectService;

  SyncPendingObjectsStep syncPendingObjectsStep;

  @BeforeEach
  void setUp() {
    syncPendingObjectsStep = new SyncPendingObjectsStep(
        fetchObjectDetailsService,
        dataObjectService
    );
  }

  @Test
  void whenExecute_thenReturnResult() {
    DataObject dataObject = spy(DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT));
    given(dataObjectService.findAllPendingForCustomer(anyLong())).willReturn(Flux.just(
        dataObject));
    given(fetchObjectDetailsService.handle(isA(FetchObjectDetailsCommand.class))).willReturn(
        Mono.just(dataObject));

    Mono<Void> result = syncPendingObjectsStep.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyComplete();

    then(dataObject).should().complete();
    then(dataObject).should().assignCustomer(anyLong());
    then(dataObjectService).should().save(isA(DataObject.class));
  }

  @Test
  void givenFindPendingFailed_whenExecute_thenReturnError() {
    DataObject dataObject = spy(DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT));
    given(dataObjectService.findAllPendingForCustomer(anyLong())).willReturn(Flux.error(
        new RuntimeException("Error")));

    Mono<Void> result = syncPendingObjectsStep.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyError();

    then(fetchObjectDetailsService).should(never()).handle(isA(FetchObjectDetailsCommand.class));
    then(dataObject).should(never()).complete();
    then(dataObject).should(never()).assignCustomer(anyLong());
    then(dataObjectService).should(never()).save(isA(DataObject.class));
  }

  @Test
  void givenFetchDetailsFailed_whenExecute_thenReturnResult() {
    DataObject dataObject = spy(DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT));
    given(dataObjectService.findAllPendingForCustomer(anyLong())).willReturn(Flux.just(
        dataObject));
    given(fetchObjectDetailsService.handle(isA(FetchObjectDetailsCommand.class))).willReturn(
        Mono.error(new RuntimeException("Error")));

    Mono<Void> result = syncPendingObjectsStep.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyComplete();

    then(dataObject).should(never()).complete();
    then(dataObject).should(never()).assignCustomer(anyLong());
    then(dataObjectService).should(never()).save(isA(DataObject.class));
  }

  @Test
  void givenSaveFailed_whenExecute_thenReturnResult() {
    DataObject dataObject = spy(DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT));
    given(dataObjectService.findAllPendingForCustomer(anyLong())).willReturn(Flux.just(
        dataObject));
    given(fetchObjectDetailsService.handle(isA(FetchObjectDetailsCommand.class))).willReturn(
        Mono.just(dataObject));
    given(dataObjectService.save(isA(DataObject.class))).willReturn(Mono.error(
        new RuntimeException("Error")));

    Mono<Void> result = syncPendingObjectsStep.execute(new SyncDataObjectCommand(
        1L,
        new Authentication("mock_token", "mock_base_url"),
        ACCOUNT,
        Instant.parse("2020-02-01T05:00:00Z")
    ));

    StepVerifier.create(result)
        .verifyComplete();
  }
}