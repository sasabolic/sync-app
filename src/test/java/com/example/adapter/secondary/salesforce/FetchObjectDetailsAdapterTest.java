package com.example.adapter.secondary.salesforce;

import static com.example.domain.model.DataObjectType.ACCOUNT;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import com.example.adapter.secondary.salesforce.client.object.ObjectClient;
import com.example.adapter.secondary.salesforce.client.object.dto.request.ObjectDetailsRequest;
import com.example.adapter.secondary.salesforce.client.object.dto.response.ObjectDetailsResponse;
import com.example.domain.model.Authentication;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.FetchObjectDetailsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FetchObjectDetailsAdapterTest {

  @Mock
  ObjectClient objectClient;

  FetchObjectDetailsAdapter fetchObjectDetailsAdapter;

  @BeforeEach
  void setUp() {
    fetchObjectDetailsAdapter = new FetchObjectDetailsAdapter(
        objectClient
    );
  }

  @Test
  void whenHandle_thenExpectDataObjectAndComplete() {
    given(objectClient.getObjectDetails(isA(ObjectDetailsRequest.class)))
        .willReturn(Mono.just(new ObjectDetailsResponse(
            "mock9000007lEieAAE",
            "Account",
            "{mock_raw_data}"
        )));

    Mono<DataObject> result = fetchObjectDetailsAdapter.handle(new FetchObjectDetailsCommand(
        new DataObjectId("mock9000007lEieAAE"),
        ACCOUNT,
        new Authentication(
            "mock_token",
            "mock_base_url"
        )
    ));

    StepVerifier.create(result)
        .expectNextMatches(dataObject ->
            dataObject.getId().equals(new DataObjectId("mock9000007lEieAAE"))
        )
        .verifyComplete();
  }

  @Test
  void givenFetchingDetailsFailed_whenHandle_thenError() {
    given(objectClient.getObjectDetails(isA(ObjectDetailsRequest.class)))
        .willReturn(Mono.error(new RuntimeException("Error")));

    Mono<DataObject> result = fetchObjectDetailsAdapter.handle(new FetchObjectDetailsCommand(
        new DataObjectId("mock9000007lEieAAE"),
        ACCOUNT,
        new Authentication(
            "mock_token",
            "mock_base_url"
        )
    ));

    StepVerifier.create(result)
        .verifyError();
  }
}