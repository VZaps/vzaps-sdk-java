package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;
import com.vzaps.models.common.InstanceRequestOptions;

public final class TypeBotsResource extends BaseResource {
  public TypeBotsResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode list(String instanceId, InstanceRequestOptions options) {
    return send("GET", "/instances/" + esc(instanceId) + "/typebots", null, options);
  }

  public JsonNode create(GenericInstanceRequest request) {
    return send("POST", "/instances/" + esc(request.instanceId()) + "/typebots", request);
  }

  public JsonNode update(GenericInstanceRequest request, String typebotId) {
    return send(
        "PATCH", "/instances/" + esc(request.instanceId()) + "/typebots/" + esc(typebotId), request);
  }

  public JsonNode delete(GenericInstanceRequest request, String typebotId) {
    return send(
        "DELETE", "/instances/" + esc(request.instanceId()) + "/typebots/" + esc(typebotId), request);
  }

  public JsonNode startSession(GenericInstanceRequest request) {
    return send(
        "POST", "/instances/" + esc(request.instanceId()) + "/typebots/sessions/start", request);
  }

  public JsonNode startSession(GenericInstanceRequest request, String typebotId) {
    return send(
        "POST",
        "/instances/"
            + esc(request.instanceId())
            + "/typebots/"
            + esc(typebotId)
            + "/sessions/start",
        request);
  }

  public JsonNode listSessions(String instanceId, InstanceRequestOptions options) {
    return send("GET", "/instances/" + esc(instanceId) + "/typebots/sessions", null, options);
  }

  public JsonNode pauseSession(GenericInstanceRequest request, String session) {
    return send(
        "POST",
        "/instances/" + esc(request.instanceId()) + "/typebots/sessions/" + esc(session) + "/pause",
        request);
  }

  public JsonNode closeSession(GenericInstanceRequest request, String session) {
    return send(
        "POST",
        "/instances/" + esc(request.instanceId()) + "/typebots/sessions/" + esc(session) + "/close",
        request);
  }
}
