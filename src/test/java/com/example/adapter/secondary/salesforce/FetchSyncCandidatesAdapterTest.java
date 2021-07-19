package com.example.adapter.secondary.salesforce;

import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import com.example.adapter.secondary.salesforce.client.object.ObjectClient;
import com.example.adapter.secondary.salesforce.client.object.dto.request.QueryObjectsRequest;
import com.example.adapter.secondary.salesforce.client.object.dto.response.Attribute;
import com.example.adapter.secondary.salesforce.client.object.dto.response.ObjectQueryResponse;
import com.example.adapter.secondary.salesforce.client.object.dto.response.ObjectResponse;
import com.example.domain.model.Authentication;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.FetchCandidatesCommand;
import com.example.domain.model.SyncDataObjectCommand;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FetchSyncCandidatesAdapterTest {

  @Mock
  ObjectClient objectClient;
  
  FetchSyncCandidatesAdapter fetchSyncCandidatesAdapter;

  @BeforeEach
  void setUp() {
    fetchSyncCandidatesAdapter = new FetchSyncCandidatesAdapter(
        objectClient
    );
  }

  @Test
  void whenHandle_thenExpectDataObjectAndComplete() {
    given(objectClient.getObjects(isA(QueryObjectsRequest.class)))
        .willReturn(Flux.just(new ObjectQueryResponse(
            "mock_next_records_url",
            List.of(new ObjectResponse(
                new Attribute("Account"),
                "mock9000007lEieAAE"
            ))
        )));

    Flux<DataObject> result = fetchSyncCandidatesAdapter.handle(new FetchCandidatesCommand(
        new SyncDataObjectCommand(
            1L,
            new Authentication(
                "mock_token",
                "mock_base_url"
            ),
            ACCOUNT,
            Instant.parse("2020-02-11T11:00:00.000Z")
        )
    ));

    StepVerifier.create(result)
        .expectNextMatches(dataObject -> dataObject.getId().equals(new DataObjectId("mock9000007lEieAAE")))
        .verifyComplete();
  }

  @Test
  void givenGetObjectsFailed_whenHandle_thenError() {
    given(objectClient.getObjects(isA(QueryObjectsRequest.class)))
        .willReturn(Flux.error(new RuntimeException("Error")));

    Flux<DataObject> result = fetchSyncCandidatesAdapter.handle(new FetchCandidatesCommand(
        new SyncDataObjectCommand(
            1L,
            new Authentication(
                "mock_token",
                "mock_base_url"
            ),
            ACCOUNT,
            Instant.parse("2020-02-11T11:00:00.000Z")
        )
    ));

    StepVerifier.create(result)
        .verifyError();
  }
}