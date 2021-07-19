package com.example.domain.service;

import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.DataObjectState;
import com.example.port.DataObjectRepository;
import com.example.port.DataObjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class DefaultDataObjectServiceTest {

  @Mock
  DataObjectRepository repository;

  DataObjectService service;

  @BeforeEach
  void setUp() {
    service = new DefaultDataObjectService(
        repository
    );
  }

  @Test
  void whenFindByCompletedId_thenExpectNextAndComplete() {
    given(repository.findByIdAndState(isA(DataObjectId.class), isA(DataObjectState.class)))
        .willReturn(Mono.just(DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT)));

    Mono<DataObject> result = service.findCompletedById(new DataObjectId("mock9000007lEieAAE"));

    StepVerifier.create(result)
        .expectNextMatches(dataObject -> dataObject.getId().equals(new DataObjectId("mock9000007lEieAAE")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryReturnsEmpty_whenFindByCompletedId_thenComplete() {
    given(repository.findByIdAndState(isA(DataObjectId.class), isA(DataObjectState.class)))
        .willReturn(Mono.empty());

    Mono<DataObject> result = service.findCompletedById(new DataObjectId("mock9000007lEieAAE"));

    StepVerifier.create(result)
        .verifyComplete();
  }

  @Test
  void givenRepositoryReturnsError_whenFindByCompletedId_thenError() {
    given(repository.findByIdAndState(isA(DataObjectId.class), isA(DataObjectState.class)))
        .willReturn(Mono.error(new RuntimeException("Error")));

    Mono<DataObject> result = service.findCompletedById(new DataObjectId("mock9000007lEieAAE"));

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void whenFindAllPendingForCustomer_thenExpectNextAndComplete() {
    given(repository.findAllForCustomerByState(anyLong(), isA(DataObjectState.class)))
        .willReturn(Flux.just(DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT)));

    Flux<DataObject> result = service.findAllPendingForCustomer(1L);

    StepVerifier.create(result)
        .expectNextMatches(dataObject -> dataObject.getId().equals(new DataObjectId("mock9000007lEieAAE")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenFindAllPendingForCustomer_thenError() {
    given(repository.findAllForCustomerByState(anyLong(), isA(DataObjectState.class)))
        .willReturn(Flux.error(new RuntimeException("Error")));

    Flux<DataObject> result = service.findAllPendingForCustomer(1L);

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void whenSave_thenExpectNextAndComplete() {
    DataObject object = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);
    given(repository.save(isA(DataObject.class))).willReturn(Mono.just(object));

    Mono<DataObject> result = service.save(object);

    StepVerifier.create(result)
        .expectNextMatches(dataObject -> dataObject.getId().equals(new DataObjectId("mock9000007lEieAAE")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenSave_thenError() {
    given(repository.save(isA(DataObject.class))).willReturn(Mono.error(new RuntimeException("Error")));

    Mono<DataObject> result = service.save(DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT));

    StepVerifier.create(result)
        .verifyError();
  }
}