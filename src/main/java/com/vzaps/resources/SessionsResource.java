package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.VZapsRequestOptions;
import com.vzaps.models.common.InstanceRequestOptions;
import com.vzaps.models.sessions.SessionStatusResponse;

public final class SessionsResource extends BaseResource {
  public SessionsResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public SessionStatusResponse status(String instanceId, InstanceRequestOptions options) {
    return http.request(
        "GET",
        "/instances/" + esc(instanceId) + "/session/status",
        null,
        VZapsRequestOptions.builder()
            .instanceToken(options == null ? null : options.instanceToken())
            .build(),
        SessionStatusResponse.class);
  }

  public JsonNode qr(String instanceId, InstanceRequestOptions options) {
    return send("GET", "/instances/" + esc(instanceId) + "/session/qr", null, options);
  }

  public JsonNode pairCode(String instanceId, String phone, InstanceRequestOptions options) {
    return send(
        "GET", "/instances/" + esc(instanceId) + "/session/paircode/" + esc(phone), null, options);
  }

  public JsonNode disconnect(String instanceId, InstanceRequestOptions options) {
    return send("POST", "/instances/" + esc(instanceId) + "/session/disconnect", null, options);
  }
}
