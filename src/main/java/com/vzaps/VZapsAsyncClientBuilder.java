package com.vzaps;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executor;

/** Fluent builder for {@link VZapsAsyncClient}. */
public final class VZapsAsyncClientBuilder {
  private final VZapsClientOptions.Builder options = VZapsClientOptions.builder();
  private Executor executor;

  VZapsAsyncClientBuilder() {}

  public VZapsAsyncClientBuilder clientToken(String clientToken) {
    options.clientToken(clientToken);
    return this;
  }

  public VZapsAsyncClientBuilder clientSecret(String clientSecret) {
    options.clientSecret(clientSecret);
    return this;
  }

  public VZapsAsyncClientBuilder baseUrl(String baseUrl) {
    options.baseUrl(baseUrl);
    return this;
  }

  public VZapsAsyncClientBuilder baseUrl(URI baseUrl) {
    options.baseUrl(baseUrl);
    return this;
  }

  public VZapsAsyncClientBuilder realtimeUrl(String realtimeUrl) {
    options.realtimeUrl(realtimeUrl);
    return this;
  }

  public VZapsAsyncClientBuilder realtimeUrl(URI realtimeUrl) {
    options.realtimeUrl(realtimeUrl);
    return this;
  }

  public VZapsAsyncClientBuilder connectTimeout(Duration timeout) {
    options.connectTimeout(timeout);
    return this;
  }

  public VZapsAsyncClientBuilder requestTimeout(Duration timeout) {
    options.requestTimeout(timeout);
    return this;
  }

  public VZapsAsyncClientBuilder tokenRefreshSkew(Duration skew) {
    options.tokenRefreshSkew(skew);
    return this;
  }

  public VZapsAsyncClientBuilder userAgent(String userAgent) {
    options.userAgent(userAgent);
    return this;
  }

  public VZapsAsyncClientBuilder httpClient(HttpClient httpClient) {
    options.httpClient(httpClient);
    return this;
  }

  public VZapsAsyncClientBuilder executor(Executor executor) {
    this.executor = executor;
    return this;
  }

  public VZapsAsyncClient build() {
    return new VZapsAsyncClient(options.build(), executor);
  }
}
