package com.example.adapter.secondary.salesforce.client.object.dto.request;


/**
 * Request used to fetch details about Salesforce object.
 *
 * @author Saša Bolić
 */
public class ObjectDetailsRequest extends Request {

  private static final String GET_OBJECT_DETAILS_ENDPOINT = "/services/data/v51.0/sobjects/";

  private final String objectId;

  public ObjectDetailsRequest(String objectId, String type, String baseUrl, String token) {
    super(type, baseUrl, token);
    this.objectId = objectId;
  }

  @Override
  public String url() {
    return baseUrl + GET_OBJECT_DETAILS_ENDPOINT + type + "/" + objectId;
  }
}
