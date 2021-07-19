package com.example.domain.service;

import static com.example.domain.model.DataObjectState.COMPLETED;
import static com.example.domain.model.DataObjectState.PENDING;

import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.port.DataObjectRepository;
import com.example.port.DataObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultDataObjectService implements DataObjectService {

  private final DataObjectRepository repository;

  @Override
  public Mono<DataObject> findCompletedById(DataObjectId id) {
    return repository.findByIdAndState(id, COMPLETED);
  }

  @Override
  public Flux<DataObject> findAllPendingForCustomer(Long customerId) {
    return repository.findAllForCustomerByState(customerId, PENDING);
  }

  @Override
  public Mono<DataObject> save(DataObject dataObject) {
    return repository.save(dataObject);
  }
}
