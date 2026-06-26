package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;
import com.vzaps.models.common.InstanceRequestOptions;

public final class WebhooksResource extends BaseResource {
  public WebhooksResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode get(String instanceId, InstanceRequestOptions options) {
    return send("GET", "/instances/" + esc(instanceId) + "/webhook", null, options);
  }

  public JsonNode set(GenericInstanceRequest request) {
    return send("POST", "/instances/" + esc(request.instanceId()) + "/webhook", request);
  }

  public JsonNode searchLogs(GenericInstanceRequest request) {
    return send("POST", "/instances/" + esc(request.instanceId()) + "/webhook/logs/search", request);
  }

  public JsonNode getLog(GenericInstanceRequest request, String logId) {
    return send(
        "GET",
        "/instances/" + esc(request.instanceId()) + "/webhook/logs/" + esc(logId),
        null,
        request.instanceToken());
  }

  public JsonNode retryLog(GenericInstanceRequest request, String logId) {
    return send(
        "POST",
        "/instances/" + esc(request.instanceId()) + "/webhook/logs/" + esc(logId) + "/retry",
        null,
        request.instanceToken());
  }
}
