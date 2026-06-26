package com.vzaps.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.VZapsClientOptions;
import com.vzaps.VZapsRequestOptions;
import com.vzaps.exceptions.VZapsAuthenticationException;
import com.vzaps.http.VZapsHttpClient;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/** Thread-safe in-memory JWT cache. */
public final class TokenProvider {
  private final VZapsHttpClient http;
  private final VZapsClientOptions options;
  private final Object lock = new Object();
  private volatile String accessToken;
  private volatile Instant expiresAt = Instant.EPOCH;

  public TokenProvider(VZapsHttpClient http, VZapsClientOptions options) {
    this.http = http;
    this.options = options;
  }

  public String getAccessToken() {
    if (hasValidToken()) {
      return accessToken;
    }
    return refresh(false);
  }

  public String forceRefresh() {
    return refresh(true);
  }

  private boolean hasValidToken() {
    return accessToken != null && Instant.now().isBefore(expiresAt);
  }

  private String refresh(boolean force) {
    synchronized (lock) {
      if (!force && hasValidToken()) {
        return accessToken;
      }

      Map<String, Object> body = new LinkedHashMap<>();
      body.put("client_token", options.clientToken());
      body.put("client_secret", options.clientSecret());

      JsonNode token =
          http.requestWithoutAuth(
              "POST",
              "/token",
              body,
              VZapsRequestOptions.builder().authenticate(false).build(),
              JsonNode.class);

      String value = text(token, "accessToken", "access_token");
      long expiresIn = number(token, "expiresIn", "expires_in");
      if (value == null || value.trim().isEmpty() || expiresIn <= 0) {
        throw new VZapsAuthenticationException(
            "VZaps token response is missing accessToken or expiresIn.",
            401,
            null,
            null,
            null,
            null);
      }

      accessToken = value;
      expiresAt = Instant.now().plusSeconds(expiresIn).minus(options.tokenRefreshSkew());
      return accessToken;
    }
  }

  private static String text(JsonNode node, String primary, String fallback) {
    if (node == null || !node.isObject()) {
      return null;
    }
    JsonNode value = node.get(primary);
    if (value == null) {
      value = node.get(fallback);
    }
    return value == null || value.isNull() ? null : value.asText();
  }

  private static long number(JsonNode node, String primary, String fallback) {
    if (node == null || !node.isObject()) {
      return 0;
    }
    JsonNode value = node.get(primary);
    if (value == null) {
      value = node.get(fallback);
    }
    return value == null || !value.isNumber() ? 0 : value.asLong();
  }
}
