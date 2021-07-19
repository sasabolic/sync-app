package com.example.adapter.secondary.salesforce.client.object.dto.request;

import java.time.Instant;

/**
 * Request used to query modified Salesforce objects.
 *
 * @author Saša Bolić
 */
public class QueryObjectsRequest extends Request {

  private static final String QUERY_ALL_ENDPOINT = "/services/data/v51.0/queryAll?q=";

  private final Instant lastSyncDate;

  public QueryObjectsRequest(Instant lastSyncDate, String type, String baseUrl,
      String token) {
    super(type, baseUrl, token);
    this.lastSyncDate = lastSyncDate;
  }

  @Override
  public String url() {
    StringBuilder builder = new StringBuilder(baseUrl + QUERY_ALL_ENDPOINT);
    builder.append("SELECT+Id+");
    builder.append("FROM+").append(type);
    if (lastSyncDate != null) {
      builder.append("+WHERE+SystemModstamp+>=+").append(lastSyncDate);
    }
    builder.append("+ORDER+BY+SystemModstamp");

    return builder.toString();
  }

  @Override
  public String token() {
    return token;
  }
}
