package com.example.adapter.secondary.salesforce.client.object.dto.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * JSON deserializer for {@link ObjectDetailsResponse}.
 *
 * @author Saša Bolić
 */
public class ObjectDetailsResponseDeserializer extends JsonDeserializer<ObjectDetailsResponse> {

  @Override
  public ObjectDetailsResponse deserialize(JsonParser jp, DeserializationContext context)
      throws IOException {
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);

    final String id = node.get("Id").asText();
    final String type = node.get("attributes").get("type").asText();
    final String rawData = node.toString();

    return new ObjectDetailsResponse(id, type, rawData);
  }
}
