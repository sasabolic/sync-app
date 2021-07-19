package com.example.adapter.secondary.salesforce;

import com.example.adapter.secondary.salesforce.client.object.ObjectClient;
import com.example.adapter.secondary.salesforce.client.object.dto.request.ObjectDetailsRequest;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.DataObjectType;
import com.example.domain.model.FetchObjectDetailsCommand;
import com.example.domain.model.RawData;
import com.example.port.FetchObjectDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class FetchObjectDetailsAdapter implements FetchObjectDetailsService {

  private final ObjectClient client;

  @Override
  public Mono<DataObject> handle(FetchObjectDetailsCommand command) {
    return client.getObjectDetails(new ObjectDetailsRequest(
        command.getDataObjectId().getValue(),
        command.getDataObjectType().objectName(),
        command.getAuthentication().getBaseUrl(),
        command.getAuthentication().getToken()))
        .map(objectDetailsResponse -> DataObject.of(
            new DataObjectId(objectDetailsResponse.getId()),
            DataObjectType.fromName(objectDetailsResponse.getType()),
            RawData.of(objectDetailsResponse.getRawData())
            )
        );
  }
}
