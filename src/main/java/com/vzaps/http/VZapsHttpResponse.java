package com.vzaps.http;

import com.vzaps.VZapsResponse;
import java.util.List;
import java.util.Map;

/** Backward-compatible alias for the raw SDK response type. */
public final class VZapsHttpResponse {
  private final VZapsResponse response;

  public VZapsHttpResponse(VZapsResponse response) {
    this.response = response;
  }

  public int statusCode() {
    return response.statusCode();
  }

  public Map<String, List<String>> headers() {
    return response.headers();
  }

  public String body() {
    return response.body();
  }
}
