package com.vzaps.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.vzaps.models.common.GenericInstanceRequest;
import com.vzaps.models.common.InstanceRequestOptions;

public final class UsersResource extends BaseResource {
  public UsersResource(com.vzaps.http.VZapsHttpClient http) {
    super(http);
  }

  public JsonNode info(GenericInstanceRequest request) {
    return post("/user/info", request);
  }

  public JsonNode check(GenericInstanceRequest request) {
    return post("/user/check", request);
  }

  public JsonNode avatar(GenericInstanceRequest request) {
    return post("/user/avatar", request);
  }

  public JsonNode contacts(String instanceId, InstanceRequestOptions options) {
    return send("GET", "/instances/" + esc(instanceId) + "/user/contacts", null, options);
  }

  private JsonNode post(String suffix, GenericInstanceRequest request) {
    return send("POST", "/instances/" + esc(request.instanceId()) + suffix, request);
  }
}
