package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;
import com.vzaps.models.common.InstanceRequestOptions;

public final class ContactsResource extends BaseResource {
  public ContactsResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode list(String instanceId, InstanceRequestOptions options) {
    return send("GET", "/instances/" + esc(instanceId) + "/contact/list", null, options);
  }

  public JsonNode add(GenericInstanceRequest request) {
    return send("POST", "/instances/" + esc(request.instanceId()) + "/contact/add", request);
  }
}
