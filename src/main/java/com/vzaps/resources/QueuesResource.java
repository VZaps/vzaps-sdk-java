package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;

public final class QueuesResource extends BaseResource {
  public QueuesResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode listMessages(GenericInstanceRequest request) {
    return send("GET", "/instances/" + esc(request.instanceId()) + "/queue/messages", request);
  }

  public JsonNode removeMessage(GenericInstanceRequest request, String messageId) {
    return send(
        "DELETE",
        "/instances/" + esc(request.instanceId()) + "/queue/messages/" + esc(messageId),
        request);
  }

  public JsonNode purgeMessages(GenericInstanceRequest request) {
    return send("DELETE", "/instances/" + esc(request.instanceId()) + "/queue/messages", request);
  }

  public JsonNode listOperations(GenericInstanceRequest request) {
    return send("GET", "/instances/" + esc(request.instanceId()) + "/queue/operations", request);
  }

  public JsonNode removeOperation(GenericInstanceRequest request, String messageId) {
    return send(
        "DELETE",
        "/instances/" + esc(request.instanceId()) + "/queue/operations/" + esc(messageId),
        request);
  }

  public JsonNode purgeOperations(GenericInstanceRequest request) {
    return send("DELETE", "/instances/" + esc(request.instanceId()) + "/queue/operations", request);
  }
}
