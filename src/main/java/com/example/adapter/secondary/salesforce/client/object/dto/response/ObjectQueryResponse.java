package com.example.adapter.secondary.salesforce.client.object.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The response for a {@link com.example.adapter.secondary.salesforce.client.object.dto.request.QueryObjectsRequest}.
 *
 * @author Saša Bolić
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectQueryResponse {

  private String nextRecordsUrl;

  @JsonProperty("records")
  private List<ObjectResponse> objectResponses;
}