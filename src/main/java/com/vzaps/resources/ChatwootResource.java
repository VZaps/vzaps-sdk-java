package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;
import com.vzaps.models.common.InstanceRequestOptions;

public final class ChatwootResource extends BaseResource {
  public ChatwootResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode get(String instanceId, InstanceRequestOptions options) {
    return send("GET", "/instances/" + esc(instanceId) + "/chatwoot", null, options);
  }

  public JsonNode set(GenericInstanceRequest request) {
    return send("POST", "/instances/" + esc(request.instanceId()) + "/chatwoot", request);
  }

  public JsonNode delete(String instanceId, InstanceRequestOptions options) {
    return send("DELETE", "/instances/" + esc(instanceId) + "/chatwoot", null, options);
  }

  public JsonNode triggerImport(GenericInstanceRequest request, String what) {
    return send(
        "POST", "/instances/" + esc(request.instanceId()) + "/chatwoot/import/" + esc(what), request);
  }
}
