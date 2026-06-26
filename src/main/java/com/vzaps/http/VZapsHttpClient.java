package com.vzaps.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.VZapsClientOptions;
import com.vzaps.VZapsRequestOptions;
import com.vzaps.VZapsResponse;
import com.vzaps.auth.TokenProvider;
import com.vzaps.exceptions.VZapsApiException;
import com.vzaps.exceptions.VZapsAuthenticationException;
import com.vzaps.exceptions.VZapsException;
import com.vzaps.exceptions.VZapsRateLimitException;
import com.vzaps.exceptions.VZapsTimeoutException;
import com.vzaps.json.VZapsJson;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

/** Internal HTTP transport built on Java 11 HttpClient. */
public final class VZapsHttpClient {
  private static final int MAX_ERROR_BODY_LENGTH = 4096;

  private final VZapsClientOptions options;
  private final HttpClient httpClient;
  private final TokenProvider tokenProvider;

  public VZapsHttpClient(VZapsClientOptions options) {
    this.options = options;
    this.httpClient =
        options.httpClient() == null
            ? HttpClient.newBuilder().connectTimeout(options.connectTimeout()).build()
            : options.httpClient();
    this.tokenProvider = new TokenProvider(this, options);
  }

  public HttpClient innerClient() {
    return httpClient;
  }

  public String getAccessToken() {
    return tokenProvider.getAccessToken();
  }

  public <T> T request(
      String method, String path, Object body, VZapsRequestOptions requestOptions, Class<T> type) {
    VZapsRequestOptions effective =
        requestOptions == null ? VZapsRequestOptions.defaults() : requestOptions;
    HttpResponse<String> response = send(method, path, body, effective, false);
    if (response.statusCode() == 401 && effective.authenticate()) {
      response = send(method, path, body, effective, true);
    }
    return readResponse(response, type);
  }

  public <T> T requestWithoutAuth(
      String method, String path, Object body, VZapsRequestOptions requestOptions, Class<T> type) {
    return readResponse(send(method, path, body, requestOptions, false), type);
  }

