package com.example.adapter.secondary.salesforce;

import com.example.adapter.secondary.salesforce.client.object.ObjectClient;
import com.example.adapter.secondary.salesforce.client.object.dto.request.QueryObjectsRequest;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.DataObjectType;
import com.example.domain.model.FetchCandidatesCommand;
import com.example.port.FetchSyncCandidatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class FetchSyncCandidatesAdapter implements FetchSyncCandidatesService {

  private final ObjectClient client;

  @Override
  public Flux<DataObject> handle(FetchCandidatesCommand command) {
    return client.getObjects(new QueryObjectsRequest(
        command.getObjectLatestSyncDate(),
        command.getDataObjectType().objectName(),
        command.getAuthentication().getBaseUrl(),
        command.getAuthentication().getToken()))
        .flatMap(response -> Flux.fromIterable(response.getObjectResponses()))
        .map(objectResponse -> DataObject.of(new DataObjectId(objectResponse.getId()), DataObjectType.fromName(objectResponse.getType())))
        .map(dataObject -> dataObject.assignCustomer(command.getCustomerId()));
  }
}
