package com.vzaps;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;

/** Immutable client-wide configuration for the VZaps SDK. */
public final class VZapsClientOptions {
  public static final URI DEFAULT_BASE_URL = URI.create("https://api.vzaps.com");
  public static final URI DEFAULT_REALTIME_URL = URI.create("wss://realtime.vzaps.com");

  private final String clientToken;
  private final String clientSecret;
  private final URI baseUrl;
  private final URI realtimeUrl;
  private final Duration connectTimeout;
  private final Duration requestTimeout;
  private final Duration tokenRefreshSkew;
  private final String userAgent;
  private final HttpClient httpClient;

  private VZapsClientOptions(Builder builder) {
    this.clientToken = requireText(builder.clientToken, "clientToken");
    this.clientSecret = requireText(builder.clientSecret, "clientSecret");
    this.baseUrl = normalizeHttpUrl(builder.baseUrl == null ? DEFAULT_BASE_URL : builder.baseUrl);
    this.realtimeUrl = builder.realtimeUrl == null ? DEFAULT_REALTIME_URL : builder.realtimeUrl;
    this.connectTimeout =
        builder.connectTimeout == null ? Duration.ofSeconds(10) : builder.connectTimeout;
    this.requestTimeout =
        builder.requestTimeout == null ? Duration.ofSeconds(30) : builder.requestTimeout;
    this.tokenRefreshSkew =
        builder.tokenRefreshSkew == null ? Duration.ofMinutes(1) : builder.tokenRefreshSkew;
    this.userAgent =
        builder.userAgent == null || builder.userAgent.trim().isEmpty()
            ? "VZaps.SDK.Java/" + VZapsVersion.VERSION
            : builder.userAgent;
    this.httpClient = builder.httpClient;

    if (connectTimeout.isNegative() || connectTimeout.isZero()) {
      throw new IllegalArgumentException("connectTimeout must be positive.");
    }
    if (requestTimeout.isNegative() || requestTimeout.isZero()) {
      throw new IllegalArgumentException("requestTimeout must be positive.");
    }
    if (tokenRefreshSkew.isNegative()) {
      throw new IllegalArgumentException("tokenRefreshSkew must not be negative.");
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public String clientToken() {
    return clientToken;
  }

  public String clientSecret() {
    return clientSecret;
  }

  public URI baseUrl() {
    return baseUrl;
  }

  public URI realtimeUrl() {
    return realtimeUrl;
  }

  public Duration connectTimeout() {
    return connectTimeout;
  }

  public Duration requestTimeout() {
    return requestTimeout;
  }

  public Duration tokenRefreshSkew() {
    return tokenRefreshSkew;
  }

  public String userAgent() {
    return userAgent;
  }

  public HttpClient httpClient() {
    return httpClient;
  }

  private static String requireText(String value, String name) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException(name + " is required.");
    }
    return value;
  }

  private static URI normalizeHttpUrl(URI uri) {
    Objects.requireNonNull(uri, "baseUrl");
    String value = uri.toString();
    return value.endsWith("/") ? uri : URI.create(value + "/");
  }

  public static final class Builder {
    private String clientToken;
    private String clientSecret;
    private URI baseUrl;
    private URI realtimeUrl;
    private Duration connectTimeout;
    private Duration requestTimeout;
    private Duration tokenRefreshSkew;
    private String userAgent;
    private HttpClient httpClient;

    public Builder clientToken(String clientToken) {
      this.clientToken = clientToken;
      return this;
    }

    public Builder clientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
      return this;
    }

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = URI.create(baseUrl);
      return this;
    }

    public Builder baseUrl(URI baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    public Builder realtimeUrl(String realtimeUrl) {
      this.realtimeUrl = URI.create(realtimeUrl);
      return this;
    }

    public Builder realtimeUrl(URI realtimeUrl) {
      this.realtimeUrl = realtimeUrl;
      return this;
    }

    public Builder connectTimeout(Duration connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    public Builder requestTimeout(Duration requestTimeout) {
      this.requestTimeout = requestTimeout;
      return this;
    }

    public Builder tokenRefreshSkew(Duration tokenRefreshSkew) {
      this.tokenRefreshSkew = tokenRefreshSkew;
      return this;
    }

    public Builder userAgent(String userAgent) {
      this.userAgent = userAgent;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public VZapsClientOptions build() {
      return new VZapsClientOptions(this);
    }
  }
}
