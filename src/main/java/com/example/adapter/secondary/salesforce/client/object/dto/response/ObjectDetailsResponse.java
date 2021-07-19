package com.example.adapter.secondary.salesforce.client.object.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response containing all the information about Salesforce object. I.e. the raw data.
 *
 * @author Saša Bolić
 */
@Data
@AllArgsConstructor
@JsonDeserialize(using = ObjectDetailsResponseDeserializer.class)
public class ObjectDetailsResponse {

  private String id;

  private String type;

  private String rawData;
}
