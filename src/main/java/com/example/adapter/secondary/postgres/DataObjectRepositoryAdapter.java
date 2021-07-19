package com.example.adapter.secondary.postgres;

import com.example.adapter.secondary.postgres.entity.DataObjectEntity;
import com.example.adapter.secondary.postgres.repository.PostgresDataObjectRepository;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.DataObjectState;
import com.example.port.DataObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class DataObjectRepositoryAdapter implements DataObjectRepository {

  private final PostgresDataObjectRepository repository;

  @Override
  public Mono<DataObject> save(DataObject entity) {
    return repository.findById(entity.getId().getValue())
        .map(existing -> existing.update(entity))
        .defaultIfEmpty(new DataObjectEntity(entity))
        .flatMap(repository::save).map(DataObjectEntity::toDataObject);
  }

  @Override
  public Flux<DataObject> findAllForCustomerByState(Long customerId, DataObjectState state) {
    return repository.findAllForCustomerByState(customerId, state).map(DataObjectEntity::toDataObject);
  }

  @Override
  public Mono<DataObject> findByIdAndState(DataObjectId id, DataObjectState state) {
    return repository.findByIdAndState(id.getValue(), state).map(DataObjectEntity::toDataObject);
  }
}
