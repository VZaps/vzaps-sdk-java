package com.vzaps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Raw HTTP response returned by {@code rawRequest}. */
public final class VZapsResponse {
  private final int statusCode;
  private final Map<String, List<String>> headers;
  private final String body;

  public VZapsResponse(int statusCode, Map<String, List<String>> headers, String body) {
    this.statusCode = statusCode;
    this.headers = Collections.unmodifiableMap(headers);
    this.body = body;
  }

  public int statusCode() {
    return statusCode;
  }

  public Map<String, List<String>> headers() {
    return headers;
  }

  public String body() {
    return body;
  }
}
