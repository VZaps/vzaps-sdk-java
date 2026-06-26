package com.vzaps;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

/** Fluent builder for {@link VZapsClient}. */
public final class VZapsClientBuilder {
  private final VZapsClientOptions.Builder options = VZapsClientOptions.builder();

  VZapsClientBuilder() {}

  public VZapsClientBuilder clientToken(String clientToken) {
    options.clientToken(clientToken);
    return this;
  }

  public VZapsClientBuilder clientSecret(String clientSecret) {
    options.clientSecret(clientSecret);
    return this;
  }

  public VZapsClientBuilder baseUrl(String baseUrl) {
    options.baseUrl(baseUrl);
    return this;
  }

  public VZapsClientBuilder baseUrl(URI baseUrl) {
    options.baseUrl(baseUrl);
    return this;
  }

  public VZapsClientBuilder realtimeUrl(String realtimeUrl) {
    options.realtimeUrl(realtimeUrl);
    return this;
  }

  public VZapsClientBuilder realtimeUrl(URI realtimeUrl) {
    options.realtimeUrl(realtimeUrl);
    return this;
  }

  public VZapsClientBuilder connectTimeout(Duration timeout) {
    options.connectTimeout(timeout);
    return this;
  }

  public VZapsClientBuilder requestTimeout(Duration timeout) {
    options.requestTimeout(timeout);
    return this;
  }

  public VZapsClientBuilder tokenRefreshSkew(Duration skew) {
    options.tokenRefreshSkew(skew);
    return this;
  }

  public VZapsClientBuilder userAgent(String userAgent) {
    options.userAgent(userAgent);
    return this;
  }

  public VZapsClientBuilder httpClient(HttpClient httpClient) {
    options.httpClient(httpClient);
    return this;
  }

  public VZapsClient build() {
    return new VZapsClient(options.build());
  }

  public VZapsAsyncClient buildAsync() {
    return new VZapsAsyncClient(options.build());
  }
}
