package com.example.adapter.secondary.salesforce.client.object.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response part of {@link ObjectQueryResponse} containing Salesforce object type and url.
 *
 * @author Saša Bolić
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {

  private String type;
}