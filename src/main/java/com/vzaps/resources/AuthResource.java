package com.vzaps.resources;

import com.vzaps.http.VZapsHttpClient;

public final class AuthResource extends BaseResource {
  public AuthResource(VZapsHttpClient http) {
    super(http);
  }

  public String getAccessToken() {
    return http.getAccessToken();
  }
}
