package com.example.adapter.secondary.salesforce.client.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Salesforce authentication response.
 *
 * @author Saša Bolić
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  private String scope;

  @JsonProperty("instance_url")
  private String instanceUrl;

  public String getTokenValue() {
    return getTokenType() + " " + getAccessToken();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AuthenticationResponse.class.getSimpleName() + "[", "]")
        .add("instanceUrl='" + instanceUrl + "'")
        .toString();
  }
}