  public VZapsResponse rawRequest(
      String method, String path, Object body, VZapsRequestOptions requestOptions) {
    VZapsRequestOptions effective =
        requestOptions == null ? VZapsRequestOptions.defaults() : requestOptions;
    HttpResponse<String> response = send(method, path, body, effective, false);
    if (response.statusCode() == 401 && effective.authenticate()) {
      response = send(method, path, body, effective, true);
    }
    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw createApiException(response);
    }
    return new VZapsResponse(response.statusCode(), response.headers().map(), response.body());
  }

  private HttpResponse<String> send(
      String method,
      String path,
      Object body,
      VZapsRequestOptions requestOptions,
      boolean refreshed) {
    HttpRequest.Builder builder =
        HttpRequest.newBuilder(buildUri(path, requestOptions.query()))
            .timeout(options.requestTimeout())
            .header("Accept", "application/json")
            .header("User-Agent", options.userAgent());

    if (requestOptions.authenticate()) {
      String accessToken =
          refreshed ? tokenProvider.forceRefresh() : tokenProvider.getAccessToken();
      builder.header("Authorization", "Bearer " + accessToken);
      builder.header("X-Client-Token", options.clientToken());
    }

    if (requestOptions.instanceToken() != null
        && !requestOptions.instanceToken().trim().isEmpty()) {
      builder.header("X-Instance-Token", requestOptions.instanceToken());
    }

    requestOptions.headers().forEach(builder::header);

    String normalizedMethod = method.toUpperCase(Locale.ROOT);
    if (body == null) {
      builder.method(normalizedMethod, HttpRequest.BodyPublishers.noBody());
    } else {
      builder.header("Content-Type", "application/json");
      builder.method(normalizedMethod, HttpRequest.BodyPublishers.ofString(VZapsJson.write(body)));
    }

    try {
      return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    } catch (java.net.http.HttpTimeoutException ex) {
      throw new VZapsTimeoutException("The VZaps request timed out.", ex);
    } catch (IOException ex) {
      throw new VZapsException("VZaps request failed before receiving a response.", ex);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new VZapsException("VZaps request was interrupted.", ex);
    }
  }

  private <T> T readResponse(HttpResponse<String> response, Class<T> type) {
    if (response.statusCode() < 200 || response.statusCode() >= 300) {
      throw createApiException(response);
    }
    String body = response.body();
    if (type == Void.class) {
      return null;
    }
    if (type == String.class) {
      return type.cast(body);
    }
    if (body == null || body.trim().isEmpty()) {
      return null;
    }
    if (type == JsonNode.class) {
      return type.cast(VZapsJson.readTree(body));
    }
    return VZapsJson.read(body, type);
  }

  private VZapsApiException createApiException(HttpResponse<String> response) {
    String body = truncate(response.body());
    ErrorShape error = readError(body);
    String requestId = firstHeader(response, "X-Request-Id");
    String message =
        error.message == null || error.message.trim().isEmpty()
            ? "VZaps request failed with HTTP " + response.statusCode() + "."
            : error.message;

    if (response.statusCode() == 401 || response.statusCode() == 403) {
      return new VZapsAuthenticationException(
          message, response.statusCode(), error.code, error.details, requestId, body);
    }
    if (response.statusCode() == 429) {
      return new VZapsRateLimitException(
          message, response.statusCode(), error.code, error.details, requestId, body);
    }
    return new VZapsApiException(
        message, response.statusCode(), error.code, error.details, requestId, body);
  }

  private URI buildUri(String path, Map<String, Object> query) {
    String cleanPath = path.startsWith("/") ? path.substring(1) : path;
    URI uri = options.baseUrl().resolve(cleanPath);
    if (query == null || query.isEmpty()) {
      return uri;
    }

    List<String> parts = new ArrayList<>();
    String existingQuery = uri.getRawQuery();
    if (existingQuery != null && !existingQuery.isEmpty()) {
      parts.add(existingQuery);
    }
    query.forEach(
        (key, value) -> {
          if (value != null) {
            parts.add(encode(snakeCase(key)) + "=" + encode(String.valueOf(value)));
          }
        });

    StringJoiner joiner = new StringJoiner("&");
    parts.forEach(joiner::add);
    return URI.create(uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?" + joiner);
  }

  private static String snakeCase(String value) {
    StringBuilder result = new StringBuilder(value.length() + 8);
    for (int i = 0; i < value.length(); i++) {
      char current = value.charAt(i);
      if (Character.isUpperCase(current)) {
        if (i > 0) {
          result.append('_');
        }
        result.append(Character.toLowerCase(current));
      } else {
        result.append(current);
      }
    }
    return result.toString();
  }

  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  private static String firstHeader(HttpResponse<String> response, String name) {
    Optional<String> value = response.headers().firstValue(name);
    return value.orElse(null);
  }

  private static String truncate(String body) {
    if (body == null || body.isEmpty()) {
      return null;
    }
    return body.length() <= MAX_ERROR_BODY_LENGTH ? body : body.substring(0, MAX_ERROR_BODY_LENGTH);
  }

  private static ErrorShape readError(String body) {
    if (body == null || body.trim().isEmpty()) {
      return new ErrorShape(null, null, null);
    }
    try {
      JsonNode root = VZapsJson.readTree(body);
      String message = text(root, "message");
      if (message == null) {
        message = text(root, "error");
      }
      String code = text(root, "code");
      if (code == null) {
        code = text(root, "error_code");
      }
      return new ErrorShape(message, code, text(root, "details"));
    } catch (RuntimeException ex) {
      return new ErrorShape(body, null, null);
    }
  }

  private static String text(JsonNode node, String name) {
    JsonNode value = node == null || !node.isObject() ? null : node.get(name);
    return value == null || value.isNull() ? null : value.asText();
  }

  private static final class ErrorShape {
    private final String message;
    private final String code;
    private final String details;

    private ErrorShape(String message, String code, String details) {
      this.message = message;
      this.code = code;
      this.details = details;
    }
  }
}
