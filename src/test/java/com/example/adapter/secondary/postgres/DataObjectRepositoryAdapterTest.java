package com.example.adapter.secondary.postgres;

import static com.example.domain.model.DataObjectState.PENDING;
import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import com.example.adapter.secondary.postgres.entity.DataObjectEntity;
import com.example.adapter.secondary.postgres.repository.PostgresDataObjectRepository;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.DataObjectState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class DataObjectRepositoryAdapterTest {

  @Mock
  PostgresDataObjectRepository postgresDataObjectRepository;

  DataObjectRepositoryAdapter dataObjectRepositoryAdapter;

  @BeforeEach
  void setUp() {
    dataObjectRepositoryAdapter = new DataObjectRepositoryAdapter(
        postgresDataObjectRepository
    );
  }

  @Test
  void whenSave_thenComplete() {
    DataObject dataObject = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);
    given(postgresDataObjectRepository.findById(anyString())).willReturn(Mono.empty());
    given(postgresDataObjectRepository.save(isA(DataObjectEntity.class)))
        .willReturn(Mono.just(new DataObjectEntity(dataObject)));

    Mono<DataObject> result = dataObjectRepositoryAdapter.save(dataObject);

    StepVerifier.create(result)
        .expectNextMatches(dobj -> dobj.getId().equals(new DataObjectId("mock9000007lEieAAE")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFindReturnsError_whenSave_thenError() {
    DataObject dataObject = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);
    given(postgresDataObjectRepository.findById(anyString()))
        .willReturn(Mono.error(new RuntimeException("Error")));

    Mono<DataObject> result = dataObjectRepositoryAdapter.save(dataObject);

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void givenRepositorySaveReturnsError_whenSave_thenError() {
    DataObject dataObject = DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT);
    given(postgresDataObjectRepository.findById(anyString())).willReturn(Mono.empty());
    given(postgresDataObjectRepository.save(isA(DataObjectEntity.class)))
        .willReturn(Mono.error(new RuntimeException("Error")));

    Mono<DataObject> result = dataObjectRepositoryAdapter.save(dataObject);

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void whenFindAllForCustomerByState_thenComplete() {
    given(postgresDataObjectRepository
        .findAllForCustomerByState(anyLong(), isA(DataObjectState.class)))
        .willReturn(Flux.just(new DataObjectEntity(
            DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT)
        )));

    Flux<DataObject> result = dataObjectRepositoryAdapter.findAllForCustomerByState(1L, PENDING);

    StepVerifier.create(result)
        .expectNextMatches(dataObject -> dataObject.getId().equals(new DataObjectId("mock9000007lEieAAE")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenFindAllForCustomerByState_thenError() {
    given(postgresDataObjectRepository.findAllForCustomerByState(anyLong(), isA(DataObjectState.class)))
        .willReturn(Flux.error(new RuntimeException("Error")));

    Flux<DataObject> result = dataObjectRepositoryAdapter.findAllForCustomerByState(1L, PENDING);

    StepVerifier.create(result)
        .verifyError();
  }

  @Test
  void whenFindByIdAndState_thenComplete() {
    given(postgresDataObjectRepository
        .findByIdAndState(anyString(), isA(DataObjectState.class)))
        .willReturn(Mono.just(new DataObjectEntity(
            DataObject.of(new DataObjectId("mock9000007lEieAAE"), ACCOUNT)
        )));

    Mono<DataObject> result = dataObjectRepositoryAdapter.findByIdAndState(new DataObjectId("mock9000007lEieAAE"), PENDING);

    StepVerifier.create(result)
        .expectNextMatches(dataObject -> dataObject.getId().equals(new DataObjectId("mock9000007lEieAAE")))
        .verifyComplete();
  }

  @Test
  void givenRepositoryFailed_whenFindByIdAndState_thenError() {
    given(postgresDataObjectRepository.findByIdAndState(anyString(), isA(DataObjectState.class)))
        .willReturn(Mono.error(new RuntimeException("Error")));

    Mono<DataObject> result = dataObjectRepositoryAdapter.findByIdAndState(new DataObjectId("mock9000007lEieAAE"), PENDING);

    StepVerifier.create(result)
        .verifyError();
  }
}