package com.example.adapter.secondary.salesforce.client.object;

import com.example.adapter.secondary.salesforce.client.object.dto.request.ObjectDetailsRequest;
import com.example.adapter.secondary.salesforce.client.object.dto.request.QueryObjectsRequest;
import com.example.adapter.secondary.salesforce.client.object.dto.response.ObjectDetailsResponse;
import com.example.adapter.secondary.salesforce.client.object.dto.response.ObjectQueryResponse;
import com.example.common.JsonFixtureTest;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ObjectClientTest extends JsonFixtureTest {

  ObjectClient client;

  MockWebServer mockWebServer;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    WebClient webClient = WebClient.create(mockWebServer.url("/").toString());

    client = new ObjectClient(
        webClient,
        new ClientProperties(
            Duration.ofSeconds(1),
            Duration.ofMillis(1),
            true,
            16,
            true,
            new ClientProperties.Retry(3, Duration.ofMillis(0))
        )
    );
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void whenGetObjects_thenResponseContainsAllData() {
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json")
        .setBody(readJSON("/fixtures/contact_1_done_true.json"))
    );

    Flux<ObjectQueryResponse> result = client.getObjects(
        new QueryObjectsRequest(
            Instant.parse("2020-02-11T11:00:00.000Z"),
            "Contact",
            "/mock_base_url",
            "mock_token"
        )
    );

    StepVerifier.create(result)
        .expectNextMatches(objectQueryResponse -> objectQueryResponse.getNextRecordsUrl() == null
            && objectQueryResponse.getObjectResponses().size() == 2)
        .verifyComplete();
  }

  @Test
  void givenErrorResponseAndRetrySucceeded_whenGetObjects_thenReturnResult() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json")
        .setBody(readJSON("/fixtures/contact_1_done_true.json"))
    );

    Flux<ObjectQueryResponse> result = client.getObjects(
        new QueryObjectsRequest(
            Instant.parse("2020-02-11T11:00:00.000Z"),
            "Contact",
            "/mock_base_url",
            "mock_token"
        )
    );

    StepVerifier.create(result)
        .expectNextMatches(objectQueryResponse -> objectQueryResponse.getNextRecordsUrl() == null
            && objectQueryResponse.getObjectResponses().size() == 2)
        .verifyComplete();
  }

  @Test
  void givenPaginatedResponse_whenGetObjects_thenAllResultReturned() {
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json")
        .setBody(readJSON("/fixtures/contact_2_done_false.json"))
    );
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json")
        .setBody(readJSON("/fixtures/contact_2_done_true.json"))
    );

    Flux<ObjectQueryResponse> result = client.getObjects(
        new QueryObjectsRequest(
            Instant.parse("2020-02-11T11:00:00.000Z"),
            "Contact",
            "/mock_base_url",
            "mock_token"
        ));

    StepVerifier.create(result)
        .expectNextCount(2)
        .verifyComplete();
  }

  @Test
  void givenErrorOccurredOnNextRecordUrl_whenGetObjects_thenExpandOnlySucceededResult() {
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json")
        .setBody(readJSON("/fixtures/contact_2_done_false.json"))
    );
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));

    Flux<ObjectQueryResponse> result = client.getObjects(
        new QueryObjectsRequest(
            Instant.parse("2020-02-11T11:00:00.000Z"),
            "Contact",
            "/mock_base_url",
            "mock_token"
        ));

    StepVerifier.create(result)
        .expectNextCount(1)
        .verifyErrorMessage("Retries exhausted: 3/3");
  }

  @Test
  void whenGetObjectDetails_thenReturnMono() {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json")
            .setBody(readJSON("/fixtures/contact_details.json"))
    );

    Mono<ObjectDetailsResponse> result = client.getObjectDetails(
        new ObjectDetailsRequest("mock_id", "Account", "/mock_base_url", "mock_token")
    );

    StepVerifier.create(result)
        .expectNextMatches(objectDetailsResponse ->
            objectDetailsResponse.getId().equals("00309000006CQb2AAG") &&
                objectDetailsResponse.getRawData().equals(
                    "{\"attributes\":{\"type\":\"Contact\",\"url\":\"/services/data/v51.0/sobjects/Contact/00309000006CQb2AAG\"},\"Id\":\"00309000006CQb2AAG\",\"IsDeleted\":false,\"MasterRecordId\":null,\"AccountId\":\"00109000007lEj7AAE\",\"LastName\":\"Whitaker\",\"FirstName\":\"Kathleen\",\"Salutation\":null,\"Name\":\"Kathleen Whitaker\",\"OtherStreet\":null,\"OtherCity\":null,\"OtherState\":null,\"OtherPostalCode\":null,\"OtherCountry\":null,\"OtherLatitude\":null,\"OtherLongitude\":null,\"OtherGeocodeAccuracy\":null,\"OtherAddress\":null,\"MailingStreet\":\"10560 Dr Martin Luther King Jr St\",\"MailingCity\":\"Petersburg\",\"MailingState\":\"Florida\",\"MailingPostalCode\":\"33716\",\"MailingCountry\":\"United States\",\"MailingLatitude\":null,\"MailingLongitude\":null,\"MailingGeocodeAccuracy\":null,\"MailingAddress\":{\"city\":\"Petersburg\",\"country\":\"United States\",\"geocodeAccuracy\":null,\"latitude\":null,\"longitude\":null,\"postalCode\":\"33716\",\"state\":\"Florida\",\"street\":\"10560 Dr Martin Luther King Jr St\"},\"Phone\":\"(727) 577-9749\",\"Fax\":null,\"MobilePhone\":null,\"HomePhone\":null,\"OtherPhone\":null,\"AssistantPhone\":null,\"ReportsToId\":null,\"Email\":\"kwhitaker@jabil.com\",\"Title\":null,\"Department\":null,\"AssistantName\":null,\"LeadSource\":null,\"Birthdate\":null,\"Description\":null,\"OwnerId\":\"00509000003UaJkAAK\",\"CreatedDate\":\"2021-02-14T09:44:19.000+0000\",\"CreatedById\":\"00509000003UaJkAAK\",\"LastModifiedDate\":\"2021-02-14T09:44:19.000+0000\",\"LastModifiedById\":\"00509000003UaJkAAK\",\"SystemModstamp\":\"2021-02-14T09:44:19.000+0000\",\"LastActivityDate\":null,\"LastCURequestDate\":null,\"LastCUUpdateDate\":null,\"LastViewedDate\":null,\"LastReferencedDate\":null,\"EmailBouncedReason\":null,\"EmailBouncedDate\":null,\"IsEmailBounced\":false,\"PhotoUrl\":\"/services/images/photo/00309000006CQb2AAG\",\"Jigsaw\":null,\"JigsawContactId\":null,\"CleanStatus\":\"Pending\",\"IndividualId\":null,\"Level__c\":null,\"Languages__c\":null,\"NCS_TestData__PopulateAccount__c\":\"Jabil Circuit Inc.\",\"NCS_TestData__Populated__c\":true}"))
        .verifyComplete();
  }

  @Test
  void givenErrorOccurred_whenGetObjectDetails_thenReturnError() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(403));
    mockWebServer.enqueue(new MockResponse().setResponseCode(403));
    mockWebServer.enqueue(new MockResponse().setResponseCode(403));
    mockWebServer.enqueue(new MockResponse().setResponseCode(403));
    mockWebServer.enqueue(new MockResponse().setResponseCode(403));

    Mono<ObjectDetailsResponse> result = client.getObjectDetails(
        new ObjectDetailsRequest("mock_id", "Account", "/mock_base_url", "mock_token")
    );

    StepVerifier.create(result)
        .verifyError();
  }

}