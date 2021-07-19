package com.example.adapter.primary.rest;

import static com.example.domain.model.DataObjectType.LEAD;
import static org.mockito.BDDMockito.given;

import com.example.common.JsonFixtureTest;
import com.example.domain.model.DataObject;
import com.example.domain.model.DataObjectId;
import com.example.domain.model.RawData;
import com.example.port.DataObjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = DataObjectController.class)
class DataObjectControllerTest extends JsonFixtureTest {

  @MockBean
  DataObjectService dataObjectService;

  @Autowired
  WebTestClient webClient;

  @Test
  void whenGetSalesforceObject_thenStatusOkWithBody() {
    String object_id = "00Q09000003YCc4EAG";
    DataObject data = DataObject.of(new DataObjectId(object_id), LEAD, RawData.of(readJSON("/fixtures/raw_data.json")));

    given(dataObjectService.findCompletedById(new DataObjectId(object_id))).willReturn(Mono.just(data));

    webClient.get()
        .uri("/salesforce/{id}", object_id)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.attributes.type").isEqualTo("Lead")
        .jsonPath("$.attributes.url").isEqualTo("/services/data/v51.0/sobjects/Lead/00Q09000003YCc4EAG")
        .jsonPath("$.Id").isEqualTo(object_id);
  }

  @Test
  void givenNoObject_whenGetSalesforceObject_thenStatusOkWithEmptyBody() {
    String object_id = "00Q09000003YCc4EAG";

    given(dataObjectService.findCompletedById(new DataObjectId(object_id))).willReturn(Mono.empty());

    webClient.get()
        .uri("/salesforce/{id}", object_id)
        .exchange()
        .expectStatus().isOk()
        .expectBody().isEmpty();
  }
}