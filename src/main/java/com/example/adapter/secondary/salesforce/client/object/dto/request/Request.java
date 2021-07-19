package com.example.adapter.secondary.salesforce.client.object.dto.request;

/**
 * Common Salesforce request properties.
 *
 * @author Saša Bolić
 */
public abstract class Request {

  /**
   * The Salesforce object type e.g. Account, Contact etc.
   */
  final String type;

  final String baseUrl;

  final String token;

  protected Request(String type, String baseUrl, String token) {
    this.type = type;
    this.baseUrl = baseUrl;
    this.token = token;
  }

  /**
   * Returns the url to the Salesforce resource.
   *
   * @return the string
   */
  public abstract String url();

  /**
   * Returns the base url of a Salesforce customer.
   *
   * @return the string
   */
  public String baseUrl() {
    return baseUrl;
  }

  /**
   * Returns the token used to authenticate app for a Salesforce customer instance.
   *
   * @return the string
   */
  public String token() {
    return token;
  }
}
