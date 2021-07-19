package com.example.adapter.secondary.salesforce.client.object.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response part of {@link ObjectQueryResponse} containing Salesforce object id.
 *
 * @author Saša Bolić
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectResponse {

  private Attribute attributes;

  @JsonProperty("Id")
  private String id;

  public String getType() {
    return attributes.getType();
  }
}